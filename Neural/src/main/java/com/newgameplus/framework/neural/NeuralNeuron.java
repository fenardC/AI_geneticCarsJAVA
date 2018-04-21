package com.newgameplus.framework.neural;

import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.misc.Misc;

public class NeuralNeuron extends NeuralInput {

    protected double threshold = 0;

    protected List<Double> listWeight = new ArrayList<Double>();
    protected List<NeuralInput> listInput = new ArrayList<NeuralInput>();

    protected double output = 0;
    protected double newOutput = 0;

    protected NeuralActivation activationFunction = NeuralActivation.SIGMOID;


    public NeuralNeuron() {

    }

    public NeuralNeuron(NeuralActivation activationFunction) {
        this();
        this.activationFunction = activationFunction;
    }


    public void random(double min, double max) {
        listWeight.clear();

        for (int i = 0 ; i < listInput.size() ; i++) {
            listWeight.add(Misc.random(min, max));
        }

        threshold = Misc.random(min, max);
    }

    public void calculate() {
        if (listInput.size() == listWeight.size() && activationFunction != null) {
            double res = -threshold;

            for (int i = 0 ; i < listInput.size() ; i++) {
                res += listInput.get(i).getValue() * listWeight.get(i);
            }

            newOutput = activationFunction.calculate(res);
        }
    }

    public void addInput(NeuralInput input) {
        listInput.add(input);
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public NeuralActivation getActivationFunction() {
        return activationFunction;
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
