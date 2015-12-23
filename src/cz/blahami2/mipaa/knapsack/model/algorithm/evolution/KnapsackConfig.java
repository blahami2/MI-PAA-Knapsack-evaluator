package cz.blahami2.mipaa.knapsack.model.algorithm.evolution;

import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.algorithm.SimulatedEvolution;

/**
 *
 * @author Michael Bláha
 */
public class KnapsackConfig extends cz.blahami2.mipaa.knapsack.model.KnapsackConfig implements SimulatedEvolution.BitArrayEvaluable {

    private int cost = 0;
    private double fitness = 0;

    public KnapsackConfig(Knapsack knapsack) {
        super(knapsack);
    }

    public KnapsackConfig(cz.blahami2.mipaa.knapsack.model.KnapsackConfig knapsackConfig) {
        super(knapsackConfig);
    }

    @Override
    public int evaluateCost() {
        this.cost = getPrice();
        return this.cost;
    }

    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public double getFitness() {
        return this.fitness;
    }

    @Override
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public int size() {
        return getItemCount();
    }

    @Override
    public double getInvalidRatio() {
        return getWeight() / getKnapsack().getMaxWeight();
    }

    @Override
    public int compareTo(Object o) {
        return ComparatorNatural.INSTANCE.compare(this, (KnapsackConfig) o);
    }

    @Override
    public int getUniqueInputId() {
        return getKnapsack().getId();
    }

    @Override
    public InputData getInputData() {
        return getKnapsack();
    }

    public enum ComparatorNatural implements java.util.Comparator<KnapsackConfig> {
        INSTANCE;

        @Override
        public int compare(KnapsackConfig o1, KnapsackConfig o2) {
            return Double.compare(o1.getFitness(), o2.getFitness());
        }

    }

    public enum ComparatorReversed implements java.util.Comparator<KnapsackConfig> {
        INSTANCE;

        @Override
        public int compare(KnapsackConfig o1, KnapsackConfig o2) {
            return Double.compare(o2.getFitness(), o1.getFitness());
        }

    }
}
