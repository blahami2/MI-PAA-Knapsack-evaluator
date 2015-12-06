/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model.io;

import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.model.KnapsackItem;
import cz.blahami2.mipaa.knapsack.model.KnapsackResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author MBlaha
 */
public class KnapsackIO {

    public static List<Knapsack> load( File input ) throws FileNotFoundException {
        ArrayList<Knapsack> knapsacks = new ArrayList<>();
        Scanner sc = new Scanner( input );
        Knapsack ks;
        int id;
        int itemCount;
        int size;
        while ( sc.hasNext() ) {
            id = sc.nextInt();
            itemCount = sc.nextInt();
            size = sc.nextInt();
            ks = new Knapsack( id, itemCount, size );
            for ( int i = 0; i < itemCount; i++ ) {
                ks.addItem( new KnapsackItem( i, sc.nextInt(), sc.nextInt() ) );
            }
            knapsacks.add( ks );
        }
        sc.close();
        return knapsacks;
    }

    public static void save( File output, List<KnapsackResult> results ) throws FileNotFoundException {
        try ( PrintWriter writer = new PrintWriter( output ) ) {
            results.stream().forEach( (result) -> {
                writer.println( result.toString() );
            } );
        }
    }

    public static List<KnapsackConfig> loadResults( File input, List<Knapsack> knapsacks ) throws FileNotFoundException {
        ArrayList<KnapsackConfig> results = new ArrayList<>();
        Scanner sin = new Scanner( input );
        knapsacks.stream().map( (knapsack) -> {
            int id = sin.nextInt();
            assert id == knapsack.getId() : id + " != " + knapsack.getId();
            return knapsack;
        } ).map( (knapsack) -> {
            int itemCount = sin.nextInt();
            assert itemCount == knapsack.getItemCount() : itemCount + " != " + knapsack.getItemCount();
            int price = sin.nextInt();
            KnapsackConfig cfg = new KnapsackConfig( knapsack );
            for ( int i = 0; i < itemCount; i++ ) {
                cfg.set( i, ( sin.nextInt() == 1 ) );
            }
            assert cfg.getPrice() == price : "not equal: " + cfg.getPrice() + " with " + price + ": " + cfg.toString();
            return cfg;
        } ).forEach( (cfg) -> {
            results.add( cfg );
        } );
        return results;
    }

}
