public class Animation {
	public Anim animation;
	public Animation(Integer frametime) {
		this.animation = new Anim(frametime);
	}
	
	private class Anim {
		public Integer frametime;
		public Anim(Integer frame) {
			this.frametime = frame;
		}
	}
}