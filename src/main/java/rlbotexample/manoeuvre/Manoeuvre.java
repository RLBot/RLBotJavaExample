package rlbotexample.manoeuvre;

import rlbotexample.input.DataPacket;
import rlbotexample.output.Controls;

public abstract class Manoeuvre {

	private boolean finished;

	public abstract Controls tick(DataPacket packet);

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		// A manoeuvre cannot be un-finished after it has already finished.
		this.finished |= finished;
	}

}
