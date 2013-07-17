package net.fuzzyblocks.animalguard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
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
import java.util.List;
import net.fuzzyblocks.animalguard.util.Updater;

public class AnimalGuard extends JavaPlugin {

    String success = ChatColor.GREEN + "[AnimalGuard]: ";
    String fail = ChatColor.RED + "[AnimalGuard]: ";
    public final DamageListener dl = new DamageListener(this);
    public final ShearListener shear = new ShearListener(this);

    //Enable stuff
    @Override
    public void onEnable() {
        //event registration
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(dl, this);
        pm.registerEvents(shear, this);

        //Check for WorldGuard
        getWorldGuardPlugin();
        //CFG Setup
        setupConfig();

        //Check config for any errors.
        validateConfig();
        collectStats();

        //Check for updates to plugin
        Updater updater;
        if (this.getConfig().getBoolean("auto-download-updates"))
            updater = new Updater(this, "animalguard", this.getFile(), Updater.UpdateType.DEFAULT, false);
        else
            updater = new Updater(this, "animalguard", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
    }

    public void collectStats() {
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            this.getLogger().warning("Could not submit stats");
        }

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

    private void validateConfig() {
        if (this.getConfig().getInt("notify-interval") > 20) {
            this.getLogger().warning("Notify interval greater then 20");
            this.getConfig().set("notify-interval", 20);
            this.getLogger().info("Notify interval has been set to 20");
            this.saveConfig();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("animalguard")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.YELLOW + "+++++++++AnimalGuard++++++++++");
                sender.sendMessage(ChatColor.GREEN + "+ A Animal Friendly Plugin!");
                sender.sendMessage(ChatColor.RED + "+ Version: " + getDescription().getVersion());
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "+ Developer: " + getDescription().getAuthors());
                sender.sendMessage(ChatColor.AQUA + "http://www.dev.bukkit.org/server-mods/animalguard");
                sender.sendMessage(ChatColor.YELLOW + "+++++++++++++++++++++++++++++");
                return true;
            }
            if (args[0].equalsIgnoreCase("-reload") && sender.isOp() || sender.hasPermission("animalguard.admin")) {
                //reload config stuff.
                this.reloadConfig();
                //Set string on reload of config.
                this.validateConfig();
                sender.sendMessage(success + "Configuration Reloaded!");
                return true;
            }
            if (args[0].equalsIgnoreCase("-list") && args[1].equalsIgnoreCase("player") && sender.isOp() || sender.hasPermission("animalguard.list")) {
                List<String> pfp = getConfig().getStringList("protect-from-player");
                sender.sendMessage(success + "The following are protected from players");
                for (String i : pfp)
                    sender.sendMessage(i);
            }
            if (args[0].equalsIgnoreCase("-list") && args[1].equalsIgnoreCase("mobs") && sender.isOp() || sender.hasPermission("animalguard.list")) {
                List<String> pfp = getConfig().getStringList("protect-from-monsters");
                sender.sendMessage(success + "The following are protected from mobs");
                for (String i : pfp)
                    sender.sendMessage(i);
            } else {
                sender.sendMessage(fail + "You lack the necessary permissions to perform this action.");
                return true;
            }
        }
        return false;
    }
}
