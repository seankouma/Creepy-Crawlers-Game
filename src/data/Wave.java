package data;

import java.util.ArrayList;
import java.util.Date;

import static helpers.Clock.*;

/*
 * This class is designed to keep track of the current enemies
 * on the board at a higher level.
 */

public class Wave {
	// Prevent enemies from spawning right after each other and
	// stacking themselves
	private float timeSinceLastSpawn, spawnTime;
	// This is how Enemies in the game are grouped and managed as one
	private ArrayList<Enemy> enemyList;
	private Enemy head;
	private int length;
	TileGrid grid;
	Tile startTile;
	
	public Wave(TileGrid grid, int length, Tile startTile) {
		enemyList = new ArrayList<Enemy>();
		spawnTime = 4;
		this.setLength(length);
		this.grid = grid;
		this.startTile = startTile;
	}
//	
	public void Update() {
		timeSinceLastSpawn += Delta();
		if (timeSinceLastSpawn >= spawnTime && enemyList.size() != getLength()) {
			enemyList.add(Enemy.quickLoad(this, grid, startTile));
			head = enemyList.get(0);
			enemyList.get(enemyList.size()-1).setParent(this);
			timeSinceLastSpawn = 0;
		}
		
		for (Enemy one: enemyList) {
				one.Update();
				one.Draw();
		}

	}
	
	
	
	public ArrayList<Enemy> getEnemyList() {
		return enemyList;
	}

	public void setEnemyList(ArrayList<Enemy> enemyList) {
		this.enemyList = enemyList;
	}

	public Enemy getHead() {
		return head;
	}

	public void setHead(Enemy head) {
		this.head = head;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	// Designed to specify where the new guy needs to be put
	public void setLength(int length, Tile t) {
		this.length = length;
		this.startTile = t;
		this.Update();
	}

}
