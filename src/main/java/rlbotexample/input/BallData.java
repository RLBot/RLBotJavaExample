package rlbotexample.input;


import rlbot.flat.BallInfo;
import rlbotexample.vector.Vector3;

public class BallData {
    public final Vector3 position;
    public final Vector3 velocity;
    public final Vector3 spin;

    public BallData(final BallInfo ball) {
        this.position = Vector3.fromFlatbuffer(ball.physics().location());
        this.velocity = Vector3.fromFlatbuffer(ball.physics().velocity());
        this.spin = Vector3.fromFlatbuffer(ball.physics().angularVelocity());
    }
}
