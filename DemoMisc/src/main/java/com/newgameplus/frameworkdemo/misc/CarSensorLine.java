package com.newgameplus.frameworkdemo.misc;

import java.awt.Color;

import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.misc.Vector2D;
import com.newgameplus.frameworkdemo.gui.ScreenGeneticCar;

final class CarSensorLine extends CarSensor {
    CarSensorLine() {
        super();
    }

    @Override
    void check() {
        final double range = ScreenGeneticCar.SENSOR_LINE_RANGE;
        final Vector2D absPosition = getAbsPosition();
        final double absAngle = getAbsAngle();

        dist = 0;


        for (double myDist = 0; myDist <= range; myDist += ScreenGeneticCar.SENSOR_LINE_PRECISION) {
            final Vector2D pos =
                Vector2D.add(absPosition, Vector2D.getVector2DFromValueAngle(myDist, absAngle));

            if (car.getTrack().isPointInTrack(pos) != -1) {
                dist = myDist;
            }
            else {
                break;
            }
        }

        value = dist / 100.0;
    }

    double getAbsAngle() {
        return car.getAngle() + angle;
    }

    Vector2D getAbsPosition() {
        return new Vector2D(car.getPosition());
    }

    double getAngle() {
        return angle;
    }

    @Override
    double getValue() {
        return value;
    }

    @Override
    void render(final Drawer drawer) {
        final Vector2D pos1 = getAbsPosition();
        final Vector2D pos2 = Vector2D.add(pos1, Vector2D.getVector2DFromValueAngle(dist, getAbsAngle()));
        drawer.setColor(Color.BLUE);
        drawer.drawLine(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
    }

    void setAngle(final double angle) {
        this.angle = angle;
    }

    private double angle = 0;

    private double dist = 0;

    private double value = 0;
}
