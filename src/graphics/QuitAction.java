package graphics;

public interface QuitAction {
	
	/**
	 * Called by the GLFW window after all resources have been exhausted and it is time to close the application.
	 * <p>
	 * Used to terminate threads/close resources on the user side
	 */
	void quit();
}
