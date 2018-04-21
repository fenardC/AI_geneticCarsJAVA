package com.newgameplus.framework.neural;

public class NeuralInputValue extends NeuralInput {

    private double value;

    public NeuralInputValue() {
        super();
        /* Add more if needed. */
    }

    public NeuralInputValue(double value) {
        this();
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
        this.value = value;
    }

}
