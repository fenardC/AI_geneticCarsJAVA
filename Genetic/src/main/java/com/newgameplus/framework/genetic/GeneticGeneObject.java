package com.newgameplus.framework.genetic;

import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.misc.Misc;

public class GeneticGeneObject extends GeneticGene {

    protected List<Object> listPossible = new ArrayList<Object>();
    protected List<Object> code = new ArrayList<Object>();
    protected int size = 0;

    public GeneticGeneObject(List<Object> listPossible) {
        this.listPossible = listPossible;
        size = 1;
    }

    public GeneticGeneObject(List<Object> listPossible, int size) {
        this(listPossible);
        this.size = size;
    }

    public GeneticGene clone() {
        GeneticGeneObject gene = new GeneticGeneObject(listPossible, size);

        for (Object o : code) {
            gene.code.add(o);
        }

        return gene;
    }

    @Override
    public boolean equals(GeneticGene gene) {
        if (gene != null && gene instanceof GeneticGeneObject) {
            GeneticGeneObject myGene = (GeneticGeneObject) gene;

            if (getCode().size() == myGene.getCode().size()) {
                for (int i = 0 ; i < getCode().size() ; i++) {
                    if (!getCode().get(i).equals(myGene.getCode().get(i))) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return code.toString();
    }

    @Override
    public GeneticGeneObject randomGene() {
        code.clear();

        if (getListPossible().size() > 0) {
            for (int i = 0 ; i < size ; i++) {
                code.add(getListPossible().get(Misc.random(0, getListPossible().size() - 1)));
            }
        }

        return this;
    }

    @Override
    public void mutate() {
        int i = 0;
        List<Object> oldCode = new ArrayList<Object>(code);

        while (i < 30 && oldCode.equals(code)) {
            randomGene();
            i++;
        }
    }

    public List<Object> getCode() {
        return code;
    }

    @Override
    public List<Object> getValue() {
        return code;
    }

    public List<Object> getListPossible() {
        return listPossible;
    }

    public void setListPossible(List<Object> listPossible) {
        this.listPossible = listPossible;
    }

    @Override
    public void destroy() {
        listPossible = null;
        code = null;
    }

}
