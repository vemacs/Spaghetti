package es.nkmem.da.spaghetti.state;

import es.nkmem.da.spaghetti.SpaghettiPlugin;
import lombok.Data;
import org.bukkit.configuration.MemoryConfiguration;

@Data
public class StateManager {
    private SpaghettiPlugin spaghetti;
    private AbstractGameState currentState;

    public StateManager(SpaghettiPlugin spaghetti) {
        this.spaghetti = spaghetti;
        currentState = new NullGameState(spaghetti);
    }

    /**
     *
     * @param state The game state to move to
     * @param transition Details of the transition
     * @param data Data that is passed to the next state (consider this something similar to an Android bundle)
     */
    public void nextState(AbstractGameState state, Transition transition, MemoryConfiguration data) {
        // cleanup
        currentState.getCommandRegistry().shutdown();
        currentState.getListenerRegistry().shutdown();
        currentState.getScheduler().shutdown();
        currentState.cleanup();
        currentState = null; // Release references

        // handle transition
        // TODO: implement handlers
        if (transition.isResetPlayers()) {

        }
        if (transition.getUnloadWorld() != null) {

        }
        if (transition.getLoadWorld() != null) {

        }

        // initialize new state
        if (state == null) {
            spaghetti.getLogger().warning("A null game state was passed. Was this expected behavior?");
            state = new NullGameState(spaghetti);
        }
        currentState = state;
        state.initialize(data);
    }
}
