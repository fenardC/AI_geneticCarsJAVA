package com.newgameplus.framework.draw;

import java.awt.Color;
import java.awt.image.AffineTransformOp;

public final class TextureModifier {
    public static final int ANTIALIASING_HIGH = AffineTransformOp.TYPE_BILINEAR;

    public TextureModifier() {
        super();
        setAngle(0);
        setCenter(0, 0);
        setColor(Color.WHITE);
        setRatio(1);
    }

    public double getAngle() {
        return angle;
    }

    public int getAntialiasing() {
        return antialiasing;
    }

    public Color getColor() {
        return color;
    }

    public int getH() {
        return h;
    }

    public double getRatio() {
        return ratio;
    }

    public int getW() {
        return w;
    }

    public void setAngle(final double angle) {
        this.angle = angle;
    }

    public void setAntialiasing(final int antialiasing) {
        this.antialiasing = antialiasing;
    }

    public void setCenter(final int width, final int height) {
        w = width;
        h = height;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public void setRatio(final double ratio) {
        this.ratio = ratio;
    }

    private double angle;

    private int antialiasing = ANTIALIASING_HIGH;

    private int w;

    private int h;

    private Color color;

    private double ratio = 1;
}
