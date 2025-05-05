public class PackMeta {
	public Pack pack;
	public PackMeta(String description, int version) {
		this.pack = new Pack(description, version);
	}
	private class Pack {
		String description;
		int pack_format;
		Pack(String description, int pack_format) {
			this.description = description;
			this.pack_format = pack_format;
		}
	}
}