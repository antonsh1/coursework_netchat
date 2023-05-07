package ru.smartjava.server.server;

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
    private final Integer SOCKET_TIMEOUT = 1000;
    private Integer SERVER_PORT = 8090;
    private Integer THREAD_LIMIT = 2;
    private final Integer REJECT_THREAD_POOL = 2;
    private Integer FULL_THREAD_POOL = THREAD_LIMIT + REJECT_THREAD_POOL;
    private final ServerSocket serverSocket;
    MessageBroker messageBroker = new MessageBroker();
//    private final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//    private final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(1);
    ThreadFactory threadFactory = Executors.defaultThreadFactory();

    ThreadPoolExecutor threadPoolExecutor;
    public Server() {
        System.out.println("Старт сервера");
        logger.info("Старт сервера");
        int port = new ReadServerConfigFile().server().port();
        if(port == 0) {
            logger.info("Старт сервера");
//            System.out.println("используем порт по умолчанию :" + SERVER_PORT);
            logger.info("используем порт по умолчанию :" + SERVER_PORT);
        } else {
            SERVER_PORT = port;
//            System.out.println("используем порт из файла конфигурации: " + SERVER_PORT);
            logger.info("используем порт из файла конфигурации: " + SERVER_PORT);
        }

        int threadLimit = new ReadServerConfigFile().server().threadLimit();
        if(threadLimit == 0) {
            logger.info("используем ограничение потоков по умолчанию :" + THREAD_LIMIT);
        } else {
            THREAD_LIMIT = threadLimit;
            FULL_THREAD_POOL = THREAD_LIMIT + REJECT_THREAD_POOL;
            logger.info("используем ограничение потоков из файла конфигурации: " + THREAD_LIMIT);
        }

//        Integer connectionLimit = Integer.parseInt(serverConfig.getProperty("threadLimit"));
//        private final ExecutorService clientProcessingPool = Executors.newCachedThreadPool();
//        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(2);

        new Thread(messageBroker).start();
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
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
            this.serverSocket.setSoTimeout(SOCKET_TIMEOUT);
        } catch (IOException e1) {
            //
        }

        while (true) {
            try {
                final Socket clientSocket = this.serverSocket.accept();
                if(threadPoolExecutor.getActiveCount() < THREAD_LIMIT) {
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
