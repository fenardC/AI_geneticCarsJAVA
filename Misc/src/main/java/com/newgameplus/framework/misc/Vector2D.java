package com.newgameplus.framework.misc;

public final class Vector2D {
    public Vector2D(double valueX, double valueY) {
        super();
        this.x = valueX;
        this.y = valueY;
    }

    public Vector2D(final Vector2D other) {
        this(other.x, other.y);
    }

    public static Vector2D add(final Vector2D vector1, final Vector2D vector2) {
        return new Vector2D(vector1.x + vector2.x, vector1.y + vector2.y);
    }

    public static double dot(final Vector2D vector1, final Vector2D vector2) {
        return (vector1.x * vector2.x) + (vector1.y * vector2.y);
    }

    public static Vector2D getVector2DFromValueAngle(final double length, final double angleInDegree) {
        final double angle = Math.toRadians(angleInDegree);
        return new Vector2D(length * Math.cos(angle), length * Math.sin(angle));
    }

    /**
     * @return Returns vector with a magnitude of 1.
     */
    public static Vector2D normalize(final Vector2D vector) {
        final Double length = new Double(vector.length());
        Vector2D result;

        if (length.intValue() != 0) {
            result = new Vector2D(vector.x / length.doubleValue(), vector.y / length.doubleValue());
        }
        else {
            result = new Vector2D(0, 0);
        }

        return result;
    }

    public static Vector2D rotate(final Vector2D vector, final double angleInDegree) {
        final double angle = Math.toRadians(angleInDegree);
        return new Vector2D((vector.x * Math.cos(angle)) - (vector.y * Math.sin(angle)),
                            (vector.x * Math.sin(angle)) + (vector.y * Math.cos(angle)));
    }

    public static Vector2D substract(final Vector2D vector1, final Vector2D vector2) {
        return new Vector2D(vector1.x - vector2.x, vector1.y - vector2.y);
    }

    public Vector2D add(final Vector2D vector) {
        x = x + vector.x;
        y = y + vector.y;
        return this;
    }

    /**
     * @return Angle in degree.
     */
    public double getOrientation() {
        return Math.toDegrees(Math.atan2(y, x));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

//    public Vector2D multiply(final double ratio) {
//        final double angle = getOrientation();
//        final Vector2D result = getVector2DFromValueAngle(ratio * length(), angle);
//        return new Vector2D(result);
//    }

    /**
     * @return Returns the length of this vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * @return Returns the square of the length of this vector
     */
    public double lengthSquared() {
        return x * x + y * y;
    }

    public Vector2D multiply(final double ratio) {
        return new Vector2D(this.multiply2(ratio));
    }

    public Vector2D multiply2(final double ratio) {
        return new Vector2D(x * ratio, y * ratio);
    }

    @Override
    public String toString() {
        return new String("Vector2D(" + x + ", " + y + ")");
    }

    private double x;

    private double y;
}
