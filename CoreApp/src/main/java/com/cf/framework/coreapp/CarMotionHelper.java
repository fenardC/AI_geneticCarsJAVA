package com.cf.framework.coreapp;

import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.genetic.GeneticDnaNeuralNetwork;
import com.newgameplus.framework.misc.Misc;
import com.newgameplus.framework.neural.NeuralActivation;
import com.newgameplus.framework.neural.NeuralInputValue;
import com.newgameplus.framework.neural.NeuralLayer;
import com.newgameplus.framework.neural.NeuralNetwork;
import com.newgameplus.frameworkdemo.gui.ScreenGeneticCar;
import com.newgameplus.frameworkdemo.misc.Car;
import com.newgameplus.frameworkdemo.misc.Track;

public class CarMotionHelper {
    public CarMotionHelper() {
        super();
    }

    protected NeuralNetwork getNetwork() {
        return network;
    }

    protected void initNetwork() {
        /* Add entry for sensors. */
        for (int i = 0; i < ScreenGeneticCar.SENSOR_LINE_NUMBER; i++) {
            network.addInput(new NeuralInputValue());
        }

        /* Add one more for speed. */
        network.addInput(new NeuralInputValue());

        /* 2 neurons x (8 sensors lines + speed input + threshold) */
        final NeuralLayer layerOuput = new NeuralLayer(NeuralActivation.SIGMOID, NETWORK_NUMBER_OF_NEURONS);
        network.addLayer(layerOuput);
        network.connectAllInputOnFirstLayer();
    }

    protected void moveCar(final Drawer drawer, final Car car) {
        /* ========= AT EACH TICK OF COMPUTATION ========= */
        /* Load AI with DNA of this car. */
        network.initFromDna((GeneticDnaNeuralNetwork) car.getIndiv().getDna());

        /* Update network with line sensor values for inputs */
        for (int i = 0; i < ScreenGeneticCar.SENSOR_LINE_NUMBER; i++) {
            /* Sensors. */
            network.setInputValue(i, car.getSensorValue("s" + i));
        }

        /* Update network with engine values for corresponding input */
        network.setInputValue(NETWORK_ENGINE_INPUT_INDEX, car.getEngine() / 128.0);

        /* Start computation of network. */
        network.calculate();

        /* Get outputs from network. */
        final int engineCommand = Misc.mix(-128.0, 128.0, network.getListResult().get(0).doubleValue());
        final int wheelCommand = Misc.mix(-128.0, 128.0, network.getListResult().get(1).doubleValue());

        /* Update Car with commands */
        car.setEngineCommand(engineCommand);
        car.setWheelCommand(wheelCommand);

        car.tick(CarMotionHelper.CAR_MILLIS_PER_TICK);

        /* Display car. */
        car.render(drawer);
    }

    protected static void displayTrackForCar(final Drawer drawer, final Track track) {
        track.renderDebug(drawer);
        track.renderCenterLine(drawer);
        track.renderStart(drawer);
    }

    protected static void startCarOnTrack(final Track track, final Car car) {
        car.putOnTrack(track);
        car.setLap(0);
        car.setTimeLap(0);
        car.setBestIndexSpline(0);
        /* Give some speed and direction to the car */
        car.setEngineCommand(5);
        car.setWheelCommand(0);
        /* Update all internals of a car. */
        car.tick(0.01);
    }

    protected static final int CAR_MILLIS_PER_TICK = 18;

    protected static final int WORKER_DELAY_IN_MS = 10;

    protected static final double CAR_RATIO = 0.5;

    protected static final int CAR_SENSOR_TYPE = ScreenGeneticCar.TYPE_SENSOR_LINE;

    private static final int NETWORK_ENGINE_INPUT_INDEX = ScreenGeneticCar.SENSOR_LINE_NUMBER;

    private static final int NETWORK_NUMBER_OF_NEURONS = 2;

    protected static final double NETWORK_GENE_VALUE_MAX = 5.0;

    protected static final int NETWORK_GENE_SIZE = 1;

    protected static final int NETWORK_GENE_NUMBER =
        NETWORK_NUMBER_OF_NEURONS * (ScreenGeneticCar.SENSOR_LINE_NUMBER + 1 + 1);

    /* 60 55 50:OK 48: KO 45:KO 42:KO 40:KO */
    protected static final int TRACK_SPLINE_WIDTH = 60;

    private NeuralNetwork network = new NeuralNetwork(NETWORK_GENE_VALUE_MAX);
}
