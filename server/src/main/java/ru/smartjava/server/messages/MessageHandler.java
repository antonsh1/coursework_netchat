package ru.smartjava.server.messages;

import ru.smartjava.enums.Commands;

import java.util.Objects;

public class MessageHandler {

    private final MessageBroker messageBroker;

    private final MessageMaker messageMaker = MessageMaker.getMessageMaker();

    public MessageHandler(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    final String CONNECT_MSG = "connect";
    final String EXIT_CMD = "exit";
    final String INFO_CMD = "info";

    public Boolean isClientExit(Message command) {
        return Objects.equals(command.getCommand(), Commands.EXIT.toString());
    }

    public Message handle(Message command) {
        Message response;
        System.out.println(command);
        System.out.println(Commands.valueOf(command.getCommand()));

//        switch (Commands.valueOf(command.getCommand())) {
        switch (command.getCommand()) {
            case Commands.CONNECT:
                if (messageBroker.isNickNameUnique(command.getNickName())) {
                    response = messageMaker.connectAccepted(command.getNickName());
                } else {
                    response = messageMaker.connectRejected(command.getNickName());
                }
                break;
            case EXIT:
                response = messageMaker.disconnect(command.getNickName(), Commands.EXIT.toString());
                break;
            case INFO:
                response = command;
                break;
            default:
                response = messageMaker.wrongMessage(command.getCommand());
        }
        return response;
    }
}
