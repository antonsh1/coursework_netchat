package ru.smartjava.server.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smartjava.params.Cmd;
import ru.smartjava.server.converter.Converter;
import ru.smartjava.server.messages.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConverterTest {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy");

    Date date;
    {
        try {
            date = formatter.parse("07/05/2013");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private final String jsonString = "{\"command\":true,\"commandValue\":\"connect\",\"message\":\"\",\"date\":\"May 7, 2013, 12:00:00 AM\",\"nickName\":\"vasya\",\"disconnect\":false}";
    private final Message message = new Message(true, Cmd.CONNECT, "", date, "vasya", false);

    Converter converter = Converter.getConverter();

    @Test
    void toJson() {
        Assertions.assertEquals(converter.messageToJson(message),jsonString);
    }
    @Test
    void toMessage() {
        Assertions.assertEquals(converter.jsonToMessage(jsonString),message);
    }
}
