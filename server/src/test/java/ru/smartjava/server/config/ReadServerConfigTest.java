package ru.smartjava.server.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smartjava.params.Config;

public class ReadServerConfigTest {
    private final int port = 8000;
    private final int threadLimit = 10;
    ReadServerConfigFile readServerConfigFile = new ReadServerConfigFile(Config.TEST_SERVER_CONFIG_FILE_PATH);

    @Test
    void ReadParams() {
        Assertions.assertEquals(readServerConfigFile.port(), port);
        Assertions.assertEquals(readServerConfigFile.threadLimit(), threadLimit);
    }
}
