package ru.smartjava.server.messages;

import ru.smartjava.interfaces.Broker;
import ru.smartjava.interfaces.Handler;
import ru.smartjava.interfaces.Maker;
import ru.smartjava.params.Cmd;

public class MessageHandler implements Handler {

    private final Broker messageBroker;

    private final Maker messageMaker = MessageMaker.getMessageMaker();

    public MessageHandler(Broker messageBroker) {
        this.messageBroker = messageBroker;
    }

//    public boolean isClientExit(Message command) {
//        return Objects.equals(command.getCommand(), Cmd.EXIT);
//    }

    public Message handle(Message command) {
        Message response;
        System.out.println(command);
//        System.out.println(Commands.valueOf(command.getCommand()));

//        switch (Commands.valueOf(command.getCommand())) {
        switch (command.getCommand()) {
            case Cmd.CONNECT:
                if (messageBroker.isNickNameUnique(command.getNickName())) {
                    response = messageMaker.connectAccepted(command.getNickName());
                } else {
                    response = messageMaker.connectRejected(command.getNickName());
                }
                break;
            case Cmd.EXIT:
                response = messageMaker.disconnect(command.getNickName(), Cmd.EXIT);
                break;
            case Cmd.INFO:
                response = command;
                break;
            default:
                response = messageMaker.wrongMessage(command.getCommand());
        }
        return response;
    }
}
