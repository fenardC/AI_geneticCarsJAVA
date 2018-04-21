package com.newgameplus.frameworkdemo.misc;

import java.awt.Color;

import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.misc.Vector2D;

public class CarSensorPoint extends CarSensor {

    protected Vector2D offset;

    protected boolean value = false;

    protected int lastIndexTriangle = 0;

    @Override
    public void check() {
        value = false;

        Vector2D pos = getAbsPosition();

        if ((lastIndexTriangle = car.getTrack().isPointInTrack(pos)) != -1) {
            value = true;
        }
        else {
            /*pos.x += 0.1;
            if (car.getTrack().isPointInTrack(pos)) {
                value = 1;
            }*/
        }

    }

    @Override
    public void render(Drawer d) {
        Vector2D pos = getAbsPosition();

        d.setColor(value ? Color.BLUE : Color.RED);
        d.drawRect((int) pos.x, (int) pos.y, 3, 3);
    }

    public Vector2D getAbsPosition() {
        return Vector2D.add(car.getPosition(), Vector2D.rotate(offset, car.getAngle()).multiply(car.getRatio()));
    }

    public Vector2D getOffset() {
        return offset;
    }

    public void setOffset(Vector2D offset) {
        this.offset = offset;
    }

    @Override
    public double getValue() {
        return value ? 1.0 : 0.0;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public int getLastIndexTriangle() {
        return lastIndexTriangle;
    }

}
