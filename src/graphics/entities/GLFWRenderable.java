package graphics.entities;

import tensor.Matrix4;

public interface GLFWRenderable {
	
	/**
	 * Update the graphics characteristics of this object, i.e. the model matrix
	 */
	void update();
	
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
