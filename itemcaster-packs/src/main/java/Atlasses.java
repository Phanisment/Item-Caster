import java.util.List;
import java.util.ArrayList;

public class Atlasses {
	public List<Atlas> sources = new ArrayList<>();
	
	public void add(String type, String source) {
		sources.add(new Atlas(type, source, source + "/"));
	}
	
	public void single(String source) {
		sources.add(new Atlas("single", source, null));
	}
	
	public static class Atlas {
		public String type;
		public String source;
		public String prefix;
		public Atlas(String type, String source, String prefix) {
			this.type = type;
			this.source = source;
			this.prefix = prefix;
		}
	}
}