package com.newgameplus.framework.genetic;

import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.misc.Couple;
import com.newgameplus.framework.misc.Misc;

public abstract class GeneticDnaTree extends GeneticDna {

    protected List<GeneticGeneTreeBranch> listRoot = new ArrayList<GeneticGeneTreeBranch>();

    public abstract GeneticDnaTree clone();

    public abstract GeneticDnaTree randomDna(int size);

    @Override
    public GeneticDnaTree randomDna() {
        randomDna(1);
        return this;
    }

    @Override
    public int getLength() {
        return listRoot.size();
        /*int nb = 0;
        for (int i = 0 ; i < listRoot.size() ; i++) {
            nb += listRoot.get(i).getLength();
        }
        return nb;*/
    }

    public int getLength(int index) {
        return listRoot.get(index).getLength();
    }

    public List<GeneticGeneTreeBranch> getListGeneticGeneTreeBranch() {
        List<GeneticGeneTreeBranch> listGene = new ArrayList<GeneticGeneTreeBranch>();

        for (int i = 0 ; i < listRoot.size() ; i++) {
            listGene.addAll(listRoot.get(i).getListGeneticGeneTreeBranch());
        }

        return listGene;
    }

    public List<GeneticGeneTreeBranch> getListGeneticGeneTreeBranch(int index) {
        return listRoot.get(index).getListGeneticGeneTreeBranch();
    }

    @Override
    public Object getCode() {
        return listRoot;
    }

    public List<GeneticGeneTreeBranch> getListRoot() {
        return listRoot;
    }

    @Override
    public double getSimilarityPercent(GeneticDna dna) {
        double percent = 0;

        if (dna instanceof GeneticDnaTree) {
            GeneticDnaTree myDna = (GeneticDnaTree) dna;

            int minRoot = Math.min(getListRoot().size(), myDna.getListRoot().size());
            int maxRoot = Math.max(getListRoot().size(), myDna.getListRoot().size());

            percent = 100;

            for (int j = 0 ; j < minRoot ; j++) {

                List<GeneticGeneTreeBranch> listGene1 = getListGeneticGeneTreeBranch();
                List<GeneticGeneTreeBranch> listGene2 = myDna.getListGeneticGeneTreeBranch();

                int minGene = Math.min(listGene1.size(), listGene2.size());
                int maxGene = Math.max(listGene1.size(), listGene2.size());

                if (maxGene > 0) {

                    int nbSimilarity = 0;

                    for (int i = 0 ; i < minGene ; i++) {
                        if (listGene1.get(i).equals(listGene2.get(i))) {
                            nbSimilarity++;
                        }
                    }

                    percent = percent * nbSimilarity / (double) maxGene;
                }
            }

            if (minRoot != 0 && maxRoot != 0) {
                percent = percent * minRoot / (double) maxRoot;
            }
            else {
                percent = 0;
            }

        }

        return percent;
    }

    @Override
    public Couple<GeneticDna, GeneticDna> cross(GeneticDna other, GeneticCrossParameter param) {
        Couple<GeneticDna, GeneticDna> couple = null;

        if (other instanceof GeneticDnaTree) {
            GeneticDnaTree b = (GeneticDnaTree) other;

            GeneticDnaTree dnaChildA = clone();
            GeneticDnaTree dnaChildB = b.clone();

            int minRoot = Math.min(dnaChildA.getListRoot().size(), dnaChildB.getListRoot().size());

            for (int i = 0 ; i < minRoot ; i++) {

                List<GeneticGeneTreeBranch> codeA = dnaChildA.getListGeneticGeneTreeBranch(i);
                List<GeneticGeneTreeBranch> codeB = dnaChildB.getListGeneticGeneTreeBranch(i);

                GeneticGeneTreeBranch geneA = codeA.get(Misc.random(0, codeA.size() - 1));
                GeneticGeneTreeBranch geneB = codeB.get(Misc.random(0, codeB.size() - 1));

                GeneticGeneTreeBranch parentGeneA = geneA.getParent();
                GeneticGeneTreeBranch parentGeneB = geneB.getParent();

                if (parentGeneA != null) {
                    int index = parentGeneA.getListBranch().indexOf(geneA);
                    parentGeneA.getListBranch().set(index, geneB);
                }
                else {
                    int index = dnaChildA.getListRoot().indexOf(geneA);
                    dnaChildA.getListRoot().set(index, geneB);
                }

                if (parentGeneB != null) {
                    int index = parentGeneB.getListBranch().indexOf(geneB);
                    parentGeneB.getListBranch().set(index, geneA);
                }
                else {
                    int index = dnaChildB.getListRoot().indexOf(geneB);
                    dnaChildB.getListRoot().set(index, geneA);
                }

                geneA.setParent(parentGeneB);
                geneB.setParent(parentGeneA);

            }

            couple = new Couple<GeneticDna, GeneticDna>();

            couple.a = dnaChildA;
            couple.b = dnaChildB;

        }

        return couple;
    }

    @Override
    public void mutate(double chancePerGeneInPercent) {
        for (int index = 0 ; index < listRoot.size() ; index++) {
            for (int i = 0 ; i < getListGeneticGeneTreeBranch(index).size() ; i++) {
                if (Misc.random(chancePerGeneInPercent)) {
                    getListGeneticGeneTreeBranch(index).get(i).mutate();
                }
            }
        }
    }

    @Override
    public void destroy() {
        for (GeneticGeneTreeBranch gene : listRoot) {
            gene.destroy();
        }

        listRoot = null;
    }

}
