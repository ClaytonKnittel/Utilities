package graphics.renderers.skybox;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

import files.FileManager;
import graphics.entities.GLFWRenderable;
import graphics.entities.VertexArrayObject;
import graphics.entities.VertexBufferObject;
import graphics.input.Observer;
import graphics.renderers.AbstractRenderer;
import graphics.renderers.InputVariable;
import graphics.renderers.Renderer;
import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;
import graphics.shaders.Texture;
import tensor.Matrix4;

public class SkyboxRenderer extends AbstractRenderer {

	private static final String VERTEX_FILE = "/Users/claytonknittel/git/Utilities/src/graphics/renderers/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "/Users/claytonknittel/git/Utilities/src/graphics/renderers/skybox/skyboxFragmentShader.txt";
	
	private SkyboxShader shader;
	
	private VertexArrayObject vao;
	private VertexBufferObject vbo;
	
	private Texture texture;
	
	private int numVertices;
	
	private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static final String[] locs = {
			Texture.path + "purplenebula_right",
			Texture.path + "purplenebula_left",
			Texture.path + "purplenebula_up",
			Texture.path + "purplenebula_down",
			Texture.path + "purplenebula_back",
			Texture.path + "purplenebula_front"};
	
	public SkyboxRenderer(Matrix4 projectionMatrix) {
		super(VERTEX_FILE, FRAGMENT_FILE, false);
		shader = new SkyboxShader();
		shader.loadProjectionMatrix(projectionMatrix);
	}
	
	public void loadUniformVariables(ShaderProgram program) {
		
	}
	
	public void setProjectionMatrix(ShaderProgram program) {
		
	}
	
	protected void setViewMatrix(Observer observer) {
		
	}
	
	public void render() {
		
	}
	
//	public SkyboxShader initiateProgram(Shader vertexShader, Shader fragmentShader) {
//		SkyboxShader program = new SkyboxShader();
//		program.attachShader(vertexShader);
//		program.attachShader(fragmentShader);
//		program.bindFragmentDataLocation(0, "fragColor");
//		program.link();
//		program.use();
//		return program;
//	}

	public void enter() {
		vao = new VertexArrayObject();
		vao.bind();
		
		instantiateVBO(FloatBuffer.wrap(VERTICES));
		
		specifyVertexAttributes();
	}
	
	private void specifyVertexAttributes() {
		ShaderProgram p = program();
		
		int id = p.getAttributeLocation("pos");
		
		p.enableVertexAttribute(id);
		p.pointVertexAttribute(id, 3 * Float.BYTES, 3 * Float.BYTES, 0);
	}
	
	private void instantiateVBO(FloatBuffer buffer) {
		FloatBuffer vertices;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			vertices = stack.mallocFloat(buffer.limit());
			for (int i = 0; i < vertices.capacity(); i++) {
				vertices.put(buffer.get());
			}
			numVertices = buffer.limit() / 3;
			
			vertices.flip();
			
			vbo = new VertexBufferObject();
			vbo.bind(GL_ARRAY_BUFFER);
			vbo.uploadData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
			
			vertices.clear();
		}
		buffer.clear();
	}

}
