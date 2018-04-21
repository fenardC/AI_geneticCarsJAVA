package com.newgameplus.frameworkdemo.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.draw.Drawer;
import com.newgameplus.framework.misc.BezierSpline2D;
import com.newgameplus.framework.misc.Triple;
import com.newgameplus.framework.misc.Vector2D;

public class Track {
    private static final boolean DEBUG_ENABLED = false;
    private String name;
    private BezierSpline2D spline;
    private double splineWidth = 60;
    private double maxLapMillis = 0;

    private List<Triple<Vector2D, Vector2D, Vector2D>> listTriangle = new ArrayList<>();

    public Track(String name, BezierSpline2D spline, double splineWidth, double maxLapMillis) {
        this.name = name;
        this.spline = spline;
        this.splineWidth = splineWidth;
        this.maxLapMillis = maxLapMillis;
        init();
    }

    private static boolean pointInTriangle(Vector2D p, Vector2D p1, Vector2D p2, Vector2D p3) {
        boolean result;
        boolean b1 = sign(p, p1, p2) < 0;
        boolean b2 = sign(p, p2, p3) < 0;
        boolean b3 = sign(p, p3, p1) < 0;
        result = (b1 == b2) && (b2 == b3);
        return result;
    }

    private static double sign(Vector2D p1, Vector2D p2, Vector2D p3) {
        return (p1.getX() - p3.getX()) * (p2.getY() - p3.getY()) -
               (p2.getX() - p3.getX()) * (p1.getY() - p3.getY());
    }

    public List<Triple<Vector2D, Vector2D, Vector2D>> getListTriangle() {
        return listTriangle;
    }

    public double getMaxLapMillis() {
        return maxLapMillis;
    }

    public String getName() {
        return name;
    }

    public BezierSpline2D getSpline() {
        return spline;
    }

    public double getSplineWidth() {
        return splineWidth;
    }

    private void init() {
        listTriangle.clear();
        List<Vector2D> listPoint = spline.getListResultPoint();
        List<Vector2D> listPerp = spline.getListResultPerpendicular();

        final int listPointSize = listPoint.size();
        Vector2D pointFirst;
        Vector2D pointSecond;
        Vector2D perpFirst;
        Vector2D perpSecond;
        Vector2D perp1;
        Vector2D perp2;
        Vector2D v11;
        Vector2D v12;
        Vector2D v21;
        Vector2D v22;

        for (int i = 0; i < listPointSize; i++) {
            if (i < listPoint.size() - 1 || BezierSpline2D.isClose()) {

                if (i == listPointSize - 1 && BezierSpline2D.isClose()) {
                    perpFirst = new Vector2D(listPerp.get(i));
                    perpSecond = new Vector2D(listPerp.get(0));
                    pointFirst = new Vector2D(listPoint.get(i));
                    pointSecond = new Vector2D(listPoint.get(0));
                }
                else {
                    perpFirst = new Vector2D(listPerp.get(i));
                    perpSecond = new Vector2D(listPerp.get(i + 1));
                    pointFirst = new Vector2D(listPoint.get(i));
                    pointSecond = new Vector2D(listPoint.get(i + 1));
                }

                perp1 = perpFirst.multiply(splineWidth / 2);
                perp2 = perpSecond.multiply(splineWidth / 2);

                v11 = Vector2D.add(pointFirst, perp1);
                v12 = Vector2D.substract(pointFirst, perp1);
                v21 = Vector2D.add(pointSecond, perp2);
                v22 = Vector2D.substract(pointSecond, perp2);

                listTriangle.add(new Triple<>(new Vector2D(v11),  new Vector2D(v12),
                                              new Vector2D(v21)));
                listTriangle.add(new Triple<>(new Vector2D(v22),  new Vector2D(v12),
                                              new Vector2D(v21)));
            }
        }
    }

    public int isPointInTrack(Vector2D pos) {
        for (int i = 0; i < listTriangle.size(); i++) {
            Triple<Vector2D, Vector2D, Vector2D> tri = listTriangle.get(i);

            if (pointInTriangle(pos, tri.getFirst(), tri.getSecond(), tri.getThird())) {
                return i;
            }
        }

        return -1;
    }
    public void renderCenterLine(Drawer d) {
        List<Vector2D> listPoint = spline.getListResultPoint();
        final int listPointSize = listPoint.size();
        /* Current point. */
        Vector2D current;
        /* After current point. */
        Vector2D next;

        d.setColor(Color.WHITE);

        for (int i = 0; i < listPointSize; i++) {
            if (i == 0) {
                /* Is first */
                current = listPoint.get(i);
                next = listPoint.get(1);

            }
            else if (i == (listPointSize - 1)) {
                /* last */
                current = listPoint.get(i);
                next = listPoint.get(0);

            }
            else {
                /* others */
                current = listPoint.get(i);
                next = listPoint.get(i + 1);
            }

            d.drawDashedLine(current.getX(), current.getY(), next.getX(), next.getY());
        }
    }
    public void renderDebug(Drawer drawer) {
        for (Triple<Vector2D, Vector2D, Vector2D> t : listTriangle) {
            final double firstX = t.getFirst().getX();
            final double firstY = t.getFirst().getY();
            final double secondX = t.getSecond().getX();
            final double secondY = t.getSecond().getY();
            final double thirdX = t.getThird().getX();
            final double thirdY = t.getThird().getY();

            if (DEBUG_ENABLED) {
                drawer.setColor(Color.GREEN);

                drawer.drawLine(firstX, firstY, secondX, secondY);
                //
                drawer.drawLine(secondX, secondY, thirdX, thirdY);
                drawer.drawLine(firstX, firstY, thirdX, thirdY);
            }
            else {
                drawer.setColor(Color.LIGHT_GRAY);
                int[] pointsX = { (int) firstX, (int) secondX, (int) thirdX };
                int[] pointsY = { (int) firstY, (int) secondY, (int) thirdY };
                drawer.fillPolygon(pointsX, pointsY, pointsX.length);
            }
        }
    }

    public void renderSplineDebug(Drawer drawer) {
        if (DEBUG_ENABLED) {
            int index = 0;

            for (Vector2D point : spline.getListResultPoint()) {
                drawer.setColor(Color.GREEN);
                drawer.drawRect(point.getX(), point.getY(), 5, 5);
                drawer.setColor(Color.BLUE);
                drawer.drawRect(point.getX() + 5 * spline.getListResultPerpendicular().get(index).getX(),
                                point.getY() + 5 * spline.getListResultPerpendicular().get(index).getY(), 5, 9);
                index ++;
            }
        }
    }


    public void renderStart(Drawer d) {
        Vector2D perpFirst = new Vector2D(spline.getListResultPerpendicular().get(0));
        Vector2D pointFirst = new Vector2D(spline.getListResultPoint().get(0));
        Vector2D perp1 = perpFirst.multiply(splineWidth);
        Vector2D v11 = Vector2D.add(pointFirst, perp1);
        Vector2D v12 = Vector2D.substract(pointFirst, perp1);
        d.setColor(Color.RED);
        d.drawLine(v11.getX(), v11.getY(), v12.getX(), v12.getY());
    }
}
