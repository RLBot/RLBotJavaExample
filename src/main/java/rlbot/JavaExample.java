package rlbot;

import rlbot.manager.BotManager;
import rlbot.pyinterop.PythonInterface;
import rlbot.pyinterop.PythonServer;
import rlbot.util.PortReader;

/**
 * See JavaAgent.py for usage instructions
 */
public class JavaExample {

    public static void main(String[] args) {

        BotManager botManager = new BotManager();
        PythonInterface pythonInterface = new SamplePythonInterface(botManager);
        Integer port = PortReader.readPortFromFile("port.cfg");
        PythonServer pythonServer = new PythonServer(pythonInterface, port);
        pythonServer.start();
    }
}