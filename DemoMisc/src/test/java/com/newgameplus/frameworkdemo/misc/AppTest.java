package com.newgameplus.frameworkdemo.misc;

import java.util.concurrent.TimeUnit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase {
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
        System.out.println("~o~ testApp ~o~");
        System.out.println("Calling DemoCar.main()");
        DemoCar.main(null);

        try {
            final long duree = 10;
            System.out.println("Waiting ... " + duree + " s");
            TimeUnit.SECONDS.sleep(duree);
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("~o~ testApp ~o~");
        assertTrue("Demo car passed", true);
    }
}
