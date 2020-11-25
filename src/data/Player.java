package data;

import org.lwjgl.input.Keyboard;

//import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import static helpers.Artist.*;

import java.util.ArrayList;

import static helpers.Clock.*;

public class Player {
	
	private int[] dir = new int[2];
	private int[] nextDir = {0, -1};
	Wave wave;
	private TileGrid grid;
	private Checkpoint current;
	private Tile startTile;
	private float x, y;
	int speed = 7;
	private Texture texture;
	private float timeSinceLastCollision;
	
	public Player(TileGrid grid) {
		wave = new Wave(grid, 3, grid.getTile(18, 14));
		this.grid = grid;
		dir[0] = 0;
		dir[1] = -1;
		this.texture = QuickLoad("Cat");
		startTile = grid.getTile(18, 14);
		this.x = startTile.getX();
		this.y = startTile.getY();
		current = this.FindNextC(startTile, dir);
		
	}
	
	public void Update() {
		wave.Update();
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_UP && Keyboard.getEventKeyState()) {
				nextDir[0] = 0;
				nextDir[1] = -1;
			} else if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
				nextDir[0] = 1;
				nextDir[1] = 0;			
			} else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && Keyboard.getEventKeyState()) {
				nextDir[0] = 0;
				nextDir[1] = 1;
			} else if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
				nextDir[0] = -1;
				nextDir[1] = 0;
			}
			wave.getEnemyList().get(0).setNextDir(nextDir);
		}
		
		}
	
	public void collision(Wave enemyWave, Enemy head) {
		if (timeSinceLastCollision == 0) timeSinceLastCollision = getTime();
		else if (getTime() - timeSinceLastCollision < 4000) return;
		Tile current = grid.getTile( (int) Math.floor(head.getX() / 64) - head.subtract(head.getDirections()[0], 'x'), (int) Math.floor((head.getY()) / 64) - head.subtract(head.getDirections()[1], 'y'));
		for (Enemy enemy : enemyWave.getEnemyList()) {
			if (current == 
					grid.getTile( (int) Math.floor(enemy.getX() / 64) - enemy.subtract(enemy.getDirections()[0], 'x'), (int) Math.floor((enemy.getY()) / 64) - enemy.subtract(enemy.getDirections()[1], 'y'))) {
				ArrayList<Enemy> myDudes = wave.getEnemyList();
				Enemy lastMan = myDudes.get(myDudes.size() - 1);
				Tile myTile = grid.getTile( (int) Math.floor(lastMan.getX() / 64) - lastMan.subtract(lastMan.getDirections()[0], 'x') - lastMan.subtract(lastMan.getDirections()[0], 'x') , (int) Math.floor((lastMan.getY()) / 64) - lastMan.subtract(lastMan.getDirections()[1], 'y'));
				this.wave.setLength(this.wave.getLength() + 1, myTile);
				timeSinceLastCollision = getTime();
				return;
			}
		}
	}
	
	public void Draw() {
		DrawQuadTex(texture, x, y, 64, 64);
	}
	
	public int[] getDir() {
		return dir;
	}

	public void setDir(int[] dir) {
		this.dir = dir;
	}

	public int[] getNextDir() {
		return nextDir;
	}

	public void setNextDir(int[] nextDir) {
		this.nextDir = nextDir;
	}

	public TileGrid getGrid() {
		return grid;
	}

	public void setGrid(TileGrid grid) {
		this.grid = grid;
	}

	public Checkpoint getCurrent() {
		return current;
	}

	public void setCurrent(Checkpoint current) {
		this.current = current;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	private Checkpoint FindNextC(Tile s, int[] dir) {
		Tile next = null;
		Checkpoint c = null;
		
		//Boolean to decide if checkpoint is found
		boolean found = false;
		
		//Looping over the tiles in the direction the enemy is moving to find a checkpoint.
		for(int i = 1; !found; i++) {
			
			ArrayList<Character> direction = new ArrayList<Character>();
			Tile u = grid.getTile(s.getXPlace() + dir[0] *(i - 1), s.getYPlace() - 1 + dir[1] *(i - 1));
			Tile r = grid.getTile(s.getXPlace() + 1 + dir[0] *(i - 1), s.getYPlace() + dir[1] *(i - 1));
			Tile d = grid.getTile(s.getXPlace() + dir[0] *(i - 1), s.getYPlace() + 1 + dir[1] *(i - 1));
			Tile l = grid.getTile(s.getXPlace() - 1 + dir[0] *(i - 1), s.getYPlace() + dir[1] *(i - 1));
			
			if(TileType.Dirt == u.getType() && this.dir[1] != 1) { //Checks up
				direction.add('U');
			} if (TileType.Dirt == r.getType() && this.dir[0] != -1) { //Checks right
				direction.add('R');
			} if (TileType.Dirt == d.getType() && this.dir[1] != -1) { //Checks below
				direction.add('D');
			} if (TileType.Dirt == l.getType() && this.dir[0] != 1) { //Checks left
				direction.add('L');
			}
			
			if (direction.size() > 1) {
				found = true;
				i--;
				next = grid.getTile(s.getXPlace() + dir[0] *i, 
						s.getYPlace() + dir[1] *i);
			} else if(s.getXPlace() + dir[0] * i == grid.getTilesWide() ||
					s.getYPlace() + dir[1] * i == grid.getTilesHigh() ||
					TileType.Dirt != 
					grid.getTile(s.getXPlace() + dir[0] * i, 
							s.getYPlace() + dir[1] * i).getType()) {
				//Checkpoint is found.
				found = true;
				--i;
				next = grid.getTile(s.getXPlace() + dir[0] * i, 
						s.getYPlace() + dir[1] * i);
				
			}
			// Sets the checkpoint to the tile before the tile changes.
			c = new Checkpoint(next, dir[0], dir[1]);
		}
		
		return c;
	}
}
