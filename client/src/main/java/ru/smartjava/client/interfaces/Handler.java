package ru.smartjava.client.interfaces;

import ru.smartjava.client.messages.Message;

public interface Handler {

    Message handle(Message command);

}
