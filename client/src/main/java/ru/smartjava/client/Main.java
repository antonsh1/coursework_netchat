package ru.smartjava.client;

import ru.smartjava.client.client.Client;

public class Main {
    public static void main(String[] args) {

        Client client = new Client("client");
        client.start();
    }
}