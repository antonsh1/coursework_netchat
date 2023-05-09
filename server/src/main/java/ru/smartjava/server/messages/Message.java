package ru.smartjava.server.messages;

import java.util.Date;
import java.util.Objects;

public class Message {

    private final Boolean command;
    private final String commandValue;
    private final String message;
    private final Date date;
    private final String nickName;
    private final Boolean disconnect;

    public Message(boolean command, String commandValue, String message, Date date, String nickName, boolean disconnect) {
        this.command = (Boolean) command;
        this.commandValue = commandValue;
        this.message = message;
        this.date = date;
        this.nickName = nickName;
        this.disconnect = (Boolean) disconnect;
    }

    public Date getDate() {
        return date;
    }

    public String getCommand() {
        return commandValue;
    }

    public boolean isCommand() {
        return command;
    }

    public String getNickName() {
        return nickName;
    }

    public String getMessage() {
        return message;
    }

    public boolean getDisconnect() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(command, message1.command) && Objects.equals(commandValue, message1.commandValue) && Objects.equals(message, message1.message) && Objects.equals(nickName, message1.nickName) && Objects.equals(disconnect, message1.disconnect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, commandValue, message, nickName, disconnect);
    }
}
