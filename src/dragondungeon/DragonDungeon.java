package dragondungeon;


import java.util.ListIterator;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class DragonDungeon extends BasicGame {
	
	public final static int SCREEN_WIDTH = 960;
	public final static int SCREEN_HEIGHT = 768;
	
	public static final float RENDERING_CONST = 1.05f;
	
	private PlanSamling planSamling;
	private float scale = 1;
	

	public DragonDungeon() {
		super("DragonDungeon");
		
		planSamling = new PlanSamling(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		for(int i = 0; i < 100; i ++) {
			
			Plan plan;
			if(i % 10 == 0) {
				plan = new Plan(true);
				plan.setHindring(2, 0, 'c');
				plan.setHindring(2, 1, 'c');
			}
			else plan = new Plan(false);
			planSamling.addPlan(plan);
			
		}
		
		
	}

	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		ListIterator<Plan> iterator = planSamling.listIterator();
		
		while(iterator.hasNext()) {
			Plan plan = iterator.next();
			float lokalScale  = plan.getLocalScale();
			
			//legg til ramme
			new Image("frame.png").draw(plan.getX(), plan.getY(), lokalScale);
						
		}
		
		while(iterator.hasPrevious()) {
			Plan plan = iterator.previous();
			float lokalScale  = plan.getLocalScale();
			
			
			if(plan.harHindringer()) {
				for(int x = 0; x < 5; x++) {
					for(int y = 0; y < 4; y++) {

						
						switch(plan.getHindring(x, y)) {
						case 0:
							break;
							
						case 'c':
							float x0 =  x * SCREEN_WIDTH/5 * lokalScale;
							float y0 = y * SCREEN_HEIGHT/4 * lokalScale;
							//new Image("c.png").draw(480, 384, lokalScale);
							new Image("c.png").draw(plan.getX()+x0, plan.getY()+y0, lokalScale);
							
							break;
						
						}
					}
				}
			}
		}
		
	}

	public void init(GameContainer arg0) throws SlickException {
		
	}

	public void update(GameContainer arg0, int delta) throws SlickException {
		
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
