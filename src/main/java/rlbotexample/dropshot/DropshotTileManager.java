package rlbotexample.dropshot;

import rlbot.cppinterop.RLBotDll;
import rlbot.flat.FieldInfo;
import rlbot.flat.GameTickPacket;
import rlbotexample.vector.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Information about where dropshot tiles are located in the arena and what state they have.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class DropshotTileManager {

    private static final ArrayList<DropshotTile> tiles = new ArrayList<>();

    public static List<DropshotTile> getTiles() {
        return tiles;
    }

    private static void loadFieldInfo(FieldInfo fieldInfo) {

        synchronized (tiles) {

            tiles.clear();

            for (int i = 0; i < fieldInfo.goalsLength(); i++) {
                rlbot.flat.GoalInfo goalInfo = fieldInfo.goals(i);
                tiles.add(new DropshotTile(new Vector3(goalInfo.location())));
            }
        }
    }

    public static void loadGameTickPacket(GameTickPacket packet) {

        if (packet.tileInformationLength() > tiles.size()) {
            try {
                loadFieldInfo(RLBotDll.getFieldInfo());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        for (int i = 0; i < packet.tileInformationLength(); i++) {
            rlbot.flat.DropshotTile tile = packet.tileInformation(i);
            DropshotTile existingTile = tiles.get(i);
            existingTile.setState(DropshotTileState.values()[tile.tileState()]);
        }
    }

}
