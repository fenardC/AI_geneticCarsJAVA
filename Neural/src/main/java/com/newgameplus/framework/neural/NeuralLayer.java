package com.newgameplus.framework.neural;

import java.util.ArrayList;
import java.util.List;

public class NeuralLayer {
    protected List<NeuralNeuron> listNeuron = new ArrayList<>();

    public NeuralLayer() {
        /* Add more if needed. */
    }

    public NeuralLayer(int nb) {
        for (int i = 0 ; i < nb ; i++) {
            addNeuron(new NeuralNeuron());
        }
    }

    public NeuralLayer(NeuralActivation activationFunction, int nb) {
        for (int i = 0 ; i < nb ; i++) {
            addNeuron(new NeuralNeuron(activationFunction));
        }
    }

    public void addNeuron(NeuralNeuron neuron) {
        listNeuron.add(neuron);
    }

    public void calculate() {
        for (NeuralNeuron neuron : listNeuron) {
            neuron.calculate();
        }

        for (NeuralNeuron neuron : listNeuron) {
            neuron.setOutput(neuron.getNewOutput());
        }
    }

    public void connectAll(NeuralLayer previousLayer) {
        for (NeuralNeuron neuron : listNeuron) {
            for (NeuralNeuron neuron2 : previousLayer.getListNeuron()) {
                neuron.addInput(neuron2);
            }
        }
    }

    public List<NeuralNeuron> getListNeuron() {
        return listNeuron;
    }

    public void random(double min, double max) {
        for (NeuralNeuron neuron : listNeuron) {
            neuron.random(min, max);
        }
    }
}
