package io.phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import io.phanisment.itemcaster.command.SubCommand;
import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Message;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class GiveCommand implements SubCommand {
	private final ItemCaster plugin;
	
	public GiveCommand(ItemCaster plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public String[] getName() {
		return new String[]{"give"};
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 3) {
			Message.send(sender, "Usage: /ic give <player name> <item id> <amount>");
			return;
		}
		ItemStack item = plugin.getItemConfig().getItem(args[2]);
		item.setAmount(Integer.parseInt(args[3]));
		Player target = Bukkit.getPlayer(args[1]);
		if (item != null) {
			target.getInventory().addItem(item);
			Message.send(sender, "Give item (" + args[2] + ") to [" + target.getName() + "]");
		} else {
			Message.send(sender, "Invalid item id!");
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 2) {
			List<Player> onlinePlayer = (List<Player>)Bukkit.getOnlinePlayers();
			onlinePlayer.forEach(player -> completions.add(player.getName()));
		} else if (args.length == 3) {
			Map<String, ItemStack> items = plugin.getItemConfig().getItemList();
			items.keySet().forEach(key -> completions.add(key));
		}
		return completions;
	}
}