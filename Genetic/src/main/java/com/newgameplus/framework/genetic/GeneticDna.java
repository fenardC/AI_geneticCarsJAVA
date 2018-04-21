package com.newgameplus.framework.genetic;

import com.newgameplus.framework.misc.Couple;

public abstract class GeneticDna {

    public GeneticDna() {
    }

    @Override
    public abstract GeneticDna clone();
    public abstract GeneticDna randomDna();
    public abstract GeneticDna randomDna(int size);

    // DNA
    public abstract int getLength();
    public abstract Object getCode();

    // Reproduction
    public abstract double getSimilarityPercent(GeneticDna dna);
    public boolean isCompatible(final GeneticDna dna) {
        return this.getClass().isInstance(dna) && dna.getClass().isInstance(this);
    }

    public abstract Couple<GeneticDna, GeneticDna> cross(GeneticDna other);
    public abstract void mutate(double chanceInPercent);
    public abstract void destroy();
}
