package rlbotexample;

import java.awt.Color;

import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.cppinterop.RLBotDll;
import rlbot.flat.GameTickPacket;
import rlbot.flat.QuickChatSelection;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.boost.BoostManager;
import rlbotexample.input.DataPacket;
import rlbotexample.input.car.CarData;
import rlbotexample.output.Controls;
import rlbotexample.prediction.BallPrediction;
import rlbotexample.prediction.BallPredictionHelper;
import rlbotexample.sequence.ControlStep;
import rlbotexample.sequence.Sequence;
import rlbotexample.vector.Vector2;

public class SampleBot implements Bot {

	private final int playerIndex;

	private Sequence activeSequence;

	public SampleBot(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	/**
	 * This is where we keep the actual bot logic. This function shows how to chase
	 * the ball. Modify it to make your bot smarter!
	 */
	private Controls processInput(DataPacket packet) {
		if (this.activeSequence != null) {
			if (this.activeSequence.done) {
				this.activeSequence = null;
			} else {
				return this.activeSequence.tick(packet);
			}
		}

		Vector2 ballPosition = packet.ball.position.flatten();
		CarData myCar = packet.car;
		Vector2 carPosition = myCar.position.flatten();
		Vector2 carDirection = myCar.orientation.noseVector.flatten();

		double carSpeed = myCar.velocity.magnitude();
		if (550 < carSpeed && carSpeed < 600) {
			this.activeSequence = new Sequence(new ControlStep(0.05, new Controls().withJump()),
					new ControlStep(0.05, new Controls()),
					new ControlStep(0.2, new Controls().withJump().withPitch(-1)),
					new ControlStep(0.8, new Controls()));
			return this.activeSequence.tick(packet);
		}

		// Subtract the two positions to get a vector pointing from the car to the ball.
		Vector2 carToBall = ballPosition.minus(carPosition);

		// How far does the car need to rotate before it's pointing exactly at the ball?
		double steerCorrectionRadians = carDirection.correctionAngle(carToBall);

		boolean goLeft = steerCorrectionRadians > 0;

		// This is optional!
		drawDebugLines(packet, myCar, goLeft);

		// This is also optional!
		if (packet.ball.position.z > 300) {
			RLBotDll.sendQuickChat(playerIndex, false, QuickChatSelection.Compliments_NiceOne);
		}

		return new Controls().withSteer(goLeft ? -1 : 1).withThrottle(1);
	}

	/**
	 * This is a nice example of using the rendering feature.
	 */
	private void drawDebugLines(DataPacket input, CarData myCar, boolean goLeft) {
		// Here's an example of rendering debug data on the screen.
		Renderer renderer = BotLoopRenderer.forBotLoop(this);

		// Draw a line from the car to the ball
		renderer.drawLine3d(Color.LIGHT_GRAY, myCar.position, input.ball.position);

		// Draw a line that points out from the nose of the car.
		renderer.drawLine3d(goLeft ? Color.BLUE : Color.RED,
				myCar.position.plus(myCar.orientation.noseVector.scaled(150)),
				myCar.position.plus(myCar.orientation.noseVector.scaled(300)));

		renderer.drawString3d(goLeft ? "left" : "right", Color.WHITE, myCar.position, 2, 2);

		if (input.ball.hasBeenTouched) {
			float lastTouchTime = myCar.elapsedSeconds - input.ball.latestTouch.gameSeconds;
			Color touchColor = input.ball.latestTouch.team == 0 ? Color.BLUE : Color.ORANGE;
			renderer.drawString3d((int) lastTouchTime + "s", touchColor, input.ball.position, 2, 2);
		}

		// Draw 3 seconds of ball prediction
		BallPredictionHelper.drawTillMoment(myCar.elapsedSeconds + 3, Color.CYAN, renderer);
	}

	@Override
	public int getIndex() {
		return this.playerIndex;
	}

	/**
	 * This is the most important function. It will automatically get called by the
	 * framework with fresh data every frame. Respond with appropriate controls!
	 */
	@Override
	public ControllerState processInput(GameTickPacket gameTickPacket) {

		if (gameTickPacket.playersLength() <= playerIndex || gameTickPacket.ball() == null
				|| !gameTickPacket.gameInfo().isRoundActive()) {
			// Just return immediately if something looks wrong with the data. This helps us
			// avoid stack traces.
			return new Controls();
		}

		// Update the boost manager and tile manager with the latest data
		BoostManager.loadGameTickPacket(gameTickPacket);

		BallPrediction.update();

		// Translate the raw packet data (which is in an unpleasant format) into our
		// custom DataPacket class.
		// The DataPacket might not include everything from GameTickPacket, so improve
		// it if you need to!
		DataPacket packet = new DataPacket(gameTickPacket, playerIndex);

		// Do the actual logic using our dataPacket.
		Controls controlsOutput = processInput(packet);

		return controlsOutput;
	}

	@Override
	public void retire() {
		System.out.println("Retiring sample bot " + playerIndex);
	}
}
