package rlbot;

import rlbot.manager.FlatBotManager;
import rlbot.py.PythonInterface;

/**
 * The public methods of this class will be called directly from the python component of the RLBot framework.
 */
public class SamplePythonInterface implements PythonInterface {

    private final FlatBotManager botManager;

    public SamplePythonInterface(FlatBotManager botManager) {
        this.botManager = botManager;
    }

    protected FlatBot initBot(int index, String botType) {
        return new SampleBot(index);
    }

    public void ensureStarted() {
        botManager.ensureStarted();
    }

    public void shutdown() {
        botManager.shutDown();
    }

    public void ensureBotRegistered(final int index, final String botType, final int team) {
        botManager.ensureBotRegistered(index, () -> initBot(index, botType));
    }

    public void retireBot(final int index) {
        botManager.retireBot(index);
    }
}
