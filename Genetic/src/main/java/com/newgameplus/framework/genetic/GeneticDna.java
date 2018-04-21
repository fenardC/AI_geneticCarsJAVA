package com.newgameplus.framework.genetic;

import com.newgameplus.framework.misc.Couple;

public abstract class GeneticDna {
	
	
	public abstract GeneticDna clone();
	
	public abstract GeneticDna randomDna();
	public abstract GeneticDna randomDna(int size);
	
	// DNA
	
	public abstract int getLength();
	
	public abstract Object getCode();
	
	// Reproduction
	
	public abstract double getSimilarityPercent(GeneticDna dna);
	
	public boolean isCompatible(GeneticDna dna) {
		return this.getClass().isInstance(dna) && dna.getClass().isInstance(this);
	}
	
	public abstract Couple<GeneticDna, GeneticDna> cross(GeneticDna other, GeneticCrossParameter param);
	
	public abstract void mutate(double chancePerGeneInPercent);
	
	public abstract void destroy();
	
}
