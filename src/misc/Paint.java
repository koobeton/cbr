package misc;

import static org.fusesource.jansi.Ansi.ansi;

public class Paint {

    public static final String RED = "fg_red,intensity_bold";
    public static final String YELLOW = "fg_yellow,intensity_bold";
    public static final String GREEN = "fg_green,intensity_bold";
    public static final String MAGENTA = "fg_magenta,intensity_bold";
    public static final String WHITE = "fg_white,intensity_bold";

    public static String getAnsiString(String attributes, String text) {

        return ansi().render(String.format("@|%s %s|@", attributes, text)).toString();
    }
}
