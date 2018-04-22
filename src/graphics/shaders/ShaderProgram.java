package graphics.shaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import tensor.Matrix;
import tensor.Matrix2;
import tensor.Matrix4;
import tensor.Vector;
import tensor.Vector2;
import tensor.Vector4;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

/**
 * This class represents a shader program.
 *
 * @author Heiko Brumme
 */
public class ShaderProgram {

	/**
	 * Stores the handle of the program.
	 */
	private final int id;

	/**
	 * Creates a shader program.
	 */
	public ShaderProgram() {
		id = glCreateProgram();
	}

	/**
	 * Attach a shader to this program.
	 *
	 * @param shader
	 *            Shader to get attached
	 */
	public void attachShader(Shader shader) {
		glAttachShader(id, shader.id());
	}

	/**
	 * Binds the fragment out color variable.
	 *
	 * @param number
	 *            Color number you want to bind
	 * @param name
	 *            Variable name
	 */
	public void bindFragmentDataLocation(int number, CharSequence name) {
		glBindFragDataLocation(id, number, name);
	}

	/**
	 * Link this program and check it's status afterwards.
	 */
	public void link() {
		glLinkProgram(id);

		checkStatus();
	}

	/**
	 * Gets the location of an attribute variable with specified name.
	 *
	 * @param name
	 *            Attribute name
	 *
	 * @return Location of the attribute
	 */
	public int getAttributeLocation(CharSequence name) {
		return glGetAttribLocation(id, name);
	}

	/**
	 * Enables a vertex attribute.
	 *
	 * @param location
	 *            Location of the vertex attribute
	 */
	public void enableVertexAttribute(int location) {
		glEnableVertexAttribArray(location);
	}

	/**
	 * Disables a vertex attribute.
	 *
	 * @param location
	 *            Location of the vertex attribute
	 */
	public void disableVertexAttribute(int location) {
		glDisableVertexAttribArray(location);
	}

	/**
	 * Sets the vertex attribute pointer.
	 *
	 * @param location
	 *            Location of the vertex attribute
	 * @param size
	 *            Number of values per vertex
	 * @param stride
	 *            Offset between consecutive generic vertex attributes in bytes
	 * @param offset
	 *            Offset of the first component of the first generic vertex
	 *            attribute in bytes
	 */
	public void pointVertexAttribute(int location, int size, int stride, int offset) {
		glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset);
	}

	/**
	 * Gets the location of a uniform variable with specified name.
	 *
	 * @param name
	 *            Uniform name
	 *
	 * @return Location of the uniform
	 */
	public int getUniformLocation(CharSequence name) {
		use();
		return glGetUniformLocation(id, name);
	}

	/**
	 * Sets the uniform variable for specified location.
	 *
	 * @param location
	 *            Uniform location
	 * @param value
	 *            Value to set
	 */
	public void setUniform(int location, int value) {
		use();
		glUniform1i(location, value);
	}

	/**
	 * Sets the uniform variable for specified location.
	 *
	 * @param location
	 *            Uniform location
	 * @param value
	 *            Value to set
	 */
	public void setUniform(int location, float value) {
		use();
		glUniform1f(location, value);
	}

	/**
	 * Sets the uniform variable for specified location.
	 *
	 * @param location
	 *            Uniform location
	 * @param value
	 *            Value to set
	 */
	public void setUniform(int location, Vector2 value) {
		use();
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(2);
			value.toBuffer(buffer);
			glUniform2fv(location, buffer);
		}
	}

	/**
	 * Sets the uniform variable for specified location.
	 *
	 * @param location
	 *            Uniform location
	 * @param value
	 *            Value to set
	 */
	public void setUniform(int location, Vector value) {
		use();
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(3);
			value.toBuffer(buffer);
			glUniform3fv(location, buffer);
		}
	}

	/**
	 * Sets the uniform variable for specified location.
	 *
	 * @param location
	 *            Uniform location
	 * @param value
	 *            Value to set
	 */
	public void setUniform(int location, Vector4 value) {
		use();
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(4);
			value.toBuffer(buffer);
			glUniform4fv(location, buffer);
		}
	}

	/**
	 * Sets the uniform variable for specified location.
	 *
	 * @param location
	 *            Uniform location
	 * @param value
	 *            Value to set
	 */
	public void setUniform(int location, Matrix2 value) {
		use();
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(2 * 2);
			value.toBuffer(buffer);
			// if true, will be transposed before used
			glUniformMatrix2fv(location, false, buffer);
		}
	}

	/**
	 * Sets the uniform variable for specified location.
	 *
	 * @param location
	 *            Uniform location
	 * @param value
	 *            Value to set
	 */
	public void setUniform(int location, Matrix value) {
		use();
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(3 * 3);
			value.toBuffer(buffer);
			glUniformMatrix3fv(location, false, buffer);
		}
	}

	/**
	 * Sets the uniform variable for specified location.
	 *
	 * @param location
	 *            Uniform location
	 * @param value
	 *            Value to set
	 */
	public void setUniform(int location, Matrix4 value) {
		use();
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(4 * 4);
			value.toBuffer(buffer);
			glUniformMatrix4fv(location, false, buffer);
		}
	}
	
	public static Matrix4 projectionMatrix(float FOV, float nearPlane, float farPlane) {
		float ratio;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			long window = GLFW.glfwGetCurrentContext();
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			GLFW.glfwGetFramebufferSize(window, width, height);
			ratio = width.get() / (float) height.get();
		}
		return Matrix4.perspective(FOV, ratio, nearPlane, farPlane);
	}

	/**
	 * Use this shader program.
	 */
	public void use() {
		glUseProgram(id);
	}
	
	public static void clear() {
		glUseProgram(0);
	}

	/**
	 * Checks if the program was linked successfully.
	 */
	public void checkStatus() {
		int status = glGetProgrami(id, GL_LINK_STATUS);
		if (status != GL_TRUE) {
			throw new RuntimeException(glGetProgramInfoLog(id));
		}
	}

	/**
	 * Deletes the shader program.
	 */
	public void delete() {
		glDeleteProgram(id);
	}

}