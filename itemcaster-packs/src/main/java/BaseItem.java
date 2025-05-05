import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class BaseItem {
	private transient String name;
	private String parent;
	private Map<String, String> textures = new HashMap<>();
	private List<JavaOverride> overrides = new ArrayList<>();
	
	public BaseItem(String json) {
		this(JsonParser.parseString(json).getAsJsonObject());
	}
	
	public BaseItem(File file) throws FileNotFoundException {
		this(new FileReader(file));
	}
	
	public BaseItem(FileReader file) {
		this(JsonParser.parseReader(file).getAsJsonObject());
	}
	
	public BaseItem(@NotNull JsonObject json) {
		this.parent = json.has("parent") ? json.get("parent").getAsString() : null;
		Map<String, JsonElement> texture_list = json.get("textures").getAsJsonObject().asMap();
		if (texture_list != null) texture_list.entrySet().forEach(e ->
			this.textures.put(e.getKey(), e.getValue().getAsString())
		);
		
		JsonArray override_list = json.getAsJsonArray("overrides");
		if (override_list != null) override_list.forEach(override_element -> {
			JsonObject override = override_element.getAsJsonObject();
			String model = override.get("model").getAsString();
			int id = override.getAsJsonObject("predicate").get("custom_model_data").getAsInt();
			this.addOverride(model, id);
		});
	}
	
	public void addOverride(String model, int id) {
		this.overrides.add(new JavaOverride(model, id));
	}

	public void clearOverride() {
		this.overrides.clear();
	}

	public void sortOverride() {
		this.overrides.sort(Comparator.comparingInt(o -> o.getPredicate().custom_model_data));
	}

	public List<JavaOverride> getOverride() {
		return this.overrides;
	}

	public String getName() {
		return this.name;
	}

	public String getParent() {
		return this.parent;
	}

	public Map<String, String> getTexture() {
		return this.textures;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public void setTexture(Map<String, String> textures) {
		this.textures = textures;
	}

	// Nested classes
	public static class JavaOverride {
		private final JavaPredicate predicate;
		private final String model;

		public JavaOverride(String model, int id) {
			this.model = model;
			this.predicate = new JavaPredicate(id);
		}

		public JavaPredicate getPredicate() {
			return this.predicate;
		}

		public String getModel() {
			return this.model;
		}
	}

	public static class JavaPredicate {
		public final int custom_model_data;

		public JavaPredicate(int custom_model_data) {
			this.custom_model_data = custom_model_data;
		}
	}
}