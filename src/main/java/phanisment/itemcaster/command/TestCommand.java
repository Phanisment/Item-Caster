package phanisment.itemcaster.command;

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;

import io.lumine.mythic.bukkit.MythicBukkit;

import phanisment.itemcaster.Main;
import phanisment.itemcaster.command.SubCommand;
import phanisment.itemcaster.skills.SkillActivator.Activator;

import java.util.ArrayList;
import java.util.List;

public class TestCommand implements SubCommand {
	private final Main plugin;
	private final FileConfiguration config;

	public TestCommand(Main plugin) {
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (args.length < 3) {
				sender.sendMessage("[ItemCaster] Usage: /ic test <skill> <activator> [-c <cooldown>] [-p <power>]");
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
						player.sendMessage("[ItemCaster] Invalid cooldown value.");
						return;
					}
				} else if (args[i].equalsIgnoreCase("-p") && i + 1 < args.length) {
					try {
						power = Float.parseFloat(args[i + 1]);
						i++;
					} catch (NumberFormatException e) {
						player.sendMessage("[ItemCaster] Invalid power value.");
						return;
					}
				}
			}
	
			Material material = Material.valueOf(config.getString("test.item", "STONE").toUpperCase());
			ItemStack item = new ItemStack(material);
			ItemMeta meta = item.getItemMeta();
	
			if (meta != null) {
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("test.displayname", "&rTest Item")));
				if (config.contains("test.modeldata")) {
					meta.setCustomModelData(config.getInt("test.modeldata"));
				}
				item.setItemMeta(meta);
			}
	
			NBTItem nbtItem = new NBTItem(item);
			NBTCompound artifactCompound = nbtItem.addCompound("Artifact");
			artifactCompound.setString("skill", skill);
			artifactCompound.setString("activator", activator);
			if (cooldown > 0) artifactCompound.setInteger("cooldown", cooldown);
			if (power > 0) artifactCompound.setFloat("power", power);
			player.getInventory().addItem(nbtItem.getItem());
			player.sendMessage("[ItemCaster] Test item created with skill: " + skill);
		} else {
			sender.sendMessage("[ItemCaster] You cannot run this command from the console!");
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		if (args.length == 2) {
			return (List)StringUtil.copyPartialMatches((String)args[1], MythicBukkit.inst().getSkillManager().getSkillNames(), new ArrayList());
		} else if (args.length == 3) {
			return getActivatorList();
		} else if (args.length > 3) {
			if (args[args.length - 1].equalsIgnoreCase("-c")) {
				return List.of("5", "10", "20");
			} else if (args[args.length - 1].equalsIgnoreCase("-p")) {
				return List.of("1", "2", "5");
			} else if (args[args.length - 2].equalsIgnoreCase("-c")) {
				return List.of("5", "10", "20");
			} else if (args[args.length - 2].equalsIgnoreCase("-p")) {
				return List.of("1", "2", "5");
			}
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
