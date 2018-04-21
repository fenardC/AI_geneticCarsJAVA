package com.cf.framework.appcurve;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.newgameplus.framework.debug.Logger;
import com.newgameplus.framework.misc.Vector2D;

/**
    This class demonstrates the code discussed in these two articles:

    http://devmag.org.za/2011/04/05/bzier-curves-a-tutorial/
    http://devmag.org.za/2011/06/23/bzier-path-algorithms/

    Use this code as you wish, at your own risk. If it blows up
    your computer, makes a plane crash, or otherwise cause damage,
    injury, or death, it is not my fault.

    @author Herman Tulleken, dev.mag.org.za

*/

/**
 * Class for representing a Bezier path, and methods for getting suitable points
 * to draw the curve with line segments.
 */
final class BezierPath {
    private static final int SEGMENTS_PER_CURVE = 10;
    private static final float MINIMUM_SQR_DISTANCE = 0.01f;
    /* This corresponds to about 172 degrees, 8 degrees from a straight line. */
    private static final float DIVISION_THRESHOLD = -0.99f;

    private List<Vector2D> controlPoints = new ArrayList<>();

    /* How many Bezier curves in this path? */
    private int curveCount;

    /**
     * Constructs a new empty Bezier curve. Use one of these methods to add
     * points: SetControlPoints, Interpolate, SamplePoints.
     */
    BezierPath() {
        /* More if needed. */
    }

    /**
     * Sets the control points of this Bezier path. Points 0-3 forms the first
     * Bezier curve, points 3-6 forms the second curve, etc.
     */
    void setControlPoints(List<Vector2D> newControlPoints) {
        Logger.debug("setControlPoints" + newControlPoints);

        controlPoints.clear();
        controlPoints.addAll(newControlPoints);
        curveCount = (controlPoints.size() - 1) / 3;

        Logger.debug("setControlPoints() :" + "curveCount: " + curveCount);
    }

    /**
     * Returns the control points for this Bezier curve.
     */
    List<Vector2D> getControlPoints() {
        return controlPoints;
    }

    /**
     * Calculates a Bezier interpolated path for the given points.
     */
    void interpolate(List<Vector2D> segmentPoints, float scale) {

        controlPoints.clear();

        final int segmentPointsSize = segmentPoints.size();

        if (segmentPointsSize < 2) {
            return;
        }

        for (int i = 0; i < segmentPointsSize; i++) {
            /* Is first */
            if (i == 0) {
                Vector2D p1 = segmentPoints.get(i);
                Vector2D p2 = segmentPoints.get(i + 1);
                Vector2D tangent = Vector2D.substract(p2, p1);
                Vector2D q1 = Vector2D.add(p1, tangent.multiply2(scale));
                controlPoints.add(p1);
                controlPoints.add(q1);
            }
            /* last */
            else if (i == (segmentPointsSize - 1)) {
                Vector2D p0 = segmentPoints.get(i - 1);
                Vector2D p1 = segmentPoints.get(i);
                Vector2D tangent = Vector2D.substract(p1, p0);
                Vector2D q0 = Vector2D.substract(p1, tangent.multiply2(scale));
                controlPoints.add(q0);
                controlPoints.add(p1);

            }
            else {
                Vector2D p0 = segmentPoints.get(i - 1);
                Vector2D p1 = segmentPoints.get(i);
                Vector2D p2 = segmentPoints.get(i + 1);
                Vector2D tangent = Vector2D.normalize(Vector2D.substract(p2, p0));
                Vector2D q0 = Vector2D.substract(p1, tangent.multiply2(scale * Vector2D.substract(p1,
                                                 p0).length()));
                Vector2D q1 = Vector2D.add(p1, tangent.multiply2(scale * Vector2D.substract(p2, p1).length()));
                controlPoints.add(q0);
                controlPoints.add(p1);
                controlPoints.add(q1);
            }
        }

        curveCount = (controlPoints.size() - 1) / 3;
    }

    /**
     * Sample the given points as a Bezier path.
     */
    public void samplePoints(List<Vector2D> sourcePoints, float minSqrDistance, float maxSqrDistance,
                             float scale) {

        if (sourcePoints.size() < 2) {
            return;
        }

        Deque<Vector2D> samplePoints = new ArrayDeque<>();
        samplePoints.push(sourcePoints.get(0));

        Vector2D potentialSamplePoint = sourcePoints.get(1);


        for (int i = 2; i < sourcePoints.size(); i++) {
            if ((Vector2D.substract(potentialSamplePoint, sourcePoints.get(i)).lengthSquared() > minSqrDistance)
                    && (Vector2D.substract(samplePoints.peek(),
                                           sourcePoints.get(i)).lengthSquared() > maxSqrDistance)) {
                samplePoints.push(potentialSamplePoint);
            }

            potentialSamplePoint = sourcePoints.get(i);
        }

        /* Now handle last bit of curve */
        /* Last sample point */
        Vector2D p1 = samplePoints.pop();
        /* Second last sample point */
        Vector2D p0 = samplePoints.peek();

        /* BUG: p0 can be null */
        if (p0 != null) {
            Vector2D tangent = Vector2D.normalize(Vector2D.substract(p0, potentialSamplePoint));

            double d2 = Vector2D.substract(potentialSamplePoint, p1).length();
            double d1 = Vector2D.substract(p1, p0).length();

            p1 = Vector2D.add(p1, tangent.multiply2((d1 - d2) / 2));

            samplePoints.push(p1);
            samplePoints.push(potentialSamplePoint);
        }

        interpolate(new ArrayList<>(samplePoints), scale);
    }

    /**
     * Calculates a point on the path.
     *
     * @param curveIndex
     *            The index of the curve that the point is on. For example, the
     *            second curve (index 1) is the curve with control points 3, 4,
     *            5, and 6.
     *
     * @param t
     *            The parameter indicating where on the curve the point is. 0
     *            corresponds to the "left" point, 1 corresponds to the "right"
     *            end point.
     */
    public Vector2D calculateBezierPoint(int curveIndex, float t) {
        int nodeIndex = curveIndex * 3;

        Vector2D p0 = controlPoints.get(nodeIndex);
        Vector2D p1 = controlPoints.get(nodeIndex + 1);
        Vector2D p2 = controlPoints.get(nodeIndex + 2);
        Vector2D p3 = controlPoints.get(nodeIndex + 3);

        return calculateBezierPoint(t, p0, p1, p2, p3);
    }

    /**
     * Gets the drawing points. This implementation simply calculates a certain
     * number of points per curve.
     */
    public List<Vector2D> getDrawingPoints0() {
        List<Vector2D> drawingPoints = new ArrayList<>();

        for (int curveIndex = 0; curveIndex < curveCount; curveIndex++) {
            /* Only do this for the first end point. */
            /* When i != 0, this coincides with the end point of the previous segment */
            if (curveIndex == 0) {
                drawingPoints.add(calculateBezierPoint(curveIndex, 0));
            }

            for (int j = 1; j <= SEGMENTS_PER_CURVE; j++) {
                float t = j / (float) SEGMENTS_PER_CURVE;
                drawingPoints.add(calculateBezierPoint(curveIndex, t));
            }
        }

        return drawingPoints;
    }

    /**
     * Gets the drawing points. This implementation simply calculates a certain
     * number of points per curve.
     *
     * This is a slightly different implementation from the one above.
     */
    public List<Vector2D> getDrawingPoints1() {
        List<Vector2D> drawingPoints = new ArrayList<>();

        for (int i = 0; i < controlPoints.size() - 3; i += 3) {
            Vector2D p0 = controlPoints.get(i);
            Vector2D p1 = controlPoints.get(i + 1);
            Vector2D p2 = controlPoints.get(i + 2);
            Vector2D p3 = controlPoints.get(i + 3);

            /* Only do this for the first end point. */
            /* When i != 0, this coincides with the end point of the previous segment */
            if (i == 0) {
                drawingPoints.add(calculateBezierPoint(0, p0, p1, p2, p3));
            }

            for (int j = 1; j <= SEGMENTS_PER_CURVE; j++) {
                float t = j / (float) SEGMENTS_PER_CURVE;
                drawingPoints.add(calculateBezierPoint(t, p0, p1, p2, p3));
            }
        }

        return drawingPoints;
    }

    /**
     * This gets the drawing points of a Bezier curve, using recursive division,
     * which results in less points for the same accuracy as the above
     * implementation.
     */
    List<Vector2D> getDrawingPoints2() {
        List<Vector2D> drawingPoints = new ArrayList<>();

        for (int curveIndex = 0; curveIndex < curveCount; curveIndex++) {
            List<Vector2D> bezierCurveDrawingPoints = findDrawingPoints(curveIndex);

            /*
             * Remove the fist point, as it coincides with the last point of the
             * previous Bezier curve.
             */
            if (curveIndex != 0) {
                bezierCurveDrawingPoints.remove(0);
            }

            drawingPoints.addAll(bezierCurveDrawingPoints);
        }

        return drawingPoints;
    }

    private List<Vector2D> findDrawingPoints(int curveIndex) {
        List<Vector2D> pointList = new ArrayList<>();

        Vector2D left = calculateBezierPoint(curveIndex, 0);
        Vector2D right = calculateBezierPoint(curveIndex, 1);

        pointList.add(left);
        pointList.add(right);

        findDrawingPoints(curveIndex, 0, 1, pointList, 1);

        return pointList;
    }

    /**
     * @returns The number of points added.
     */
    private int findDrawingPoints(int curveIndex, float t0, float t1, List<Vector2D> pointList,
                                  int insertionIndex) {
        Vector2D left = calculateBezierPoint(curveIndex, t0);
        Vector2D right = calculateBezierPoint(curveIndex, t1);

        if (Vector2D.substract(left, right).lengthSquared() < MINIMUM_SQR_DISTANCE) {
            return 0;
        }

        float tMid = (t0 + t1) / 2;
        Vector2D mid = calculateBezierPoint(curveIndex, tMid);

        Vector2D leftDirection = Vector2D.substract(left, mid);
        Vector2D rightDirection = Vector2D.substract(right, mid);

        if (Vector2D.dot(leftDirection, rightDirection) > DIVISION_THRESHOLD
                || Math.abs(tMid - 0.5f) < 0.0001f) {

            int pointsAddedCount = 0;

            pointsAddedCount += findDrawingPoints(curveIndex, t0, tMid, pointList, insertionIndex);
            pointList.add(insertionIndex + pointsAddedCount, mid);

            pointsAddedCount++;
            pointsAddedCount += findDrawingPoints(curveIndex, tMid, t1, pointList,
                                                  insertionIndex + pointsAddedCount);
            return pointsAddedCount;
        }

        return 0;
    }

    /**
     * Calculates a point on the Bezier curve represented with the four control
     * points given.
     */
    private static Vector2D calculateBezierPoint(float t, Vector2D p0, Vector2D p1, Vector2D p2,
            Vector2D p3) {
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;

        Vector2D p = p0.multiply2(uuu); // first term

        p.add(p1.multiply2(3 * uu * t)); // second term
        p.add(p2.multiply2(3 * u * tt)); // third term
        p.add(p3.multiply2(ttt)); // fourth term

        return p;
    }
}
