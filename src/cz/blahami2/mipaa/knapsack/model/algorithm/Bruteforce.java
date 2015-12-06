/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model.algorithm;

import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;

/**
 *
 * @author MBlaha
 */
public class Bruteforce extends Algorithm {

    public Bruteforce( int warmupCycles, int runCycles ) {
        super( warmupCycles, runCycles );
    }

    @Override
    public KnapsackConfig solve( Knapsack knapsack, Object data ) {
        KnapsackConfig config = new KnapsackConfig( knapsack );
        KnapsackConfig result = new KnapsackConfig( knapsack );
        result.setFromLongInteger( 0 );
        for ( long i = 1; i <= ( 1 << knapsack.getItemCount() ); i++ ) {
            config.setFromLongInteger( i );
            if ( config.getWeight() <= knapsack.getMaxWeight() ) { // CONSTRAINT - weight does not exceed the knapsack capacity
                if ( result.getPrice() < config.getPrice() || // OPTIMIZATION CRITERIA - price is smaller
                        ( result.getPrice() == config.getPrice() && ( result.getSelectedItemCount() < config.getSelectedItemCount() ) ) ) { // OR price is the same and item count is larger
                    result = new KnapsackConfig( config );
                }
            }
        }
        return result;
    }
}
