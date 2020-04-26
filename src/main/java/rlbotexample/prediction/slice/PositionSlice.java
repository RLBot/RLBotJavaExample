package rlbotexample.prediction.slice;

import rlbotexample.vector.Vector3;

public class PositionSlice {

	private Vector3 position;
	private float elapsedSeconds;

	public PositionSlice(Vector3 position, float elapsedSeconds) {
		super();
		this.position = position;
		this.elapsedSeconds = elapsedSeconds;
	}

	public PositionSlice() {
		this(new Vector3(), 0);
	}

	public Vector3 getPosition() {
		return position;
	}

	public float getElapsedSeconds() {
		return elapsedSeconds;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public void setElapsedSeconds(float elapsedSeconds) {
		this.elapsedSeconds = elapsedSeconds;
	}

}
