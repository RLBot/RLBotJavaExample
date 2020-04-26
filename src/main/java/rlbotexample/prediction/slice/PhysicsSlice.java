package rlbotexample.prediction.slice;

import rlbotexample.vector.Vector3;

public class PhysicsSlice extends PositionSlice {

	private Vector3 velocity;

	public PhysicsSlice(Vector3 position, Vector3 velocity, float elapsedSeconds) {
		super(position, elapsedSeconds);
		this.velocity = velocity;
	}

	public PhysicsSlice() {
		this(new Vector3(), new Vector3(), 0);
	}

	public Vector3 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}

}
