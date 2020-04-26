package rlbotexample.manoeuvre;

import rlbotexample.manoeuvre.sequence.ControlStep;
import rlbotexample.manoeuvre.sequence.Sequence;
import rlbotexample.output.Controls;

public class Jump extends Sequence {

	public Jump(float holdTime, float idleTime) {
		super(new ControlStep(holdTime, new Controls().withJump()), new ControlStep(idleTime, new Controls()));
	}

}
