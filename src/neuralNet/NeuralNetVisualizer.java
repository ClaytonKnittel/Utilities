package neuralNet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import numbers.F;
import tensor.FMatrix;
import tensor.FVector;
import timing.ActionTimer;

public class NeuralNetVisualizer {
	
	private Network n;
	private static long delta = 1000 / 30;
	private static long last;
	private static int width, height;
	
	private static final int menuBar = 23;
	
	private static BufferStrategy b;
	
	private static final Color high, low, chosenColor;
	
	private static ActionTimer t;
	
	static {
		high = new Color(26, 240, 69);
		low = new Color(240, 36, 34);
		chosenColor = new Color(150, 54, 182);
	}
	
	public static void main(String args[]) {
		NeuralNetVisualizer v = new NeuralNetVisualizer("/users/claytonknittel/documents/workspace/practice programs/src/genetics/neuralNet");
		v.start();
	}
	
	private void graphics() {
		Graphics g = b.getDrawGraphics();
		
		g.clearRect(0, 0, width, height);
		t.check();
		draw(g);
		
		g.dispose();
		b.show();
		long now = System.currentTimeMillis();
		if (now - last < delta)
			try {
				Thread.sleep(delta + last - now);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		last = now;
	}
	
	public NeuralNetVisualizer(String loc) {
		this(Network.fromFile(loc));
	}
	
	public NeuralNetVisualizer(Network n) {
		this.n = n;
		JFrame j = new JFrame();
		width = 800;
		height = 600;
		j.setSize(width, height + menuBar);
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setTitle("Neural Net Visualizer");
		
		j.createBufferStrategy(2);
		b = j.getBufferStrategy();
		last = System.currentTimeMillis();
		
		t = new ActionTimer();
		t.loop();
		for (int i = 0; i < n.getLayer(0).size() - 1; i++) {
			float[] f = new float[n.getLayer(0).size() - 1];
			f[i] = 1;
			t.add(() -> n.input(f), 1);
		}
	}
	
	public void start() {
		Thread graphics = new Thread(() -> { while (true) graphics(); });
		graphics.start();
	}
	
	private static Color blend(Color c1, Color c2, float percent1) {
		float percent2 = 1 - percent1;
		return new Color((int) (c1.getRed() * percent1 + c2.getRed() * percent2), 
				(int) (c1.getGreen() * percent1 + c2.getGreen() * percent2), 
				(int) (c1.getBlue() * percent1 + c2.getBlue() * percent2));
	}
	
	public void draw(Graphics g) {
		int layers = n.layers(), ns;
		float radius;
		float x, y, z;
		FVector neurons, next;
		FMatrix weights;
		
		int chosen = n.maxOutput();
		
		for (int i = 0; i < layers; i++) {
			neurons = n.getLayer(i);
			ns = neurons.size();
			x = (2 * i + 1) * width / (2 * layers);
			radius = 15;
			
			if (i != layers - 1)
				ns--;
			
			int nss;
			if (i != layers - 1) {
				weights = n.getWeights(i);
				next = n.getLayer(i + 1);
				nss = next.size() - (i != layers - 2 ? 1 : 0);
			}
			else {
				weights = null;
				next = null;
				nss = 0;
			}
			
			for (int j = 0; j < ns; j++) {
				y = (2 * j + 1) * height / (2 * ns);
				
				if (i != layers - 1) {
					for (int k = 0; k < nss; k++) {
						z = (float) (2 * k + 1) * height / (2 * nss);
						g.setColor(blend(high, low, F.sigmoid(weights.get(k, j + 1))));
						g.drawLine((int) x, (int) y + menuBar, (int) (x + (float) width / layers), (int) z + menuBar);
					}
				}
				
				radius = Math.min(radius, height / (ns - 1f));
				g.setColor(blend(high, low, neurons.get(j + (i != layers - 1 ? 1 : 0))));
				g.fillArc((int) (x - radius), menuBar + (int) (y - radius), (int) (2 * radius), (int) (2 * radius), 0, 360);
				if (i != 0) {
					g.setColor(blend(high, low, F.sigmoid(n.getWeights(i - 1).get(j, 0))));
					g.drawArc((int) (x - radius), menuBar + (int) (y - radius), (int) (2 * radius), (int) (2 * radius), 0, 360);
					g.drawArc((int) (x - radius - 1), menuBar + (int) (y - radius - 1), (int) (2 * radius + 2), (int) (2 * radius + 2), 0, 360);
				}
				if (i == layers - 1 && j == chosen) {
					g.setColor(chosenColor);
					g.drawArc((int) (x - radius - 2), menuBar + (int) (y - radius - 2), (int) (2 * radius + 4), (int) (2 * radius + 4), 0, 360);
					g.drawArc((int) (x - radius - 3), menuBar + (int) (y - radius - 3), (int) (2 * radius + 6), (int) (2 * radius + 6), 0, 360);
				}
			}
		}
	}
	
}
