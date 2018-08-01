package rlbotexample.boost;


import rlbotexample.vector.Vector3;

public class BoostPad {

    private final Vector3 location;
    private final boolean isFullBoost;
    private boolean isActive;

    public BoostPad(Vector3 location, boolean isFullBoost) {
        this.location = location;
        this.isFullBoost = isFullBoost;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Vector3 getLocation() {
        return location;
    }

    public boolean isFullBoost() {
        return isFullBoost;
    }

    public boolean isActive() {
        return isActive;
    }
}
