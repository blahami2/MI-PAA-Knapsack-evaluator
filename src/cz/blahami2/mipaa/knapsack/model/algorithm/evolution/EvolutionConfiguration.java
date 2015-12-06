package cz.blahami2.mipaa.knapsack.model.algorithm.evolution;

/**
 *
 * @author Michael Bláha
 */
public class EvolutionConfiguration {
    
    private final int populationSize;
    private final int numberOfGenerations;
    private final double crossoverRate;
    private final double mutationRate;
    private final double elitismRate;
    private final double invalidRate;
    private final SelectionStrategy selectionStrategy;
    private final CrossoverStrategy crossoverStrategy;
    private final MutationStrategy mutationStrategy;

    public EvolutionConfiguration(int populationSize, int numberOfGenerations, double crossoverRate, double mutationRate, double elitismRate, double invalidRate, SelectionStrategy selectionStrategy, CrossoverStrategy crossoverStrategy, MutationStrategy mutationStrategy) {
        this.populationSize = populationSize;
        this.numberOfGenerations = numberOfGenerations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.elitismRate = elitismRate;
        this.invalidRate = invalidRate;
        this.selectionStrategy = selectionStrategy;
        this.crossoverStrategy = crossoverStrategy;
        this.mutationStrategy = mutationStrategy;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public SelectionStrategy getSelectionStrategy() {
        return selectionStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    public MutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }

    public double getElitismRate() {
        return elitismRate;
    }

    public int getNumberOfGenerations() {
        return numberOfGenerations;
    }
    
    public int getEliteCount(){
        return (int) (elitismRate * populationSize);
    }

    public double getInvalidRate() {
        return invalidRate;
    }

    @Override
    public String toString() {
        return "EvolutionConfiguration{" + "populationSize=" + populationSize + ", numberOfGenerations=" + numberOfGenerations +", crossoverRate=" + crossoverRate + ", mutationRate=" + mutationRate + ", elitismRate=" + elitismRate + ", invalidRate=" + invalidRate + ", selectionStrategy=" + selectionStrategy.name() + ", crossoverStrategy=" + crossoverStrategy.name() + ", mutationStrategy=" + mutationStrategy.name() +  '}';
    }
    
    
    
}
