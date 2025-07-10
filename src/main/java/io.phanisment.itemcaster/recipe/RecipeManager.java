package io.phanisment.itemcaster.recipe;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import io.lumine.mythic.core.config.IOHandler;
import io.lumine.mythic.core.config.IOLoader;
import io.lumine.mythic.api.packs.Pack;
import io.lumine.mythic.core.logging.MythicLogger;
import io.lumine.mythic.bukkit.MythicBukkit;

import io.phanisment.itemcaster.config.Configuration;
import io.phanisment.itemcaster.ItemCaster;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class RecipeManager {
	private static Configuration configuration;
	private Pack pack;
	private File recipes_file;
	
	private static final Map<String, Recipe> RECIPES = new HashMap<>();
	
	public RecipeManager(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public void load(Pack pack, File recipes_file) {
		this.pack = pack;
		this.recipes_file = recipes_file;
		
		RECIPES.clear();
		MythicLogger.log("Loading Recipes...");
		List<File> file_recipes = IOHandler.getAllFiles(recipes_file.toString());
		for (File file : file_recipes) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			for (String name : config.getKeys(false)) {
				ConfigurationSection sec = config.getConfigurationSection(name);
				Recipe r = new Recipe(name, file, sec);
				RECIPES.put(name, r);
			}
		}
	}
	
	public static Collection<Recipe> getRecipes() {
		return RECIPES.values();
	}
}