package net.fuzzyblocks.animalguard.commands;

import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public class BaseCommand extends AnimalGuardCommand {
    public BaseCommand(AnimalGuard instance) {
        super(instance);
        setName("AnimalGuard: Main");
        setCommandUsage("/ag");
        // TODO: Add examples
        addCommandExample(colour2 + "/ag" + colour1 + " version ");
        setArgRange(0, 1);
        addKey("animalguard");
        addKey("ag");
        setPermission("animalguard", "Welcome to AnimalGuard's help system! Try a command to see usage examples.", PermissionDefault.TRUE);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        showHelp(sender);
    }
}
