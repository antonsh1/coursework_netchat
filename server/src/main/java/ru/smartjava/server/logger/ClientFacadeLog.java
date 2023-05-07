package ru.smartjava.server.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientFacadeLog {
    private static Logger logger = null;

    public static Logger getLogger(String name) {
        String logFilePath = name + "/src/main/resources/client.log";
        if (logger == null) {
            synchronized (ClientFacadeLog.class) {
                if (logger == null) {
                    logger = Logger.getLogger("ClientLog");
                    try {
                        System.setProperty("java.util.logging.SimpleFormatter.format",
                                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
                        FileHandler fh = new FileHandler(logFilePath, true);
                        fh.setFormatter(new SimpleFormatter());
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

    private ClientFacadeLog() {
    }



//    private static Log logger = null;
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM HH:mm:ss");
//    private String fileNamePath;
//    private Log() {
//    }
//
//    public static Log getLogger() {
//        if(logger == null) {
//            synchronized(MessageMaker.class) {
//                if (logger == null) {
//                    logger = new Log();
//                }
//            }
//        }
//        return logger;
//    }
//
//
//
//    public void setLogFileName(String logFilePath) {
//        fileNamePath = logFilePath;
//    }
//
//    public void log(String message) {
//        simpleDateFormat.format(new Date())
//    }
}
