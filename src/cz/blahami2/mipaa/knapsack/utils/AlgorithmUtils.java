package cz.blahami2.mipaa.knapsack.utils;

import cz.blahami2.mipaa.knapsack.InstanceNumbers;
import cz.blahami2.mipaa.knapsack.model.GeneratorConfiguration;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.model.KnapsackResult;
import cz.blahami2.mipaa.knapsack.model.Measurement;
import cz.blahami2.mipaa.knapsack.model.Result;
import cz.blahami2.mipaa.knapsack.model.algorithm.Algorithm;
import cz.blahami2.mipaa.knapsack.model.algorithm.DynamicWeight;
import cz.blahami2.mipaa.knapsack.model.io.DataManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Blaha
 */
public class AlgorithmUtils {

    public static List<Result> runAlgorithm( DataManager dataManager, Algorithm knapsackAlgorithm, InstanceNumbers instanceNumbers ) throws FileNotFoundException {
        return runAlgorithm( dataManager, knapsackAlgorithm, instanceNumbers, null );
    }

    public static List<Result> runAlgorithm( DataManager dataManager, Algorithm knapsackAlgorithm, InstanceNumbers instanceNumbers, Object data ) throws FileNotFoundException {
        List<Result> results = new ArrayList<>();
        for ( int instanceNumber : instanceNumbers.getInstanceNumbers() ) {
            List<Knapsack> knapsacks = dataManager.load( instanceNumber );
            List<KnapsackConfig> expected = dataManager.loadResults( instanceNumber, knapsacks );
            Result result = runInstance( dataManager, knapsackAlgorithm, knapsacks, expected, instanceNumber, data );
            results.add( result );
        }
        return results;
    }
    public static List<Result> runAlgorithm( DataManager dataManager, Algorithm knapsackAlgorithm, List<List<Knapsack>> input, List<List<KnapsackConfig>> expectedConfig, GeneratorConfiguration generatorConfig ) throws IOException {
        return runAlgorithm( dataManager, knapsackAlgorithm, input, expectedConfig, generatorConfig, null );
    }

    public static List<Result> runAlgorithm( DataManager dataManager, Algorithm knapsackAlgorithm, List<List<Knapsack>> input, List<List<KnapsackConfig>> expectedConfig, GeneratorConfiguration generatorConfig, Object data ) throws IOException {
        List<Result> results = new ArrayList<>();
        for ( int i = 0; i < input.size(); i++ ) {
            List<Knapsack> knapsacks = input.get( i );
            List<KnapsackConfig> expected = expectedConfig.get( i );
            Result result = runInstance( dataManager, knapsackAlgorithm, knapsacks, expected, generatorConfig, data );
            results.add( result );
        }
        return results;
    }

    public static Result runInstance( DataManager dataManager, Algorithm knapsackAlgorithm, List<Knapsack> knapsacks, List<KnapsackConfig> expected, GeneratorConfiguration generatorConfig, Object data ) throws IOException {
        List<KnapsackResult> results;
        Measurement measurement = new Measurement();
        measurement.start();
        results = knapsackAlgorithm.run( knapsacks, expected, data );
        measurement.stop();
        dataManager.save( generatorConfig, knapsackAlgorithm.getClass().getSimpleName(), results );
        System.out.println( knapsackAlgorithm.getClass().getSimpleName() + "-instance[" + generatorConfig.itemCount + "]-Success-time[milliseconds]=" + FormatUtils.formatTimeInNanos( measurement.getMeasuredTime() ) );
        return new Result( results );
    }

    public static Result runInstance( DataManager dataManager, Algorithm knapsackAlgorithm, List<Knapsack> knapsacks, List<KnapsackConfig> expected, int instanceNumber, Object data ) throws FileNotFoundException {
        List<KnapsackResult> results;
        Measurement measurement = new Measurement();
        measurement.start();
        results = knapsackAlgorithm.run( knapsacks, expected, data );
        measurement.stop();
        dataManager.save( instanceNumber, knapsackAlgorithm.getClass().getSimpleName(), results );
        System.out.println( knapsackAlgorithm.getClass().getSimpleName() + "-instance[" + instanceNumber + "]-Success-time[milliseconds]=" + FormatUtils.formatTimeInNanos( measurement.getMeasuredTime() ) );
        return new Result( results );
    }

    public static List<KnapsackConfig> getExpectedResult( DataManager dataManager, List<Knapsack> knapsacks ) {
        Algorithm dp = new DynamicWeight( 0, 1 );
        List<KnapsackConfig> fake = new ArrayList<>();
        List<KnapsackConfig> expected = new ArrayList<>();
        knapsacks.stream().forEachOrdered( (knapsack) -> fake.add( new KnapsackConfig( knapsack ).setFromLongInteger( 0 ) ) );
        List<KnapsackResult> run = dp.run( knapsacks, fake, null );
        run.stream().forEachOrdered( (knapsackResult) -> expected.add( knapsackResult.getConfig() ) );
        return expected;
    }

    public static double getTotalError( List<Result> results ) {
        return results.stream().reduce( (double) 0, (x, y) -> x + y.getMaxError(), (x, y) -> x + y );
    }
}
