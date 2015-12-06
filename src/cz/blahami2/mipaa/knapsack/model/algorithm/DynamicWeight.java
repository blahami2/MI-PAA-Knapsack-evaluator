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
 * @author Michael
 */
public class DynamicWeight extends Algorithm {

//    private class CellConfig {
//
//        int price;
//        int weight;
//
//        public CellConfig( int price, int weight ) {
//            this.price = price;
//            this.weight = weight;
//        }
//    }
    private KnapsackConfig[][] table;
    private Knapsack knapsack;

    public DynamicWeight( int warmupCycles, int runCycles ) {
        super( warmupCycles, runCycles );
    }

    @Override
    public KnapsackConfig solve( Knapsack knapsack, Object data ) {
        this.table = new KnapsackConfig[knapsack.getMaxWeight() + 1][knapsack.getItemCount()];
        this.knapsack = knapsack;
        KnapsackConfig tableCellValue = getTableCellValue( knapsack.getMaxWeight(), knapsack.getItemCount() - 1 );
//        printTable();
        return tableCellValue;
    }

    private KnapsackConfig getTableCellValue( int remainingWeight, int itemIdx ) {
        if ( remainingWeight < 0 || itemIdx < 0 ) { // bacha, mozna jde o uroven niz
//            System.out.println( "remaining weight = " + remainingWeight );
            KnapsackConfig cfg = new KnapsackConfig( knapsack );
            cfg.setFromLongInteger( 0 );
            return cfg;
        }
        if ( table[remainingWeight][itemIdx] == null ) {
            int remainingWeightWithItem = remainingWeight - knapsack.getItem( itemIdx ).getWeight();
            KnapsackConfig withoutItem = getTableCellValue( remainingWeight, itemIdx - 1 );
            KnapsackConfig withItem = new KnapsackConfig( getTableCellValue( remainingWeightWithItem, itemIdx - 1 ) );
            withItem.set( itemIdx, true );
            if ( withItem.getPrice() < withoutItem.getPrice() || remainingWeightWithItem < 0 ) {
//                System.out.println( "remaining = " + remainingWeight + ", skipping item: (" + knapsack.getItem( itemIdx ).getWeight() + ", " + knapsack.getItem( itemIdx ).getWeight() + ")" );
                table[remainingWeight][itemIdx] = withoutItem;
            } else {
//                System.out.println( "remaining = " + remainingWeight + ", adding item: (" + knapsack.getItem( itemIdx ).getWeight() + ", " + knapsack.getItem( itemIdx ).getWeight() + ")" );
                table[remainingWeight][itemIdx] = withItem;
            }
        }
        return table[remainingWeight][itemIdx];
    }

    private void printTable() {
        for ( int i = knapsack.getMaxWeight(); i >= 0; i-- ) {
            for ( int j = 0; j < knapsack.getItemCount(); j++ ) {
                if ( table[i][j] == null ) {
                    System.out.print( "(null)\t\t" );
                } else {
                    System.out.print( "(" + table[i][j].getPrice() + ", " + table[i][j].getWeight() + ")  \t" );
                }
            }
            System.out.println( "" );
        }
    }

}
