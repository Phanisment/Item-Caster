package io.phanisment.itemcaster.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;

import io.phanisment.itemcaster.command.*;
import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCommand implements CommandExecutor, TabCompleter {
	private static final Map<String, SubCommand> commands = new HashMap<>();
	private final ItemCaster plugin;
	
	public BaseCommand(ItemCaster plugin) {
		this.plugin = plugin;
		
		register(new GetCommand(plugin));
		register(new TestCommand(plugin));
		register(new ReloadCommand(plugin));
		register(new GiveCommand(plugin));
	}
	
	private void register(SubCommand cmd) {
		for (String name : cmd.getName()) {
			commands.put(name.toLowerCase(), cmd);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			Message.send(sender, "This is base command for this plugin.");
			return true;
		}
		
		SubCommand cmd = commands.get(args[0].toLowerCase());
		if (cmd != null) {
			if (!cmd.allowConsole() && sender instanceof ConsoleCommandSender) {
				Message.send(sender, "You can not run this command on console!");
				return true;
			}
			if (cmd.allowMember() && sender.hasPermission("itemcaster.admin")) {
				Message.send(sender, "Sorry, you don't have permission this comamnd.");
				return true;
			}
			cmd.execute(sender, args);
		} else {
			Message.send(sender, "Unknown argument!");
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
		} else if (args.length >= 2) {
			SubCommand cmd = commands.get(args[0].toLowerCase());
			if (cmd != null) {
				completions = cmd.tabComplete(sender, args);
			}
		}
		return completions;
	}
}