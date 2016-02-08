package es.nkmem.da.spaghetti.handlers;

import es.nkmem.da.spaghetti.SpaghettiPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class WorldHandler {
    public static String worldName;

    private static void copyFile(File source, File target) throws IOException {
        ArrayList<String> ignore = new ArrayList<>(Arrays.asList(new String[]{"uid.dat", "session.dat"}));

        if (!ignore.contains(source.getName())) {
            int length;

            if (source.isDirectory()) {
                if (!target.exists()) {
                    target.mkdir();
                }

                String[] in = source.list();
                String[] out = in;
                int buffer = in.length;

                for (length = 0; length < buffer; ++length) {
                    String file = out[length];
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

    private static void purgeDirectory(File dir) {
        if (dir == null) return;
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) purgeDirectory(file);
            file.delete();
        }
    }

    public static World loadMap(String mapName, World.Environment environment) {
        worldName = mapName;
        SpaghettiPlugin spaghetti = SpaghettiPlugin.getInstance();
        File dataFolder = spaghetti.getMapsDirectory();
        File mapFile = new File(dataFolder, spaghetti.getGameWorldName());
        try {
            try {
                purgeDirectory(mapFile);
                mapFile.delete();
            } catch (Throwable ignored) {

            }
            WorldHandler.copyFile(new File(dataFolder, mapName), mapFile);
            WorldCreator wc = new WorldCreator(mapFile.getPath());
            wc.environment(environment);
            return Bukkit.createWorld(wc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clearMapsDir() {
        File mapsFolder = SpaghettiPlugin.getInstance().getMapsDirectory();
        purgeDirectory(mapsFolder);
    }

    public static void attemptWorldUnload(final World gameWorld) {
        final SpaghettiPlugin spaghetti = SpaghettiPlugin.getInstance();
        if (gameWorld != null) {
            if (Bukkit.unloadWorld(gameWorld, false)) {
                new BukkitRunnable() {
                    private int tries = 0;

                    @Override
                    public void run() {
                        try {
                            SpaghettiPlugin.getInstance().getLogger().info("Attempting world unload for " + worldName);
                            File worldFolder = gameWorld.getWorldFolder();
                            purgeDirectory(worldFolder);
                            worldFolder.delete();
                            worldName = null;
                        } catch (Exception e) {
                            if (tries < 3) {
                                tries++;
                                this.runTaskLaterAsynchronously(spaghetti, (tries + 1) * 20L);
                            }
                        }
                    }
                }.runTaskLaterAsynchronously(spaghetti, 2L);
            }
        }
    }
}
