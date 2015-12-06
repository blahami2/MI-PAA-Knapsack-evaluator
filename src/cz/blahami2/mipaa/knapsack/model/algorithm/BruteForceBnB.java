/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model.algorithm;

import java.util.Stack;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;

/**
 *
 * @author MBlaha
 */
public class BruteForceBnB extends Algorithm {

    private Stack stack;
    private int[] subsum;

    public BruteForceBnB( int warmupCycles, int runCycles ) {
        super( warmupCycles, runCycles );
    }

    @Override
    public KnapsackConfig solve( Knapsack knapsack, Object data ) {
        this.stack = new Stack();
        this.subsum = new int[knapsack.getItemCount()];
        KnapsackConfigWithLastItem config;
        KnapsackConfig result = new KnapsackConfig( knapsack );
        for ( int i = 0; i < knapsack.getItemCount(); i++ ) {
            config = new KnapsackConfigWithLastItem( knapsack, i );
            config.set( i, true );
            stack.push( config );
            int j = knapsack.getItemCount() - 1 - i;
            if ( j == knapsack.getItemCount() - 1 ) {
                subsum[j] = knapsack.getItem( j ).getPrice();
            } else {
                subsum[j] = subsum[j + 1] + knapsack.getItem( j ).getPrice();
            }
//            System.out.println( "adding config: " + config.toString() + " with last item = " + config.getLastItem()
//                                + " and itemCount = " + config.getSelectedItemCount() );
        }
        int updateCounter = 0;
        while ( !stack.empty() ) {
            KnapsackConfigWithLastItem currentConfig = (KnapsackConfigWithLastItem) stack.pop();
//            updateCounter++;
//            if(updateCounter % 1000 == 0){
//                System.out.println( "current config = " + currentConfig.toString() );
//            }
            if ( result.getPrice() < currentConfig.getPrice()
                 || ( result.getPrice() == currentConfig.getPrice() && ( result.getSelectedItemCount() < currentConfig.getSelectedItemCount() ) ) ) {
                result = new KnapsackConfig( currentConfig );
//                System.out.println( "result item count = " + result.getSelectedItemCount() + " result = " + result.toString() );
            }
            for ( int i = currentConfig.getLastItem() + 1; i < knapsack.getItemCount(); i++ ) {
                // cut bottom
                if (  currentConfig.getPrice() + subsum[i] < result.getPrice()  ) {
                    break;
                }
                if ( currentConfig.getWeight() + knapsack.getItem( i ).getWeight() <= knapsack.getMaxWeight() ) { // cut top
                    config = new KnapsackConfigWithLastItem( currentConfig, i );
                    config.set( i, true );
                    stack.push( config );
//                    System.out.println( "adding config: " + config.toString() + " with last item = " + config.getLastItem()
//                                        + " and itemCount = " + config.getSelectedItemCount() );
                }
            }
        }
        return result;
    }

    private class KnapsackConfigWithLastItem extends KnapsackConfig {

        private int lastItem;

        public KnapsackConfigWithLastItem( Knapsack knapsack, int lastItem ) {
            super( knapsack );
            this.lastItem = lastItem;
        }

        public KnapsackConfigWithLastItem( KnapsackConfig knapsackConfig, int lastItem ) {
            super( knapsackConfig );
            this.lastItem = lastItem;
        }

        public int getLastItem() {
            return lastItem;
        }

        public void setLastItem( int lastItem ) {
            this.lastItem = lastItem;
        }

    }
}
