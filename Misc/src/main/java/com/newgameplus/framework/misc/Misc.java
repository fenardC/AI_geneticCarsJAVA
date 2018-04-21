package com.newgameplus.framework.misc;

import java.awt.Color;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class Misc {

    public static double bound(final double val, final int low, final int high) {
        double result = val;

        if (val > high) {
            result = high;
        }
        else if (val < low) {
            result = low;
        }

        return result;
    }

    public static int mix(final double min, final double max, final double value) {
        return (int)(value * (max - min) + min);
    }

    public static Color mix(final int fromRgbMin, final int toRgbMax, final double ratio) {
        final Color color1 = new Color(fromRgbMin);
        final Color color2 = new Color(toRgbMax);
        final double ratioComplement = 1 - ratio;

        final int red = (int)(color2.getRed() * ratio  + color1.getRed() * ratioComplement);
        final int green = (int)(color2.getGreen() * ratio + color1.getGreen() * ratioComplement);
        final int blue = (int)(color2.getBlue() * ratio + color1.getBlue() * ratioComplement);
        return new Color(red, green, blue);
    }

    public static boolean random(final double chanceInPercent) {
        final double randomInPercent = generator.nextInt(100);
        return chanceInPercent > randomInPercent ? true : false;
    }

    public static double random(final double min, final double max) {
        return generator.doubles(1, min, max).toArray()[0];
    }

    public static int random(final int min, final int max) {
        return generator.ints(1, min, max).toArray()[0];
    }

    public static Object randomInWeightedMap(final Map<Object, Double> map) {
        final Set<Object> keys = map.keySet();
        final int index = generator.nextInt(keys.size());
        return keys.toArray()[index];
    }

    /* One single random generator for all random numbers. */
    private static Random generator = new Random();

    private Misc() {
        super();
        /*
         * Utility classes, which are a collection of static members, are not
         * meant to be instantiated. Even abstract utility classes, which can be
         * extended, should not have public constructors.
         */
    }
}
