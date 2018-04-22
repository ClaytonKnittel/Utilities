package graphics;

import graphics.renderers.AbstractRenderer;
import graphics.shaders.ShaderProgram;

public interface State {
	
	/**
	 * Initializes the state
	 */
	void enter(AbstractRenderer r);
	
	/**
	 * Updates the location of the object / uniform variables
	 * @param delta how much time has passed since last update
	 */
	void update();
	
	/**
	 * Renders to the screen
	 * @param alpha what percentage of the way through this frame we are
	 */
	void render(ShaderProgram program, int uniModel, int uniReflectivity, int uniShineDamper);
	
	/**
	 * Closes resources associated with this state
	 */
	void exit();
	
}
