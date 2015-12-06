/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model;

import java.util.List;

/**
 *
 * @author MBlaha
 */
public class Result {
    private final List<KnapsackResult> results;
    private final long time;
    private final double maxError;
    private final double avgError;
    private final GeneratorConfiguration config;
    private final SimulatedAnnealingConfiguration saConfig;

    public Result( List<KnapsackResult> results) {
        this.results = results;
        this.time = KnapsackResult.getTotalTime( results );
        this.maxError = KnapsackResult.getMaxError( results );
        this.avgError = KnapsackResult.getAvgError( results );
        this.config = null;
        this.saConfig = null;
    }

    public Result( List<KnapsackResult> results, long time, GeneratorConfiguration config) {
        this.results = results;
        this.time = time;
        this.maxError = 0;
        this.avgError = 0;
        this.config = config;
        this.saConfig = null;
    }

    public Result( List<KnapsackResult> results, long time, double maxError, double avgError ) {
        this.results = results;
        this.time = time;
        this.maxError = maxError;
        this.avgError = avgError;
        this.config = null;
        this.saConfig = null;
    }

    public Result( List<KnapsackResult> results, long time, double maxError, double avgError, SimulatedAnnealingConfiguration saConfig ) {
        this.results = results;
        this.time = time;
        this.maxError = maxError;
        this.avgError = avgError;
        this.config = null;
        this.saConfig = saConfig;
    }

    public Result( List<KnapsackResult> results, long time, double maxError, double avgError, GeneratorConfiguration config ) {
        this.results = results;
        this.time = time;
        this.maxError = maxError;
        this.avgError = avgError;
        this.config = config;
        this.saConfig = null;
    }

    public List<KnapsackResult> getResults() {
        return results;
    }

    public long getTime() {
        return time;
    }

    public double getMaxError() {
        return maxError;
    }

    public double getAvgError() {
        return avgError;
    }

    public GeneratorConfiguration getConfig() {
        return config;
    }

    public SimulatedAnnealingConfiguration getSaConfig() {
        return saConfig;
    }
    
    
}
