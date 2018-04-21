package com.newgameplus.framework.genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.newgameplus.framework.misc.Misc;

public class GeneticGeneFormulaOperand extends GeneticGeneTreeBranch {

    protected static List<Object> listOperandPossible = new ArrayList<>();
    protected static List<Object> listOperatorPossible = new ArrayList<>();
    protected static HashMap<Object, Integer> mapNbOperand = new HashMap<>();

    static {

        final Integer oneOperand = new Integer(1);
        final Integer twoOperand = new Integer(2);
        addOperator("+", twoOperand);
        addOperator("-", twoOperand);
        addOperator("*", twoOperand);
        addOperator("/", twoOperand);
        addOperator("sqrt", oneOperand);
        addOperator("^2", oneOperand);

        for (int i = 1 ; i <= 10 ; i++) {
            addOperand(new Integer(i));
        }
    }

    public GeneticGeneFormulaOperand() {
        super(null);
    }

    @Override
    public GeneticGeneFormulaOperand clone() {
        GeneticGeneFormulaOperand gene = new GeneticGeneFormulaOperand();

        for (Object o : code) {
            gene.code.add(o);
        }

        for (GeneticGeneTreeBranch branch : listBranch) {
            GeneticGeneTreeBranch op2 = branch.clone();
            op2.setParent(gene);
            gene.listBranch.add(op2);
        }

        return gene;
    }

    @Override
    public GeneticGeneFormulaOperand randomGene() {
        code.clear();

        if (Misc.random(50)) {
            code.add(listOperatorPossible.get(Misc.random(0, listOperatorPossible.size() - 1)));
        }
        else {
            code.add(listOperandPossible.get(Misc.random(0, listOperandPossible.size() - 1)));
        }

        repareGene();
        return this;
    }

    @Override
    public void mutate() {
        super.mutate();
        repareGene();
    }

    public void repareGene() {
        List<GeneticGeneTreeBranch> listOld = new ArrayList<>(listBranch);

        if (isOperator()) {
            int nb = getNbOperandForOperator(code.get(0)).intValue() - listBranch.size();

            if (nb > 0) {
                for (int i = 0 ; i < nb ; i++) {
                    listBranch.add(new GeneticGeneFormulaOperand().randomGene());
                }
            }
            else {
                for (int i = 0 ; i < -nb ; i++) {
                    listBranch.remove(Misc.random(0, listBranch.size() - 1));
                }
            }
        }
        else {
            listBranch.clear();
        }

        for (GeneticGeneTreeBranch gene : listOld) {
            gene.setParent(null);
        }

        for (GeneticGeneTreeBranch gene : listBranch) {
            gene.setParent(this);
        }
    }

    public boolean isOperator() {
        return code.size() > 0 && listOperatorPossible.contains(code.get(0));
    }

    public static Integer getNbOperandForOperator(Object operator) {
        return mapNbOperand.get(operator);
    }

    public static void addOperator(Object operator, Integer nbOperand) {
        listOperatorPossible.add(operator);
        mapNbOperand.put(operator, nbOperand);
    }

    public static void addOperand(Object operand) {
        listOperandPossible.add(operand);
    }
}
