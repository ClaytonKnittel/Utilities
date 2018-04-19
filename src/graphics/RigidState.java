package graphics;

import graphics.entities.GLFWRenderable;
import graphics.entities.VertexArrayObject;
import graphics.entities.VertexBufferObject;
import graphics.renderers.InputVariable;
import graphics.renderers.Renderer;
import graphics.shaders.ShaderProgram;
import tensor.Matrix4;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryStack;

public class RigidState implements graphics.State {
	
	private GLFWRenderable owner;
	
	private FloatBuffer shapeData;

	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	
	private int numVertices;
	
	// Keep a reference to the GLFWRenderable's model matrix
	private Matrix4 model;
	
	// total number of attributes, usually 3 for position, 4 for color, etc
	private int numAttributes;
	
	protected RigidState(GLFWRenderable r) {
		this(r.modelData());
		this.owner = r;
	}
	
	private RigidState(float[] shapeData) {
		this.shapeData = FloatBuffer.wrap(shapeData);
	}
	
	public void update() {
		owner.update();
		model = owner.model();
	}

	public void render(ShaderProgram program, int uniModel, int uniReflectivity, int uniShineDamper) {
		vao.bind();
		
		program.setUniform(uniModel, model);
		program.setUniform(uniReflectivity, owner.reflectivity());
		program.setUniform(uniShineDamper, owner.shineDamper());

		glDrawArrays(GL_TRIANGLES, 0, numVertices);
	}
	
	private void instantiateVBO(FloatBuffer buffer) {
		FloatBuffer vertices;
		try {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				vertices = stack.mallocFloat(buffer.limit());
				for (int i = 0; i < vertices.capacity(); i++) {
					vertices.put(buffer.get());
				}
				numVertices = buffer.limit() / numAttributes;
				
				vertices.flip();
				
				vbo = new VertexBufferObject();
				vbo.bind(GL_ARRAY_BUFFER);
				vbo.uploadData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
				
				vertices.clear();
			}
		} catch (java.lang.OutOfMemoryError e) {
			vbo = new VertexBufferObject();
			vbo.bind(GL_ARRAY_BUFFER);
			vbo.uploadData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		}
		buffer.clear();
	}

	public void enter(Renderer r) {
		vao = new VertexArrayObject();
		vao.bind();
		
		// calculate the stride
		for (InputVariable i : r.inputs())
			numAttributes += i.size();
		
		instantiateVBO(this.shapeData);
		
		specifyVertexAttributes(r);
	}
	
	private void specifyVertexAttributes(Renderer r) {
		ShaderProgram p = r.shaderProgram();
		
		int offset = 0;
		
		for (InputVariable i : r.inputs()) {
			p.enableVertexAttribute(i.id());
			p.pointVertexAttribute(i.id(), i.size(), numAttributes * Float.BYTES, Float.BYTES * offset);
			offset += i.size();
		}
	}

	public void exit() {
		vao.delete();
		vbo.delete();
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
