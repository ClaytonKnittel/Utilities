package graphics.entities;

import graphics.shaders.ShaderProgram;
import tensor.Matrix4;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryStack;

public class RigidState implements graphics.State {
	private FloatBuffer shapeData;

	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	
	private int numVertices;
	
	private float velocity;
	private float time;
	
	public RigidState(float[] shapeData, float velocity) {
		this.shapeData = FloatBuffer.wrap(shapeData);
		this.velocity = velocity;
		time = 0;
	}
	
	public void input() {
		
	}

	public void update(float delta) {
		time += delta;
	}

	public void render(ShaderProgram program, int uniModel, float alpha) {
		vao.bind();
		
		Matrix4 model = Matrix4.translate(0, 0, -time * velocity / 1000f);
		program.setUniform(uniModel, model);

		glDrawArrays(GL_TRIANGLES, 0, numVertices);
	}
	
	private void instantiateVBO(FloatBuffer buffer) {
		FloatBuffer vertices;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			/* Vertex data */
			vertices = stack.mallocFloat(buffer.limit());
			for (int i = 0; i < vertices.capacity(); i++) {
				vertices.put(buffer.get());
			}
			numVertices = buffer.limit() / 6;
			
			vertices.flip();
			
			vbo = new VertexBufferObject();
			vbo.bind(GL_ARRAY_BUFFER);
			vbo.uploadData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		}
	}

	public void enter() {
		vao = new VertexArrayObject();
		vao.bind();
		
		instantiateVBO(this.shapeData);
	}

	public void exit() {
		vao.delete();
		vbo.delete();
	}

}
