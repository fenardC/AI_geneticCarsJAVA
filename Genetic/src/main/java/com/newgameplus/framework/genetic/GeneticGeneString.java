package com.newgameplus.framework.genetic;

import com.newgameplus.framework.misc.Misc;

public class GeneticGeneString extends GeneticGene {
	
	protected String charset = "";
	protected String code = "";
	protected int size = 0;
	
	public GeneticGeneString(String charset, int size) {
		this.charset = charset;
		this.size = size;
	}
	
	public GeneticGene clone() {
		GeneticGeneString gene = new GeneticGeneString(charset, size);
		
		gene.code = code;
		
		return gene;
	}
	
	@Override
	public boolean equals(GeneticGene gene) {
		if (gene != null && gene instanceof GeneticGeneString) {
			GeneticGeneString myGene = (GeneticGeneString) gene;
			if (size == myGene.size) {
				return code.equals(myGene.code);
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return code;
	}
	
	@Override
	public GeneticGeneString randomGene() {
		StringBuilder builder = new StringBuilder();
		int n = charset.length();
		for (int i = 0 ; i < size ; i++) {
			int index = Misc.random(0, n - 1);
			builder.append(charset.substring(index, index + 1));
		}
		code = builder.toString();
		return this;
	}
	
	@Override
	public void mutate() {
		int i = 0;
		String oldCode = code;
		while (i < 10 && oldCode.equals(code)) {
			randomGene();
			i++;
		}
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getCharset() {
		return charset;
	}
	
	@Override
	public String getValue() {
		return code;
	}
	
	@Override
	public void destroy() {
		charset = null;
		code = null;
	}
	
}
