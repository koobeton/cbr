package exchange;

import java.math.BigDecimal;

public class Currency {

    private String name;
    private BigDecimal lastValue = BigDecimal.ZERO;
    private BigDecimal previousValue = BigDecimal.ZERO;

    Currency(String name) {
        this.name = name.toUpperCase();
    }

    void setLastValue(BigDecimal lastValue) {
        this.lastValue = lastValue;
    }

    void setPreviousValue(BigDecimal previousValue) {
        this.previousValue = previousValue;
    }

    /**
     * @return a currency name (e.g. USD EUR GBP ...)
     *
     * @see ExchangeRates#ExchangeRates()
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
}
