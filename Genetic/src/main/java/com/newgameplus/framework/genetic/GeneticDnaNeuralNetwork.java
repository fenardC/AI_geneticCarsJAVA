package com.newgameplus.framework.genetic;

public class GeneticDnaNeuralNetwork extends GeneticDnaList {
	
	protected double min;
	protected double max;
	
	protected int geneSize;
	protected int nbGene;
	
	
	public GeneticDnaNeuralNetwork(double min, double max, int geneSize, int nbGene) {
		this.min = min;
		this.max = max;
		this.geneSize = geneSize;
		this.nbGene = nbGene;
	}
	
	@Override
	public GeneticDnaNeuralNetwork clone() {
		GeneticDnaNeuralNetwork dna = new GeneticDnaNeuralNetwork(min, max, geneSize, nbGene);
		
		for (GeneticGene gene : listGene) {
			dna.getListGene().add(gene.clone());
		}
		
		return dna;
	}
	
	@Override
	public GeneticDna randomDna() {
		return randomDna(nbGene);
	}
	
	@Override
	public GeneticDna randomDna(int size) {
		listGene.clear();
		
		for (int i = 0 ; i < size ; i++) {
			listGene.add(new GeneticGeneDouble(min, max, geneSize).randomGene());
		}
		
		return this;
	}
	
}
