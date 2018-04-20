package graphics.renderers;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import graphics.Color;
import graphics.State;
import graphics.input.Observer;
import graphics.shaders.ShaderProgram;
import tensor.Matrix4;
import tensor.Vector;

public class Renderer extends AbstractRenderer {
	
	private static int uniModel, uniView, uniProjection;
	private static int uniReflectivity, uniShineDamper;
	
	public static final String defaultVertexShader, defaultFragmentShader;
	
	private static final float FOV = 72;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 4000f;
	
	private InputVariable[] inputs;
	
	static {
		defaultVertexShader = "/Users/claytonknittel/git/Utilities/src/graphics/shaders/vertexShader";
		defaultFragmentShader = "/Users/claytonknittel/git/Utilities/src/graphics/shaders/fragmentShader";
	}
	
	public Renderer() {
		super(defaultVertexShader, defaultFragmentShader, true);
	}
	
	@Override
	public void loadUniformVariables(ShaderProgram program) {
		uniModel = program.getUniformLocation("model");
		uniView = program.getUniformLocation("view");
		uniProjection = program.getUniformLocation("projection");
		
		uniReflectivity = program.getUniformLocation("reflectivity");
		uniShineDamper = program.getUniformLocation("shineDamper");
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
			inputs[i] = new InputVariable(program().getAttributeLocation(name[i]), name[i], sizes[i]);
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
		super.update();
		for (State s : states)
			s.update();
	}
	
	public void render(Iterable<State> states) {
		super.render();
		for (State s : states)
			s.render(program(), uniModel, uniReflectivity, uniShineDamper);
	}
	
	@Override
	protected void setViewMatrix(Observer camera) {
		Matrix4 view = Matrix4.cameraRotateMatrix(-camera.phi(), -camera.theta(), -camera.psi())
				.multiply(Matrix4.translate(camera.pos().times(-1)));
		program().setUniform(uniView, view);
	}
	
	@Override
	public void setProjectionMatrix(ShaderProgram program) {
		float ratio;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			long window = GLFW.glfwGetCurrentContext();
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			GLFW.glfwGetFramebufferSize(window, width, height);
			ratio = width.get() / (float) height.get();
		}
		
		Matrix4 projection = Matrix4.perspective(FOV, ratio, NEAR_PLANE, FAR_PLANE);
		program.setUniform(uniProjection, projection);
	}
	
	public void setLightPos(Vector p) {
		int uniPos = program().getUniformLocation("lightPos");
		program().setUniform(uniPos, p);
	}
	
	public void setLightColor(Color c) {
		int uniCol = program().getUniformLocation("lightColor");
		program().setUniform(uniCol, new Vector(c.redf(), c.greenf(), c.bluef()));
	}
	
}
