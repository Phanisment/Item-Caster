package io.phanisment.itemcaster.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;

import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;

import io.phanisment.itemcaster.block.BlockObject;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
			ItemStack item = event.getItem();
			Block clickedBlock = event.getClickedBlock();
			BlockFace face = event.getBlockFace();
			if (item != null && clickedBlock != null) {
				new BlockObject(clickedBlock, item, event.getHand(), face);
			}
		}
	}
}