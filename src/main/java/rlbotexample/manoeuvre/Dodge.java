package rlbotexample.manoeuvre;

import rlbotexample.manoeuvre.sequence.ControlStep;
import rlbotexample.manoeuvre.sequence.Sequence;
import rlbotexample.output.Controls;

public class Dodge extends Sequence {

	public Dodge(float angle) {
		super(new ControlStep(0.05, new Controls().withJump()), new ControlStep(0.05, new Controls()),
				new ControlStep(0.2,
						new Controls().withJump().withPitch((float) -Math.cos(angle)).withYaw((float) Math.sin(angle))),
				new ControlStep(0.8, new Controls()));
	}

}
