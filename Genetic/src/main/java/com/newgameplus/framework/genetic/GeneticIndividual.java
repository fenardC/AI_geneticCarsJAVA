package com.newgameplus.framework.genetic;


public class GeneticIndividual {

    protected double score = 0;

    protected boolean bloodline = false;

    protected GeneticDna dna = null;

    protected Object tag = null;

    public GeneticIndividual(GeneticDna dna) {
        this.dna = dna;
    }

    public GeneticIndividual(GeneticIndividual indiv) {
        dna = indiv.dna.clone();
        score = indiv.score;
        tag = indiv.tag;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public GeneticDna getDna() {
        return dna;
    }

    public void setDna(GeneticDna dna) {
        this.dna = dna;
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

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public boolean isBloodline() {
        return bloodline;
    }

    public void setBloodline(boolean bloodline) {
        this.bloodline = bloodline;
    }

}
