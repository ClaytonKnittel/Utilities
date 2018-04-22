package graphics;

import graphics.entities.GLFWRenderable;
import graphics.renderers.AbstractRenderer;
import graphics.renderers.InputVariable;
import graphics.shaders.ShaderProgram;
import tensor.Matrix4;

public class RigidState extends Entity {
	
	private GLFWRenderable owner;
	
	// Keep a reference to the GLFWRenderable's model matrix
	private Matrix4 model;
	
	// total number of attributes, usually 3 for position, 3 for color, etc
	private int numAttributes;
	
	protected RigidState(GLFWRenderable r) {
		super(r.modelData(), r.texture());
		this.owner = r;
	}
	
	public void update() {
		owner.update();
		model = owner.model();
	}
	
	@Override
	public void setUniforms(ShaderProgram program, int uniModel, int uniReflectivity, int uniShineDamper) {
		program.setUniform(uniModel, model);
		program.setUniform(uniReflectivity, owner.reflectivity());
		program.setUniform(uniShineDamper, owner.shineDamper());
	}
	
	@Override
	public void specifyVertexAttributes(AbstractRenderer r) {
		ShaderProgram p = r.program();
		
		int offset = 0;
		
		for (InputVariable i : r.inputs()) {
			p.enableVertexAttribute(i.id());
			p.pointVertexAttribute(i.id(), i.size(), numAttributes * Float.BYTES, Float.BYTES * offset);
			offset += i.size();
		}
	}
	
	@Override
	public int numAttributes() {
		return numAttributes;
	}
	
	@Override
	public void init(AbstractRenderer r) {
		// calculate the stride
		for (InputVariable i : r.inputs())
			numAttributes += i.size();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!GLFWRenderable.class.isAssignableFrom(o.getClass()))
			return false;
		GLFWRenderable r = (GLFWRenderable) o;
		return r == owner;
	}

}
