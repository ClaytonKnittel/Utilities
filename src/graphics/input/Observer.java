package graphics.input;

import graphics.entities.Updatable;
import tensor.Vector;

public interface Observer extends Updatable {
	
	Vector pos();
	
	/**
	 * Describes which orientation the object is facing in the x-y plane.
	 * This is the first rotation to be applied in the transformation of the world.
	 * @return the Euler angle phi
	 */
	float phi();
	
	/**
	 * Describes the objects up-down orientation. This is the second rotation
	 * to be applied in the transformation of the world. 
	 * @return the Euler angle theta
	 */
	float theta();
	
	/**
	 * Describes the orientation about the axis going through the view frustum. Is
	 * analagous to twisting one's head about a vector passing out through their nose.
	 * This is the third transformation to be applied to the world.
	 * @return the Euler angle psi
	 */
	float psi();
	
}
