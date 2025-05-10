package io.phanisment.itemcaster.script;

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import io.phanisment.itemcaster.ItemCaster;

import javax.script.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.lang.reflect.Array;

public class FormatScript {
	private static final ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
	public static Map<String, CompiledScript> chace = new HashMap<>();
	
	public FormatScript() {
		load();
	}
	
	public void load() {
		File file = new File(ItemCaster.getInst().getDataFolder(), "formats");
		if (!file.exists()) {
			file.mkdirs();
			ItemCaster.getInst().saveResource("formats/default.js", false);
		}
		
		try {
			Files.walk(file.toPath()).forEach(p -> {
				File script = new File(p.toString());
				if (script.isFile()) {
					try {
						readFile(p, script);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
				}
			});
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readFile(Path p, File f) throws ScriptException {
		String name = f.getName();
		Compilable compile = (Compilable)engine;
		StringBuilder script = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(p.toString()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				script.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		CompiledScript compiled = compile.compile(script.toString());
		chace.put(name, compiled);
	}
	
	public static List<String> run(String type, ScriptData... datas) {
		CompiledScript script = chace.get(type);
		if (script == null) return null;
		Bindings bindings = engine.createBindings();
		for (ScriptData dat : datas) {
			bindings.put(dat.name, dat.value);
		}
		
		try {
			Object result = script.eval(bindings);
			if (result instanceof List) {
				@SuppressWarnings("unchecked")
				List<String> list = (List<String>) result;
				return list;
			} else if (result instanceof String[]) {
				return Arrays.asList((String[]) result);
			} else if (result != null) {
				return List.of(result.toString());
			}
		} catch (ScriptException e) {
			String message = simplifyScriptException(e);
			System.err.println("[Script Error] " + message);
			return Collections.singletonList("§cScript Error: " + message);
		}
		return Collections.emptyList();
	}
	
	private static String simplifyScriptException(ScriptException e) {
		String msg = e.getMessage();
		int line = e.getLineNumber();
		int column = e.getColumnNumber();

		if (msg.contains("\n")) {
			msg = msg.substring(0, msg.indexOf("\n")).trim();
		}
		return String.format("Line %d, Column %d: %s", line, column, msg);
	}

	
	public static class ScriptData {
		public String name;
		public Object value;
		public ScriptData(String name, Object value) {
			this.name = name;
			this.value = value;
		}
	}
}