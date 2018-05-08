package com.newgameplus.framework.draw;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    final JFrame mainFrame = new JFrame("Testing drawer");
    final JPanel mainPanel = new JPanel();
    final Canvas canvas = new Canvas() {
        /* Drop painting operation by AWT system. */
        @Override
        public void paint(Graphics g) {
            //Logger.debug("paint()>");
        }
    };


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

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                canvas.setIgnoreRepaint(true);
                canvas.setForeground(Color.WHITE);
                canvas.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 16));

                mainPanel.setBackground(Color.BLACK);
                mainPanel.setLayout(new BorderLayout(0, 0));
                mainPanel.add(canvas);

                mainFrame.setBounds(10, 10, 500, 500);
                mainFrame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
                mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);

                //System.out.println("run() : " + EventQueue.isDispatchThread());
            }
        });


        try {
            final long duree = 1;
            System.out.println("Waiting ... " + duree + " s");
            TimeUnit.SECONDS.sleep(duree);
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }

        final Drawer drawer = new Drawer(canvas);

        mainFrame.setVisible(true);
        canvas.createBufferStrategy(2);

        // System.out.println("testApp() : " + EventQueue.isDispatchThread());
        TestDraw tester = new TestDraw();

        tester.testDraw(drawer);
        assertTrue("testApp() finished", true);
    }
}
