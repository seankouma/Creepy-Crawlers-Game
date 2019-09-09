package data;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.*;

import static org.lwjgl.opengl.GL11.*;

import helpers.Artist;

public class Boot {
	
	public Boot() {
		
		Artist.BeginSession();
		
		Tile tile = new Tile(0, 0, 64, 64, TileType.Grass);
		Tile tile2 = new Tile(0, 64, 64, 64, TileType.Dirt);
		while(!Display.isCloseRequested()) {
			
			tile.Draw();
			tile2.Draw();

			
			Display.update();
			Display.sync(60);
		}
	}
	
	public static void main(String[] args) {
		new Boot();
	}
}
