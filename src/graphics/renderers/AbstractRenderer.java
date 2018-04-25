package graphics.renderers;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import java.io.IOException;

import files.FileManager;
import graphics.input.Observer;
import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;

public abstract class AbstractRenderer {
	
	private Shader vertexShader;
	private Shader fragmentShader;
	
	private boolean depthTest;
	
	private Observer observer;
	
//	public AbstractRenderer(ShaderProgram program, boolean cullFaces) {
//		if (cullFaces) {
//			glEnable(GL_CULL_FACE);
//			glCullFace(GL_BACK);
//		}
//		this.program = program;
//		
//		loadUniformVariables(program);
//	}
	
	public AbstractRenderer(String vertexShader, String fragmentShader, boolean depthTest) {
		this.depthTest = depthTest;
		
		this.vertexShader = Shader.createShader(GL_VERTEX_SHADER, fileToStr(vertexShader));
		this.fragmentShader = Shader.createShader(GL_FRAGMENT_SHADER, fileToStr(fragmentShader));
		
		initiateProgram(this.vertexShader, this.fragmentShader);
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		loadUniformVariables();
		setProjectionMatrix();
	}
	
	public abstract ShaderProgram program();
	
	public abstract void initiateProgram(Shader vertexShader, Shader fragmentShader);
	
	public abstract void loadUniformVariables();
	
	public abstract void setProjectionMatrix();
	
	protected abstract void setViewMatrix(Observer observer);
	
	public abstract InputVariable[] inputs();
	
	public void update() {
		program().use();
		observer.update();
		setViewMatrix(observer);
	}
	
	protected void render() {
		if (depthTest)
			glEnable(GL_DEPTH_TEST);
		else
			glDisable(GL_DEPTH_TEST);
		program().use();
	}
	
	public static void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void exit() {
		program().delete();
		vertexShader.delete();
		fragmentShader.delete();
	}
	
	private static String fileToStr(String location) {
		try {
			return FileManager.readAll(location);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void createObserver(Observer observer) {
		this.observer = observer;
	}
	
}
