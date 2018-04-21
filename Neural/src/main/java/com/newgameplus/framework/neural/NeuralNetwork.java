package com.newgameplus.framework.neural;

import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.debug.Logger;
import com.newgameplus.framework.draw.DrawParameterNeuralNetwork;
import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.genetic.GeneticDnaNeuralNetwork;
import com.newgameplus.framework.genetic.GeneticGene;
import com.newgameplus.framework.misc.Misc;

public class NeuralNetwork {
	
	protected List<NeuralInput> listInput = new ArrayList<NeuralInput>();
	
	protected List<NeuralLayer> listLayer = new ArrayList<NeuralLayer>();
	
	protected double maxValue = 1;
	
	public NeuralNetwork() {
		
	}
	
	public NeuralNetwork(double maxValue) {
		this();
		this.maxValue = maxValue;
	}
	
	
	public void initFromDna(GeneticDnaNeuralNetwork dna) {
		List<GeneticGene> listGene = dna.getListGene();
		int i = 0;
		for (NeuralLayer layer : listLayer) {
			for (NeuralNeuron neuron : layer.getListNeuron()) {
				neuron.getListWeight().clear();
				for (int j = 0 ; j < neuron.getListInput().size() ; j++) {
					GeneticGene gene = listGene.get(i);
					neuron.getListWeight().add((Double) ((List<Object>) gene.getValue()).get(0));
					i++;
				}
				GeneticGene gene = listGene.get(i);
				neuron.setThreshold((Double) ((List<Object>) gene.getValue()).get(0) * neuron.getListWeight().size());
				i++;
			}
		}
	}
	
	public GeneticDnaNeuralNetwork generateDnaModel() {
		int nb = 0;
		for (NeuralLayer layer : listLayer) {
			for (NeuralNeuron neuron : layer.getListNeuron()) {
				nb += neuron.getListInput().size() + 1;
			}
		}
		
		Logger.debug("DNA Length = " + nb);
		
		GeneticDnaNeuralNetwork dna = new GeneticDnaNeuralNetwork(-maxValue, maxValue, 1, nb);
		
		return dna;
	}
	
	public void render(Drawer d, double x, double y, DrawParameterNeuralNetwork param) {
		
		double radius = param.getRatioNeuron() * 5;
		double offsetX = param.getRatioX() * 25;
		double offsetY = param.getRatioY() * 16;
		
		for (int i = 0 ; i < listLayer.size() ; i++) {
			NeuralLayer layer = listLayer.get(i);
			
			for (int j = 0 ; j < layer.getListNeuron().size() ; j++) {
				NeuralNeuron neuron = layer.getListNeuron().get(j);
				
				double posX = x + offsetX * (i - listLayer.size() / 2.0);
				double posY = y + offsetY * (j - layer.getListNeuron().size() / 2.0);
				
				d.setColor(Misc.mix(param.getColorDesactivated(), param.getColorActivated(), neuron.getOutput()));
				d.fillCircle(posX, posY, (int) radius);
				
			}
			
		}
		
		for (int i = 1 ; i < listLayer.size() ; i++) {
			NeuralLayer layer = listLayer.get(i);
			
			for (int j = 0 ; j < layer.getListNeuron().size() ; j++) {
				NeuralNeuron neuron = layer.getListNeuron().get(j);
				
				double posX = x + offsetX * (i - listLayer.size() / 2.0);
				double posY = y + offsetY * (j - layer.getListNeuron().size() / 2.0);
				
				for (NeuralInput input : neuron.getListInput()) {
					if (input instanceof NeuralNeuron) {
						NeuralNeuron neuron2 = (NeuralNeuron) input;
						int index = listLayer.get(i-1).getListNeuron().indexOf(neuron2);
						
						double posX2 = x + offsetX * (i-1 - listLayer.size() / 2.0);
						double posY2 = y + offsetY * (index - listLayer.get(i-1).getListNeuron().size() / 2.0);
						
						d.setColor(param.getColorLink());
						d.drawLine(posX, posY, posX2, posY2);
						
					}
				}
				
				
			}
			
		}
		
	}
	
	public void random() {
		for (NeuralLayer layer : listLayer) {
			layer.random(-maxValue, maxValue);
		}
	}
	
	public void calculate() {
		for (NeuralLayer layer : listLayer) {
			layer.calculate();
		}
	}
	
	public void connectAllInputOnFirstLayer() {
		if (listLayer.size() >= 1) {
			for (NeuralNeuron neuron : listLayer.get(0).getListNeuron()) {
				for (NeuralInput input : listInput) {
					neuron.addInput(input);
				}
			}
		}
	}
	
	public void addLayer(NeuralLayer layer) {
		listLayer.add(layer);
	}
	
	public List<NeuralLayer> getListLayer() {
		return listLayer;
	}
	
	public void addInput(NeuralInput input) {
		listInput.add(input);
	}
	
	public void setInputValue(int index, double value) {
		((NeuralInputValue)listInput.get(index)).setValue(value);
	}
	
	public List<Double> getListResult() {
		List<Double> listResult = new ArrayList<Double>();
		
		NeuralLayer layer = listLayer.get(listLayer.size() - 1);
		for (NeuralNeuron neuron : layer.getListNeuron()) {
			listResult.add(neuron.getOutput());
		}
		
		return listResult;
	}
	
	public List<NeuralInput> getListInput() {
		return listInput;
	}
	
}
