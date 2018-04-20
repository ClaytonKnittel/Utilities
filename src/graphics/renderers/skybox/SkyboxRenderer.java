package graphics.renderers.skybox;

import graphics.input.Observer;
import graphics.renderers.AbstractRenderer;
import graphics.renderers.Renderer;
import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;
import graphics.shaders.Texture;
import tensor.Matrix4;

public class SkyboxRenderer extends AbstractRenderer {

	private static final String VERTEX_FILE = "/Users/claytonknittel/git/Utilities/src/graphics/renderers/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "/Users/claytonknittel/git/Utilities/src/graphics/renderers/skybox/skyboxFragmentShader.txt";
	
	private SkyboxShader program;

	private int location_projectionMatrix;
	private int location_viewMatrix;
	
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
	}
	
	@Override
	public void loadUniformVariables() {
		location_projectionMatrix = program.getUniformLocation("projectionMatrix");
		location_viewMatrix = program.getUniformLocation("viewMatrix");
	}
	
	@Override
	public void setProjectionMatrix() {
		program.setUniform(location_projectionMatrix, ShaderProgram.projectionMatrix(Renderer.FOV, Renderer.NEAR_PLANE, Renderer.FAR_PLANE));
	}
	
	@Override
	protected void setViewMatrix(Observer observer) {
		program.setUniform(location_viewMatrix, Matrix4.translate(observer.pos())
			.multiply(Matrix4.cameraRotateMatrix(observer.phi(), observer.theta(), observer.psi())));
	}
	
	@Override
	public ShaderProgram program() {
		return program;
	}
	
	public void render() {
		
	}
	
	@Override
	public void initiateProgram(Shader vertexShader, Shader fragmentShader) {
		program = new SkyboxShader();
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);
		program.bindFragmentDataLocation(0, "fragColor");
		program.link();
		program.use();
	}
//
//	public void enter() {
//		vao = new VertexArrayObject();
//		vao.bind();
//		
//		instantiateVBO(FloatBuffer.wrap(VERTICES));
//		
//		specifyVertexAttributes();
//	}
//	
//	private void specifyVertexAttributes() {
//		ShaderProgram p = program();
//		
//		int id = p.getAttributeLocation("pos");
//		
//		p.enableVertexAttribute(id);
//		p.pointVertexAttribute(id, 3 * Float.BYTES, 3 * Float.BYTES, 0);
//	}
//	
//	private void instantiateVBO(FloatBuffer buffer) {
//		FloatBuffer vertices;
//		try (MemoryStack stack = MemoryStack.stackPush()) {
//			vertices = stack.mallocFloat(buffer.limit());
//			for (int i = 0; i < vertices.capacity(); i++) {
//				vertices.put(buffer.get());
//			}
//			numVertices = buffer.limit() / 3;
//			
//			vertices.flip();
//			
//			vbo = new VertexBufferObject();
//			vbo.bind(GL_ARRAY_BUFFER);
//			vbo.uploadData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
//			
//			vertices.clear();
//		}
//		buffer.clear();
//	}

}
