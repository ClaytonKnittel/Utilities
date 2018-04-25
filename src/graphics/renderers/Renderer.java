package graphics.renderers;

import graphics.Color;
import graphics.State;
import graphics.input.Observer;
import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;
import tensor.Matrix4;
import tensor.Vector;

public class Renderer extends AbstractRenderer {
	
	private int uniModel, uniView, uniProjection;
	private int uniReflectivity, uniShineDamper;
	private int uniTexture;
	
	public static final String defaultVertexShader, defaultFragmentShader;
	
	private ShaderProgram program;
	
	public static final float FOV = 72;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 4000f;
	
	private InputVariable[] inputs;
	
	static {
		defaultVertexShader = "/Users/claytonknittel/git/Utilities/src/graphics/shaders/vertexShader";
		defaultFragmentShader = "/Users/claytonknittel/git/Utilities/src/graphics/shaders/fragmentShader";
	}
	
	public Renderer() {
		super(defaultVertexShader, defaultFragmentShader, true);
	}
	
	@Override
	public void loadUniformVariables() {
		uniModel = program.getUniformLocation("model");
		uniView = program.getUniformLocation("view");
		uniProjection = program.getUniformLocation("projection");
		
		uniReflectivity = program.getUniformLocation("reflectivity");
		uniShineDamper = program.getUniformLocation("shineDamper");
		
		uniTexture = program.getUniformLocation("texImage");
		
		loadTextureUnits();
	}
	
	private void loadTextureUnits() {
		program.setUniform(uniTexture, 0);
	}
	
	@Override
	public ShaderProgram program() {
		return program;
	}
	
	@Override
	public void initiateProgram(Shader vertexShader, Shader fragmentShader) {
		program = new ShaderProgram();
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);
		program.bindFragmentDataLocation(0, "fragColor");
		program.link();
		program.use();
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
		program.setUniform(uniView, view);
	}
	
	@Override
	public void setProjectionMatrix() {
		program.setUniform(uniProjection, ShaderProgram.projectionMatrix(FOV, NEAR_PLANE, FAR_PLANE));
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
