package io.phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;

import io.lumine.mythic.bukkit.MythicBukkit;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.command.SubCommand;
import io.phanisment.itemcaster.skills.SkillActivator.Activator;
import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.util.Message;

import java.util.ArrayList;
import java.util.List;

public class TestCommand implements SubCommand {
	private final ItemCaster plugin;
	private final FileConfiguration config;
	
	public TestCommand(ItemCaster plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig("config");
	}

	@Override
	public String[] getName() {
		return new String[]{"test", "t"};
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (args.length < 3) {
				Message.send(sender, "Usage: /ic test <skill> <activator> [-c <cooldown>] [-p <power>]");
				return;
			}
			Player player = (Player) sender;
			String skill = args[1];
			String activator = args[2];
			int cooldown = 0;
			float power = 0;
			for (int i = 3; i < args.length; i++) {
				if (args[i].equalsIgnoreCase("-c") && i + 1 < args.length) {
					try {
						cooldown = Integer.parseInt(args[i + 1]);
						i++;
					} catch (NumberFormatException e) {
						Message.send(sender, "Invalid cooldown value.");
						return;
					}
				} else if (args[i].equalsIgnoreCase("-p") && i + 1 < args.length) {
					try {
						power = Float.parseFloat(args[i + 1]);
						i++;
					} catch (NumberFormatException e) {
						Message.send(sender, "Invalid power value.");
						return;
					}
				}
			}
			Material material = Material.valueOf(config.getString("item_test.item", "STONE").toUpperCase());
			ItemStack item = new ItemStack(material);
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				meta.setDisplayName(Legacy.serializer("<reset>" + config.getString("item_test.displayname")));
				if (config.contains("item_test.modeldata")) {
					meta.setCustomModelData(config.getInt("item_test.modeldata"));
				}
				item.setItemMeta(meta);
			}
			NBTItem nbtItem = new NBTItem(item);
			NBTCompoundList artifactList = nbtItem.getCompoundList("Artifact");
			NBTCompound skillCompound = artifactList.addCompound();
			skillCompound.setString("skill", skill);
			skillCompound.setString("activator", activator);
			if (cooldown > 0) skillCompound.setInteger("cooldown", cooldown);
			if (power > 0) skillCompound.setFloat("power", power);
			player.getInventory().addItem(nbtItem.getItem());
			Message.send(player, "Test item created with skill: " + skill);
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		if (args.length == 2) {
			return (List)StringUtil.copyPartialMatches((String)args[1], MythicBukkit.inst().getSkillManager().getSkillNames(), new ArrayList());
		} else if (args.length == 3) {
			return getActivatorList();
		}
		return new ArrayList<>();
	}
	
	private List<String> getActivatorList() {
		List<String> activators = new ArrayList<>();
		for (Activator activator : Activator.values()) {
			activators.add(activator.name().toLowerCase());
		}
		return activators;
	}
}