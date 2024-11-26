package phanisment.artifact.skill;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;

import phanisment.artifact.MythicMobs;

public class ItemNbtManager {
	private Player player;
	private ItemStack item = null;
	private NBTCompoundList data = null;
	private Activator activator;
	
	public ItemNbtManager(Player player, Activator activator) {
		this.player = player;
		this.activator = activator;
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item != null || item.getType() != Material.AIR) return;
		this.item = item;
		NBTItem nbt = new NBTItem(item);
		if (nbt.hasKey("Artifact")) {
			this.data = nbt.getCompoundList("Artifact");
		}
	}
	
	public void check() {
		if (data == null || data.isEmpty()) return;
		for (ReadWriteNBT ability : data) {
			if (ability.getString("activator").toUpperCase().equals(activator.toString())) {
				MythicMobs.cast(ability.getString("skill"), player);
			}
		}
	}
	
	public enum Activator {
		LEFT_CLICK,
		RIGHT_CLICK,
		SNEAK,
		TICK
	}
}