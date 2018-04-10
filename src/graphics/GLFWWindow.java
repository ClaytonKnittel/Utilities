package graphics;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedList;

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
	
	//private GraphicsHandler g;
	private LinkedList<State> states;
	
	private long last;
	
	private QuitAction q;
	
	
	public GLFWWindow(State... states) {
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
		
		this.states = new LinkedList<State>();
		for (State s : states)
			this.states.add(s);
    	
    	last = System.currentTimeMillis();
	}
	
	/**
	 * Call after loading shaders
	 */
	public void enter() {
		for (State s : states)
			s.enter();
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
		
		long l = System.currentTimeMillis();
		
		for (State s : states)
			s.update(l - last);
		for (State s : states)
			s.render(0);
		
		last = l;
		
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public void destroy() {
		for (State s : states)
			s.exit();
		glfwDestroyWindow(window);
		keyCallback.free();
		glfwTerminate();
		errorCallback.free();
		if (q != null)
			q.quit();
	}
	
}
