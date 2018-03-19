package rlbot;

import rlbot.api.GameData;
import rlbot.interop.RLBotDll;

public class Bot {

    private final int playerIndex;
    private boolean keepRunning;
    private Thread looper;

    public Bot(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    private GameData.ControllerState reactToFrame(GameData.GameTickPacket request) {
        return GameData.ControllerState.newBuilder().build();
    }

    public void start() {
        keepRunning = true;
        looper = new Thread(this::doLoop);
        looper.start();
    }

    private void doLoop() {
        while (keepRunning) {

            GameData.GameTickPacket packet = BotManager.latestPacket;
            RLBotDll.setControllerState(reactToFrame(packet), playerIndex);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void retire() {
        keepRunning = false;
    }
}
