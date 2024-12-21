package phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import phanisment.itemcaster.command.SubCommand;
import phanisment.itemcaster.config.ItemConfig;
import phanisment.itemcaster.Main;

import java.util.*;

public class GetCommand implements SubCommand {
	private final Main plugin;
	
	public GetCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (args.length < 2) {
				sender.sendMessage("[ItemCaster] Usage: /ic get <item id>");
				return;
			}
			ItemStack item = plugin.itemConfig.getItem(args[1]);
			Player player = (Player)sender;
			if (item != null) {
				player.getInventory().addItem(item);
				sender.sendMessage("[ItemCaster] Geting item (" + args[1] + ")!");
			} else {
				sender.sendMessage("[ItemCaster] Invalid item id!");
			}
		} else {
			sender.sendMessage("[ItemCaster] You can not run this command on console!");
		}
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		List<String> completions = new ArrayList<>();
		
		if (args.length == 1) {
			Map<String, ItemStack> items = plugin.itemConfig.getItemList();
			items.keySet().forEach(key -> {
				completions.add(key);
			});
		}
		return completions;
	}
}