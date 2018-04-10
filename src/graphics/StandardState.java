package graphics;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;
import tensor.Matrix4;

public abstract class StandardState implements State {
	
	protected ShaderProgram program;
	
	public ShaderProgram initiateProgram(Shader vertexShader, Shader fragmentShader) {
		ShaderProgram program = new ShaderProgram();
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);
		program.bindFragmentDataLocation(0, "fragColor");
		program.link();
		program.use();
		return program;
	}
	
	public void setViewMatrix() {
		Matrix4 view = new Matrix4();
		int uniView = program.getUniformLocation("view");
		program.setUniform(uniView, view);
	}
	
	public void setOrthographicMatrix() {
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
	
}
