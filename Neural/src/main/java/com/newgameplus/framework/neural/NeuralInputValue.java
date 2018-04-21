package com.newgameplus.framework.neural;

public class NeuralInputValue extends NeuralInput {
	
	protected double value;
	
	
	public NeuralInputValue() {
		
	}
	
	public NeuralInputValue(double value) {
		this();
		this.value = value;
	}
	
	
	@Override
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
}
