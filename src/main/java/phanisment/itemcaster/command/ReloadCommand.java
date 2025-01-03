package phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;

import phanisment.itemcaster.command.SubCommand;
import phanisment.itemcaster.Main;

public class ReloadCommand implements SubCommand {
	private Main plugin;
	
	public ReloadCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		plugin.reloadConfig();
		plugin.itemConfig.loadItems();
		sender.sendMessage("[ItemCaster] Reload Done!");
	}
}