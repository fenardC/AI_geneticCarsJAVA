package com.newgameplus.framework.neural;

public abstract class NeuralActivation {
	
	public static final NeuralActivation HEAVISIDE = new NeuralActivationHeaviside();
	public static final NeuralActivation SIGMOID = new NeuralActivationSigmoid();
	
	public abstract double calculate(double value);
	
}
