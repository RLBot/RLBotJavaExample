package rlbotexample.manoeuvre;

import rlbotexample.input.DataPacket;
import rlbotexample.input.car.CarData;
import rlbotexample.output.Controls;
import rlbotexample.vector.Vector3;

public class AerialTurn extends Manoeuvre {

	private Vector3 targetForward, targetUp;
	private Controls controls;

	public AerialTurn(Vector3 targetForward, Vector3 targetUp) {
		this.targetForward = targetForward.normalized();
		this.targetUp = targetUp.normalized();
		this.controls = new Controls();
	}

	public AerialTurn(Vector3 targetForward) {
		this(targetForward, Vector3.Z);
	}

	/**
	 * https://github.com/DomNomNom/RocketBot/blob/32e69df4f2841501c5f1da97ce34673dccb670af/NomBot_v1.5/NomBot_v1_5.py#L56-L103
	 */
	public Controls tick(DataPacket packet) {
		CarData car = packet.car;
		double pitchVel = car.angularVelocity.pitch;
		double yawVel = -car.angularVelocity.yaw;
		double rollVel = car.angularVelocity.roll;

		Vector3 desiredFacingAngVel = car.orientation.forward.cross(this.targetForward);
		Vector3 desiredUpVel = car.orientation.up.cross(this.targetUp);

		double pitch = desiredFacingAngVel.dot(car.orientation.right);
		double yaw = -desiredFacingAngVel.dot(car.orientation.up);
		double roll = desiredUpVel.dot(car.orientation.forward);

		// Avoid getting stuck in directly-opposite states
		if (car.orientation.up.dot(this.targetUp) < -0.8 && car.orientation.forward.dot(this.targetForward) > 0.8) {
			if (roll == 0)
				roll = 1;
			roll *= 1e10;
		}
		if (car.orientation.forward.dot(this.targetForward) < -0.8) {
			if (pitch == 0)
				pitch = 1;
			pitch *= 1e10;
		}

		// PID control to stop overshooting.
		roll = 3 * roll + 0.30 * rollVel;
		yaw = 3 * yaw + 0.70 * yawVel;
		pitch = 3 * pitch + 0.90 * pitchVel;

		// Only start adjusting roll once we're roughly facing the right way.
		if (car.orientation.forward.dot(this.targetForward) < 0) {
			roll = 0;
		}

		this.setFinished(
				car.orientation.forward.dot(this.targetForward) > 0.9 && car.orientation.up.dot(this.targetUp) > 0.7);

		return this.controls.withPitch(pitch).withRoll(roll).withYaw(yaw);
	}

}
