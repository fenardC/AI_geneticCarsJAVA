package com.newgameplus.frameworkdemo.gui;

public class ScreenGeneticCar {
    public static final int TYPE_SENSOR_POINT = 0;
    public static final int TYPE_SENSOR_LINE = 1;

    /* Eight sensors per car. */
    public static final int SENSOR_LINE_NUMBER = 8;
    public static final double SENSOR_LINE_ANGLE_COVER = 180;
    public static final double SENSOR_LINE_RANGE = 100;
    public static final double SENSOR_LINE_PRECISION = 0.5;

    /* Not relevant since TYPE_SENSOR_LINE in use. */
    public static final int SENSOR_X = 0;
    public static final int SENSOR_Y = 0;

    private ScreenGeneticCar() {
    }
}
