package net.fuzzyblocks.animalguard;

import com.pneumaticraft.commandhandler.CommandHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.fuzzyblocks.animalguard.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fuzzyblocks.animalguard.util.Updater;

public class AnimalGuard extends JavaPlugin {

    private CommandHandler commandHandler;

    public final DamageListener dl = new DamageListener(this);
    public final ShearListener shear = new ShearListener(this);

    //Enable stuff
    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();

        // Check for WorldGuard
        getWorldGuardPlugin();

        // Config Setup
        setupConfig();

        // Check config for any errors.
        validateConfig();

        // Enable plugin metrics
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.enable();
        } catch (IOException e) {
            getLogger().info("An error occurred while posting results to the Metrics.");
            getLogger().warning(e.getLocalizedMessage());
        }

        //Check for updates to plugin
        Updater updater;
        if (this.getConfig().getBoolean("auto-download-updates"))
            updater = new Updater(this, "animalguard", this.getFile(), Updater.UpdateType.DEFAULT, false);
        else
            updater = new Updater(this, "animalguard", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
    }

    //WorldGuard Check
    public WorldGuardPlugin getWorldGuardPlugin() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        PluginManager pm = this.getServer().getPluginManager();
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            //warn WG was not found.
            this.getLogger().warning("WorldGuard plugin not found");
            this.getLogger().warning("AnimalGuard Disabled!");
            //Disable the plugin.
            pm.disablePlugin(this);
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    //Configuration setup
    private void setupConfig() {
        final FileConfiguration cfg = getConfig();
        FileConfigurationOptions cfgOptions = cfg.options();
        this.saveDefaultConfig();
        cfgOptions.copyDefaults(true);
        cfgOptions.header("Default Config for AnimalGuard");
        cfgOptions.copyHeader(true);
        saveConfig();
    }

    public void validateConfig() {
        reloadConfig();
        if (this.getConfig().getInt("notify-interval") > 20) {
            this.getLogger().warning("Notify interval greater then 20");
            this.getConfig().set("notify-interval", 20);
            this.getLogger().info("Notify interval has been set to 20");
            this.saveConfig();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> allArgs = new ArrayList<String>();
        allArgs.addAll(Arrays.asList(args));
        allArgs.add(0, label);
        return commandHandler.locateAndRunCommand(sender, allArgs);
    }

    private void registerCommands() {
        PermissionsModule pm = new PermissionsModule();
        commandHandler = new CommandHandler(this, pm);

        commandHandler.registerCommand(new BaseCommand(this));
        commandHandler.registerCommand(new VersionCommand(this));
        commandHandler.registerCommand(new ReloadCommand(this));
    }

    private void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(dl, this);
        pm.registerEvents(shear, this);
    }
}
