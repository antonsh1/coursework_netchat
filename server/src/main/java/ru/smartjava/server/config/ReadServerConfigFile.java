package ru.smartjava.server.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;
import ru.smartjava.server.logger.ServerFacadeLog;

public class ReadServerConfigFile {

    private final String SERVER_CONFIG_FILE_PATH = "server/src/main/resources/server.xml";
    private final String PORT_FIELD_NAME = "port";
    private final String HOST_FIELD_NAME = "host";
    private final String THREAD_LIMIT_NAME = "threadLimit";

    Properties properties  = new Properties();

    public ReadServerConfigFile server() {
        properties = readFile(SERVER_CONFIG_FILE_PATH);
        return this;
    }

    public Integer port() {
        int serverPort = 0;
        try {
            if (!properties.getProperty(PORT_FIELD_NAME).isEmpty()) {
                serverPort = Integer.parseInt(properties.getProperty(PORT_FIELD_NAME));
            }
        } catch (NumberFormatException ne) {
            System.out.println("Ошибка преобразования порта из файла конфигурации " + ne.getMessage());
        }
        return serverPort;
    }

    public Integer threadLimit() {
        int threadLimit = 0;
        try {
            if (!properties.getProperty(THREAD_LIMIT_NAME).isEmpty()) {
                threadLimit = Integer.parseInt(properties.getProperty(THREAD_LIMIT_NAME));
            }
        } catch (NumberFormatException ne) {
            System.out.println("Ошибка преобразования порта из файла конфигурации " + ne.getMessage());
        }
        return threadLimit;
    }

    public String host() {
        String host = "";
            if (!properties.getProperty(HOST_FIELD_NAME).isEmpty()) {
                host = properties.getProperty(HOST_FIELD_NAME);
            }
        return host;
    }

    private Properties readFile(String filePath) {
        Properties configProperties = new Properties();
        try {
            configProperties.loadFromXML(Files.newInputStream(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return configProperties;
    }

}
