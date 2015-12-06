/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author MBlaha
 */
public class KnapsackResult {

    private final Knapsack knapsack;
    private final KnapsackConfig result;
    private final long time;
    private final double error;

    public KnapsackResult( Knapsack knapsack, KnapsackConfig result, long time, double error ) {
        this.knapsack = knapsack;
        this.result = result;
        this.time = time;
        this.error = error;
    }

    @Override
    public String toString() {
        return knapsack.getId() + " " + result.getItemCount() + " " + result.getPrice() + " " + result.toString() + ", time = " + time + ", error = " + error;
    }

    public Knapsack getKnapsack() {
        return knapsack;
    }

    public KnapsackConfig getConfig() {
        return result;
    }

    public long getTime() {
        return time;
    }

    public double getError() {
        return error;
    }

    public static long getTotalTime( List<KnapsackResult> results ) {
        long time = 0;
        time = results.stream().map( (result) -> result.getTime() ).reduce( time, (accumulator, _item) -> accumulator + _item );
        return time;
    }

    public static double getAvgError( List<KnapsackResult> results ) {
        double totalError;
//        for ( KnapsackResult result : results ) {
//            System.out.println( "result = " + result.toString() );
//        }
        totalError = results.stream().parallel().reduce( (double) 0, (x, y) -> x + y.getError(), (x, y) -> x + y );
//        if ( Double.isNaN( totalError / results.size() ) ) {
//            System.out.println( "NaN: " + totalError + " / " + results.size() );
//        }
        return totalError / results.size();
    }

    public static double getMaxError( List<KnapsackResult> results ) {
        double maxError = 0;
        for ( KnapsackResult result : results ) {
            maxError = ( maxError < result.getError() ) ? result.getError() : maxError;
        }
        return maxError;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode( this.knapsack );
        hash = 31 * hash + Objects.hashCode( this.result );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final KnapsackResult other = (KnapsackResult) obj;
        if ( !Objects.equals( this.knapsack, other.knapsack ) ) {
            System.out.println( "knapsacks are not equal" );
            return false;
        }
        if ( !Objects.equals( this.result, other.result ) ) {
            System.out.println( "results are not equal" );
            return false;
        }
        return true;
    }

}
