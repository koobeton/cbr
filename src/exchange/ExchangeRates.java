package exchange;

import static misc.Paint.*;

import org.fusesource.jansi.AnsiConsole;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRates {

    private static final String CBR_URL = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";

    private Map<String, Currency> currencyMap;

    /**
     * Creates an object of exchange rates.
     *
     * @see Currency#getName()
     * */
    public ExchangeRates() {

        currencyMap = new HashMap<>();
    }

    /**
     * @return a map of currencies with a fixed rate
     *
     * @see Currency#getName()
     * @see Currency#getLastValue()
     * @see Currency#getChange()
     * */
    public Map<String, Currency> getRates() {

        String lastDate = "";
        Document xml = getXML(lastDate);
        if (xml != null) {
            lastDate = xml.getDocumentElement().getAttribute("Date");
        }
        parseXML(xml);

        parseXML(getXML(getPreviousDate(lastDate)));

        return currencyMap;
    }

    private String getPreviousDate(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return dateFormat.format(calendar.getTime());
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

        NodeList charCodeList = xml.getElementsByTagName("CharCode");
        for (int i = 0; i < charCodeList.getLength(); i++) {
            Element charCode = (Element) charCodeList.item(i);

            String name = charCode.getTextContent().toUpperCase();

            Element valute = (Element) charCode.getParentNode();

            Element nominal = (Element) valute.getElementsByTagName("Nominal").item(0);
            BigDecimal currentNominal = new BigDecimal(nominal.getTextContent());

            Element value = (Element) valute.getElementsByTagName("Value").item(0);
            BigDecimal currentValue = new BigDecimal(value.getTextContent().replace(',', '.')).divide(currentNominal);

            if (currencyMap.containsKey(name)) {
                currencyMap.get(name).setPreviousValue(currentValue);
            } else {
                Currency currency = new Currency(name);
                currency.setLastValue(currentValue);
                currencyMap.put(name, currency);
            }
        }
    }

    /**
     * Prints a currencies in the provided collection as described in the {@link #print(Currency)}.
     *
     * @param currencies collection of currencies to print
     *
     * @see #print(Currency)
     * */
    public static void print(Collection<Currency> currencies) {

        for (Currency currency : currencies) {
            print(currency);
        }
    }

    /**
     * Prints a currency name, a currency value at the last registered date
     * and a change for the currency.
     *
     * @param currency currency to print
     *
     * @see #print(Collection)
     * */
    public static void print(Currency currency) {

        if (currency == null) return;

        String name = currency.getName();
        BigDecimal lastValue = currency.getLastValue();
        BigDecimal change = currency.getChange();

        AnsiConsole.systemInstall();

        System.out.printf("%s %s%n",
                getAnsiString(MAGENTA, name),
                String.format("%s %s",
                        getAnsiString(WHITE, String.format("%10.4f", lastValue)),
                        getAnsiString(change.signum() < 0 ? RED : GREEN, String.format("%+8.2f", change))
                )
        );

        AnsiConsole.systemUninstall();
    }
}
