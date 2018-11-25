package rlbotexample.dropshot;

import rlbot.cppinterop.RLBotDll;
import rlbot.flat.FieldInfo;
import rlbot.flat.GameTickPacket;
import rlbotexample.vector.Vector2;
import rlbotexample.vector.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static rlbotexample.dropshot.DropshotTile.TILE_HEIGHT;
import static rlbotexample.dropshot.DropshotTile.TILE_WIDTH;

/**
 * Information about where dropshot tiles are located in the arena and what state they have. Can also convert a
 * vector2 point to a tile, which is useful for checking the state of the tile where the ball lands.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class DropshotTileManager {

    private static final ArrayList<DropshotTile> tiles = new ArrayList<>();
    private static final HashMap<Hex, DropshotTile> blueTileMap = new HashMap<>();
    private static final HashMap<Hex, DropshotTile> orangeTileMap = new HashMap<>();

    public static List<DropshotTile> getTiles() {
        return tiles;
    }

    private static void loadFieldInfo(FieldInfo fieldInfo) {

        synchronized (tiles) {

            tiles.clear();

            for (int i = 0; i < fieldInfo.goalsLength(); i++) {
                rlbot.flat.GoalInfo goalInfo = fieldInfo.goals(i);
                Vector3 location = new Vector3(goalInfo.location());
                DropshotTile tile = new DropshotTile(location);
                tiles.add(new DropshotTile(location));

                Hex hex = pointToHex(location.flatten());
                if (location.y < 0) {
                    blueTileMap.put(hex, tile);
                } else {
                    orangeTileMap.put(hex, tile);
                }
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

    /**
     * Returns the tile under the point, or null if none is.
     */
    public static DropshotTile pointToTile(Vector2 point) {
        Hex hex = pointToHex(point);
        if (point.y < 0) return blueTileMap.get(hex);
        else return orangeTileMap.get(hex);
    }

    /**
     * Converts a point to a hex.
     */
    private static Hex pointToHex(Vector2 point) {

        // Apply offset
        if (point.y < 0) {
            point = point.plus(new Vector2(0, 128));
        } else {
            point = point.plus(new Vector2(0, -128));
        }

        // Calculate q and r component
        double q = point.x / TILE_WIDTH - point.y * 2 / (3 * TILE_HEIGHT);
        double r = point.y * 4 / (3 * TILE_HEIGHT);
        return Hex.fromRounding(q, r);
    }
}
