package dragondungeon;


import java.awt.SplashScreen;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.StringTokenizer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class DragonDungeon extends BasicGame {
	
	public final static int SCREEN_WIDTH = 960;
	public final static int SCREEN_HEIGHT = 768;
	
	public final static int RUTERX = 5;
	public final static int RUTERY = 4;
	
	public static final float RENDERING_CONST = 1.05f;
	private static final int KOLLISJON_AVSTAND = 50;
	
	private PlanSamling planSamling;
	private float scale = 1;
	
	private Player spiller;
	private Animation spillerAnimasjon;
	private Image spillerGliding;
	
	private String themeLoctation = "dragontheme";
	
	private DDInput ddInput;
	
	private int straff;
	

	public DragonDungeon() {
		super("DragonDungeon");
		
		ddInput = new KeyboardInput();
		
		
		try {
			planSamling = LevelReader.getLevel("level1.lvl", SCREEN_WIDTH, SCREEN_HEIGHT);
			
						
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		ListIterator<Plan> iterator = planSamling.listIterator();
		
		int teller = 0;
		while(iterator.hasNext() && teller <= 20) {
			Plan plan = iterator.next();
			float lokalScale  = plan.getLocalScale();
			
			//legg til ramme
			new Image("frame.png").draw(plan.getX(), plan.getY(), lokalScale);
			
			teller++;
						
		}
		
		while(iterator.hasPrevious()) {
			Plan plan = iterator.previous();
			float lokalScale  = plan.getLocalScale();
			
			
			if(plan.harHindringer()) {
				for(int x = 0; x < RUTERX; x++) {
					for(int y = 0; y  < RUTERY; y++) {

						
						short blokkVerdi = plan.getHindring(x, y);
						
						if(blokkVerdi != 'o') {
							float x0 =  x * SCREEN_WIDTH/RUTERX * lokalScale;
							float y0 = y * SCREEN_HEIGHT/RUTERY * lokalScale;
							
							Image hindring = new Image(
									"images/"+themeLoctation+"/hindringer/"+(char)(blokkVerdi % 1000)+".png");
							
							hindring.setRotation(blokkVerdi/1000*90);
							
							
							
							hindring.draw(plan.getX()+x0, plan.getY()+y0, lokalScale);
							
							//TODO: ROTASJON!!
						}
						

					}
				}
			}
		}
		
		//teng spiller
		spillerAnimasjon.draw(spiller.getX(), spiller.getY());
		
		
	}

	public void init(GameContainer arg0) throws SlickException {
		
		//kalibrer innput
		ddInput.kalibrer();
		
		//last spilleranimasjon
		spillerGliding = new Image("images/"+themeLoctation+"/player_texture/frame-000001.png").getScaledCopy(0.2f);
		
		File mappe = new File("images/"+themeLoctation+"/player_texture/");
		String[] filer = mappe.list();
		Arrays.sort(filer);
		
		Image[] bilder = new Image[filer.length];
		for(int i = 0; i < filer.length; i++) {
			bilder[i] = new Image(mappe.getAbsolutePath()+"/"+filer[i]).getScaledCopy(0.2f);
			//System.out.println(filer[i]);
		}
		
		//anta 30 frames som standard
		spillerAnimasjon = new Animation(bilder, 33);
		spillerAnimasjon.setLooping(true);
		
		spiller = new Player(spillerGliding.getWidth(), spillerGliding.getHeight());
		spiller.setX(SCREEN_WIDTH/2);
		spiller.setY(SCREEN_HEIGHT/2);
		
		
	}

	public void update(GameContainer arg0, int delta) throws SlickException {
		updateScale(delta);
		
		updatePlayerSpeed();
		
		updatePlayerPos();
		
		sjekkKollisjon();
		
		
	}
	
	private void updatePlayerSpeed() {
		
		spiller.setSpeedX(ddInput.getSpeedX());
		spiller.setSpeedY(ddInput.getSpeedY());
	}
	
	private void updatePlayerPos() {
		spiller.setX(spiller.getX() + spiller.getSpeedX());
		spiller.setY(spiller.getY() + spiller.getSpeedY());
		
	}
	
	
	private void sjekkKollisjon() {
		
		//sjekk kollisjon med veggen
		
		if(spiller.getGlobalSenterX() < KOLLISJON_AVSTAND) {
			spiller.setX( KOLLISJON_AVSTAND - spiller.getLokalSenterX());
			straff++;
			
		} else if(spiller.getGlobalSenterX() > SCREEN_WIDTH - KOLLISJON_AVSTAND) {
			spiller.setX( SCREEN_WIDTH - KOLLISJON_AVSTAND - spiller.getLokalSenterX());
			straff++;
			
		}
		
		
		if(spiller.getGlobalSenterY() < KOLLISJON_AVSTAND) {
			spiller.setY( KOLLISJON_AVSTAND - spiller.getLokalSenterY());
			straff++;
			
		} if(spiller.getGlobalSenterY() > SCREEN_HEIGHT - KOLLISJON_AVSTAND) {
			spiller.setY( SCREEN_HEIGHT - KOLLISJON_AVSTAND - spiller.getLokalSenterY());
			straff++;
			
		}
		
		//sjekk kollisjon med hindringer
		Plan plan = planSamling.peek();
		
		if(plan.harHindringer()) {
		
			for(int y = 0; y < RUTERY; y++) {
				for(int x = 0; x < RUTERX; x++) {
				
					if(plan.getHindring(x, y) != 'o') {
						
						float hindringGlobalX = (x + 0.5f)* SCREEN_WIDTH / RUTERX;
						float hindringGlobalY = (y + 0.5f)* SCREEN_HEIGHT / RUTERX;
						
						if(Math.abs(spiller.getGlobalSenterX() -hindringGlobalX) < KOLLISJON_AVSTAND) {
							System.out.println("Kollisjon x");
						} else if(Math.abs(spiller.getGlobalSenterY() -hindringGlobalY) < KOLLISJON_AVSTAND) {
							System.out.println("Kollisjon y");
						}
					}
				
				}
			}
		}
		
	}
	
	private void updateScale(int delta) {
		scale = scale * RENDERING_CONST * (delta/17f);
		
		if(scale < 1) scale = 1; //ikke gå i revers
		
		if(scale > 1.17f) {
			scale = 1;
			
			planSamling.endOfPlan();
			
		}
		
		planSamling.setScale(scale);
	}

	public static void main(String[] args)
            throws SlickException
    {
         AppGameContainer app =
            new AppGameContainer( new DragonDungeon() );
  
         app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
         app.start();
    }

}
