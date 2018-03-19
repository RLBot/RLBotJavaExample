package rlbot;

import com.google.protobuf.InvalidProtocolBufferException;
import py4j.GatewayServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * See JavaAgent.py for usage instructions
 */
public class JavaExample {

    public static void main(String[] args) throws InvalidProtocolBufferException {

        // Scenario: you finished your bot and submitted it to a tournament. Your opponent hard-coded the same
        // as you, and the match can't start because of the conflict. Because of this line, you can ask the
        // organizer make a file called "port.txt" in the same directory as your .jar, and put some other number in it.
        // This matches code in JavaAgent.py
        int port = readPortFromFile();

        GatewayServer gatewayServer = new GatewayServer(new PythonEntryPoint(), port);
        gatewayServer.start();
        System.out.println(String.format("Gateway server started on port %s. Listening for Rocket League data!", port));
    }

    private static Integer readPortFromFile() {
        Path path = Paths.get("port.cfg");

        try (Stream<String> lines = Files.lines(path)) {
            Optional<String> firstLine = lines.findFirst();
            return firstLine.map(Integer::parseInt).orElseThrow(() -> new RuntimeException("Port config file was empty!"));
        } catch (final IOException e) {
            throw new RuntimeException("Failed to read port file! Tried to find it at " + path.toAbsolutePath().toString());
        }
    }

}