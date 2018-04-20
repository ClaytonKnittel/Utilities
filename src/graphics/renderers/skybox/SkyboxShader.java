package graphics.renderers.skybox;

import tensor.Matrix4;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL20.*;

import graphics.input.Observer;
import graphics.renderers.InputVariable;
import graphics.shaders.ShaderProgram;

public class SkyboxShader extends ShaderProgram {

	private int location_projectionMatrix;
	private int location_viewMatrix;

	private InputVariable pos;

	public SkyboxShader() {
		super();
		getAllUniformLocations();
	}

	public void loadProjectionMatrix(Matrix4 matrix) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(4 * 4);
			matrix.toBuffer(buffer);
			glUniformMatrix4fv(location_projectionMatrix, false, buffer);
		}
	}

	public void loadViewMatrix(Observer camera) {
		super.setUniform(location_viewMatrix, Matrix4.translate(camera.pos())
				.multiply(Matrix4.cameraRotateMatrix(camera.phi(), camera.theta(), camera.psi())));
	}

	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

}
