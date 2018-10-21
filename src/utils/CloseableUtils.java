package utils;

import java.io.Closeable;
import java.io.IOException;

public class CloseableUtils {

    public static void close(Closeable obj) {
        if (obj == null) {
            return;
        }
        try {
            obj.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
