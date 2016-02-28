package es.nkmem.da.spaghetti.handlers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Getter
public class WorldHandler {
    private File mapsDirectory;
    private File gameWorldFile;
    private String gameWorldName;
    private String lastLoadedMap;

    private Plugin parent;

    public WorldHandler(Plugin parent, String mapsDirectory, String gameWorldName) {
        this.parent = parent;
        this.mapsDirectory = new File(mapsDirectory);
        this.gameWorldFile = new File(gameWorldName);
        this.gameWorldName = gameWorldName;
    }

    public World getGameWorld() {
        return Bukkit.getWorld(gameWorldName);
    }

    public FileConfiguration loadWorldConfig() throws FileNotFoundException {
        File configFile = new File(gameWorldFile, "config.yml");
        if (!configFile.exists()) {
            throw new FileNotFoundException("World configuration not found");
        }
        return YamlConfiguration.loadConfiguration(new File(gameWorldFile, "config.yml"));
    }

    /*
    Map loading/unloading functionality
     */
    public World loadMap(String mapName, World.Environment environment) {
        try {
            try {
                clearGameWorld();
            } catch (Throwable ignored) {
            }
            WorldHandler.copyFile(new File(mapsDirectory, mapName), gameWorldFile);
            WorldCreator wc = new WorldCreator(gameWorldName);
            wc.environment(environment);
            World w =  Bukkit.createWorld(wc);
            lastLoadedMap = mapName;
            return w;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearGameWorld() {
        purgeDirectory(gameWorldFile);
    }

    public void attemptWorldUnload(final World world) {
        if (world != null) {
            if (Bukkit.unloadWorld(world, false)) {
                new BukkitRunnable() {
                    private int tries = 0;

                    @Override
                    public void run() {
                        try {
                            parent.getLogger().info("Attempting world unload for " + world.getName());
                            File worldFolder = world.getWorldFolder();
                            purgeDirectory(worldFolder);
                            worldFolder.delete();
                        } catch (Exception e) {
                            if (tries < 3) {
                                tries++;
                                this.runTaskLaterAsynchronously(parent, (tries + 1) * 20L);
                            }
                        }
                    }
                }.runTaskLaterAsynchronously(parent, 2L);
            } else {
                parent.getLogger().info("Catastrophic failure in unloading!");
            }
        }
    }

    /*
    File Utilities, probably some wheel reinvention going on here
     */
    private static void copyFile(File source, File target) throws IOException {
        ArrayList<String> ignore = new ArrayList<>(Arrays.asList(new String[]{"uid.dat", "session.dat"}));

        if (!ignore.contains(source.getName())) {
            int length;

            if (source.isDirectory()) {
                if (!target.exists()) {
                    target.mkdir();
                }

                String[] in = source.list();
                int buffer = in.length;

                for (length = 0; length < buffer; ++length) {
                    String file = in[length];
                    File srcFile = new File(source, file);
                    File destFile = new File(target, file);

                    copyFile(srcFile, destFile);
                }
            } else {
                FileInputStream fileinputstream = new FileInputStream(source);
                FileOutputStream fileoutputstream = new FileOutputStream(target);
                byte[] abyte = new byte[1024];

                while ((length = fileinputstream.read(abyte)) > 0) {
                    fileoutputstream.write(abyte, 0, length);
                }

                fileinputstream.close();
                fileoutputstream.close();
            }
        }
    }

    @SuppressWarnings("all")
    private static void purgeDirectory(File dir) {
        if (dir != null && dir.exists()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) purgeDirectory(file);
                file.delete();
            }
            dir.delete();
        }
    }
}
