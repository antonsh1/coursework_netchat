package ru.smartjava.server.messages;

import ru.smartjava.interfaces.Maker;
import ru.smartjava.params.Cmd;
import ru.smartjava.params.Defaults;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MessageMaker implements Maker {

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

    final String NEW_CLIENT = "К чату подключился новый участник: ";
    final String OVER_SUBSCRIBE = "Сервер перегружен, подключитесь позже";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public boolean isExitCommand(Message command) {
        return Objects.equals(command.getCommand(), Cmd.EXIT);
    }
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

    public String connectAcceptedMessage(String nickname) {
        return "Добро пожаловать " + nickname + "!\nДля отправки сообщения нажмите Enter.";
    }

    public String connectRejectedMessage(String nickname) {
        return "Такое имя " + nickname + " уже занято, выберите другое!";
    }

    public String exitMessage(String nickname) {
        return "из чата выходит " + nickname + ".";
    }

    public String disconnectMessage(String nickname) {
        return "До скорой встречи " + nickname + "!";
    }

    public String errorMessageString(String command) {
        return "Неизвестная команда " + command + "!";
    }

    public Message exitClientMessage(String nickName) {
        return new Message(true, Cmd.EXIT, exitMessage(nickName), new Date(), Defaults.SERVER_NAME, false);
    }

    public Message newClientMessage(String nickName) {
        return new Message(true, Cmd.INFO, NEW_CLIENT + nickName, new Date(), nickName, false);
    }

    public Message message(String nickName, String message) {
        return new Message(false, Cmd.EMPTY, message, new Date(), nickName, false);
    }

    public Message connect(String nickName) {
        return new Message(true, Cmd.CONNECT, Defaults.EMPTY_STRING, new Date(), nickName, false);
    }

    public Message clientCommand(String nickName, String userCommand) {
        return new Message(true, userCommand, Defaults.EMPTY_STRING, new Date(), nickName, false);
    }

    public Message connectAccepted(String nickName) {
        Message message = new Message(true, Cmd.CONNECT, connectAcceptedMessage(nickName), new Date(), nickName, false);
        System.out.println(message);
        return message;
    }

    public Message disconnect(String nickName, String command) {
        return new Message(true, command, disconnectMessage(nickName), new Date(), Defaults.SERVER_NAME, true);
    }

    public Message connectRejected(String nickName) {
        return new Message(true, Cmd.CONNECT, connectRejectedMessage(nickName), new Date(), nickName, true);
    }

    public Message wrongMessage(String command) {
        return new Message(true, Cmd.EXIT, errorMessageString(command), new Date(), Defaults.SERVER_NAME, false);
    }

    public Message overSubscribe() {
        return new Message(true, Cmd.CONNECT, OVER_SUBSCRIBE, new Date(), Defaults.SERVER_NAME, true);
    }


}
