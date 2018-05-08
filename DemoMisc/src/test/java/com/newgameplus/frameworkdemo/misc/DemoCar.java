package com.newgameplus.frameworkdemo.misc;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import com.newgameplus.framework.debug.Logger;
import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.misc.Misc;
import com.newgameplus.framework.misc.Vector2D;
import com.newgameplus.frameworkdemo.gui.ScreenGeneticCar;

class DemoCar {
    private static final double CAR_MILLIS_PER_TICK = 20;
    private static final double CAR_RATIO = 0.5;
    private static final int CAR_SENSOR_TYPE = ScreenGeneticCar.TYPE_SENSOR_LINE;
    private static final int TASK_PERFORMER_DELAY_IN_MS = 20;

    private JFrame mainFrame;
    private Canvas canvas;
    private Drawer drawer;
    private Car car;
    private int command = 0;

    DemoCar() {
        super();
        initialize();

        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                /* Clear graphics of drawer. */
                drawer.clear();
                drawer.setColor(Color.WHITE);
                drawer.drawRect(10, 10, 50, 25);
                drawer.setColor(Color.GREEN);
                drawer.drawString("Command : " + command, 10, 70);
                moveCar();
                drawer.show();
            }
        };

        new Timer(DemoCar.TASK_PERFORMER_DELAY_IN_MS, taskPerformer).start();
    }

    private void initialize() {
        mainFrame = new JFrame();
        mainFrame.setTitle("Demo for car motion");
        mainFrame.setBounds(100, 100, 943, 623);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

        final JPanel mainPanel = new JPanel();
        mainPanel.setFont(new Font("Verdana", Font.PLAIN, 14));
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));

        mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new BorderLayout(0, 0));

        canvas = new Canvas() {
            /* Drop painting operation by AWT system. */
            @Override
            public void paint(Graphics g) {
                //Logger.debug("paint()>");
            }
        };

        canvas.setIgnoreRepaint(true);
        canvas.setForeground(Color.WHITE);
        canvas.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 16));
        canvas.setBackground(Color.BLACK);
        mainPanel.add(canvas);

        drawer = new Drawer(canvas);

        car = new Car("Car_" + 0, CAR_SENSOR_TYPE, CAR_RATIO);
        car.setColor(Color.RED);
        car.setPosition(new Vector2D(400, 200));
        car.setAngle(90);
    }

    private void moveCar() {
        /* Update Car with commands */
        final int engineCommand = Misc.mix(-128.0, 128.0, command);
        final int wheelCommand = Misc.mix(-128.0, 128.0, command);
        command++;

        if (command > 128) {
            command = -128;
        }

        car.setEngineCommand(engineCommand);
        car.setWheelCommand(wheelCommand);
        car.testTick(CAR_MILLIS_PER_TICK);
        car.testRender(drawer);
    }

    /**
     * Launch the application.
     */
    static void main(final String[] args) {
        Logger.debug("main()>");
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final DemoCar demo = new DemoCar();
                    demo.mainFrame.setVisible(true);
                    /* Before being able to do that, the Canvas has to be visible. */
                    demo.canvas.createBufferStrategy(2);
                }
                catch (Exception e) {
                    Logger.debug("main() " + e);
                }
            }
        });
    }
}
