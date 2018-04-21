package com.newgameplus.framework.neural;

import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.misc.Misc;

final class NeuralNeuron extends NeuralInput {
    private double threshold = 0;
    private List<Double> listWeight = new ArrayList<>();
    private List<NeuralInput> listInput = new ArrayList<>();
    private double output = 0;
    private double newOutput = 0;
    private NeuralActivation activationFunction = NeuralActivation.SIGMOID;

    public NeuralNeuron() {
        /* Add more if needed. */
    }

    public NeuralNeuron(NeuralActivation activationFunction) {
        this();
        this.activationFunction = activationFunction;
    }

    public void random(double min, double max) {
        listWeight.clear();

        for (int i = 0; i < listInput.size(); i++) {
            listWeight.add(new Double(Misc.random(min, max)));
        }

        threshold = Misc.random(min, max);
    }

    public void calculate() {
        if (listInput.size() == listWeight.size() && activationFunction != null) {
            double res = -threshold;

            for (int i = 0; i < listInput.size(); i++) {
                res += listInput.get(i).getValue() * listWeight.get(i).doubleValue();
            }

            newOutput = activationFunction.calculate(res);
        }
    }

    public void addInput(NeuralInput input) {
        listInput.add(input);
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void setActivationFunction(NeuralActivation activationFunction) {
        this.activationFunction = activationFunction;
    }

    public List<Double> getListWeight() {
        return listWeight;
    }

    public List<NeuralInput> getListInput() {
        return listInput;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getNewOutput() {
        return newOutput;
    }

    public void setNewOutput(double newOutput) {
        this.newOutput = newOutput;
    }

    @Override
    public double getValue() {
        return output;
    }
}
