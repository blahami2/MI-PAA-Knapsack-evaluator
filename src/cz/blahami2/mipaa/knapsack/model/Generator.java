/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model;

import cz.blahami2.mipaa.knapsack.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MBlaha
 */
public class Generator {

    public Generator() {
    }

    public List<Knapsack> generateInput( GeneratorConfiguration config ) {
        List<Knapsack> result = new ArrayList<>();

        for ( int i = config.id; i < config.id + config.instanceCount; i++ ) {
            result.add( generateKnapsack( config ) );
        }

        return result;
    }

    private Knapsack generateKnapsack( GeneratorConfiguration config ) {
        List<KnapsackItem> items = new ArrayList<>();
        boolean[] issued = new boolean[config.maxWeight + 1];
        for ( int i = 0; i < issued.length; i++ ) {
            issued[i] = false;
        }
        int weight;
        int totalWeight = 0;
        int i = 0;
        while ( i < config.itemCount ) {
            weight = RandomUtils.nextInt( 1, config.maxWeight + 1 );
            if ( issued[weight] ) {
                continue;
            }
            if ( validateWeight( config, weight) ) {
                issued[weight] = true;
                items.add( new KnapsackItem( i, weight, RandomUtils.nextInt( 1, config.maxPrice + 1 ) ) );
                totalWeight += weight;
                i++;
            }
        }
        Knapsack knapsack = new Knapsack( config.id, config.itemCount, (int) ( config.weightRatio * totalWeight ) );
        items.stream().forEach( (item) -> {
            knapsack.addItem( item );
        } );
        return knapsack;
    }

    private boolean validateWeight(GeneratorConfiguration config, int weight) {
        double thr;
        switch(config.d){
            case 0:
                return true;
            case -1:
                thr = 1.0 / Math.pow( weight, config.k);
                break;
            case 1:
                thr = 1.0 / Math.pow(config.maxWeight - weight + 1, config.k);
                break;
            default:
                thr = 1;
        }
        return thr >= RandomUtils.nextDouble();
    }
}
