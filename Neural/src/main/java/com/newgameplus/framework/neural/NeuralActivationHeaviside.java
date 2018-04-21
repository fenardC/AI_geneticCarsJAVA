package com.newgameplus.framework.neural;

public class NeuralActivationHeaviside extends NeuralActivation {

    NeuralActivationHeaviside() {
        super();
    }

    @Override
    public double calculate(final double value) {
        return value >= 0 ? 1 : 0;
    }
}
