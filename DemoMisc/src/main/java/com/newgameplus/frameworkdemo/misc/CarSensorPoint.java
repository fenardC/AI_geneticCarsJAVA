package com.newgameplus.frameworkdemo.misc;

import java.awt.Color;

import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.misc.Vector2D;

final class CarSensorPoint extends CarSensor {
    CarSensorPoint() {
    }

    @Override
    public void check() {
        value = false;
        final Vector2D pos = getAbsPosition();

        lastIndexTriangle = car.getTrack().isPointInTrack(pos);

        if (lastIndexTriangle != -1) {
            value = true;
        }
    }

    public Vector2D getAbsPosition() {
        return Vector2D.add(car.getPosition(),
                            Vector2D.rotate(offset, car.getAngle()).multiply(car.getRatio()));
    }

    public int getLastIndexTriangle() {
        return lastIndexTriangle;
    }

    @Override
    public double getValue() {
        return value ? 1.0 : 0.0;
    }

    @Override
    public void render(final Drawer drawer) {
        final Vector2D pos = getAbsPosition();
        drawer.setColor(value ? Color.BLUE : Color.RED);
        drawer.drawRect(pos.getX(), pos.getY(), 3, 3);
    }

    public void setOffset(final Vector2D offset) {
        this.offset = offset;
    }

    private Vector2D offset;

    private boolean value = false;

    private int lastIndexTriangle = 0;
}
