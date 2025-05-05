package io.phanisment.itemcaster.block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import de.tr7zw.changeme.nbtapi.NBTType;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;

import java.util.List;

public class BlockObject {
	public Block block;
	public ItemStack item;
	public EquipmentSlot slot;
	public BlockFace face;
	
	public String type;
	public boolean need_ground;
	public List<DropData> drops;
	
	public BlockObject(Block block, ItemStack item, EquipmentSlot slot, BlockFace face) {
		this.block = block;
		this.item = item;
		this.slot = slot;
		this.face = face;
		NBTCompound nbt = new NBTItem(item).getCompound("ItemCaster").getCompound("furniture");
		if (nbt != null) {
			this.type = nbt.getString("type");
			NBTCompoundList list_drop = nbt.getCompoundList("drops");
			if (list_drop != null) list_drop.forEach(drop -> {
				this.item = drop.getItemStack("item");
			});
		}
	}
	
	public void place() {
		if (!type.isEmpty()) {
			switch (type.toLowerCase()) {
				case "item_frame":
					break;
				case "glowing_item_frame":
					break;
				case "item_display":
					break;
				case "modelengine":
					break;
				default:
					break;
			}
		}
	}
	
	private void spawnItemFrame() {
		World world = block.getLocation().getWorld();
		if (world == null) return;

		Vector offset = face.getDirection().multiply(0.5);
		Location spawnLoc = block.getLocation().clone().add(0.5, 0.5, 0.5).add(offset);

		ItemFrame frame = (ItemFrame) world.spawnEntity(spawnLoc, EntityType.ITEM_FRAME);
		frame.setFacingDirection(face, true);
		frame.setItem(item);
		frame.setInvulnerable(true);
		frame.setFixed(true);
	}
	
	public static class DropData {
		public ItemStack item;
		public int min = 1;
		public int max = -1; // Empty
		public float weight = 1; // DEF: 100%
		
		public DropData(ItemStack item, int min, int max, float weight) {
			this.item = item;
			this.min = min;
			this.max = max;
			this.weight = weight;
		}
	}
}

/*

ItemCaster -> furniture: {
	type: "item_frame",
	display: "model_when_placed",
	drops: [
		{item: "item id its self", amount: 1}
	]
}

*/