package cz.blahami2.mipaa.knapsack.model.algorithm.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates new List of individuals based on their fitness. After selection and
 * returning modified collection, one should randomly choose from the given
 * collection (list.get(rand.nextInt(list.size()))). The individual will be then
 * chosen based on his fitness and selection strategy.
 *
 * @author Michael Bláha
 */
public interface SelectionStrategy {

    public static final SelectionStrategy ROULETTE_WHEEL = new SelectionStrategy() {
        @Override
        public <T extends Evaluable> List<T> select(List<T> population, Creator<T> creator) {
            List<T> tmpPopulation = new ArrayList<>();
            double totalFitness = population.stream().reduce((double) 0, (x, y) -> x + y.getFitness(), (x, y) -> x + y);
            int size = population.size() * 100; // 100 larger than original
            population.stream().forEach((evaluable) -> {
                int count = (int) Math.round(size * (evaluable.getFitness() / totalFitness)); // insert portion of size given by ratio of fitness
                for (int i = 0; i < count; i++) {
                    tmpPopulation.add(creator.createCopyFrom(evaluable));
                }
            });
            return tmpPopulation;
        }

        @Override
        public String name() {
            return "Roulette wheel";
        }
    };
    public static final SelectionStrategy LINEAR_SCALING = new SelectionStrategy() {
        @Override
        public <T extends Evaluable> List<T> select(List<T> population, Creator<T> creator) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String name() {
            return "Linear scaling";
        }
    };
    public static final SelectionStrategy RANKING = new SelectionStrategy() {
        @Override
        public <T extends Evaluable> List<T> select(List<T> population, Creator<T> creator) {
            List<T> tmpPopulation = new ArrayList<>();
            Collections.sort(population, Evaluable.ComparatorNatural.INSTANCE);
            for (int i = 0; i < population.size(); i++) {
                for (int j = 0; j <= i; j++) {
                    tmpPopulation.add(creator.createCopyFrom(population.get(i)));
                }
            }
            return tmpPopulation;
        }

        @Override
        public String name() {
            return "Ranking";
        }
    };
    public static final SelectionStrategy TOURNAMENT = new SelectionStrategy() {
        @Override
        public <T extends Evaluable> List<T> select(List<T> population, Creator<T> creator) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String name() {
            return "Tournament";
        }
    };

    public <T extends Evaluable> List<T> select(List<T> population, Creator<T> creator);
    
    public String name();

    public interface Creator<T extends Evaluable> {

        T createCopyFrom(T original);
    }

}
