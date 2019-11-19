package rlbotexample;

import rlbot.manager.BotManager;
import rlbotexample.util.PortReader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * See JavaAgent.py for usage instructions.
 *
 * Look inside SampleBot.java for the actual bot logic!
 */
public class JavaExample {

    private static final int DEFAULT_PORT = 17357;

    public static void main(String[] args) {

        BotManager botManager = new BotManager();
        int port = PortReader.readPortFromArgs(args).orElseGet(() -> {
            System.out.println("Could not read port from args, using default!");
            return DEFAULT_PORT;
        });

        SamplePythonInterface pythonInterface = new SamplePythonInterface(port, botManager);
        new Thread(pythonInterface::start).start();

        displayWindow(botManager, port);
    }
    
    private static void displayWindow(BotManager botManager, int port) {
        JFrame frame = new JFrame("Java Bot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        BorderLayout borderLayout = new BorderLayout();
        panel.setLayout(borderLayout);
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        dataPanel.add(new JLabel("Listening on port " + port), BorderLayout.CENTER);
        dataPanel.add(new JLabel("I'm the thing controlling the Java bot, keep me open :)"), BorderLayout.CENTER);
        JLabel botsRunning = new JLabel("Bots running: ");
        dataPanel.add(botsRunning, BorderLayout.CENTER);
        panel.add(dataPanel, BorderLayout.CENTER);
        frame.add(panel);

        URL url = JavaExample.class.getClassLoader().getResource("icon.png");
        Image image = Toolkit.getDefaultToolkit().createImage(url);
        panel.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        frame.setIconImage(image);

        frame.pack();
        frame.setVisible(true);

        ActionListener myListener = e -> {
            Set<Integer> runningBotIndices = botManager.getRunningBotIndices();

            String botsStr;
            if (runningBotIndices.isEmpty()) {
                botsStr = "None";
            } else {
                botsStr = runningBotIndices.stream()
                        .sorted()
                        .map(i -> "#" + i)
                        .collect(Collectors.joining(", "));
            }
            botsRunning.setText("Bots indices running: " + botsStr);
        };

        new Timer(1000, myListener).start();
    }
}
