package rlbotexample.dropshot;

import java.util.Objects;

/**
 * This class is used to convert a 2d point to a hex grid in the DropshotTileManager. Look here for more information:
 * https://www.redblobgames.com/grids/hexagons/
 *
 * This class is here for your convenience, it is NOT part of the framework. You can add to it as much
 * as you want, or delete it.
 */
public class Hex {

    // For a hex the following must always be true: q + r + s == 0
    public final int q, r, s;

    public Hex(int q, int r) {
        this.q = q;
        this.r = r;
        this.s = -q - r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hex that = (Hex) o;
        return q == that.q &&
                r == that.r;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r);
    }

    /**
     * Construct a Hex from rounding two floating point q and r coordinates.
     */
    public static Hex fromRounding(double fq, double fr) {
        double fs = -fq - fr;

        int rx = (int)Math.round(fq);
        int ry = (int)Math.round(fr);
        int rz = (int)Math.round(fs);

        // Find how much each component was rounded
        double x_diff = Math.abs(rx - fq);
        double y_diff = Math.abs(ry - fr);
        double z_diff = Math.abs(rz - fs);

        // We reset the component with the largest change back to what the constraint rx + ry + rz = 0 requires
        if (x_diff > y_diff && x_diff > z_diff) {
            rx = -ry - rz;
        } else if (y_diff > z_diff) {
            ry = -rx - rz;
        }

        return new Hex(rx, ry);
    }
}
