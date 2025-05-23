package io.phanisment.itemcaster.config.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.ConfigurationSection;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;

import io.lumine.mythic.api.skills.Skill;

import io.phanisment.itemcaster.ItemCaster;
import io.phanisment.itemcaster.util.Message;
import io.phanisment.itemcaster.util.Legacy;
import io.phanisment.itemcaster.config.item.CasterItem;

public class IconItem implements Cloneable, ItemCasterItems {
	private ConfigurationSection section;
	public ItemStack item;
	public Skill skill;
	public Pack pack;
	
	public String icon;
	/*
	public String format;
	public int model_data;
	public List<String> lore;
	public List<String> description;
	*/
	
	private ItemCaster getPl() {
		return ItemCaster.getInst();
	}
	
	public IconItem(Pack pack, ConfigurationSection section, Skill skill) {
		this.pack = pack;
		this.section = section;
		this.skill = skill;
		try {
			this.icon = section.getString("icon", "STONE");
		} catch (Exception e) {
			getPl().getLogger().warning("Error creating item [" + skill.getInternalName() + "] in file [" + pack.file.getName() + "]: " + e.getMessage());
		}
	}
	
	public IconItem load() {
		try {
			this.item = CasterItem.itemType(icon);
			
			NBTItem nbtItem = new NBTItem(item);
			NBTCompound nbt = nbtItem.getOrCreateCompound("ItemCaster");
			nbt.setString("icon_id", skill.getInternalName());
		} catch (IllegalArgumentException e) {
			getPl().getLogger().warning("Material " + this.icon + " is invalid!");
		}
		
		return this;
	}
	
	@Override
	public ItemStack getItemStack() {
		return this.item;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Override
	public String toString() {
		return "IconItem{skill= " + skill.getInternalName() + "}";
	}
}