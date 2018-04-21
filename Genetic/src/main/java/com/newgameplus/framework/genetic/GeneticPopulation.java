package com.newgameplus.framework.genetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.newgameplus.framework.misc.Couple;
import com.newgameplus.framework.misc.Misc;

public class GeneticPopulation {

    protected List<GeneticIndividual> listIndividual = new ArrayList<>();

    protected TreeMap<Double, List<GeneticIndividual>> mapScore = new TreeMap<>();

    protected List<GeneticIndividual> listSelection = new ArrayList<>();

    protected int populationGoal = 0;
    protected GeneticDna dnaType = null;

    protected double mutantPercentChance = 50;
    protected double mutationPercentChancePerGene = 50;

    protected double selectionPercent = 50;

    protected double newbePercent = 5;
    protected int nbBloodline = 0;
    protected double bloodlineScoreTolerance = 0;
    protected double bloodlineDnaMinimumDifference = 0;
    protected double bestWeight = 5;

    protected List<GeneticIndividual> listBloodline = new ArrayList<>();

    protected double averageScore = 0;
    protected double averageScoreBloodline = 0;
    protected double bestScoreInLastGeneration = -1000000000;
    protected double bestScoreEver = -1000000000;
    protected int generationNumber = 1;

    private GeneticIndividual bestIndiv = null;

    public void generatePopulation(int number, GeneticDna dnaType) {
        populationGoal = number;
        this.dnaType = dnaType;

        for (int i = 0 ; i < populationGoal ; i++) {
            generateRandomIndividual();
        }
    }

    public void generateRandomIndividual() {
        GeneticIndividual indiv = new GeneticIndividual(dnaType.clone());
        indiv.getDna().randomDna(dnaType.getLength());
        listIndividual.add(indiv);
    }

    public void proceedNextGeneration() {

        proceedEvaluation();

        proceedSelection();

        proceedReproduction();

        proceedPopulationControl();
        generationNumber++;
    }

    public void proceedEvaluation() {
        mapScore.clear();
        bestScoreInLastGeneration = -1000000000;
        GeneticIndividual bestIndivGen = null;
        double totalScore = 0;

        for (GeneticIndividual indiv : listIndividual) {
            Double score = new Double(indiv.getScore());
            totalScore += score.doubleValue();

            if (!mapScore.containsKey(score)) {
                mapScore.put(score, new ArrayList<GeneticIndividual>());
            }

            mapScore.get(score).add(indiv);

            if (bestScoreInLastGeneration < score.doubleValue()) {
                bestScoreInLastGeneration = score.doubleValue();
                bestIndivGen = indiv;
            }
        }

        if (!listIndividual.isEmpty()) {
            averageScore = totalScore / listIndividual.size();
        }
        else {
            averageScore = totalScore;
        }

        if (bestScoreEver < bestScoreInLastGeneration) {
            bestScoreEver = bestScoreInLastGeneration;
            bestIndiv = new GeneticIndividual(bestIndivGen);
        }

        // Adds new bloodlines
        for (GeneticIndividual indiv : listIndividual) {
            checkBloodline(indiv);
        }

        // Removes too low scores
        Iterator<GeneticIndividual> it = listBloodline.iterator();

        while (it.hasNext()) {
            GeneticIndividual indiv = it.next();

            if (bestIndiv.getScore() * (1 - bloodlineScoreTolerance / 100.0) > indiv.getScore()) {
                it.remove();
            }
        }

        // Removes if too much bloodlines
        int nbDelete = listBloodline.size() - nbBloodline;

        for (int i = 0 ; i < nbDelete ; i++) {
            removeLowestBloodline();
        }

        if (!listBloodline.isEmpty()) {
            totalScore = 0;

            for (GeneticIndividual indiv : listBloodline) {
                Double score = new Double(indiv.getScore());
                totalScore += score.doubleValue();
            }

            averageScoreBloodline = totalScore / listBloodline.size();
        }
        else {
            averageScoreBloodline = 0;
        }
    }

    public void proceedSelection() {
        listSelection.clear();

        // Taking X% best
        int nb = (int) Math.ceil(populationGoal * (selectionPercent / 100.0));
        Iterator<List<GeneticIndividual>> it = mapScore.descendingMap().values().iterator();

        while (it.hasNext() && listSelection.size() < nb) {
            List<GeneticIndividual> list = it.next();
            int i = 0;

            while (i < list.size() && listSelection.size() < nb) {
                listSelection.add(list.get(i));
                i++;
            }
        }

    }

    public void proceedReproductionBetweenSelectioned() {
        listIndividual.clear();
        mapScore.clear();

        // Randomly reproduce
        for (GeneticIndividual indiv : listSelection) {
            reproduce22(indiv, listSelection.get(Misc.random(0, listSelection.size() - 1)));
        }

    }

    public void proceedReproduction() {
        HashMap<Object, Double> map = new HashMap<>();
        double min = 0;
        double max = 0;

        for (int i = 0 ; i < listSelection.size() ; i++) {
            double score = listSelection.get(i).getScore();

            if (score > max || i == 0) {
                max = score;
            }

            if (score < min || i == 0) {
                min = score;
            }
        }

        // [min, max] => [1, bestWeight]
        for (GeneticIndividual indiv : listSelection) {
            if (max > min) {
                map.put(indiv, new Double(1 + (bestWeight - 1) * (indiv.getScore() - min) / (max - min)));
            }
            else {
                map.put(indiv, new Double(1.0));
            }
        }

        listIndividual.clear();
        mapScore.clear();

        int nbNewbe = (int) Math.ceil(newbePercent / 100 * populationGoal);
        int nbBlood = listBloodline.size();
        int nbLess = nbNewbe + nbBlood;

        // Randomly reproduce
        for (int i = 0 ; i < populationGoal - nbLess ; i++) {
            GeneticIndividual indiv = (GeneticIndividual) Misc.randomInWeightedMap(map);
            HashMap<Object, Double> map2 = new HashMap<>(map);
            map2.remove(indiv);
            GeneticIndividual indiv2 = (GeneticIndividual) Misc.randomInWeightedMap(map2);

            reproduce21(indiv, indiv2);
        }

        for (int i = 0 ; i < nbBlood ; i++) {
            GeneticIndividual indiv = new GeneticIndividual(listBloodline.get(i));
            indiv.setBloodline(true);
            listIndividual.add(indiv);
        }

        for (int i = 0 ; i < nbNewbe ; i++) {
            generateRandomIndividual();
        }
    }

    public void proceedPopulationControl() {
        if (listIndividual.size() > populationGoal) {
            for (int i = 0 ; i < listIndividual.size() - populationGoal ; i++) {
                killRandomIndividual();
            }
        }
        else if (listIndividual.size() < populationGoal) {
            for (int i = 0 ; i < populationGoal - listIndividual.size() ; i++) {
                generateRandomIndividual();
            }
        }
    }

    public void killRandomIndividual() {
        GeneticIndividual indiv = listIndividual.remove(Misc.random(0, listIndividual.size() - 1));
        indiv.destroy();
    }

    public void reproduce22(GeneticIndividual a, GeneticIndividual b) {
        if (a.getDna().isCompatible(b.getDna())) {

            Couple<GeneticDna, GeneticDna> couple = a.getDna().cross(b.getDna());

            if (couple != null) {
                final GeneticDna first = couple.getFirst();
                final GeneticDna second = couple.getSecond();

                checkAndApplyMutation(first);
                checkAndApplyMutation(second);

                listIndividual.add(new GeneticIndividual(first));
                listIndividual.add(new GeneticIndividual(second));
            }
        }
    }

    public void reproduce21(GeneticIndividual a, GeneticIndividual b) {
        if (a.getDna().isCompatible(b.getDna())) {

            Couple<GeneticDna, GeneticDna> couple = a.getDna().cross(b.getDna());

            if (couple != null) {
                final GeneticDna first = couple.getFirst();

                checkAndApplyMutation(first);

                listIndividual.add(new GeneticIndividual(first));
            }
        }
    }

    public void checkAndApplyMutation(GeneticDna dna) {
        if (Misc.random(mutantPercentChance)) {
            dna.mutate(mutationPercentChancePerGene);
        }
    }

    public void checkBloodline(GeneticIndividual indiv) {
        if (nbBloodline > 0 &&
                (bestIndiv == null
                 || (bestIndiv.getScore() * (1 - bloodlineScoreTolerance / 100.0) <= indiv.getScore()))) {

            GeneticIndividual lowest = null;

            if (nbBloodline <= listBloodline.size()) {
                for (int i = 0 ; i < listBloodline.size() ; i++) {
                    if (lowest == null || lowest.getScore() > listBloodline.get(i).getScore()) {
                        lowest = listBloodline.get(i);
                    }
                }

                if (lowest != null && lowest.getScore() >= indiv.getScore()) {
                    return;
                }
            }

            boolean ok = true;
            List<GeneticIndividual> listSimi = new ArrayList<>();

            for (int i = 0 ; i < listBloodline.size() ; i++) {
                if (bloodlineDnaMinimumDifference > 100 - indiv.getDna().getSimilarityPercent(listBloodline.get(
                            i).getDna())) {
                    // Too much similarity
                    if (listBloodline.get(i).getScore() < indiv.getScore()) {
                        listSimi.add(listBloodline.get(i));
                    }
                    else {
                        return;
                    }
                }
            }

            if (ok) {
                if (!listSimi.isEmpty()) {
                    for (GeneticIndividual simi : listSimi) {
                        listBloodline.remove(simi);
                    }

                    listBloodline.add(new GeneticIndividual(indiv));
                }
                else {
                    if (nbBloodline <= listBloodline.size()) {
                        listBloodline.remove(lowest);
                    }

                    listBloodline.add(new GeneticIndividual(indiv));
                }
            }
        }
    }

    public void removeLowestBloodline() {
        GeneticIndividual lowest = null;

        for (int i = 0 ; i < listBloodline.size() ; i++) {
            if (lowest == null || lowest.getScore() > listBloodline.get(i).getScore()) {
                lowest = listBloodline.get(i);
            }
        }

        if (lowest != null) {
            listBloodline.remove(lowest);
        }
    }

    public int getPopulationGoal() {
        return populationGoal;
    }

    public void setPopulationGoal(int populationGoal) {
        this.populationGoal = populationGoal;
    }

    public List<GeneticIndividual> getListIndividual() {
        return listIndividual;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public double getBestScoreInLastGeneration() {
        return bestScoreInLastGeneration;
    }

    public double getBestScoreEver() {
        return bestScoreEver;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public GeneticDna getDnaType() {
        return dnaType;
    }

    public void setDnaType(GeneticDna dnaType) {
        this.dnaType = dnaType;
    }

    public double getMutantPercentChance() {
        return mutantPercentChance;
    }

    public void setMutantPercentChance(double mutantPercentChance) {
        this.mutantPercentChance = mutantPercentChance;
    }

    public double getMutationPercentChancePerGene() {
        return mutationPercentChancePerGene;
    }

    public void setMutationPercentChancePerGene(double mutationPercentChancePerGene) {
        this.mutationPercentChancePerGene = mutationPercentChancePerGene;
    }

    public GeneticIndividual getBestIndiv() {
        return bestIndiv;
    }

    public double getNewbePercent() {
        return newbePercent;
    }

    public void setNewbePercent(double newbePercent) {
        this.newbePercent = newbePercent;
    }

    public int getNbBloodline() {
        return nbBloodline;
    }

    public void setNbBloodline(int nbBloodline) {
        this.nbBloodline = nbBloodline;
    }

    public double getBloodlineScoreTolerance() {
        return bloodlineScoreTolerance;
    }

    public void setBloodlineScoreTolerance(double bloodlineScoreTolerance) {
        this.bloodlineScoreTolerance = bloodlineScoreTolerance;
    }

    public double getBloodlineDnaMinimumDifference() {
        return bloodlineDnaMinimumDifference;
    }

    public void setBloodlineDnaMinimumDifference(double bloodlineDnaMinimumDifference) {
        this.bloodlineDnaMinimumDifference = bloodlineDnaMinimumDifference;
    }

    public double getSelectionPercent() {
        return selectionPercent;
    }

    public void setSelectionPercent(double selectionPercent) {
        this.selectionPercent = selectionPercent;
    }

    public double getAverageScoreBloodline() {
        return averageScoreBloodline;
    }

    public List<GeneticIndividual> getListBloodline() {
        return listBloodline;
    }

    public double getBestWeight() {
        return bestWeight;
    }

    public void setBestWeight(double bestWeight) {
        this.bestWeight = bestWeight;
    }

}
