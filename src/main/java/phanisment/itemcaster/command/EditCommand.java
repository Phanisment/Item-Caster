package phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;

import phanisment.itemcaster.command.SubCommand;
import phanisment.itemcaster.Main;

import java.io.File;

public class EditCommand implements SubCommand {
	private Main plugin;
	
	public EditCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage("[ItemCaster] In development.");
	}
}