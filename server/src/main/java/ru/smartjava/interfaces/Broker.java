package ru.smartjava.interfaces;

import ru.smartjava.server.messages.Message;

import java.util.concurrent.BlockingDeque;

public interface Broker {

    void addClientQueues(String clientName, BlockingDeque<Message> fromClientBlockingQueue, BlockingDeque<Message> toClientBlockingQueue);
    void removeClientQueues(String clientName);
    boolean isNickNameUnique(String nickName);

    void sendEveryOneExceptSender(Message message);
    void run();
}
