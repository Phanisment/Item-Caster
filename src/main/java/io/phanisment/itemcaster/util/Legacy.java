package io.phanisment.itemcaster.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Legacy {
	public static final LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();
	public static final MiniMessage mm = MiniMessage.miniMessage();
	public static String serializer(String text) {
		return serializer.serialize(mm.deserialize(text));
	}
	public static String serializer(Component component) {
		return serializer.serialize(component);
	}
}