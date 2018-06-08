package timing;

public class FrameLimiter {
	
	private long delta;
	private long lastTime;
	
	public FrameLimiter(int framerate) {
		delta = Math.round(1000f / framerate);
	}
	
	public void begin() {
		lastTime = System.currentTimeMillis();
	}
	
	public void delay() {
		long currentTime = System.currentTimeMillis();
		if (lastTime + delta > currentTime) {
			try {
				Thread.sleep(delta + lastTime - currentTime);
			} catch (InterruptedException e) {}
		}
	}
	
}
