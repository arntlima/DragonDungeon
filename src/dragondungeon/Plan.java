package dragondungeon;

public class Plan {
	
	private short[][] hindringer;
	private float x, y, localScale;
	
	public Plan(short[][] hindringer) {
		//TODO: sjekk dimensjoner
		
		this.hindringer = hindringer;
	}
	
	public Plan(boolean harHindringer) {
		if(harHindringer)
			hindringer = new short[4][5];
	}
	
	public boolean harHindringer() {
		return hindringer != null;
	}
	
	public short getHindring(int x, int y) {
		return hindringer[y][x];
	}
	
	//TODO:: skal sikkert fjernes
	public void setHindring(int x, int y, short verdi) {
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
