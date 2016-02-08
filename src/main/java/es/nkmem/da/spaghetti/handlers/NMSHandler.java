package es.nkmem.da.spaghetti.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class NMSHandler {
    private static String ver;

    static {
        String name = Bukkit.getServer().getClass().getName();
        String[] parts = name.split("\\.");
        ver = parts[3];
    }

    private static Method getHandleCraftPlayer;
    private static Method getDataWatcher;
    private static Method watch;

    public static void setDataWatcher(Player p, int key, byte value) throws Throwable {
        if (getHandleCraftPlayer == null) {
            getHandleCraftPlayer = p.getClass().getDeclaredMethod("getHandle");
            getHandleCraftPlayer.setAccessible(true);
        }
        Object entityPlayer = getHandleCraftPlayer.invoke(p);
        if (getDataWatcher == null) {
            Class<?> nmsEntity = Class.forName("net.minecraft.server." + ver + ".Entity");
            getDataWatcher = nmsEntity.getDeclaredMethod("GetDataWatcher");
            getDataWatcher.setAccessible(true);
        }
        Object dataWatcher = getDataWatcher.invoke(entityPlayer);
        if (watch == null) {
            for (Method m : dataWatcher.getClass().getDeclaredMethods()) {
                if (m.getName().equals("watch")) {
                    watch = m;
                    watch.setAccessible(true);
                    break;
                }
            }
        }
        watch.invoke(dataWatcher, key, value);
    }

    public static void removeArrows(Player p) throws Throwable {
        setDataWatcher(p, 9, (byte) 0);
    }
}
