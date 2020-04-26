package rlbotexample.input;

import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.BallPrediction;
import rlbot.flat.Physics;
import rlbot.flat.PredictionSlice;
import rlbotexample.input.ball.BallData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModernBallPrediction {

    public final int tickRate; // close to 60 or 120
    public final float tickFrequency; // close to 1/60 or 1/120
    public final List<ModernPredictionFrame> frames;

    private ModernBallPrediction(List<ModernPredictionFrame> frames, float tickFrequency) {
        if (frames == null)
            frames = new ArrayList<>();
        float latencyCompensation = RLConstants.gameLatencyCompensation;
        if (latencyCompensation > 0) {
            List<ModernPredictionFrame> newFrames = new ArrayList<>(frames.size());
            for (ModernPredictionFrame frame : frames) {
                if (frame.relativeTime < latencyCompensation)
                    continue;

                frame.adjustForLatencyCompensation(latencyCompensation);
                newFrames.add(frame);
            }
            frames = newFrames;
        }
        frames = Collections.unmodifiableList(frames);

        this.frames = frames;
        this.tickFrequency = tickFrequency;
        this.tickRate = Math.round(1 / tickFrequency);
    }

    public static ModernBallPrediction from(List<ModernPredictionFrame> frames, float tickFrequency) {
        return new ModernBallPrediction(frames, tickFrequency);
    }

    public static ModernBallPrediction empty() {
        return new ModernBallPrediction(null, RLConstants.tickFrequency);
    }

    private static ModernBallPrediction from(BallPrediction ballPrediction) {
        assert ballPrediction.slicesLength() > 0 : "RLBot Ball Prediction has no frames";

        List<ModernPredictionFrame> frames = new ArrayList<>(ballPrediction.slicesLength());

        float startTime = ballPrediction.slices(0).gameSeconds();
        float lastTime = startTime;
        float averageDt = 0;

        for (int i = 0; i < ballPrediction.slicesLength(); i++) {
            PredictionSlice slice = ballPrediction.slices(i);
            frames.add(new ModernPredictionFrame(slice.gameSeconds() - startTime, slice));

            averageDt += slice.gameSeconds() - lastTime;
            lastTime = slice.gameSeconds();
        }

        averageDt /= ballPrediction.slicesLength();

        return new ModernBallPrediction(frames, averageDt);
    }

    public static ModernBallPrediction get() {
        return get(BallPredictionType.RLBOT);
    }

    public static ModernBallPrediction get(BallPredictionType ballPredictionType) {
        switch (ballPredictionType) {
            case RLBOT:
                try {
                    return ModernBallPrediction.from(RLBotDll.getBallPrediction());
                } catch (RLBotInterfaceException e) {
                    System.err.println("Could not get RLBot ball Prediction!");
                    throw new RuntimeException(e);
                }
        }

        throw new IllegalStateException("Ball Prediction Type '" + ballPredictionType.name() + "' not recognized");
    }

    /*public void draw(AdvancedRenderer renderer, Color color, float length) {
        if (this.frames.size() == 0)
            return;
        if (length <= 0)
            length = this.relativeTimeOfLastFrame();

        float time = 0;
        float lastAbsTime = this.frames.get(0).absoluteTime;
        Vector3 lastPos = this.frames.get(0).ballData.position;
        while (time < length) {
            Optional<YangPredictionFrame> frame = this.getFrameAfterRelativeTime(time);
            if (!frame.isPresent())
                break;

            if (Math.floor(lastAbsTime) < Math.floor(frame.get().absoluteTime)) {
                renderer.drawLine3d(color.brighter(), frame.get().ballData.position, frame.get().ballData.position.add(0, 0, 50));
            }
            lastAbsTime = frame.get().absoluteTime;
            time = frame.get().relativeTime;
            ImmutableBallData ball = frame.get().ballData;

            if (ball.makeMutable().isInAnyGoal()) {
                renderer.drawCentered3dCube(color.brighter().brighter(), ball.position, 50);
                renderer.drawString3d(String.format("Goal! (%.1f)", time), Color.WHITE, ball.position.add(0, 0, 150), 1, 1);
                renderer.drawLine3d(color, lastPos, ball.position);
                break;
            }

            if (lastPos.distance(ball.position) < 50)
                continue;
            renderer.drawLine3d(color, lastPos, ball.position);
            lastPos = ball.position;
        }
    }*/

    public float relativeTimeOfLastFrame() {
        if (this.frames.size() == 0)
            return -1;
        return this.frames.get(this.frames.size() - 1).relativeTime;
    }

    public ModernBallPrediction trim(float relativeStartTime, float relativeEndTime) {
        if (relativeEndTime < relativeStartTime)
            throw new IllegalArgumentException("Relative end time smaller than relative start time");
        if (relativeEndTime - relativeStartTime == 0)
            return ModernBallPrediction.empty();

        return new ModernBallPrediction(this.getFramesBetweenRelative(relativeStartTime, relativeEndTime), this.tickFrequency);
    }

    public Optional<ModernPredictionFrame> getFrameAtRelativeTime(float relativeTime) {
        return this.frames
                .stream()
                .filter((f) -> f.relativeTime >= relativeTime)
                .findFirst();
    }

    public Optional<ModernPredictionFrame> getFrameAfterRelativeTime(float relativeTime) {
        return this.frames
                .stream()
                .filter((f) -> f.relativeTime > relativeTime)
                .findFirst();
    }

    public Optional<ModernPredictionFrame> getFrameAtAbsoluteTime(float absolute) {
        return this.frames
                .stream()
                .filter((f) -> f.absoluteTime >= absolute)
                .findFirst();
    }

    public List<ModernPredictionFrame> getFramesBeforeRelative(float relativeTime) {
        return this.frames
                .stream()
                .filter((f) -> f.relativeTime < relativeTime)
                .collect(Collectors.toList());
    }

    public ModernBallPrediction getBeforeRelative(float relativeTime) {
        return new ModernBallPrediction(getFramesBeforeRelative(relativeTime), this.tickFrequency);
    }

    public List<ModernPredictionFrame> getFramesAfterRelative(float relativeTime) {
        return this.frames
                .stream()
                .filter((f) -> f.relativeTime > relativeTime)
                .collect(Collectors.toList());
    }

    public List<ModernPredictionFrame> getFramesBetweenRelative(float start, float end) {
        return this.frames
                .stream()
                .filter((f) -> f.relativeTime > start && f.relativeTime < end)
                .collect(Collectors.toList());
    }

    public boolean hasFrames() {
        return this.frames.size() > 0;
    }

    enum BallPredictionType {
        RLBOT
    }

    public static class ModernPredictionFrame {
        public final float absoluteTime;
        public final BallData ballData;
        public float relativeTime;

        public ModernPredictionFrame(float absoluteTime, float relativeTime, BallData ballData) {
            this.absoluteTime = absoluteTime;
            this.relativeTime = relativeTime;
            this.ballData = ballData;
        }

        public ModernPredictionFrame(float relativeTime, PredictionSlice predictionSlice) {
            this.relativeTime = relativeTime;
            this.absoluteTime = predictionSlice.gameSeconds();
            Physics physics = predictionSlice.physics();
            this.ballData = new BallData(physics);
        }

        public void adjustForLatencyCompensation(float offset) {
            relativeTime -= offset;
        }
    }
}
