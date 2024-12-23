package phanisment.itemcaster.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import phanisment.itemcaster.command.*;
import phanisment.itemcaster.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCommand implements CommandExecutor, TabCompleter {
	private static final Map<String, SubCommand> commands = new HashMap<>();
	private final Main plugin;
	
	public BaseCommand(Main plugin) {
		this.plugin = plugin;
		
		commands.put("reload", new ReloadCommand(plugin));
		commands.put("get", new GetCommand(plugin));
	}
	
	@Override
	public boolean onCommand(CommandSender sender ,Command command, String label, String[] args) {
		if (sender.hasPermission("itemcaster.admin")) {
			if (args.length < 1) {
				sender.sendMessage("[ItemCaster] This is base command for this plugin.");
				return true;
			}
			SubCommand cmd = commands.get(args[0].toLowerCase());
			if (cmd != null) {
				cmd.execute(sender, args);
			} else {
				sender.sendMessage("[ItemCaster] Unknown argument!");
			}
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			for (String sub : commands.keySet()) {
				completions.add(sub);
			}
		} else if (args.length > 1) {
			SubCommand cmd = commands.get(args[0].toLowerCase());
			if (cmd != null) {
				completions = cmd.tabComplete(sender, args);
			}
		}
		return completions;
	}
}