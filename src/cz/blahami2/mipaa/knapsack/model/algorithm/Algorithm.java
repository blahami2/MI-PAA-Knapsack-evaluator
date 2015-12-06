/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model.algorithm;

import java.util.ArrayList;
import java.util.List;
import cz.blahami2.mipaa.knapsack.model.Measurement;
import cz.blahami2.mipaa.knapsack.utils.ResultComparator;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.model.KnapsackResult;

/**
 *
 * @author MBlaha
 * @param <T> data object type
 */
public abstract class Algorithm<T> {
    
    private final int warmupCycles;
    private final int runCycles;

    public Algorithm( int warmupCycles, int runCycles ) {
        this.warmupCycles = warmupCycles;
        this.runCycles = runCycles;
    }

    public List<KnapsackResult> run( List<Knapsack> knapsacks, List<KnapsackConfig> expected, T data ) {
        List<KnapsackResult> results = new ArrayList<>();
        for(int i = 0; i < knapsacks.size(); i++){
            Knapsack knapsack = knapsacks.get( i );
            KnapsackConfig expectedConfig = expected.get( i );
            for(int j = 0; j < warmupCycles; j++){
                solve( knapsack, data );
            }
            KnapsackConfig resultConfig = null;
            Measurement measurement = new Measurement();
            measurement.start();
            for ( int j = 0; j < runCycles; j++ ) {
                resultConfig = solve(knapsack, data);
            }
            long time = measurement.stop();
            results.add( new KnapsackResult( knapsack, resultConfig, time / runCycles, ResultComparator.compare( expectedConfig, resultConfig ) ) );
        }
        return results;
    }

    public abstract KnapsackConfig solve( Knapsack knapsack, T data );

}
