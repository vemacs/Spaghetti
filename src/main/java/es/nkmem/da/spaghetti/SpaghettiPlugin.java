package es.nkmem.da.spaghetti;

import es.nkmem.da.spaghetti.handlers.WorldHandler;
import es.nkmem.da.spaghetti.state.NullGameState;
import es.nkmem.da.spaghetti.state.StateManager;
import es.nkmem.da.spaghetti.state.Transition;
import lombok.Getter;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SpaghettiPlugin extends JavaPlugin {
    @Getter
    private static SpaghettiPlugin instance;
    @Getter
    private StateManager stateManager;
    @Getter
    private WorldHandler worldHandler;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        stateManager = new StateManager(this);
        getLogger().info("Initialized StateManager");

        worldHandler = new WorldHandler(this,
                getConfig().getString("maps-directory"),
                getConfig().getString("game-world-name"));
        worldHandler.clearGameWorld();
        getLogger().info("Initialized World Handler and cleared maps directory");
    }

    @Override
    public void onDisable() {
        stateManager.nextState(new NullGameState(this), Transition.builder().build(), new MemoryConfiguration());
    }
}
