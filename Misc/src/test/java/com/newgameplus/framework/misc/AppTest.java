package com.newgameplus.framework.misc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {

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
        final double delta = 1E-15;
        final double sqrtOf2 = Math.sqrt(2.0);
        {
            final Vector2D vector1 = new Vector2D(0, 1);
            final Vector2D vector1Same = new Vector2D(vector1);
            assertEquals("Vector2D()", vector1Same.getX(), vector1.getX(), delta);
            assertEquals("Vector2D()", vector1Same.getY(), vector1.getY(), delta);
        }

        {
            final Vector2D vector1 = new Vector2D(1, 1);
            final Vector2D vector2 = new Vector2D(-1, -1);
            final Vector2D vector12 = Vector2D.add(vector1, vector2);
            assertEquals("add()", vector12.getX(), 0.0, delta);
            assertEquals("add()", vector12.getY(), 0.0, delta);
        }

        {
            final Vector2D vector1 = new Vector2D(1.1, 1.1);
            final Vector2D vector2 = new Vector2D(1.0, 1.0);
            final Vector2D vector12 = Vector2D.substract(vector1, vector2);
            assertEquals("substract()", vector12.getX(), 0.1, delta);
            assertEquals("substract()", vector12.getY(), 0.1, delta);
        }

        {
            final Vector2D vector1 = new Vector2D(0.0, 1.0);
            final Vector2D vector1Same = Vector2D.getVector2DFromValueAngle(1.0, 90.0);
            assertEquals("getVector2DFromValueAngle()", vector1Same.getX(), vector1.getX(), delta);
            assertEquals("getVector2DFromValueAngle()", vector1Same.getY(), vector1.getY(), delta);
            final double v1Orientation = vector1.getOrientation();
            assertEquals("getOrientation()", v1Orientation, 90.0, delta);
        }

        {
            final Vector2D vector1 = new Vector2D(0.0, 4.0);
            final Vector2D vector1Normalized = Vector2D.normalize(vector1);
            assertEquals("normalize()", vector1Normalized.getX(), 0.0, delta);
            assertEquals("normalize()", vector1Normalized.getY(), 1.0, delta);

            final double len = vector1.length();
            assertEquals("length()", len, 4.0, delta);

            final double lenSquared = vector1.lengthSquared();
            assertEquals("lengthSquared()", lenSquared, 16.0, delta);
        }

        {
            final Vector2D vector1 = new Vector2D(0.0, 2.0);
            final Vector2D vector1Rotated = Vector2D.rotate(vector1, 45.0);
            assertEquals("rotate()", vector1Rotated.getX(), -sqrtOf2, delta);
            assertEquals("rotate()", vector1Rotated.getY(), sqrtOf2, delta);
        }

        {
            final Vector2D vector1 = new Vector2D(2.0, 2.0);
            final Vector2D vector1Multiplied = vector1.multiply(0.5);
            assertEquals("multiply()", vector1Multiplied.getX(), 1.0, delta);
            assertEquals("multiply()", vector1Multiplied.getY(), 1.0, delta);

            final Vector2D vector1Multipli2ed = vector1.multiply2(0.5);
            assertEquals("multiply2()", vector1Multipli2ed.getX(), 1.0, delta);
            assertEquals("multiply2()", vector1Multipli2ed.getY(), 1.0, delta);
        }

        {
            for (int i = 0; i < 10 ; i++) {
                boolean res = Misc.random(50.0);
                System.out.println("Misc.random(50.0) returned: " + res);
            }
        }

        {
            for (int i = 0; i < 10 ; i++) {
                double res = Misc.random(0.0, 5.0);
                System.out.println("Misc.random(double, double) returned: " + res);
            }
        }

        {
            for (int i = 0; i < 10 ; i++) {
                int res = Misc.random(0, 5);
                System.out.println("Misc.random(int, int) returned: " + res);
            }
        }
    }
}
