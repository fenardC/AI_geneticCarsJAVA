package com.newgameplus.framework.neural;

public class NeuralActivationSigmoid extends NeuralActivation {

    NeuralActivationSigmoid() {
        super();
    }

    @Override
    public double calculate(final double value) {
        return 1 / (1 + Math.exp(-value));
    }
}
