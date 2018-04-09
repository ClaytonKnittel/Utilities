package methods;

@FunctionalInterface
public interface Lambda<I, O> {
	O f(I p);
}