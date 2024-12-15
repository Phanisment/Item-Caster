package phanisment.itemcaster.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ItemCasterCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender ,Command command, String label, String[] args) {
		if (args[0] == "reload" || args[0] == "r") {
			return true;
		}
		if (args[0] == "give") {
			return true;
		}
		return false;
	}
}