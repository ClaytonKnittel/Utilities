package graphics.entities;

import graphics.Color;
import tensor.Vector;

public interface LightSource {
	
	Vector pos();
	
	Color color();
	
	float brightness();
	
}
