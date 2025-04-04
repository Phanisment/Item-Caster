package io.phanisment.itemcaster.config;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.NamespacedKey;

import java.util.Map;
import java.util.HashMap;

public class TrimsData {
	public static final Map<String, TrimMaterial> trim_material = new HashMap<>();
	public static final Map<String, TrimPattern> trim_pattern = new HashMap<>();
	
	public TrimsData() {
		trim_material.put("AMETHYST", TrimMaterial.AMETHYST);
		trim_material.put("COPPER", TrimMaterial.COPPER);
		trim_material.put("DIAMOND", TrimMaterial.DIAMOND);
		trim_material.put("EMERALD", TrimMaterial.EMERALD);
		trim_material.put("GOLD", TrimMaterial.GOLD);
		trim_material.put("IRON", TrimMaterial.IRON);
		trim_material.put("LAPIS", TrimMaterial.LAPIS);
		trim_material.put("NETHERITE", TrimMaterial.NETHERITE);
		trim_material.put("QUARTZ", TrimMaterial.QUARTZ);
		
		trim_pattern.put("COAST", TrimPattern.COAST);
		trim_pattern.put("DUNE", TrimPattern.DUNE);
		trim_pattern.put("EYE", TrimPattern.EYE);
		trim_pattern.put("HOST", TrimPattern.HOST);
		trim_pattern.put("RAISER", TrimPattern.RAISER);
		trim_pattern.put("RIB", TrimPattern.RIB);
		trim_pattern.put("SENTRY", TrimPattern.SENTRY);
		trim_pattern.put("SHAPER", TrimPattern.SHAPER);
		trim_pattern.put("SILENCE", TrimPattern.SILENCE);
		trim_pattern.put("SNOUT", TrimPattern.SNOUT);
		trim_pattern.put("SPIRE", TrimPattern.SPIRE);
		trim_pattern.put("TIDE", TrimPattern.TIDE);
		trim_pattern.put("VEX", TrimPattern.VEX);
		trim_pattern.put("WARD", TrimPattern.WARD);
		trim_pattern.put("WAYFINDER", TrimPattern.WAYFINDER);
		trim_pattern.put("WILD", TrimPattern.WILD);
	}
	
	public TrimMaterial getMaterial(String material) {
		return trim_material.get(material);
	}
	
	public TrimPattern getPattern(String pattern) {
		return trim_pattern.get(pattern);
	}
	
	public Map<String, TrimMaterial> getTrimMaterial() {
		return trim_material;
	}
	
	public Map<String, TrimPattern> getTrimPattern()  {
		return trim_pattern;
	}
}