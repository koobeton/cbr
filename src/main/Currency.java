package main;

class Currency {

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

    String getName() {
        return name;
    }

    double getTodayValue() {
        return todayValue;
    }

    double getDelta() {
        return todayValue - yesterdayValue;
    }

    boolean isValid() {
        return valid;
    }
}
