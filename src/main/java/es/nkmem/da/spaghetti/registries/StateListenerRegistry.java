package es.nkmem.da.spaghetti.registries;

import es.nkmem.da.spaghetti.internal.DummyPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@RequiredArgsConstructor
public class StateListenerRegistry {
    @NonNull
    private Plugin parent;
    @NonNull
    private DummyPlugin dummy;

    private PluginManager getPluginManager() {
        return parent.getServer().getPluginManager();
    }

    public void registerListener(Listener listener) {
        getPluginManager().registerEvents(listener, dummy);
    }

    public void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public void shutdown() {
        HandlerList.unregisterAll(dummy);
    }
}
