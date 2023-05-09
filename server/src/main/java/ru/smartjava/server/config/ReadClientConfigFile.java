package ru.smartjava.server.config;

import ru.smartjava.params.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ReadClientConfigFile {

    private String configFilePath;

    Properties properties = new Properties();

    public ReadClientConfigFile(String name) {
        configFilePath = name + Config.CLIENT_BASE_CONFIG_FILE_PATH;
        this.properties = readFile(configFilePath);
    }

    public ReadClientConfigFile() {
        configFilePath = Config.TEST_CLIENT_CONFIG_FILE_PATH;
        this.properties = readFile(configFilePath);
    }

//    public void setFilePath(String filePath) {
//        configFilePath = filePath;
//    }
//    public ReadClientConfigFile client() {
//        properties = readFile(configFilePath);
//        return this;
//    }

    public Integer port() {
        int serverPort = 0;
        try {
            if (!properties.getProperty(Config.PORT_FIELD_NAME).isEmpty()) {
                serverPort = Integer.parseInt(properties.getProperty(Config.PORT_FIELD_NAME));
            }
        } catch (NumberFormatException ne) {
            System.out.println("Ошибка преобразования порта из файла конфигурации " + ne.getMessage());
        }
        return serverPort;
    }

    public String host() {
        String host = "";
        if (!properties.getProperty(Config.HOST_FIELD_NAME).isEmpty()) {
            host = properties.getProperty(Config.HOST_FIELD_NAME);
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
