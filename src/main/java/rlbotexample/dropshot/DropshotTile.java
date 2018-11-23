package rlbotexample.dropshot;


import rlbotexample.vector.Vector3;

/**
 * Representation of one of the floor tiles in dropshot mode.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class DropshotTile {

    public static final double TILE_WIDTH = 768;
    public static final double TILE_SIZE = 443.405;
    public static final double TILE_HEIGHT = 886.81;

    private final Vector3 location;
    private final int teamIndex;
    private DropshotTileState state;

    public DropshotTile(Vector3 location) {
        this.location = location;
        this.teamIndex = location.y < 0 ? 0 : 1;
    }

    public void setState(DropshotTileState state) {
        this.state = state;
    }

    public Vector3 getLocation() {
        return location;
    }

    public DropshotTileState getState() {
        return state;
    }

    public int getTeamIndex() {
        return teamIndex;
    }
}
