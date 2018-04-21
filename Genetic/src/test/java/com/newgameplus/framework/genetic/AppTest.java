package com.newgameplus.framework.genetic;

import com.newgameplus.framework.debug.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase {
    private static final int SENSOR_LINE_NUMBER = 8;
    private static final int NETWORK_NUMBER_OF_NEURONS = 2;
    private static final double NETWORK_GENE_VALUE_MAX = 5.0;
    private static final int NETWORK_GENE_SIZE = 1;
    private static final int NETWORK_GENE_NUMBER = NETWORK_NUMBER_OF_NEURONS * (SENSOR_LINE_NUMBER + 1 + 1);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        {
            GeneticGeneDouble geneDouble =
                new GeneticGeneDouble(-NETWORK_GENE_VALUE_MAX, NETWORK_GENE_VALUE_MAX, 10);

            geneDouble.randomGene();
            Logger.debug("geneDouble : After randomGene(): " + geneDouble.toString());

            geneDouble.destroy();
            Logger.debug("geneDouble : After destroy(): " + geneDouble.getCode());
            geneDouble = null;
        }

        {
            GeneticGeneDouble geneDouble =
                new GeneticGeneDouble(-NETWORK_GENE_VALUE_MAX, NETWORK_GENE_VALUE_MAX, 20);

            Logger.debug("");

            geneDouble.randomGene();
            Logger.debug("geneDouble: After randomGene(): " + geneDouble.toString());

            GeneticGene geneDoubleClone = geneDouble.clone();
            Logger.debug("geneDoubleClone: After clone(): " + geneDoubleClone.toString());

            geneDouble.destroy();
            Logger.debug("geneDouble : After destroy(): " + geneDouble.getCode());
            Logger.debug("geneDouble : After destroy(): " + geneDouble.toString());
            geneDouble = null;

            geneDoubleClone.destroy();
            Logger.debug("geneDoubleClone : After destroy(): " + geneDoubleClone.getValue());
            Logger.debug("geneDoubleClone : After destroy(): " + geneDoubleClone.toString());
            geneDoubleClone = null;
        }

        {
            final double [/*NETWORK_GENE_NUMBER*/] weights = {
                1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0,
                11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0
            };

            Logger.debug("");

            /* 2 neurons x (8 sensors lines + speed input + threshold) */
            final GeneticDnaNeuralNetwork dnaNetwork =
                new GeneticDnaNeuralNetwork(-NETWORK_GENE_VALUE_MAX, NETWORK_GENE_VALUE_MAX,
                                            NETWORK_GENE_SIZE, NETWORK_GENE_NUMBER);

            Logger.debug("dnaNetwork: After new: " + dnaNetwork.toString());

            for (int i = 0; i < NETWORK_GENE_NUMBER; i++) {
                GeneticGeneDouble geneDouble = new GeneticGeneDouble(-NETWORK_GENE_VALUE_MAX, NETWORK_GENE_VALUE_MAX, NETWORK_GENE_SIZE);

                Logger.debug("weights[" + i + "]: "  + weights[i]);
                geneDouble.getCode().add(weights[i]);
                Logger.debug("geneDouble: After push_back(): " + geneDouble.toString());

                Object code = geneDouble.getCode().get(0);
                Logger.debug("geneDouble: After getCode(): " + code);

                dnaNetwork.getListGene().add((GeneticGene)(geneDouble));
                Logger.debug("dnaNetwork : After push_back(): " + dnaNetwork.toString());

                Object value = dnaNetwork.getListGene().get(i).getValue();
                Logger.debug("dnaNetwork: After getListGene() .getValue(): " + value);
            }


            Logger.debug("");
            GeneticDnaNeuralNetwork dnaNetworkClone = dnaNetwork.clone();
            Logger.debug("dnaNetworkClone: After clone(): " + dnaNetworkClone.toString());

            dnaNetwork.destroy();
            Logger.debug("dnaNetwork : After destroy(): " + dnaNetwork.getListGene());
            Logger.debug("dnaNetwork : After destroy(): " + dnaNetwork.toString());
            //dnaNetwork = null;

            GeneticIndividual indiv = new GeneticIndividual(dnaNetworkClone);
            Logger.debug("indiv : After new: " + indiv.toString());

            indiv.destroy();
            Logger.debug("indiv : After destroy(): " + indiv.toString());
            indiv = null;
        }
        assertTrue("Nothing done in testApp()", true);
    }
}
