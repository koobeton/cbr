package main;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static main.Currency.*;

public class ExchangeRates {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static String today, yesterday;

    public static void main(String... args) {

        setDates();

        for (Currency currency : Currency.values()) {
            if (currency.getCbrCode() != null) {
                System.out.printf("%s, cbr code: %s%n", currency.name(), currency.getCbrCode());
            }
        }

        System.out.println(today);
        System.out.println(yesterday);
    }

    private static void setDates() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        today = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        yesterday = dateFormat.format(calendar.getTime());
    }
}
