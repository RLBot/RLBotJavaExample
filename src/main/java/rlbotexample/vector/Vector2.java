package rlbotexample.vector;

/**
 * A vector that only knows about x and y components.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can add to it as much
 * as you want, or delete it.
 */
public class Vector2 {

    public final double x;
    public final double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 plus(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 minus(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 scaled(double scale) {
        return new Vector2(x * scale, y * scale);
    }

    /**
     * If magnitude is negative, we will return a vector facing the opposite direction.
     */
    public Vector2 scaledToMagnitude(double magnitude) {
        if (isZero()) {
            throw new IllegalStateException("Cannot scale up a vector with length zero!");
        }
        double scaleRequired = magnitude / magnitude();
        return scaled(scaleRequired);
    }

    public double distance(Vector2 other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    /**
     * This is the length of the vector.
     */
    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public double magnitudeSquared() {
        return x * x + y * y;
    }

    public Vector2 normalized() {

        if (isZero()) {
            throw new IllegalStateException("Cannot normalize a vector with length zero!");
        }
        return this.scaled(1 / magnitude());
    }

    public double dotProduct(Vector2 other) {
        return x * other.x + y * other.y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    /**
     * The correction angle is how many radians you need to rotate this vector to make it line up with the "ideal"
     * vector. This is very useful for deciding which direction to steer.
     */
    public double correctionAngle(Vector2 ideal) {
        double currentRad = Math.atan2(y, x);
        double idealRad = Math.atan2(ideal.y, ideal.x);

        if (Math.abs(currentRad - idealRad) > Math.PI) {
            if (currentRad < 0) {
                currentRad += Math.PI * 2;
            }
            if (idealRad < 0) {
                idealRad += Math.PI * 2;
            }
        }

        return idealRad - currentRad;
    }

    /**
     * Will always return a positive value <= Math.PI
     */
    public static double angle(Vector2 a, Vector2 b) {
        return Math.abs(a.correctionAngle(b));
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }
}
