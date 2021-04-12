package entities;

/**
 * @author Alexandr Smirnov
 *
 * Класс кассы банка
 */
public class Cashbox {

    /**
     * Сумма, которая хранится в кассе
     */
    private long cash;

    /**
     *
     * @param initialCash Начальная сумма кассы
     */
    public Cashbox(long initialCash) {
        this.cash = initialCash;
    }

    /**
     *
     * @param moneyAmount Сумма, которая будет положена в кассу
     */
    public void putCash(long moneyAmount) {
        cash += moneyAmount;
    }

    /**
     *
     * @param moneyAmount Сумма, которая будет снята из кассы
     * @return Значение, отражающее получилось ли снятие или нет
     */
    public boolean WithdrawMoney(long moneyAmount) {
        long moneyDelta = cash - moneyAmount;

        if(moneyDelta < 0) {
            cash = 0;
            return false;
        } else {
            cash = moneyDelta;
            return true;
        }
    }

    @Override
    public String toString() {
        return "Cashbox money: " + cash;
    }
}
