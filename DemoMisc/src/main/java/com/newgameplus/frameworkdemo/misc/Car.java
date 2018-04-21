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
import com.newgameplus.frameworkdemo.gui.ScreenGeneticCar;

public final class Car {
    private String name;
    private int maxIndexTriangleDiff = 10;
    private Track track;
    private GeneticIndividual indiv;
    private HashMap<String, CarSensor> mapRoadSensor = new HashMap<>();
    private List<CarSensorPoint> listRoadCollision = new ArrayList<>();
    private int wheelCommand = 0;
    private int engineCommand = 0;
    private int engine = 0;
    private double acceleration = 128;
    private Vector2D position;
    private double angle;
    private double ratio = 1;
    private TextureModifier textureModifier = new TextureModifier();
    private int bestIndexSpline = 0;
    private int lap = 0;
    private double timeLap = 0;
    private Color color = null;

    public Car(String name, int typeSensor, double ratio) {

        Texture.init();
        this.name = name;
        this.ratio = ratio;

        final double w = 50;
        final double h = 25;

        addRoadCollision(new Vector2D(w, h));
        addRoadCollision(new Vector2D(w, -h));
        addRoadCollision(new Vector2D(-w, h));
        addRoadCollision(new Vector2D(-w, -h));

        if (typeSensor == ScreenGeneticCar.TYPE_SENSOR_POINT) {
            Vector2D offset = new Vector2D(70, 0);
            Vector2D size = new Vector2D(40, 40);

            for (int i = 0; i < ScreenGeneticCar.SENSOR_X; i++) {
                for (int j = 0; j < ScreenGeneticCar.SENSOR_Y; j++) {
                    int a = j - (int) Math.floor(ScreenGeneticCar.SENSOR_Y / 2.0);
                    CarSensorPoint sensor = new CarSensorPoint();
                    sensor.setCar(this);
                    sensor.setOffset(new Vector2D(i * size.getX(), a * size.getY()).add(offset));
                    mapRoadSensor.put("s" + i + "_" + j, sensor);
                }
            }
        }
        else {
            for (int i = 0; i < ScreenGeneticCar.SENSOR_LINE_NUMBER; i++) {
                CarSensorLine sensor = new CarSensorLine();
                sensor.setCar(this);
                sensor.setAngle(i / (double)(ScreenGeneticCar.SENSOR_LINE_NUMBER - 1)
                                * ScreenGeneticCar.SENSOR_LINE_ANGLE_COVER - ScreenGeneticCar.SENSOR_LINE_ANGLE_COVER / 2);
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

    public double getAngle() {
        return angle;
    }

    public Color getColor() {
        return color;
    }

    public int getEngine() {
        return engine;
    }

    public int getEngineCommand() {
        return engineCommand;
    }

    public GeneticIndividual getIndiv() {
        return indiv;
    }

    public int getLap() {
        return lap;
    }

    public String getName() {
        return name;
    }

    public Vector2D getPosition() {
        return position;
    }

    public double getRatio() {
        return ratio;
    }

    public double getSensorValue(String name) {
        if (mapRoadSensor.containsKey(name)) {
            return mapRoadSensor.get(name).getValue();
        }

        return 0;
    }

    public double getTimeLap() {
        return timeLap;
    }

    public Track getTrack() {
        return track;
    }

    public double getTrackPercentCompletion() {
        return 100 * bestIndexSpline / (double) track.getListTriangle().size();
    }

    public int getWheelCommand() {
        return wheelCommand;
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

    public void putOnTrack(Track track) {
        this.track = track;
        this.position = new Vector2D(track.getSpline().getListResultPoint().get(0));
        /* Start in the same direction as the index of the list grows !!!!!! */
        this.angle = track.getSpline().getListResultPerpendicular().get(0).getOrientation() - 90;
    }

    public void render(Drawer d) {
        renderCarOnly(d);

        if (Debugger.isDebugEnabled("sensor")) {
            for (CarSensorPoint sensor : listRoadCollision) {
                sensor.render(d);
            }

            for (CarSensor sensor : mapRoadSensor.values()) {
                sensor.render(d);
            }
        }
    }

    private void renderCarOnly(Drawer d) {
        textureModifier.setAngle(angle);
        textureModifier.setRatio(ratio);

        if (color != null) {
            textureModifier.setColor(color);
        }

        Texture.draw(d, position.getX(), position.getY(), textureModifier);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setBestIndexSpline(int bestIndexSpline) {
        this.bestIndexSpline = bestIndexSpline;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setEngineCommand(int engineCommand) {
        this.engineCommand = engineCommand;
    }

    public void setIndiv(GeneticIndividual indiv) {
        this.indiv = indiv;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public void setTimeLap(double timeLap) {
        this.timeLap = timeLap;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public void setWheelCommand(int wheelCommand) {
        this.wheelCommand = wheelCommand;
    }

    protected void testRender(Drawer d) {
        renderCarOnly(d);
    }

    protected void testTick(double millis) {
        tickCarOnly(millis);
    }

    public void tick(double millis) {

        tickCarOnly(millis);

        /* Update Sensors */
        for (CarSensorPoint sensor : listRoadCollision) {
            sensor.tick();
            final int sensorLastIndexTriangle = sensor.getLastIndexTriangle();

            if (sensorLastIndexTriangle > -1) {
                if (sensorLastIndexTriangle > bestIndexSpline
                        && sensorLastIndexTriangle <= bestIndexSpline + maxIndexTriangleDiff) {
                    bestIndexSpline = sensorLastIndexTriangle;
                }
                else if (sensorLastIndexTriangle <= maxIndexTriangleDiff
                         && track.getListTriangle().size() <= bestIndexSpline + maxIndexTriangleDiff) {
                    bestIndexSpline = sensorLastIndexTriangle;
                    lap++;
                }
            }
        }

        for (CarSensor sensor : mapRoadSensor.values()) {
            sensor.tick();
        }
    }

    private void tickCarOnly(double millis) {
        final double m = millis / 1000;
        final double ratioWheel = Misc.bound(wheelCommand / 128.0, -1, 1);
        final double accel = acceleration * m;

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

        final double ratioEngine = Misc.bound(engine / 128.0, -1, 1);

        /* Move Car */
        if (ratioEngine >= 0) {
            angle = angle + ratioWheel * (0.5 + 0.5 * ratioEngine) * 180 * m;
        }
        else {
            angle = angle + ratioWheel * (-0.5 + 0.5 * ratioEngine) * 180 * m;
        }

        position.add(Vector2D.getVector2DFromValueAngle(ratioEngine * 300 * m, angle));
    }
}
