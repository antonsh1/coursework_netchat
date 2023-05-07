package ru.smartjava.server.logger;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerFacadeLog {
    private static Logger logger = null;
    private static final String logFilePath = "server/src/main/resources/server.log";
    public static Logger getLogger() {
        if (logger == null) {
            synchronized (ServerFacadeLog.class) {
                if (logger == null) {
                    logger = Logger.getLogger("Server");
                    try {

                        System.setProperty("java.util.logging.SimpleFormatter.format",
                                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
                        FileHandler fh = new FileHandler(logFilePath, true);
                        fh.setFormatter(new SimpleFormatter() {
                        private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

                        @Override
                        public synchronized String format(LogRecord lr) {
                            return String.format(format,
                                    new Date(lr.getMillis()),
                                    lr.getLevel().getLocalizedName(),
                                    lr.getMessage()
                            );
                        }
                    });
                        logger.addHandler(fh);
                        logger.setUseParentHandlers(false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return logger;
    }

    private ServerFacadeLog() {
    }
}
