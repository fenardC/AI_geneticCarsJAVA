package com.cf.framework.apprace;

import java.awt.Color;

import com.cf.framework.apprace.CarParameters.NetworkWeights;
import com.cf.framework.coreapp.CarMotionHelper;
import com.newgameplus.framework.debug.Logger;
import com.newgameplus.framework.draw.DrawParameterNeuralNetwork;
import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.genetic.GeneticDnaNeuralNetwork;
import com.newgameplus.framework.genetic.GeneticGeneDouble;
import com.newgameplus.framework.genetic.GeneticIndividual;
import com.newgameplus.framework.misc.BezierSpline2D;
import com.newgameplus.frameworkdemo.misc.Car;
import com.newgameplus.frameworkdemo.misc.Track;

final class CarRacingWorker extends CarMotionHelper {

    private class Worker extends Thread {

        @Override
        public void run() {
            Logger.debug(isDaemon() ? "Deamon Thread" : "User Thread (none-deamon)" + " is starting");

            while (onRunning) {
                try {
                    Thread.sleep(WORKER_DELAY_IN_MS);
                }
                catch (InterruptedException e) {
                    Logger.debug("run() " + e);
                    Thread.currentThread().interrupt();
                }

                doCarRacing(drawer);
            }

            Logger.debug("Worker thread ends.");
        }

        public void startRunning() {
            onRunning = true;
            super.start();
        }

        public void stopRunning() {
            onRunning = false;
        }

        private volatile boolean onRunning = true;

        /*
         * daemon
         * When false, (i.e. when it's a user thread), the Worker thread
         * continues to run. When true, (i.e. when it's a daemon thread), the
         * Worker thread terminates when the main thread terminates.
         */

        private int lapCount = 0;
        private double trackCompletion = 0;

        private Worker(boolean daemon) {
            super(CarRacingWorker.class.getName());
            setDaemon(daemon);
        }

        private void displayStatistics() {
            final int posX = 750;
            drawer.setColor(Color.GREEN);
            drawer.drawString(currentTrack.getName() + " (" + TRACK_SPLINE_WIDTH + ")" +
                              "/" + CAR_MILLIS_PER_TICK + "/" + WORKER_DELAY_IN_MS, posX, 20);
            drawer.drawString("PERCENT : " + trackCompletion, posX, 40);
            drawer.drawString("LAPS       : " + lapCount, posX, 60);
        }

        private void doCarRacing(final Drawer drawer) {
            /* Clear graphics of drawer. */
            drawer.clear();
            /* Some progress informations on screen*/
            displayStatistics();

            /* The track on screen. */
            displayTrackForCar(drawer, currentTrack);

            /* For debug. */
            currentTrack.renderSplineDebug(drawer);

            /* Update car on track. */
            moveCar(drawer, car);

            /* Display neurons */
            if (NEURAL_NETWORK_DRAW) {
                final int neuronPosX = 750;
                final int neuronPosY = 80;

                final DrawParameterNeuralNetwork param = new DrawParameterNeuralNetwork(4);
                getNetwork().render(drawer, neuronPosX, neuronPosY, param);
            }

            trackCompletion = car.getTrackPercentCompletion();
            lapCount = car.getLap();
            drawer.show();
        }
    }

    /*********************************************************************************/
    CarRacingWorker(boolean daemon, Drawer drawer) {
        super();

        /* Super class creates the neural network. */
        initNetwork();

        this.drawer = drawer;
        worker = new Worker(daemon);

        init();
    }

    public void startRunning() {
        worker.startRunning();
    }

    public void stopRunning() {
        worker.stopRunning();
    }

    private void init() {
        /* Prepare track for the car. */
        final TrackDataRace trackDataRace = new TrackDataRace();
        final BezierSpline2D spline = new BezierSpline2D(trackDataRace.getPointsAndPerpList());
        trackDataRace.debug();

        currentTrack = new Track(trackDataRace.getClass().getName(), spline, TRACK_SPLINE_WIDTH,
                                 spline.getListResultPoint().size() * 100 * CAR_MILLIS_PER_TICK);

        final CarParameters carParameters = new CarParameters();
        final NetworkWeights weights = carParameters.getNext();
        Logger.debug(weights.getName());

        /* 2 neurons x (8 sensors lines + speed input + threshold) */
        final GeneticDnaNeuralNetwork dnaNetwork =
            new GeneticDnaNeuralNetwork(-NETWORK_GENE_VALUE_MAX, NETWORK_GENE_VALUE_MAX,
                                        NETWORK_GENE_SIZE, NETWORK_GENE_NUMBER);

        for (int i = 0; i < NETWORK_GENE_NUMBER; i++) {
            final GeneticGeneDouble geneDouble =
                new GeneticGeneDouble(-NETWORK_GENE_VALUE_MAX, NETWORK_GENE_VALUE_MAX,
                                      NETWORK_GENE_SIZE);
            geneDouble.getCode().add(new Double(weights.getValue()[i]));
            dnaNetwork.getListGene().add(geneDouble);
        }

        /* Initialize weight of neurons with values from learning. */
        final GeneticIndividual indiv = new GeneticIndividual(dnaNetwork.clone());

        /* ============ AT BEGINING OF RACE ============ */
        /* Create the car provided the genetic. */
        car = new Car("TheCar", CAR_SENSOR_TYPE, CAR_RATIO);
        car.setColor(Color.RED);
        car.setIndiv(indiv);
        startCarOnTrack(currentTrack, car);
    }

    private static final boolean NEURAL_NETWORK_DRAW = true;

    private Drawer drawer;

    private Track currentTrack;

    private Car car;

    private Worker worker;
}
