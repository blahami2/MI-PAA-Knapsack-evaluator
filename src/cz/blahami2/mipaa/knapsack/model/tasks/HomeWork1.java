package cz.blahami2.mipaa.knapsack.model.tasks;

import cz.blahami2.mipaa.knapsack.InstanceNumbers;
import cz.blahami2.mipaa.knapsack.model.Result;
import cz.blahami2.mipaa.knapsack.model.algorithm.Algorithm;
import cz.blahami2.mipaa.knapsack.model.algorithm.Bruteforce;
import cz.blahami2.mipaa.knapsack.model.algorithm.HeuristicRatio;
import cz.blahami2.mipaa.knapsack.model.chart.Axis;
import cz.blahami2.mipaa.knapsack.model.chart.Chart;
import cz.blahami2.mipaa.knapsack.model.chart.PointSet;
import cz.blahami2.mipaa.knapsack.model.table.Column;
import cz.blahami2.mipaa.knapsack.model.table.DataTable;
import cz.blahami2.mipaa.knapsack.utils.AlgorithmUtils;
import cz.blahami2.mipaa.knapsack.utils.FormatUtils;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Blaha
 */
public class HomeWork1 extends HomeWorkTask {

    public HomeWork1() throws IOException {
        super( 10, 10 );
    }

    @Override
    public void run() {
        try {
            InstanceNumbers instanceNumbers = InstanceNumbers.MEDIUM;
            Algorithm bf = new Bruteforce( getWarmup(), getApprox() );
            List<Result> bfResults = AlgorithmUtils.runAlgorithm( getDataManager(), bf, instanceNumbers );
            assert AlgorithmUtils.getTotalError( bfResults ) == 0;
            Algorithm he = new HeuristicRatio( getWarmup(), getApprox() );
            List<Result> heResults = AlgorithmUtils.runAlgorithm( getDataManager(), he, instanceNumbers );

            getDataManager().saveTable( new DataTable( "hw1", instanceNumbers.getInstanceNumbers().size() )
                    .addColumn( Column.newColumn( "Počet předmětů", instanceNumbers.getInstanceNumbers().stream().map( obj -> Integer.toString( obj ) ) ) )
                    .addColumn( Column.newColumn( "Počet instancí", heResults.stream().map( obj -> Integer.toString( obj.getResults().size() ) ) ) )
                    .addColumn( Column.newColumn( "Průměrná relativní chyba", heResults, (Result obj) -> FormatUtils.formatDecimal( obj.getAvgError() ) ) )
                    .addColumn( Column.newColumn( "Maximální relativní chyba", heResults, (Result obj) -> FormatUtils.formatDecimal( obj.getMaxError() ) ) )
                    .addColumn( Column.newColumn( "Čas algoritmu hrubé síly [milisekundy]", bfResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
                    .addColumn( Column.newColumn( "Čas s využitím heuristiky [milisekundy]", heResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) ) );

            getDataManager().saveChart( Chart.builder()
                    .setTitle( "Řešení hrubou silou" )
                    .setXAxis(
                            Axis.builder()
                            .setLabel( "počet předmětů" )
                            .build()
                    )
                    .setYAxis(
                            Axis.builder()
                            .setLabel( "doba výpočtu [milisekundy]" )
                            .build()
                    )
                    .addPointSet(
                            PointSet.fromData( "hrubá síla",
                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                               bfResults, (Result obj) -> (int) Math.round( FormatUtils.nanosToMillis( obj.getTime() ) ) ) )
                    .build(), "bruteforce" );

            getDataManager().saveChart( Chart.builder()
                    .setTitle( "Řešení heuristikou" )
                    .setXAxis(
                            Axis.builder()
                            .setLabel( "počet předmětů" )
                            .build()
                    )
                    .setYAxis(
                            Axis.builder()
                            .setLabel( "doba výpočtu [milisekundy]" )
                            .divideBy( DECIMAL_DIVISOR )
                            .setFormat( DECIMAL_FORMAT )
                            .build()
                    )
                    .addPointSet(
                            PointSet.fromData( "heuristika",
                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                               heResults, (Result obj) -> (int) Math.round( DECIMAL_DIVISOR * FormatUtils.nanosToMillis( obj.getTime() ) ) ) )
                    .build(), "heuristic" );

            getDataManager().saveChart( Chart.builder()
                    .setTitle( "Chyba heuristiky" )
                    .setXAxis(
                            Axis.builder()
                            .setLabel( "počet předmětů" )
                            .build()
                    )
                    .setYAxis(
                            Axis.builder()
                            .setLabel( "Průměrná relativní chyba" )
                            .divideBy( DECIMAL_DIVISOR )
                            .setFormat( DECIMAL_FORMAT )
                            .build()
                    )
                    .addPointSet(
                            PointSet.fromData( "heuristika",
                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                               heResults, (Result obj) -> (int) Math.round( DECIMAL_DIVISOR * obj.getAvgError() ) ) )
                    .build(), "heuristicError" );
        } catch ( IOException ex ) {
            Logger.getLogger( HomeWork1.class.getName() ).log( Level.SEVERE, null, ex );
            System.exit( 1 );
        }
    }

}
