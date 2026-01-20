package Main;

public class RacingCar implements Car {
	private int speed;
	
	public RacingCar() {
		this.speed = 0;
	}
	
	@Override
	public void speedUp(int increment) {
		this.speed += increment;
	}

	@Override
	public void applyBrakes(int decrement) {
		this.speed -= decrement;
	}
	
}
