package graphics.renderers.skybox;

import graphics.Entity;
import graphics.input.Observer;
import graphics.renderers.AbstractRenderer;
import graphics.renderers.InputVariable;
import graphics.renderers.Renderer;
import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;
import graphics.shaders.Texture;
import tensor.Matrix4;

public class SkyboxRenderer extends AbstractRenderer {

	private static final String VERTEX_FILE = "/Users/claytonknittel/git/Utilities/src/graphics/renderers/skybox/skyboxVertexShader";
	private static final String FRAGMENT_FILE = "/Users/claytonknittel/git/Utilities/src/graphics/renderers/skybox/skyboxFragmentShader";
	
	private ShaderProgram program;
	
	private Box box;

	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public SkyboxRenderer(String texture) {
		super(VERTEX_FILE, FRAGMENT_FILE, false);
		this.box = new Box(texture);
	}
	
	@Override
	public void loadUniformVariables() {
		location_projectionMatrix = program.getUniformLocation("projection");
		location_viewMatrix = program.getUniformLocation("view");
	}
	
	@Override
	public void setProjectionMatrix() {
		program.setUniform(location_projectionMatrix, ShaderProgram.projectionMatrix(Renderer.FOV, Renderer.NEAR_PLANE, Renderer.FAR_PLANE));
	}
	
	@Override
	protected void setViewMatrix(Observer observer) {
		program.setUniform(location_viewMatrix, Matrix4.cameraRotateMatrix(-observer.phi(), -observer.theta(), -observer.psi()));
	}
	
	@Override
	public InputVariable[] inputs() {
		return null;
	}
	
	public void enter() {
		box.enter(this);
	}
	
	@Override
	public void render() {
		super.render();
		box.render(null, 0, 0, 0);
	}
	
	@Override
	public ShaderProgram program() {
		return program;
	}
	
	@Override
	public void initiateProgram(Shader vertexShader, Shader fragmentShader) {
		ShaderProgram.clear();
		program = new ShaderProgram();
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);
		program.bindFragmentDataLocation(0, "frag_Color");
		program.link();
		program.use();
	}
	
	private static class Box extends Entity {
		
		public Box(String texture) {
			super(VERTICES, getLocs(texture));
		}

		@Override
		public void update() {
			return;
		}

		@Override
		public void setUniforms(ShaderProgram program, int uniModel, int uniReflectivity, int uniShineDamper) {
		}

		@Override
		public int numAttributes() {
			return 3;
		}

		@Override
		public void init(AbstractRenderer r) {
			return;
		}

		@Override
		public void specifyVertexAttributes(AbstractRenderer r) {
			ShaderProgram p = r.program();
			
			int id = p.getAttributeLocation("pos");
			p.enableVertexAttribute(id);
			p.pointVertexAttribute(id, 3, 3 * Float.BYTES, 0);
		}
		
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
		
		private static String[] getLocs(String texture) {
			 return new String[] {
				Texture.path + texture + "_right.jpg",
				Texture.path + texture + "_left.jpg",
				Texture.path + texture + "_up.jpg",
				Texture.path + texture + "_down.jpg",
				Texture.path + texture + "_back.jpg",
				Texture.path + texture + "_front.jpg"};
		}
		
	}

}
