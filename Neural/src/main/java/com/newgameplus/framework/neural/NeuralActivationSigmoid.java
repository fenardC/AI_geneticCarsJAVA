package com.newgameplus.framework.neural;

public class NeuralActivationSigmoid extends NeuralActivation {
	
	@Override
	public double calculate(double value) {
		return 1 / (1 + Math.exp(-value));
	}
	
}
