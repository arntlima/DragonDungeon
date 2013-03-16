package dragondungeon;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.io.FileReader;



public class LevelReader {

	public static PlanSamling getLevel(String filename, int levelWidth, int levelHeight) throws IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		PlanSamling planSamling = new PlanSamling(levelWidth, levelHeight);
		
		while ( in.ready() ) {
			 String linje = in.readLine();
			
			 if(linje.isEmpty()) continue;
			 
			 if(linje.matches("[0-9]*")) {
				 
				 int antall = Integer.parseInt(linje);
				 
				 for(int i = 0; i < antall; i++)
					 planSamling.addPlan(new Plan(false));
				 
			 } else {
				 String[] planData = new String[4];
				 planData[0] = linje;
				 
				 for(int i = 1; i < 4; i++)
					 planData[i] = in.readLine();
					 
				 
				planSamling.addPlan(getPlanWithHindringer(planData)); 
				
			 }
			
			
			
		}
		
		in.close();
		
		
		
		return planSamling;
		
	}
	
	
	private static Plan getPlanWithHindringer(String[] input) {
		
		assert input.length == 4; 
		
		short[][] data = new short[4][5];
		
		for(int y = 0; y < input.length; y++) {
			StringTokenizer st = new StringTokenizer(input[y], " ");
			
			for(int x = 0; x < 5; x++) {
				String tuppel = st.nextToken();
				
				data[y][x] = (short)tuppel.charAt(0);
				
				if(tuppel.length() == 2)		
						data[y][x] += hashRotation(tuppel.charAt(1)); 
				 
			}
			
		}
		
		
		return new Plan(data);
	}
	
	private static short hashRotation(char rotation) {
		switch (rotation) {
		case '0':
			return 0;
		case '1':
			return 1000;
		case '2':
			return 2000;
		case '3':
			return 3000;

		}
		
		return 0;
	}
	
}
