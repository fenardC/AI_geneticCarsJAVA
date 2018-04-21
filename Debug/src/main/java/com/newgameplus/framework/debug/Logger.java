package com.newgameplus.framework.debug;

public class Logger {

    private Logger() {
    }

    public static void debug(final String message) {
        System.out.println(message);
    }
}
