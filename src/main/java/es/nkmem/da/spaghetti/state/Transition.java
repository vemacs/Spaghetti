package es.nkmem.da.spaghetti.state;

import es.nkmem.da.spaghetti.data.WrappedLocation;
import lombok.Builder;
import lombok.Data;
import org.bukkit.World;

@Data
@Builder
public class Transition {
    private Boolean resetPlayers;
    private WrappedLocation teleportTo = null;
    private World unloadWorld = null;
    private String loadWorld = null;
    private World.Environment loadEnvironment;
}
