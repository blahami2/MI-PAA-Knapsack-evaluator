/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model;

import cz.blahami2.mipaa.knapsack.model.algorithm.evolution.InputData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 *
 * @author MBlaha
 */
public class Knapsack implements InputData {

    int id;
    int itemCount;
    int maxWeight;
    int maxPrice = -1;
    int weightSum = -1;
    int priceSum = -1;
    ArrayList<KnapsackItem> items;

    public Knapsack( int id, int itemCount, int maxWeight ) {
        this.itemCount = itemCount;
        this.id = id;
        this.maxWeight = maxWeight;
        this.items = new ArrayList<>();
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getId() {
        return id;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void addItem( KnapsackItem item ) {
        items.add( item );
    }

    public KnapsackItem getItem( int idx ) {
        return items.get( idx );
    }

    public int getMaxPrice() {
        if ( maxPrice == -1 ) {
            maxPrice = items.stream().map( (x) -> x.getPrice() ).max( Integer::compare ).get();
        }
        return maxPrice;
    }

    public int getWeightSum() {
        if ( weightSum == -1 ) {
            weightSum = items.stream().parallel().reduce( 0, (x, y) -> x + y.getWeight(), (x, y) -> x + y );
        }
        return weightSum;
    }

    public int getPriceSum() {
        if ( priceSum == -1 ) {
            priceSum = items.stream().parallel().reduce( 0, (x, y) -> x + y.getPrice(), (x, y) -> x + y );
        }
        return priceSum;
    }

    public void sortItemsByRatio() {
        Collections.sort( items, Collections.reverseOrder() );
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.id;
        hash = 79 * hash + this.itemCount;
        hash = 79 * hash + this.maxWeight;
        hash = 79 * hash + Objects.hashCode( this.items );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Knapsack other = (Knapsack) obj;
        if ( this.id != other.id ) {
            return false;
        }
        if ( this.itemCount != other.itemCount ) {
            return false;
        }
        if ( this.maxWeight != other.maxWeight ) {
            return false;
        }
        if ( !Objects.equals( this.items.size(), other.items.size() ) ) {
            return false;
        }
        for ( int i = 0; i < this.itemCount; i++ ) {
            if ( this.items.get( i ).getId() != other.items.get( i ).getId() ) {
                return false;
            }
            if ( this.items.get( i ).getPrice() != other.items.get( i ).getPrice() ) {
                return false;
            }
            if ( this.items.get( i ).getWeight() != other.items.get( i ).getWeight() ) {
                return false;
            }
        }
        return true;
    }

}
