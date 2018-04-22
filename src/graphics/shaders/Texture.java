package graphics.shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

/**
 * @author claytonknittel
 * 
 *         Textures use (s, t) coordinate system, both ranging from 0.0 to 1.0.
 * 
 *         Different wrapping modes: GL_REPEAT repeats texture
 *         GL_MIRRORED_REPEAT repeats, mirroring with odd coordinates
 *         GL_CLAMP_TO_EDGE clamps the coordinates between 0.0 qnd 1.0
 *         GL_CLAMP_TO_BORDER Gives coordinates outside range a specific border
 *         color
 * 
 */
public class Texture {

	public static final String path = "/Users/claytonknittel/git/Utilities/src/graphics/models/textures/";

	/**
	 * Stores the handle of the texture
	 */
	private final int id;

	/**
	 * Width of the texture.
	 */
	private int width;

	/**
	 * Height of the texture.
	 */
	private int height;

	public Texture() {
		id = glGenTextures();
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void bindCubeMap() {
		glBindTexture(GL_TEXTURE_CUBE_MAP, id);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	}

	/**
	 * 
	 * @param modeX
	 *            GL_REPEAT, GL_MIRRORED_REPEAT, GL_CLAMP_TO_EDGE, or
	 *            GL_CLAMP_TO_BORDER
	 * @param modeY
	 *            GL_REPEAT, GL_MIRRORED_REPEAT, GL_CLAMP_TO_EDGE, or
	 *            GL_CLAMP_TO_BORDER
	 */
	public void setWrappingMode(int modeX, int modeY) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, modeX);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, modeY);
	}

	/**
	 * @param GL_REPEAT,
	 *            GL_MIRRORED_REPEAT, GL_CLAMP_TO_EDGE, or GL_CLAMP_TO_BORDER
	 */
	public void setWrappingMode(int mode) {
		setWrappingMode(mode, mode);
	}

	/**
	 * @param mode
	 *            GL_NEAREST, GL_LINEAR, etc.
	 */
	public void setFilterMode(int mode) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, mode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mode);
	}

	public void setParameter(int param, int mode) {
		glTexParameteri(GL_TEXTURE_2D, param, mode);
	}

	/**
	 * Uploads image data with specified width and height.
	 *
	 * @param width
	 *            Width of the image
	 * @param height
	 *            Height of the image
	 * @param data
	 *            Pixel data of the image
	 */
	public void uploadData(int width, int height, ByteBuffer data) {
		uploadData(GL_RGBA8, width, height, GL_RGBA, data);
	}

	/**
	 * Uploads image data with specified internal format, width, height and
	 * image format.
	 *
	 * @param internalFormat
	 *            Internal format of the image data
	 * @param width
	 *            Width of the image
	 * @param height
	 *            Height of the image
	 * @param format
	 *            Format of the image data
	 * @param data
	 *            Pixel data of the image
	 */
	public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
	}
	
	public void uploadCubeMap(int i, int internalFormat, int width, int height, int format, ByteBuffer data) {
		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
	}

	/**
	 * Delete the texture.
	 */
	public void delete() {
		glDeleteTextures(id);
	}

	/**
	 * Gets the texture width.
	 *
	 * @return Texture width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the texture width.
	 *
	 * @param width
	 *            The width to set
	 */
	public void setWidth(int width) {
		if (width > 0) {
			this.width = width;
		}
	}

	/**
	 * Gets the texture height.
	 *
	 * @return Texture height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the texture height.
	 *
	 * @param height
	 *            The height to set
	 */
	public void setHeight(int height) {
		if (height > 0) {
			this.height = height;
		}
	}

	/**
	 * Creates a texture with specified width, height and data.
	 *
	 * @param width
	 *            Width of the texture
	 * @param height
	 *            Height of the texture
	 * @param data
	 *            Picture Data in RGBA format
	 *
	 * @return Texture from the specified data
	 */
	public static Texture createTexture(int width, int height, ByteBuffer data) {
		Texture texture = new Texture();
		texture.setWidth(width);
		texture.setHeight(height);

		texture.bind();

		texture.setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		texture.setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		texture.uploadData(GL_RGBA8, width, height, GL_RGBA, data);

		return texture;
	}
	
	
	/**
	 * goes right face, left face, top face, bottom face, front face, back face
	 * @param width
	 * @param height
	 * @param data
	 * @return
	 */
	public static Texture createCubeMap(int width, int height, ByteBuffer[] data) {
		Texture texture = new Texture();
		texture.setWidth(width);
		texture.setHeight(height);

		//glActiveTexture(GL_TEXTURE0);
		texture.bindCubeMap();

		for (int i = 0; i < data.length; i++)
			texture.uploadCubeMap(i, GL_RGBA, width, height, GL_RGBA, data[i]);
		
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		
		return texture;
	}

	/**
	 * Load texture from file.
	 *
	 * @param path
	 *            File path of the texture
	 *
	 * @return Texture from specified file
	 */
	public static Texture loadTexture(String path) {
		ByteBuffer image;
		int width, height;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			/* Prepare image buffers */
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			/* Load image */
			//stbi_set_flip_vertically_on_load(true);
			image = stbi_load(path, w, h, comp, 4);
			if (image == null) {
				throw new RuntimeException(
						"Failed to load a texture file!" + System.lineSeparator() + stbi_failure_reason());
			}

			/* Get width and height of image */
			width = w.get();
			height = h.get();
		}
		return createTexture(width, height, image);
	}

	public static Texture loadCubeMap(String[] paths) {
		ByteBuffer[] images = new ByteBuffer[paths.length];
		int width = 0, height = 0;
		
		for (int i = 0; i < paths.length; i++) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				/* Prepare image buffers */
				IntBuffer w = stack.mallocInt(1);
				IntBuffer h = stack.mallocInt(1);
				IntBuffer comp = stack.mallocInt(1);
				
				/* Load image */
				//stbi_set_flip_vertically_on_load(true);
				images[i] = stbi_load(paths[i], w, h, comp, 4);
				if (images[i] == null) {
					throw new RuntimeException(
							"Failed to load a texture file!" + System.lineSeparator() + stbi_failure_reason() + "\n" + paths[i]);
				}
	
				/* Get width and height of image */
				width = w.get();
				height = h.get();
			}
		}
		return createCubeMap(width, height, images);

	}

}
