package es.nkmem.da.spaghetti.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@AllArgsConstructor
@Data
public class WrappedLocation {
    @NonNull
    private String name;
    private double x, y, z;
    private float yaw, pitch;

    public WrappedLocation(String worldName, double x, double y, double z) {
        this(worldName, x, y, z, 0, 0);
    }

    public Location getBukkitLocation() {
        return new Location(Bukkit.getWorld(name), x, y, z, yaw, pitch);
    }
}
