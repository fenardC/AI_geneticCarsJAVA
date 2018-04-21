package com.newgameplus.frameworkdemo.misc;

import java.awt.Color;

import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.misc.Vector2D;
import com.newgameplus.frameworkdemo.gui.ScreenGeneticCar;

public class CarSensorLine extends CarSensor {

    protected double angle = 0;

    protected double dist = 0;
    protected double value = 0;


    @Override
    public void check() {

        double range = ScreenGeneticCar.sensorLineRange;

        //dist = getDist(range / 2, range / 2);
        dist = 0;

        for (double myDist = 0 ; myDist <= range ; myDist += ScreenGeneticCar.sensorLinePrecision) {
            Vector2D pos = Vector2D.add(getAbsPosition(), Vector2D.getVector2DFromValueAngle(myDist, getAbsAngle()));

            if (car.getTrack().isPointInTrack(pos) != -1) {
                dist = myDist;
            }
            else {
                break;
            }
        }

        value = dist / 100.0;

    }

    @Override
    public void render(Drawer d) {
        Vector2D p1 = getAbsPosition();
        Vector2D p2 = Vector2D.add(p1, Vector2D.getVector2DFromValueAngle(dist, getAbsAngle()));

        d.setColor(Color.BLUE);
        d.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public double getDist(double distToCheck, double rangeStep) {
        if (rangeStep > 2) {
            Vector2D pos = Vector2D.add(getAbsPosition(), Vector2D.getVector2DFromValueAngle(distToCheck, getAbsAngle()));

            if (car.getTrack().isPointInTrack(pos) != -1) {
                return getDist(distToCheck + rangeStep / 2, rangeStep / 2);
            }
            else {
                return getDist(distToCheck - rangeStep / 2, rangeStep / 2);
            }
        }
        else {
            return distToCheck;
        }
    }

    public Vector2D getAbsPosition() {
        return new Vector2D(car.getPosition());
    }

    public double getAbsAngle() {
        return car.getAngle() + angle;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getValue() {
        return value;
    }

}
