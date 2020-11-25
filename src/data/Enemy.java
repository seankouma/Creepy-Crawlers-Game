package data;

import org.newdawn.slick.opengl.Texture;

import static helpers.Artist.*;
import static helpers.Clock.*;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {
	private int width, height, health;
	private float speed, x, y;
	private Texture texture;
	private Tile startTile;
	private boolean first = true, alive = true;;
	private TileGrid grid;
	private int[] directions;
	private int[] nextDir;
	

	private Checkpoint current;
	private Wave parent;
	
	public Enemy(Texture tex, Tile startTile, TileGrid grid, int width, int height, float speed) {
		this.texture = tex;
		this.startTile = startTile;
		this.x = startTile.getX();
		this.y = startTile.getY();
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.grid = grid;
		this.setDirections(new int[2]);
		
		//X direction
		this.getDirections()[0] = 0;
		//Y direction
		this.getDirections()[1] = 0;
		setDirections(FindNextD(startTile));
	}
	
	public Enemy(Texture tex, Tile startTile, TileGrid grid, int width, int height, float speed, Wave parent) {
		this.texture = tex;
		this.startTile = startTile;
		this.x = startTile.getX();
		this.y = startTile.getY();
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.grid = grid;
		this.setDirections(new int[2]);
		this.parent = parent;
		
		//X direction
		this.getDirections()[0] = 0;
		//Y direction
		this.getDirections()[1] = 0;
		setDirections(FindNextD(startTile));
	}
	
	//Needs to be fixed/debugged. Current enemies just travel around the corners of the map
	public void Update() {
		if (first) {
			first = false;
			current = this.FindNextC(grid.getTile(((int) Math.floor(this.getX() / 64)), 
					((int) Math.floor(this.getY() / 64) - 1)), getDirections());
			setDirections(this.FindNextD(grid.getTile( (int) Math.floor(this.getX() / 64), 
					(int) Math.floor((this.getY()) / 64))));
		} else {
			if (grid.getTile( (int) Math.floor(this.getX() / 64) - subtract(getDirections()[0], 'x'), (int) Math.floor((this.getY()) / 64) - subtract(getDirections()[1], 'y')) == current.getTile()) {
				setDirections(FindNextD(grid.getTile( (int) Math.floor(this.getX() / 64) - subtract(getDirections()[0], 'x'), (int) Math.floor((this.getY()) / 64) - subtract(getDirections()[1], 'y'))));			
				this.y += 1;
				
				// If the tile the enemy is on is the next checkpoint, set the enemy's position to the exact checkpoint tile location
				x = current.getTile().getX();
				y = current.getTile().getY();
				
				current = this.FindNextC(grid.getTile( (int) Math.floor(this.getX() / 64) + getDirections()[0], (int) Math.floor((this.getY()) / 64) + getDirections()[1]), getDirections());
			}
				x += Delta() * getDirections()[0] * speed;
				y += Delta() * getDirections()[1] * speed;
		}
	}
	
	//grid.getTile( (int) Math.floor(this.getX() / 64), (int) Math.floor((HEIGHT - this.getY() - 1) / 64))
	//grid.getTile( (int) Math.floor(x / 64), (int) Math.floor(y / 64))
	
	
	public int subtract(int a, char b) {
		if (a == 1) {
			return 0;
		} else {
			return a;
		}
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
			
			if(TileType.Dirt == u.getType() && this.getDirections()[1] != 1) { //Checks up
				direction.add('U');
			} if (TileType.Dirt == r.getType() && this.getDirections()[0] != -1) { //Checks right
				direction.add('R');
			} if (TileType.Dirt == d.getType() && this.getDirections()[1] != -1) { //Checks below
				direction.add('D');
			} if (TileType.Dirt == l.getType() && this.getDirections()[0] != 1) { //Checks left
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
	
	private int[] FindNextD(Tile s) {
		if (this.parent.getHead() != null) {
			if (!this.parent.getHead().equals(this)) {
				int[] dir = this.parent.getHead().getDirections();
				return dir;
			}
		}
		if (nextDir != null) {
			int[] nextDirs = {nextDir[0], nextDir[1]};
			return isValidDir(nextDir, s) ? nextDirs : new int[2];
		}
			int[] dir = new int[2];
			ArrayList<Character> direction = new ArrayList<Character>();
			Tile u = grid.getTile(s.getXPlace(), s.getYPlace() - 1);
			Tile r = grid.getTile(s.getXPlace() + 1, s.getYPlace());
			Tile d = grid.getTile(s.getXPlace(), s.getYPlace() + 1);
			Tile l = grid.getTile(s.getXPlace() - 1, s.getYPlace());
			
			if(TileType.Dirt == u.getType() && this.getDirections()[1] != 1) { //Checks up
				direction.add('U');
			} if (TileType.Dirt == r.getType() && this.getDirections()[0] != -1) { //Checks right
				direction.add('R');
			} if (TileType.Dirt == d.getType() && this.getDirections()[1] != -1) { //Checks below
				direction.add('D');
			} if (TileType.Dirt == l.getType() && this.getDirections()[0] != 1) { //Checks left
				direction.add('L');
			} if (direction.isEmpty()) {
				direction.add('B');
			}
			
			Random rand = new Random();
			int rand1 = rand.nextInt(direction.size());
			char dir1 = direction.get(rand1);
			switch (dir1) {
				case 'U':
					dir[0] = 0;
					dir[1] = -1;
					break;
				case 'R':
					dir[0] = 1;
					dir[1] = 0;
					break;
				case 'D':
					dir[0] = 0;
					dir[1] = 1;
					break;
				case 'L':
					dir[0] = -1;
					dir[1] = 0;
					break;
				case 'B':
					dir[0] = 2;
					dir[1] = 2;
					break;
			}
			
			return dir;
		}
	private boolean isValidDir(int[] dir, Tile s) {
		Tile u = grid.getTile(s.getXPlace(), s.getYPlace() - 1);
		Tile r = grid.getTile(s.getXPlace() + 1, s.getYPlace());
		Tile d = grid.getTile(s.getXPlace(), s.getYPlace() + 1);
		Tile l = grid.getTile(s.getXPlace() - 1, s.getYPlace());
		
		if (TileType.Dirt == u.getType() && this.getDirections()[1] != 1) { //Checks up
			if (dir[0] == 0 && dir[1] == -1) {
				return true;
			}
		} if (TileType.Dirt == r.getType() && this.getDirections()[0] != -1) { //Checks right
			if (dir[0] == 1 && dir[1] == 0) {
				return true;
			}
		} if (TileType.Dirt == d.getType() && this.getDirections()[1] != -1) { //Checks below
			if (dir[0] == 0 && dir[1] == 1) {
				return true;
			}
		} if (TileType.Dirt == l.getType() && this.getDirections()[0] != 1) { //Checks left
			if (dir[0] == -1 && dir[1] == 0) {
				return true;
			}
		}
		return false;
	}
	
	public static Enemy quickLoad(Wave wave, TileGrid grid) {
		return new Enemy(QuickLoad("UFO"), grid.getTile(18, 14), grid, 64, 64, 14, wave);
	}
	
	public static Enemy quickLoad(Wave wave, TileGrid grid, Tile startTile) {
		return new Enemy(QuickLoad("UFO"), startTile, grid, 64, 64, 14, wave);
	}
	
	private void Die() {
		alive = false;
	}
	
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void Draw() {
		DrawQuadTex(texture, x, y, width, height);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
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

	public Texture getTexture() {
		return texture;
	}
	
	public TileGrid getTileGrid() {
		return grid;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Tile getStartTile() {
		return startTile;
	}

	public void setStartTile(Tile startTile) {
		this.startTile = startTile;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}
	public int[] getNextDir() {
		return nextDir;
	}

	public void setNextDir(int[] nextDir) {
		this.nextDir = nextDir;
	}

	int[] getDirections() {
		return directions;
	}

	public void setDirections(int[] directions) {
		this.directions = directions;
	}
	
	public void setParent(Wave wave) {
		this.parent = wave;
	}
	
	
}
