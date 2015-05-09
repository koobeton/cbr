package main;

import static misc.Paint.*;

import org.fusesource.jansi.AnsiConsole;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;

        try {
            builder = factory.newDocumentBuilder();
            for (Currency currency : Currency.values()) {
                url = new URL(cbrURL + currency.getCbrCode());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(REQUEST_METHOD_GET);
                document = builder.parse(connection.getInputStream());
                connection.disconnect();

                System.out.printf("%s %s%n",
                        getAnsiString(MAGENTA, currency.name()),
                        getRates(document));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
            AnsiConsole.systemUninstall();
        }
    }

    private static String getRates(Document xml) {

        /*
        double v = 999.123456789;
        double d = -1.123456789;
        System.out.printf("%s %s%n",
                getAnsiString(GREEN, String.format("%+.4f", v)),
                getAnsiString(RED, String.format("%.2f", d)));
        */

        return getAnsiString(WHITE, xml.getDocumentElement().getTagName());
    }
}
