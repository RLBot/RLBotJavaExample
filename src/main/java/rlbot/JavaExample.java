package rlbot;

import rlbot.manager.FlatBotManager;
import rlbot.py.PythonInterface;
import rlbot.py.PythonServer;
import rlbot.util.PortReader;

/**
 * See JavaAgent.py for usage instructions
 */
public class JavaExample {

    public static void main(String[] args) {

        FlatBotManager botManager = new FlatBotManager();
        PythonInterface pythonInterface = new SamplePythonInterface(botManager);
        Integer port = PortReader.readPortFromFile("port.cfg");
        PythonServer pythonServer = new PythonServer(pythonInterface, port);
        pythonServer.start();
    }
}