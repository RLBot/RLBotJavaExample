package rlbot;

import com.google.protobuf.InvalidProtocolBufferException;
import rlbot.api.GameData;
import rlbot.interop.RLBotDll;

import java.util.HashMap;
import java.util.Map;

public class BotManager {

    private static final Map<Integer, Bot> initializedBots = new HashMap<>();

    private boolean keepRunning;
    private Thread looper;

    public static GameData.GameTickPacket latestPacket;

    public void registerBot(final int index, final String botType) {
        if (initializedBots.containsKey(index)) {
            return;
        }

        initializedBots.computeIfAbsent(index, (idx) -> {
            Bot bot = this.createBot(idx, botType);
            bot.start();
            return bot;
        });
    }

    private void retireBots() {
        initializedBots.values().forEach(Bot::retire);
        initializedBots.clear();
    }

    private Bot createBot(int playerIndex, String botType) {
        return new Bot(playerIndex);
    }

    public void start() {
        if (keepRunning) {
            return; // Already started
        }

        keepRunning = true;
        looper = new Thread(this::doLoop);
        looper.start();
    }

    private void doLoop() {
        while (keepRunning) {

            try {
                latestPacket = RLBotDll.getProtoPacket();

            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void retire() {
        keepRunning = false;
        retireBots();
    }
}
