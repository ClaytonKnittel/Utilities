package graphics.trash;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import files.FileManager;
import graphics.entities.Batch;
import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;
import tensor.Matrix4;

public class GraphicsHandler {
	
	private CharSequence vertexSource, fragmentSource;
	
	private Batch batch;
	private Shader vertexShader;
	private Shader fragmentShader;
	private ShaderProgram program;
	
	private int uniModel, uniTex;
	
	private long time;
	
	private int numVertexAttributeBytes;
	
	public GraphicsHandler() {
		time = 0;
	}
	
	public void loadVertexShader(String code) {
		System.out.println(code);
		vertexSource = code;
	}
	
	public void loadFragmentShader(String code) {
		fragmentSource = code;
	}
	
	
	public void input() {
		
	}
	
	public void update(long delta) {
		time += delta;
	}
	
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		
		/*
		 * Set uniforms
		 */
        Matrix4 model = Matrix4.translate(0, 0, 0).multiply(Matrix4.rotate(0, 0f, 0f, 1f));
        program.setUniform(uniModel, model);
        
		program.use();
		batch.begin();
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer vertices = stack.mallocFloat(3 * 6);
            vertices.put(-0.6f).put(-0.4f).put(0f).put(1f).put(0f).put(0f);
            vertices.put(0.6f).put(-0.4f).put(0f).put(0f).put(1f).put(0f);
            vertices.put(0f).put(0.6f).put(0f).put(0f).put(0f).put(1f);
            vertices.flip();
            
    		batch.draw(vertices);
    		vertices.clear();
            /* Generate Vertex Buffer Object */
		}
//		float[] vertices = new float[] {
//			-0.6f, -0.4f, 0f, 1f, 0f, 0f,
//	      	0.6f, -0.4f, 0f, 0f, 1f, 0f,
//	      	0f, 0.6f, 0f, 0f, 0f, 1f
//		};
        
        
		batch.end();
	}

    public void enter() {
        /* Generate Vertex Array Object */

//        try (MemoryStack stack = MemoryStack.stackPush()) {
//            /* Vertex data */
//            FloatBuffer vertices = stack.mallocFloat(3 * 6);
//            vertices.put(-0.6f).put(-0.4f).put(0f).put(1f).put(0f).put(0f);
//            vertices.put(0.6f).put(-0.4f).put(0f).put(0f).put(1f).put(0f);
//            vertices.put(0f).put(0.6f).put(0f).put(0f).put(0f).put(1f);
//            vertices.flip();
//
//            /* Generate Vertex Buffer Object */
//            vbo = new VertexBufferObject();
//            vbo.bind(GL_ARRAY_BUFFER);
//            vbo.uploadData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
//        }

        /* Load shaders */
        vertexShader = Shader.createShader(GL_VERTEX_SHADER, vertexSource);
        fragmentShader = Shader.createShader(GL_FRAGMENT_SHADER, fragmentSource);

        /* Create shader program */
        program = new ShaderProgram();
        program.attachShader(vertexShader);
        program.attachShader(fragmentShader);
        program.bindFragmentDataLocation(0, "fragColor");
        program.link();
        program.use();
        
        specifyVertexAttributes();
        
        batch = new Batch(4096, numVertexAttributeBytes);

        /* Get uniform location for the model matrix */
        uniModel = program.getUniformLocation("model");
        
        
        uniTex = program.getUniformLocation("texImage");
        // sets the value to the texture at location 0 which the first is by default)
        program.setUniform(uniTex, 0);

        /* Set view matrix to identity matrix */
        Matrix4 view = new Matrix4();
        int uniView = program.getUniformLocation("view");
        program.setUniform(uniView, view);

        /* Get width and height for calculating the ratio */
        float ratio;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long window = GLFW.glfwGetCurrentContext();
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, width, height);
            ratio = width.get() / (float) height.get();
        }

        /* Set projection matrix to an orthographic projection */
        Matrix4 projection = Matrix4.orthographic(-ratio, ratio, -1f, 1f, -1f, 1f);
        int uniProjection = program.getUniformLocation("projection");
        program.setUniform(uniProjection, projection);
    }
	
	public void exit() {
        batch.dispose();
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
        
//        int texAttrib = program.getAttributeLocation("texcoord");
//        program.enableVertexAttribute(texAttrib);
//        program.pointVertexAttribute(texAttrib, 2, 8 * Float.BYTES, 5 * Float.BYTES);
        numVertexAttributeBytes = 6;
    }
	
}
