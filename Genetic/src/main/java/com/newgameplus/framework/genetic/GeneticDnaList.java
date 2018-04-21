package com.newgameplus.framework.genetic;

import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.misc.Couple;
import com.newgameplus.framework.misc.Misc;

public abstract class GeneticDnaList extends GeneticDna {

    protected List<GeneticGene> listGene = new ArrayList<GeneticGene>();

    // DNA

    public List<GeneticGene> getListGene() {
        return listGene;
    }

    @Override
    public List<GeneticGene> getCode() {
        return listGene;
    }

    @Override
    public int getLength() {
        return listGene.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0 ; i < listGene.size() ; i++) {
            if (i > 0) {
                builder.append(",");
            }

            builder.append(listGene.get(i).toString());
        }

        return builder.toString();
    }

    // Reproduction

    @Override
    public double getSimilarityPercent(GeneticDna dna) {
        double percent = 0;

        if (dna instanceof GeneticDnaList) {
            GeneticDnaList myDna = (GeneticDnaList) dna;
            int minGene = Math.min(myDna.getListGene().size(), getListGene().size());
            int maxGene = Math.max(myDna.getListGene().size(), getListGene().size());

            if (maxGene > 0) {

                int nbSimilarity = 0;

                for (int i = 0 ; i < minGene ; i++) {
                    if (myDna.getListGene().get(i).equals(getListGene().get(i))) {
                        nbSimilarity++;
                    }
                }

                percent = nbSimilarity / (double) maxGene * 100.0;
            }
        }

        return percent;
    }

    @Override
    public Couple<GeneticDna, GeneticDna> cross(GeneticDna other, GeneticCrossParameter param) {
        Couple<GeneticDna, GeneticDna> couple = null;

        if (other instanceof GeneticDnaList) {
            GeneticDnaList b = (GeneticDnaList) other;
            List<GeneticGene> codeA = listGene;
            List<GeneticGene> codeB = b.getListGene();

            couple = new Couple<GeneticDna, GeneticDna>();

            int maxLength = Math.max(codeA.size(), codeB.size());
            int sizeCross = (int)(maxLength * param.getCrossRatio());
            int indexCross = Misc.random(1, maxLength - sizeCross);

            List<GeneticGene> startA = new ArrayList<GeneticGene>();
            List<GeneticGene> endA = new ArrayList<GeneticGene>();

            if (indexCross < codeA.size()) {
                endA.addAll(codeA.subList(indexCross, codeA.size()));
                startA.addAll(codeA.subList(0, indexCross));
            }
            else {
                startA.addAll(codeA);
            }

            List<GeneticGene> startB = new ArrayList<GeneticGene>();
            List<GeneticGene> endB = new ArrayList<GeneticGene>();

            if (indexCross < codeB.size()) {
                endB.addAll(codeB.subList(indexCross, codeB.size()));
                startB.addAll(codeB.subList(0, indexCross));
            }
            else {
                startB.addAll(codeB);
            }

            GeneticDnaList dnaChildA = (GeneticDnaList) clone();
            dnaChildA.getListGene().clear();

            for (GeneticGene gene : startA) {
                dnaChildA.getListGene().add(gene.clone());
            }

            for (GeneticGene gene : endB) {
                dnaChildA.getListGene().add(gene.clone());
            }

            GeneticDnaList dnaChildB = (GeneticDnaList) b.clone();
            dnaChildB.getListGene().clear();

            for (GeneticGene gene : startB) {
                dnaChildB.getListGene().add(gene.clone());
            }

            for (GeneticGene gene : endA) {
                dnaChildB.getListGene().add(gene.clone());
            }

            //dnaChildB.getListGene().addAll(startB);
            //dnaChildB.getListGene().addAll(endA);

            couple.a = dnaChildA;
            couple.b = dnaChildB;

        }

        return couple;
    }

    public void mutate(double chancePerGeneInPercent) {
        for (int i = 0 ; i < listGene.size() ; i++) {
            if (Misc.random(chancePerGeneInPercent)) {
                listGene.get(i).mutate();
            }
        }
    }

    @Override
    public void destroy() {
        for (GeneticGene gene : listGene) {
            gene.destroy();
        }

        listGene = null;
    }

}
