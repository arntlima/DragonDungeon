package dragondungeon;

public class Player {
	private float x, y, speedX, speedY;
	private float lokalSenterX, lokalSenterY;
	
	public Player(int width, int height) {
		lokalSenterX = width / 2;
		lokalSenterY = height / 2;
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

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float seedY) {
		this.speedY = seedY;
	}
	
	public float getGlobalSenterX() {
		return x + lokalSenterX-10;
	}
	
	public float getGlobalSenterY() {
		return y + lokalSenterY+10;
	}
	
	public float getLokalSenterX() {
		return lokalSenterX-10;
	}
	
	public float getLokalSenterY() {
		return lokalSenterY+10;
	}
	
	

}
