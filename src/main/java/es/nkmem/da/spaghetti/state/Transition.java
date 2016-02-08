package es.nkmem.da.spaghetti.state;

import es.nkmem.da.spaghetti.data.WrappedLocation;
import lombok.Builder;
import lombok.Data;
import org.bukkit.World;

@Data
@Builder
public class Transition {
    private Boolean resetPlayers;
    private WrappedLocation teleportTo;
    private World unloadWorld;
    private String loadWorld;
    private World.Environment loadEnvironment;

    Transition(Boolean resetPlayers, WrappedLocation teleportTo,
               World unloadWorld, String loadWorld, World.Environment loadEnvironment) {
        if (resetPlayers == null) {
            this.resetPlayers = false;
        } else {
            this.resetPlayers = resetPlayers;
        }
        this.teleportTo = teleportTo;
        this.unloadWorld = unloadWorld;
        this.loadWorld = loadWorld;
        if (loadEnvironment == null) {
            this.loadEnvironment = World.Environment.NORMAL;
        } else {
            this.loadEnvironment = loadEnvironment;
        }
    }
}