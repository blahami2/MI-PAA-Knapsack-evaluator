/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model.algorithm;

import java.util.Map;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackItem;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.utils.Formula;

/**
 *
 * @author MBlaha
 */
public class DynamicPriceFPTAS extends Algorithm {

    private KnapsackConfig[] table;
    private Knapsack knapsack;
    private final double errorKoef;
//    private int error; // TODO fix
    private Map<Integer, Integer> errors;

    public DynamicPriceFPTAS(int warmupCycles, int runCycles, double errorKoef) {
        super(warmupCycles, runCycles);
        this.errorKoef = errorKoef;
    }

    public void setErrors(Map<Integer, Integer> errors) {
        this.errors = errors;
    }

    @Override
    public KnapsackConfig solve(Knapsack knapsack, Object data) {
        
//        System.out.println( "formula = log2 ( (" + errorKoef + " * " + knapsack.getMaxPrice() + ") / " + knapsack.getItemCount() + ")" );
        int error = Formula.fptasBits(errorKoef, knapsack.getMaxPrice(), knapsack.getItemCount());
        int maxPrice = knapsack.getPriceSum() >> error;
//        ERROR = 0;
//        ERROR = errors.get( knapsack.getId() );
//        ERROR = Formula.fptasBits( errorKoef, knapsack.getMaxPrice(), knapsack.getItemCount());
//        System.out.println( "ERROR = " + error );

//        System.out.println( "Max price before = " + maxPrice );
//        System.out.println( "Max price after = " + maxPrice );
        this.table = new KnapsackConfig[maxPrice + 1];
        Knapsack alteredKnapsack = new Knapsack(knapsack.getId(), knapsack.getItemCount(), knapsack.getMaxWeight());
        for (int i = 0; i < knapsack.getItemCount(); i++) {
            KnapsackItem item = knapsack.getItem(i);
            alteredKnapsack.addItem(new KnapsackItem(item.getId(), item.getWeight(), item.getPrice() >> error));
        }

        KnapsackConfig cfg = new KnapsackConfig(alteredKnapsack);
        cfg.setFromLongInteger(0);
        table[0] = cfg;
        for (int i = 0; i < alteredKnapsack.getItemCount(); i++) {
            for (int j = maxPrice; j >= 0; j--) {
                if (table[j] != null) {
//                    System.out.println( j + " != null" );
                    KnapsackItem item = alteredKnapsack.getItem(i);

//                    if(item.getPrice() == 0){
//                        System.out.println( "item[" + i + "] = 0" );
//                    }
                    int price = table[j].getPrice() + item.getPrice();
                    cfg = new KnapsackConfig(table[j]);
                    cfg.set(i, true);
                    if ((table[price] == null)
                            || (table[price].getWeight() > cfg.getWeight())
                            || (table[price].getWeight() == cfg.getWeight() && table[price].getSelectedItemCount() < cfg.getSelectedItemCount())
                            ) {
                        table[price] = cfg;
                    }
                }
            }
        }

//        printTable();
        for (int i = maxPrice; i >= 0; i--) {
            if (table[i] != null && table[i].getWeight() <= alteredKnapsack.getMaxWeight()) {
                // quickfix for zero price items, which fit weight
//                System.out.println( "weight = " + table[i].getWeight() + " out of " + knapsack.getCapacity() );
                for (int j = 0; j < alteredKnapsack.getItemCount(); j++) {
                    KnapsackItem item = alteredKnapsack.getItem(j);
//                    System.out.println( "item[" + j + "]: p = " + item.getPrice() + ", w = " + item.getWeight() );
                    if (!table[i].get(j) && table[i].getWeight() + item.getWeight() <= alteredKnapsack.getMaxWeight()) {
//                        System.out.println( "adding above" );
                        table[i].set(j, true);
                    }
                }
                return new KnapsackConfig(knapsack).setConfigFrom(table[i]);
            }
        }
        throw new AssertionError("this should never get here");
    }

    private void printTable() {
        int maxPrice = 0;
        for (int i = 0; i < knapsack.getItemCount(); i++) {
            maxPrice += knapsack.getItem(i).getPrice();
        }
        for (int i = 0; i <= maxPrice; i++) {
            if (table[i] == null) {
                System.out.println("(null)");
            } else {
                System.out.println("(" + table[i].getPrice() + ", " + table[i].getWeight() + ")");
            }
        }
    }
}
