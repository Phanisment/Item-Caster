package io.phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import io.phanisment.itemcaster.command.SubCommand;
import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.config.item.CasterItem;
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
			Message.send(sender, "<color:#677178>Usage: /ic give <player> <item> [amount]</color>");
			return;
		}
		ItemStack item = plugin.getItemConfig().getItem(args[2]).getItemStack();
		if (args.length == 4) item.setAmount(Integer.parseInt(args[3]));
		Player target = Bukkit.getPlayer(args[1]);
		if (item != null) {
			target.getInventory().addItem(item);
			Message.send(sender, "<color:#f7d340>" + args[2] + "</color> added to <color:#677178>" + target.getName() + "</color> inventory");
		} else {
			Message.send(sender, "<color:#d61c38>Invalid item id!</color>");
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 2) {
			List<Player> onlinePlayer = (List<Player>)Bukkit.getOnlinePlayers();
			onlinePlayer.forEach(player -> completions.add(player.getName()));
		} else if (args.length == 3) {
			Map<String, CasterItem> items = plugin.getItemConfig().getItemList();
			items.keySet().forEach(key -> completions.add(key));
		}
		return completions;
	}
}