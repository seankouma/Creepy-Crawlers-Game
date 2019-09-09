package data;

public enum TileType {

	Grass("grass", true), Dirt("dirt", false);
	
	String textureName;
	boolean buildable;
	
	TileType(String textureName, boolean buildable) {
			this.textureName = textureName;
			this.buildable = buildable;
		}
}
