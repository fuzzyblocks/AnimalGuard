package net.fuzzyblocks.animalguard;

import com.pneumaticraft.commandhandler.CommandHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.fuzzyblocks.animalguard.commands.BaseCommand;
import net.fuzzyblocks.animalguard.commands.ReloadCommand;
import net.fuzzyblocks.animalguard.commands.VersionCommand;
import net.fuzzyblocks.animalguard.listeners.DamageListener;
import net.fuzzyblocks.animalguard.listeners.InteractListener;
import net.fuzzyblocks.animalguard.util.Updater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimalGuard extends JavaPlugin {

    private CommandHandler commandHandler;
    public List<EntityType> protectedFromPlayer = new ArrayList<>();
    public List<EntityType> protectedFromMonster = new ArrayList<>();

    //Enable stuff
    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();

        // Check for WorldGuard
        getWorldGuardPlugin();

        // Config Setup
        setupConfig();

        // Enable plugin metrics
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.enable();
        } catch (IOException e) {
            getLogger().info("An error occurred while posting results to the Metrics.");
            getLogger().warning(e.getLocalizedMessage());
        }

        // Check for updates to plugin
        updatePlugin();
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> allArgs = new ArrayList<>();
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
        pm.registerEvents(new DamageListener(this), this);
        if (!this.getConfig().getBoolean("allow-sheep-shearing"))
            pm.registerEvents(new InteractListener(this), this);
    }

    private void updatePlugin() {
        Updater updater;
        if (this.getConfig().getBoolean("auto-download-updates"))
            updater = new Updater(this, "animalguard", this.getFile(), Updater.UpdateType.DEFAULT, false);
        else if (this.getConfig().getBoolean("notify-outdated")) {
            updater = new Updater(this, "animalguard", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
            if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE)
                this.getLogger().info("There is an update availible on BukkitDev");
        }
    }
}
