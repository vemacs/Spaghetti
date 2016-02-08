package es.nkmem.da.spaghetti.state;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;

@Builder
@Data
public class Transition {
    private boolean resetPlayers = false;
    private Location teleportTo = null;
    private World unloadWorld = null;
    private String loadWorld = null;
    private World.Environment loadEnvironment = World.Environment.NORMAL;
}
