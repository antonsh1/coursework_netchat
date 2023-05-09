package ru.smartjava.client2;

import ru.smartjava.client.client.Client;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("client2");
        client.start();
    }
}