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

/**
 *
 * @author MBlaha
 */
public class DynamicPrice extends Algorithm {

    private KnapsackConfig[] table;
    private Knapsack knapsack;
//    private int error; // TODO fix
    private Map<Integer, Integer> errors;

    public DynamicPrice(int warmupCycles, int runCycles) {
        super(warmupCycles, runCycles);
    }

    public void setErrors(Map<Integer, Integer> errors) {
        this.errors = errors;
    }

    @Override
    public KnapsackConfig solve(Knapsack knapsack, Object data) {
        int maxPrice = knapsack.getPriceSum();
        this.table = new KnapsackConfig[maxPrice + 1];

        KnapsackConfig cfg = new KnapsackConfig(knapsack);
        cfg.setFromLongInteger(0);
        table[0] = cfg;
        for (int i = 0; i < knapsack.getItemCount(); i++) {
            for (int j = maxPrice; j >= 0; j--) {
                if (table[j] != null) {
//                    System.out.println( j + " != null" );
                    KnapsackItem item = knapsack.getItem(i);

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
            if (table[i] != null && table[i].getWeight() <= knapsack.getMaxWeight()) {
                // quickfix for zero price items, which fit weight
//                System.out.println( "weight = " + table[i].getWeight() + " out of " + knapsack.getCapacity() );
                for (int j = 0; j < knapsack.getItemCount(); j++) {
                    KnapsackItem item = knapsack.getItem(j);
//                    System.out.println( "item[" + j + "]: p = " + item.getPrice() + ", w = " + item.getWeight() );
                    if (!table[i].get(j) && table[i].getWeight() + item.getWeight() <= knapsack.getMaxWeight()) {
//                        System.out.println( "adding above" );
                        table[i].set(j, true);
                    }
                }
                return table[i];
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
