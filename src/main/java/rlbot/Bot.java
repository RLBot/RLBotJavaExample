package rlbot;

import rlbot.api.GameData;
import rlbot.input.CarData;
import rlbot.input.DataPacket;
import rlbot.interop.RLBotDll;
import rlbot.vector.Vector2;

import java.time.Duration;
import java.time.LocalDateTime;

public class Bot {

    private final int playerIndex;
    private boolean keepRunning;
    private Thread looper;

    public Bot(int playerIndex) {
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

    private GameData.ControllerState reactToFrame(GameData.GameTickPacket request) {
        DataPacket dataPacket = new DataPacket(request, playerIndex);
        return processInput(dataPacket).toControllerState();
    }

    public void start() {
        keepRunning = true;
        looper = new Thread(this::doLoop);
        looper.start();
    }

    private void doLoop() {
        while (keepRunning) {

            LocalDateTime before = LocalDateTime.now();
            try {
                GameData.GameTickPacket packet = BotManager.latestPacket;
                if (packet != null) {
                    GameData.ControllerState controllerState = reactToFrame(packet);
                    RLBotDll.setControllerState(controllerState, playerIndex);
                }
            } catch (final Throwable e) {
                e.printStackTrace();
            }

            try {
                long executionMillis = Duration.between(before, LocalDateTime.now()).toMillis();
                long sleepTime = Math.max(0, (1000 / 60) - executionMillis);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void retire() {
        keepRunning = false;
    }
}
