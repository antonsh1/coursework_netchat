package ru.smartjava.server.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smartjava.params.Config;
import ru.smartjava.params.Defaults;
import ru.smartjava.server.config.ReadClientConfigFile;
import ru.smartjava.server.config.ReadServerConfigFile;

public class ReadClientConfigTest {
    private final int port = 8080;
    private final String host = "netology";
    ReadClientConfigFile readClientConfigFile = new ReadClientConfigFile();

    @Test
    void ReadParams() {
        Assertions.assertEquals(readClientConfigFile.port(),port);
        Assertions.assertEquals(readClientConfigFile.host(),host);
    }
}
