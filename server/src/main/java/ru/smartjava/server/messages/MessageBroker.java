package ru.smartjava.server.messages;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ru.smartjava.interfaces.Broker;
import ru.smartjava.interfaces.Handler;
import ru.smartjava.interfaces.Maker;
import ru.smartjava.server.logger.ServerFacadeLog;
public class MessageBroker implements Runnable, Broker {

    Logger logger = ServerFacadeLog.getLogger();
    private final Integer SLEEP_TIMEOUT = 500;
    private final Handler messageHandler = new MessageHandler(this);
    private final Maker messageMaker = MessageMaker.getMessageMaker();
    private final Map<String, BlockingDeque<Message>> mapFromClientQueue = new LinkedHashMap<>();
    private final Map<String, BlockingDeque<Message>> mapToClientQueue = new LinkedHashMap<>();

    public void addClientQueues(String clientName, BlockingDeque<Message> fromClientBlockingQueue, BlockingDeque<Message> toClientBlockingQueue) {
        mapFromClientQueue.put(clientName, fromClientBlockingQueue);
        mapToClientQueue.put(clientName, toClientBlockingQueue);
    }

    public void removeClientQueues(String clientName) {
        mapFromClientQueue.remove(clientName);
        mapToClientQueue.remove(clientName);
    }

    public boolean isNickNameUnique(String nickName) {
        return mapToClientQueue.keySet().stream().noneMatch(name -> Objects.equals(name, nickName)) && !Objects.equals(nickName, "");
    }

    public void sendEveryOneExceptSender(Message message) {
        for (Map.Entry<String, BlockingDeque<Message>> output : mapToClientQueue.entrySet()) {
            if (!Objects.equals(output.getKey(), message.getNickName())) {
                output.getValue().offerFirst(message);
            }
        }
    }

    @Override
    public void run() {
        logger.info("Message broker запущен");
        System.out.println("Message broker запущен");
        while (true) {
            try {
                Thread.sleep(SLEEP_TIMEOUT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (Map.Entry<String, BlockingDeque<Message>> input : mapFromClientQueue.entrySet()) {
                try {
                    List<Message> messages = new ArrayList<>(input.getValue());
                    if (messages.size() > 0) {
                        input.getValue().removeAll(messages);
                        List<Message> commandsMessages = messages.stream().filter(Message::isCommand).collect(Collectors.toList());

                        messages.removeAll(commandsMessages);
                        for (Message command : commandsMessages) {
                            mapToClientQueue.get(command.getNickName()).put(messageHandler.handle(command));
                            if(messageMaker.isExitCommand(command)) {
                                Message message = messageMaker.exitClientMessage(command.getNickName());
                                messages.add(message);
                            }
                        }
                        messages.forEach(this::sendEveryOneExceptSender);
                    }
                } catch (NoSuchElementException nsl) {
//                    System.out.println("Нет элементов");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

