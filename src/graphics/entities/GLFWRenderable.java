package graphics.entities;

import tensor.Matrix4;

public interface GLFWRenderable extends Updatable {
	

	/**
	 * @return the model matrix. Will be the first operator to be performed on the data to transform it into the right shape / size
	 */
	Matrix4 model();
	
	/**
	 * @return the model data for this shape, in compliance with whatever rendering method is being used
	 */
	float[] modelData();
	
	float reflectivity();
	float shineDamper();
	
	void setLightAttribs(float reflectivity, float shineDamper);
	
	String texture();
	
}
