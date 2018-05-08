package com.cf.framework.appcurve;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

import com.newgameplus.framework.debug.Logger;
import java.awt.Component;

/**
 * This class demonstrates the code discussed in these two articles:
 *
 * http://devmag.org.za/2011/04/05/bzier-curves-a-tutorial/
 * http://devmag.org.za/2011/06/23/bzier-path-algorithms/
 *
 */

public class AppCurve {

    private static final String FONT_VERDANA = "Verdana";
    private JFrame mainFrame;

    private DrawerOnCanvas drawer;

    private final ButtonGroup modeButtonGroup = new ButtonGroup();

    private JPanel southPanel;
    private JButton clearPush1;
    private JButton dumpPush2;

    public AppCurve() {
        super();
        init();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void init() {
        mainFrame = new JFrame("Tool for bezier curves");
        mainFrame.setFont(new Font(FONT_VERDANA, Font.PLAIN, 16));
        mainFrame.setBounds(100, 100, 1300, 1100);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

        final JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
        mainPanel.setLayout(new BorderLayout(10, 10));

        drawer = new DrawerOnCanvas();
        drawer.setFont(new Font(FONT_VERDANA, Font.PLAIN, 16));
        drawer.setForeground(Color.RED);
        drawer.setBackground(Color.BLACK);

        final JPanel northPanel = new JPanel();
        northPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        northPanel.setToolTipText("Select mode.");
        northPanel.setPreferredSize(new Dimension(150, 100));
        northPanel.setLayout(new GridLayout(5, 1, 5, 5));

        final JLabel modeSelection = new JLabel("Mode");
        modeSelection.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeSelection.setFont(new Font(FONT_VERDANA, Font.PLAIN, 14));
        modeSelection.setToolTipText("The different mode the user can select thanks to the buttons below.");

        final JRadioButton modeButton1 = new JRadioButton("Line mode");
        modeButton1.setFont(new Font(FONT_VERDANA, Font.PLAIN, 12));
        modeButton1.setToolTipText("Line Segments (Click to add points)");
        modeButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawer.setMode(DrawerOnCanvas.Mode.LINE);
            }
        });

        final JRadioButton modeButton4 = new JRadioButton("Bezier Sampling mode");
        modeButton4.setPreferredSize(new Dimension(180, 23));
        modeButton4.setFont(new Font(FONT_VERDANA, Font.PLAIN, 12));
        modeButton4.setToolTipText("Bezier sampling / reduction (Drag to add points)");
        modeButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawer.setMode(DrawerOnCanvas.Mode.BEZIER_REDUCED);
            }
        });

        southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 0, 0, 0));

        clearPush1 = new JButton("Clear");
        clearPush1.setFont(new Font(FONT_VERDANA, Font.PLAIN, 12));
        clearPush1.setToolTipText("Clear all points.");
        clearPush1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawer.clearAllPoints();
            }
        });

        dumpPush2 = new JButton("Dump");
        dumpPush2.setFont(new Font(FONT_VERDANA, Font.PLAIN, 12));
        dumpPush2.setToolTipText("Dump points");
        dumpPush2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawer.dumpAllPoints();
            }
        });

        final JRadioButton modeButton2 = new JRadioButton("Bezier mode");
        modeButton2.setFont(new Font(FONT_VERDANA, Font.PLAIN, 12));
        modeButton2.setToolTipText("Bezier curve (Click to add points)");
        modeButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawer.setMode(DrawerOnCanvas.Mode.BEZIER);
            }
        });

        final JRadioButton modeButton3 = new JRadioButton("Bezier Interpolation mode");
        modeButton3.setFont(new Font(FONT_VERDANA, Font.PLAIN, 12));
        modeButton3.setToolTipText("Bezier interpolation (Click to add points)");
        modeButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                drawer.setMode(DrawerOnCanvas.Mode.BEZIER_INTERPOLATED);
            }
        });

        modeButtonGroup.add(modeButton1);
        modeButtonGroup.add(modeButton2);
        modeButtonGroup.add(modeButton3);
        modeButtonGroup.add(modeButton4);

        drawer.setMode(DrawerOnCanvas.Mode.LINE);
        modeButton1.setSelected(true);

        northPanel.add(modeSelection);
        northPanel.add(modeButton1);
        northPanel.add(modeButton2);
        northPanel.add(modeButton3);
        northPanel.add(modeButton4);

        southPanel.add(clearPush1);
        southPanel.add(dumpPush2);

        mainPanel.add(drawer, BorderLayout.CENTER);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        mainFrame.getContentPane().add(mainPanel);
    }

    /**
     * Launch the application.
     *
     */
    public static void main(final String[] args) {
        Logger.debug("main()>");

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final AppCurve window = new AppCurve();
                    window.mainFrame.setVisible(true);
                }
                catch (Exception e) {
                    Logger.debug("main()" + e);
                }
            }
        });
    }
}
