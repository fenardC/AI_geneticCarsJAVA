package com.cf.framework.trackdata;

import java.util.ArrayList;
import java.util.List;

import com.newgameplus.framework.debug.Logger;
import com.newgameplus.framework.misc.Couple;
import com.newgameplus.framework.misc.Vector2D;

public abstract class TrackData {
    public TrackData(String name) {
        this.name = name;
    }

    public void debug() {
        if (DEBUG_ENABLED) {
            for (Couple<Vector2D, Vector2D> c : pointsAndPerpList) {
                Logger.debug(c.getFirst().toString() + " | " + c.getSecond().toString());
            }
        }
    }

    public List<Couple<Vector2D, Vector2D>> getPointsAndPerpList() {
        return pointsAndPerpList;
    }

    public String getName() {
        return name;
    }

    protected void translate(final int deltaX, final int deltaY) {
        final Vector2D translator = new Vector2D(deltaX, deltaY);

        for (Vector2D p : points) {
            p.add(translator);
        }
    }

    protected void computePointsAndPerpList() {
        final int pointsSize = points.size();
        /* Before current point. */
        Vector2D previous;
        /* After current point. */
        Vector2D next;

        for (int i = 0; i < pointsSize; i++) {
            if (i == 0) {
                /* Is first */
                previous = points.get(pointsSize - 1);
                next = points.get(1);

            }
            else if (i == (pointsSize - 1)) {
                /* last */
                previous = points.get(pointsSize - 2);
                next = points.get(0);

            }
            else {
                /* others */
                previous = points.get(i - 1);
                next = points.get(i + 1);
            }

            pointsAndPerpList.add(new Couple<>(points.get(i), computePerp(previous, next, true)));
        }
    }

    private static Vector2D computePerp(Vector2D previous, Vector2D next, boolean onRight) {
        Vector2D tangent = Vector2D.substract(next, previous);
        return Vector2D.normalize(new Vector2D((onRight ? -1 : 1) * tangent.getY(),
                                               (onRight ? 1 : -1) * tangent.getX()));
    }

    private static final boolean DEBUG_ENABLED = false;
    protected List<Vector2D> points = new ArrayList<>();
    protected List<Couple<Vector2D, Vector2D>> pointsAndPerpList = new ArrayList<>();
    private String name;

}
