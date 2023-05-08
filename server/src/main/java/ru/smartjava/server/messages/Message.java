package ru.smartjava.server.messages;

import ru.smartjava.enums.Commands;

import java.util.Date;

public class Message {

    private final Boolean command;
    private final Commands commandValue;
    private final String message;
    private final Date date;
    private final String nickName;
    private final Boolean disconnect;

    public Message(Boolean command, Commands commandValue, String message, Date date, String nickName, Boolean disconnect) {
        this.command = command;
        this.commandValue = commandValue;
        this.message = message;
        this.date = date;
        this.nickName = nickName;
        this.disconnect = disconnect;
    }

    public Date getDate() {
        return date;
    }

    public String getCommand() {
        return commandValue;
    }

    public Boolean isCommand() {
        return command;
    }

    public String getNickName() {
        return nickName;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getDisconnect() {
        return disconnect;
    }

    @Override
    public String toString() {
        return "Message{" +
                "command=" + command +
                ", commandString='" + commandValue + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", nickName='" + nickName + '\'' +
                ", disconnect=" + disconnect +
                '}';
    }
}
