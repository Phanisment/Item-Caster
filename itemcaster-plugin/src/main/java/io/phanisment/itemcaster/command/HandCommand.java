package io.phanisment.itemcaster.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.phanisment.itemcaster.skills.HandActivator;
import io.phanisment.itemcaster.command.SubCommand;
import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HandCommand implements SubCommand {
	private final ItemCaster plugin;

	public HandCommand(ItemCaster plugin) {
		this.plugin = plugin;
	}

	@Override
	public String[] getName() {
		return new String[]{"hand", "h"};
	}

	@Override
	public boolean allowConsole() {
		return false;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player)sender;
		if (args.length < 2) {
			Message.send(player, "<color:#677178>Usage: /ic hand <set/remove/removeAll> [skill] [activator]</color>");
			return;
		}

		String action = args[1].toLowerCase();
		switch (action) {
			case "set":
				if (args.length < 4) {
					Message.send(player, "<color:#677178>Usage: /ic hand set <skill> <activator></color>");
					return;
				}
				String skill = args[2];
				String activatorName = args[3].toUpperCase();
				HandActivator.Activator activator;
				try {
					activator = HandActivator.Activator.valueOf(activatorName);
				} catch (IllegalArgumentException e) {
					Message.send(player, "<color:#d61c38>Invalid activator! Valid options: RIGHT_CLICK, LEFT_CLICK, SNEAK, TICK</color>");
					return;
				}
				HandActivator.set(player, activator, skill);
				Message.send(player, "Skill <color:#3ccf79>" + skill + "</color> has been set with activator <color:#3ccf79>" + activator.name() + "</color>.");
				break;
			case "remove":
				if (args.length < 3) {
					Message.send(player, "<color:#677178>Usage: /ic hand remove <skill></color>");
					return;
				}
				String skillToRemove = args[2];
				HandActivator.remove(player, skillToRemove);
				Message.send(player, "Skill <color:#d61c38>" + skillToRemove + "</color> has been removed.");
				break;
			case "removeall":
				HandActivator.removeAll(player);
				Message.send(player, "<color:#d61c38>All skills have been removed.</color>");
				break;
			default:
				Message.send(player, "<color:#d61c38>Invalid action! Valid options: set, remove, removeAll</color>");
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		List<String> completions = new ArrayList<>();

		if (args.length == 2) {
			completions.add("set");
			completions.add("remove");
			completions.add("removeAll");
		} else if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
			StringUtil.copyPartialMatches(args[2], MythicBukkit.inst().getSkillManager().getSkillNames(), completions);
		} else if (args.length == 3 && args[1].equalsIgnoreCase("remove")) {
			Player player = (Player) sender;
			Map<String, HandActivator.Activator> skills = HandActivator.getSkills(player);
			completions.addAll(skills.keySet());
		} else if (args.length == 4 && args[1].equalsIgnoreCase("set")) {
			for (HandActivator.Activator activator : HandActivator.Activator.values()) {
				completions.add(activator.name().toLowerCase());
			}
		}
		return completions;
	}
}
