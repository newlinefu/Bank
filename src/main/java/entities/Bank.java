package entities;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Queue;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Alexandr Smirnov
 *
 * Класс банковской системы
 */
public class Bank {

    private static final Logger LOGGER = LogManager.getLogger(Bank.class);

    /**
     * Колличество работников в банке
     */
    private int workersCount;

    /**
     * Значение, отражающее среднее время обслуживания клиента
     */
    private int AVG_SERVICE_TIME;

    /**
     * Значение, отражающее среднее колличество клиентов в минуту
     */
    private int CLIENTS_PER_MINUTE;

    /**
     * Очередь всех клиентов банка (изначально сгенерированных ClientGenerator).
     * ! Для каждого работника есть своя очередь клиентов, данная очередь необходима для работы всего банка
     */
    private Queue<Client> allClients;

    /**
     * Список всех сотрудников банка
     */
    private Worker[] workers;

    /**
     * Касса банка
     */
    private Cashbox cashbox;

    public Bank() {
        this.workersCount = Runtime.getRuntime().availableProcessors() - 1;
        this.AVG_SERVICE_TIME = 10000;
        this.CLIENTS_PER_MINUTE = 600;

        this.allClients = new LinkedBlockingQueue<Client>();
        this.workers = new Worker[this.workersCount];
        this.cashbox = new Cashbox(2500000);

        for(int i = 0; i < this.workersCount; i++) {
            this.workers[i] = new Worker(this.cashbox);
        }
    }

    /**
     * Функция начала работы банка
     */
    public void getStarted() {
        for (int i = 0; i < workers.length; i++) {
            new Thread(workers[i], "Worker: " + i).start();
        }
        ClientGenerator generator = new ClientGenerator();
        Thread t = new Thread(generator, "ClientGeneratorThread");
        t.start();
        LOGGER.info("Bank has been started");
    }

    private Worker getMostFreeWorker() {
        int minSize = workers[0].getQueueSize();
        int minIndex = 0;

        for(int i = 1; i < this.workersCount; i++) {
            if(workers[i].getQueueSize() < minSize) {
                minSize = workers[i].getQueueSize();
                minIndex = i;
            }
        }
        LOGGER.info("Get most free worker with num: " + minIndex);
        return workers[minIndex];
    }

    /**
     * @author Alexandr Smirnov
     *
     * Класс генерации клиентов
     */
    private class ClientGenerator implements Runnable {

        private Random random;

        public ClientGenerator() {
            random = new Random();
        }

        /**
         *
         * @return Сгенерированнное значение миллисекунд, через которое будет создан новый клиент
         */
        private int generateNextClientTime() {
            boolean sign = random.nextBoolean();
            int clientsQuantity = random.nextInt(CLIENTS_PER_MINUTE / 2);
            int actualClientsPerMinute = sign
                        ? CLIENTS_PER_MINUTE - clientsQuantity
                        : CLIENTS_PER_MINUTE + clientsQuantity;

            return (60 / actualClientsPerMinute) * 1000;
        }

        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            while (true) {
                try {
                    Worker mostFreeWorker = getMostFreeWorker();
                    Client client = new Client(AVG_SERVICE_TIME);
                    mostFreeWorker.addToServiceQueue(client);
                    int sleepingTime = generateNextClientTime();

                    LOGGER.info("Client has been created: " + client + " | sleep in: " + sleepingTime);

                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
