package io.phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import io.phanisment.itemcaster.command.SubCommand;
import io.phanisment.itemcaster.config.ItemConfig;
import io.phanisment.itemcaster.config.item.CasterItem;
import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Message;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class GetCommand implements SubCommand {
	private final ItemCaster plugin;
	
	public GetCommand(ItemCaster plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public String[] getName() {
		return new String[]{"get", "g"};
	}
	
	@Override
	public boolean allowConsole() {
		return false;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (args.length < 2) {
				Message.send(sender, "<color:#677178>Usage: /ic get <item id> [amount]</color>");
				return;
			}
			ItemStack item = plugin.getItemConfig().getItem(args[1]).getItemStack();
			Player player = (Player)sender;
			if (item != null) {
				if (args.length == 3) item.setAmount(Integer.parseInt(args[2]));
				player.getInventory().addItem(item);
				Message.send(sender, "<color:#f7d340>" + args[1] + "</color> added to your inventory");
			} else {
				Message.send(sender, "<color:#d61c38>Invalid item id!</color>");
			}
		}
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 2) {
			Map<String, CasterItem> items = plugin.getItemConfig().getItemList();
			items.keySet().forEach(key -> completions.add(key));
		} else if (args.length == 3) {
			completions.add("2");
			completions.add("8");
			completions.add("16");
			completions.add("64");
		}
		return completions;
	}
}