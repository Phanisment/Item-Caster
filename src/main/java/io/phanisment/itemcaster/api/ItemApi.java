package io.phanisment.itemcaster.api;

import io.phanisment.itemcaster.config.item.ItemAbilityInterface;
import io.phanisment.itemcaster.config.item.ItemEditInterface;
import io.phanisment.itemcaster.config.item.CasterItem;
import io.phanisment.itemcaster.config.ItemEditConfig;
import io.phanisment.itemcaster.config.ItemConfig;
import io.phanisment.itemcaster.ItemCaster;

public class ItemApi {
	private ItemApi() {
	}
	
	/**
	 * the value is must be like this
	 * <namespace>:<id>
	 */
	public static CasterItem getItem(String full_id) {
		return ItemConfig.getItem(full_id);
	}
	
	public static CasterItem getItem(String namespace, String id) {
		return ItemConfig.getItem(namespace, id);
	}
	
	public static CasterItem getItem(String[] full_id) {
		return ItemConfig.getItem(full_id);
	}
	
	public static ItemEditInterface getMenuIconItem(String id) {
		return ItemEditConfig.icon.get(id);
	}
	
	public static ItemEditInterface getMenuItem(int index) {
		return ItemEditConfig.items.get(index);
	}
	
	public static ItemAbilityInterface getAbilityMenuItem(int index) {
		return ItemEditConfig.ability_icons.get(index);
	}
}