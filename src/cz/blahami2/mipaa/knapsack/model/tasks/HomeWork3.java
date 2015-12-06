package cz.blahami2.mipaa.knapsack.model.tasks;

import cz.blahami2.mipaa.knapsack.model.Generator;
import cz.blahami2.mipaa.knapsack.model.GeneratorConfiguration;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.model.KnapsackResult;
import cz.blahami2.mipaa.knapsack.model.Result;
import cz.blahami2.mipaa.knapsack.model.algorithm.Algorithm;
import cz.blahami2.mipaa.knapsack.model.algorithm.BruteForceBnB;
import cz.blahami2.mipaa.knapsack.model.algorithm.Bruteforce;
import cz.blahami2.mipaa.knapsack.model.algorithm.DynamicPrice;
import cz.blahami2.mipaa.knapsack.model.algorithm.DynamicWeight;
import cz.blahami2.mipaa.knapsack.model.algorithm.HeuristicRatio;
import cz.blahami2.mipaa.knapsack.model.chart.Axis;
import cz.blahami2.mipaa.knapsack.model.chart.Chart;
import cz.blahami2.mipaa.knapsack.model.chart.ExtractDataStrategy;
import cz.blahami2.mipaa.knapsack.model.chart.PointSet;
import cz.blahami2.mipaa.knapsack.model.table.Column;
import cz.blahami2.mipaa.knapsack.model.table.DataTable;
import cz.blahami2.mipaa.knapsack.utils.AlgorithmUtils;
import cz.blahami2.mipaa.knapsack.utils.FormatUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Blaha
 */
public class HomeWork3 extends HomeWorkTask {

    private final Generator generator = new Generator();

    private static final GeneratorConfiguration DEFAULT_CONFIG = GeneratorConfiguration.builder()
            .setItemCount( 50 )
            .setInstanceCount( 50 )
            .setWeightRatio( 0.25 )
            .setMaxWeight( 128 )
            .setMaxPrice( 256 )
            .setGranularityExponentK( 0 )
            .setGranularityParameterD( 0 )
            .build();

    public HomeWork3() throws IOException {
        super( 10, 100 );
    }

    @Override
    public void run() {
        /* DESCRIPTION
        methods:
        - BruteForce
        - B&B
        - DP (both)
        - Heuristics
        Computation time dependency:
        - max weight
        - max price
        - backpack_capacity / total_weight ratio
        - granularity
        Procedure:
        - choose one parameter to test (change)
        - fix all the other parameters
        Exceptions:
        - test BruteForce up to 20
        - - compare everything up to 20 items
        - test B&B up to 25
        - - compare everything (but BF) up to 25 items
        - test everything else up to 50
        - - compare everything (but BF and B&B) up to 50 items
        - shitload of tests
        - - watch out for duplicit
        - - no need to test 0 - 30 for everything else (extract and combine data properly)
         */

        try {
            AlgorithmList algorithms = new AlgorithmList( getWarmup(), getApprox() );

            List<Integer> itemCountList = new ArrayList<>();
            List<Integer> maxWeightList = new ArrayList<>();
            List<Integer> maxPriceList = new ArrayList<>();
            List<Double> ratioList = new ArrayList<>();
            List<Granurality> granularityList = new ArrayList<>();
            List<Result> bfResults = new ArrayList<>();
            List<Result> bnbResults = new ArrayList<>();
            List<Result> dpwResults = new ArrayList<>();
            List<Result> dppResults = new ArrayList<>();
            List<Result> heResults = new ArrayList<>();

            List<List<Result>> results = new ArrayList<>();

            runTest( algorithms.getAll(), 20, itemCountList, maxWeightList, maxPriceList, ratioList, granularityList, bfResults, bnbResults, dpwResults, dppResults, heResults );
            runTest( algorithms.getAllButBf(), 25, itemCountList, maxWeightList, maxPriceList, ratioList, granularityList, bfResults, bnbResults, dpwResults, dppResults, heResults );
            runTest( algorithms.getAllButBnb(), 50, itemCountList, maxWeightList, maxPriceList, ratioList, granularityList, bfResults, bnbResults, dpwResults, dppResults, heResults );

        } catch ( IOException ex ) {
            Logger.getLogger( HomeWork3.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    private void runTest(
            List<Algorithm> algorithms,
            int itemCount,
            List<Integer> itemCountList,
            List<Integer> maxWeightList,
            List<Integer> maxPriceList,
            List<Double> ratioList,
            List<Granurality> granularityList,
            List<Result> bfResults,
            List<Result> bnbResults,
            List<Result> dpwResults,
            List<Result> dppResults,
            List<Result> heResults ) throws IOException {

        List<KnapsackResult> fakeResults = new ArrayList<>();
        Result fakeResult = new Result( fakeResults, 0, 0, 0 );
        List<List<Result>> results = new ArrayList<>();

        List<Integer> tmpWeightList = new ArrayList<>();
        for ( int i = 32; i <= 1024; i *= 2 ) {
            List<Result> runConfigResults = runConfig( GeneratorConfiguration.builderFrom( DEFAULT_CONFIG ).setMaxWeight( i ).setItemCount( itemCount ).build(), algorithms );
            results.add( runConfigResults );
            tmpWeightList.add( i );
            itemCountList.add( itemCount );
            maxWeightList.add( i );
            maxPriceList.add( DEFAULT_CONFIG.maxPrice );
            ratioList.add( DEFAULT_CONFIG.weightRatio );
            granularityList.add( new Granurality( DEFAULT_CONFIG.k, DEFAULT_CONFIG.d ) );
            dpwResults.add( runConfigResults.get( AlgorithmList.DPW ) );
            dppResults.add( runConfigResults.get( AlgorithmList.DPP ) );
            heResults.add( runConfigResults.get( AlgorithmList.HE ) );
            if ( algorithms.size() > AlgorithmList.BF ) {
                bfResults.add( runConfigResults.get( AlgorithmList.BF ) );
            } else {
                bfResults.add( fakeResult );
            }
            if ( algorithms.size() > AlgorithmList.BNB ) {
                bnbResults.add( runConfigResults.get( AlgorithmList.BNB ) );
            } else {
                bnbResults.add( fakeResult );
            }
        }
        exportGraph( algorithms, "Citlivost algoritmů na maximální hmotnost", "maximální hmotnost", tmpWeightList, (Integer obj) -> ( obj ), results, "hw3_" + itemCount + "_time_maxWeight" );

        // maxPrice
        results = new ArrayList<>();
        List<Integer> tmpPriceList = new ArrayList<>();
        for ( int i = 32; i <= 1024; i *= 2 ) {
            List<Result> runConfigResults = runConfig( GeneratorConfiguration.builderFrom( DEFAULT_CONFIG ).setMaxPrice( i ).setItemCount( itemCount ).build(), algorithms );
            results.add( runConfigResults );
            tmpPriceList.add( i );
            itemCountList.add( itemCount );
            maxWeightList.add( DEFAULT_CONFIG.maxWeight );
            maxPriceList.add( i );
            ratioList.add( DEFAULT_CONFIG.weightRatio );
            granularityList.add( new Granurality( DEFAULT_CONFIG.k, DEFAULT_CONFIG.d ) );
            dpwResults.add( runConfigResults.get( AlgorithmList.DPW ) );
            dppResults.add( runConfigResults.get( AlgorithmList.DPP ) );
            heResults.add( runConfigResults.get( AlgorithmList.HE ) );
            if ( algorithms.size() > AlgorithmList.BF ) {
                bfResults.add( runConfigResults.get( AlgorithmList.BF ) );
            } else {
                bfResults.add( fakeResult );
            }
            if ( algorithms.size() > AlgorithmList.BNB ) {
                bnbResults.add( runConfigResults.get( AlgorithmList.BNB ) );
            } else {
                bnbResults.add( fakeResult );
            }
        }
        exportGraph( algorithms, "Citlivost algoritmů na maximální cenu", "maximální cena", tmpPriceList, (Integer obj) -> ( obj ), results, "hw3_" + itemCount + "_time_maxPrice" );

        // ratio
        results = new ArrayList<>();
        List<Double> tmpRatioList = new ArrayList<>();
        for ( double i = 0.1; i <= 1; i += 0.1 ) {
            List<Result> runConfigResults = runConfig( GeneratorConfiguration.builderFrom( DEFAULT_CONFIG ).setWeightRatio( i ).setItemCount( itemCount ).build(), algorithms );
            results.add( runConfigResults );
            tmpRatioList.add( i );
            itemCountList.add( itemCount );
            maxWeightList.add( DEFAULT_CONFIG.maxWeight );
            maxPriceList.add( DEFAULT_CONFIG.maxPrice );
            ratioList.add( i );
            granularityList.add( new Granurality( DEFAULT_CONFIG.k, DEFAULT_CONFIG.d ) );
            dpwResults.add( runConfigResults.get( AlgorithmList.DPW ) );
            dppResults.add( runConfigResults.get( AlgorithmList.DPP ) );
            heResults.add( runConfigResults.get( AlgorithmList.HE ) );
            if ( algorithms.size() > AlgorithmList.BF ) {
                bfResults.add( runConfigResults.get( AlgorithmList.BF ) );
            } else {
                bfResults.add( fakeResult );
            }
            if ( algorithms.size() > AlgorithmList.BNB ) {
                bnbResults.add( runConfigResults.get( AlgorithmList.BNB ) );
            } else {
                bnbResults.add( fakeResult );
            }
        }
        getDataManager().saveChart(
                addPointSets(
                        algorithms,
                        Chart.builder()
                        .setTitle( "Citlivost algoritmů na poměr kapacity" )
                        .setXAxis(
                                Axis.builder()
                                .setLabel( "poměr kapacity a celkové hmotnosti" )
                                .divideBy( 10 )
                                .setFormat( "%.01f" )
                                .build()
                        )
                        .setYAxis(
                                Axis.builder()
                                .setLabel( "doba výpočtu [milisekundy]" )
                                .build()
                        ),
                        tmpRatioList,
                        (Double obj) -> ( round( obj, 10 ) ),
                        results )
                .build(), "hw3_" + itemCount + "_time_ratio"
        );

        // granurality
        results = new ArrayList<>();
        List<Granurality> tmpGranuralityList = new ArrayList<>();
        for ( int d = -1; d <= 1; d += 2 ) {
            for ( double k = 0.5; k <= 2; k += 0.5 ) {

                List<Result> runConfigResults = runConfig( GeneratorConfiguration.builderFrom( DEFAULT_CONFIG ).setGranularityParameterD( d ).setGranularityExponentK( k ).setItemCount( itemCount ).build(), algorithms );
                results.add( runConfigResults );
                tmpGranuralityList.add( new Granurality( k, d ) );
                itemCountList.add( itemCount );
                maxWeightList.add( DEFAULT_CONFIG.maxWeight );
                maxPriceList.add( DEFAULT_CONFIG.maxPrice );
                ratioList.add( DEFAULT_CONFIG.weightRatio );
                granularityList.add( new Granurality( k, d ) );
                dpwResults.add( runConfigResults.get( AlgorithmList.DPW ) );
                dppResults.add( runConfigResults.get( AlgorithmList.DPP ) );
                heResults.add( runConfigResults.get( AlgorithmList.HE ) );
                if ( algorithms.size() > AlgorithmList.BF ) {
                    bfResults.add( runConfigResults.get( AlgorithmList.BF ) );
                } else {
                    bfResults.add( fakeResult );
                }
                if ( algorithms.size() > AlgorithmList.BNB ) {
                    bnbResults.add( runConfigResults.get( AlgorithmList.BNB ) );
                } else {
                    bnbResults.add( fakeResult );
                }
            }
        }
        getDataManager().saveChart(
                addPointSets(
                        algorithms,
                        Chart.builder()
                        .setTitle( "Citlivost algoritmů na granuralitu" )
                        .setXAxis(
                                Axis.builder()
                                .setLabel( "granuralita [k, d]" )
                                .setValueToStringStrategy( x -> {
                                    double val = x / 10.0;
                                    int d = ( val > 2 ) ? 1 : -1;
                                    double k = d * ( val - 2 );
                                    if ( val - 2 < 0.001 && val - 2 > -0.001 ) {
                                        k = d = 0;
                                    }
                                    return String.format( "%.01f, %d", k, d );
                                }
                                )
                                .build() )
                        .setYAxis(
                                Axis.builder()
                                .setLabel( "doba výpočtu [milisekundy]" )
                                .build()
                        ),
                        tmpGranuralityList,
                        (Granurality obj) -> {
                            return (int) Math.round( ( ( obj.k * obj.d ) + 2 ) * 10 );
                        },
                        results )
                .build(), "hw3_" + itemCount + "_time_granularity"
        );

        DataTable timeTable = new DataTable( "hw3_time", itemCountList.size() )
                .addColumn( Column.newColumn( "Počet předmětů", itemCountList, (Integer obj) -> obj.toString() ) )
                .addColumn( Column.newColumn( "Maximální hmotnost", maxWeightList, (Integer obj) -> obj.toString() ) )
                .addColumn( Column.newColumn( "Maximální cena", maxPriceList, (Integer obj) -> obj.toString() ) )
                .addColumn( Column.newColumn( "Poměr kapacity", ratioList, (Double obj) -> String.format( "%.02f", obj ) ) )
                .addColumn( Column.newColumn( "Granularita [k,d]", granularityList, (Granurality obj) -> String.format( "%.01f, %d", obj.k, obj.d ) ) )
                .addColumn( Column.newColumn( "Čas hrubé síly [milisekundy]", bfResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
                .addColumn( Column.newColumn( "Čas B&B [milisekundy]", bnbResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
                .addColumn( Column.newColumn( "Čas DP dle kapacity [milisekundy]", dpwResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
                .addColumn( Column.newColumn( "Čas DP dle ceny [milisekundy]", dppResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) )
                .addColumn( Column.newColumn( "Čas heuristiky [milisekundy]", heResults, (Result obj) -> FormatUtils.formatTimeInNanos( obj.getTime() ) ).setAlignment( Column.Alignment.RIGHT ) );
        getDataManager().saveTable( timeTable );

        DataTable errorTable = new DataTable( "hw3_error", itemCountList.size() )
                .addColumn( Column.newColumn( "Počet předmětů", itemCountList, (Integer obj) -> obj.toString() ) )
                .addColumn( Column.newColumn( "Maximální hmotnost", maxWeightList, (Integer obj) -> obj.toString() ) )
                .addColumn( Column.newColumn( "Maximální cena", maxPriceList, (Integer obj) -> obj.toString() ) )
                .addColumn( Column.newColumn( "Poměr kapacity", ratioList, (Double obj) -> String.format( "%.02f", obj ) ) )
                .addColumn( Column.newColumn( "Granularita [k,d]", granularityList, (Granurality obj) -> String.format( "%.01f, %d", obj.k, obj.d ) ) )
                .addColumn( Column.newColumn( "Průměrná relativní chyba heuristiky", heResults, (Result obj) -> FormatUtils.formatDecimal( obj.getAvgError() ) ) )
                .addColumn( Column.newColumn( "Maximální relativní chyba heuristiky", heResults, (Result obj) -> FormatUtils.formatDecimal( obj.getAvgError() ) ) );
        getDataManager().saveTable( errorTable );

    }

    private <X> void exportGraph( List<Algorithm> algorithms, String title, String xLabel, List<X> xValues, ExtractDataStrategy<X> xStrategy, List<List<Result>> results, String fileName ) throws IOException {
//        System.out.println( "xValues = " + xValues.size() + ", results = " + results.size() );

        getDataManager().saveChart(
                addPointSets(
                        algorithms,
                        Chart.builder()
                        .setTitle( title )
                        .setXAxis(
                                Axis.builder()
                                .setLabel( xLabel )
                                .build()
                        )
                        .setYAxis(
                                Axis.builder()
                                .setLabel( "doba výpočtu [milisekundy]" )
                                .build()
                        ),
                        xValues,
                        xStrategy,
                        results )
                .build(), fileName
        );
    }

    private <X> Chart.Builder addPointSets( List<Algorithm> algorithms, Chart.Builder builder, List<X> xValues, ExtractDataStrategy<X> xStrategy, List<List<Result>> results ) {
        if ( algorithms.size() > AlgorithmList.BF ) {
            builder.addPointSet( PointSet.fromData( "čas hrubé síly",
                                                    xValues, xStrategy,
                                                    results, (List<Result> obj) -> round( FormatUtils.nanosToMillis( obj.get( AlgorithmList.BF ).getTime() ) ) ) );
        }
        if ( algorithms.size() > AlgorithmList.BNB ) {
            builder.addPointSet( PointSet.fromData( "čas B&B",
                                                    xValues, xStrategy,
                                                    results, (List<Result> obj) -> round( FormatUtils.nanosToMillis( obj.get( AlgorithmList.BNB ).getTime() ) ) ) );
        }
        if ( algorithms.size() > AlgorithmList.DPW ) {
            builder.addPointSet( PointSet.fromData( "čas DP dle kapacity",
                                                    xValues, xStrategy,
                                                    results, (List<Result> obj) -> round( FormatUtils.nanosToMillis( obj.get( AlgorithmList.DPW ).getTime() ) ) ) );
        }
        if ( algorithms.size() > AlgorithmList.DPP ) {
            builder.addPointSet( PointSet.fromData( "čas DP dle ceny",
                                                    xValues, xStrategy,
                                                    results, (List<Result> obj) -> round( FormatUtils.nanosToMillis( obj.get( AlgorithmList.DPP ).getTime() ) ) ) );
        }
        if ( algorithms.size() > AlgorithmList.HE ) {
            builder.addPointSet( PointSet.fromData( "čas heuristiky",
                                                    xValues, xStrategy,
                                                    results, (List<Result> obj) -> round( FormatUtils.nanosToMillis( obj.get( AlgorithmList.HE ).getTime() ) ) ) );
        }
        return builder;
    }

    private List<Result> runConfig( GeneratorConfiguration config, List<Algorithm> algorithms ) throws IOException {
        List<Result> results = new ArrayList<>();
        List<Knapsack> generatedInput = generator.generateInput( config );
        List<KnapsackConfig> expectedResult = AlgorithmUtils.getExpectedResult( getDataManager(), generatedInput );
        for ( Algorithm algorithm : algorithms ) {
            results.add( AlgorithmUtils.runInstance( getDataManager(), algorithm, generatedInput, expectedResult, config, null ) );
        }
        return results;
    }

    private static class AlgorithmList {

        public static final int HE = 0;
        public static final int DPP = 1;
        public static final int DPW = 2;
        public static final int BNB = 3;
        public static final int BF = 4;

        private final List<Algorithm> algorithms;

        public AlgorithmList( int warmup, int approx ) {
            algorithms = Arrays.asList( new HeuristicRatio( warmup, approx ), new DynamicPrice( warmup, approx ), new DynamicWeight( warmup, approx ), new BruteForceBnB( warmup, approx ), new Bruteforce( warmup, approx ) );
        }

        public List<Algorithm> getAll() {
            return algorithms;
        }

        public List<Algorithm> getAllButBf() {
            return algorithms.subList( 0, algorithms.size() - 1 );
        }

        public List<Algorithm> getAllButBnb() {
            return algorithms.subList( 0, algorithms.size() - 2 );
        }
    }

    private static class Granurality {

        public final double k;
        public final int d;

        public Granurality( double k, int d ) {
            this.k = k;
            this.d = d;
        }
    }
}
