package es.nkmem.da.spaghetti.handlers;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Map;

public class NMSHandler {
    private static String ver;

    static {
        String name = Bukkit.getServer().getClass().getName();
        String[] parts = name.split("\\.");
        ver = parts[3];
    }

    private static Method getHandleCraftPlayer;
    private static Method resetDataWatcher;
    private static Map<String, String> resetMethod = ImmutableMap.<String, String>builder()
            .put("v1_7_", "c")
            .put("v1_8_", "h")
            .build();

    public static void resetDataWatchers(Player p) throws Throwable {
        if (getHandleCraftPlayer == null) {
            getHandleCraftPlayer = p.getClass().getDeclaredMethod("getHandle");
            getHandleCraftPlayer.setAccessible(true);
        }
        Object entityPlayer = getHandleCraftPlayer.invoke(p);
        if (resetDataWatcher == null) {
            String methodName = null;
            for (Map.Entry<String, String> entry : resetMethod.entrySet()) {
                if (ver.startsWith(entry.getKey())) {
                    methodName = entry.getValue();
                }
            }
            if (methodName == null) {
                throw new RuntimeException("Undefined field name for player reset");
            }
            resetDataWatcher = entityPlayer.getClass() // EntityPlayer
                    .getSuperclass() // EntityHuman
                    .getDeclaredMethod(methodName);
            resetDataWatcher.setAccessible(true);
        }
        resetDataWatcher.invoke(entityPlayer);
    }
}
