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
//    static String filePattern = "src/main/resources/logs/server%.log";
//    static int limit = 1000 * 1000; // 1 Mb
//    static int numLogFiles = 3;
    public static Logger getLogger() {
        if (logger == null) {
            synchronized (ServerFacadeLog.class) {
                if (logger == null) {
                    logger = Logger.getLogger("Server");
                    try {

                        System.setProperty("java.util.logging.SimpleFormatter.format",
                                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
//                        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s %n");
                        FileHandler fh = new FileHandler(logFilePath, true);
//                        FileHandler fh = new FileHandler(filePattern,limit, numLogFiles);
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

    private ServerFacadeLog() throws IOException {
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
