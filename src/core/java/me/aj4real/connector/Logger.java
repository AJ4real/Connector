package me.aj4real.connector;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {
    private static List<Level> allowedLogLevels = new ArrayList<Level>();
    static {
        addAllowedLogLevels(Level.INFO, Level.WARN, Level.ERROR, Level.SEVERE);
    }
    public static void handle(Throwable throwable) {
        //TODO
        throwable.printStackTrace();
    }
    public static void log(String s) {
        log(Level.INFO, s);
    }
    public static void log(Level level, String s) {
        if(!allowedLogLevels.contains(level)) return;
        Date t = Date.from(Instant.now());
        StackTraceElement e = new Exception().getStackTrace()[1];
        System.out.println("[" + t.getHours() + ":" + t.getMinutes() + ":" + t.getSeconds() + " " + level.name() + "] [" + e.getClassName() + ":" + e.getLineNumber() + "] " + s);
    }
    public static void addAllowedLogLevels(Level... levels) {
        for (Level level : levels) {
            allowedLogLevels.add(level);
        }
    }
    public static void removeAllowedLogLevels(Level... levels) {
        for (Level level : levels) {
            allowedLogLevels.remove(level);
        }
    }

    public enum Level {
        INFO,
        DEBUG,
        WARN,
        ERROR,
        SEVERE;
    }
}
