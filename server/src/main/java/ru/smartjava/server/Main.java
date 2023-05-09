package ru.smartjava.server;

import ru.smartjava.server.converter.Converter;
import ru.smartjava.server.messages.Message;
import ru.smartjava.server.server.Server;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
//        Message message = new Message(true, "connect", "", new Date(), "vasya", false);
//        String json = Converter.getConverter().messageToJson(message);
//        System.out.println(Converter.getConverter().messageToJson(message));
//        System.out.println(Converter.getConverter().jsonToMessage(json));
        Server server = new Server();
        server.start();
    }
}