package io.phanisment.itemcaster.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import io.phanisment.itemcaster.command.SubCommand;
import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Message;

public class ReloadCommand implements SubCommand {
	private ItemCaster plugin;
	
	public ReloadCommand(ItemCaster plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public String[] getName() {
		return new String[]{"reload", "r"};
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		Message.send(sender, "Reloading...");
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			try {
				plugin.reloadConfigs();
				Message.send(sender, "Reload Done!");
			} catch (Exception e) {
				Message.send(sender, "An error occurred while reloading the configuration.");
				e.printStackTrace();
			}
		});
	}
}