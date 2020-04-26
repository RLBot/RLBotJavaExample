package rlbotexample.vector;

import com.google.flatbuffers.FlatBufferBuilder;

/**
 * A simple 3d vector class with the most essential operations.
 *
 * This class is here for your convenience, it is NOT part of the framework. You
 * can add to it as much as you want, or delete it.
 */
public class Vector3 extends rlbot.vector.Vector3 {

	public static final Vector3 X = new Vector3(1, 0, 0);
	public static final Vector3 Y = new Vector3(0, 1, 0);
	public static final Vector3 Z = new Vector3(0, 0, 1);

	public Vector3(double x, double y, double z) {
		super((float) x, (float) y, (float) z);
	}

	public Vector3() {
		this(0, 0, 0);
	}

	public Vector3(rlbot.flat.Vector3 vec) {
		// Invert the X value so that the axes make more sense.
		this(-vec.x(), vec.y(), vec.z());
	}

	public int toFlatbuffer(FlatBufferBuilder builder) {
		// Invert the X value again so that rlbot sees the format it expects.
		return rlbot.flat.Vector3.createVector3(builder, -x, y, z);
	}

	public Vector3 plus(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	public Vector3 minus(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	public Vector3 scaled(double scale) {
		return new Vector3(x * scale, y * scale, z * scale);
	}

	public double dot(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 add(float other) {
        return new Vector3(x + other, y + other, z + other);
    }

    public Vector3 add(Vector2 other, float z) {
        return new Vector3(x + other.x, y + other.y, this.z + z);
    }

    public Vector3 add(float x, float y, float z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 sub(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    public Vector3 sub(float xS, float yS, float zS) {
        return new Vector3(x - xS, y - yS, z - zS);
    }

    public Vector3 sub(float other) {
        return new Vector3(x - other, y - other, z - other);
    }

    public Vector3 mul(double scale) {
        return new Vector3(x * scale, y * scale, z * scale);
    }

    public Vector3 mul(Vector3 other) {
        return new Vector3(x * other.x, y * other.y, z * other.z);
    }

    public Vector3 mul(float xS, float yS, float zS) {
        return new Vector3(x * xS, y * yS, z * zS);
    }


    public Vector3 dot(Matrix3x3 o) {
        float[] vA = new float[3];
        for (int i = 0; i < 3; i++) {
            vA[i] = 0;
            for (int j = 0; j < 3; j++) {
                vA[i] += this.get(j) * o.get(j, i);
            }
        }
        return new Vector3(vA[0], vA[1], vA[2]);
    }

    public float get(int index) {
        if (index == 0)
            return x;
        if (index == 1)
            return y;
        if (index == 2)
            return z;
        return 0;
    }

    /**
     * If magnitude is negative, we will return a vector facing the opposite* direction.
	 */
	public Vector3 scaledToMagnitude(double magnitude) {
		if (isZero()) {
			throw new IllegalStateException("Cannot scale up a vector with length zero!");
		}
		double scaleRequired = magnitude / magnitude();
		return scaled(scaleRequired);
	}

	public Vector3 div(double scale) {
        return new Vector3(x / scale, y / scale, z / scale);
    }

    public double distance(Vector3 other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        double zDiff = z - other.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }

	public double magnitude() {
		return Math.sqrt(magnitudeSquared());
	}

	public double magnitudeSquared() {
		return x * x + y * y + z * z;
	}

	public Vector3 normalized() {

		if (isZero()) {
			throw new IllegalStateException("Cannot normalize a vector with length zero!");
		}
		return this.scaled(1 / magnitude());
	}

	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}

	public Vector2 flatten() {
		return new Vector2(x, y);
	}

	public double angle(Vector3 v) {
		double mag2 = magnitudeSquared();
		double vmag2 = v.magnitudeSquared();
		double dot = dot(v);
		return Math.acos(dot / Math.sqrt(mag2 * vmag2));
	}

	public Vector3 cross(Vector3 v) {
		double tx = y * v.z - z * v.y;
		double ty = z * v.x - x * v.z;
		double tz = x * v.y - y * v.x;
		return new Vector3(tx, ty, tz);
	}

	@Override
	public String toString() {
		return String.format("(%s, %s, %s)", x, y, z);
	}
}
