package es.nkmem.da.spaghetti;

import es.nkmem.da.spaghetti.state.NullGameState;
import es.nkmem.da.spaghetti.state.StateManager;
import es.nkmem.da.spaghetti.state.Transition;
import lombok.Getter;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SpaghettiPlugin extends JavaPlugin {
    @Getter
    private static SpaghettiPlugin instance;
    @Getter
    private StateManager stateManager;
    @Getter
    private File mapsDirectory;
    @Getter
    private String gameWorldName;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        stateManager = new StateManager(this);
        getLogger().info("Initialized StateManager");
        mapsDirectory = new File(getConfig().getString("maps-directory"));
        gameWorldName = getConfig().getString("game-world-name");
    }

    @Override
    public void onDisable() {
        stateManager.nextState(new NullGameState(this), Transition.builder().build(), new MemoryConfiguration());
    }
}
