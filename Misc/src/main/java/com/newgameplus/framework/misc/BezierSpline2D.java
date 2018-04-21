package com.newgameplus.framework.misc;

import java.util.ArrayList;
import java.util.List;

public final class BezierSpline2D {

    public BezierSpline2D(List<Couple<Vector2D, Vector2D>> pointsAndPerpList) {
        for (Couple<Vector2D, Vector2D> c : pointsAndPerpList) {
            listResultPoint.add(new Vector2D(c.getFirst()));
            listResultPerpendicular.add(new Vector2D(c.getSecond()));
        }
    }

    public List<Vector2D> getListResultPerpendicular() {
        return listResultPerpendicular;
    }

    public List<Vector2D> getListResultPoint() {
        return listResultPoint;
    }

    public static boolean isClose() {
        return CLOSE;
    }

    private static final boolean CLOSE = true;

    private List<Vector2D> listResultPoint = new ArrayList<>();

    private List<Vector2D> listResultPerpendicular = new ArrayList<>();
}
