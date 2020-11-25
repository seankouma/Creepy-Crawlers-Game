package data;

import static helpers.Artist.*;

public class TileGrid {

	public Tile[][] map;
	private int tilesWide;
	private int tilesHigh;
	
	public TileGrid() {
		map = new Tile[20][15];
		for (int i = 0; i < map.length; i++) {
			for (int k = 0; k < map[i].length; k++) {
				map[i][k] = new Tile(i*64,  k*64, 64, 64, TileType.Grass);
			}
		}
	}
	
	public Tile[][] getMap() {
		return map;
	}

	public void setMap(Tile[][] map) {
		this.map = map;
	}

	public int getTilesWide() {
		return tilesWide;
	}

	public void setTilesWide(int tilesWide) {
		this.tilesWide = tilesWide;
	}

	public int getTilesHigh() {
		return tilesHigh;
	}

	public void setTilesHigh(int tilesHigh) {
		this.tilesHigh = tilesHigh;
	}

	public TileGrid(int[][] newMap) {
		this.tilesWide = newMap[0].length;
		this.tilesHigh = newMap.length;
		map = new Tile[tilesWide][tilesHigh];
		for (int i = 0; i< map.length; i++) {
			for (int k = 0; k < map[i].length; k++) {
				
				switch (newMap[k][i]) {
				case 0:
					map[i][k] = new Tile(i*64,  k*64, 64, 64, TileType.Grass);
					break;
				case 1: 
					map[i][k] = new Tile(i*64,  k*64, 64, 64, TileType.Dirt);
					break;
				case 2:
					map[i][k] = new Tile(i*64,  k*64, 64, 64, TileType.Water);
					break;
				case 3:
					map[i][k] = new Tile(i*64,  k*64, 64, 64, TileType.Sand);
					break;

				}
			}
		}
	}
	
	public void setTile(int xCoord, int yCoord, TileType type) {
		map[xCoord][yCoord] = new Tile(xCoord*64, yCoord*64, 64, 64, type);
	}
	
	public Tile getTile(int xPlace, int yPlace) {
		if (xPlace < tilesWide && yPlace < tilesHigh && xPlace > -1 && yPlace > -1)
			return map[xPlace][yPlace];
		else 
			return new Tile(0, 0, 0, 0, TileType.NULL);
	}
	
	public void Draw() {
		for (int i = 0; i < map.length; i++) {
			for (int k = 0; k < map[i].length; k++) {
				map[i][k].Draw();
				
			}
		}
	}
}
