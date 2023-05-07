package ru.smartjava.server.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ReadClientConfigFile {

    private String configFilePath;
    private final String PORT_FIELD_NAME = "port";
    private final String HOST_FIELD_NAME = "host";

    Properties properties = new Properties();

    public ReadClientConfigFile(String name) {
        configFilePath = name + "/src/main/resources/client.xml";
    }

    public ReadClientConfigFile client() {
        properties = readFile(configFilePath);
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
