package graphics.renderers;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import files.FileManager;
import graphics.input.Observer;
import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;

public abstract class AbstractRenderer {
	
	private ShaderProgram program;
	
	private Shader vertexShader;
	private Shader fragmentShader;
	
	private Observer observer;
	
	public AbstractRenderer(ShaderProgram program, boolean cullFaces) {
		if (cullFaces) {
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
		}
		this.program = program;
		
		loadUniformVariables(program);
	}
	
	public AbstractRenderer(String vertexShader, String fragmentShader, boolean cullFaces) {
		if (cullFaces) {
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);
		}
		this.vertexShader = Shader.createShader(GL_VERTEX_SHADER, fileToStr(vertexShader));
		this.fragmentShader = Shader.createShader(GL_FRAGMENT_SHADER, fileToStr(fragmentShader));
		
		program = initiateProgram(this.vertexShader, this.fragmentShader);
		
		loadUniformVariables(program);
		setProjectionMatrix(program);
	}
	
	public ShaderProgram program() {
		return program;
	}
	
	public abstract void loadUniformVariables(ShaderProgram program);
	
	public abstract void setProjectionMatrix(ShaderProgram program);
	
	protected abstract void setViewMatrix(Observer observer);
	
	public ShaderProgram initiateProgram(Shader vertexShader, Shader fragmentShader) {
		ShaderProgram program = new ShaderProgram();
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);
		program.bindFragmentDataLocation(0, "fragColor");
		program.link();
		program.use();
		return program;
	}
	
	protected void update() {
		observer.update();
		setViewMatrix(observer);
	}
	
	protected void render() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		program.use();
	}
	
	public void exit() {
		program.delete();
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
