package main;

import static misc.Paint.*;

import org.fusesource.jansi.AnsiConsole;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExchangeRates {

    private static final String CBR_URL = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";

    private String today, yesterday;
    private List<Currency> currencyList;

    public ExchangeRates(String... currencies) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        today = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        yesterday = dateFormat.format(calendar.getTime());

        currencyList = new ArrayList<>();
        for (String currency : currencies) {
            currencyList.add(new Currency(currency));
        }
    }

    public static void main(String... args) {

        if (args.length == 0) {
            showUsage();
            System.exit(0);
        }

        ExchangeRates exchangeRates = new ExchangeRates(args);

        for (Currency currency : exchangeRates.getRates()) {
            print(currency);
        }
    }

    public List<Currency> getRates() {
        parseXML(getXML(yesterday));
        parseXML(getXML(today));
        return currencyList;
    }

    private Document getXML(String date) {

        URL url;
        HttpURLConnection connection = null;
        BufferedInputStream buffer = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;

        try {
            url = new URL(CBR_URL + date);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            buffer = new BufferedInputStream(connection.getInputStream());

            builder = factory.newDocumentBuilder();
            document = builder.parse(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) connection.disconnect();
        }

        return document;
    }

    private void parseXML(Document xml) {

        if (xml == null) return;

        String date = xml.getDocumentElement().getAttribute("Date");

        NodeList charCodeList = xml.getElementsByTagName("CharCode");
        for (int i = 0; i < charCodeList.getLength(); i++) {
            Element charCode = (Element) charCodeList.item(i);

            for (Currency currency : currencyList) {
                if (currency.getName().toUpperCase().equals(charCode.getTextContent().toUpperCase())) {
                    Element valute = (Element) charCode.getParentNode();

                    Element nominal = (Element) valute.getElementsByTagName("Nominal").item(0);
                    int currentNominal = Integer.parseInt(nominal.getTextContent());

                    Element value = (Element) valute.getElementsByTagName("Value").item(0);
                    double currentValue = Double.parseDouble(value.getTextContent().replace(',', '.')) / currentNominal;

                    if (date.equals(yesterday)) {
                        currency.setYesterdayValue(currentValue);
                    } else if (date.equals(today)) {
                        currency.setTodayValue(currentValue);
                    }

                    currency.setValid();
                }
            }
        }
    }

    public static void print(Currency currency) {

        String name = currency.getName();
        double todayValue = currency.getTodayValue();
        double delta = currency.getDelta();

        AnsiConsole.systemInstall();

        System.out.printf("%s %s%n",
                getAnsiString(MAGENTA, name),
                currency.isValid() ?
                        String.format("%s %s",
                                getAnsiString(WHITE, String.format("%.4f", todayValue)),
                                getAnsiString(delta < 0 ? RED : GREEN, String.format("%+.2f", delta))) :
                        getAnsiString(RED, "no such currency")
                );

        AnsiConsole.systemUninstall();
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
