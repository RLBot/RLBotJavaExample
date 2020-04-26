package rlbotexample.util;

import rlbotexample.input.car.CarData;
import rlbotexample.input.car.CarOrientation;
import rlbotexample.vector.Vector3;

public class MathUtils {

	public static float round(float value, int places) {
		float ten = (float) Math.pow(10, places);
		return Math.round(value * 10) / ten;
	}

	public static float clamp11(float value) {
		return clamp(value, -1, 1);
	}

	private static float clamp(float value, int bound1, int bound2) {
		return Math.max(Math.min(bound1, bound2), Math.min(value, Math.max(bound1, bound2)));
	}
	
	public static Vector3 toLocal(CarData car, Vector3 vector) {
		return toLocal(car.orientation, vector.minus(car.position));
	}

	public static Vector3 toLocal(CarOrientation orientation, Vector3 relative) {
		return new Vector3(relative.dot(orientation.forward), relative.dot(orientation.right), relative.dot(orientation.up));
	}

}
