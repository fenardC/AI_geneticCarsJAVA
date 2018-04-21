package com.newgameplus.framework.genetic;

import java.util.ArrayList;
import java.util.List;

public class GeneticDnaListObject extends GeneticDnaList {

    protected List<Object> listPossible = new ArrayList<Object>();
    protected int geneSize;
    protected int nbGene;

    public GeneticDnaListObject(List<Object> listPossible, int geneSize) {
        if (listPossible != null) {
            for (Object o : listPossible) {
                this.listPossible.add(o);
            }
        }

        this.geneSize = geneSize;
    }

    public GeneticDnaListObject(List<Object> listPossible, int geneSize, int nbGene) {
        this(listPossible, geneSize);
        this.nbGene = nbGene;
    }

    @Override
    public GeneticDnaListObject clone() {
        GeneticDnaListObject dna = new GeneticDnaListObject(listPossible, geneSize, nbGene);

        for (GeneticGene gene : listGene) {
            dna.getListGene().add(gene.clone());
        }

        return dna;
    }

    @Override
    public GeneticDnaListObject randomDna() {
        return randomDna(nbGene);
    }

    @Override
    public GeneticDnaListObject randomDna(int size) {
        listGene.clear();

        for (int i = 0 ; i < size ; i++) {
            listGene.add(new GeneticGeneObject(listPossible, geneSize).randomGene());
        }

        return this;
    }

    public List<Object> getListPossible() {
        return listPossible;
    }

    @Override
    public void destroy() {
        super.destroy();
        listPossible = null;
    }

}
