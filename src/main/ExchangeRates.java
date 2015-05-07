package main;

import static main.Currency.*;

public class ExchangeRates {

    public static void main(String... args) {

        for (Currency currency : Currency.values()) {
            if (currency.getCbrCode() != null) {
                System.out.printf("%s, cbr code: %s%n", currency.name(), currency.getCbrCode());
            }
        }
    }
}
