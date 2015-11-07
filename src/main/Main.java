package main;

import exchange.Currency;
import exchange.ExchangeRates;

import java.util.Map;

public class Main {

    public static void main(String... args) {

        Map<String, Currency> rates = new ExchangeRates().getRates();

        if (args.length == 0) {
            ExchangeRates.print(rates.values());
        } else {
            for (String arg : args) {
                ExchangeRates.print(rates.get(arg.toUpperCase()));
            }
        }
    }
}
