package ru.smartjava.server.logger;

import java.io.IOException;
import java.util.logging.*;

public class ClientLog {
    public static Logger LOGGER = Logger.getLogger(ClientLog.class.getName());
    private static String logFilePath = "client1/src/main/resources/client.log";
    public static Handler FILE_HANDLER;

    static {
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(Level.ALL);

        try {
            //file handler
//            FILE_HANDLER = new FileHandler(logFilePath, 20000, 10);
            FILE_HANDLER = new FileHandler(logFilePath);
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "[%1$tF %1$tT] [%4$-7s] %5$s %n");
            LOGGER.setUseParentHandlers(false);
            FILE_HANDLER.setFormatter(new SimpleFormatter());
            FILE_HANDLER.setLevel(Level.ALL);
            LOGGER.addHandler(FILE_HANDLER);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return LOGGER;
    }

}