/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.blahami2.mipaa.knapsack.utils.ResultComparator;
import cz.blahami2.mipaa.knapsack.model.GeneratorConfiguration;
import cz.blahami2.mipaa.knapsack.model.Result;
import cz.blahami2.mipaa.knapsack.model.SimulatedAnnealingConfiguration;

/**
 *
 * @author MBlaha
 */
public class MeasurementIO {

    private static final int NUMBER_DIVIDE = 1000000;
    private static final int NUMBER_FLOAT = 1000;

    public static void save( File outputFile, long bruteForceTime, long heuristicTime, int instancesCount, double avgRelativeError, double maxRelativeError ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
//        System.out.println( "instances count = " + instancesCount );
//        System.out.println( "bruteforce time = " + bruteForceTime );
//        System.out.println( "heuristic time = " + heuristicTime );
//        System.out.println( "average relative error = " + avgRelativeError );
//        System.out.println( "max relative error = " + maxRelativeError );
        writer.println( instancesCount );
        writer.println( bruteForceTime );
        writer.println( heuristicTime );
        writer.println( avgRelativeError );
        writer.println( maxRelativeError );
        writer.close();
    }

    public static void save( File outputFile, long time, int instancesCount ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
        writer.println( instancesCount );
        writer.println( time );
        writer.close();

    }

    public static void save4Progress( File outputFile, List<Double> values ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
        for ( int i = 0; i < values.size(); i++ ) {
            writer.println( String.format( "%d %.03f", i, values.get( i ) ).replace( '.', ',' ) );
        }
        writer.close();
    }

    public static void save4All( File outputFile, List<List<Result>> results, List<String> titles ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
        // todo
        writer.print( "^ Počet předmětů ^ Počáteční teplota ^ Vnitřní cyklus ^" );
        for ( int i = 0; i < titles.size(); i++ ) {
            writer.print( titles.get( i ) + " ^" );
        }
        writer.println();
        for ( int inst = 0; inst < results.get( 0 ).size(); inst++ ) {
            writer.print( "| " + results.get( 0 ).get( inst ).getResults().get( 0 ).getKnapsack().getItemCount() + " |" );
            SimulatedAnnealingConfiguration config = results.get( 1 ).get( inst ).getSaConfig();
            writer.print( " " + config.TEMPERATURE_INITIAL + " |" );
            writer.print( " " + config.INNER_LOOP + " |" );
            for ( int alg = 0; alg < results.size(); alg++ ) {
                long time = results.get( alg ).get( inst ).getTime();
                String number = Long.toString( time / NUMBER_DIVIDE ) + "." + String.format( "%03d", ( ( time % NUMBER_DIVIDE ) / NUMBER_FLOAT ) );
                writer.print( "  " + number + "|" );
            }
            Result result = results.get( 1 ).get( inst );
            double mstime = result.getTime() / (double) NUMBER_DIVIDE;
            writer.print( " " + String.format( "%.03f%%", result.getAvgError() * 100 ) + " | "
                          + String.format( "%.03f%%", result.getMaxError() * 100 ) + " | "
                          + String.format( "%.03f", mstime / ( 100 * ( 1 - result.getAvgError() ) ) ) + " |" );
            writer.println();
        }
        writer.close();

    }

    public static void save4Error( File outputFile, List<List<Result>> results, List<String> titles ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
        // todo
        writer.print( "^ Počet předmětů ^ Teplota ^ Vnitřní cyklus ^" );
        for ( int i = 0; i < titles.size(); i++ ) {
            writer.print( titles.get( i ) + " ^" );
        }
        writer.println();
        for ( int inst = 0; inst < results.get( 0 ).size(); inst++ ) {
            SimulatedAnnealingConfiguration config = results.get( 1 ).get( inst ).getSaConfig();
            writer.print( "| " + config.TEMPERATURE_INITIAL + " |" );
            writer.print( " " + config.INNER_LOOP + " |" );
            Result result = results.get( 1 ).get( inst );
            writer.print( " " + String.format( "%.03f%%", result.getAvgError() * 100 ) + " | "
                          + String.format( "%.03f%%", result.getMaxError() * 100 ) + "|" );
            writer.println();
        }
        writer.close();
    }

    public static void save4( File outputFile, List<List<Result>> results, List<String> titles ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
        // todo
        writer.print( "^ Počet předmětů ^ Teplota ^ Vnitřní cyklus ^" );
        for ( int i = 0; i < titles.size(); i++ ) {
            writer.print( titles.get( i ) + " ^" );
        }
        writer.println();
        for ( int inst = 0; inst < results.get( 0 ).size(); inst++ ) {
            SimulatedAnnealingConfiguration config = results.get( 1 ).get( inst ).getSaConfig();
            writer.print( "| " + config.TEMPERATURE_INITIAL + " |" );
            writer.print( " " + config.INNER_LOOP + " |" );
            for ( int alg = 0; alg < results.size(); alg++ ) {
                long time = results.get( alg ).get( inst ).getTime();
                String number = Long.toString( time / NUMBER_DIVIDE ) + "." + String.format( "%03d", ( ( time % NUMBER_DIVIDE ) / NUMBER_FLOAT ) );
                writer.print( "  " + number + "|" );
            }
            writer.println();
        }
        writer.close();

    }

    public static void save3Error( File outputFile, List<List<Result>> results, List<String> titles ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
        // todo
        writer.print( "^ Počet předmětů ^ Maximální váha ^ Maximální cena ^ Poměr kapacity ^ Granularita [k, d] ^" );
        for ( int i = 0; i < titles.size(); i++ ) {
            writer.print( titles.get( i ) + " ^" );
        }
        writer.println();
        for ( int inst = 0; inst < results.get( 0 ).size(); inst++ ) {
            GeneratorConfiguration config = results.get( 0 ).get( inst ).getConfig();
            writer.print( "| " + config.itemCount + " |" );
            writer.print( " " + config.maxWeight + " |" );
            writer.print( " " + config.maxPrice + " |" );
            writer.print( " " + String.format( "%.02f", config.weightRatio ) + " |" );
            writer.print( " " + config.k + ", " + config.d + " |" );
            Result result = results.get( 1 ).get( inst );
            writer.print( " " + String.format( "%.03f%%", result.getAvgError() * 100 ) + " | "
                          + String.format( "%.03f%%", result.getMaxError() * 100 ) + "|" );
            writer.println();
        }
        writer.close();
    }

    public static void save3( File outputFile, List<List<Result>> results, List<String> titles ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
        // todo
        writer.print( "^ Počet předmětů ^ Maximální váha ^ Maximální cena ^ Poměr kapacity ^ Granularita [k, d] ^" );
        for ( int i = 0; i < titles.size(); i++ ) {
            writer.print( titles.get( i ) + " ^" );
        }
        writer.println();
        for ( int inst = 0; inst < results.get( 0 ).size(); inst++ ) {
            GeneratorConfiguration config = results.get( 0 ).get( inst ).getConfig();
            writer.print( "| " + config.itemCount + " |" );
            writer.print( " " + config.maxWeight + " |" );
            writer.print( " " + config.maxPrice + " |" );
            writer.print( " " + String.format( "%.02f", config.weightRatio ) + " |" );
            writer.print( " " + config.k + ", " + config.d + " |" );
            for ( int alg = 0; alg < results.size(); alg++ ) {
                long time = results.get( alg ).get( inst ).getTime();
                String number = Long.toString( time / NUMBER_DIVIDE ) + "." + String.format( "%03d", ( ( time % NUMBER_DIVIDE ) / NUMBER_FLOAT ) );
                writer.print( "  " + number + "|" );
            }
            writer.println();
        }
        writer.close();

    }

    public static void save( File outputFile, List<List<Result>> results, List<String> titles ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
        // todo
        writer.print( "^ Počet předmětů ^" );
        for ( int i = 0; i < titles.size(); i++ ) {
            writer.print( titles.get( i ) + " ^" );
        }
        writer.println();
        for ( int inst = 0; inst < results.get( 0 ).size(); inst++ ) {
            writer.print( "| " + results.get( 0 ).get( inst ).getResults().get( 0 ).getKnapsack().getItemCount() + " |" );
            for ( int alg = 0; alg < results.size(); alg++ ) {
                long time = results.get( alg ).get( inst ).getTime();
                String number = Long.toString( time / NUMBER_DIVIDE ) + "." + String.format( "%03d", ( ( time % NUMBER_DIVIDE ) / NUMBER_FLOAT ) );
                writer.print( "  " + number + "|" );
            }
            writer.println();
        }
        writer.close();

    }

    public static void saveError( File outputFile, List<List<Result>> results, List<String> titles ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );

        writer.print( "^ Počet předmětů ^" );
        for ( int i = 0; i < titles.size(); i++ ) {
            writer.print( titles.get( i ) + " ^" );
        }
        double log2 = Math.log( 2 );
        writer.println();
        for ( int inst = 0; inst < results.get( 0 ).size(); inst++ ) {
            writer.print( "| " + results.get( 0 ).get( inst ).getResults().get( 0 ).getKnapsack().getItemCount() + " |" );
            for ( int alg = 2; alg < results.size(); alg++ ) {
                Result result = results.get( alg ).get( inst );
                writer.print( " " + String.format( "%.03f%%", result.getAvgError() * 100 ) + " | "
                              + String.format( "%.03f%%", result.getMaxError() * 100 ) + "|" );
            }
            writer.println();
        }
        writer.close();

    }

    public static void save( File outputFile, String text ) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter( outputFile );
        writer.println( text );
        writer.close();

    }
}
