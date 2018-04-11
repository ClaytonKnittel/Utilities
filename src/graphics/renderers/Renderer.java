package graphics.renderers;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import files.FileManager;
import graphics.Color;
import graphics.State;
import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;
import tensor.Matrix4;
import tensor.Vector;

public class Renderer {
	
	private ShaderProgram program;
	
	private Shader vertexShader;
	private Shader fragmentShader;
	
	private static int uniModel;
	
	public static final String defaultVertexShader, defaultFragmentShader;
	
	private static final float FOV = 72;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	private InputVariable[] inputs;
	
	static {
		defaultVertexShader = "/Users/claytonknittel/git/Utilities/src/graphics/shaders/vertexShader";
		defaultFragmentShader = "/Users/claytonknittel/git/Utilities/src/graphics/shaders/fragmentShader";
	}
	
	public Renderer() {
		this(defaultVertexShader, defaultFragmentShader);
	}
	
	public Renderer(String vertexShaderLoc, String fragmentShaderLoc) {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		vertexShader = Shader.createShader(GL_VERTEX_SHADER, fileToStr(defaultVertexShader));
		fragmentShader = Shader.createShader(GL_FRAGMENT_SHADER, fileToStr(defaultFragmentShader));
		
		program = initiateProgram(vertexShader, fragmentShader);
		
		uniModel = program.getUniformLocation("model");
		
		setViewMatrix();
		setProjectionMatrix();
		setLightPos(new Vector(10, 0, -5));
		setLightColor(Color.yellow);
	}
	
	public ShaderProgram shaderProgram() {
		return program;
	}
	
	public InputVariable[] inputs() {
		return inputs;
	}
	
	/**
	 * Call before enter()
	 * 
	 * @param names a list of the names of input variables, of the form "pos, color, ..."
	 * @param sizes the corresponding sizes of each of the input variables (how many degrees of freedom each has)
	 */
	public void configureInputVariables(String names, int... sizes) {
		inputs = new InputVariable[sizes.length];
		String[] name = names.split(", ");
		if (name.length != sizes.length)
			throw new IllegalArgumentException("number of names and sizes need to be the same, but were " + name.length + " and " + sizes.length);
		
		for (int i = 0; i < sizes.length; i++)
			inputs[i] = new InputVariable(program.getAttributeLocation(name[i]), name[i], sizes[i]);
	}
	
	/**
	 * Call after configureInputVariables()
	 * <p>
	 * This configures each state's VAO to draw with the strategies defined in this Renderer
	 * 
	 * @param states the states to be entered with this Renderer
	 */
	public void enter(Iterable<State> states) {
		for (State s : states)
			s.enter(this);
	}
	
	
	
	public void update(Iterable<State> states) {
		for (State s : states)
			s.update();
	}
	
	public void render(Iterable<State> states) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		program.use();
		for (State s : states)
			s.render(program, uniModel);
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
//	private void configureAttributeLocations() {
//		/* Specify Vertex Pointer */
//		pos = program.getAttributeLocation("pos");
//
//		/* Specify Color Pointer */
//		color = program.getAttributeLocation("color");
//	}
	
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
	
	public void setLightPos(Vector p) {
		int uniPos = program.getUniformLocation("lightPos");
		program.setUniform(uniPos, p);
	}
	
	public void setLightColor(Color c) {
		int uniCol = program.getUniformLocation("lightColor");
		program.setUniform(uniCol, new Vector(c.red(), c.green(), c.blue()));
	}
	
}
