package rlbotexample.output;

import rlbot.ControllerState;

/**
 * A helper class for returning controls for your bot.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class Controls implements ControllerState {

    // 0 is straight, -1 is hard left, 1 is hard right.
    private float steer;

    // -1 for front flip, 1 for back flip
    private float pitch;

    // 0 is straight, -1 is hard left, 1 is hard right.
    private float yaw;

    // 0 is straight, -1 is hard left, 1 is hard right.
    private float roll;

    // 0 is none, -1 is backwards, 1 is forwards
    private float throttle;

    private boolean jumpDepressed;
    private boolean boostDepressed;
    private boolean slideDepressed;
    private boolean useItemDepressed;

    public Controls() {
    }

    public Controls withSteer(double steer) {
        this.steer = clamp(steer);
        return this;
    }

    public Controls withPitch(double pitch) {
        this.pitch = clamp(pitch);
        return this;
    }

    public Controls withYaw(double yaw) {
        this.yaw = clamp(yaw);
        return this;
    }

    public Controls withRoll(double roll) {
        this.roll = clamp(roll);
        return this;
    }

    public Controls withThrottle(double throttle) {
        this.throttle = clamp(throttle);
        return this;
    }

    public Controls withJump(boolean jumpDepressed) {
        this.jumpDepressed = jumpDepressed;
        return this;
    }

    public Controls withBoost(boolean boostDepressed) {
        this.boostDepressed = boostDepressed;
        return this;
    }

    public Controls withSlide(boolean slideDepressed) {
        this.slideDepressed = slideDepressed;
        return this;
    }

    public Controls withUseItem(boolean useItemDepressed) {
        this.useItemDepressed = useItemDepressed;
        return this;
    }

    public Controls withJump() {
        this.jumpDepressed = true;
        return this;
    }

    public Controls withBoost() {
        this.boostDepressed = true;
        return this;
    }

    public Controls withSlide() {
        this.slideDepressed = true;
        return this;
    }

    public Controls withUseItem() {
        this.useItemDepressed = true;
        return this;
    }

    private float clamp(double value) {
        return Math.max(-1, Math.min(1, (float)value));
    }

    @Override
    public float getSteer() {
        return steer;
    }

    @Override
    public float getThrottle() {
        return throttle;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getRoll() {
        return roll;
    }

    @Override
    public boolean holdJump() {
        return jumpDepressed;
    }

    @Override
    public boolean holdBoost() {
        return boostDepressed;
    }

    @Override
    public boolean holdHandbrake() {
        return slideDepressed;
    }

    @Override
    public boolean holdUseItem() {
        return useItemDepressed;
    }
}