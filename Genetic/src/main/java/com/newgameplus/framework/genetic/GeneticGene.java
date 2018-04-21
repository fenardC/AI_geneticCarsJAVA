package com.newgameplus.framework.genetic;

public abstract class GeneticGene {

    @Override
    public abstract GeneticGene clone();

    public abstract boolean equals(GeneticGene gene);

    public abstract GeneticGene randomGene();

    public abstract void mutate();

    public abstract Object getValue();

    public abstract void destroy();
}
