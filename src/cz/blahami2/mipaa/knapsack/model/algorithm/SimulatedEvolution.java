package cz.blahami2.mipaa.knapsack.model.algorithm;

import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.CrossoverStrategy;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.MutationStrategy;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.EvolutionConfiguration;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.Logger;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.SelectionStrategy;
import cz.blahami2.mipaa.knapsack.utils.RandomUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Michael Bláha
 */
public class SimulatedEvolution extends Algorithm<EvolutionConfiguration> {

//    static {
//        System.err.println( "WARNING: SimulatedEvolution works only up to 99 items. See SimulatedEvolution.getFitness(KnapsackConfig) for details." );
//    }

    private final Logger logger = new Logger();
    private static final SelectionStrategy.Creator<KnapsackConfig> SELECTION_CREATOR = (KnapsackConfig original) -> new KnapsackConfig( original );
    private static final CrossoverStrategy.Creator<KnapsackConfig> CROSSOVER_CREATOR = (KnapsackConfig original) -> new KnapsackConfig( original );

    public SimulatedEvolution( int warmupCycles, int runCycles ) {
        super( warmupCycles, runCycles );
    }

    @Override
    public KnapsackConfig solve( Knapsack knapsack, EvolutionConfiguration evolutionConfiguration ) {
        KnapsackConfig resultConfig = null;
        List<KnapsackConfig> population = initPopulation( knapsack, evolutionConfiguration.getPopulationSize() );// init population 
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
            Collections.sort( population, KnapsackConfig.ComparatorNatural.INSTANCE );
            KnapsackConfig bestConfig = null;
            KnapsackConfig worstConfig = null;
            for ( int i = 0; i < population.size(); i++ ) {
                if ( population.get( i ).isValid() ) {
                    worstConfig = new KnapsackConfig( population.get( i ) );
                    break;
                }
            }
            for ( int i = population.size() - 1; i >= 0; i-- ) {
                if ( population.get( i ).isValid() ) {
                    bestConfig = new KnapsackConfig( population.get( i ) );
                    break;
                }
            }
            if ( bestConfig == null ) {
                if ( worstConfig != null ) {
                    throw new AssertionError( "wtf?" );
                }
                bestConfig = (KnapsackConfig) new KnapsackConfig( knapsack ).setFromLongInteger( 0 );
                worstConfig = (KnapsackConfig) new KnapsackConfig( knapsack ).setFromLongInteger( 0 );
            }
            List<KnapsackConfig> validStream = population.stream().filter( (cfg) -> cfg.isValid() ).collect( Collectors.toCollection( ArrayList::new ) );
            int totalPrice = validStream.stream().reduce( 0, (x, y) -> x + y.getPrice(), (x, y) -> x + y );
            logger.log( knapsack, counter, bestConfig, worstConfig, (double) totalPrice / validStream.size() );
            if ( resultConfig == null
                 || resultConfig.compareTo( bestConfig ) < 0 ) {
                resultConfig = new KnapsackConfig( bestConfig );
            }
            /* elites */
            List<KnapsackConfig> elites = population.stream().sorted( KnapsackConfig.ComparatorReversed.INSTANCE ).limit( eliteCount ).collect( Collectors.toCollection( ArrayList::new ) );
            /* PHASE: selection */
            List<KnapsackConfig> tmpPopulation = select( evolutionConfiguration.getSelectionStrategy(), population );
            population.clear();
            /* PHASE: crossover */
            // filling population!
            if ( RandomUtils.nextDouble() <= evolutionConfiguration.getCrossoverRate() ) { // if crossover happening
                while ( population.size() + elites.size() < evolutionConfiguration.getPopulationSize() ) {
                    KnapsackConfig aConfig = random( tmpPopulation );
                    KnapsackConfig bConfig = random( tmpPopulation );
                    CrossoverStrategy.Duo<KnapsackConfig> crossoverResult = crossover( evolutionConfiguration.getCrossoverStrategy(), aConfig, bConfig );
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

    private KnapsackConfig findBest( List<KnapsackConfig> population ) {
        return population.stream().filter( (cfg) -> cfg.isValid() ).max( KnapsackConfig.ComparatorNatural.INSTANCE ).get();
    }

    private KnapsackConfig findWorst( List<KnapsackConfig> population ) {
        return population.stream().filter( (cfg) -> cfg.isValid() ).min( KnapsackConfig.ComparatorNatural.INSTANCE ).get();
    }

    private KnapsackConfig random( List<KnapsackConfig> population ) {
        return population.get( RandomUtils.nextInt( population.size() ) );
    }

    private List<KnapsackConfig> initPopulation( Knapsack knapsack, int populationSize ) {
        List<KnapsackConfig> population = new ArrayList<>();
        for(int i = 0; i < knapsack.getItemCount(); i++){
            KnapsackConfig k = new KnapsackConfig(knapsack );
            k.set( i, true );
            population.add( k );
        }
        while ( population.size() < populationSize ) {
            KnapsackConfig k = new KnapsackConfig(knapsack );
            for(int i = 0; i < knapsack.getItemCount(); i++){
                k.set( i, RandomUtils.nextDouble() >= 0.5);
            }
            population.add( k );
        }
        return population;
    }

    private List<KnapsackConfig> select( SelectionStrategy strategy, List<KnapsackConfig> population ) {
        return (List<KnapsackConfig>) strategy.select( population, SELECTION_CREATOR );
    }

    private KnapsackConfig mutate( MutationStrategy strategy, KnapsackConfig config ) {
        return (KnapsackConfig) strategy.mutate( config );
    }

    private CrossoverStrategy.Duo<KnapsackConfig> crossover( CrossoverStrategy strategy, KnapsackConfig aConfig, KnapsackConfig bConfig ) {
        return strategy.crossover( aConfig, bConfig, CROSSOVER_CREATOR );
    }

    private boolean isTerminationConditionFulfilled( EvolutionConfiguration evolutionConfiguration, int counter ) {
        return counter < evolutionConfiguration.getNumberOfGenerations();
    }

}
