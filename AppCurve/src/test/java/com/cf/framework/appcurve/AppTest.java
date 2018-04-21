package com.cf.framework.appcurve;

import java.util.concurrent.TimeUnit;

import com.newgameplus.framework.debug.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName
     *            name of the test case
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
        System.out.println("~o~ AppCurve ~o~");
        System.out.println("Calling AppCurve.main()");
        final String[] dummy = {"", ""};
        AppCurve.main(dummy);

        try {
            final long duree = 3;
            System.out.println("Waiting ... " + duree + " s");
            TimeUnit.SECONDS.sleep(duree);
        }
        catch (InterruptedException e) {
            Logger.debug("testApp() :" + e);
        }

        assertTrue("testApp() finished", true);
        System.out.println("~o~ AppCurve ~o~");
    }
}
