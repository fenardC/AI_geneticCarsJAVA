package com.newgameplus.framework.genetic;

import java.util.ArrayList;
import java.util.List;

public abstract class GeneticGeneTreeBranch extends GeneticGeneObject {

    protected List<GeneticGeneTreeBranch> listBranch = new ArrayList<>();

    protected GeneticGeneTreeBranch parent = null;

    public GeneticGeneTreeBranch(List<Object> listPossible) {
        super(listPossible);
    }

    @Override
    public abstract GeneticGeneTreeBranch clone();

    public int getLength() {
        int i = 1;

        for (GeneticGeneTreeBranch operand : listBranch) {
            i += operand.getLength();
        }

        return i;
    }

    public List<GeneticGeneTreeBranch> getListGeneticGeneTreeBranch() {
        List<GeneticGeneTreeBranch> list = new ArrayList<>();
        list.add(this);

        for (GeneticGeneTreeBranch operand : listBranch) {
            list.addAll(operand.getListGeneticGeneTreeBranch());
        }

        return list;
    }

    public GeneticGeneTreeBranch getParent() {
        return parent;
    }

    public void setParent(GeneticGeneTreeBranch parent) {
        this.parent = parent;
    }

    public List<GeneticGeneTreeBranch> getListBranch() {
        return listBranch;
    }

    @Override
    public void destroy() {
        for (GeneticGeneTreeBranch gene : listBranch) {
            gene.destroy();
        }

        listBranch = null;
        parent = null;
    }

}
