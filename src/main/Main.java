package main;

import exchange.Currency;
import exchange.ExchangeRates;
import org.fusesource.jansi.AnsiConsole;

import java.util.Map;

import static misc.Paint.*;

public class Main {

    public static void main(String... args) {

        Map<String, Currency> rates = new ExchangeRates().getRates();

        if (args.length == 0) {
            ExchangeRates.print(rates.values());
        } else {
            for (String arg : args) {
                if (rates.containsKey(arg.toUpperCase())) {
                    ExchangeRates.print(rates.get(arg.toUpperCase()));
                } else {
                    AnsiConsole.systemInstall();
                    System.out.printf("%s %s%n",
                            getAnsiString(MAGENTA, arg.toUpperCase()),
                            getAnsiString(RED, "no such currency"));
                    AnsiConsole.systemUninstall();
                }
            }
        }
    }
}
