package graphics.renderers.skybox;

import tensor.Matrix4;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL20.*;

import graphics.input.Observer;
import graphics.renderers.InputVariable;
import graphics.shaders.ShaderProgram;

public class SkyboxShader extends ShaderProgram {

	private InputVariable pos;

	public SkyboxShader() {
		super();
		getAllUniformLocations();
	}

	protected void getAllUniformLocations() {
	}

}
