package graphics.entities;

import graphics.StandardState;
import graphics.shaders.Shader;
import tensor.Matrix4;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryStack;

import files.FileManager;

public class RigidState extends StandardState {
	
	private String vertexLoc, fragmentLoc;
	private FloatBuffer shapeData;

	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	private Shader vertexShader;
	private Shader fragmentShader;

	private int uniModel;
	
	public RigidState(float[] shapeData, String vertexLoc, String fragmentLoc) {
		this.shapeData = FloatBuffer.wrap(shapeData);
		this.vertexLoc = vertexLoc;
		this.fragmentLoc = fragmentLoc;
	}
	
	public void input() {

	}

	public void update(float delta) {

	}

	public void render(float alpha) {
		glClear(GL_COLOR_BUFFER_BIT);

		vao.bind();
		program.use();

		Matrix4 model = Matrix4.rotate(0, 0f, 0f, 1f);
		program.setUniform(uniModel, model);

		glDrawArrays(GL_TRIANGLES, 0, 3);
	}
	
	private void instantiateVBO(FloatBuffer buffer) {
		FloatBuffer vertices;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			/* Vertex data */
			vertices = stack.mallocFloat(3 * 6);
			for (int i = 0; i < vertices.capacity(); i++) {
				vertices.put(buffer.get());
			}
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

		vertexShader = Shader.createShader(GL_VERTEX_SHADER, fileToStr(vertexLoc));
		fragmentShader = Shader.createShader(GL_FRAGMENT_SHADER, fileToStr(fragmentLoc));
		
		program = initiateProgram(vertexShader, fragmentShader);
		
		specifyVertexAttributes();

		/* Get uniform location for the model matrix */
		uniModel = program.getUniformLocation("model");
		
		setViewMatrix();
		setOrthographicMatrix();
	}
	
	public static String fileToStr(String location) {
		try {
			return FileManager.readAll(location);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void exit() {
		vao.delete();
		vbo.delete();
		vertexShader.delete();
		fragmentShader.delete();
		program.delete();
	}

	/**
	 * Specifies the vertex attributes.
	 */
	private void specifyVertexAttributes() {
		/* Specify Vertex Pointer */
		int posAttrib = program.getAttributeLocation("pos");
		program.enableVertexAttribute(posAttrib);
		program.pointVertexAttribute(posAttrib, 3, 6 * Float.BYTES, 0);

		/* Specify Color Pointer */
		int colAttrib = program.getAttributeLocation("color");
		program.enableVertexAttribute(colAttrib);
		program.pointVertexAttribute(colAttrib, 3, 6 * Float.BYTES, 3 * Float.BYTES);
	}

}
