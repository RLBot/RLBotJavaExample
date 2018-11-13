package rlbotexample.dropshot;


import rlbotexample.vector.Vector3;

/**
 * Representation of one of the floor tiles in dropshot mode.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class DropshotTile {

    private final Vector3 location;
    private DropshotTileState state;

    public DropshotTile(Vector3 location) {
        this.location = location;
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
}
