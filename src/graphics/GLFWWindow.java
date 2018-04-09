package graphics;

import static org.lwjgl.glfw.GLFW.*;
import static graphics.GraphicsHandler.fileToStr;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;


public class GLFWWindow {
	
	/**
	 * Holds strong references to the callbacks (so the garbage collector doesnt get rid of them)
	 */
	private static GLFWErrorCallback errorCallback;
	
	private static GLFWKeyCallback keyCallback;
	
	private long window;
	
	double nextTime = 1;
	
	private GraphicsHandler g;
	
	private long last;
	
	private QuitAction q;
	
	
	public GLFWWindow() {
		errorCallback = GLFWErrorCallback.createPrint(System.err);
		
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		useOpenGL3_2CoreProfileContext();
		window = glfwCreateWindow(800, 600, "Planetary Motion", 0, 0);
		if (window == 0) {
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_Q && action == GLFW_PRESS) {
					glfwSetWindowShouldClose(window, true);
				}
			}
		};
		
		glfwSetKeyCallback(window, keyCallback);
		
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		g = new GraphicsHandler();
    	
    	last = System.currentTimeMillis();
	}
	
	public void loadShaders(String vertexShaderLocation, String fragmentShaderLocation) {
		g.loadVertexShader(fileToStr(vertexShaderLocation));
		g.loadFragmentShader(fileToStr(fragmentShaderLocation));
	}
	
	/**
	 * Call after loading shaders
	 */
	public void enter() {
		g.enter();
	}
	
	public void setQuitAction(QuitAction q) {
		this.q = q;
	}
	
	
	public void useOpenGL3_2CoreProfileContext() {
		// reset window hints
		glfwDefaultWindowHints();
		
		// create a 3.2 context
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public void render() {
		if (glfwGetTime() > nextTime) {
			nextTime += 1;
			System.out.println(glfwGetTime());
		}
		
		g.update(System.currentTimeMillis() - last);
		last = System.currentTimeMillis();
		g.render();
		
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public void destroy() {
		g.exit();
		glfwDestroyWindow(window);
		keyCallback.free();
		glfwTerminate();
		errorCallback.free();
		if (q != null)
			q.quit();
	}
	
}
