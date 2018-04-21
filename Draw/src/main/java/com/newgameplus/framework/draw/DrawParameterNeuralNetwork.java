package com.newgameplus.framework.draw;

import java.awt.Color;

public final class DrawParameterNeuralNetwork {

    public DrawParameterNeuralNetwork() {
        this(2);
    }

    public DrawParameterNeuralNetwork(int scaler) {
        super();
        this.scaler = scaler;
    }

    public static int getColorActivated() {
        return Color.BLUE.getRGB();
    }

    public static int getColorDesactivated() {
        return Color.CYAN.getRGB();
    }

    public Color getColorLink() {
        return Color.PINK;
    }

    public int getRatioNeuron() {
        return 1 * scaler;
    }

    public int getRatioX() {
        return 1 * scaler;
    }

    public int getRatioY() {
        return 1 * scaler;
    }

    private int scaler = 1;
}
