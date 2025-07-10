package io.phanisment.itemcaster.recipe;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.entity.Player;

import io.lumine.mythic.core.logging.MythicLogger;

import io.phanisment.itemcaster.util.ItemUtil;
import io.phanisment.itemcaster.ItemCaster;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

public class Recipe {
	private final String name;
	private final File file;
	private final ConfigurationSection config;
	
	public RecipeType type;
	public char[] shape = new char[9];
	public Map<Character, Ingredient> ingredients = new HashMap<>();
	public ItemStack result;
	
	public Recipe(String name, File file, ConfigurationSection config) {
		this.name = name;
		this.file = file;
		this.config = config;
		
		this.type = RecipeType.value(config.getString("Type", "SHAPED").toUpperCase());
		this.shape = convertShape(config.getStringList("Shape"));
		
		ConfigurationSection sec = config.getConfigurationSection("Ingredients");
		for (String char_key : sec.getKeys(false)) {
			char symbol = char_key.charAt(0);
			ConfigurationSection isec = sec.getConfigurationSection(char_key);
			ingredients.put(symbol, new Ingredient(isec));
		}
		
		ConfigurationSection resultSec = config.getConfigurationSection("Result");
		if (resultSec != null) {
			this.result = itemResult(resultSec);
		} else {
			this.result = new ItemStack(Material.STONE);
		}
	}
	
	public void matchingCraft(PrepareItemCraftEvent e, ItemStack[] matrix) {
		switch (type) {
			case SHAPED:
				shapedCraft(e, matrix);
				break;
			case SHAPELESS:
				break;
		}
	}
	
	public void shapedCraft(PrepareItemCraftEvent e, ItemStack[] matrix) {
		boolean match = true;
		for (int i = 0; i < 9; i++) {
			char symbol = this.shape[i];
			Ingredient ingredient = this.getIngredient(symbol);
			
			ItemStack expect = (ingredient != null) ? ingredient.getItem() : null;
			ItemStack actual = matrix[i];
			
			if (expect == null && (actual == null || actual.getType().isAir())) continue;
			
			if (!itemMatch(expect, actual)) {
				match = false;
				break;
			}
		}
		
		if (match) {
			e.getInventory().setResult(this.result);
		}
	}
	
	public void reduceMatrix(ItemStack[] matrix, CraftingInventory inv) {
		for (int i = 0; i < 9; i++) {
			int slot = i + 1;
			inv.setItem(slot, null);
		}
		
		Bukkit.getScheduler().runTask(ItemCaster.getInst(), () -> {
			for (int i = 0; i < 9; i++) {
				char symbol = this.shape[i];
				Ingredient ingredient = this.getIngredient(symbol);
				ItemStack actual = matrix[i];
				if (ingredient == null || actual == null || actual.getType().isAir()) continue;
				
				int left = actual.getAmount() - ingredient.getAmount();
				if (left > 0) {
					ItemStack new_item = actual.clone();
					new_item.setAmount(left);
					inv.setItem(i + 1, new_item);
				}
			}
		});
	}
	
	private ItemStack itemResult(ConfigurationSection config) {
		ItemStack item = ItemUtil.getItem(config.getString("Item", "STONE"));
		int amount = config.getInt("Amount", 1);
		item.setAmount(amount);
		
		return item;
	}
	
	public char[] convertShape(List<String> shapeLines) {
		char[] shape = new char[9];
		int index = 0;
		
		for (int row = 0; row < 3; row++) {
			String line = (row < shapeLines.size()) ? shapeLines.get(row) : "   ";
			for (int col = 0; col < 3; col++) {
				char c = (col < line.length()) ? line.charAt(col) : ' ';
				shape[index++] = c;
			}
		}
		
		return shape;
	}
	
	public static boolean itemMatch(ItemStack expect, ItemStack actual) {
		if (expect == null || actual == null) return false;
		if (expect.getType() != actual.getType()) return false;
		if (expect.getAmount() > actual.getAmount()) return false;
		return true;
	}
	
	public boolean matchOnly(ItemStack[] matrix) {
		for (int i = 0; i < 9; i++) {
			char symbol = this.shape[i];
			Ingredient ingredient = this.getIngredient(symbol);
			ItemStack expect = (ingredient != null) ? ingredient.getItem() : null;
			ItemStack actual = matrix[i];
			
			if (expect == null && (actual == null || actual.getType().isAir())) continue;
			if (!itemMatch(expect, actual)) return false;
		}
		return true;
	}
	
	public Ingredient getIngredient(char symbol) {
		return this.ingredients.get(symbol);
	}
	
	public static class Ingredient {
		private final ConfigurationSection config;
		private ItemStack item;
		private int amount;
		
		public Ingredient(ConfigurationSection config) {
			this.config = config;
			
			this.item = ItemUtil.getItem(config.getString("Item", "STONE"));
			this.amount = config.getInt("Amount", 1);
			item.setAmount(amount);
		}
		
		public ItemStack getItem() {
			return this.item;
		}
		
		public int getAmount() {
			return this.amount;
		}
	}
}