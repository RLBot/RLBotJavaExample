package rlbotexample.util;

public class MathUtils {

	public static double round(double value, int places) {
		double ten = Math.pow(10, places);
		return Math.round(value * 10) / ten;
	}

}
