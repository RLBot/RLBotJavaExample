package rlbotexample.prediction;

import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.Physics;
import rlbotexample.prediction.slice.PhysicsSlice;
import rlbotexample.vector.Vector3;

public class BallPrediction {

	public static final int MAX_SLICES = 360;

	public static PhysicsSlice[] slices = new PhysicsSlice[MAX_SLICES];

	public static void update() {
		try {
			rlbot.flat.BallPrediction ballPrediction = RLBotDll.getBallPrediction();

			for (int i = 0; i < MAX_SLICES; i++) {
				if (slices[i] == null) {
					slices[i] = new PhysicsSlice();
				}
				Physics physics = ballPrediction.slices(i).physics();
				float elapsedSeconds = ballPrediction.slices(i).gameSeconds();
				slices[i].setPosition(new Vector3(physics.location()));
				slices[i].setVelocity(new Vector3(physics.velocity()));
				slices[i].setElapsedSeconds(elapsedSeconds);
			}
		} catch (RLBotInterfaceException e) {
			e.printStackTrace();
		}
	}

}
