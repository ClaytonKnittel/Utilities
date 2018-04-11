package graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import files.FileManager;
import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;
import tensor.Matrix4;

public class Renderer {
	
	private ShaderProgram program;
	
	private Shader vertexShader;
	private Shader fragmentShader;
	
	private static int uniModel;
	
	private static final String defaultVertexShader, defaultFragmentShader;
	
	private static final float FOV = 72;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	public int pos, color;
	
	static {
		defaultVertexShader = "/Users/claytonknittel/git/Utilities/src/graphics/shaders/vertexShader";
		defaultFragmentShader = "/Users/claytonknittel/git/Utilities/src/graphics/shaders/fragmentShader";
	}
	
	public Renderer() {
	}
	
	public void enter(Iterable<State> states) {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		vertexShader = Shader.createShader(GL_VERTEX_SHADER, fileToStr(defaultVertexShader));
		fragmentShader = Shader.createShader(GL_FRAGMENT_SHADER, fileToStr(defaultFragmentShader));
		
		program = initiateProgram(vertexShader, fragmentShader);
		
		uniModel = program.getUniformLocation("model");
		
		configureAttributeLocations();
		
		for (State s : states)
			s.enter(program, pos, color);
		
		setViewMatrix();
		setProjectionMatrix();
	}
	
	public void update(Iterable<State> states, float delta) {
		for (State s : states)
			s.update(delta);
	}
	
	public void render(Iterable<State> states, float alpha) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		program.use();
		for (State s : states)
			s.render(program, uniModel, alpha);
	}
	
	public ShaderProgram initiateProgram(Shader vertexShader, Shader fragmentShader) {
		ShaderProgram program = new ShaderProgram();
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);
		program.bindFragmentDataLocation(0, "fragColor");
		program.link();
		program.use();
		return program;
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
		program.delete();
	}

	/**
	 * Specifies the vertex attributes.
	 */
	private void configureAttributeLocations() {
		/* Specify Vertex Pointer */
		pos = program.getAttributeLocation("pos");
//		program.enableVertexAttribute(posAttrib);
//		program.pointVertexAttribute(posAttrib, 3, 6 * Float.BYTES, 0);

		/* Specify Color Pointer */
		color = program.getAttributeLocation("color");
//		program.enableVertexAttribute(colAttrib);
//		program.pointVertexAttribute(colAttrib, 3, 6 * Float.BYTES, 3 * Float.BYTES);
	}
	
	public void setViewMatrix() {
		Matrix4 view = new Matrix4();
		int uniView = program.getUniformLocation("view");
		program.setUniform(uniView, view);
	}
	
	public void setProjectionMatrix() {
		float ratio;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			long window = GLFW.glfwGetCurrentContext();
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			GLFW.glfwGetFramebufferSize(window, width, height);
			ratio = width.get() / (float) height.get();
		}
		
		Matrix4 projection = Matrix4.perspective(FOV, ratio, NEAR_PLANE, FAR_PLANE);
		int uniProjection = program.getUniformLocation("projection");
		program.setUniform(uniProjection, projection);
	}
	
}
