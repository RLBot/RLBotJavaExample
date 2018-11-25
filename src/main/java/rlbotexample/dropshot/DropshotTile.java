package rlbotexample.dropshot;


import rlbotexample.vector.Vector3;

/**
 * Representation of one of the floor tiles in dropshot mode.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class DropshotTile {

    public static final double TILE_SIZE = 443.405; // side length and length from center to side
    public static final double TILE_WIDTH = 768; // length from side to opposite side
    public static final double TILE_HEIGHT = 886.81; // length from corner to opposite corner

    private final Vector3 location;
    private final int team;
    private DropshotTileState state;

    public DropshotTile(Vector3 location) {
        this.location = location;
        this.team = location.y < 0 ? 0 : 1;
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

    public int getTeam() {
        return team;
    }
}
