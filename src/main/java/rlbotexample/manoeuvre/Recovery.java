package rlbotexample.manoeuvre;

import rlbotexample.input.DataPacket;
import rlbotexample.input.car.CarData;
import rlbotexample.output.Controls;
import rlbotexample.vector.Vector3;

public class Recovery extends AerialTurn {

	public Recovery(CarData car) {
		super(new Vector3(car.velocity.x, car.velocity.y, 0));
	}
	
	public Controls tick(DataPacket packet) {
		Controls controls = super.tick(packet);
		this.setFinished(packet.car.hasWheelContact);
		return controls;
	}

}
