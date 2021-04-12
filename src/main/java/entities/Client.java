package entities;

import utils.OperationType;

import java.util.Random;

/**
 * @author Smirnov Alexandr
 *
 * Класс клиента банка
 */
public class Client {

    private static Random random = new Random();

    /**
     * Тип операции, которую хочет совершить клиент
     */
    private OperationType operationType;

    /**
     * Сумма, на которую хочет совершить операцию
     */
    private long operationMoneyAmount;

    /**
     * Время обслуживания данного клиента
     */
    private int serviceTime;


    /**
     *
     * @param avgServiceTime Среднее время обслуживания клиентов
     */
    public Client(int avgServiceTime) {
        this.operationType = generateOperationType();
        this.operationMoneyAmount = generateOperationMoneyAmount();
        this.serviceTime = generateServiceTime(avgServiceTime);
    }

    /**
     *
     * @param avgServiceTime Среднее время обслуживания клиентов
     * @return Сгенерированное время обслуживания данного клиента
     */
    private int generateServiceTime(int avgServiceTime) {
        boolean sign = random.nextBoolean();
        int time = random.nextInt(avgServiceTime / 2);
        return sign
                ? avgServiceTime + time
                : avgServiceTime - time;
    }

    /**
     *
     * @return Сгенерированный тип операции для данного клиента
     */
    private OperationType generateOperationType() {
        boolean sign = random.nextBoolean();

        return sign ? OperationType.REPLENISHMENT : OperationType.WITHDRAWAL;
    }
    /**
     *
     * @return Сгенерированная сумма, на которую клиент хочет совершить операцию
     */
    private long generateOperationMoneyAmount() {
        return random.nextInt(10000);
    }

    /**
     *
     * @return Тип операции клиента
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     *
     * @return Сумма, на которую клиент хочет совершить операцию
     */
    public long getOperationMoneyAmount() {
        return operationMoneyAmount;
    }

    /**
     *
     * @return Время обслуживания для данного клиента
     */
    public int getServiceTime() {
        return serviceTime;
    }


    @Override
    public String toString() {
        return "Client with: operationType - " + operationType
                + " | operationMoneyAmount: " + operationMoneyAmount
                + " | serviceTime: " + serviceTime;
    }
}
