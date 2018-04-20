package graphics;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

import graphics.entities.VertexArrayObject;
import graphics.entities.VertexBufferObject;
import graphics.renderers.Renderer;
import graphics.shaders.ShaderProgram;
import graphics.shaders.Texture;

public abstract class Entity implements graphics.State {
	
	private Texture texture;
	
	private FloatBuffer shapeData;

	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	
	private int numVertices;
	
	public Entity(float[] shapeData, String texture) {
		this.shapeData = FloatBuffer.wrap(shapeData);
		this.texture = Texture.loadTexture(Texture.path + texture + ".jpg");
	}
	
	@Override
	public final void render(ShaderProgram program, int uniModel, int uniReflectivity, int uniShineDamper) {
		vao.bind();
		setUniforms(program, uniModel, uniReflectivity, uniShineDamper);
		
		glActiveTexture(GL_TEXTURE0);
		texture.bind();
		
		glDrawArrays(GL_TRIANGLES, 0, numVertices);
	}
	
	public abstract void setUniforms(ShaderProgram program, int uniModel, int uniReflectivity, int uniShineDamper);
	
	public abstract int numAttributes();
	
	public abstract void init(Renderer r);
	
	public abstract void specifyVertexAttributes(Renderer r);
	
	private void instantiateVBO(FloatBuffer buffer) {
		FloatBuffer vertices;
		try {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				vertices = stack.mallocFloat(buffer.limit());
				for (int i = 0; i < vertices.capacity(); i++) {
					vertices.put(buffer.get());
				}
				numVertices = buffer.limit() / numAttributes();
				
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

	@Override
	public void enter(Renderer r) {
		vao = new VertexArrayObject();
		vao.bind();
		init(r);
		instantiateVBO(shapeData);
		specifyVertexAttributes(r);
	}

	public void exit() {
		vao.delete();
		vbo.delete();
		texture.delete();
	}
	
}
