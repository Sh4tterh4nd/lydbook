package ch.fhnw.webec.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AudiobookUtil {
    //https://regex101.com/r/18u6or/3
    private static Pattern fileExtensionPattern = Pattern.compile("(?<extension>\\.[^\\.]*$)");
    private static Pattern stringNumericPattern = Pattern.compile("^(\\d+\\.*\\d*)|(\\d*\\.\\d+)$");

    public static String getFileEnding(String filename) {
        Matcher matcher = fileExtensionPattern.matcher(filename);
        if (matcher.find()) {
            return matcher.group("extension");
        }
        return "";
    }

    public static boolean isNumeric( String value){
        Matcher matcher = stringNumericPattern.matcher(value);
        return matcher.matches();
    }
}
