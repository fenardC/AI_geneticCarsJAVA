package com.cf.framework.appcurve;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.debug.Logger;
import com.newgameplus.framework.misc.Vector2D;

@SuppressWarnings("serial")
final class DrawerOnCanvas extends Canvas implements MouseListener, MouseMotionListener {

    enum Mode {
        /* Draws Line Segments at points */
        LINE,
        /* Interprets points as control points of Bezier curve */
        BEZIER,
        /* Interpolates */
        BEZIER_INTERPOLATED,
        BEZIER_REDUCED,
        UNDEFINED
    }

    private Mode mode;
    private transient List<Vector2D> points = new ArrayList<>();
    private int mouseX = 0;
    private int mouseY = 0;

    DrawerOnCanvas() {
        setMode(Mode.UNDEFINED);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void bezierInterpolate(Graphics g) {
        BezierPath bezierPath = new BezierPath();
        bezierPath.interpolate(points, .25f);
        List<Vector2D> drawingPoints = bezierPath.getDrawingPoints2();
        setLinePoints(g, drawingPoints);
    }

    private void bezierReduce(Graphics g) {
        BezierPath bezierPath = new BezierPath();
        bezierPath.samplePoints(points, 10, 1000, 0.33f);
        List<Vector2D> drawingPoints = bezierPath.getDrawingPoints2();
        setLinePoints(g, drawingPoints);
    }

    void clearAllPoints() {
        points.clear();
    }

    void dumpAllPoints() {
        BezierPath bezierPath = new BezierPath();
        bezierPath.interpolate(points, .25f);
        List<Vector2D> drawingPoints = bezierPath.getDrawingPoints2();

        Logger.debug("List<Vector2D> points = new ArrayList<>();");

        for (Vector2D v : drawingPoints) {
            Logger.debug("points.add(new Vector2D(" + v.getX() + ", " + v.getY() + "));");
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        if (MouseEvent.BUTTON1 == arg0.getButton()) {
            mouseX = arg0.getX();
            mouseY = arg0.getY();
            Vector2D screenPosition = new Vector2D(mouseX, mouseY);
            points.add(screenPosition);
            /* Display points. */
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        /* Add more if needed. */
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        /* Add more if needed. */
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        /* Add more if needed. */
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        mouseX = arg0.getX();
        mouseY = arg0.getY();
        /* Display this position as a string. */
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        /* Add more if needed. */
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        /* Add more if needed. */
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawString("(" + mouseX + ", " + mouseY + ")", mouseX, mouseY);

        switch (mode) {
            case LINE:
                renderLineSegments(g);
                break;

            case BEZIER:
                renderBezier(g);
                break;

            case BEZIER_INTERPOLATED:
                bezierInterpolate(g);
                break;

            case BEZIER_REDUCED:
                bezierReduce(g);
                break;

            case UNDEFINED:
            default:
        }
    }

    private void renderBezier(Graphics g) {
        BezierPath bezierPath = new BezierPath();
        bezierPath.setControlPoints(points);
        List<Vector2D> drawingPoints = bezierPath.getDrawingPoints2();
        setLinePoints(g, drawingPoints);
    }

    private void renderLineSegments(Graphics g) {
        setLinePoints(g, points);
    }

    private void setLinePoints(Graphics g, List<Vector2D> drawingPoints) {
        if (!drawingPoints.isEmpty()) {
            Color color = getForeground();
            g.setColor(Color.BLUE);

            final int size = drawingPoints.size();

            for (int i = 0; i < (size - 1); i++) {
                double x1 = drawingPoints.get(i).getX();
                double y1 = drawingPoints.get(i).getY();
                double x2 = drawingPoints.get(i + 1).getX();
                double y2 = drawingPoints.get(i + 1).getY();

                g.setColor(Color.RED);
                g.drawRect((int)x1, (int)y1, 3, 3);
                g.setColor(Color.GREEN);
                g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
            }

            if (size > 1) {
                double x = drawingPoints.get(size - 1).getX();
                double y = drawingPoints.get(size - 1).getY();
                g.setColor(Color.RED);
                g.drawRect((int)x, (int)y, 3, 3);
            }

            g.setColor(color);
        }
    }

    void setMode(Mode mode) {
        this.mode = mode;
    }
}
