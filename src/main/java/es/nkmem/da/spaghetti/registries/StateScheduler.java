package es.nkmem.da.spaghetti.registries;

import es.nkmem.da.spaghetti.internal.DummyPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class StateScheduler {
    @NonNull
    private Plugin parent;
    @NonNull
    private DummyPlugin dummy;

    private BukkitScheduler getScheduler() {
        return parent.getServer().getScheduler();
    }

    public BukkitTask runTaskSync(Runnable r) {
        return getScheduler().runTask(dummy, r);
    }

    public BukkitTask runTaskAsync(Runnable r) {
        return getScheduler().runTaskAsynchronously(dummy, r);
    }

    public BukkitTask runTaskSyncLater(Runnable r, long delay) {
        return getScheduler().runTaskLater(dummy, r, delay);
    }

    public BukkitTask runTaskAsyncLater(Runnable r, long delay) {
        return getScheduler().runTaskLaterAsynchronously(dummy, r, delay);
    }

    public BukkitTask runTaskSyncTimer(Runnable r, long delay, long period) {
        return getScheduler().runTaskTimer(dummy, r, delay, period);
    }

    public BukkitTask runTaskAsyncTimer(Runnable r, long delay, long period) {
        return getScheduler().runTaskTimerAsynchronously(dummy, r, delay, period);
    }

    public void shutdown() {
        getScheduler().cancelTasks(dummy);
    }
}
