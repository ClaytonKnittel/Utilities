package graphics.entities;

import tensor.Matrix4;

public interface GLFWRenderable extends Updatable {
	
	/**
	 * 
	 * @return the current model matrix for this object
	 */
	Matrix4 model();
	
	/**
	 * 
	 * @return the model data in compliance with the format of the VBO
	 */
	float[] modelData();
}
