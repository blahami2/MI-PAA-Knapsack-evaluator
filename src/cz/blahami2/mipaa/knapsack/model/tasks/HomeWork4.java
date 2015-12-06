package cz.blahami2.mipaa.knapsack.model.tasks;

import cz.blahami2.mipaa.knapsack.InstanceNumbers;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.Result;
import cz.blahami2.mipaa.knapsack.model.algorithm.SimulatedEvolution;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.CrossoverStrategy;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.EvolutionConfiguration;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.Logger;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.MutationStrategy;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.SelectionStrategy;
import cz.blahami2.mipaa.knapsack.model.chart.Axis;
import cz.blahami2.mipaa.knapsack.model.chart.Chart;
import cz.blahami2.mipaa.knapsack.model.chart.PointSet;
import cz.blahami2.mipaa.knapsack.model.table.Column;
import cz.blahami2.mipaa.knapsack.model.table.DataTable;
import cz.blahami2.mipaa.knapsack.utils.AlgorithmUtils;
import cz.blahami2.mipaa.knapsack.utils.FormatUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author Michael Blaha
 */
public class HomeWork4 extends HomeWorkTask {

    public HomeWork4( int warmup, int approx ) throws IOException {
        super( warmup, approx );
    }

    @Override
    public void run() {
        InstanceNumbers instanceNumbers = InstanceNumbers.SINGLE_NUMBER_40;
        List<EvolutionConfiguration> evolConfigList = new ArrayList<>();
        final int DEFAULT_POPULATION = 100;
        final int DEFAULT_GENERATIONS = 150;
        final double DEFAULT_CROSSOVER = 0.85;
        final double DEFAULT_MUTATION = 0.05;
        final double DEFAULT_ELITISM = 0.03;
        final double DEFAULT_INVALID = 0.2;
        final SelectionStrategy DEFAULT_SELECTION_STRATEGY = SelectionStrategy.ROULETTE_WHEEL;
        final CrossoverStrategy DEFAULT_CROSSOVER_STRATEGY = CrossoverStrategy.TWO_POINT;
        final MutationStrategy DEFAULT_MUTATION_STRATEGY = MutationStrategy.RANDOM_BIT_INVERSION;
        /* TEST */
        evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, 0, DEFAULT_ELITISM, DEFAULT_INVALID,
                                                        DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
        evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, 0.05, DEFAULT_ELITISM, DEFAULT_INVALID,
                                                        DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, DEFAULT_MUTATION, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                        DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
        /* TEST END */

//        for ( int population = 100; population < 5000; population *= 3 ) {
//            evolConfigList.add( new EvolutionConfiguration( population, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, DEFAULT_MUTATION, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                            DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        }
//        for ( int generations = 100; generations < 1000; generations *= 3 ) {
//            evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, generations, DEFAULT_CROSSOVER, DEFAULT_MUTATION, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                            DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        }
//        for ( double crossover = 0; crossover <= 1; crossover += 0.2 ) {
//            evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, crossover, DEFAULT_MUTATION, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                            DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        }
//        for ( double mutation = 0; mutation <= 1; mutation += 0.2 ) {
//            evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, mutation, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                            DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        }
//        for ( double elitism = 0; elitism <= 1; elitism += 0.2 ) {
//            evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, DEFAULT_MUTATION, elitism, DEFAULT_INVALID,
//                                                            DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        }
//        for ( double invalid = 0; invalid <= 1; invalid += 0.2 ) {
//            evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, DEFAULT_MUTATION, DEFAULT_ELITISM, invalid,
//                                                            DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        }
//        evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, DEFAULT_MUTATION, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                        SelectionStrategy.RANKING, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, DEFAULT_MUTATION, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                        DEFAULT_SELECTION_STRATEGY, CrossoverStrategy.ONE_POINT, DEFAULT_MUTATION_STRATEGY ) );
        try {
            for ( EvolutionConfiguration evolConfig : evolConfigList ) {
                getDataManager().nextVersion();
                System.out.println( "running-evolutionconfig=" + evolConfig );

                SimulatedEvolution evolution = new SimulatedEvolution( getWarmup(), getApprox() );
                List<Result> results = AlgorithmUtils.runAlgorithm( getDataManager(), evolution, instanceNumbers, evolConfig );
                getDataManager().saveTable( new DataTable( "hw4_time", instanceNumbers.getInstanceNumbers().size() )
                        .addColumn( Column.newColumn( "Počet předmětů", instanceNumbers.getInstanceNumbers().stream().map( obj -> Integer.toString( obj ) ) ) )
                        .addColumn( Column.newColumn( "Počet instancí", results.stream().map( obj -> Integer.toString( obj.getResults().size() ) ) ) )
                        .addColumn( Column.newColumn( "Čas evoluce [milisekundy]", results, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
                );

                getDataManager().saveTable( new DataTable( "hw4_error", instanceNumbers.getInstanceNumbers().size() )
                        .addColumn( Column.newColumn( "Počet předmětů", instanceNumbers.getInstanceNumbers().stream().map( obj -> Integer.toString( obj ) ) ) )
                        .addColumn( Column.newColumn( "Počet instancí", results.stream().map( obj -> Integer.toString( obj.getResults().size() ) ) ) )
                        .addColumn( Column.newColumn( "Průměrná relativní chyba evoluce", results, (Result obj) -> FormatUtils.formatDecimal( obj.getAvgError() ) ) )
                        .addColumn( Column.newColumn( "Maximální relativní chyba evoluce", results, (Result obj) -> FormatUtils.formatDecimal( obj.getMaxError() ) ) )
                );

                Logger log = evolution.getLogger();
                Set<Knapsack> knapsacks = log.getAllKnapsacks();
                List<Integer> steps = new ArrayList<>();
//        System.out.println( log.toString() );
//        System.out.println( "steps = " + log.getStepsCount( knapsacks.stream().findAny().get() ) );
                for ( int i = 0; i < log.getStepsCount( knapsacks.stream().findAny().get() ); i++ ) {
                    steps.add( i );
                }
                List<Integer> bestPrices = new ArrayList<>();
                List<Integer> worstPrices = new ArrayList<>();
                List<Integer> avgPrices = new ArrayList<>();
                for ( Integer step : steps ) {
                    int bestPriceSum = 0;
                    int worstPriceSum = 0;
                    double averagePriceSum = 0;
                    for ( Knapsack knapsack : knapsacks ) {
                        Logger.LogResult logResult = log.getAverage( knapsack, step );
                        bestPriceSum += logResult.best;
                        worstPriceSum += logResult.worst;
                        averagePriceSum += logResult.average;
                    }
                    bestPrices.add( bestPriceSum / knapsacks.size() );
                    worstPrices.add( worstPriceSum / knapsacks.size() );
                    avgPrices.add( (int) ( averagePriceSum / knapsacks.size() ) );
                }

//        System.out.println( "Done preparing data for graph" );
                getDataManager().saveChart( Chart.builder()
                        .setTitle( "Konvergence evolučního algoritmu" )
                        .setXAxis(
                                Axis.builder()
                                .setLabel( "generace" )
                                .build()
                        )
                        .setYAxis(
                                Axis.builder()
                                .setLabel( "cena" )
                                .build()
                        )
                        .addPointSet(
                                PointSet.fromData( "Nejlepší individuum",
                                                   steps, (Integer obj) -> ( obj ),
                                                   bestPrices, (Integer obj) -> ( obj ) ) )
                        .addPointSet(
                                PointSet.fromData( "Průměrné individuum",
                                                   steps, (Integer obj) -> ( obj ),
                                                   avgPrices, (Integer obj) -> ( obj ) ) )
                        .addPointSet(
                                PointSet.fromData( "Nejhorší individuum",
                                                   steps, (Integer obj) -> ( obj ),
                                                   worstPrices, (Integer obj) -> ( obj ) ) )
                        .build(), "konvergence" );
                getDataManager().saveEvolutionConfiguration( evolConfig, 1 );
            }
        } catch ( IOException ex ) {
            java.util.logging.Logger.getLogger( HomeWork4.class.getName() ).log( Level.SEVERE, null, ex );
            System.exit( 1 );
        }
    }

}
