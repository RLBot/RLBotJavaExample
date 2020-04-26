package rlbotexample.input;

import rlbotexample.input.car.CarOrientation;
import rlbotexample.vector.Vector3;

public class Rotator {

	public final float pitch, roll, yaw;

	public Rotator(float pitch, float roll, float yaw) {
		super();
		this.pitch = pitch;
		this.roll = roll;
		this.yaw = yaw;
	}

	public Rotator(CarOrientation orientation, Vector3 vector) {
		this.pitch = (float) vector.dot(orientation.right);
		this.roll = (float) vector.dot(orientation.forward);
		this.yaw = (float) vector.dot(orientation.up);
	}

}
