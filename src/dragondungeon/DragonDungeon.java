package dragondungeon;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ListIterator;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


public class DragonDungeon extends BasicGame {
	
	public final static int SCREEN_WIDTH = 960;
	public final static int SCREEN_HEIGHT = 768;
	public final static float RENDERING_CONST_HIGH = 1.03f;
	public final static float RENDERING_CONST_LOW = 1.01f;
	public final static float PLAYER_SCALE_CONSTANT = 0.2f;

	
	public final static int RUTERX = 5;
	public final static int RUTERY = 4;

	private static final int KOLLISJON_AVSTAND = 50;
	private static final int KOLLISJON_AVSTAND_HINDRING = 192;
	private static final int OVRE_STRAFFGRENSE = 4000;
	
	private static final float ELLIPSE_HOYDE = 270 * PLAYER_SCALE_CONSTANT;
	private static final float ELLIPSE_BREDDE = 446 * PLAYER_SCALE_CONSTANT;
	
	private PlanSamling planSamling;
	private float scale = 1;
	private float renderingRatio;
	
	private Player spiller;
	private Animation spillerAnimasjon;
	private Image spillerGliding;
	private Animation stovSky;
	
	private String themeLoctation = "dragontheme";
	
	private DDInput ddInput;
	
	private int straff;
	private int remainingDustTime;
	private int dustDuration;
	private int straffTimer;
	
	private Shape hindringForm;
	private Ellipse spillerFrom;
	private boolean pause;
	
	private Graphics g;
	private Shape planRektangel;
	
	
	

	public DragonDungeon() {
		super("DragonDungeon");
		
		//ddInput = new KeyboardInput();
		ddInput = new comPortJoystick();
		renderingRatio = RENDERING_CONST_HIGH;
		
		
		try {
			planSamling = LevelReader.getLevel("level1.lvl", SCREEN_WIDTH, SCREEN_HEIGHT);
			
						
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public void render(GameContainer arg0, Graphics g) throws SlickException {
		this.g = g;
		
		
		ListIterator<Plan> iterator = planSamling.listIterator();
		
		/*
		if(hindringForm != null && spillerFrom != null) {
			g.draw(hindringForm);
			g.draw(spillerFrom);
			g.draw(planRektangel);
			
			
		//	pause = true;
			
		}
		*/
		
		
		int teller = 0;
		while(iterator.hasNext() && teller <= 20) {
			Plan plan = iterator.next();
			float lokalScale  = plan.getLocalScale();
			
			//tegn ramme
			new Image("frame.png").draw(plan.getX(), plan.getY(), lokalScale);
			
			teller++;
						
		}
		
		do {
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
							
							
							
							
							
							//felsjekktegning
							//float hindringSX = (x + 0.5f)* SCREEN_WIDTH / RUTERX;
							//float hindringSY = (y + 0.5f)* SCREEN_HEIGHT / RUTERY;
							
							//g.setColor(Color.gray);
							//g.fillOval(spiller.getGlobalSenterX()-ELLIPSE_BREDDE, spiller.getGlobalSenterY()-ELLIPSE_HOYDE, ELLIPSE_BREDDE*2, ELLIPSE_HOYDE*2);
							
							
							//g.setColor(Color.white);
							
							
							
							
							//tegn hindringen
							hindring.draw(plan.getX()+x0, plan.getY()+y0, lokalScale);		
							
							
							//g.fillOval(hindringSX-4, hindringSY-4, 8, 8);
							
							
							
							
						}
						

					}
				}
			}
			if(teller == 3) {
				drawPlayer();
				
				
				
				
				//g.drawOval(spiller.getGlobalSenterX()-4, spiller.getGlobalSenterY()-4, 8, 8);
				
				
			}
			teller--;
		} while(iterator.hasPrevious());
		
		
		
		
		
		
	}
	
	private void drawPlayer() {
		//teng spiller
		spillerAnimasjon.draw(spiller.getX(), spiller.getY());
		
		
				
		if(remainingDustTime > 0) {
			//stovSky.draw(spiller.getX()+spiller.getLokalSenterX()/2-10,spiller.getY()+spiller.getLokalSenterY()/2);
			stovSky.draw(spiller.getX()-20,spiller.getY());
		}
	}

	public void init(GameContainer arg0) throws SlickException {
		
		//kalibrer innputt
		ddInput.kalibrer();
		
		//last spilleranimasjon
		spillerGliding = new Image("images/"+themeLoctation+"/player_texture/frame-000001.png").getScaledCopy(0.2f);
		
		File mappeDrage = new File("images/"+themeLoctation+"/player_texture/");
		
		
		//anta 30 frames som standard
		spillerAnimasjon = new Animation(getImeages(mappeDrage, PLAYER_SCALE_CONSTANT), 33);
		spillerAnimasjon.setLooping(true);
		
		spiller = new Player(spillerGliding.getWidth(), spillerGliding.getHeight());
		spiller.setX(SCREEN_WIDTH/2);
		spiller.setY(SCREEN_HEIGHT/2);
		
		
		File mappeStovsky = new File("images/"+themeLoctation+"/dust/");
		stovSky = new Animation(getImeages(mappeStovsky, PLAYER_SCALE_CONSTANT), 33);
		dustDuration = stovSky.getDuration(0)*stovSky.getFrameCount();
		//stovSky.setLooping(false);
		
		
	}

	public void update(GameContainer arg0, int delta) throws SlickException {
		if(pause) return;
		
		updateScale(delta);
		
		updatePlayerSpeed();
		
		updatePlayerPos();
		
		enforceStraff(delta);
		
		sjekkKollisjonRamme(delta);
			
		
	}
	
	private void enforceStraff(int delta) {
		
		if(straffTimer > OVRE_STRAFFGRENSE)
			straffTimer = OVRE_STRAFFGRENSE;
		
		if(straffTimer > 0) {
			straffTimer -= delta;
			renderingRatio = RENDERING_CONST_LOW;
			//System.out.println(straffTimer);
		} else
			renderingRatio = RENDERING_CONST_HIGH;
			
	}
	
	private Image[] getImeages(File mappe, float scale) throws SlickException {
		String[] filer = mappe.list();
		Arrays.sort(filer);
		
		Image[] bilder = new Image[filer.length];
		for(int i = 0; i < filer.length; i++) {
			bilder[i] = new Image(mappe.getAbsolutePath()+"/"+filer[i]).getScaledCopy(scale);
			//System.out.println(filer[i]);
		}
		
		return bilder;
	}
	
	private void updatePlayerSpeed() {
		
		spiller.setSpeedX(ddInput.getSpeedX());
		spiller.setSpeedY(ddInput.getSpeedY());
	}
	
	private void updatePlayerPos() {
		spiller.setX(spiller.getX() + spiller.getSpeedX());
		spiller.setY(spiller.getY() + spiller.getSpeedY());
		
	}
	
	private void sjekkKollisjonHindring(int delta) {
		
		
		
		
		Plan plan = planSamling.getPlan(3);
		
		
		
		if(!plan.harHindringer()) return;
		
		
		
		float lokalScale = plan.getLocalScale();
		
		planRektangel = new Rectangle(plan.getX(), plan.getY(), SCREEN_WIDTH*lokalScale, SCREEN_HEIGHT*lokalScale);
		
		
		
		
		spillerFrom = new Ellipse(spiller.getGlobalSenterX(), spiller.getGlobalSenterY(), ELLIPSE_BREDDE, ELLIPSE_HOYDE);
		
		for(int y = 0; y < RUTERY; y++) {
			for(int x = 0; x < RUTERX; x++) {
				if(plan.getHindring(x, y) != 'o') {
				
					float x0 =  x * SCREEN_WIDTH/RUTERX * lokalScale;
					float y0 = y * SCREEN_HEIGHT/RUTERY * lokalScale;
				
					hindringForm = new Rectangle(plan.getX()+x0, plan.getY()+y0, 192*lokalScale, 192*lokalScale);
				
					
					if(spillerFrom.intersects(hindringForm)) {
						krasj();
						System.out.println("Krasjhindring");
					}
					
				}
			}
		}
		
		
		
		
	}
	
	private void krasj() {
		straff++;
		remainingDustTime = dustDuration;
		straffTimer += 1000;
		
		try {
			Sound fx = new Sound("lyd/krasj.wav");
			fx.play();
			
		} catch (SlickException e) {
			System.err.println("Feil med lyd: !!!");
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	private void sjekkKollisjonRamme(int delta) {
		
		if(remainingDustTime > 0)
			remainingDustTime  -= delta;
		
		
		//sjekk kollisjon med veggen
		
		if(spiller.getGlobalSenterX() < KOLLISJON_AVSTAND) {
			spiller.setX( KOLLISJON_AVSTAND - spiller.getLokalSenterX() + 2);
			krasj();
			
		} else if(spiller.getGlobalSenterX() > SCREEN_WIDTH - KOLLISJON_AVSTAND) {
			spiller.setX( SCREEN_WIDTH - KOLLISJON_AVSTAND - spiller.getLokalSenterX() - 2);
			krasj();
			
		}
		
		
		if(spiller.getGlobalSenterY() < KOLLISJON_AVSTAND) {
			spiller.setY( KOLLISJON_AVSTAND - spiller.getLokalSenterY() + 2);
			krasj();
			
		} if(spiller.getGlobalSenterY() > SCREEN_HEIGHT - KOLLISJON_AVSTAND) {
			spiller.setY( SCREEN_HEIGHT - KOLLISJON_AVSTAND - spiller.getLokalSenterY() - 2);
			krasj();
			
		}
	}
	
	
	private void updateScale(int delta) {
		scale = scale * renderingRatio;// * (delta/17f);
		
		if(scale < 1) {
			scale = 1; //ikke gå i revers
			System.out.println("Prvde å gå i revers");
		}
		
		if(scale > 1.17f) {
			scale = 1;
			
			
			
			
			sjekkKollisjonHindring(delta);
			planSamling.endOfPlan();
			
			
			
		}
		
		planSamling.setScale(scale);
	}

	public static void main(String[] args)
            throws SlickException {
         AppGameContainer app =
            new AppGameContainer( new DragonDungeon() );
  
         app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
         app.start();
    }

}
