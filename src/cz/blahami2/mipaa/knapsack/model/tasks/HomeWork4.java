package cz.blahami2.mipaa.knapsack.model.tasks;

import cz.blahami2.mipaa.knapsack.InstanceNumbers;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.model.Result;
import cz.blahami2.mipaa.knapsack.model.algorithm.SimulatedEvolution;
import cz.blahami2.mipaa.knapsack.model.algorithm.SimulatedEvolutionKnapsack;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.CrossoverStrategy;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.EvolutionConfiguration;
import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.InputData;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 *
 * @author Michael Blaha
 */
public class HomeWork4 extends HomeWorkTask {

    public HomeWork4() throws IOException {
        super( 5, 25 );
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

        List<KnapsackConfig> expected;
        try {
            List<Knapsack> origKnapsacks = getDataManager().load( 40 );
            expected = getDataManager().loadResults( 40, origKnapsacks );
        } catch ( FileNotFoundException ex ) {
            java.util.logging.Logger.getLogger( HomeWork4.class.getName() ).log( Level.SEVERE, null, ex );
            return;
        }
        // ================== CREATE NEW (getVersion, saveTable) or APPEND (version, appendTable) ====================================
        int fullTableVersion = 1;
//        try {
//            getDataManager().saveTable( fullTable );
//        } catch ( FileNotFoundException ex ) {
//            java.util.logging.Logger.getLogger( HomeWork4.class.getName() ).log( Level.SEVERE, null, ex );
//        }

        /* SINGLE CONFIGURATIONS */
        evolConfigList.add( new EvolutionConfiguration( 300, 300, 1.0, 0.2, DEFAULT_ELITISM, 0.3,
                                                        SelectionStrategy.ROULETTE_WHEEL, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, DEFAULT_MUTATION, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                        DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
        /* TEST */
//        evolConfigList.add( new EvolutionConfiguration( 5, 5, DEFAULT_CROSSOVER, 0, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                        DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, 0, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                        DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
//        evolConfigList.add( new EvolutionConfiguration( DEFAULT_POPULATION, DEFAULT_GENERATIONS, DEFAULT_CROSSOVER, 0.05, DEFAULT_ELITISM, DEFAULT_INVALID,
//                                                        DEFAULT_SELECTION_STRATEGY, DEFAULT_CROSSOVER_STRATEGY, DEFAULT_MUTATION_STRATEGY ) );
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

                SimulatedEvolutionKnapsack evolution = new SimulatedEvolutionKnapsack( getWarmup(), getApprox() );
                List<Result> results = AlgorithmUtils.runAlgorithm( getDataManager(), evolution, instanceNumbers, evolConfig );
                List<Integer> runs = new ArrayList<>();
                for ( Result r : results ) {
                    runs.add( getApprox() );
                }
                System.out.println( "results = " + results.size() );

                Logger log = evolution.getLogger();
                Set<Knapsack> knapsacks = log.getAllInputs().stream().map( (id) -> (Knapsack) id ).collect( Collectors.toCollection( HashSet::new ) );
                List<Integer> steps = new ArrayList<>();
//        System.out.println( log.toString() );
//        System.out.println( "steps = " + log.getStepsCount( knapsacks.stream().findAny().get() ) );
                for ( int i = 0; i < log.getStepsCount( knapsacks.stream().findAny().get() ); i++ ) {
                    steps.add( i );
                }
                List<Integer> bestPrices = new ArrayList<>();
                List<Integer> worstPrices = new ArrayList<>();
                List<Integer> avgPrices = new ArrayList<>();
                List<Double> avgErrors = new ArrayList<>();
                List<Double> maxErrors = new ArrayList<>();
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
                double avgError = 0;
                double maxError = 0;
                for ( Knapsack knapsack : knapsacks ) {
                    for ( KnapsackConfig knapsackConfig : expected ) {
                        if ( knapsackConfig.getKnapsack().getId() == knapsack.getId() ) {
                            double error = countError( knapsackConfig.getPrice(), log.getLowestPrice( knapsack ) );
                            maxError = ( error > maxError ) ? error : maxError;
                            avgError += countError( knapsackConfig.getPrice(), log.getAveragePrice( knapsack ) );
                            break;
                        }
                    }
                }
                avgError /= knapsacks.size();
                avgErrors.add( avgError );
                maxErrors.add( maxError );

                getDataManager().saveTable( new DataTable( "hw4_time", instanceNumbers.getInstanceNumbers().size() )
                        .addColumn( Column.newColumn( "Počet předmětů", instanceNumbers.getInstanceNumbers().stream().map( obj -> Integer.toString( obj ) ) ) )
                        .addColumn( Column.newColumn( "Počet instancí", results.stream().map( obj -> Integer.toString( obj.getResults().size() ) ) ) )
                        .addColumn( Column.newColumn( "Počet běhů", runs.stream().map( obj -> Integer.toString( obj ) ) ) )
                        .addColumn( Column.newColumn( "Čas evoluce [milisekundy]", results, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
                        .addColumn( Column.newColumn( "Průměrná relativní chyba evoluce", avgErrors, (Double obj) -> String.format( "%.09f", obj ).replace( '.', ',' ) ) )
                        .addColumn( Column.newColumn( "Maximální relativní chyba evoluce", maxErrors, (Double obj) -> String.format( "%.09f", obj ).replace( '.', ',' ) ) )
                );
//        System.out.println( "Done preparing data for the graph" );
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
                List<String> data = Arrays.asList(
                        "40",
                        "50",
                        "" + getApprox(),
                        "" + evolConfig.getPopulationSize(),
                        "" + evolConfig.getNumberOfGenerations(),
                        String.format( "%.03f", evolConfig.getCrossoverRate() ),
                        String.format( "%.03f", evolConfig.getMutationRate() ),
                        String.format( "%.03f", evolConfig.getElitismRate() ),
                        String.format( "%.03f", evolConfig.getInvalidRate() ),
                        evolConfig.getSelectionStrategy().name(),
                        evolConfig.getCrossoverStrategy().name(),
                        evolConfig.getMutationStrategy().name(),
                        FormatUtils.formatTimeInNanos( results.get( 0 ).getTime() ),
                        String.format( "%.09f", avgError ).replace( '.', ',' ),
                        String.format( "%.09f", maxError ).replace( '.', ',' )
                );
                DataTable fullTable = new DataTable( "hw4_full", 0 )
                        .addColumn( Column.newColumn( "Počet předmětů", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Počet instancí", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Počet běhů", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Velikost populace", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Počet generací", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Šance křížení", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Šance mutace", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Poměr elity", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Postih nevalidního stavu", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Strategie selekce", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Strategie křížení", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Strategie mutace", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Čas evoluce [milisekundy]", new ArrayList<>() ).setAlignment( Column.Alignment.RIGHT ) )
                        .addColumn( Column.newColumn( "Průměrná relativní chyba evoluce", new ArrayList<>() ) )
                        .addColumn( Column.newColumn( "Maximální relativní chyba evoluce", new ArrayList<>() ) );
                fullTable.addRow( data );
                getDataManager().saveTable( fullTable );
//                getDataManager().appendTable( fullTable, fullTableVersion );
            }
        } catch ( IOException ex ) {
            java.util.logging.Logger.getLogger( HomeWork4.class.getName() ).log( Level.SEVERE, null, ex );
            System.exit( 1 );
        }
    }

    private double countError( int optimal, int real ) {
        if ( optimal == 0 ) {
            return 0;
        }
        return ( (double) optimal - (double) real ) / (double) optimal;
    }

    public void generateGraphs() throws IOException {
        getDataManager().saveChart(
                Chart.builder()
                .setTitle( "Změna průměrné relativní chyby v závislosti na velikosti populace" )
                .setXAxis(
                        Axis.builder()
                        .setLabel( "velikost populace" )
                        .build()
                )
                .setYAxis(
                        Axis.builder()
                        .setLabel( "průměrná relativní chyba" )
                        .divideBy( 1000000 )
                        .setFormat( "%.03f" )
                        .build()
                )
                .addPointSet( PointSet.fromData( "chyba", Arrays.asList( 100, 300, 900, 2700 ), x -> x, Arrays.asList( 0.013635856, 0.002540470, 0.000572264, 0.000129883 ), y -> round( y, 1000000 ) ) )
                .build(), "hw4_ps_avgerr" );
        getDataManager().saveChart(
                Chart.builder()
                .setTitle( "Změna průměrné relativní chyby v závislosti na počtu generací" )
                .setXAxis(
                        Axis.builder()
                        .setLabel( "počet generací" )
                        .build()
                )
                .setYAxis(
                        Axis.builder()
                        .setLabel( "průměrná relativní chyba" )
                        .divideBy( 1000000 )
                        .setFormat( "%.03f" )
                        .build()
                )
                .addPointSet( PointSet.fromData( "chyba", Arrays.asList( 100, 150, 300, 900 ), x -> x, Arrays.asList( 0.017345666, 0.013635856, 0.008838345, 0.004572771 ), y -> round( y, 1000000 ) ) )
                .build(), "hw4_gc_avgerr" );
        getDataManager().saveChart(
                Chart.builder()
                .setTitle( "Změna průměrné relativní chyby v závislosti na šanci křížení" )
                .setXAxis(
                        Axis.builder()
                        .setLabel( "šance křížení" )
                        .divideBy( 100 )
                        .setFormat( "%.02f" )
                        .build()
                )
                .setYAxis(
                        Axis.builder()
                        .setLabel( "průměrná relativní chyba" )
                        .divideBy( 1000000 )
                        .setFormat( "%.03f" )
                        .build()
                )
                .addPointSet( PointSet.fromData( "chyba", Arrays.asList( 0.0, 0.2, 0.4, 0.6, 0.8, 0.85, 1.0 ), x -> round( x, 100 ), Arrays.asList( 0.069236591, 0.036780894, 0.024539310, 0.018371807, 0.013889928, 0.013635856, 0.011224615 ), y -> round( y, 1000000 ) ) )
                .build(), "hw4_cr_avgerr" );
        getDataManager().saveChart(
                Chart.builder()
                .setTitle( "Změna průměrné relativní chyby v závislosti na šanci mutace" )
                .setXAxis(
                        Axis.builder()
                        .setLabel( "šance mutace" )
                        .divideBy( 100 )
                        .setFormat( "%.02f" )
                        .build()
                )
                .setYAxis(
                        Axis.builder()
                        .setLabel( "průměrná relativní chyba" )
                        .divideBy( 1000000 )
                        .setFormat( "%.03f" )
                        .build()
                )
                .addPointSet( PointSet.fromData( "chyba", Arrays.asList( 0.0, 0.05, 0.2, 0.4, 0.6, 0.8, 1.0 ), x -> round( x, 100 ), Arrays.asList( 0.035226299, 0.013635856, 0.005323127, 0.005701325, 0.007382073, 0.008813031, 0.008794242 ), y -> round( y, 1000000 ) ) )
                .build(), "hw4_mr_avgerr" );
        getDataManager().saveChart(
                Chart.builder()
                .setTitle( "Změna průměrné relativní chyby v závislosti na poměru elity" )
                .setXAxis(
                        Axis.builder()
                        .setLabel( "poměr elity" )
                        .divideBy( 100 )
                        .setFormat( "%.02f" )
                        .build()
                )
                .setYAxis(
                        Axis.builder()
                        .setLabel( "průměrná relativní chyba" )
                        .divideBy( 1000000 )
                        .setFormat( "%.03f" )
                        .build()
                )
                .addPointSet( PointSet.fromData( "chyba", Arrays.asList( 0.0, 0.03, 0.2, 0.4, 0.6, 0.8, 1.0 ), x -> round( x, 100 ), Arrays.asList( 0.061391803, 0.013635856, 0.020500364, 0.021821063, 0.023735697, 0.027103188, 0.244338742 ), y -> round( y, 1000000 ) ) )
                .build(), "hw4_er_avgerr" );
        getDataManager().saveChart(
                Chart.builder()
                .setTitle( "Změna průměrné relativní chyby v závislosti na postihu nevalidního stavu" )
                .setXAxis(
                        Axis.builder()
                        .setLabel( "postih nevalidního stavu" )
                        .divideBy( 10 )
                        .setFormat( "%.02f" )
                        .build()
                )
                .setYAxis(
                        Axis.builder()
                        .setLabel( "průměrná relativní chyba" )
                        .divideBy( 1000000 )
                        .setFormat( "%.03f" )
                        .build()
                )
                .addPointSet( PointSet.fromData( "chyba", Arrays.asList( 0.0, 0.2, 0.4, 0.6, 0.8, 1.0 ), x -> round( x, 10 ), Arrays.asList( 0.014279544, 0.013612894, 0.012776043, 0.025276351, 0.025861834, 0.995559265 ), y -> round( y, 1000000 ) ) )
                .build(), "hw4_ir_avgerr" );
//        getDataManager().saveChart(
//                Chart.builder()
//                .setTitle( "Změna průměrné relativní chyby v závislosti na velikosti populace" )
//                .setXAxis(
//                        Axis.builder()
//                        .setLabel( "velikost populace" )
//                        .divideBy( 10 )
//                        .setFormat( "%.01f" )
//                        .build()
//                )
//                .setYAxis(
//                        Axis.builder()
//                        .setLabel( "doba výpočtu [milisekundy]" )
//                        .build()
//                ).build(), "hw4_time_ratio" );
    }

}
