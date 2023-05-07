package ru.smartjava.server.server;

import ru.smartjava.server.converter.Converter;
import ru.smartjava.server.logger.ServerFacadeLog;
import ru.smartjava.server.messages.Message;
import ru.smartjava.server.messages.MessageBroker;
import ru.smartjava.server.messages.MessageHandler;
import ru.smartjava.server.messages.MessageMaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

public class ServerClientHandler implements Runnable {
    Logger logger = ServerFacadeLog.getLogger();
    private final Integer SOCKET_TIMEOUT = 300;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private final Socket clientSocket;
    private final Converter converter = Converter.getConverter();
    private final MessageMaker messageMaker = MessageMaker.getMessageMaker();
    private final MessageHandler messageHandler;
    private final MessageBroker messageBroker;
    private final BlockingDeque<Message> toClientQueue = new LinkedBlockingDeque<>();
    private final BlockingDeque<Message> fromClientQueue = new LinkedBlockingDeque<>();
    private final Boolean reject;

    public ServerClientHandler(Socket clientSocket, MessageBroker messageBroker, Boolean reject) {
        this.messageBroker = messageBroker;
        this.messageHandler = new MessageHandler(messageBroker);
        this.clientSocket = clientSocket;
        this.reject = reject;
    }

    @Override
    public void run() {

        try {
            clientSocket.setSoTimeout(SOCKET_TIMEOUT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (SocketException socketException) {
            logger.severe("Ошибка подключения " + socketException.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info(String.format("Открываем поток обработки подключение от %s:%s", clientSocket.getInetAddress(), clientSocket.getPort()));
        try {
            Message initialMessage = converter.jsonToMessage(in.readLine());
            logger.info(messageMaker.logIncomingMessage(initialMessage));
            Message response;
            if (reject) {
                response = messageMaker.overSubscribe();
            } else {
                response = messageHandler.handle(initialMessage);
                clientName = response.getNickName();
            }
            messageMaker.logOutgoingMessage(response, clientName);
            if (!response.getDisconnect()) {
                messageBroker.addClientQueues(initialMessage.getNickName(), fromClientQueue, toClientQueue);
                messageBroker.sendEveryOneExceptSender(messageMaker.newClientMessage(clientName));
                toClientQueue.addFirst(response);
                boolean disconnect = response.getDisconnect();
                while (!disconnect) {
                    //Отправка
                    try {
                        toClientQueue.getLast();
                        Message send = toClientQueue.removeLast();
                        logger.info(messageMaker.logOutgoingMessage(send, clientName));
                        out.println(converter.messageToJson(send));
                        if (messageHandler.isClientExit(send)) {
                            disconnect = true;
                        }
                    } catch (NoSuchElementException nsl) {
//                        System.out.println("Нет сообщений для отправки " + nsl.getMessage());
                    }
                    //Получение
                    if (in.ready()) {
                        String result = in.readLine();
                        if (Objects.equals(result, "null") || result == null) {
                            throw new SocketException("соединение разорвано");
                        } else {
                            Message incoming = converter.jsonToMessage(result);
                            messageMaker.logOutgoingMessage(incoming, clientName);
                            if (incoming.isCommand() || !Objects.equals(incoming.getMessage(), "")) {
                                fromClientQueue.addFirst(incoming);
                            }

                        }
                    }
                }
                logger.info("Завершаем поток обработки клиента " + clientName);
            } else {
                out.println(converter.messageToJson(response));
            }

        } catch (SocketException ex) {
            logger.severe("Client " + clientName + " " + ex.getMessage());
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            messageBroker.removeClientQueues(clientName);
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
