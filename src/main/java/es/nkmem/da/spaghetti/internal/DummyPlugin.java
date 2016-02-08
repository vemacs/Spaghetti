package es.nkmem.da.spaghetti.internal;

import com.avaje.ebean.EbeanServer;
import lombok.AllArgsConstructor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

@AllArgsConstructor
public class DummyPlugin implements Plugin {
    private Plugin parent;
    private String name;

    @Override
    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public File getDataFolder() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EbeanServer getDatabase() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String arg0, String arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return this.parent.getDescription();
    }

    @Override
    public Logger getLogger() {
        return this.parent.getLogger();
    }

    @Override
    public PluginLoader getPluginLoader() {
        return this.parent.getPluginLoader();
    }

    @Override
    public InputStream getResource(String arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Server getServer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isNaggable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onDisable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEnable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onLoad() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reloadConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveDefaultConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveResource(String arg0, boolean arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNaggable(boolean arg0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return parent.getDescription().getName() + "{" + name + "@" + System.identityHashCode(this) + "}";
    }
}