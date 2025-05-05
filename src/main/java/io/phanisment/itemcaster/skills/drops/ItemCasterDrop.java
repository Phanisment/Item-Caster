package io.phanisment.itemcaster.skills.drops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.IItemDrop;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;

import io.phanisment.itemcaster.config.ItemConfig;
import io.phanisment.itemcaster.ItemCaster;

public class ItemCasterDrop implements IItemDrop {
	private String type;
	
	public ItemCasterDrop(MythicLineConfig config, String argument) {
		type = config.getString(new String[]{"type", "t"}, argument);
	}

	@Override
	public AbstractItemStack getDrop(DropMetadata data, double amount) {
		return new BukkitItemStack(ItemCaster.getInst().getItemConfig().getItem(type).getItemStack());
	}
}