package ru.smartjava.client;

import ru.smartjava.interfaces.Maker;
import ru.smartjava.params.Defaults;
import ru.smartjava.server.config.ReadClientConfigFile;
import ru.smartjava.server.converter.Converter;
import ru.smartjava.server.logger.ClientFacadeLog;
import ru.smartjava.server.messages.Message;
import ru.smartjava.server.messages.MessageMaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Client {

    Logger logger;
    ReadClientConfigFile configFile;
    private final Maker messageMaker = MessageMaker.getMessageMaker();
    private final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    private AtomicBoolean connect = new AtomicBoolean(true);
    private AtomicBoolean stop = new AtomicBoolean(false);
    private final Converter converter = Converter.getConverter();
    private final Runnable incomingThread = this::inputMessagesHandle;
    private final Runnable outgoingThread = this::outputMessagesHandle;
    private BufferedReader in;
    private PrintWriter out;
    private String userName = "";
    private Socket clientSocket;

    void outputMessagesHandle() {
        while (connect.get()) {
            try {
                String inputLine = console.readLine();
                if (Objects.equals(inputLine, "")) {
                    if (connect.get()) {
                        stop = new AtomicBoolean(true);
                        System.out.print("Введите Сообщение>");
                        inputLine = console.readLine();
                        stop = new AtomicBoolean(false);
                    }
                    if (inputLine.startsWith("/")) {
//                        System.out.println("Команда " + inputLine.substring(1));
                        out.println(converter.messageToJson(messageMaker.clientCommand(userName, inputLine.substring(1))));
                    } else {
                        if (!inputLine.equals("")) {
                            out.println(converter.messageToJson(messageMaker.message(userName, inputLine)));
                            System.out.println("Сообщение отправлено");
                        }
//                        out.println(converter.messageToJson(messageMaker.message(userName, inputLine)));

                    }
                }
            } catch (IOException e) {
                connect = new AtomicBoolean(false);
                throw new RuntimeException(e);
            }
        }
//        System.out.println("Exit outgoing");
    }

    void inputMessagesHandle() {
        while (connect.get()) {
            try {
                Thread.sleep(Defaults.CLIENT_SLEEP_TIMEOUT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            System.out.println("Ждем входящих сообщений...");
            try {
                if (!stop.get() && in.ready()) {
                    String remoteInputLine = in.readLine();
//                    System.out.println(remoteInputLine);
                    Message message = converter.jsonToMessage(remoteInputLine);
                    logger.info(messageMaker.logIncomingMessage(message));
//                    System.out.println(message);
                    if (message.isCommand()) {
//                        System.out.println(message.getMessage());
                        connect = new AtomicBoolean(!message.getDisconnect());
//                        System.out.println(connect);
                        messageMaker.printCommand(message);
                    } else {
                        messageMaker.printMessage(message);
                    }
                }
            } catch (IOException e) {
                connect = new AtomicBoolean(false);
                throw new RuntimeException(e);
            }
        }
//        System.out.println("Exit incoming");
    }

    public Client(String name) {
        this.logger = ClientFacadeLog.getLogger(name);
        this.configFile = new ReadClientConfigFile(name);
    }

    public void start() {

        logger.info("Запуск клиента");
        int clientPort = configFile.port();
        if (clientPort == 0) {
            clientPort = Defaults.CLIENT_DEFAULT_PORT;
            System.out.println("используем порт по умолчанию :" + clientPort);
            logger.info("используем порт по умолчанию :" + clientPort);
        } else {
            System.out.println("используем порт из файла конфигурации: " + clientPort);
            logger.info("используем порт из файла конфигурации: " + clientPort);
        }
        String clientHost = configFile.host();
        if (Objects.equals(clientHost, "")) {
            clientHost = Defaults.CLIENT_DEFAULT_HOST;
            System.out.println("используем имя подключения по умолчанию :" + clientHost);
            logger.info("используем имя подключения по умолчанию :" + clientHost);
        } else {
            System.out.println("используем имя подключения из файла конфигурации: " + clientHost);
            logger.info("используем имя подключения из файла конфигурации: " + clientHost);
        }
        String hostIp;
        //Получаем ip хоста
        try {
            InetAddress inetAddress = InetAddress.getByName(clientHost);
            hostIp = inetAddress.getHostAddress();
//            System.out.println();
            logger.info(clientHost + ", ip address: " + inetAddress.getHostAddress());
//            System.out.println();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        while (Objects.equals(userName, "")) {
            System.out.println("Ваше имя для подключения к серверу");
            try {
                userName = console.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            clientSocket = new Socket(hostIp, clientPort);
            clientSocket.setSoTimeout(Defaults.CLIENT_SOCKET_TIMEOUT);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (SocketException socketException) {
            logger.severe("Ошибка подключения к серверу");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Message initialMessage = messageMaker.connect(userName);
        logger.info(messageMaker.logOutgoingMessage(initialMessage, userName));
        out.println(converter.messageToJson(initialMessage));
        Thread incoming = new Thread(incomingThread);
        Thread outgoing = new Thread(outgoingThread);
        incoming.start();
        outgoing.start();
        try {
            incoming.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Для выхода нажмите Enter");
    }
}
