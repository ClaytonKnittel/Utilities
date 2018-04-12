package graphics.entities;

public interface Updatable {
	
	/**
	 * called by the window before each draw to update this object's position / other graphical parameters
	 */
	void update();
	
}
