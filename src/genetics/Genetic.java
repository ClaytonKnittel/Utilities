package genetics;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import methods.P;
import structures.LList;

public class Genetic {
	
	private Breed[] population;
	private float mutateChance, similarityCutoff, killPercent, killChance, breedChance;
	
	private float[] lastSims;
	private int loc;
	
//	public static void main(String args[]) {
//		O[] o = new O[100];
//		for (int i = 0; i < o.length; i++)
//			o[i] = new O((int) (Math.random() * 0b11111111));
//		Genetic g = new Genetic(o);
//		P.pl(g.run());
//	}
	
//	public static class O implements Breed {
//		int val;
//		public O(int val) {
//			this.val = val;
//		}
//		
//		public O breed(Breed other) {
//			int split = (int) (Math.random() * 33);
//			int val = ((O) other).val;
//			
//			int bab = 0;
//			for (int i = 0; i < split; i++) {
//				bab <<= 1;
//				if (((this.val >>> (31 - i)) & 1) != 0)
//					bab ^= 1;
//			}
//			for (int i = split; i < 32; i++) {
//				bab <<= 1;
//				if (((val >>> (31 - i)) & 1) != 0)
//					bab ^= 1;
//			}
//			
//			O baby = new O(bab);
//			return baby;
//		}
//		
//		public void mutate() {
//			int val = 1 << ((int) (Math.random() * 31));
//			this.val ^= val;
//		}
//		
//		public float fitness() {
//			return val / 100f;
//		}
//		
//		public float similarity(Breed other) {
//			int s = 0;
//			int val = ((O) other).val ^ this.val;
//			int comp = 1;
//			for (int i = 0; i < 32; i++) {
//				if ((val & comp) != 0)
//					s++;
//				comp <<= 1;
//			}
//			return s;
//		}
//		
//		public String toString() {
//			return val + "";
//		}
//	}
	
	public Genetic(Breed... population) {
		this.population = population;
		setHyperParameters(.04f, .1f, .5f, .2f, .01f, 4);
	}
	
	public Genetic(List<Breed> population) {
		this.population = (Breed[]) Array.newInstance(population.get(0).getClass(), population.size());
		population.toArray(this.population);
		setHyperParameters(.04f, .1f, .5f, .2f, .01f, 4);
	}
	
	public void setHyperParameters(float mutateChance, float similarityCutoff, float killPercent, float killChance, float breedChance, int numSimilaritiesKept) {
		this.mutateChance = mutateChance;
		this.similarityCutoff = similarityCutoff;
		this.killPercent = killPercent;
		this.killChance = killChance;
		this.breedChance = breedChance;
		lastSims = new float[numSimilaritiesKept];
		loc = 0;
	}
	
	public Breed run() {
		Arrays.sort(population, (a, b) -> {
			float aa = a.fitness();
			float bb = b.fitness();
			if (aa < bb)
				return -1;
			return 1;
		});
		LList<Fit> pop = genFit();
		
		int generations = 0;
		
		do {
			P.pl("Generation " + ++generations);
			calculateFitness(pop);
			sort(pop);
			kill(pop);
			repopulate(pop);
			mutate(pop);
			
//			calculateFitness(pop);
//			sort(pop);
			fillPop(pop);
			
//			P.pl("\n\n\nAfter:\n");
//			P.pl(this);
			
			addSimilarity(similarity());
//			P.pl(simInfo());
		} while (varianceOfSims() > similarityCutoff);
		return pop.getFirst().individual;
	}
	
	private void addSimilarity(float s) {
		lastSims[loc % lastSims.length] = s;
		
		if (loc >= lastSims.length - 1)
			loc = (loc + 1) % lastSims.length + lastSims.length;
		else
			loc = (loc + 1) % lastSims.length;
	}
	
	private float varianceOfSims() {
		if (loc < lastSims.length)
			return Float.MAX_VALUE;
		float avg = 0;
		int numS = lastSims.length;
		for (int i = 0; i < numS; i++)
			avg += lastSims[i];
		avg /= numS;
		float var = 0;
		for (int i = 0; i < numS; i++)
			var += square(lastSims[i] - avg);
		return var / (numS - 1);
	}
	
//	private String simInfo() {
//		String ret = "Sims: variance = " + varianceOfSims() + "\n";
//		for (int i = 0; i < Math.min(loc, lastSims.length); i++)
//			ret += lastSims[i] + " ";
//		return ret;
//	}
	
	private static float square(float f) {
		return f * f;
	}
	
	private void sort(LList<Fit> pop) {
		pop.sort((a, b) -> a.fitness < b.fitness ? -1 : a.fitness == b.fitness ? 0 : 1);
	}
	
	private LList<Fit> genFit() {
		LList<Fit> pop = new LList<>();
		for (int i = 0; i < population.length; i++)
			pop.add(new Fit(population[i], 0));
		return pop;
	}
	
	private void calculateFitness(LList<Fit> pop) {
		for (Fit p : pop)
			p.fitness = p.individual.fitness();
	}
	
	private void kill(LList<Fit> pop) {
		int kills = (int) (pop.size() * killPercent);
		LList.Node<Fit> n;
		while (kills > 0) {
			n = pop.getLastNode();
			while (Math.random() > killChance) {
				n = n.prev();
				if (n == null)
					n = pop.getLastNode();
			}
			pop.removeNode(n);
			kills--;
		}
	}
	
	private void repopulate(LList<Fit> pop) {
		LList<Fit> adds = new LList<>();
		int numBabies = population.length - pop.size();
		for (int i = 0; i < numBabies; i++) {
			adds.add(new Fit(breedRandom(pop), 0));
		}
		for (Fit f : adds)
			pop.add(f);
	}
	
	private Breed breedRandom(LList<Fit> pop) {
		Breed mom = null, dad = null;
		while (mom == null) {
			for (Fit f : pop) {
				if (Math.random() < breedChance) {
					mom = f.individual;
					break;
				}
			}
		}
		while (dad == null || dad == mom) {
			for (Fit f : pop) {
				if (Math.random() < breedChance) {
					dad = f.individual;
					break;
				}
			}
		}
		return mom.breed(dad);
	}
	
	private void mutate(LList<Fit> pop) {
		for (Fit f : pop) {
			if (Math.random() < mutateChance)
				f.individual.mutate();
		}
	}
	
	private void fillPop(LList<Fit> pop) {
		int l = 0;
		for (Fit f : pop)
			population[l++] = f.individual;
	}
	
	public float similarity() {
		float sum = 0;
		for (int i = 0; i < population.length; i++) {
			for (int j = i + 1; j < population.length; j++)
				sum += population[i].similarity(population[j]);
		}
		return sum * 2 / (population.length * (population.length + 1));
	}
	
	private class Fit {
		Breed individual;
		float fitness;
		
		Fit(Breed individual, float fitness) {
			this.individual = individual;
			this.fitness = fitness;
		}
		
		public String toString() {
			return individual + "\t" + fitness;
		}
	}
	
	
	public String toString() {
		String ret = "Population of " + population.length + "\n";
		ret += "Similarity: " + similarity() + "\n";
		for (Breed b : population)
			ret += b + "\n";
		return ret;
	}
	
}
