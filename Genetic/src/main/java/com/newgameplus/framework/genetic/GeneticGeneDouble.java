package com.newgameplus.framework.genetic;

import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.misc.Misc;

public class GeneticGeneDouble extends GeneticGene {

    protected List<Object> code = new ArrayList<Object>();
    protected int size = 0;

    protected double min = 0;
    protected double max = 0;


    public GeneticGeneDouble(double min, double max) {
        this.min = min;
        this.max = max;
        size = 1;
    }

    public GeneticGeneDouble(double min, double max, int size) {
        this(min, max);
        this.size = size;
    }

    public GeneticGene clone() {
        GeneticGeneDouble gene = new GeneticGeneDouble(min, max, size);

        for (Object o : code) {
            gene.code.add(o);
        }

        return gene;
    }

    @Override
    public boolean equals(GeneticGene gene) {
        if (gene != null && gene instanceof GeneticGeneDouble) {
            GeneticGeneDouble myGene = (GeneticGeneDouble) gene;

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
    public GeneticGeneDouble randomGene() {
        code.clear();

        for (int i = 0 ; i < size ; i++) {
            code.add(Misc.random(min, max));
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

    public void setCode(List<Object> code) {
        this.code = code;
    }

    @Override
    public void destroy() {
        code = null;
    }

}
