package ru.smartjava.server;

import ru.smartjava.server.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}