package dragondungeon;

public class Plan {
	
	private char[][] hindringer;
	private float x, y, localScale;
	
	public Plan(char[][] hindringer) {
		//TODO: sjekk dimensjoner
		
		this.hindringer = hindringer;
	}
	
	public Plan(boolean harHindringer) {
		if(harHindringer)
			hindringer = new char[4][5];
	}
	
	public boolean harHindringer() {
		return hindringer != null;
	}
	
	public char getHindring(int x, int y) {
		return hindringer[y][x];
	}
	
	//TODO:: skal sikkert fjernes
	public void setHindring(int x, int y, char verdi) {
		hindringer[y][x] = verdi;
	}

	public float getLocalScale() {
		return localScale;
	}

	public void setLocalScale(float localScale) {
		this.localScale = localScale;
	}
	

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	

}
