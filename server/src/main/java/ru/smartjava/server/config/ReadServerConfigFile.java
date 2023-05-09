package ru.smartjava.server.config;

import ru.smartjava.server.params.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ReadServerConfigFile {

    Properties properties;
    public ReadServerConfigFile() {
        properties = readFile(Config.SERVER_CONFIG_FILE_PATH);
    }

    public ReadServerConfigFile(String filePath) {
        this.properties = readFile(filePath);
    }

    public Integer port() {
        int serverPort = 0;
        try {
            if (!properties.getProperty(Config.PORT_FIELD_NAME).isEmpty()) {
                serverPort = Integer.parseInt(properties.getProperty(Config.PORT_FIELD_NAME));
            }
        } catch (NumberFormatException ne) {
            System.err.println("Ошибка преобразования порта из файла конфигурации " + ne.getMessage());
        }
        return serverPort;
    }

    public Integer threadLimit() {
        int threadLimit = 0;
        try {
            if (!properties.getProperty(Config.THREAD_LIMIT_NAME).isEmpty()) {
                threadLimit = Integer.parseInt(properties.getProperty(Config.THREAD_LIMIT_NAME));
            }
        } catch (NumberFormatException ne) {
            System.err.println("Ошибка преобразования порта из файла конфигурации " + ne.getMessage());
        }
        return threadLimit;
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
