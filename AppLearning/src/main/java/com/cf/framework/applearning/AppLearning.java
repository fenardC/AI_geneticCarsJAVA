package com.cf.framework.applearning;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.newgameplus.framework.debug.Logger;
import com.newgameplus.framework.draw.Drawer;

final class AppLearning {
    private static final String FONT_VERDANA = "Verdana";

    private JFrame mainFrame;
    private Canvas canvas;
    private Drawer drawer;
    private CarsLearningWorker worker;

    private AppLearning() {
        super();
        initialize();
        worker = new CarsLearningWorker(false, drawer);
    }

    /* Initialize the contents of the frame. */
    private void initialize() {
        mainFrame = new JFrame("The car learning application");
        mainFrame.setMaximumSize(new Dimension(1280, 1024));
        mainFrame.setResizable(false);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent arg0) {
                Logger.debug("windowClosed()>");
                worker.stopRunning();
            }

            @Override
            public void windowClosing(WindowEvent arg0) {
                Logger.debug("windowClosing()>");
                worker.stopRunning();
            }
        });

        mainFrame.setFont(new Font(FONT_VERDANA, Font.PLAIN, 16));
        mainFrame.setBounds(10, 10, 1300, 1100);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

        final JPanel mainPanel = new JPanel();
        mainPanel.setForeground(Color.GRAY);
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setFont(new Font(FONT_VERDANA, Font.PLAIN, 14));
        mainPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        mainPanel.setLayout(new BorderLayout(0, 0));

        canvas = new Canvas() {
            /* Drop painting operation by AWT system. */
            @Override
            public void paint(Graphics g) {
                //Logger.debug("paint()>");
            }
        };

        canvas.setSize(new Dimension(1280, 1024));
        canvas.setIgnoreRepaint(true);
        canvas.setFont(new Font(FONT_VERDANA, Font.BOLD | Font.ITALIC, 14));

        mainPanel.add(canvas, BorderLayout.WEST);
        mainFrame.getContentPane().add(mainPanel);

        drawer = new Drawer(canvas);
    }

    private void startWorker() {
        worker.startRunning();
    }

    /* Launch the application. */
    public static void main(final String[] args) {
        Logger.debug("main()>");
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final AppLearning learningApp = new AppLearning();
                    learningApp.mainFrame.setVisible(true);
                    /* Before being able to do that, the Canvas has to be visible. */
                    learningApp.canvas.createBufferStrategy(2);
                    learningApp.startWorker();
                }
                catch (Exception e) {
                    Logger.debug("main() " + e);
                }
            }
        });
    }
}
