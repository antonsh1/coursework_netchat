package ru.smartjava.client.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smartjava.client.interfaces.Maker;
import ru.smartjava.client.params.Cmd;

import java.util.Date;

public class MessageMakerTest {
    Maker messageMaker = MessageMaker.getMessageMaker();

    String nickName = "Petya";
    Message message = new Message(true, Cmd.CONNECT, messageMaker.connectAcceptedMessage(nickName), new Date(), nickName, false);

    @Test
    void check() {

        Assertions.assertEquals(messageMaker.connectAccepted(nickName), message);
    }

}
