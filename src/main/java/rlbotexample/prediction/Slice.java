package rlbotexample.prediction;

import rlbotexample.vector.Vector3;

public class Slice {

	private Vector3 position, velocity;
	private float elapsedSeconds;

	public Slice(Vector3 position, Vector3 velocity, float elapsedSeconds) {
		super();
		this.position = position;
		this.velocity = velocity;
		this.elapsedSeconds = elapsedSeconds;
	}

	public Slice() {
		this(new Vector3(), new Vector3(), 0);
	}

	public Vector3 getPosition() {
		return position;
	}

	public Vector3 getVelocity() {
		return velocity;
	}

	public float getElapsedSeconds() {
		return elapsedSeconds;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}

	public void setElapsedSeconds(float elapsedSeconds) {
		this.elapsedSeconds = elapsedSeconds;
	}

}
