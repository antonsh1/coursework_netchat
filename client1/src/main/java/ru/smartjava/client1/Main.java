package ru.smartjava.client1;

import ru.smartjava.client.client.Client;

public class Main {
    public static void main(String[] args) {

        Client client = new Client("client1");
        client.start();
    }
}