package rlbotexample.prediction;

import java.awt.Color;

import rlbot.render.Renderer;
import rlbotexample.prediction.slice.PhysicsSlice;
import rlbotexample.prediction.slice.PositionSlice;
import rlbotexample.vector.Vector3;

/**
 * This class can help you get started with ball prediction. Feel free to change
 * it as much as you want, this is part of your bot, not part of the framework!
 */
public class BallPredictionHelper {

	public static void drawTillMoment(float gameSeconds, Color color, Renderer renderer) {
		Vector3 previousPosition = null;
		for (int i = 0; i < BallPrediction.MAX_SLICES; i += 4) {
			PhysicsSlice slice = BallPrediction.slices[i];
			if (slice.getElapsedSeconds() > gameSeconds) {
				break;
			}
			Vector3 position = slice.getPosition();
			if (previousPosition != null) {
				renderer.drawLine3d(color, previousPosition, position);
			}
			previousPosition = position;
		}
	}

	public static PositionSlice findFutureGoal() {
		for (PhysicsSlice slice : BallPrediction.slices) {
			if (Math.abs(slice.getPosition().y) > 5235) {
				return new PositionSlice(slice.getPosition(), slice.getElapsedSeconds());
			}
		}
		return null;
	}

}
