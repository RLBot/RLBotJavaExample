package rlbotexample.manoeuvre.sequence;

import rlbotexample.input.DataPacket;
import rlbotexample.manoeuvre.Manoeuvre;
import rlbotexample.output.Controls;

public class Sequence extends Manoeuvre {

	private Step[] steps;
	private int index;
	
	public Sequence(Step... steps) {
		super();
		this.steps = steps;
	}

	public Controls tick(DataPacket packet) {
		while(true) {
			if(this.index >= this.steps.length) {
				this.setFinished(true);
				return new Controls();
			}
			Step step = this.steps[this.index];
			StepResult result = step.tick(packet);
			if(result.done) {
				this.index ++;
				if(this.index >= this.steps.length) {
					this.setFinished(true);
				}
			}
			return result.controls;
		}
	}

}
