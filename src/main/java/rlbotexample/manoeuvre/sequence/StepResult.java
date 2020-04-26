package rlbotexample.manoeuvre.sequence;

import rlbotexample.output.Controls;

public class StepResult {

	public Controls controls;
	public boolean done;

	public StepResult(Controls controls, boolean done) {
		super();
		this.controls = controls;
		this.done = done;
	}

}
