package rlbotexample.manoeuvre.sequence;

import rlbotexample.input.DataPacket;
import rlbotexample.output.Controls;

public class ControlStep extends Step {

	private float duration, startTime = -1;
	private Controls controls;

	public ControlStep(float duration, Controls controls) {
		super();
		this.duration = duration;
		this.controls = controls;
	}
	
	public ControlStep(double duration, Controls controls) {
		this((float)duration, controls);
	}

	@Override
	public StepResult tick(DataPacket packet) {
		if(this.startTime == -1) {
			this.startTime = packet.elapsedSeconds;
		}
		float elapsedTime = packet.elapsedSeconds - this.startTime;
		return new StepResult(this.controls, elapsedTime > this.duration);
	}

}
