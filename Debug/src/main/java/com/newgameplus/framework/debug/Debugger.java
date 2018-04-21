package com.newgameplus.framework.debug;

public class Debugger {

    private static final String SENSOR = "sensor";

    private Debugger() {

    }

    public static boolean isDebugEnabled(String string) {
        boolean result = false;

        if (SENSOR.equals(string)) {
            result = true;
        }

        return result;
    }
}
