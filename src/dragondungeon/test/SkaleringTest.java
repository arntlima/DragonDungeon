package dragondungeon.test;

import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SkaleringTest extends BasicGame {
	
	public final static int SCREEN_WIDTH = 960;
	public final static int SCREEN_HEIGHT = 768;
	
	public static final float RENDERING_CONST = 1.05f;
	public static final float RAMME_BREDDE = 68;
	
	float scale = 1, x, y;
	LinkedList<Image> images;

	public SkaleringTest() {
		super("Skaleringstest");
		
	}

	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		float lokalScale = scale;
		
		for(Image img : images  ) {
			
			
			
			x= SCREEN_WIDTH/2 - SCREEN_WIDTH/2*lokalScale;//*RENDERING_CONST;
			y= SCREEN_HEIGHT/2 - SCREEN_HEIGHT/2*lokalScale;//*RENDERING_CONST;
			
			
			img.draw(x, y, lokalScale);
			
			//regn ut for neste iterasjon
			lokalScale = lokalScale - 2 * RAMME_BREDDE * lokalScale / SCREEN_WIDTH;		
		}
		
		
		
		
	}

	public void init(GameContainer arg0) throws SlickException {
		images = new LinkedList<Image>();
		for(int i = 0; i < 20; i++)
			images.add(new Image("frame.png"));
		
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		//System.out.println(delta);
		//scale *= RENDERING_CONST + scale*(delta/17);
		
		scale = scale * RENDERING_CONST * (delta/17f);//*(RENDERING_CONST-1);
		
		if(scale < 1) scale = 1;
		
		//System.out.println((delta/17f));
		//System.out.println(scale);
		if(scale > 1.17f) {
			scale = 1;
			
			images.removeFirst();
			images.add(new Image("frame.png"));
		}
		
		
		

		
		
		//x= SCREEN_WIDTH/2 - SCREEN_WIDTH/2*scale;
		//y= SCREEN_HEIGHT/2 - SCREEN_HEIGHT/2*scale;
		
		
		
		
	}
	
	
	 public static void main(String[] args)
	            throws SlickException
	    {
	         AppGameContainer app =
	            new AppGameContainer( new SkaleringTest() );
	  
	         app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
	         app.start();
	    }
	
	
	
}