package es.nkmem.da.spaghetti.state;

import es.nkmem.da.spaghetti.SpaghettiPlugin;
import es.nkmem.da.spaghetti.internal.DummyPlugin;
import es.nkmem.da.spaghetti.registries.StateCommandRegistry;
import es.nkmem.da.spaghetti.registries.StateListenerRegistry;
import es.nkmem.da.spaghetti.registries.StateScheduler;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.configuration.MemoryConfiguration;

@Getter
public abstract class AbstractGameState {
    private SpaghettiPlugin parent;
    private DummyPlugin dummy;

    private StateCommandRegistry commandRegistry;
    private StateListenerRegistry listenerRegistry;
    private StateScheduler scheduler;

    protected AbstractGameState(@NonNull SpaghettiPlugin parent) {
        this.parent = parent;
        dummy = new DummyPlugin(parent, getClass().getSimpleName());
        commandRegistry = new StateCommandRegistry(parent, dummy);
        listenerRegistry = new StateListenerRegistry(parent, dummy);
        scheduler = new StateScheduler(parent, dummy);
    }

    public abstract void initialize(MemoryConfiguration data);

    /**
     * This method should be used for cleanup operations (e.g. on server shutdown)
     * It can be left empty
     */
    public void cleanup() {
    }

    public final void stop(AbstractGameState next, Transition stateTransition) {
        stop(next, stateTransition, new MemoryConfiguration());
    }

    public final void stop(AbstractGameState next, Transition stateTransition, MemoryConfiguration data) {
        parent.getStateManager().nextState(next, stateTransition, data);
    }
}
