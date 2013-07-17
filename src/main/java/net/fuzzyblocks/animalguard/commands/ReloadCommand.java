package net.fuzzyblocks.animalguard.commands;

import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public class ReloadCommand extends AnimalGuardCommand {

    public ReloadCommand(AnimalGuard plugin) {
        super(plugin);
        this.setName("AnimalGuard: Reload");
        this.setCommandUsage("/ag reload");
        this.setArgRange(0, 0);
        this.addKey("animalguard reload");
        this.addKey("ag reload");
        this.setPermission("animalguard.reload", "Reload AnimalGuard configuration", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        // Set string on reload of config.
        plugin.validateConfig();
        sender.sendMessage(colour2 + ":: AnimalGuard configuration " + colour1 +"reloaded");
    }
}
