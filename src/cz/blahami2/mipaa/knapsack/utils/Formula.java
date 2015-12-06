/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.utils;

/**
 *
 * @author MBlaha
 */
public class Formula {

    private static final double LOG_BASE_2 = Math.log( 2 );
    
    public static double log(double base, double number){
        return (Math.log( number ) / Math.log( base ));
    }

    public static int fptasBits( double eps, int maxPrice, int itemCount ) {
        double log2 = ( Math.log( eps * (double) maxPrice / (double) itemCount 
        ) / LOG_BASE_2 );
        int bits = (int) ( Math.floor( log2 ) + 0.1 );
        return bits;
    }

    public static double fptasMaxEps( int bits, int maxPrice, int itemCount ) {
        return (double) itemCount * 
               ( 1 << bits ) / (double) maxPrice;
    }
}
