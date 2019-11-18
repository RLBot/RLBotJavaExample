package rlbotexample.output;

import rlbot.ControllerState;

/**
 * A helper class for returning controls for your bot.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class ControlsOutput implements ControllerState {

    // Single builder instance to avoid multiple object instantiation per game tick
    private static final Builder BUILDER = new Builder();

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

    public ControlsOutput() { }

    public static class Builder {

        private ControlsOutput controlsOutput;

        private Builder() {
            reset();
        }

        private void reset() {
            this.controlsOutput = new ControlsOutput();
        }

        public Builder withSteer(float steer) {
            controlsOutput.steer = clamp(steer);
            return this;
        }

        public Builder withPitch(float pitch) {
            controlsOutput.pitch = clamp(pitch);
            return this;
        }

        public Builder withYaw(float yaw) {
            controlsOutput.yaw = clamp(yaw);
            return this;
        }

        public Builder withRoll(float roll) {
            controlsOutput.roll = clamp(roll);
            return this;
        }

        public Builder withThrottle(float throttle) {
            controlsOutput.throttle = clamp(throttle);
            return this;
        }

        public Builder withJump(boolean jumpDepressed) {
            controlsOutput.jumpDepressed = jumpDepressed;
            return this;
        }

        public Builder withBoost(boolean boostDepressed) {
            controlsOutput.boostDepressed = boostDepressed;
            return this;
        }

        public Builder withSlide(boolean slideDepressed) {
            controlsOutput.slideDepressed = slideDepressed;
            return this;
        }

        public Builder withUseItem(boolean useItemDepressed) {
            controlsOutput.useItemDepressed = useItemDepressed;
            return this;
        }

        public Builder withJump() {
            controlsOutput.jumpDepressed = true;
            return this;
        }

        public Builder withBoost() {
            controlsOutput.boostDepressed = true;
            return this;
        }

        public Builder withSlide() {
            controlsOutput.slideDepressed = true;
            return this;
        }

        public Builder withUseItem() {
            controlsOutput.useItemDepressed = true;
            return this;
        }

        public ControlsOutput build() {
            return controlsOutput;
        }

        private float clamp(float value) {
            return Math.max(-1, Math.min(1, value));
        }
    }

    public static Builder builder() {
        BUILDER.reset();
        return BUILDER;
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
