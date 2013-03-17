package dragondungeon;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


public class PlanSamling implements Iterable<Plan> {
	
	//TODO: denne må sikkert flyttes til en annen klasse
	private static final int RAMME_BREDDE = 68;
	
	private LinkedList<Plan> planListe;
	private int levelWidth, levelHeight;
	
	public PlanSamling(int levelWidth, int levelHeight) {
		this.levelHeight = levelHeight;
		this.levelWidth = levelWidth;
		
		planListe = new LinkedList<Plan>();
	}
	
	public void addPlan(Plan nyttPlan) {
		planListe.add(nyttPlan);
	}
	
	public void setScale(float scale) {
		
		float lokalScale = scale;
		
		Iterator<Plan> iterator = planListe.iterator();
			
		for(int i = 0; i < 21 && iterator().hasNext(); i++) {
			
			float x= levelWidth/2 - levelWidth/2*lokalScale;
			float y= levelHeight/2 - levelHeight/2*lokalScale;
			
			Plan plan = iterator.next();
			plan.setLocalScale(lokalScale);
			plan.setX(x);
			plan.setY(y);
			
			//System.out.println(x + "  " + y);
			
			//regn ut for neste iterasjon
			lokalScale = lokalScale - 2 * RAMME_BREDDE * lokalScale / levelWidth;		
		}
		
	}

	public Iterator<Plan> iterator() {
		return planListe.iterator();
	}

	public void endOfPlan() {
		planListe.removeFirst();
		//Plan p = planListe.removeFirst();
		//System.out.println("Sletter et plan: "+p.harHindringer());
		
	}
	
	public ListIterator<Plan> listIterator() {
		return planListe.listIterator();
	}
	
	public Plan peek() {
		return planListe.getFirst();
	}
	
	public Plan getPlan(int index) {
		return planListe.get(index);
	}

	public int size() {
		return planListe.size();
	}
	

}
