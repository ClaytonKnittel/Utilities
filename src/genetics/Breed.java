package genetics;

public interface Breed {
	
	Breed breed(Breed other);
	
	void mutate();
	
	float fitness();
	
	/**
	 * 
	 * @param other
	 * @return how similar these breeds are. Having a similary less than 1 means these are very similar, higher
	 * mean they are not very similar.
	 */
	float similarity(Breed other);
	
}
