package ru.smartjava.server.server;

import ru.smartjava.server.interfaces.Broker;
import ru.smartjava.server.params.Defaults;
import ru.smartjava.server.messages.MessageBroker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;
import java.util.logging.Logger;
import ru.smartjava.server.logger.ServerFacadeLog;
import ru.smartjava.server.config.ReadServerConfigFile;

public class Server extends Thread {

    Logger logger = ServerFacadeLog.getLogger();
    private int FULL_THREAD_POOL;
    private int SERVER_THREAD_LIMIT;
    private final ServerSocket serverSocket;
    private final Broker messageBroker = new MessageBroker();
//    private final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//    private final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(1);
    private final ThreadFactory threadFactory = Executors.defaultThreadFactory();

    private ThreadPoolExecutor threadPoolExecutor;
    public Server() {
        System.out.println("Старт сервера");
        logger.info("Старт сервера");
        int serverPort = new ReadServerConfigFile().port();
        if(serverPort == 0) {
            serverPort = Defaults.SERVER_DEFAULT_PORT;
            logger.info("используем порт по умолчанию :" + serverPort);
        } else {
            logger.info("используем порт из файла конфигурации: " + serverPort);
        }

        SERVER_THREAD_LIMIT = new ReadServerConfigFile().threadLimit();
        if(SERVER_THREAD_LIMIT == 0) {
            SERVER_THREAD_LIMIT = Defaults.SERVER_DEFAULT_THREAD_LIMIT;
            logger.info("используем ограничение потоков по умолчанию :" + SERVER_THREAD_LIMIT);
        } else {
            logger.info("используем ограничение потоков из файла конфигурации: " + SERVER_THREAD_LIMIT);
        }
        FULL_THREAD_POOL = SERVER_THREAD_LIMIT + Defaults.SERVER_REJECT_THREAD_POOL;
//        Integer connectionLimit = Integer.parseInt(serverConfig.getProperty("threadLimit"));
//        private final ExecutorService clientProcessingPool = Executors.newCachedThreadPool();
//        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(2);

        new Thread((Runnable) messageBroker).start();
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Сервер запущен");
            logger.info("Сервер запущен");
        } catch (IOException e) {
            logger.severe("Ошибка открытия сокета: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        threadPoolExecutor = new ThreadPoolExecutor(FULL_THREAD_POOL,FULL_THREAD_POOL,10, TimeUnit.SECONDS,new ArrayBlockingQueue<>(FULL_THREAD_POOL),threadFactory);
        try {
            this.serverSocket.setSoTimeout(Defaults.SERVER_SOCKET_TIMEOUT);
        } catch (IOException e1) {
            //
        }

        while (true) {
            try {
                final Socket clientSocket = this.serverSocket.accept();
                if(threadPoolExecutor.getActiveCount() < SERVER_THREAD_LIMIT) {
                    ServerClientHandler clientHandler = new ServerClientHandler(clientSocket, messageBroker, false);
                    threadPoolExecutor.execute(clientHandler);
                } else {
                    ServerClientHandler clientHandler = new ServerClientHandler(clientSocket, messageBroker, true);
                    threadPoolExecutor.execute(clientHandler);
                }

            } catch (SocketTimeoutException te) {
                // Срабатывает каждые SOCKET_TIMEOUT миллисекунд
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
