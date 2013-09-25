package net.fuzzyblocks.animalguard;

import com.pneumaticraft.commandhandler.CommandHandler;
import net.fuzzyblocks.animalguard.commands.BaseCommand;
import net.fuzzyblocks.animalguard.commands.ReloadCommand;
import net.fuzzyblocks.animalguard.commands.VersionCommand;
import net.fuzzyblocks.animalguard.listeners.*;
import net.fuzzyblocks.animalguard.util.MessagesManager;
import net.fuzzyblocks.animalguard.util.Updater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AnimalGuard extends JavaPlugin {

    private CommandHandler commandHandler;
    private static List<EntityType> protectedFromPlayer = new ArrayList<>();
    private static List<EntityType> protectedFromMonster = new ArrayList<>();
    private Map<String, String> messages;
    private boolean cowMilking, mobLeashing, sheepDying, sheepShearing, mooshroomShearing, tameablePvp, vehicleTheft;

    //Enable stuff
    @Override
    public void onEnable() {
        registerCommands();

        // Config Setup
        setupConfig();

        // Fill the lists
        for (String entity : this.getConfig().getStringList("protect-from-player"))
            protectedFromPlayer.add(EntityType.fromName(entity));

        for (String entity : this.getConfig().getStringList("protect-from-monsters"))
            protectedFromMonster.add(EntityType.fromName(entity));

        registerEvents();

        // Enable plugin metrics
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().info("An error occurred while posting results to the Metrics.");
            getLogger().warning(e.getLocalizedMessage());
        }

        // Check for updates to plugin
        updatePlugin();
    }

    public static boolean isProtectedFromPlayer(EntityType type) {
        return protectedFromPlayer.contains(type);
    }

    public static boolean isProtectedFromMonster(EntityType type) {
        return protectedFromMonster.contains(type);
    }

    public String getMessage(String messageId) {
        if (messages == null)
            messages = new MessagesManager(this).getMessages();
        return messages.get(messageId);
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

        cowMilking = getConfig().getBoolean("allow-cow-milking", true);
        mobLeashing = getConfig().getBoolean("allow-mob-leashing", false);
        sheepDying = getConfig().getBoolean("allow-sheep-dye", false);
        sheepShearing = getConfig().getBoolean("allow-sheep-shearing", false);
        mooshroomShearing = getConfig().getBoolean("allow-mooshroom-shearing", false);
        tameablePvp = getConfig().getBoolean("allow-pvp-tameable", true);
        vehicleTheft = getConfig().getBoolean("allow-vehicle-riding", false);
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
        pm.registerEvents(new DamageListener(this, tameablePvp), this);
        if (!sheepShearing)
            pm.registerEvents(new SheepShearListener(this), this);
        if (!sheepDying)
            pm.registerEvents(new SheepDyeListener(this), this);
        if (!cowMilking)
            pm.registerEvents(new CowMilkListener(this), this);
        if (!mobLeashing)
            pm.registerEvents(new MobLeashListener(this), this);
        if (!mooshroomShearing)
            pm.registerEvents(new MooshroomShearListener(this), this);
        if (!vehicleTheft)
            pm.registerEvents(new VehicleListener(this), this);
    }

    private void updatePlugin() {
        Updater updater;
        if (this.getConfig().getBoolean("auto-download-updates"))
            updater = new Updater(this, this.getName().toLowerCase(), this.getFile(), Updater.UpdateType.DEFAULT, false);
        else if (this.getConfig().getBoolean("notify-outdated")) {
            updater = new Updater(this, this.getName().toLowerCase(), this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
            if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE)
                this.getLogger().info("There is an update availible on BukkitDev");
        }
    }
}
