package ru.smartjava.enums;

public enum Commands {

    CONNECT("connect"),
    INFO("info"),
    EXIT("exit");

    private final String command;
    Commands(String commandString) {
        this.command = commandString;
    }

    public final String command() {
        return this.command;
    }

    @Override
    public String toString() {
        return this.command;
    }
}
