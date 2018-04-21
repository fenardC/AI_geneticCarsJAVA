package com.cf.framework.applearning;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cf.framework.coreapp.CarMotionHelper;
import com.cf.framework.trackdata.TrackData;
import com.newgameplus.framework.debug.Logger;
import com.newgameplus.framework.draw.DrawParameterNeuralNetwork;
import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.genetic.GeneticIndividual;
import com.newgameplus.framework.genetic.GeneticPopulation;
import com.newgameplus.framework.misc.BezierSpline2D;
import com.newgameplus.framework.misc.Vector2D;
import com.newgameplus.frameworkdemo.misc.Car;
import com.newgameplus.frameworkdemo.misc.Track;

final class CarsLearningWorker extends CarMotionHelper {

    private class Worker extends Thread {
        private volatile boolean onRunning = true;

        /*
         * daemon:
         * When false, (i.e. when it's a user thread), the Worker thread
         * continues to run. When true, (i.e. when it's a daemon thread), the
         * Worker thread terminates when the main thread terminates.
         */
        private int learningCount = 0;
        private boolean isOnSameTrack = false;
        private int validCarAmount = 0;
        private double validCarTrackCompletionBest = 0;
        private double trackCompletionBest = 0;
        private double carScoreBest = 0;

        private Worker(boolean daemon) {
            super(CarsLearningWorker.class.getName());
            setDaemon(daemon);
        }

        private void checkAndChangeTrack() {
            isOnSameTrack = false;
            trackListIndex++;

            if (trackListIndex > (trackList.size() - 1)) {
                trackListIndex = 0;
                learningCount++;
            }

            currentTrack = trackList.get(trackListIndex);
            trackCompletionBest = 0;

            if (learningCount > 4) {
                stopRunning();
            }
        }

        private void updateValidCarStatistics(double trackCompletion, double score) {

            validCarAmount++;

            if (trackCompletion > trackCompletionBest) {
                trackCompletionBest = trackCompletion;
            }

            if (trackCompletion > validCarTrackCompletionBest) {
                validCarTrackCompletionBest = trackCompletion;
            }

            if (score > carScoreBest) {
                carScoreBest = score;
            }
        }

        private void displayStatistics() {
            final int posX = 800;
            drawer.setColor(Color.GREEN);
            drawer.drawString(currentTrack.getName() + " (" + TRACK_SPLINE_WIDTH + ")" +
                              "/" + CAR_MILLIS_PER_TICK + "/" + WORKER_DELAY_IN_MS, posX, 20);
            drawer.drawString("LEARNING           : " + learningCount, posX, 40);
            drawer.drawString("GENERATION      : " + population.getGenerationNumber(), posX, 60);
            drawer.drawString("NB CARS             : " + validCarAmount, posX, 80);
            drawer.drawString("SCORE (BEST)    : " + carScoreBest, posX, 100);
            drawer.drawString("PERCENT (BEST): " + trackCompletionBest, posX, 120);
            drawer.drawString("PERCENT            : " + validCarTrackCompletionBest, posX, 140);
        }

        private void doCarLearning() {
            /* Clear graphics of drawer. */
            drawer.clear();
            /* Some progress informations on screen*/
            displayStatistics();

            /* The track on screen. */
            displayTrackForCar(drawer, currentTrack);

            /* For debug. */
            currentTrack.renderSplineDebug(drawer);

            isOnSameTrack = true;
            validCarAmount = 0;
            validCarTrackCompletionBest = 0;
            int validCarNoProgress = 0;

            for (Car car : carList) {
                if (isOnSameTrack && car.isOnTrack()) {
                    /* Update car on track. */
                    moveCar(drawer, car);

                    /* Check if the car is actually moving. */
                    final boolean noProgress = isNoProgress(car.getEngine());

                    if (noProgress) {
                        validCarNoProgress++;
                    }

                    /* Display neurons */
                    if (NEURAL_NETWORK_DRAW) {
                        Vector2D carPos = car.getPosition();
                        DrawParameterNeuralNetwork param = new DrawParameterNeuralNetwork();
                        getNetwork().render(drawer, carPos.getX(), carPos.getY(), param);
                    }

                    double trackCompletion = car.getTrackPercentCompletion();

                    if (trackCompletion < 3.0) {
                        trackCompletion = 0;
                    }

                    double score = car.getIndiv().getScore() + (noProgress ? 0 : trackCompletion);

                    updateValidCarStatistics(trackCompletion, score);

                    /* ========= AT END OF EACH RACE ================ */
                    /* For each car set the score. */
                    car.getIndiv().setScore(score);

                    if (score > carScoreBest) {
                        car.setColor(Color.RED);
                    }

                    if (trackCompletion > 99.1) {
                        car.getIndiv().setScore(score + 2000);

                        Logger.debug("GENERATION : " + population.getGenerationNumber());
                        Logger.debug("TRACK   " + currentTrack.getName());
                        Logger.debug("CAR     " + car.getName());
                        Logger.debug("SCORE   " + car.getIndiv().getScore());
                        Logger.debug("PERCENT (BEST): " + trackCompletionBest + "\n");
                        Logger.debug("PERCENT " + trackCompletion + "\n");
                        Logger.debug(car.getIndiv().toString());

                        checkAndChangeTrack();
                    }
                }
            }

            drawer.show();

            if (!isOnSameTrack || (validCarAmount == validCarNoProgress)) {
                /* Create new generation. */
                population.proceedNextGeneration();
                Logger.debug("GENERATION : " + population.getGenerationNumber());

                for (int index = 0, carListSize = carList.size(); index < carListSize; index++) {
                    final Car car = carList.get(index);
                    car.setIndiv(population.getListIndividual().get(index));
                    startCarOnTrack(currentTrack, car);
                }
            }
        }

        private boolean isNoProgress(int carEngine) {
            boolean result = false;

            if ((-1 <= carEngine) && (carEngine <= 1)) {
                result = true;
            }

            return result;
        }

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

                doCarLearning();
            }

            drawer.clear();
            drawer.setColor(Color.WHITE);
            drawer.drawString("LEARNING APPLICATION FINISHED", 500, 500);
            drawer.show();
            Logger.debug("Worker thread ends.");
        }

        public void startRunning() {
            onRunning = true;
            super.start();
        }

        public void stopRunning() {
            onRunning = false;
        }
    }

    /*********************************************************************************/
    private static final int CAR_POPULATION_SIZE = 200;
    private static final boolean NEURAL_NETWORK_DRAW = false;

    private Worker worker;

    private Drawer drawer;

    private List<Track> trackList = new ArrayList<>();
    private int trackListIndex = 0;
    private Track currentTrack;
    private List<Car> carList = new ArrayList<>();
    private GeneticPopulation population;

    protected CarsLearningWorker(boolean daemon, Drawer drawer) {
        /* Super class creates the neural network. */
        initNetwork();

        this.drawer = drawer;
        worker = new Worker(daemon);

        init();
    }

    private void init() {
        /* Prepare track for the cars. */
        final List<TrackData> trackDataList = new ArrayList<>();

        trackDataList.add(new TrackData1());
        trackDataList.add(new TrackData2());
        trackDataList.add(new TrackData3());
        trackDataList.add(new TrackData4());

        for (TrackData t : trackDataList) {
            /* For debug */
            t.debug();
            BezierSpline2D spline = new BezierSpline2D(t.getPointsAndPerpList());

            trackList.add(new Track(t.getName(),
                                    spline, TRACK_SPLINE_WIDTH, spline.getListResultPoint().size() * 100 *
                                    CAR_MILLIS_PER_TICK));
        }

        trackDataList.clear();

        currentTrack = trackList.get(0);

        population = new GeneticPopulation();
        /* Initialize weight of neurons with random values. */
        population.generatePopulation(CAR_POPULATION_SIZE, getNetwork().generateDnaModel().randomDna());

        /* ============ AT EACH BEGINING OF RACE ============ */
        /* Create the cars provided the genetic. */
        Random colorMaker = new Random();
        int i = 0;

        for (GeneticIndividual indiv : population.getListIndividual()) {
            Car car = new Car("Car_" + i++, CAR_SENSOR_TYPE, CAR_RATIO);
            Color color = new Color(colorMaker.nextInt(256), colorMaker.nextInt(256), colorMaker.nextInt(256));
            car.setColor(color);
            car.setIndiv(indiv);
            carList.add(car);
            startCarOnTrack(currentTrack, car);
        }
    }

    public void startRunning() {
        worker.startRunning();
    }

    public void stopRunning() {
        worker.stopRunning();
    }
}
