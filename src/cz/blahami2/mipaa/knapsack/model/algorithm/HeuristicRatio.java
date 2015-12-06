/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model.algorithm;

import cz.blahami2.mipaa.knapsack.model.algorithm.Algorithm;
import java.util.ArrayList;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;

/**
 *
 * @author MBlaha
 */
public class HeuristicRatio extends Algorithm {

    public HeuristicRatio( int warmupCycles, int runCycles ) {
        super( warmupCycles, runCycles );
    }

    @Override
    public KnapsackConfig solve( Knapsack knapsack, Object data ) {
        ArrayList<Item> items = new ArrayList<>( knapsack.getItemCount() );
        for ( int i = 0; i < knapsack.getItemCount(); i++ ) {
            items.add( new Item( i, (double) knapsack.getItem( i ).getPrice() / (double) knapsack.getItem( i ).getWeight() ) );
        }
        items.sort( null );
        KnapsackConfig resultConfig = new KnapsackConfig( knapsack );
        int weight = 0;
        int price = 0;
        for ( int i = items.size() - 1; i >= 0; i-- ) {
            int currentItemIndex = items.get( i ).idx;
            int currentItemWeight = knapsack.getItem( currentItemIndex ).getWeight();
//            System.out.println( "id: " + knapsack.getId() + ": weight = " + weight + ", newItem = w:" + currentItemWeight + ", p:" + currentItemPrice );
            if ( resultConfig.getWeight() + currentItemWeight <= knapsack.getMaxWeight() ) {
                resultConfig.set( currentItemIndex, true );
            }
        }
        return resultConfig;
    }

    private class Item implements Comparable {

        int idx;
        double ratio;

        public Item( int idx, double ratio ) {
            this.idx = idx;
            this.ratio = ratio;
        }

        @Override
        public int compareTo( Object o ) {
            Item item = (Item) o;
            if ( this.ratio > item.ratio ) {
                return 1;
            }
            if ( this.ratio == item.ratio ) {
                return 0;
            }
            return -1;
        }
    }

}
