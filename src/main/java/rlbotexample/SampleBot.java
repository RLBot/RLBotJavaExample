package rlbotexample;

import java.awt.Color;
import java.awt.Point;

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
import rlbotexample.manoeuvre.Dodge;
import rlbotexample.manoeuvre.Manoeuvre;
import rlbotexample.manoeuvre.Recovery;
import rlbotexample.manoeuvre.sequence.ControlStep;
import rlbotexample.manoeuvre.sequence.Sequence;
import rlbotexample.output.Controls;
import rlbotexample.prediction.BallPrediction;
import rlbotexample.prediction.BallPredictionHelper;
import rlbotexample.prediction.slice.PositionSlice;
import rlbotexample.util.MathUtils;
import rlbotexample.vector.Vector2;
import rlbotexample.vector.Vector3;

public class SampleBot implements Bot {

	private final int playerIndex;

	private Manoeuvre manoeuvre;

	public SampleBot(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	/**
	 * This is where we keep the actual bot logic. This function shows how to chase
	 * the ball. Modify it to make your bot smarter!
	 */
	private Controls processInput(DataPacket packet) {
		if (this.manoeuvre != null) {
			if (this.manoeuvre.isFinished()) {
				this.manoeuvre = null;
			} else {
				return this.manoeuvre.tick(packet);
			}
		}

		CarData car = packet.car;
		Vector3 ballPosition = packet.ball.position;
		Vector3 localBall = MathUtils.toLocal(car, ballPosition);

		// How far does the car need to rotate before it's pointing exactly at the ball?
		float angle = (float) Vector2.X.correctionAngle(localBall.flatten());

		if (car.hasWheelContact) {
			double carSpeed = car.velocity.magnitude();
			if (!car.isSupersonic && carSpeed > 1200 && car.boost < 10) {
				this.manoeuvre = new Dodge(angle * 2);
				return this.manoeuvre.tick(packet);
			}
		} else {
			this.manoeuvre = new Recovery(packet.car);
			return this.manoeuvre.tick(packet);
		}

		// This is optional!
		drawDebugLines(packet, car);

		// This is also optional!
		if (packet.ball.position.z > 300) {
			RLBotDll.sendQuickChat(playerIndex, false, QuickChatSelection.Compliments_NiceOne);
		}

		return new Controls().withSteer(MathUtils.clamp11(angle * 5)).withThrottle(1)
				.withBoost(!car.isSupersonic && Math.abs(angle) < Math.toRadians(25));
	}

	/**
	 * This is a nice example of using the rendering feature.
	 */
	private void drawDebugLines(DataPacket packet, CarData car) {
		// Here's an example of rendering debug data on the screen.
		Renderer renderer = BotLoopRenderer.forBotLoop(this);

		// Draw a line from the car to the ball
		renderer.drawLine3d(Color.LIGHT_GRAY, car.position, packet.ball.position);

		// Draw a line that points out from the nose of the car.
		renderer.drawLine3d(car.team == 0 ? Color.BLUE : Color.ORANGE,
				car.position.plus(car.orientation.forward.scaled(150)),
				car.position.plus(car.orientation.forward.scaled(300)));

		if (packet.ball.hasBeenTouched) {
			float lastTouchTime = car.elapsedSeconds - packet.ball.latestTouch.gameSeconds;
			Color touchColor = packet.ball.latestTouch.team == 0 ? Color.BLUE : Color.ORANGE;
			renderer.drawString3d((int) lastTouchTime + "s", touchColor, packet.ball.position, 2, 2);
		}

		// Draw 3 seconds of ball prediction
		BallPredictionHelper.drawTillMoment(car.elapsedSeconds + 3, Color.CYAN, renderer);

		PositionSlice futureGoal = BallPredictionHelper.findFutureGoal();
		if (futureGoal == null) {
			renderer.drawString2d("No future goal", Color.WHITE, new Point(20, 20), 2, 2);
		} else {
			renderer.drawString2d("Goal in "
					+ MathUtils.round(futureGoal.getElapsedSeconds() - packet.elapsedSeconds, 2) + " seconds!",
					Color.RED, new Point(20, 20), 2, 2);
		}
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

		if (gameTickPacket.playersLength() <= playerIndex || gameTickPacket.ball() == null) {
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
