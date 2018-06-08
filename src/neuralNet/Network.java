package neuralNet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import files.FileManager;
import tensor.FMatrix;
import tensor.FVector;
import tensor.MatFunctions;

/**
 * Simple feed-forward network.
 * 
 * @author claytonknittel
 *
 */
public class Network {
	
	protected NetworkInput input;
	
	protected FVector[] activations;
	private int[] sizes;
	
	private int totalWeights, totalNeurons;
	
	/**
	 * Top neuron in prev. layer is considered bias and is always 1
	 */
	protected FMatrix[] weights;
	
	public Network(int... sizes) {
		this.sizes = sizes;
		weights = new FMatrix[sizes.length - 1];
		activations = new FVector[sizes.length];
		for (int i = 0; i < weights.length; i++)
			weights[i] = FMatrix.rand(sizes[i + 1], sizes[i] + 1, 1);
		for (int i = 0; i < activations.length; i++)
			activations[i] = new FVector(sizes[i] + (i < weights.length ? 1 : 0));
		computeTotals();
	}
	
	public Network(Network n) {
		this.sizes = n.sizes;
		this.activations = new FVector[sizes.length];
		this.weights = new FMatrix[activations.length - 1];
		this.input = n.input;
		for (int i = 0; i < activations.length; i++)
			activations[i] = new FVector(n.activations[i]);
		for (int i = 0; i < weights.length; i++)
			weights[i] = new FMatrix(n.weights[i]);
		computeTotals();
	}
	
	/**
	 * Files are in the format:<br>
	 * size of inputs<br>
	 * size of hidden layers<br>
	 * ...<br>
	 * size of outputs<br>
	 * <br>
	 * matrix 1:<br>
	 * a11<br>
	 * a12<br>
	 * ...<br>
	 * a21<br>
	 * a22<br>
	 * ...<br>
	 * <br>
	 * matrix 2:<br>
	 * ...<br>
	 * 
	 * 
	 * 
	 * @param location
	 * @return
	 */
	public static Network fromFile(String location) {
		LinkedList<String> s = FileManager.toList(location);
		
		LinkedList<Integer> sizes = new LinkedList<>();
		while (!s.getFirst().equals(""))
			sizes.add(Integer.parseInt(s.removeFirst()));
		
		int[] size = new int[sizes.size()];
		Iterator<Integer> i = sizes.iterator();
		int l = 0;
		while (i.hasNext())
			size[l++] = i.next();
		
		Network network = new Network(size);
		
		int exRows, exCols;
		
		for (int ie = 0; ie < size.length - 1; ie++) {
			s.removeFirst();
			exRows = size[ie + 1];
			exCols = size[ie] + 1;
			for (int a = 0; a < exRows; a++) {
				for (int b = 0; b < exCols; b++)
					network.weights[ie].set(a, b, Float.parseFloat(s.removeFirst()));
			}
		}
		
		return network;
	}
	
	public void save(String location) {
		List<String> s = new LinkedList<>();
		
		for (int i = 0; i < activations.length; i++)
			s.add("" + (activations[i].size() - (i < activations.length - 1 ? 1 : 0)));
		for (int i = 0; i < weights.length; i++) {
			s.add("");
			for (float f : weights[i].data())
				s.add(f + "");
		}
		
		FileManager.save(s, location);
	}
	
	private void computeTotals() {
		totalNeurons = activations.length - 1;
		for (int i : sizes)
			totalNeurons += i;
		totalWeights = 0;
		for (FMatrix m : weights)
			totalWeights += m.length();
	}
	
	public int totalNeurons() {
		return totalNeurons;
	}
	
	public int totalWeights() {
		return totalWeights;
	}
	
	public void set(NetworkInput c) {
		input = c;
	}
	
	public FVector output() {
		return activations[activations.length - 1];
	}
	
	public void input(float... args) {
		activations[0] = new FVector(args);
		input(1);
	}
	
	private void input(int layer) {
		if (layer == activations.length)
			return;
		activations[layer - 1] = activations[layer - 1].appendRow(1);
		activations[layer] = MatFunctions.sigmoid(weights[layer - 1].multiply(activations[layer - 1]));
		input(layer + 1);
	}
	
	public void input(NetworkInput c) {
		input(c.inputs());
	}
	
	/**
	 * Polls input from the network controller that has been set. Requires
	 * the network controller to be non-null.
	 */
	public void input() {
		input(input);
	}
	
	public int size(int layer) {
		return sizes[layer];
	}
	
	public int[] sizes() {
		return sizes;
	}
	
	public FVector getLayer(int layer) {
		return activations[layer];
	}
	
	public FMatrix getWeights(int layer) {
		return weights[layer];
	}
	
	public int layers() {
		return activations.length;
	}
	
	public boolean[] choices() {
		boolean[] ret = new boolean[output().size()];
		for (int i = 0; i < ret.length; i++) {
			if (output().get(i) > .5f)
				ret[i] = true;
		}
		return ret;
	}
	
	public int maxOutput() {
		int loc = 0;
		float max = output().get(0);
		float v;
		for (int i = 1; i < output().size(); i++) {
			v = output().get(i);
			if (v > max) {
				max = v;
				loc = i;
			}
		}
		return loc;
	}
	
	public String toString() {
		String ret = "";
		for (FMatrix f : weights)
			ret += f + "\n";
		for (FVector v : activations)
			ret += v + "\n";
		return ret;
	}
	
	
	
}
