package neuralNet;

import tensor.FMatrix;
import tensor.FVector;
import static tensor.MatFunctions.*;

import methods.P;

public class SupervisedNet extends Network {
	
	private FMatrix[] changes;
	
	private float nu;
	
	public static void main(String args[]) {
		SupervisedNet n = new SupervisedNet(4, 3);
		for (int i = 0; i < 300; i++) {
			n.input(1, 0, 0, 0);
			n.train(0, 0, 1);
			n.applyChanges();
			float f = n.weights[0].sumAbs();
			P.pl("F: " + f);
			if ((f + "").equals("NaN"))
				break;
		}
		
		n.save("src/neuralNet/test.txt");
		
//		NeuralNetVisualizer v = new NeuralNetVisualizer(n);
//		v.start();
	}
	
	public SupervisedNet(Network n) {
		super(n);
		changes = new FMatrix[n.sizes().length - 1];
		for (int i = 0; i < changes.length; i++)
			changes[i] = new FMatrix(weights[i].rows(), weights[i].columns());
		nu = .3f;
	}
	
	public SupervisedNet(int...sizes) {
		super(sizes);
		changes = new FMatrix[sizes.length - 1];
		for (int i = 0; i < changes.length; i++)
			changes[i] = new FMatrix(weights[i].rows(), weights[i].columns());
		nu = .3f;
	}
	
	public void setNu(float nu) {
		this.nu = nu;
	}
	
	public void applyChanges() {
		for (int i = 0; i < changes.length; i++)
			super.weights[i].add(changes[i].multiply(nu));
		zeroChanges();
	}
	
	private void zeroChanges() {
		for (FMatrix m : changes)
			m.setZero();
	}
	
	public void train(float...expected) {
		FVector da = dCost(output(), new FVector(expected));
		FVector v = da.mul(dSigmoid(output()));
		backPropagate(changes.length - 1, v);
	}
	
	private void backPropagate(int layer, FVector v) {
		if (layer < 0)
			return;

		P.pl("Weights:\n" + weights[0]);
		P.pl("ch =\n" + v + " outer " + activations[layer] + "\n\n");
		changes[layer] = changes[layer].minus(v.outer(activations[layer]));
		
		if (layer == 0)
			return;
		
		FVector vp = new FVector(weights[layer].transpose().multiply(v).mul(dSigmoidInvSigmoid(activations[layer]))).removeTopRow();
		backPropagate(layer - 1, vp);
	}
	
}
