package com.newgameplus.framework.genetic;

final class GeneticCrossParameter {
    private static final double CROSS_RATIO = 0.7;

    private GeneticCrossParameter() {

    }

    static double getCrossRatio() {
        return CROSS_RATIO;
    }
}
