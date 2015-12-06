/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author MBlaha
 */
public class KnapsackConfig {

    private final Knapsack knapsack;
    private final ArrayList<Boolean> config;
    private int selectedItemCount;
    private int weight;
    private int price;

    public KnapsackConfig(Knapsack knapsack) {
        this.knapsack = knapsack;
        this.selectedItemCount = 0;
        this.weight = 0;
        this.price = 0;
        config = new ArrayList<>(knapsack.getItemCount());
        for (int i = 0; i < knapsack.getItemCount(); i++) {
            config.add(false);
        }
    }

    public KnapsackConfig(KnapsackConfig knapsackConfig) {
        this.knapsack = knapsackConfig.knapsack;
        this.selectedItemCount = knapsackConfig.selectedItemCount;
        this.weight = knapsackConfig.weight;
        this.price = knapsackConfig.price;
        this.config = new ArrayList<>(knapsack.getItemCount());
        for (int i = 0; i < knapsack.getItemCount(); i++) {
            config.add(knapsackConfig.get(i));
        }
    }

    public KnapsackConfig setFromLongInteger(long number) {
        for (int i = 0; i < knapsack.getItemCount(); i++) {
//            System.out.println( "i = " + i + ", 1 << i = " + (1 << i) + ", number & (1 << i) = " + (number & (1 << i)) );
            if ((number & (1 << i)) == (1 << i)) {
                set(i, true);
            } else {
                set(i, false);
            }
        }
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    public int getSelectedItemCount() {
        return selectedItemCount;
    }

//    public void set( int idx ) {
//        if ( !config.get( idx ) ) {
//            config.set( idx, true );
//            price += knapsack.getItem( idx ).getPrice();
//            weight += knapsack.getItem( idx ).getWeight();
//            selectedItemCount++;
//        }
//    }
    public void set(int idx, boolean value) {
        if (!config.get(idx).equals(value)) {
            config.set(idx, value);
            if (value) {
                selectedItemCount++;
                price += knapsack.getItem(idx).getPrice();
                weight += knapsack.getItem(idx).getWeight();
            } else {
                selectedItemCount--;
                price -= knapsack.getItem(idx).getPrice();
                weight -= knapsack.getItem(idx).getWeight();
            }
        }
//        System.out.println( "setting idx:[" + idx + "]: " + value );
//        System.out.println( "price = " + getPrice() );
    }

    public KnapsackConfig switchItem(int idx) {
        set(idx, !config.get(idx));
        return this;
    }

    public boolean get(int idx) {
        return config.get(idx);
    }

    public int getItemCount() {
        return knapsack.getItemCount();
    }

    public Knapsack getKnapsack() {
        return knapsack;
    }

    public boolean isValid() {
        return getWeight() <= knapsack.getMaxWeight();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getItemCount(); i++) {
            sb.append((get(i)) ? " 1" : " 0");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.config);
        hash = 47 * hash + knapsack.hashCode();
        hash = 47 * hash + this.weight;
        hash = 47 * hash + this.price;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
//            System.out.println( "config object is null" );
            return false;
        }
        if (getClass() != obj.getClass()) {
//            System.out.println( "config classes do not match" );
            return false;
        }
        final KnapsackConfig other = (KnapsackConfig) obj;
        if (!Objects.equals(this.config.size(), other.config.size())) {
//            System.out.println( "configs have different size" );
            return false;
        }
        for (int i = 0; i < this.config.size(); i++) {
            if (!Objects.equals(this.config.get(i), other.config.get(i))) {
//                System.out.println( "configs[" + i + "] do not equal: this." + this.config.get( i ) + ", other." + other.config.get( i ) );
                return false;
            }
        }
        if (!this.knapsack.equals(other.knapsack)) {
//            System.out.println( "configs have different knapsacks" );
            return false;
        }
        if (this.weight != other.weight) {
//            System.out.println( "configs have different weight" );
            return false;
        }
        return this.price == other.price;
    }

    public KnapsackConfig setConfigFrom(KnapsackConfig knapsackConfig) {
        for (int i = 0; i < knapsack.getItemCount(); i++) {
            set(i, knapsackConfig.get(i));
        }
        return this;
    }
}
