package com.newgameplus.framework.genetic;

import com.newgameplus.framework.misc.Misc;

public class GeneticGeneString extends GeneticGene {

    private String charset = "";
    private String code = "";
    private int size = 0;

    public GeneticGeneString(String charset, int size) {
        super();
        this.charset = charset;
        this.size = size;
    }

    @Override
    public GeneticGene clone() {
        final GeneticGeneString gene = new GeneticGeneString(charset, size);

        gene.code = code;

        return gene;
    }

    @Override
    public boolean equals(GeneticGene gene) {
        if (gene != null && gene instanceof GeneticGeneString) {
            final GeneticGeneString myGene = (GeneticGeneString) gene;

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
        final StringBuilder builder = new StringBuilder();
        final int charsetLength = charset.length();

        for (int i = 0 ; i < size ; i++) {
            final int index = Misc.random(0, charsetLength - 1);
            builder.append(charset.substring(index, index + 1));
        }

        code = builder.toString();
        return this;
    }

    @Override
    public void mutate() {
        int count = 0;
        final String oldCode = code;

        while (count < 10 && oldCode.equals(code)) {
            randomGene();
            count++;
        }
    }

    public String getCode() {
        return code;
    }

    public int getSize() {
        return size;
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
