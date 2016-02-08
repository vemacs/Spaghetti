package es.nkmem.da.spaghetti.util;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.*;

@ToString(of = {"start", "end"})
@EqualsAndHashCode(of = {"start", "end"})
public final class VectorRange {
    private final Vector start;
    private final Vector end;

    public VectorRange(Vector start, Vector end) {
        Preconditions.checkNotNull(start, "start");
        Preconditions.checkNotNull(end, "end");

        Vector startNormalized = new Vector(
                Math.min(start.getX(), end.getX()),
                Math.min(start.getY(), end.getY()),
                Math.min(start.getZ(), end.getZ())
        );
        Vector endNormalized = new Vector(
                Math.max(start.getX(), end.getX()),
                Math.max(start.getY(), end.getY()),
                Math.max(start.getZ(), end.getZ())
        );
        this.start = startNormalized;
        this.end = endNormalized;
    }

    public VectorRange(VectorRange other) {
        this.start = other.getStart();
        this.end = other.getEnd();
    }

    public Vector getStart() {
        return start.clone();
    }

    public Vector getEnd() {
        return end.clone();
    }

    public boolean withinRange(Location location) {
        return withinRange(location, false);
    }

    public boolean withinRange(Location location, boolean ignoreY) {
        return withinRange(location.toVector(), ignoreY);
    }

    public boolean withinRange(Vector vec) {
        return withinRange(vec, CheckType.PRECISE);
    }

    public boolean withinRange(Vector vec, boolean ignoreY) {
        CheckType[] flags = new CheckType[2];
        flags[0] = CheckType.PRECISE;
        if (ignoreY)
            flags[1] = CheckType.IGNORE_Y;
        return withinRange(vec, flags);
    }

    public boolean withinRange(Vector vec, CheckType... type) {
        List<CheckType> args = new ArrayList<>(Arrays.asList(type));

        Preconditions.checkArgument(!(args.contains(CheckType.PRECISE) && args.contains(CheckType.BLOCK)), "both PRECISE and BLOCK checking were specified");
        boolean ignoreY = args.contains(CheckType.IGNORE_Y);

        if (args.contains(CheckType.PRECISE)) {
            double x = vec.getX(), y = vec.getY(), z = vec.getZ();
            return x >= start.getX() && x <= end.getX() &&
                    (ignoreY || y >= start.getY() && y <= end.getY()) &&
                    z >= start.getZ() && z <= end.getZ();
        } else if (args.contains(CheckType.BLOCK)) {
            int x = vec.getBlockX(), y = vec.getBlockY(), z = vec.getBlockZ();
            return x >= start.getX() && x <= end.getX() &&
                    (ignoreY || y >= start.getY() && y <= end.getY()) &&
                    z >= start.getZ() && z <= end.getZ();
        } else {
            throw new IllegalArgumentException("Unknown CheckType passed");
        }
    }

    public Vector getRandomVector(Random random) {
        return new Vector(
                start.getX() + (end.getX() - start.getX()) * random.nextDouble(),
                start.getY() + (end.getY() - start.getY()) * random.nextDouble(),
                start.getZ() + (end.getZ() - start.getZ()) * random.nextDouble()
        );
    }

    public static enum CheckType {
        /**
         * Uses floating point coordinates to compare.
         */
        PRECISE,
        /**
         * Uses integer coordinates to compare.
         */
        BLOCK,
        /**
         * Ignores the Y value.
         */
        IGNORE_Y
    }
}