package neuralNet;

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
	
	/**
	 * Top neuron in prev. layer is considered bias and is always 1
	 */
	protected FMatrix[] weights;
	
	protected Network(int... sizes) {
		weights = new FMatrix[sizes.length - 1];
		activations = new FVector[sizes.length];
		for (int i = 0; i < weights.length; i++)
			weights[i] = FMatrix.rand(sizes[i + 1], sizes[i] + 1, 1);
		for (int i = 0; i < activations.length; i++)
			activations[i] = new FVector(sizes[i] + (i < weights.length ? 1 : 0));
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
	
	public String toString() {
		String ret = "";
		for (FMatrix f : weights)
			ret += f + "\n";
		for (FVector v : activations)
			ret += v + "\n";
		return ret;
	}
	
	
	
}
