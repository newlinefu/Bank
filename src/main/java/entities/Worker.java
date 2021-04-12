package entities;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import utils.OperationType;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Alexandr Smirnov
 *
 * Класс работника банка
 */
public class Worker implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Worker.class);

    /**
     * Очередь клиентов для данного работника
     */
    private Queue<Client> clients;

    /**
     * Касса банка
     */
    private Cashbox cashbox;

    /**
     *
     * @param cashbox Касса банка
     */
    public Worker(Cashbox cashbox) {
        this.cashbox = cashbox;

        clients = new LinkedBlockingQueue<Client>();
    }

    /**
     *
     * @return Клиент, обслуживание которого завершилось
     */
    public Client finishServiceClient() {
        return clients.poll();
    }

    /**
     *
     * @param newClient Новый клиент, нуждающийся в обслуживании
     */
    public void addToServiceQueue(Client newClient) {
        synchronized (clients) {
            clients.offer(newClient);
            clients.notify();
        }
    }

    /**
     *
     * @return Размер очереди для данного работника
     */
    public int getQueueSize() {
        return clients.size();
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
        Client client;
        while (true) {
            try {
                synchronized (clients) {
                    while (clients.isEmpty()) {
                        clients.wait();
                    }
                    client = finishServiceClient();
                }
                Thread.sleep(client.getServiceTime());
                if(client.getOperationType() == OperationType.REPLENISHMENT) {
                    cashbox.putCash(client.getOperationMoneyAmount());
                    LOGGER.info("Put cash: " + cashbox);
                } else if(client.getOperationType() == OperationType.WITHDRAWAL){
                    cashbox.WithdrawMoney(client.getOperationMoneyAmount());
                    LOGGER.info("Withdraw cash: " + cashbox);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
