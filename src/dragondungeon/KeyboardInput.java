package dragondungeon;

import org.newdawn.slick.Input;

public class KeyboardInput implements DDInput {
	
	private float speedX, speedY;
	private Input input;
	
	public KeyboardInput() {
		input = new Input(1);
	}
	

	public void kalibrer() {
		speedX = 10f;
		speedY = 10f;
	}

	public float getSpeedX() {
		
		if(input.isKeyDown(Input.KEY_D)) {
			return speedX;
		} else if(input.isKeyDown(Input.KEY_A)) {
			return -speedX;
		}
		
		
		return 0;
	}

	public float getSpeedY() {
		
		if(input.isKeyDown(Input.KEY_W)) {
			return -speedY;
		} else if(input.isKeyDown(Input.KEY_S)) {
			return speedY;
		}
		
		
		return 0;
		
	}

}
