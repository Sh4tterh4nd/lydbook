package ch.fhnw.webec.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Audiobook util.
 */
public class AudiobookUtil {
    //https://regex101.com/r/18u6or/3
    private static Pattern fileExtensionPattern = Pattern.compile("(?<extension>\\.[^\\.]*$)");
    private static Pattern stringNumericPattern = Pattern.compile("^(\\d+\\.*\\d*)|(\\d*\\.\\d+)$");

    /**
     * Gets file ending, empty if there is no datatype
     *
     * @param filename the filename
     * @return the file ending
     */
    public static String getFileEnding(String filename) {
        Matcher matcher = fileExtensionPattern.matcher(filename);
        if (matcher.find()) {
            return matcher.group("extension");
        }
        return "";
    }

    /**
     * returns true or false if the string is numeric, decimal or int doesn't matter
     *
     * @param value the value
     * @return the boolean
     */
    public static boolean isNumeric( String value){
        Matcher matcher = stringNumericPattern.matcher(value);
        return matcher.matches();
    }
}
