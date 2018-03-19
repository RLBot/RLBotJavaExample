package rlbot;

public class PythonEntryPoint {

    private static final BotManager botManager = new BotManager();

    public void startup() {
        botManager.start();
    }

    public void shutdown() {
        botManager.retire();
    }

    public void registerBot(int index, String botType) {
        botManager.registerBot(index, botType);
    }
}
