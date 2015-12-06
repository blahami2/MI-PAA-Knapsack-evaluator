package cz.blahami2.mipaa.knapsack.model.tasks;

import cz.blahami2.mipaa.knapsack.InstanceNumbers;
import cz.blahami2.mipaa.knapsack.model.KnapsackResult;
import cz.blahami2.mipaa.knapsack.model.Result;
import cz.blahami2.mipaa.knapsack.model.algorithm.Algorithm;
import cz.blahami2.mipaa.knapsack.model.algorithm.BruteForceBnB;
import cz.blahami2.mipaa.knapsack.model.algorithm.DynamicPrice;
import cz.blahami2.mipaa.knapsack.model.algorithm.DynamicPriceFPTAS;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Blaha
 */
public class HomeWork2 extends HomeWorkTask {

    private static final double START = 0;
    private static final double END = 1;
    private static final double STEP = 0.1;
    private static final int BNB_WARMUP = 2;
    private static final int BNB_APPROX = 10;

    private static final int FPTAS_DECIMAL_DIVISION = 10;
    private static final String FPTAS_DECIMAL_FORMAT = "%.01f";

    public HomeWork2() throws IOException {
        super( 10, 100 );
    }

    @Override
    public void run() {
        try {
            InstanceNumbers bnbNumbers = InstanceNumbers.LARGE;
            InstanceNumbers instanceNumbers = InstanceNumbers.FULL;
            List<Double> errorList = new ArrayList<>();
            for ( double error = START; compare( error, END ) != 1; error += STEP ) {
                errorList.add( error );
            }
//            System.out.println( "errorList = " + errorList );
            Algorithm bnb = new BruteForceBnB( BNB_WARMUP, BNB_APPROX );
            List<Result> bnbResults = AlgorithmUtils.runAlgorithm( getDataManager(), bnb, bnbNumbers );
            Algorithm dp = new DynamicPrice( getWarmup(), getApprox() );
            List<Result> dpResults = AlgorithmUtils.runAlgorithm( getDataManager(), dp, instanceNumbers );
            List<KnapsackResult> bnbFakeResults = new ArrayList<>();
            for ( int i = bnbResults.size(); i < dpResults.size(); i++ ) {
                bnbResults.add( new Result( bnbFakeResults, 0, 0, 0 ) );
            }
            List<Algorithm> fptasList = new ArrayList<>();
            errorList.stream().forEach( (error) -> {
                fptasList.add( new DynamicPriceFPTAS( getWarmup(), getApprox(), error ) );
            } );
            List<List<Result>> resultList = new ArrayList<>();
            for ( Algorithm algorithm : fptasList ) {
                resultList.add( AlgorithmUtils.runAlgorithm( getDataManager(), algorithm, instanceNumbers ) );
            }
            DataTable timeTable = new DataTable( "hw2_time", instanceNumbers.getInstanceNumbers().size() )
                    .addColumn( Column.newColumn( "Počet předmětů", instanceNumbers.getInstanceNumbers().stream().map( obj -> Integer.toString( obj ) ) ) )
                    .addColumn( Column.newColumn( "Počet instancí", dpResults.stream().map( obj -> Integer.toString( obj.getResults().size() ) ) ) )
                    .addColumn( Column.newColumn( "Čas B&B [milisekundy]", bnbResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
                    .addColumn( Column.newColumn( "Čas DP dle ceny [milisekundy]", dpResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) );

            for ( int i = 0; i < errorList.size(); i++ ) {
                timeTable.addColumn( Column.newColumn( "Čas FPTAS " + format( errorList.get( i ) ) + " [milisekundy]", resultList.get( i ), (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) );
            }
            getDataManager().saveTable( timeTable );
            DataTable errorTable = new DataTable( "hw2_error", instanceNumbers.getInstanceNumbers().size() )
                    .addColumn( Column.newColumn( "Počet předmětů", instanceNumbers.getInstanceNumbers().stream().map( obj -> Integer.toString( obj ) ) ) )
                    .addColumn( Column.newColumn( "Počet instancí", dpResults.stream().map( obj -> Integer.toString( obj.getResults().size() ) ) ) );

            for ( int i = 0; i < errorList.size(); i++ ) {
                errorTable.addColumn( Column.newColumn( "Průměrná relativní chyba FPTAS " + format( errorList.get( i ) ) + "", resultList.get( i ), (Result obj) -> FormatUtils.formatDecimal( obj.getAvgError() ) ) );
                errorTable.addColumn( Column.newColumn( "Maximální relativní chyba FPTAS " + format( errorList.get( i ) ) + "", resultList.get( i ), (Result obj) -> FormatUtils.formatDecimal( obj.getMaxError() ) ) );
            }
            getDataManager().saveTable( errorTable );

            getDataManager().saveChart( Chart.builder()
                    .setTitle( "Srovnání FPTAS algoritmu dle chyby" )
                    .setXAxis(
                            Axis.builder()
                            .setLabel( "zvolená chyba" )
                            .divideBy( FPTAS_DECIMAL_DIVISION )
                            .setFormat( FPTAS_DECIMAL_FORMAT )
                            .build()
                    )
                    .setYAxis(
                            Axis.builder()
                            .setLabel( "doba výpočtu [milisekundy]" )
                            .build()
                    )
                    .addPointSet( PointSet.fromData( "čas FPTAS",
                                                     errorList, (Double obj) -> ( round( obj, FPTAS_DECIMAL_DIVISION ) ),
                                                     resultList, (List<Result> obj) -> round( FormatUtils.nanosToMillis( obj.get( instanceNumbers.getInstanceNumbers().size() - 1 ).getTime() ) ) ) )
                    .build(), "hw2_time_FPTAS"
            );

            Chart.Builder allTimeChartBuilder = Chart.builder()
                    .setTitle( "Srovnání všech algoritmů" )
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
                            PointSet.fromData( "B&B",
                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                               bnbResults, (Result obj) -> round( FormatUtils.nanosToMillis( obj.getTime() ) ) ) )
                    .addPointSet(
                            PointSet.fromData( "DP dle ceny",
                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                               dpResults, (Result obj) -> round( FormatUtils.nanosToMillis( obj.getTime() ) ) ) );
            for ( int i = 0; i < errorList.size(); i++ ) {
                allTimeChartBuilder.addPointSet(
                        PointSet.fromData( "FPTAS " + format( errorList.get( i ) ),
                                           instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                           resultList.get( i ), (Result res) -> round( FormatUtils.nanosToMillis( res.getTime() ) ) )
                );
            }
            getDataManager().saveChart( allTimeChartBuilder.build(), "hw2_times_all" );            
            Chart.Builder timeChartBuilder = Chart.builder()
                    .setTitle( "Srovnání algoritmů" )
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
                            PointSet.fromData( "DP dle ceny",
                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                               dpResults, (Result obj) -> round( FormatUtils.nanosToMillis( obj.getTime() ) ) ) );
            for ( int i = 0; i < errorList.size(); i++ ) {
                timeChartBuilder.addPointSet(
                        PointSet.fromData( "FPTAS " + format( errorList.get( i ) ),
                                           instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                           resultList.get( i ), (Result res) -> round( FormatUtils.nanosToMillis( res.getTime() ) ) )
                );
            }
            getDataManager().saveChart( timeChartBuilder.build(), "hw2_times" );

            Chart.Builder avgErrorChartBuilder = Chart.builder()
                    .setTitle( "Průměrná relativní chyba FPTAS" )
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
                    );
            for ( int i = 0; i < errorList.size(); i++ ) {
                avgErrorChartBuilder.addPointSet(
                        PointSet.fromData( "FPTAS " + format( errorList.get( i ) ),
                                           instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                           resultList.get( i ), (Result res) -> round( res.getAvgError(), DECIMAL_DIVISOR ) )
                );
            }
            getDataManager().saveChart( avgErrorChartBuilder.build(), "hw2_errorAvg" );

            Chart.Builder maxErrorChartBuilder = Chart.builder()
                    .setTitle( "Maximální relativní chyba FPTAS" )
                    .setXAxis(
                            Axis.builder()
                            .setLabel( "počet předmětů" )
                            .build()
                    )
                    .setYAxis(
                            Axis.builder()
                            .setLabel( "Maximální relativní chyba" )
                            .divideBy( DECIMAL_DIVISOR )
                            .setFormat( DECIMAL_FORMAT )
                            .build()
                    );
            for ( int i = 0; i < errorList.size(); i++ ) {
                maxErrorChartBuilder.addPointSet(
                        PointSet.fromData( "FPTAS " + format( errorList.get( i ) ),
                                           instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
                                           resultList.get( i ), (Result res) -> round( res.getMaxError(), DECIMAL_DIVISOR ) )
                );
            }
            getDataManager().saveChart( maxErrorChartBuilder.build(), "hw2_errorMax" );

//            Algorithm fptas01 = new DynamicPriceFPTAS( getWarmup(), getApprox(), 0.1 );
//            List<Result> fptas01Results = AlgorithmUtils.runAlgorithm( getDataManager(), fptas01, instanceNumbers, null );
//            Algorithm fptas025 = new DynamicPriceFPTAS( getWarmup(), getApprox(), 0.25 );
//            List<Result> fptas025Results = AlgorithmUtils.runAlgorithm( getDataManager(), fptas025, instanceNumbers, null );
//            Algorithm fptas05 = new DynamicPriceFPTAS( getWarmup(), getApprox(), 0.5 );
//            List<Result> fptas05Results = AlgorithmUtils.runAlgorithm( getDataManager(), fptas05, instanceNumbers, null );
//
//            getDataManager().saveTable( new DataTable( "hw2_time", instanceNumbers.getInstanceNumbers().size() )
//                    .addColumn( Column.newColumn( "Počet předmětů", instanceNumbers.getInstanceNumbers().stream().map( obj -> Integer.toString( obj ) ) ) )
//                    .addColumn( Column.newColumn( "Počet instancí", dpResults.stream().map( obj -> Integer.toString( obj.getResults().size() ) ) ) )
//                    .addColumn( Column.newColumn( "Čas DP dle ceny [milisekundy]", dpResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
//                    .addColumn( Column.newColumn( "Čas FPTAS 0.1 [milisekundy]", fptas01Results, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
//                    .addColumn( Column.newColumn( "Čas FPTAS 0.25 [milisekundy]", fptas025Results, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
//                    .addColumn( Column.newColumn( "Čas FPTAS 0.5 [milisekundy]", fptas05Results, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
//            );
//
//            getDataManager().saveTable( new DataTable( "hw2_error", instanceNumbers.getInstanceNumbers().size() )
//                    .addColumn( Column.newColumn( "Počet předmětů", instanceNumbers.getInstanceNumbers().stream().map( obj -> Integer.toString( obj ) ) ) )
//                    .addColumn( Column.newColumn( "Počet instancí", dpResults.stream().map( obj -> Integer.toString( obj.getResults().size() ) ) ) )
//                    .addColumn( Column.newColumn( "Průměrná relativní chyba FPTAS 0.1", fptas01Results, (Result obj) -> FormatUtils.formatDecimal( obj.getAvgError() ) ) )
//                    .addColumn( Column.newColumn( "Maximální relativní chyba FPTAS 0.1", fptas01Results, (Result obj) -> FormatUtils.formatDecimal( obj.getMaxError() ) ) )
//                    .addColumn( Column.newColumn( "Průměrná relativní chyba FPTAS 0.25", fptas025Results, (Result obj) -> FormatUtils.formatDecimal( obj.getAvgError() ) ) )
//                    .addColumn( Column.newColumn( "Maximální relativní chyba FPTAS 0.25", fptas025Results, (Result obj) -> FormatUtils.formatDecimal( obj.getMaxError() ) ) )
//                    .addColumn( Column.newColumn( "Průměrná relativní chyba FPTAS 0.5", fptas05Results, (Result obj) -> FormatUtils.formatDecimal( obj.getAvgError() ) ) )
//                    .addColumn( Column.newColumn( "Maximální relativní chyba FPTAS 0.5", fptas05Results, (Result obj) -> FormatUtils.formatDecimal( obj.getMaxError() ) ) )
//            );
//            getDataManager().saveChart( Chart.builder()
//                    .setTitle( "Srovnání pseudopolynomiální algoritmů" )
//                    .setXAxis(
//                            Axis.builder()
//                            .setLabel( "počet instancí" )
//                            .build()
//                    )
//                    .setYAxis(
//                            Axis.builder()
//                            .setLabel( "doba výpočtu [milisekundy]" )
//                            .build()
//                    )
//                    .addPointSet(
//                            PointSet.fromData( "Dynamické programování",
//                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
//                                               dpResults, (Result obj) -> (int) Math.round( FormatUtils.nanosToMillis( obj.getTime() ) ) ) )
//                    .addPointSet(
//                            PointSet.fromData( "FPTAS 0.1",
//                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
//                                               fptas01Results, (Result obj) -> (int) Math.round( FormatUtils.nanosToMillis( obj.getTime() ) ) ) )
//                    .addPointSet(
//                            PointSet.fromData( "FPTAS 0.25",
//                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
//                                               fptas025Results, (Result obj) -> (int) Math.round( FormatUtils.nanosToMillis( obj.getTime() ) ) ) )
//                    .addPointSet(
//                            PointSet.fromData( "FPTAS 0.5",
//                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
//                                               fptas05Results, (Result obj) -> (int) Math.round( FormatUtils.nanosToMillis( obj.getTime() ) ) ) )
//                    .build(), "time" );
//            getDataManager().saveChart( Chart.builder()
//                    .setTitle( "Chyba FPTAS" )
//                    .setXAxis(
//                            Axis.builder()
//                            .setLabel( "počet instancí" )
//                            .build()
//                    )
//                    .setYAxis(
//                            Axis.builder()
//                            .setLabel( "Průměrná relativní chyba" )
//                            .divideBy( DECIMAL_DIVISOR )
//                            .setFormat( DECIMAL_FORMAT )
//                            .build()
//                    )
//                    .addPointSet(
//                            PointSet.fromData( "FPTAS 0.1",
//                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
//                                               fptas01Results, (Result obj) -> (int) Math.round( DECIMAL_DIVISOR * obj.getAvgError() ) ) )
//                    .addPointSet(
//                            PointSet.fromData( "FPTAS 0.25",
//                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
//                                               fptas025Results, (Result obj) -> (int) Math.round( DECIMAL_DIVISOR * obj.getAvgError() ) ) )
//                    .addPointSet(
//                            PointSet.fromData( "FPTAS 0.5",
//                                               instanceNumbers.getInstanceNumbers(), (Integer obj) -> ( obj ),
//                                               fptas05Results, (Result obj) -> (int) Math.round( DECIMAL_DIVISOR * obj.getAvgError() ) ) )
//                    .build(), "error" );
        } catch ( IOException ex ) {
            Logger.getLogger( HomeWork2.class.getName() ).log( Level.SEVERE, null, ex );
            System.exit( 1 );
        }
    }

    private static String format( double number ) {
        return String.format( FPTAS_DECIMAL_FORMAT, number );
    }

}
