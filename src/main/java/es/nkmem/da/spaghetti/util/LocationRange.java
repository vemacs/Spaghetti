package es.nkmem.da.spaghetti.util;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.List;

/**
 This is a bridge between {@link VectorRange} and Bukkit's locations.
 This allows easy iteration over blocks within a range.
 */
public class LocationRange {
    @Getter
    private final List<Location> locations;

    /**
     * Creates a new {@link LocationRange} instance, fills the internal location list
     * with the blocks at locations within the range in the provided world.
     *
     * @param range the {@link VectorRange}
     * @param world the {@link org.bukkit.World}
     */
    public LocationRange(VectorRange range, World world) {
        Vector start = range.getStart();
        Vector end = range.getEnd();

        ImmutableList.Builder<Location> listBuilder = ImmutableList.builder();

        for(int x = start.getBlockX(); x <= end.getBlockX(); ++x) {
            for(int y = start.getBlockY(); y <= end.getBlockY(); ++y) {
                for(int z = start.getBlockZ(); z <= end.getBlockZ(); ++z) {
                    Location blockLocation = new Location(world, x, y, z);
                    listBuilder.add(blockLocation);
                }
            }
        }

        locations = listBuilder.build();
    }
}
