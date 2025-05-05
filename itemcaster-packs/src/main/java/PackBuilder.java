import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Material;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedHashMap;
import java.io.Reader;
import java.io.Writer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class PackBuilder {
	public static Map<Material, Map<String, Integer>> list_model_data = new HashMap<>();
	public static Map<String, ModelItem> items = new HashMap<>();
	private static Map<String, Set<String>> atlasses = new HashMap<>();
	private static List<String> namespaces = new ArrayList<>();
	private static Map<String, List<Font>> fonts = new HashMap<>();
	private static List<Font> impl_fonts = new ArrayList<>();
	private static Map<String, Map<String, String>> local_lang = new HashMap<>();
	private static Map<String, String> global_lang = new HashMap<>();
	public static List<String> language = List.of(
		"af_za",
		"ar_sa",
		"ast_es",
		"az_az",
		"ba_ru",
		"bar",
		"be_by",
		"bg_bg",
		"br_fr",
		"brb",
		"bs_ba",
		"ca_es",
		"cs_cz",
		"cy_gb",
		"da_dk",
		"de_at",
		"de_ch",
		"de_de",
		"el_gr",
		"en_au",
		"en_ca",
		"en_gb",
		"en_nz",
		"en_pt",
		"en_ud",
		"en_us",
		"enp",
		"enws",
		"eo_uy",
		"es_ar",
		"es_cl",
		"es_ec",
		"es_es",
		"es_mx",
		"es_uy",
		"es_ve",
		"esan",
		"et_ee",
		"eu_es",
		"fa_ir",
		"fi_fi",
		"fr_ca",
		"fr_fr",
		"ga_ie",
		"gd_gb",
		"gl_es",
		"he_il",
		"hi_in",
		"hr_hr",
		"hu_hu",
		"hy_am",
		"id_id",
		"is_is",
		"it_it",
		"ja_jp",
		"jbo_en",
		"ka_ge",
		"kk_kz",
		"kn_in",
		"ko_kr",
		"kw_gb",
		"la_la",
		"lb_lu",
		"li_li",
		"lt_lt",
		"lv_lv",
		"mi_nz",
		"mk_mk",
		"ml_in",
		"mn_mn",
		"ms_my",
		"mt_mt",
		"nb_no",
		"nds_de",
		"nl_be",
		"nl_nl",
		"nn_no",
		"no_no",
		"oc_fr",
		"pl_pl",
		"pt_br",
		"pt_pt",
		"qya_aa",
		"ro_ro",
		"ru_ru",
		"se_no",
		"sk_sk",
		"sl_si",
		"sq_al",
		"sr_cs",
		"sv_se",
		"swg",
		"ta_in",
		"th_th",
		"tl_ph",
		"tr_tr",
		"tt_ru",
		"uk_ua",
		"val_es",
		"vi_vn",
		"zh_cn",
		"zh_hk",
		"zh_tw"
	);
	private File input;
	private FileWriter log;
	
	public PackBuilder(File location) {
		this.input = location;
		if (!location.exists()) location.mkdirs();
		File[] ns_folders = location.listFiles();
		if (ns_folders == null) return;
		
		for (File ns : ns_folders) {
			if (!ns.isDirectory()) continue;
			String namespace = ns.getName();
			if (!namespace.equals("minecraft")) {
				File config_file = new File(ns, "configs.yml");
				File config_folder = new File(ns, "configs/");
				if (config_file.exists()) {
					if (config_folder.exists()) return;
					YamlConfiguration config = YamlConfiguration.loadConfiguration(config_file);
					namespaces.add(namespace);
					for (String id : config.getKeys(false)) {
						ModelItem data = new ModelItem(new Pack(config, config_file), config.getConfigurationSection(id), namespace + ":" + id);
						if (data != null) {
							items.put(namespace + ":" + id, data);
							list_model_data.putIfAbsent(data.base, new HashMap<>());
							Map<String, Integer> item = list_model_data.get(data.base);
							if (data.model_data < 1) {
								item.put(namespace + ":" + id, findModelData(item));
								list_model_data.put(data.base, item);
							} else {
								item.put(namespace + ":" + id, data.model_data);
								list_model_data.put(data.base, item);
							}
						}
					}
				}
				
				File fonts_folder = new File(ns, "font");
				if (!fonts_folder.exists()) fonts_folder.mkdirs();
				
				File[] fonts_file = fonts_folder.listFiles();
				for (File font_config : fonts_file) {
					if (!font_config.isFile()) return;
					YamlConfiguration config = YamlConfiguration.loadConfiguration(font_config);
					List<Font> list_font = new ArrayList<>();
					int index = 0;
					for (String id : config.getKeys(false)) {
						Font font = new Font(new Pack(config, font_config), config.getConfigurationSection(id), namespace);
						if (font != null) {
							list_font.add(font);
							impl_fonts.add(font);
						}
					}
					fonts.put(namespace, list_font);
				}
				
				File lang_folder = new File(ns, "lang");
				if (!lang_folder.exists()) lang_folder.mkdirs();
				
				File[] lang_files = lang_folder.listFiles();
				if (lang_files != null) {
					for (File lang_file : lang_files) {
						if (!lang_file.isFile()) continue;
						String fileName = lang_file.getName();
						if (!fileName.endsWith(".json")) continue;
						String langKey = fileName.substring(0, fileName.length() - 5); // remove .json
						try (Reader reader = new FileReader(lang_file)) {
							JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
							if (langKey.equals("global")) {
								for (String key : data.keySet()) {
									global_lang.put(key, data.get(key).getAsString());
								}
							} else if (language.contains(langKey)) {
								Map<String, String> map = new HashMap<>();
								for (String key : data.keySet()) {
									map.put(key, data.get(key).getAsString());
								}
								local_lang.put(langKey, map);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			} else {
				try {
					Files.walk(ns.toPath()).forEach(this::readMcNmsc);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void readMcNmsc(Path path) {
		File file = new File(path.toString());
		if (!file.isFile()) return;
	}
	
	public void generate(File location) {
		delete(location);
		if (!location.exists()) location.mkdirs();
		
		String description = Main.pack.getString("description");
		int version = Main.pack.getInt("version", 15);
		
		try {
			File logFile = new File(".data/generate.log");
			logFile.getParentFile().mkdirs();
			this.log = new FileWriter(logFile, false); // false untuk overwrite log lama
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File assets = new File(location, "assets");
		if (!assets.exists()) assets.mkdirs();
		
		File mc_ns = new File(assets, "minecraft");
		if (!mc_ns.exists()) mc_ns.mkdirs();
		
		Atlasses atlas = new Atlasses();
		List atlas_used = new ArrayList<>();
		
		try (FileWriter writer = new FileWriter(new File(location, "pack.mcmeta"))) {
			new GsonBuilder().create().toJson(new PackMeta(description, version), writer);
			Files.copy(new File("pack.png").toPath(), new File(location, "pack.png").toPath(), StandardCopyOption.REPLACE_EXISTING);
			log("Moving the icon.png to " + location.toString());
		} catch (IOException e) {
			log("Whent wrong when move the icon.png: " + e);
			e.printStackTrace();
		}
		
		for (String id : items.keySet()) {
			if (!id.contains(":")) return;
			String[] split = id.split(":");
			File ns = new File(assets, split[0]);
			if (!ns.exists()) ns.mkdirs();
			log("Detected namespace: " + split[0]);
			
			// Models & Tetxtures
			if (!split[0].equals("minecraft")) {
				ModelItem item = items.get(id);
				if (item != null) {
					File model = new File(ns, "models/" + surfix(item.model, "json"));
					File src_model = new File(input, split[0] + "/models/" + surfix(item.model, "json"));
					if (!src_model.exists()) {
						log("WARNING, Model File " + src_model.toString() + " is not found!");
						return;
					}
					
					JsonObject fix_model = new JsonObject();
					try {
						JsonElement element = JsonParser.parseReader(new FileReader(src_model));
						if (!element.isJsonObject()) return;
						JsonObject obj = element.getAsJsonObject();
						if (obj.has("textures")) {
							JsonObject textures = obj.getAsJsonObject("textures");
							for (String key : textures.keySet()) {
								String value = textures.get(key).getAsString();
								if (!value.contains(":")) {
									textures.addProperty(key, split[0] + ":" + value);
									File src_texture = new File(input, split[0] + "/textures/" + surfix(value, "png"));
									File rslt_txt = new File(ns, "textures/" + surfix(value, "png"));
									boolean is_animated = false;
									if (!src_texture.exists()) {
										log("WARNING, Texture " + value +" not found in model: " + split[0] + "/models/" + item.model);
									} else {
										BufferedImage img = ImageIO.read(src_texture);
										if (img.getHeight() > img.getWidth()) is_animated = true;
									}
									rslt_txt.getParentFile().mkdirs();
									if (!rslt_txt.exists()) {
										try {
											Files.copy(src_texture.toPath(), rslt_txt.toPath());
											log("Move texture file " + src_texture.toPath() + " to " + rslt_txt.toPath());
										} catch (IOException e) {
											e.printStackTrace();
										}
										if (is_animated) {
											try (FileWriter writer = new FileWriter(new File(rslt_txt.getParentFile(), src_texture.getName() + ".mcmeta"))) {
												new GsonBuilder().create().toJson(new Animation(1), writer);
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									}
									atlasses.putIfAbsent(split[0], new TreeSet<>());
									atlasses.get(split[0]).add(value);
								} else {
									String[] v = value.split(":");
									File src_texture = new File(input, v[0] + "/textures/" + surfix(v[1], "png"));
									if (!src_texture.exists()) {
										log("WARNING, Texture " + value +" not found in model: " + split[0] + "/models/" + item.model);
									}
									File rslt_txt = new File(assets, v[0] + "/textures/" + surfix(v[1], "png"));
									rslt_txt.getParentFile().mkdirs();
									if (!rslt_txt.exists()) {
										try {
											Files.copy(src_texture.toPath(), rslt_txt.toPath());
											log("Move texture file " + src_texture.toPath() + " to " + rslt_txt.toPath());
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									atlasses.putIfAbsent(v[0], new TreeSet<>());
									atlasses.get(v[0]).add(v[1]);
								}
							}
						}
						fix_model = obj;
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if (fix_model != null) {
						model.getParentFile().mkdirs();
						try (FileWriter writer = new FileWriter(model)) {
							new GsonBuilder().create().toJson(fix_model, writer);
							log("Edited model move to " + src_model.toPath() + " to " + model.toPath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Files.copy(src_model.toPath(), model.toPath(), StandardCopyOption.REPLACE_EXISTING);
							log("Move file " + src_model.toPath() + " to " + model.toPath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				
			}
		}
		
		// Fonts
		File font_loc = new File(mc_ns, "font/default.json");
		if (!font_loc.exists()) font_loc.getParentFile().mkdirs();
		if (impl_fonts != null) {
			for (int i = 0; i < impl_fonts.size(); i++) {
				Font f = impl_fonts.get(i);
				if (f.file != null && !f.file.contains(":")) {
					File ft_loc = new File(input, f.namespace + "/textures/" + surfix(f.file, "png"));
					File tt_loc = new File(assets, f.namespace + "/textures/" + surfix(f.file, "png"));
					if (!tt_loc.exists()) tt_loc.getParentFile().mkdirs();
					if (ft_loc.exists()) {
						try {
							Files.copy(ft_loc.toPath(), tt_loc.toPath(), StandardCopyOption.REPLACE_EXISTING);
							log("Move file " + ft_loc.toPath() + " to " + tt_loc.toPath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						log("Unknown file for " + ft_loc.toString());
					}
					f.file = f.namespace + ":" + f.file;
				} else if (f.file != null && f.file.contains(":")) {
					String[] split = f.file.split(":");
					File ft_loc = new File(input, split[0] + "/textures/" + surfix(f.file, "png"));
					File tt_loc = new File(assets, split[0] + "/textures/" + surfix(f.file, "png"));
					if (!tt_loc.exists()) tt_loc.getParentFile().mkdirs();
					if (ft_loc.exists()) {
					try {
							Files.copy(ft_loc.toPath(), tt_loc.toPath(), StandardCopyOption.REPLACE_EXISTING);
							log("Move file " + ft_loc.toPath() + " to " + tt_loc.toPath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						log("Unknown file for " + ft_loc.toString());
					}
				}
			}
			Fonts i_f = new Fonts(impl_fonts);
			try (FileWriter writer = new FileWriter(font_loc)) {
				new GsonBuilder().create().toJson(i_f, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Lang
		File lang_loc = new File(mc_ns, "lang");
		if (!lang_loc.exists()) lang_loc.mkdirs();
		if (lang_loc != null) {
			for (String lc : language) {
				File langFile = new File(lang_loc, surfix(lc, "json"));
				Map<String, String> globalData = new LinkedHashMap<>(global_lang);
				if (!globalData.isEmpty()) {
					try (Writer writer = new FileWriter(langFile)) {
						new GsonBuilder().create().toJson(globalData, writer);
						log("Generate lang: " + langFile.getName());
					} catch (IOException e) {
						log("Failed to create global lang: " + langFile.getName());
						e.printStackTrace();
					}
				}
			}
			
			for (Map.Entry<String, Map<String, String>> entry : local_lang.entrySet()) {
				String fileName = entry.getKey();
				Map<String, String> langData = entry.getValue();
				if (langData.isEmpty()) continue;
			
				File langFile = new File(lang_loc, surfix(fileName, "json"));
			
				Map<String, String> merged = new LinkedHashMap<>(global_lang);
				merged.putAll(langData);
			
				try (Writer writer = new FileWriter(langFile)) {
					new GsonBuilder().create().toJson(merged, writer);
					log("Generate merged lang: " + langFile.getName());
				} catch (IOException e) {
					log("Failed to write merged lang: " + langFile.getName());
					e.printStackTrace();
				}
			}
		}
		
		// Atlasses
		if (atlasses != null) {
			for (String namspc : atlasses.keySet()) {
				Set<String> folders = atlasses.get(namspc);
				for (String folder : folders) {
					String[] source = folder.split("/");
					String type = Main.pack.getString("generate.atlas_type");
					if (!source[0].equals("item")) {
						if (type.equals("directory") && !atlas_used.contains(source[0])) {
							atlas.add("directory", source[0]);
							atlas_used.add(source[0]);
						} else if (type.equals("single")) {
							atlas.single(namspc + ":" + folder);
						}
					}
				}
			}
		}
		
		File atlas_folder = new File(mc_ns, "atlasses");
		if (!atlas_folder.exists()) atlas_folder.mkdirs();
		
		try (FileWriter writer = new FileWriter(new File(atlas_folder, "blocks.json"))) {
			new GsonBuilder().create().toJson(atlas, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sortModelData(Main.pack.getInt("start_model_data", 100));
		generateBaseItem(new File(".data/models/item"), new File(".data/output/assets/minecraft/models/item"));
		try (FileWriter writer = new FileWriter(new File(".data/model_data.json"))) {
			new GsonBuilder().create().toJson(list_model_data, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File generated = new File(".data/generated.zip");
		File o_l = new File(".data/output/");
		if (generated.exists()) generated.delete();
		try {
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(CompressionMethod.DEFLATE);
			parameters.setCompressionLevel(CompressionLevel.NORMAL);
			ZipFile zip = new ZipFile(generated);
			for (File file : o_l.listFiles()) {
				if (file.isDirectory()) {
					zip.addFolder(file, parameters);
				} else {
					zip.addFile(file, parameters);
				}
			}
			log("Created a Zip file.");
		} catch (Exception e) {
			log("Failed to Create Zip file");
		}
	}
	
	private String surfix(String value, String surfix) {
		if (!value.endsWith("." + surfix)) {
			return value + "." + surfix;
		}
		return value;
	}
	
	private void generateBaseItem(File baseInput, File baseOutput) {
		if (!baseInput.exists() || !baseInput.isDirectory()) {
			log("Base model folder not found: " + baseInput.getPath());
			return;
		}
	
		for (Material material : list_model_data.keySet()) {
			File inputFile = new File(baseInput, material.name().toLowerCase() + ".json");
			if (!inputFile.exists()) {
				log("Base model for " + material.name() + " not found: " + inputFile.getPath());
				continue;
			}
	
			try {
				BaseItem baseItem = new BaseItem(inputFile);
				baseItem.clearOverride();
	
				Map<String, Integer> entries = list_model_data.get(material);
				for (Map.Entry<String, Integer> entry : entries.entrySet()) {
					String namespacedId = entry.getKey();
					int modelData = entry.getValue();
					ModelItem modelItem = items.get(namespacedId);
					if (modelItem == null) continue;
	
					String modelPath = namespacedId.split(":")[0] + ":" + modelItem.model;
					baseItem.addOverride(modelPath, modelData);
				}
	
				baseItem.sortOverride(); // Urutkan berdasarkan custom_model_data
	
				// Simpan hasil
				File outputFile = new File(baseOutput, material.name().toLowerCase() + ".json");
				outputFile.getParentFile().mkdirs();
	
				JsonObject outputJson = new JsonObject();
				if (baseItem.getParent() != null) {
					outputJson.addProperty("parent", baseItem.getParent());
				}
	
				JsonObject texturesJson = new JsonObject();
				for (Map.Entry<String, String> tex : baseItem.getTexture().entrySet()) {
					texturesJson.addProperty(tex.getKey(), tex.getValue());
				}
				outputJson.add("textures", texturesJson);
	
				JsonArray overridesArray = new JsonArray();
				for (BaseItem.JavaOverride override : baseItem.getOverride()) {
					JsonObject overrideObj = new JsonObject();
					JsonObject predicateObj = new JsonObject();
					predicateObj.addProperty("custom_model_data", override.getPredicate().custom_model_data);
					overrideObj.add("predicate", predicateObj);
					overrideObj.addProperty("model", override.getModel());
					overridesArray.add(overrideObj);
				}
				outputJson.add("overrides", overridesArray);
	
				try (FileWriter writer = new FileWriter(outputFile)) {
					new GsonBuilder().create().toJson(outputJson, writer);
				}
	
			} catch (Exception e) {
				log("Error processing base model for " + material.name() + ": " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private void sortModelData(int startFrom) {
		Map<Material, Map<String, Integer>> fixed = new HashMap<>();
		Set<Integer> used = new TreeSet<>();
		for (Map<String, Integer> item : list_model_data.values()) {
			used.addAll(item.values());
		}
		for (Map.Entry<Material, Map<String, Integer>> entry : list_model_data.entrySet()) {
			Material material = entry.getKey();
			Map<String, Integer> tempItems = new HashMap<>();
			Map<Integer, String> reverseCheck = new HashMap<>();
			for (Map.Entry<String, Integer> item : entry.getValue().entrySet()) {
				String id = item.getKey();
				int modelData = startFrom + item.getValue();
				if (reverseCheck.containsKey(modelData)) {
					int newModelData = modelData;
					while (used.contains(newModelData)) {
						newModelData++;
					}
					log("Duplicate found for " + id + " (" + modelData + "), changing to " + newModelData);
					modelData = newModelData;
					used.add(modelData);
				} else {
					used.add(modelData);
				}
				tempItems.put(id, modelData);
				reverseCheck.put(modelData, id);
			}
			List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(tempItems.entrySet());
			sortedEntries.sort(Map.Entry.comparingByValue());
			Map<String, Integer> sortedItems = new LinkedHashMap<>();
			for (Map.Entry<String, Integer> e : sortedEntries) {
				sortedItems.put(e.getKey(), e.getValue());
			}
			fixed.put(material, sortedItems);
		}
	
		list_model_data = fixed;
	}
	
	private static int findModelData(Map<String, Integer> item) {
		int id = 1;
		List<Integer> used = new ArrayList<>(item.values());
		while (used.contains(id)) {
			id++;
		}
		return id;
	}
	
	private static void delete(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null) {
				for (File child : children) {
					delete(child);
				}
			}
		}
		file.delete();
	}
	
	private void log(String message) {
		try {
			System.out.println(message);
			if (log != null) {
				log.write(message + "\n");
				log.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class ModelItem {
		private ConfigurationSection config;
		private String id;
		
		public Material base;
		public String model;
		public int model_data;
		
		public ModelItem(Pack pack, ConfigurationSection config, String id) {
			this.config = config;
			this.id = id;
			try {
				this.base = Material.valueOf(config.getString("base", "STICK").toUpperCase());
				this.model = config.getString("model");
				this.model_data = config.getInt("model_data", 0);
			} catch (Exception e) {
				
			}
		}
		
		public String getId() {
			return this.id;
		}
	}
	
	private class Pack {
		YamlConfiguration config;
		File file;
		Pack(YamlConfiguration config, File file) {
			this.config = config;
			this.file = file;
		}
	}
	
	private class Fonts {
		List<Font> providers;
		public Fonts(List<Font> providers) {
			this.providers = providers;
		}
	}
	
	private class Font {
		transient Pack pack;
		transient ConfigurationSection config;
		transient String namespace;
		
		String type;
		String file;
		Integer height;
		Integer ascent;
		List<Character> chars;
		Map<String, Integer> advances;
		Font(Pack pack, ConfigurationSection config, String namespace) {
			this.pack = pack;
			this.config = config;
			this.namespace = namespace;
			
			try {
				this.type = config.getString("type", "bitmap");
				this.file = config.getString("file");
				if (config.contains("height")) this.height = config.getInt("height");
				if (config.contains("ascent")) this.ascent = config.getInt("ascent");
				this.chars = (List<Character>)config.getList("chars");
				if (config.contains("advances")) {
					this.advances = new HashMap<>();
					for (String c : config.getConfigurationSection("advances").getKeys(false)) {
						int asc = config.getInt("advances." + c);
						advances.put(c, asc);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}