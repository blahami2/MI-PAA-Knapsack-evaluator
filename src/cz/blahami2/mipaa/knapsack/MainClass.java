package cz.blahami2.mipaa.knapsack;

import cz.blahami2.mipaa.knapsack.model.Measurement;
import cz.blahami2.mipaa.knapsack.model.chart.Axis;
import cz.blahami2.mipaa.knapsack.model.chart.Chart;
import cz.blahami2.mipaa.knapsack.model.chart.PointSet;
import cz.blahami2.mipaa.knapsack.model.chart.view.ChartExporter;
import cz.blahami2.mipaa.knapsack.model.tasks.*;
import cz.blahami2.mipaa.knapsack.utils.FormatUtils;
import cz.blahami2.mipaa.knapsack.view.GraphViewer;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Michael Blaha
 */
public class MainClass {

    public static void main( String[] args ) throws IOException {
        startTimer();
        new HomeWork3().run();
//        List<Double> data = Arrays.asList( 0.0, -0.5, -1.0, -1.5, -2.0, 0.5, 1.0, 1.5, 2.0 );
//        GraphViewer gv = new GraphViewer();
//        gv.setGraph( ChartExporter.createImage(
//                Chart.builder()
//                .setTitle( "Priklad grafu" )
//                .setXAxis(
//                        Axis.builder()
//                        .setLabel( "x" )
//                        .setValueToStringStrategy( x -> {
//                            double val = x / 10.0;
//                            int d = ( val > 2 ) ? 1 : -1;
//                            double k = d * ( val - 2 );
//                            if ( val - 2 < 0.001 && val - 2 > -0.001 ) {
//                                k = d = 0;
//                            }
//                            return String.format( "%.01f, %d", k, d );
//                        }
//                        )
//                        .build() )
//                .setYAxis(
//                        Axis.builder()
//                        .setLabel( "y" )
//                        .build()
//                )
//                .addPointSet( PointSet.fromData( "dataset", data, x -> (int) Math.round( ( x + 2 ) * 10 ), data, x -> (int) Math.round( ( x + 2 ) * 10 ) ) )
//                .build(), 800, 600 ) );
//        gv.display();
    }

    private static void startTimer() {
        Thread t = new Thread( () -> {
            Measurement m = new Measurement();
            m.start();
            while ( true ) {
                try {
                    Thread.sleep( 600000 );
                    int seconds = (int) ( FormatUtils.nanosToMillis( m.stop() ) / 1000 );
                    int hour = seconds / 3600;
                    seconds %= 3600;
                    int min = seconds / 60;
                    seconds %= 60;
                    System.out.printf( "time-%02d:%02d:%02d\n", hour, min, seconds );
                } catch ( InterruptedException ex ) {
                    java.util.logging.Logger.getLogger( MainClass.class.getName() ).log( Level.SEVERE, null, ex );
                }

            }
        } );
        t.setDaemon( true );
        t.start();
    }

    private static void test() {

//        Random r = new Random();
//        Knapsack k = new Knapsack( 1, 30, 1000 );
//        for ( int i = 0; i < 30; i++ ) {
//            k.addItem( new KnapsackItem( i, i, i ) );
//        }
//        int max = (1 << 30) - 1;
////        System.out.println( "max = " + max );
//        CrossoverStrategy cs = CrossoverStrategy.TWO_POINT;
//        CrossoverStrategy.Creator<cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig> creator = (cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig original) -> new cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig( original );
//
//        cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig a = (cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig) new cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig( k ).setFromLongInteger( 0 );
//        cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig b = (cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig) new cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig( k ).setFromLongInteger( max );
//
//        for ( int i = 0; i < 10; i++ ) {
////            cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig a = (cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig) new cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig( k ).setFromLongInteger( r.nextInt( max ) );
////            cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig b = (cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig) new cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig( k ).setFromLongInteger( r.nextInt( max ) );
//            CrossoverStrategy.Duo crossover = cs.crossover( a, b, creator );
//            System.out.println( "======================================================================================" );
//            System.out.println( "input:" );
//            System.out.println( a );
//            System.out.println( b );
//            System.out.println( "output:" );
//            System.out.println( crossover.a );
//            System.out.println( crossover.b );
//            System.out.println( "======================================================================================" );
//        }
//        cs = CrossoverStrategy.ONE_POINT;
//        for ( int i = 0; i < 10; i++ ) {
////            cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig a = (cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig) new cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig( k ).setFromLongInteger( r.nextInt( max ) );
////            cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig b = (cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig) new cz.blahami2.mipaa.knapsack.model.algorithm.evolution.KnapsackConfig( k ).setFromLongInteger( r.nextInt( max ) );
//            CrossoverStrategy.Duo crossover = cs.crossover( a, b, creator );
//            System.out.println( "======================================================================================" );
//            System.out.println( "input:" );
//            System.out.println( a );
//            System.out.println( b );
//            System.out.println( "output:" );
//            System.out.println( crossover.a );
//            System.out.println( crossover.b );
//            System.out.println( "======================================================================================" );
//        }
//        if(true){
//            throw new AssertionError("TEST crossover only");
//        }
//        System.out.println( InstanceNumbers.SMALL.getInstanceNumbers() );
//        System.out.println( "max = " + InstanceNumbers.FULL.getInstanceNumbers().stream().max( (a, b) -> {
//            return a - b;
//        } ).get() );
//        System.out.println( "sum = " + InstanceNumbers.FULL.getInstanceNumbers().stream().parallel().reduce( (x, y) -> x + y ).get() );
//        String[] list = { "aa", "bbb", "cccc" };
//        System.out.println( "sumString1 = " + Stream.of( list ).parallel().reduce( 0, (x, y) -> x + y.length(), (x, y) -> x + y ) );
//
//        for ( int j = 1; j <= 10; j++ ) {
//            Knapsack knapsack = new Knapsack( j, 10, 100 );
//            Random r = new Random();
//            int wSum = 0;
//            int pSum = 0;
//            for ( int i = 0; i < 10; i++ ) {
//                int w = r.nextInt( 100 );
//                wSum += w;
//                int p = r.nextInt( 500 );
//                pSum += p;
//                knapsack.addItem( new KnapsackItem( i, w, p ) );
//            }
//            System.out.println( "pSum = " + pSum + ", wSum = " + wSum + ", knapsack: p = " + knapsack.getPriceSum() + ", w = " + knapsack.getWeightSum() );
//        }
    }

    public void run4() throws IOException {
//        List<Result> results = new ArrayList<>();
//        List<Integer> instanceCountNumbers = Arrays.asList( 30, 32, 35, 37, 40, 45, 50, 55, 60, 70, 80 );
//        Generator generator = new Generator();
//        SimulatedEvolution evolution = new SimulatedEvolution( WARMUP, APPROX );
//        EvolutionConfiguration evolConfig = new EvolutionConfiguration( 10, 10, 10, 10, 10, SelectionStrategy.TOURNAMENT, CrossoverStrategy.ONE_POINT, MutationStrategy.RANDOM_BIT_INVERSION, 10 );
//        for ( int i = 0; i < instanceCountNumbers.size(); i++ ) {
//            GeneratorConfiguration genConfig = new GeneratorConfiguration( i, instanceCountNumbers.get( i ), 50, 0, 0, 0, 0, 0 );
//            List<Knapsack> input = generator.generateInput( genConfig );
//            List<KnapsackConfig> expected = getExpectedResult( input );
//            results.add( new Result( evolution.run( input, expected, evolConfig ) ) );
//        }
    }

    private void run1He() throws IOException {
//        List<Integer> instanceNumbers = InstanceNumbers.FULL.getInstanceNumbers();
//        Algorithm he = new HeuristicRatio( WARMUP, APPROX );
//        List<Result> heResults = runAlgorithm( he, instanceNumbers );
//
//        dataManager.saveTable( new DataTable( "hw1", instanceNumbers.size() )
//                .addColumn( Column.newColumn( "Počet předmětů", instanceNumbers, (Integer obj) -> obj.toString() ) )
//                .addColumn( Column.newColumn( "Počet instancí", heResults, (Result obj) -> Integer.toString( obj.getResults().size() ) ) )
//                .addColumn( Column.newColumn( "Průměrná relativní chyba", heResults, (Result obj) -> formatDecimal( obj.getAvgError() ) ) )
//                .addColumn( Column.newColumn( "Maximální relativní chyba", heResults, (Result obj) -> formatDecimal( obj.getMaxError() ) ) )
//                .addColumn( Column.newColumn( "Čas s využitím heuristiky [milisekundy]", heResults, (Result obj) -> formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) ) );
//
//        dataManager.saveChart( Chart.builder()
//                .setTitle( "Řešení heuristikou" )
//                .setXAxis(
//                        Axis.builder()
//                        .setLabel( "počet instancí" )
//                        .build()
//                )
//                .setYAxis(
//                        Axis.builder()
//                        .setLabel( "doba výpočtu [milisekundy]" )
//                        .divideBy( DECIMAL_DIVISOR )
//                        .setFormat( DECIMAL_FORMAT )
//                        .build()
//                )
//                .addPointSet(
//                        PointSet.fromData( "heuristika",
//                                           instanceNumbers, (Integer obj) -> ( obj ),
//                                           heResults, (Result obj) -> (int) Math.round( DECIMAL_DIVISOR * nanosToMillis( obj.getTime() ) ) ) )
//                .build(), "heuristic" );
//
//        dataManager.saveChart( Chart.builder()
//                .setTitle( "Chyba heuristiky" )
//                .setXAxis(
//                        Axis.builder()
//                        .setLabel( "počet instancí" )
//                        .build()
//                )
//                .setYAxis(
//                        Axis.builder()
//                        .setLabel( "Průměrná relativní chyba" )
//                        .divideBy( DECIMAL_DIVISOR )
//                        .setFormat( DECIMAL_FORMAT )
//                        .build()
//                )
//                .addPointSet(
//                        PointSet.fromData( "heuristika",
//                                           instanceNumbers, (Integer obj) -> ( obj ),
//                                           heResults, (Result obj) -> (int) Math.round( DECIMAL_DIVISOR * obj.getAvgError() ) ) )
//                .build(), "heuristicError" );
    }
}
