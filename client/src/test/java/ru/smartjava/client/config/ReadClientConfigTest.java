package ru.smartjava.client.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReadClientConfigTest {
    private final int port = 8080;
    private final String host = "netology";
    ReadClientConfigFile readClientConfigFile = new ReadClientConfigFile();

    @Test
    void ReadParams() {
        Assertions.assertEquals(readClientConfigFile.port(), port);
        Assertions.assertEquals(readClientConfigFile.host(), host);
    }
}
