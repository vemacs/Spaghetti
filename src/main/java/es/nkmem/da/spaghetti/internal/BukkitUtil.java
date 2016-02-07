package es.nkmem.da.spaghetti.internal;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class BukkitUtil {
    private static Method getCommandMap;

    public static CommandMap getCommandMap(Server server) {
        try {
            if (getCommandMap == null) {
                getCommandMap = server.getClass().getDeclaredMethod("getCommandMap");
                getCommandMap.setAccessible(true);
            }
            return (CommandMap) getCommandMap.invoke(server);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    private static Field knownCommands;

    @SuppressWarnings("unchecked")
    public static Map<String, Command> getKnownCommands(CommandMap commandMap) {
        try {
            if (knownCommands == null) {
                knownCommands = commandMap.getClass().getDeclaredField("knownCommands");
                knownCommands.setAccessible(true);
            }
            return (Map<String, Command>) knownCommands.get(commandMap);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }
}