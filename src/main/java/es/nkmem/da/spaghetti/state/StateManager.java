package es.nkmem.da.spaghetti.state;

import es.nkmem.da.spaghetti.SpaghettiPlugin;
import es.nkmem.da.spaghetti.data.WrappedLocation;
import es.nkmem.da.spaghetti.handlers.PlayerResetHandler;
import es.nkmem.da.spaghetti.handlers.WorldHandler;
import es.nkmem.da.spaghetti.registries.StateListenerRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StateManager {
    private SpaghettiPlugin spaghetti;
    private AbstractGameState currentState;

    public StateManager(SpaghettiPlugin spaghetti) {
        this.spaghetti = spaghetti;
        currentState = new NullGameState(spaghetti);
    }

    /**
     * @param state      The game state to move to
     * @param transition Details of the transition
     * @param data       Data that is passed to the next state (consider this something similar to an Android bundle)
     */
    public void nextState(@NonNull AbstractGameState state, Transition transition, MemoryConfiguration data) {
        // cleanup
        currentState.getCommandRegistry().shutdown();
        currentState.getListenerRegistry().shutdown();
        currentState.getScheduler().shutdown();
        currentState.cleanup();

        spaghetti.getLogger().info("Switching from " + currentState.getClass().getSimpleName() + " to " +
                state.getClass().getSimpleName() + ", with " + transition.toString());
        currentState = null; // Release references

        // handle transition
        WorldHandler worldHandler = spaghetti.getWorldHandler();
        boolean hasTeleported = false;
        if (transition.getResetPlayers()) {
            Bukkit.getOnlinePlayers().forEach(PlayerResetHandler::resetPlayer);
        }
        if (transition.getUnloadWorld() != null) {
            WrappedLocation teleportTo = transition.getTeleportTo();
            if (teleportTo == null
                    || teleportTo.getBukkitLocation().getWorld().equals(transition.getUnloadWorld())) {
                spaghetti.getLogger().severe("Teleport destination world not defined correctly, reverting to null");
                state = new NullGameState(spaghetti);
            } else {
                teleportPlayers(transition);
                hasTeleported = true;
                worldHandler.attemptWorldUnload(transition.getUnloadWorld());
            }
        }
        if (transition.getLoadWorld() != null) {
            worldHandler.loadMap(transition.getLoadWorld(), transition.getLoadEnvironment());
        }
        if (!hasTeleported) {
            teleportPlayers(transition);
        }

        // initialize new state
        StateListenerRegistry listenerRegistry = state.getListenerRegistry();
        if (transition.getTeleportTo() != null) {
            listenerRegistry.registerListener(
                    new TeleportListener(transition.getTeleportTo().getBukkitLocation().clone())
            );
        }
        if (transition.getResetPlayers()) {
            listenerRegistry.registerListener(
                    new ResetListener()
            );
        }
        currentState = state;
        state.initialize(data);
    }

    private void teleportPlayers(Transition transition) {
        if (transition.getTeleportTo() != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.teleport(transition.getTeleportTo().getBukkitLocation());
            }
        }
    }

    @RequiredArgsConstructor
    private class TeleportListener implements Listener {
        @NonNull
        private final Location teleportDest;

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerJoin(PlayerJoinEvent event) {
            event.getPlayer().teleport(teleportDest);
        }
    }

    private class ResetListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerJoin(PlayerJoinEvent event) {
            PlayerResetHandler.resetPlayer(event.getPlayer());
        }
    }
}
