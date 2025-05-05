import javax.script.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.lang.reflect.Array;

public class FormatScript {
	private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
	public static Map<String, CompiledScript> chace = new HashMap<>();
	
	public FormatScript() {
		try {
			Files.walk(Path.of("formats")).forEach(p -> {
				File script = new File(p.toString());
				if (!script.isFile()) return;
				try {
					readFile(p, script);
				} catch (ScriptException e) {
					e.printStackTrace();
				}
			});
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readFile(Path p, File f) throws ScriptException {
		String name = p.toString().replaceAll("formats/", "");
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
			System.err.println("Error running script: " + type);
			e.printStackTrace();
		}
		return Collections.emptyList();
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