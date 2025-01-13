package io.phanisment.itemcaster.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.ItemCaster;

import java.util.logging.Logger;

public class Message {
	private static final String prefix = ItemCaster.getInst().getConfig("message").getString("prefix");
	private static final Logger logger = ItemCaster.getInst().getLogger();

	public static void sendConsole(String message) {
		Bukkit.getConsoleSender().sendMessage(Legacy.serializer(prefix + message));
	}

	public static void send(Player player, String message) {
		player.sendMessage(Legacy.serializer(prefix + message));
	}

	public static void send(CommandSender sender, String message) {
		sender.sendMessage(Legacy.serializer(prefix + message));
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

	public enum Type {
		WARNING, INFO, ERROR, SYSTEM, CONSOLE;
	}
}
