package io.phanisment.itemcaster.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import io.phanisment.itemcaster.command.SubCommand;
import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Message;

public class MenuCommand implements SubCommand {
	private ItemCaster plugin;
	
	public MenuCommand(ItemCaster plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public String[] getName() {
		return new String[]{"menu", "m"};
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
	}
}