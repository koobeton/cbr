package main;

import exchange.ExchangeRates;
import org.fusesource.jansi.AnsiConsole;

import static misc.Paint.*;

public class Main {

    public static void main(String... args) {

        if (args.length == 0) {
            showUsage();
            System.exit(0);
        }

        ExchangeRates exchangeRates = new ExchangeRates(args);

        ExchangeRates.print(exchangeRates.getRates());
    }

    private static void showUsage() {

        AnsiConsole.systemInstall();

        System.out.printf("Usage: %s%n" +
                        "\twhere %s - the ISO 4217 currency code (%s)%n",
                getAnsiString(YELLOW, "java -jar cbr.jar code ..."),
                getAnsiString(YELLOW, "code"),
                getAnsiString(MAGENTA, "USD EUR GBP ..."));

        AnsiConsole.systemUninstall();
    }
}
