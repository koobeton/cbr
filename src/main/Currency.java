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

    /**
     * Return a currency name
     * */
    public String getName() {
        return name;
    }

    /**
     * Return a currency today value
     * */
    public double getTodayValue() {
        return todayValue;
    }

    /**
     * Return a currency change
     * */
    public double getChange() {
        return todayValue - yesterdayValue;
    }

    /**
     * If the name of the given currency is not in server response, it is not valid
     * */
    public boolean isValid() {
        return valid;
    }
}
