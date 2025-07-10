package io.phanisment.itemcaster.recipe;

public enum RecipeType {
	SHAPED,
	SHAPELESS;
	
	public static RecipeType value(String name) {
		return Enum.valueOf(RecipeType.class, name);
	}
}