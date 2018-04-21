package com.newgameplus.frameworkdemo.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.newgameplus.framework.debug.Debugger;
import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.draw.Texture;
import com.newgameplus.framework.draw.TextureModifier;
import com.newgameplus.framework.genetic.GeneticIndividual;
import com.newgameplus.framework.misc.Misc;
import com.newgameplus.framework.misc.Vector2D;
import com.newgameplus.frameworkdemo.draw.ArtInit;
import com.newgameplus.frameworkdemo.gui.ScreenGeneticCar;

public class Car {

    protected int maxIndexTriangleDiff = 10;

    protected Track track;

    protected GeneticIndividual indiv;

    protected HashMap<String, CarSensor> mapRoadSensor = new HashMap<String, CarSensor>();
    protected List<CarSensorPoint> listRoadCollision = new ArrayList<CarSensorPoint>();

    protected int wheelCommand = 0;
    protected int engineCommand = 0;
    protected int engine = 0;

    protected double acceleration = 128;

    protected Vector2D position;
    protected double angle;
    protected double ratio = 1;

    protected static Texture texture = ArtInit.gui[0][12];
    protected TextureModifier textureModifier = new TextureModifier();

    protected int bestIndexSpline = 0;
    protected int lap = 0;
    protected double timeLap = 0;

    protected Color color = null;

    public Car(int typeSensor, double ratio) {
        this.ratio = ratio;

        double w = 50;
        double h = 25;

        addRoadCollision(new Vector2D(w, h));
        addRoadCollision(new Vector2D(w, -h));
        addRoadCollision(new Vector2D(-w, h));
        addRoadCollision(new Vector2D(-w, -h));

        if (typeSensor == ScreenGeneticCar.TYPE_SENSOR_POINT) {
            Vector2D offset = new Vector2D(70, 0);
            Vector2D size = new Vector2D(40, 40);

            for (int i = 0 ; i < ScreenGeneticCar.sensorX ; i++) {
                for (int j = 0 ; j < ScreenGeneticCar.sensorY ; j++) {
                    int a = j - (int) Math.floor(ScreenGeneticCar.sensorY / 2);
                    CarSensorPoint sensor = new CarSensorPoint();
                    sensor.setCar(this);
                    sensor.setOffset(new Vector2D(i * size.x, a * size.y).add(offset));
                    mapRoadSensor.put("s" + i + "_" + j, sensor);
                }
            }
        }
        else {
            for (int i = 0 ; i < ScreenGeneticCar.sensorLineNumber ; i++) {
                CarSensorLine sensor = new CarSensorLine();
                sensor.setCar(this);
                sensor.setAngle(i / (double)(ScreenGeneticCar.sensorLineNumber - 1) * ScreenGeneticCar.sensorLineAngleCover -
                                ScreenGeneticCar.sensorLineAngleCover / 2);
                mapRoadSensor.put("s" + i, sensor);
            }
        }

        textureModifier.setCenter(50, 25);
        textureModifier.setAntialiasing(TextureModifier.ANTIALIASING_HIGH);

    }

    public void addRoadCollision(Vector2D offset) {
        CarSensorPoint sensor = new CarSensorPoint();
        sensor.setCar(this);
        sensor.setOffset(offset);
        listRoadCollision.add(sensor);
    }

    public void putOnTrack(Track track) {
        this.track = track;
        this.position = new Vector2D(track.getSpline().getListResultPoint().get(0));

        //this.angle = 180;
        this.angle = track.getSpline().getListResultPerpendicular().get(0).getOrientation() - 90;
    }

    public void tick(double millis) {

        double m = millis / 1000;

        double ratioWheel = wheelCommand / 128.0;
        ratioWheel = Misc.bound(ratioWheel, -1, 1);

        double accel = acceleration * m;

        if (engineCommand > engine) {
            if (engineCommand > engine + accel) {
                engine += accel;
            }
            else {
                engine = engineCommand;
            }
        }
        else if (engineCommand < engine) {
            if (engineCommand < engine - accel) {
                engine -= accel;
            }
            else {
                engine = engineCommand;
            }
        }

        double ratioEngine = engine / 128.0;
        ratioEngine = Misc.bound(ratioEngine, -1, 1);

        // Move Car
        if (ratioEngine >= 0) {
            angle = angle + ratioWheel * (0.5 + 0.5 * ratioEngine) * 180 * m;
        }
        else {
            angle = angle + ratioWheel * (-0.5 + 0.5 * ratioEngine) * 180 * m;
        }

        position.add(Vector2D.getVector2DFromValueAngle(ratioEngine * 300 * m, angle));

        // Update Sensors
        for (CarSensorPoint sensor : listRoadCollision) {
            sensor.tick(millis);

            if (sensor.getLastIndexTriangle() > -1) {
                if (sensor.getLastIndexTriangle() > bestIndexSpline
                        && sensor.getLastIndexTriangle() <= bestIndexSpline + maxIndexTriangleDiff) {
                    bestIndexSpline = sensor.getLastIndexTriangle();
                }
                else if (sensor.getLastIndexTriangle() <= maxIndexTriangleDiff
                         && track.getListTriangle().size() <= bestIndexSpline + maxIndexTriangleDiff) {
                    bestIndexSpline = sensor.getLastIndexTriangle();
                    lap++;
                }
            }
        }

        for (CarSensor sensor : mapRoadSensor.values()) {
            sensor.tick(millis);
        }

    }

    public void render(Drawer d) {

        textureModifier.setAngle(angle);
        textureModifier.setRatio(ratio);

        if (color != null) {
            textureModifier.setColor(color);
        }

        texture.draw(position.x, position.y, textureModifier);

        if (Debugger.isDebugEnabled("sensor")) {
            for (CarSensorPoint sensor : listRoadCollision) {
                sensor.render(d);
            }

            for (CarSensor sensor : mapRoadSensor.values()) {
                sensor.render(d);
            }
        }

    }

    public boolean isOnTrack() {

        int nb = 0;

        for (CarSensorPoint sensor : listRoadCollision) {
            if (sensor.getValue() > 0) {
                nb++;

                if (nb >= 3) {
                    return true;
                }
            }
        }

        return false;
    }

    public double getSensorValue(String name) {
        if (mapRoadSensor.containsKey(name)) {
            return mapRoadSensor.get(name).getValue();
        }

        return 0;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getWheelCommand() {
        return wheelCommand;
    }

    public void setWheelCommand(int wheelCommand) {
        this.wheelCommand = wheelCommand;
    }

    public int getEngineCommand() {
        return engineCommand;
    }

    public void setEngineCommand(int engineCommand) {
        this.engineCommand = engineCommand;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public GeneticIndividual getIndiv() {
        return indiv;
    }

    public void setIndiv(GeneticIndividual indiv) {
        this.indiv = indiv;
    }

    public double getTrackPercentCompletion() {
        return bestIndexSpline / (double) track.getListTriangle().size() * 100.0;
    }

    public int getLap() {
        return lap;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getTimeLap() {
        return timeLap;
    }

    public void setTimeLap(double timeLap) {
        this.timeLap = timeLap;
    }

    public int getEngine() {
        return engine;
    }

}
