package com.newgameplus.framework.genetic;

public class GeneticDnaTreeFormula extends GeneticDnaTree {

    public GeneticDnaTreeFormula() {
        /* Add more if needed. */
    }

    @Override
    public GeneticDnaTreeFormula clone() {
        GeneticDnaTreeFormula dna = new GeneticDnaTreeFormula();

        for (GeneticGeneTreeBranch root : listRoot) {
            dna.listRoot.add(root.clone());
        }

        return dna;
    }

    @Override
    public GeneticDnaTreeFormula randomDna(int size) {
        listRoot.clear();

        for (int i = 0 ; i < size ; i++) {
            GeneticGeneFormulaOperand root = new GeneticGeneFormulaOperand();
            root.randomGene();
            listRoot.add(root);
        }

        return this;
    }
}
