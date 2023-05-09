package ru.smartjava.server.interfaces;

import ru.smartjava.server.messages.Message;

public interface Handler {

    Message handle(Message command);

}
