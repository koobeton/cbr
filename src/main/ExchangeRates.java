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
import java.util.Calendar;

public class ExchangeRates {

    private static String today, yesterday;
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        today = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        yesterday = dateFormat.format(calendar.getTime());
    }
    private static String cbrURL = String.format("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=%s&date_req2=%s&VAL_NM_RQ=",
            yesterday, today);

    public static void main(String... args) {

        for (Currency currency : Currency.values()) {
            print(getRate(currency));
        }
    }

    public static Rate getRate(Currency currency) {

        return parseXML(getXML(currency), currency);
    }

    private static Document getXML(Currency currency) {

        URL url;
        HttpURLConnection connection = null;
        BufferedInputStream buffer = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;

        try {
            url = new URL(cbrURL + currency.getCbrCode());
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

    private static Rate parseXML(Document xml, Currency currency) {

        double yesterdayValue = 0;
        double todayValue = 0;

        if (xml != null) {

            NodeList recordList = xml.getElementsByTagName("Record");
            for (int i = 0; i < recordList.getLength(); i++) {

                Element record = (Element) recordList.item(i);

                Element value = (Element) record.getElementsByTagName("Value").item(0);
                double currentValue = Double.parseDouble(value.getTextContent().replace(',', '.'));

                Element nominal = (Element) record.getElementsByTagName("Nominal").item(0);
                int currentNominal = Integer.parseInt(nominal.getTextContent());

                String date = record.getAttribute("Date");
                if (date.equals(yesterday)) {
                    yesterdayValue = currentValue / currentNominal;
                } else if (date.equals(today)) {
                    todayValue = currentValue / currentNominal;
                }
            }
        }

        return new Rate(currency, todayValue, todayValue - yesterdayValue);
    }

    public static void print(Rate rate) {

        Currency currency = rate.getCurrency();
        double todayValue = rate.getTodayValue();
        double delta = rate.getDelta();

        AnsiConsole.systemInstall();

        System.out.printf("%s %s %s%n",
                getAnsiString(MAGENTA, currency.name()),
                getAnsiString(WHITE, String.format("%.4f", todayValue)),
                getAnsiString(delta < 0 ? RED : GREEN, String.format("%+.2f", delta)));

        AnsiConsole.systemUninstall();
    }
}
