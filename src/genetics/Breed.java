package genetics;

public interface Breed {
	
	Breed breed(Breed other);
	
	Breed mutate();
	
	/**
	 * 
	 * @param other
	 * @return how similar these breeds are. Having a similary less than 1 means these are very similar, higher
	 * mean they are not very similar.
	 */
	float similarity(Breed other);
	
	/**
	 * to subtract off the fitness (thus should be positive), used for regularization / encouraging or discouraging a certain trait.
	 * @return
	 */
	float subtractor();
	
}
