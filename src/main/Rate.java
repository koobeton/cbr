package main;

public class Rate {

    private Currency currency;
    private double todayValue;
    private double delta;

    public Rate(Currency currency, double todayValue, double delta) {
        this.currency = currency;
        this.todayValue = todayValue;
        this.delta = delta;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getTodayValue() {
        return todayValue;
    }

    public double getDelta() {
        return delta;
    }
}
