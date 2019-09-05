package data;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import helpers.Artist;

public class Boot {
	
	public Boot() {
		
		Artist.BeginSession();
		
		float width = 50;
		float height = 50;
		float x = 100;
		float y = 100;
		
		while(!Display.isCloseRequested()) {
			
			Artist.DrawQuad(50, 50, 100, 100);
			Artist.DrawQuad(150, 150, 100, 100);
			
			Display.update();
			Display.sync(60);
		}
	}
	
	public static void main(String[] args) {
		new Boot();
	}
}
