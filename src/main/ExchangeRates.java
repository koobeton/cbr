package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExchangeRates {

    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String REQUEST_METHOD = "GET";
    private static final String EMPTY_STRING = "";

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

        URL url;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String line, result;

        try {
            for (Currency currency : Currency.values()) {
                result = EMPTY_STRING;
                if (currency.getCbrCode() != null) {
                    url = new URL(String.format("%s%s", cbrURL, currency.getCbrCode()));
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(REQUEST_METHOD);
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                    reader.close();
                    connection.disconnect();
                    System.out.printf("%s:%n%s%n", currency.name(), result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) connection.disconnect();
        }
    }
}
