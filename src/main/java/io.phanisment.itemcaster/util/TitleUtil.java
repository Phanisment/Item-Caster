package io.phanisment.itemcaster.util;

import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class TitleUtil {
	private TitleUtil() {
	}
	
	public static void subtitle(Player player, String content) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(content));
	}
}