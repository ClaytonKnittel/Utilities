package graphics;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedList;
import java.util.Map;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import graphics.entities.GLFWRenderable;
import graphics.input.KeyAction;
import graphics.renderers.Renderer;

/**
 * @author claytonknittel
 * <p>
 * 
 * Call order:
 * <br>
 * constuctor
 * <br>
 * configureInputs
 * <br>
 * add states
 * <br>
 * enter
 * 
 */
public class GLFWWindow {
	
	/**
	 * Holds strong references to the callbacks (so the garbage collector doesnt get rid of them)
	 */
	private static GLFWErrorCallback errorCallback;
	
	private static GLFWKeyCallback keyCallback;
	
	private long window;
	
	//private GraphicsHandler g;
	private LinkedList<State> states;
	
	private Renderer r;
	
	private QuitAction q;
	
	
	public void quit() {
		glfwSetWindowShouldClose(window, true);
	}
	
	public GLFWWindow(int width, int height, String name, Map<Integer, KeyAction> keyPressed, Map<Integer, KeyAction> keyReleased) {
		this(width, height, name, keyPressed, keyReleased, Renderer.defaultVertexShader, Renderer.defaultFragmentShader);
	}
	
	public GLFWWindow(int width, int height, String name, Map<Integer, KeyAction> keyPressed, Map<Integer, KeyAction> keyReleased, String vertexShaderLoc, String fragmentShaderLoc) {
		errorCallback = GLFWErrorCallback.createPrint(System.err);
		
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		useOpenGL3_2CoreProfileContext();
		window = glfwCreateWindow(width, height, name, 0, 0);
		if (window == 0) {
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW_PRESS) {
					if (keyPressed.containsKey(key))
						keyPressed.get(key).act();
				} else if (action == GLFW_RELEASE) {
					if (keyReleased.containsKey(key))
						keyReleased.get(key).act();
				}
			}
		};
		
		glfwSetKeyCallback(window, keyCallback);
		
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		r = new Renderer(vertexShaderLoc, fragmentShaderLoc);
		
		this.states = new LinkedList<State>();
   	}
	
	public void configureInputs(String names, int[] sizes) {
		r.configureInputVariables(names, sizes);
	}
	
	public void add(GLFWRenderable...states) {
		for (GLFWRenderable g : states)
			this.states.add(new RigidState(g));
	}
	
	/**
	 * Call after loading shaders
	 */
	public void enter() {
		r.enter(states);
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
		r.update(states);
		r.render(states);
		
		
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public void destroy() {
		for (State s : states)
			s.exit();
		r.exit();
		glfwDestroyWindow(window);
		keyCallback.free();
		glfwTerminate();
		errorCallback.free();
		if (q != null)
			q.quit();
	}
	
}
