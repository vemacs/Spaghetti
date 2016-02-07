package es.nkmem.da.spaghetti.state;

import es.nkmem.da.spaghetti.SpaghettiPlugin;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.configuration.MemoryConfiguration;

@Data
public class StateManager {
    @NonNull
    private SpaghettiPlugin spaghetti;

    private AbstractGameState currentState;

    /**
     *
     * @param state The game state to move to
     * @param transition Details of the transition
     * @param data Data that is passed to the next state (consider this something similar to an Android bundle)
     */
    public void nextState(AbstractGameState state, Transition transition, MemoryConfiguration data) {
        currentState.getCommandRegistry().shutdown();
        currentState.getListenerRegistry().shutdown();
        currentState.getScheduler().shutdown();
        currentState = null; // Release references

        // handle transition
        if (transition.isResetPlayers()) {

        }
        if (transition.getUnloadWorld() != null) {

        }
        if (transition.getLoadWorld() != null) {

        }


        currentState = state;
        state.initialize(data);
    }
}
