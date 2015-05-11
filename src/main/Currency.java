package main;

import java.math.BigDecimal;

public class Currency {

    private String name;
    private BigDecimal todayValue = BigDecimal.ZERO;
    private BigDecimal yesterdayValue = BigDecimal.ZERO;
    private boolean valid = false;

    Currency(String name) {
        this.name = name.toUpperCase();
    }

    void setTodayValue(BigDecimal todayValue) {
        this.todayValue = todayValue;
    }

    void setYesterdayValue(BigDecimal yesterdayValue) {
        this.yesterdayValue = yesterdayValue;
    }

    void setValid() {
        this.valid = true;
    }

    /**
     * @return a given currency name (e.g. USD EUR GBP ...)
     *
     * @see #isValid()
     * @see ExchangeRates#ExchangeRates(String...)
     * */
    public String getName() {
        return name;
    }

    /**
     * @return a currency today value
     * */
    public BigDecimal getTodayValue() {
        return todayValue;
    }

    /**
     * @return a currency change
     * */
    public BigDecimal getChange() {
        return todayValue.subtract(yesterdayValue);
    }

    /**
     * If the given currency name is not present in the server response,
     * the currency is not valid.
     *
     * @return {@code true} if currency name present in the server response, {@code false} otherwise
     *
     * @see #getName()
     * @see ExchangeRates#ExchangeRates(String...)
     * */
    public boolean isValid() {
        return valid;
    }
}
