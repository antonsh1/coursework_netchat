package ru.smartjava.interfaces;

import ru.smartjava.server.messages.Message;

public interface Handler {

    Message handle(Message command);

}
