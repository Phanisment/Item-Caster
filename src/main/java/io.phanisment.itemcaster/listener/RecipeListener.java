package io.phanisment.itemcaster.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.Bukkit;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import io.phanisment.itemcaster.recipe.RecipeManager;
import io.phanisment.itemcaster.recipe.Recipe;
import io.phanisment.itemcaster.util.DebugUtil;
import io.phanisment.itemcaster.ItemCaster;

import java.util.Map;
import java.util.HashMap;

public class RecipeListener implements Listener {
	
	@EventHandler
	public void onPrepare(PrepareItemCraftEvent e) {
		if (e.isRepair()) return;
		
		ItemStack[] matrix = e.getInventory().getMatrix();
		for (Recipe r : RecipeManager.getRecipes()) {
			if (r.matchOnly(matrix)) {
				r.matchingCraft(e, matrix);
				break;
			}
		}
	}
	
	@EventHandler
	public void onCraft(InventoryClickEvent e) {
		if (e.getInventory() instanceof CraftingInventory inv_craft) {
			if (e.getSlotType() != SlotType.RESULT) return;
			if (e.getCursor() != null && !e.getCursor().getType().isAir()) return;
			
			ItemStack[] matrix = inv_craft.getMatrix();
			for (Recipe r : RecipeManager.getRecipes()) {
				if (r.matchOnly(matrix)) {
					e.setCancelled(true);
					r.reduceMatrix(matrix, inv_craft);
					break;
				}
			}
		}
		// Furnace, etc.
	}
}