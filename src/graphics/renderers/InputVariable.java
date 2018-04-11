package graphics.renderers;

/**
 * @author claytonknittel
 * 
 * Represents variables that can be input to shaders
 * 
 */
public class InputVariable {
	
	private final int id;
	
	private final String name;
	
	// How mant degrees of freedom this input variable has
	private final int dimensions;
	
	public InputVariable(int id, String name, int dimensions) {
		this.id = id;
		this.name = name;
		this.dimensions = dimensions;
	}
	
	public int id() {
		return id;
	}
	
	public String name() {
		return name;
	}
	
	public int size() {
		return dimensions;
	}
	
}
