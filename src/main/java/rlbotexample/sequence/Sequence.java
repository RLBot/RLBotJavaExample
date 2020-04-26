package rlbotexample.sequence;

import rlbotexample.input.DataPacket;
import rlbotexample.output.Controls;

public class Sequence {

	private Step[] steps;
	private int index;
	public boolean done = false;
	
	public Sequence(Step... steps) {
		super();
		this.steps = steps;
	}

	public Controls tick(DataPacket packet) {
		while(true) {
			if(this.index >= this.steps.length) {
				this.done = true;
				return new Controls();
			}
			Step step = this.steps[this.index];
			StepResult result = step.tick(packet);
			if(result.done) {
				this.index ++;
				if(this.index >= this.steps.length) {
					this.done = true;
				}
			}
			return result.controls;
		}
	}

}
