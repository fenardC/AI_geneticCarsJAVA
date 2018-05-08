package com.newgameplus.frameworkdemo.misc;

import com.newgameplus.framework.draw.Drawer;

abstract class CarSensor {

    void check() {
    }

    abstract double getValue();

    void render(Drawer d) {
    }

    void setCar(final Car car) {
        this.car = car;
    }

    void tick() {
        check();
    }

    Car car;
}
