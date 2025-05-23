package io.phanisment.itemcaster.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.ItemCaster;

import java.util.logging.Logger;

public class Message {
	private static final Logger logger = ItemCaster.getInst().getLogger();

	public static String get(String path, String def) {
		if (!ItemCaster.getInst().hasPapi) {
			return Legacy.serializer(ItemCaster.getInst().getConfig().getString(path, def));
		}
		return PlaceholderAPI.setPlaceholders(null, Legacy.serializer(ItemCaster.getInst().getConfig().getString(path, def)));
	}

	public static void send(String message) {
		Bukkit.getConsoleSender().sendMessage(Legacy.serializer(ItemCaster.getInst().getConfig().getString("prefix") + message));
	}

	public static void send(Player player, String message) {
		player.sendMessage(Legacy.serializer(ItemCaster.getInst().getConfig().getString("prefix") + message));
	}
	
	public static void sendTo(Player player, String message) {
		player.sendMessage(Legacy.serializer(message));
	}

	public static void debug(String message) {
		if (ItemCaster.getInst().getConfig().getBoolean("debug", false)) Bukkit.getConsoleSender().sendMessage(Legacy.serializer(message));
	}


	public static void send(CommandSender sender, String message) {
		sender.sendMessage(Legacy.serializer(ItemCaster.getInst().getConfig().getString("prefix") + message));
	}

	public static void send(Type type, String message) {
		switch (type) {
			case WARNING:
				logger.warning(message);
				break;
			case INFO:
				logger.info(message);
				break;
			case ERROR:
				logger.severe(message);
				break;
			case SYSTEM:
				System.out.println(message);
				break;
			case CONSOLE:
				Bukkit.getConsoleSender().sendMessage(Legacy.serializer(message));
				break;
		}
	}

	public static enum Type {
		WARNING, INFO, ERROR, SYSTEM, CONSOLE;
	}
}