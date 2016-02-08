package es.nkmem.da.spaghetti.handlers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class PlayerResetHandler {
    private static boolean errorShown = false;

    public static void resetPlayer(Player p) {
        if (p.isInsideVehicle()) {
            p.getVehicle().eject();
        }

        p.setHealth(20.0D);
        p.setFoodLevel(20);
        p.setLevel(0);
        p.setExp(0.0F);
        p.setTotalExperience(0);
        p.setGameMode(GameMode.SURVIVAL);
        p.setAllowFlight(false);

        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }

        p.getInventory().clear();
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);

        try {
           // NMSHandler.resetDataWatchers(p);
        } catch (Throwable t) {
            if (!errorShown) {
                errorShown = true;
                t.printStackTrace();
            }
        }
    }
}
