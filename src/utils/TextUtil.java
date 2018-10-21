package utils;

public class TextUtil {

    public static boolean isEmpty(String content) {
        if (content == null) {
            return true;
        }
        return content.trim().equals("");
    }
}
