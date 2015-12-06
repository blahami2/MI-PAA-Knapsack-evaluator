/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model;

/**
 *
 * @author MBlaha
 */
public class KnapsackItem implements Comparable {
    private final int weight;
    private final int price;
    private final int id;

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public KnapsackItem( int id, int weight, int price ) {
        this.id = id;
        this.weight = weight;
        this.price = price;
    }
    
    public double getRatio(){
        return (double) price / (double) weight;
    }

    @Override
    public int compareTo( Object o ) {
        KnapsackItem otherItem = (KnapsackItem) o;
        return Double.compare( getRatio(), otherItem.getRatio());
    }
    
}
