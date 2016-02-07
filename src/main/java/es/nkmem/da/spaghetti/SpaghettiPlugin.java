package es.nkmem.da.spaghetti;

import es.nkmem.da.spaghetti.state.NullGameState;
import es.nkmem.da.spaghetti.state.StateManager;
import es.nkmem.da.spaghetti.state.Transition;
import lombok.Getter;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpaghettiPlugin extends JavaPlugin {
    private static SpaghettiPlugin instance;
    private StateManager stateManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        stateManager = new StateManager(this);
        getLogger().info("Initialized StateManager");
    }

    @Override
    public void onDisable() {
        stateManager.nextState(new NullGameState(this), Transition.builder().build(), new MemoryConfiguration());
    }
}
