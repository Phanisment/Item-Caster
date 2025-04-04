package io.phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;

import dev.lone.itemsadder.api.CustomStack;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.command.SubCommand;
import io.phanisment.itemcaster.skills.SkillActivator.Activator;
import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.util.Message;
import io.phanisment.itemcaster.config.item.CasterItem;

import java.util.ArrayList;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class TestCommand implements SubCommand {
	private final ItemCaster plugin;
	
	public TestCommand(ItemCaster plugin) {
		this.plugin = plugin;
	}

	@Override
	public String[] getName() {
		return new String[]{"test", "t"};
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (args.length < 3) {
				Message.send(sender, "<color:#677178>Usage: /ic test <skill> <activator> [-c <cooldown>] [-p <power>]</color>");
				return;
			}
			Player player = (Player) sender;
			String skill = args[1];
			String activator = args[2];
			int cooldown = 0;
			float power = 0;
			Map<String, Object> variables = new HashMap<>();
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
				} else if (args[i].equalsIgnoreCase("-v") && i + 1 < args.length) {
					String[] keyValue = args[i + 1].split(":");
					if (keyValue.length == 2) {
						variables.put(keyValue[0], (Object)keyValue[1]);
						i++;
					} else {
						Message.send(sender, "Invalid variable format. Use key:value.");
						return;
					}
				}
			}
			
			String type = plugin.getConfig().getString("item_test.type", "STONE");
			try {
				ItemStack item = CasterItem.itemType(type);
				ItemMeta meta = item.getItemMeta();
				if (meta != null) {
					meta.setDisplayName(Legacy.serializer("<reset>" + plugin.getConfig().getString("item_test.display_name")));
					if (plugin.getConfig().contains("item_test.model_data")) meta.setCustomModelData(plugin.getConfig().getInt("item_test.modeldata"));
					item.setItemMeta(meta);
				}
				NBTItem nbtItem = new NBTItem(item);
				NBTCompound nbt = nbtItem.getOrCreateCompound("ItemCaster");
				nbt.setBoolean("test_item", true);
				NBTCompoundList artifactList = nbt.getCompoundList("Artifact");
				NBTCompound skillCompound = artifactList.addCompound();
				skillCompound.setString("skill", skill);
				skillCompound.setString("activator", activator);
				if (cooldown > 0) skillCompound.setInteger("cooldown", cooldown);
				if (power > 0) skillCompound.setFloat("power", power);
				NBTCompound variableCompound = skillCompound.getOrCreateCompound("variable");
				for (Map.Entry<String, Object> entry : variables.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					if (value instanceof String) {
						variableCompound.setString(key, (String) value);
					} else if (value instanceof Float) {
						variableCompound.setFloat(key, (Float) value);
					} else if (value instanceof Integer) {
						variableCompound.setInteger(key, (Integer) value);
					} else {
						Message.send(player, "Unsupported variable type key in: " + key);
					}
				}
				player.getInventory().addItem(nbtItem.getItem());
				Message.send(player, "Test item created with skill: " + skill);
			} catch (IllegalArgumentException e) {
				plugin.getLogger().warning("Material " + type + " is invalid!");
			}
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 2) {
			return (List)StringUtil.copyPartialMatches((String)args[1], MythicBukkit.inst().getSkillManager().getSkillNames(), new ArrayList());
		} else if (args.length == 3) {
			for (Activator activator : Activator.values()) {
				completions.add(activator.name().toLowerCase());
			}
		} else if (args.length > 3) {
			completions.add("-v <key>:<value>");
			completions.add("-c <number>");
			completions.add("-p <number>");
		}
		return completions;
	}
}