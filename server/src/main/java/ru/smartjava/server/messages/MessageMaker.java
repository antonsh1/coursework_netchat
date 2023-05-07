package ru.smartjava.server.messages;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageMaker {

    private static MessageMaker messageMaker = null;

    private MessageMaker() {
    }

    public static MessageMaker getMessageMaker() {
        if (messageMaker == null) {
            synchronized (MessageMaker.class) {
                if (messageMaker == null) {
                    messageMaker = new MessageMaker();
                }
            }
        }
        return messageMaker;
    }

    final String SERVER_NAME = "Server";
    final String CONNECT_CMD = "connect";
    final String INFO_CMD = "info";
    final String EMPTY_STRING = "";
    final String NEW_CLIENT = "К чату подключился новый участник: ";
    final String OVER_SUBSCRIBE = "Сервер перегружен";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public void printMessage(Message message) {
        System.out.println(simpleDateFormat.format(message.getDate()) + " " + message.getNickName() + " : " + message.getMessage());
    }

    public String logIncomingMessage(Message message) {
        if (message.isCommand()) {
            return "Получили команду от " + message.getNickName() + " : " + message.getCommand() + " (\"" + message.getMessage() + "\")";
        } else {
            return "Получили сообщение от " + message.getNickName() + " : " + " \"" + message.getMessage() + "\"";
        }
    }

    public String logOutgoingMessage(Message message, String destination) {
        if (message.isCommand()) {
            return "Отправляем " + destination + " команду " + message.getCommand() + " (\"" + message.getMessage() + "\")";
        } else {
            return "Отправляем " + destination + " сообщение от " + message.getNickName() + " : " + " \"" + message.getMessage() + "\")";
        }
    }

    public void printCommand(Message message) {
        System.out.println(message.getMessage());
    }

    private String connectAcceptedMessage(String nickname) {
        return "Добро пожаловать " + nickname + "!\nДля отправки сообщения нажмите Enter.";
    }

    private String connectRejectedMessage(String nickname) {
        return "Такое имя " + nickname + " уже занято, выберите другое!";
    }

    private String exitMessage(String nickname) {
        return "из чата выходит " + nickname + ".";
    }

    private String disconnectMessage(String nickname) {
        return "До скорой встречи " + nickname + "!";
    }

    private String errorMessageString(String command) {
        return "Неизвестная команда " + command + "!";
    }

    public Message exitClientMessage(String nickName) {
        return new Message(true, INFO_CMD, exitMessage(nickName), new Date(), SERVER_NAME, false);
    }

    public Message newClientMessage(String nickName) {
        return new Message(true, INFO_CMD, NEW_CLIENT + nickName, new Date(), nickName, false);
    }

    public Message message(String nickName, String message) {
        return new Message(false, EMPTY_STRING, message, new Date(), nickName, false);
    }

    public Message connect(String nickName) {
        return new Message(true, CONNECT_CMD, EMPTY_STRING, new Date(), nickName, false);
    }

    public Message clientCommand(String nickName, String command) {
        return new Message(true, command, EMPTY_STRING, new Date(), nickName, false);
    }

    public Message connectAccepted(String nickName) {
        return new Message(true, CONNECT_CMD, connectAcceptedMessage(nickName), new Date(), nickName, false);
    }

    public Message disconnect(String nickName, String command) {
        return new Message(true, command, disconnectMessage(nickName), new Date(), SERVER_NAME, true);
    }

    public Message connectRejected(String nickName) {
        return new Message(true, CONNECT_CMD, connectRejectedMessage(nickName), new Date(), nickName, true);
    }

    public Message wrongMessage(String command) {
        return new Message(true, EMPTY_STRING, errorMessageString(command), new Date(), SERVER_NAME, false);
    }

    public Message overSubscribe() {
        return new Message(true, CONNECT_CMD, OVER_SUBSCRIBE, new Date(), SERVER_NAME, true);
    }

}
