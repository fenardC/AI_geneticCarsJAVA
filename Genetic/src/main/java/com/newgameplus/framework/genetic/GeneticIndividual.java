package com.newgameplus.framework.genetic;

public class GeneticIndividual {

    private double score = 0;
    private boolean bloodline = false;
    private GeneticDna dna = null;
    private Object tag = null;

    public GeneticIndividual(GeneticDna dna) {
        super();
        this.dna = dna;
    }

    public GeneticIndividual(GeneticIndividual indiv) {
        super();
        dna = indiv.dna.clone();
        score = indiv.score;
        tag = indiv.tag;
    }

    public double getScore() {
        return score;
    }

    public void setScore(final double score) {
        this.score = score;
    }

    public GeneticDna getDna() {
        return dna;
    }

    @Override
    public String toString() {
        return "DNA : " + dna.toString() + "\nScore : " + score;
    }

    public void destroy() {
        dna.destroy();
        tag = null;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(final Object tag) {
        this.tag = tag;
    }

    public boolean isBloodline() {
        return bloodline;
    }

    public void setBloodline(final boolean bloodline) {
        this.bloodline = bloodline;
    }
}
