package com.newgameplus.framework.genetic;


public class GeneticDnaListString extends GeneticDnaList {

    protected String charset = "";
    protected int geneSize;
    protected int nbGene;


    public GeneticDnaListString(String charset, int geneSize) {
        this.charset = charset;
        this.geneSize = geneSize;
    }

    public GeneticDnaListString(String charset, int geneSize, int nbGene) {
        this.charset = charset;
        this.geneSize = geneSize;
        this.nbGene = nbGene;
    }

    @Override
    public GeneticDnaListString clone() {

        GeneticDnaListString dna = new GeneticDnaListString(charset, geneSize, nbGene);

        for (GeneticGene gene : listGene) {
            dna.getListGene().add(gene.clone());
        }

        return dna;
    }

    @Override
    public GeneticDnaListString randomDna() {
        return randomDna(nbGene);
    }

    @Override
    public GeneticDnaListString randomDna(int size) {
        listGene.clear();

        for (int i = 0 ; i < size ; i++) {
            listGene.add(new GeneticGeneString(charset, geneSize).randomGene());
        }

        return this;
    }

    public String getCharset() {
        return charset;
    }

    @Override
    public void destroy() {
        super.destroy();
        charset = null;
    }

}
