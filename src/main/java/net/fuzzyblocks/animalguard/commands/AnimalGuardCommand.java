package net.fuzzyblocks.animalguard.commands;

import com.pneumaticraft.commandhandler.Command;
import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/** @author cedeel */
public abstract class AnimalGuardCommand extends Command {

    protected AnimalGuard plugin;
    protected ChatColor colour1 = ChatColor.GOLD;
    protected ChatColor colour2 = ChatColor.GRAY;

    public AnimalGuardCommand(AnimalGuard instance) {
        super(instance);
        plugin = instance;
    }

    @Override
    public abstract void runCommand(CommandSender sender, List<String> args);

    public String colourise(ChatColor colour, String value) {
        return colour + value + ChatColor.RESET;
    }

    public void showHelp(CommandSender sender) {
        sender.sendMessage(colour2 + "=== " + colour1 + getCommandName() + colour2 + " ===");
        sender.sendMessage(colour2 + "Usage: " + colour1 + getCommandUsage());
        sender.sendMessage(colour2 + getCommandDesc());
        sender.sendMessage((colour2 + "Permission: " + colour1 + this.getPermissionString()));
        String keys = "";
        String prefix = "";

        if (sender instanceof Player)
            prefix = "/";
        for (String key : this.getKeyStrings())
            keys += prefix + key + ", ";

        keys = keys.substring(0, keys.length() - 2);
        sender.sendMessage(colour2 + "Aliases: " + colour1 + keys);
        if (this.getCommandExamples().size() > 0) {
            sender.sendMessage(colour2 + "Examples: ");
            if (sender instanceof Player) {
                for (int i = 0; i < 4 && i < this.getCommandExamples().size(); i++) {
                    sender.sendMessage(this.getCommandExamples().get(i));
                }
            } else {
                for (String c : this.getCommandExamples()) {
                    sender.sendMessage(c);
                }
            }
        }
    }
}
