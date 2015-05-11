package main;

public class Currency {

    private String name;
    private double todayValue;
    private double yesterdayValue;
    private boolean valid = false;

    Currency(String name) {
        this.name = name.toUpperCase();
    }

    void setTodayValue(double todayValue) {
        this.todayValue = todayValue;
    }

    void setYesterdayValue(double yesterdayValue) {
        this.yesterdayValue = yesterdayValue;
    }

    void setValid() {
        this.valid = true;
    }

    public String getName() {
        return name;
    }

    public double getTodayValue() {
        return todayValue;
    }

    public double getDelta() {
        return todayValue - yesterdayValue;
    }

    public boolean isValid() {
        return valid;
    }
}
