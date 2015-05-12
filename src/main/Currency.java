package main;

import java.math.BigDecimal;

public class Currency {

    private String name;
    private BigDecimal lastValue = BigDecimal.ZERO;
    private BigDecimal previousValue = BigDecimal.ZERO;
    private boolean valid = false;

    Currency(String name) {
        this.name = name.toUpperCase();
    }

    void setLastValue(BigDecimal lastValue) {
        this.lastValue = lastValue;
    }

    void setPreviousValue(BigDecimal previousValue) {
        this.previousValue = previousValue;
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
     * @return a currency value at the last registered date
     * */
    public BigDecimal getLastValue() {
        return lastValue;
    }

    /**
     * @return a currency change
     * */
    public BigDecimal getChange() {
        return lastValue.subtract(previousValue);
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
