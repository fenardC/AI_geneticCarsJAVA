package com.newgameplus.framework.neural;

public class NeuralActivationHeaviside extends NeuralActivation {
	
	@Override
	public double calculate(double value) {
		return value>=0?1:0;
	}
	
}
