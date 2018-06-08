package genetics;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.List;

import methods.P;
import structures.LList;

public class Genetic {
	
	private Breed[] population;
	private float mutateChance, similarityCutoff, similarityVarCutoff, killPercent, killChance, breedChance;
	
	boolean competitive;
	
	private float[] lastSims;
	private int loc;
	
	private int size;
	
	private int bestOf;
	
	private Comparator<Fit> f;
	
	private Test test;
	
	private Algorithm calculateFitness, sort, kill, breed, mutate;
	
	private boolean division; // if true, asexual reproduction, if false, sexual reproduction
	
	public final Test SIMILARITY_TEST = pop -> loc >= lastSims.length && varianceOfSims() <= similarityVarCutoff && lastSim() < similarityCutoff;
	
	// they need to beat all other networks
	public final Test BEST_PLAYER_TEST = pop -> !pop.getFirst().lost;
	
	public final Algorithm COMPETITIVE_FITNESS = pop -> {
		for (int i = 0; i < bestOf; i++) {
			pop.actPairs((a, b) -> {
				if (((Competitive) a.individual).better((Competitive) b.individual)) {
					a.fitness++;
					b.fitness--;
					b.lost = true;
					
				} else {
					a.fitness--;
					b.fitness++;
					a.lost = true;
				}
			});
		}
		for (Fit f : pop) {
			f.fitness -= f.individual.subtractor();
		}
	};
	
	public final Algorithm ABSOLUTE_FITNESS = pop -> {
		for (Fit p : pop)
			p.fitness = ((Absolute) p.individual).fitness();
	};
	
	public final Algorithm DEFAULT_SORT = pop -> pop.sort(f);
	
	public final Algorithm DEFAULT_KILL = pop -> {
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
	};
	
	public final Algorithm DIVISION_BREEDING = pop -> {
		LList<Fit> adds = new LList<>();
		int numBabies = size - pop.size();
		for (int i = 0; i < numBabies; i++) {
			adds.add(new Fit(randomMutation(pop)));
		}
		for (Fit f : adds)
			pop.add(f);
	};
	
	public final Algorithm REPRODUCTIVE_BREEDING = pop -> {
		LList<Fit> adds = new LList<>();
		int numBabies = size - pop.size();
		for (int i = 0; i < numBabies; i++) {
			adds.add(new Fit(randomBreed(pop)));
		}
		for (Fit f : adds)
			pop.add(f);
	};
	
	public final Algorithm DEFAULT_MUTATION = pop -> {
		for (Fit f : pop) {
			if (Math.random() < mutateChance)
				f.individual = f.individual.mutate();
		}
	};
	
	
	public Genetic(Breed... population) {
		this.population = population;
		setHyperParameters(.04f, .1f, .002f, .5f, .2f, .01f, 4, 1, false);
	}
	
	public Genetic(List<Breed> population) {
		this.population = (Breed[]) Array.newInstance(population.get(0).getClass(), population.size());
		population.toArray(this.population);
		setHyperParameters(.04f, .1f, .002f, .5f, .2f, .01f, 4, 1, false);
	}
	
	public void setTest(Test t) {
		test = t;
	}
	
	public void setDefaultAlgorithms() {
		if (competitive) {
			if (division)
				setAlgorithms(COMPETITIVE_FITNESS, DEFAULT_SORT, DEFAULT_KILL, DIVISION_BREEDING, pop -> {});
			else
				setAlgorithms(COMPETITIVE_FITNESS, DEFAULT_SORT, DEFAULT_KILL, REPRODUCTIVE_BREEDING, DEFAULT_MUTATION);
		} else {
			if (division)
				setAlgorithms(ABSOLUTE_FITNESS, DEFAULT_SORT, DEFAULT_KILL, DIVISION_BREEDING, pop -> {});
			else
				setAlgorithms(ABSOLUTE_FITNESS, DEFAULT_SORT, DEFAULT_KILL, REPRODUCTIVE_BREEDING, DEFAULT_MUTATION);
		}
	}
	
	public void setAlgorithms(Algorithm fitness, Algorithm sort, Algorithm kill, Algorithm breed, Algorithm mutate) {
		this.calculateFitness = fitness;
		this.sort = sort;
		this.kill = kill;
		this.breed = breed;
		this.mutate = mutate;
	}
	
	public void setHyperParameters(float mutateChance, float similarityCutoff, float similarityVarCutoff, float killPercent,
			float killChance, float breedChance, int numSimilaritiesKept, int bestOf, boolean division) {
		this.mutateChance = mutateChance;
		this.similarityCutoff = similarityCutoff;
		this.similarityVarCutoff = similarityVarCutoff;
		this.killPercent = killPercent;
		this.killChance = killChance;
		this.breedChance = breedChance;
		this.division = division;
		lastSims = new float[numSimilaritiesKept];
		loc = 0;
		
		this.bestOf = bestOf;
		
		competitive = Competitive.class.isAssignableFrom(population[0].getClass());
		
		if (competitive)
			f = (a, b) -> {
				if (a.fitness < b.fitness)
					return 1;
				if (a.fitness == b.fitness)
					return 0;
				return -1;
			};
		else
			f = (a, b) -> {
				if (a.fitness < b.fitness)
					return -1;
				if (a.fitness == b.fitness)
					return 0;
				return 1;
			};
	}
	
	public Breed runDivided(Integer...divs) {
		LList<Integer> l = new LList<>(divs);
		LList<Fit> pop = genFit();
		Breed winner = runDividedH(pop, l);
		fillPop(pop);
		return winner;
	}
	
	private Breed runDividedH(LList<Fit> pop, LList<Integer> divs) {
		if (divs.size() == 0)
			return run(pop);
		
		LList<LList<Fit>> pops = pop.divideBy(divs.removeFirst());
		
		LList<Fit> now = new LList<>();
		for (LList<Fit> p : pops)
			now.add(new Fit(runDividedH(p, divs)));
		return run(now);
	}
	
	public Breed run() {
		LList<Fit> pop = genFit();
		Breed winner = run(pop);
		fillPop(pop);
		return winner;
	}
	
	private Breed run(LList<Fit> pop) {
		int generations = 0;
		size = pop.size();
		
		System.out.println("Running " + pop);
		
		do {
			generations++;
			clearFitness(pop);
			calculateFitness.act(pop);
			sort.act(pop);
			kill.act(pop);
			
			if (test == SIMILARITY_TEST) {
				addSimilarity(similarity(pop));
				if (generations % 100 == 0)
					P.pl("Generation " + generations + ":\tsimilarity: " + lastSim() + "\tvariance: " + varianceOfSims());
			}
			else if (test == BEST_PLAYER_TEST) {
				if (generations % 100 == 0)
					P.pl("Generation " + generations + ":\ttop player's points: " + pop.getFirst().fitness);
			}
			else {
				addSimilarity(similarity(pop));
				if (generations % 100 == 0)
					P.pl("Generation " + generations + ":\ttop player's points: " + pop.getFirst().fitness);
			}
			
			breed.act(pop);
			
			
			if (test.pass(pop))
				break;
			
			mutate.act(pop);
		} while (true);
		pop.sort(f);
		P.pl("Winner emerges in generation " + generations + ":\ttop player's points: " + pop.getFirst().fitness);
		return pop.getFirst().individual;
	}
	
	private void addSimilarity(float s) {
		lastSims[loc % lastSims.length] = s;
		
		if (loc >= lastSims.length - 1)
			loc = (loc + 1) % lastSims.length + lastSims.length;
		else
			loc = (loc + 1) % lastSims.length;
	}
	
	private float lastSim() {
		return lastSims[(loc + lastSims.length - 1) % lastSims.length];
	}
	
	private float varianceOfSims() {
		if (loc <= 1)
			return Float.MAX_VALUE;
		float avg = 0;
		int numS = Math.min(loc, lastSims.length);
		for (int i = 0; i < numS; i++)
			avg += lastSims[i];
		avg /= numS;
		float var = 0;
		for (int i = 0; i < numS; i++)
			var += square(lastSims[i] - avg);
		return var / ((numS - 1) * avg * avg);
	}
	
	private static float square(float f) {
		return f * f;
	}
	
	private LList<Fit> genFit() {
		LList<Fit> pop = new LList<>();
		for (int i = 0; i < population.length; i++)
			pop.add(new Fit(population[i]));
		return pop;
	}
	
	private void clearFitness(LList<Fit> pop) {
		for (Fit f : pop) {
			f.fitness = 0;
			f.lost = false;
		}
	}
	
	private Breed randomMutation(LList<Fit> pop) {
		Breed mom = null;
		while (mom == null) {
			for (Fit f : pop) {
				if (Math.random() < breedChance) {
					mom = f.individual;
					break;
				}
			}
		}
		return mom.mutate();
	}
	
	private Breed randomBreed(LList<Fit> pop) {
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
	
	private void fillPop(LList<Fit> pop) {
		int l = 0;
		for (Fit f : pop)
			population[l++] = f.individual;
	}
	
	public float similarity(LList<Fit> pop) {
		float sum = pop.sumPairs((a, b) -> a.individual.similarity(b.individual));
		return sum * 2 / (population.length * (population.length + 1));
	}
	
	public float similarity(Breed[] pop) {
		float sum = 0;
		for (int i = 0; i < pop.length; i++) {
			for (int j = i + 1; j < pop.length; j++)
				sum += pop[i].similarity(pop[j]);
		}
		return sum * 2 / (pop.length * (pop.length - 1));
	}
	
	public class Fit {
		Breed individual;
		float fitness;
		boolean lost;
		
		Fit(Breed individual) {
			this.individual = individual;
			fitness = -Float.MAX_VALUE;
			lost = false;
		}
		
		public String toString() {
			return individual + "\t" + fitness;
		}
	}
	
	public static interface Test {
		boolean pass(LList<Fit> pop);
	}
	
	public static interface Algorithm {
		void act(LList<Fit> pop);
	}
	
	
	public String toString() {
		String ret = "Population of " + population.length + "\n";
		ret += "Similarity: " + similarity(population) + "\n";
		for (Breed b : population)
			ret += b + "\n";
		return ret;
	}
	
}
