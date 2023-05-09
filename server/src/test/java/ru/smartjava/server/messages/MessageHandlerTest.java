package ru.smartjava.server.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.smartjava.server.interfaces.Broker;
import ru.smartjava.server.interfaces.Handler;
import ru.smartjava.server.interfaces.Maker;
import ru.smartjava.server.params.Cmd;

public class MessageHandlerTest {

    static String nickName = "Petya";
    Maker messageMaker = MessageMaker.getMessageMaker();
    static Broker messageBroker = Mockito.spy(Broker.class);
    Handler messageHandler = new MessageHandler(messageBroker);

    @Test
    void check() {
        Assertions.assertTrue(messageMaker.isExitCommand(messageMaker.exitClientMessage(nickName)));
        Mockito.when(messageBroker.isNickNameUnique(nickName)).thenReturn(true);
        Assertions.assertEquals(messageHandler.handle(messageMaker.connect(nickName)), messageMaker.connectAccepted(nickName) );
        Mockito.when(messageBroker.isNickNameUnique(nickName)).thenReturn(false);
        Assertions.assertEquals(messageHandler.handle(messageMaker.connect(nickName)), messageMaker.connectRejected(nickName) );
        Assertions.assertEquals(messageHandler.handle(messageMaker.exitClientMessage(nickName)), messageMaker.disconnect(nickName, Cmd.EXIT));
        Assertions.assertEquals(messageHandler.handle(messageMaker.clientCommand(nickName, Cmd.EXIT)), messageMaker.disconnect(nickName, Cmd.EXIT));
    }
}
