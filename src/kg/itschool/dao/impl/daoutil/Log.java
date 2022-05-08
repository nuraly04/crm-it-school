package kg.itschool.dao.impl.daoutil;

import java.time.LocalDateTime;

public class Log {

    public static String info (String className, String targetName, String message) {
        String.format("%s[INFO] ----- %s ----- %s ----- %s", LocalDateTime.now(), className, targetName, message);
    }

    public static String error (String className, String targetName, String message) {
        String.format("%s[INFO] ----- %s ----- %s ----- %s", LocalDateTime.now(), className, targetName, message);
    }
}
