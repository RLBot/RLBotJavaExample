package rlbot;

import rlbot.manager.BotManager;
import rlbot.py.DefaultPythonInterface;

/**
 * The public methods of this class will be called directly from the python component of the RLBot framework.
 */
public class SamplePythonInterface extends DefaultPythonInterface {

    public SamplePythonInterface(BotManager botManager) {
        super(botManager);
    }

    @Override
    protected Bot initBot(int index, String botType) {
        return new SampleBot(index);
    }
}
