package ru.smartjava.server.messages;

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
        return Objects.equals(command.getCommand(), EXIT_CMD);
    }

    public Message handle(Message command) {
        Message response;
        switch (command.getCommand()) {
            case CONNECT_MSG:
                if (messageBroker.isNickNameUnique(command.getNickName())) {
                    response = messageMaker.connectAccepted(command.getNickName());
                } else {
                    response = messageMaker.connectRejected(command.getNickName());
                }
                break;
            case EXIT_CMD:
                response = messageMaker.disconnect(command.getNickName(), EXIT_CMD);
                break;
            case INFO_CMD:
                response = command;
                break;
            default:
                response = messageMaker.wrongMessage(command.getCommand());
        }
        return response;
    }
}
