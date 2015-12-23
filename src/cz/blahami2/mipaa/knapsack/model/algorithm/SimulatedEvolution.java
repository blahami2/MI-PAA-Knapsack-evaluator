package cz.blahami2.mipaa.knapsack.model.algorithm;

import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.BitArray;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.CrossoverStrategy;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.Evaluable;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.MutationStrategy;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.EvolutionConfiguration;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.InputData;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.Logger;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.SelectionStrategy;
import cz.blahami2.mipaa.knapsack.utils.RandomUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Michael Bláha
 */
public class SimulatedEvolution {

//    static {
//        System.err.println( "WARNING: SimulatedEvolution works only up to 99 items. See SimulatedEvolution.getFitness(KnapsackConfig) for details." );
//    }
    private final Logger logger = new Logger();
    private final SelectionStrategy.Creator selectionCreator;
    private final CrossoverStrategy.Creator crossoverCreator;
    private final Creator creator;

    public SimulatedEvolution( int warmupCycles, int runCycles, Creator creator ) {
        this.creator = creator;
        this.selectionCreator = (SelectionStrategy.Creator<BitArrayEvaluable>) (BitArrayEvaluable original) -> creator.createCopyFrom( original );
        this.crossoverCreator = (CrossoverStrategy.Creator<BitArrayEvaluable>) (BitArrayEvaluable original) -> creator.createCopyFrom( original );
    }

    public BitArrayEvaluable solve( BitArrayEvaluable defaultConfig, EvolutionConfiguration evolutionConfiguration ) {
        BitArrayEvaluable resultConfig = null;
        List<BitArrayEvaluable> population = initPopulation( defaultConfig, evolutionConfiguration.getPopulationSize() );// init population 
        int eliteCount = evolutionConfiguration.getEliteCount();
        if ( evolutionConfiguration.getPopulationSize() % 2 == 0 && eliteCount % 2 == 1 ) {
            eliteCount++; // even count
        } else if ( ( evolutionConfiguration.getPopulationSize() % 2 == 1 && eliteCount % 2 == 0 ) ) {
            eliteCount++; // odd count
        }
        for ( int counter = 0; counter < evolutionConfiguration.getNumberOfGenerations(); counter++ ) {
            /* counting fitness */
            int totalCost = population.stream().reduce( 0, (x, y) -> x + y.evaluateCost(), (x, y) -> x + y );
            double averageCost = (double) totalCost / (double) population.size();
            population.stream().forEach( (cfg) -> cfg.evaluateFitness( averageCost, evolutionConfiguration.getInvalidRate() ) );
            /* logging results */
            Collections.sort( population );
            BitArrayEvaluable bestConfig = null;
            BitArrayEvaluable worstConfig = null;
            for ( int i = 0; i < population.size(); i++ ) {
                if ( population.get( i ).isValid() ) {
                    worstConfig = (BitArrayEvaluable) selectionCreator.createCopyFrom( population.get( i ) );
                    break;
                }
            }
            for ( int i = population.size() - 1; i >= 0; i-- ) {
                if ( population.get( i ).isValid() ) {
                    bestConfig = (BitArrayEvaluable) selectionCreator.createCopyFrom( population.get( i ) );
                    break;
                }
            }
            if ( bestConfig == null ) {
                if ( worstConfig != null ) {
                    throw new AssertionError( "wtf?" );
                }
                bestConfig = creator.createDefault();
                worstConfig = creator.createDefault();
            }
            List<BitArrayEvaluable> validStream = population.stream().filter( (cfg) -> cfg.isValid() ).collect( Collectors.toCollection( ArrayList::new ) );
            int totalPrice = validStream.stream().reduce( 0, (x, y) -> x + y.evaluateCost(), (x, y) -> x + y );
            logger.log( defaultConfig.getInputData(), counter, bestConfig, worstConfig, (double) totalPrice / validStream.size() );
            if ( resultConfig == null
                 || resultConfig.compareTo( bestConfig ) < 0 ) {
                resultConfig = creator.createCopyFrom( bestConfig );
            }
            /* elites */
            List<BitArrayEvaluable> elites = population.stream().sorted( Evaluable.ComparatorReversed.INSTANCE ).limit( eliteCount ).collect( Collectors.toCollection( ArrayList::new ) );
            /* PHASE: selection */
            List<BitArrayEvaluable> tmpPopulation = select( evolutionConfiguration.getSelectionStrategy(), population );
            population.clear();
            /* PHASE: crossover */
            // filling population!
            if ( RandomUtils.nextDouble() <= evolutionConfiguration.getCrossoverRate() ) { // if crossover happening
                while ( population.size() + elites.size() < evolutionConfiguration.getPopulationSize() ) {
                    BitArrayEvaluable aConfig = random( tmpPopulation );
                    BitArrayEvaluable bConfig = random( tmpPopulation );
                    CrossoverStrategy.Duo<BitArrayEvaluable> crossoverResult = crossover( evolutionConfiguration.getCrossoverStrategy(), aConfig, bConfig );
                    population.add( crossoverResult.a );
                    population.add( crossoverResult.b );
                }
            } else { // else add old population randomly
                while ( population.size() + elites.size() < evolutionConfiguration.getPopulationSize() ) {
                    population.add( random( tmpPopulation ) );
                }
            }
            /* PHASE: mutation */
            // MUTATION RATE???
            // foreach: random <= mutationRate ? -> mutate
            population.stream().filter( (cfg) -> ( RandomUtils.nextDouble() <= evolutionConfiguration.getMutationRate() ) ).forEach( (cfg) -> {
                mutate( evolutionConfiguration.getMutationStrategy(), cfg );
            } );
            /* PHASE: elite */
            population.addAll( elites );
        }
        if ( resultConfig == null ) {
            throw new AssertionError( "no assign" );
        }
        return resultConfig;
    }

    public Logger getLogger() {
        return logger;
    }

    private BitArrayEvaluable findBest( List<BitArrayEvaluable> population ) {
        return population.stream().filter( (cfg) -> cfg.isValid() ).max( Evaluable.ComparatorNatural.INSTANCE ).get();
    }

    private BitArrayEvaluable findWorst( List<BitArrayEvaluable> population ) {
        return population.stream().filter( (cfg) -> cfg.isValid() ).min( Evaluable.ComparatorNatural.INSTANCE ).get();
    }

    private BitArrayEvaluable random( List<BitArrayEvaluable> population ) {
        return population.get( RandomUtils.nextInt( population.size() ) );
    }

    private List<BitArrayEvaluable> initPopulation( BitArrayEvaluable defaultConfig, int populationSize ) {
        List<BitArrayEvaluable> population = new ArrayList<>();
        for ( int i = 0; i < defaultConfig.size(); i++ ) {
            BitArrayEvaluable k = creator.createCopyFrom( defaultConfig );
            k.set( i, true );
            population.add( k );
        }
        while ( population.size() < populationSize ) {
            BitArrayEvaluable k = creator.createCopyFrom( defaultConfig );
            for ( int i = 0; i < defaultConfig.size(); i++ ) {
                k.set( i, RandomUtils.nextDouble() >= 0.5 );
            }
            population.add( k );
        }
        return population;
    }

    private List<BitArrayEvaluable> select( SelectionStrategy strategy, List<BitArrayEvaluable> population ) {
        return (List<BitArrayEvaluable>) strategy.select( population, selectionCreator );
    }

    private BitArrayEvaluable mutate( MutationStrategy strategy, BitArrayEvaluable config ) {
        return (BitArrayEvaluable) strategy.mutate( config );
    }

    private CrossoverStrategy.Duo<BitArrayEvaluable> crossover( CrossoverStrategy strategy, BitArrayEvaluable aConfig, BitArrayEvaluable bConfig ) {
        return strategy.crossover( aConfig, bConfig, crossoverCreator );
    }

    private boolean isTerminationConditionFulfilled( EvolutionConfiguration evolutionConfiguration, int counter ) {
        return counter < evolutionConfiguration.getNumberOfGenerations();
    }

    public interface Creator<T extends BitArrayEvaluable> {

        T createDefault();

        T createCopyFrom( T original );
    }

    public interface BitArrayEvaluable extends Evaluable, BitArray {
        InputData getInputData();
    }

}
