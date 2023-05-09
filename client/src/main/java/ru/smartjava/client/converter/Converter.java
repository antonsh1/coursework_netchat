package ru.smartjava.client.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.smartjava.client.messages.Message;


public class Converter {

    private static Converter converter= null;

    private Converter() {
    }

    public static Converter getConverter() {
        if(converter == null) {
            synchronized(Converter.class) {
                if (converter == null) {
                    converter = new Converter();
                }
            }
        }
        return converter;
    }
    Gson gson = new GsonBuilder().create();

    public String messageToJson(Message message) {
        return gson.toJson(message, new TypeToken<Message>() {}.getType());
    }

    public Message jsonToMessage(String json) {
        return gson.fromJson(json, new TypeToken<Message>(){}.getType());
    }


}
