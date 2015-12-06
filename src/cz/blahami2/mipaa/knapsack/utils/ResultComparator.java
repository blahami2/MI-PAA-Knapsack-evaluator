/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.utils;

import cz.blahami2.mipaa.knapsack.utils.Formula;
import java.util.List;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;

/**
 *
 * @author MBlaha
 */
public class ResultComparator {

    public static class SComparison {

        public double maxError;
        public double avgError;

        public SComparison( double maxError, double avgError ) {
            this.maxError = maxError;
            this.avgError = avgError;
        }
    }

    public static double compare( KnapsackConfig optimal, KnapsackConfig approx ) {
        int optimalPrice = optimal.getPrice();
        int approxPrice = approx.getPrice();
        if(optimalPrice == 0){
            return 0;
        }
        return ( (double) optimalPrice - (double) approxPrice ) / (double) optimalPrice;
    }

    public static SComparison compare( List<KnapsackConfig> optimal, List<KnapsackConfig> approx ) throws Exception {
        if ( optimal.size() != approx.size() ) {
            throw new Exception( "uncomparable result sets" );
        }
        double comparisonResult = 0;
        double maxError = 0;
        for ( int i = 0; i < optimal.size(); i++ ) {
            double error = compare( optimal.get( i), approx.get( i));
//            System.out.println( "i = " + i + ", error = " + error );
            maxError = ( maxError > error ) ? maxError : error;
            comparisonResult += error;
        }
//        System.out.println( comparationResult + " / " + resultsA.size() );
        return new SComparison( maxError, comparisonResult / (double) optimal.size() );
    }

    public static SComparison compare( List<KnapsackConfig> optimal, List<KnapsackConfig> approx, double eps ) throws Exception {
        if ( optimal.size() != approx.size() ) {
            throw new Exception( "uncomparable result sets" );
        }
        double theoretic = 0;
        double theoreticMax = 0;
        double maxError = 0;
        int bits = 0;
        int maxPrice = 0;
        for ( int i = 0; i < optimal.size(); i++ ) {
            double error = compare( optimal.get( i), approx.get( i));
//            System.out.println( "i = " + i + ", error = " + error );
            maxError = ( maxError > error ) ? maxError : error;

            maxPrice = optimal.get( i ).getKnapsack().getMaxPrice();

            bits = Formula.fptasBits( eps, maxPrice, optimal.get( i ).getItemCount() );
            theoretic = Formula.fptasMaxEps( bits, maxPrice, optimal.get( i ).getItemCount() );
            theoreticMax = ( theoretic > theoreticMax ) ? theoretic : theoreticMax;
        }
//        System.out.println( comparationResult + " / " + resultsA.size() );
        return new SComparison( maxError, theoreticMax );
    }
}
