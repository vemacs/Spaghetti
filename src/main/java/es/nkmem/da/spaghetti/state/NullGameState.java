package es.nkmem.da.spaghetti.state;

import es.nkmem.da.spaghetti.SpaghettiPlugin;
import org.bukkit.configuration.MemoryConfiguration;

public class NullGameState extends AbstractGameState {
    public NullGameState(SpaghettiPlugin parent) {
        super(parent);
    }

    @Override
    public void initialize(MemoryConfiguration data) {
    }
}
