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

    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String REQUEST_METHOD_GET = "GET";

    private static String today, yesterday;
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        today = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        yesterday = dateFormat.format(calendar.getTime());
    }
    private static String cbrURL = String.format("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=%s&date_req2=%s&VAL_NM_RQ=",
            yesterday, today);

    public static void main(String... args) {

        AnsiConsole.systemInstall();

        URL url;
        HttpURLConnection connection = null;
        BufferedInputStream buffer = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;

        try {
            builder = factory.newDocumentBuilder();
            for (Currency currency : Currency.values()) {
                url = new URL(cbrURL + currency.getCbrCode());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(REQUEST_METHOD_GET);
                buffer = new BufferedInputStream(connection.getInputStream());
                document = builder.parse(buffer);

                buffer.close();
                connection.disconnect();

                System.out.printf("%s %s%n",
                        getAnsiString(MAGENTA, currency.name()),
                        parseRates(document));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) connection.disconnect();
            AnsiConsole.systemUninstall();
        }
    }

    private static String parseRates(Document xml) {

        double yesterdayValue = 0;
        double todayValue = 0;
        double delta;

        NodeList recordList = xml.getElementsByTagName("Record");
        for (int i = 0; i < recordList.getLength(); i++) {
            Element record = (Element) recordList.item(i);
            Element value = (Element) record.getElementsByTagName("Value").item(0);
            double currentValue = Double.parseDouble(value.getTextContent().replace(',', '.'));
            String date = record.getAttribute("Date");
            if (date.equals(yesterday)) {
                yesterdayValue = currentValue;
            } else if (date.equals(today)) {
                todayValue = currentValue;
            }
        }
        delta = todayValue - yesterdayValue;

        return String.format("%s %s",
                getAnsiString(WHITE, String.format("%.4f", todayValue)),
                getAnsiString(delta < 0 ? RED : GREEN, String.format("%+.2f", delta)));
    }
}
