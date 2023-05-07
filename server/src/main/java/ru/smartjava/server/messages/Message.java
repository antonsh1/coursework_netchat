package ru.smartjava.server.messages;

import java.util.Date;

public class Message {

    private final Boolean command;
    private final String commandString;
    private final String message;
    private final Date date;
    private final String nickName;
    private final Boolean disconnect;

    public Message(Boolean command, String commandString, String message, Date date, String nickName, Boolean disconnect) {
        this.command = command;
        this.commandString = commandString;
        this.message = message;
        this.date = date;
        this.nickName = nickName;
        this.disconnect = disconnect;
    }

    public Date getDate() {
        return date;
    }

    public String getCommand() {
        return commandString;
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
                ", commandString='" + commandString + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", nickName='" + nickName + '\'' +
                ", disconnect=" + disconnect +
                '}';
    }
}
