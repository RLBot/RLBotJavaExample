package rlbot;

import rlbot.api.GameData;
import rlbot.input.CarData;
import rlbot.input.DataPacket;
import rlbot.output.ControlsOutput;
import rlbot.vector.Vector2;

public class SampleBot implements Bot {

    private final int playerIndex;

    public SampleBot(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    private ControlsOutput processInput(DataPacket input) {

        Vector2 ballPosition = input.ball.position.flatten();
        CarData myCar = input.car;
        Vector2 carPosition = myCar.position.flatten();
        Vector2 carDirection = myCar.orientation.noseVector.flatten();
        Vector2 carToBall = ballPosition.minus(carPosition);

        double steerCorrectionRadians = carDirection.correctionAngle(carToBall);
        float steer;
        if (steerCorrectionRadians > 0) {
            steer = -1;
        } else {
            steer = 1;
        }

        return new ControlsOutput()
                .withSteer(steer)
                .withThrottle(1);
    }


    @Override
    public int getIndex() {
        return this.playerIndex;
    }

    @Override
    public GameData.ControllerState processInput(GameData.GameTickPacket gameTickPacket) {
        DataPacket dataPacket = new DataPacket(gameTickPacket, playerIndex);
        return processInput(dataPacket).toControllerState();
    }

    public void retire() {
    }
}
