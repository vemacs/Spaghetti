package es.nkmem.da.spaghetti.registries;

import es.nkmem.da.spaghetti.internal.BukkitUtil;
import es.nkmem.da.spaghetti.internal.DummyPlugin;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class StateCommandRegistry {
    @NonNull
    private Plugin parent;
    @NonNull
    private DummyPlugin dummy;

    private Map<String, Command> getKnownCommands() {
        CommandMap commandMap = BukkitUtil.getCommandMap(parent.getServer());
        return BukkitUtil.getKnownCommands(commandMap);
    }

    public void registerCommand(String name, CommandExecutor executor) {
        CommandMap commandMap = BukkitUtil.getCommandMap(parent.getServer());
        getKnownCommands().put(name, null);
        commandMap.register(name, new StateCommand(name, dummy, executor));
    }

    public void unregisterCommand(String name) {
        getKnownCommands().remove(name);
    }

    public void shutdown() {
        Iterator<Command> itr = getKnownCommands().values().iterator();
        while (itr.hasNext()) {
            Command cmd = itr.next();
            if (cmd instanceof StateCommand) {
                if (((StateCommand) cmd).getPlugin().equals(dummy)) {
                    itr.remove();
                }
            }
        }
    }

    public class StateCommand extends Command {
        @Getter
        private Plugin plugin;
        private CommandExecutor executor;

        protected StateCommand(String name, Plugin plugin, CommandExecutor executor) {
            super(name);
            this.plugin = plugin;
            this.executor = executor;
        }

        @Override
        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            return executor.onCommand(sender, this, commandLabel, args);
        }
    }
}
