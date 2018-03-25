package rlbot;

import rlbot.manager.BotManager;
import rlbot.py.PythonInterface;

/**
 * The public methods of this class will be called directly from the python component of the RLBot framework.
 */
public class SamplePythonInterface implements PythonInterface {

    private final BotManager botManager;

    public SamplePythonInterface(BotManager botManager) {
        this.botManager = botManager;
    }

    public void startup() {
        botManager.start();
    }

    public void shutdown() {
        botManager.retire();
    }

    public void registerBot(final int index, final String botType) {
        botManager.registerBot(index, new SampleBot(index));
    }
}
