package es.nkmem.da.spaghetti;

import es.nkmem.da.spaghetti.state.StateManager;
import lombok.Getter;
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


}
