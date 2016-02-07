package es.nkmem.da.spaghetti.state;

import es.nkmem.da.spaghetti.SpaghettiPlugin;
import es.nkmem.da.spaghetti.internal.DummyPlugin;
import es.nkmem.da.spaghetti.registries.StateCommandRegistry;
import es.nkmem.da.spaghetti.registries.StateListenerRegistry;
import es.nkmem.da.spaghetti.registries.StateScheduler;
import lombok.Getter;
import org.bukkit.configuration.MemoryConfiguration;

@Getter
public abstract class AbstractGameState {
    private SpaghettiPlugin parent;
    private DummyPlugin dummy;

    private StateCommandRegistry commandRegistry;
    private StateListenerRegistry listenerRegistry;
    private StateScheduler scheduler;

    protected AbstractGameState(SpaghettiPlugin parent) {
        this.parent = parent;
        dummy = new DummyPlugin(parent, getClass().getSimpleName());
        commandRegistry = new StateCommandRegistry(parent, dummy);
        listenerRegistry = new StateListenerRegistry(parent, dummy);
        scheduler = new StateScheduler(parent, dummy);
    }

    public abstract void initialize(MemoryConfiguration data);

    public final void stop(AbstractGameState next, Transition stateTransition) {
        stop(next, stateTransition, new MemoryConfiguration());
    }

    public final void stop(AbstractGameState next, Transition stateTransition, MemoryConfiguration data) {
        parent.getStateManager().nextState(next, stateTransition, data);
    }
}
