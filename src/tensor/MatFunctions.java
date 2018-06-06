package tensor;

import org.jblas.FloatMatrix;
import static org.jblas.MatrixFunctions.*;

public class MatFunctions {
	
	public static FVector sigmoid(FVector v) {
		return new FVector(exp(v.v.neg()).add(1).rdiv(1));
	}
	
	public static FVector dSigmoid(FVector v) {
		FloatMatrix expa = exp(v.v);
		return new FVector(expa.add(expa.rdiv(1)).add(2).rdiv(1));
	}
	
	public static FVector dSigmoidInvSigmoid(FVector v) {
		return new FVector(v.v.sub(1).mul(v.v).neg());
	}
	
	public static FVector cost(FVector actual, FVector ex) {
		FloatMatrix a = actual.v;
		FloatMatrix e = ex.v;
		return new FVector(log(a).mul(e.neg()).add(log(a.neg().add(1)).mul(e.sub(1))));
	}
	
	public static FVector dCost(FVector actual, FVector ex) {
		FloatMatrix a = actual.v;
		FloatMatrix e = ex.v;
		return new FVector(e.sub(a).div(a.mul(a.sub(1))));
	}
	
}
