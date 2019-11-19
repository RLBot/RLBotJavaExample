package rlbotexample;

import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.BallPrediction;
import rlbot.flat.GameTickPacket;
import rlbot.flat.QuickChatSelection;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.boost.BoostManager;
import rlbotexample.input.DataPacket;
import rlbotexample.input.car.CarData;
import rlbotexample.output.ControlsOutput;
import rlbotexample.prediction.BallPredictionHelper;
import rlbotexample.vector.Vector2;

import java.awt.*;

public class SampleBot implements Bot {

    private final int playerIndex;

    public SampleBot(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    /**
     * This is where we keep the actual bot logic. This function shows how to chase the ball.
     * Modify it to make your bot smarter!
     */
    private ControlsOutput processInput(DataPacket input) {

        Vector2 ballPosition = input.ball.position.flatten();
        CarData myCar = input.car;
        Vector2 carPosition = myCar.position.flatten();
        Vector2 carDirection = myCar.orientation.noseVector.flatten();

        // Subtract the two positions to get a vector pointing from the car to the ball.
        Vector2 carToBall = ballPosition.minus(carPosition);

        // How far does the car need to rotate before it's pointing exactly at the ball?
        double steerCorrectionRadians = carDirection.correctionAngle(carToBall);

        boolean goLeft = steerCorrectionRadians > 0;

        // This is optional!
        drawDebugLines(input, myCar, goLeft);

        // This is also optional!
        if (input.ball.position.z > 300) {
            RLBotDll.sendQuickChat(playerIndex, false, QuickChatSelection.Compliments_NiceOne);
        }

        return new ControlsOutput()
                .withSteer(goLeft ? -1 : 1)
                .withThrottle(1);
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

        if(input.ball.hasBeenTouched) {
            float lastTouchTime = myCar.elapsedSeconds - input.ball.latestTouch.gameSeconds;
            Color touchColor = input.ball.latestTouch.team == 0 ? Color.BLUE : Color.ORANGE;
            renderer.drawString3d((int)lastTouchTime + "s", touchColor, input.ball.position, 2, 2);
        }

        try {
            // Draw 3 seconds of ball prediction
            BallPrediction ballPrediction = RLBotDll.getBallPrediction();
            BallPredictionHelper.drawTillMoment(ballPrediction, myCar.elapsedSeconds + 3, Color.CYAN, renderer);
        } catch (RLBotInterfaceException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getIndex() {
        return this.playerIndex;
    }

    /**
     * This is the most important function. It will automatically get called by the framework with fresh data
     * every frame. Respond with appropriate controls!
     */
    @Override
    public ControllerState processInput(GameTickPacket packet) {

        if (packet.playersLength() <= playerIndex || packet.ball() == null || !packet.gameInfo().isRoundActive()) {
            // Just return immediately if something looks wrong with the data. This helps us avoid stack traces.
            return new ControlsOutput();
        }

        // Update the boost manager and tile manager with the latest data
        BoostManager.loadGameTickPacket(packet);

        // Translate the raw packet data (which is in an unpleasant format) into our custom DataPacket class.
        // The DataPacket might not include everything from GameTickPacket, so improve it if you need to!
        DataPacket dataPacket = new DataPacket(packet, playerIndex);

        // Do the actual logic using our dataPacket.
        ControlsOutput controlsOutput = processInput(dataPacket);

        return controlsOutput;
    }

    @Override
    public void retire() {
        System.out.println("Retiring sample bot " + playerIndex);
    }
}
