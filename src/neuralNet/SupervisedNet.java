package neuralNet;

import tensor.FMatrix;
import tensor.FVector;
import static tensor.MatFunctions.*;

public class SupervisedNet extends Network {
	
	private FMatrix[] changes;
	
	private float nu;
	
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
		
		changes[layer] = changes[layer].minus(v.outer(activations[layer]));
		
		FVector vp = new FVector(weights[layer].transpose().multiply(v).mul(dSigmoidInvSigmoid(activations[layer]))).removeTopRow();
		backPropagate(layer - 1, vp);
	}
	
}
