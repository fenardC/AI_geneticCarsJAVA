package com.newgameplus.frameworkdemo.misc;

import com.newgameplus.framework.draw.Drawer;

public abstract class CarSensor {
	
	protected Car car;
	
	
	public void tick(double millis) {
		check();
	}
	
	public void check() {
		
	}
	
	public void render(Drawer d) {
		
	}
	
	public abstract double getValue();
	
	public Car getCar() {
		return car;
	}
	
	public void setCar(Car car) {
		this.car = car;
	}
	
}
