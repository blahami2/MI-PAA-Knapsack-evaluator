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
public class GeneratorConfiguration {

    public final int id;
    public final int itemCount;
    public final int instanceCount;
    public final double weightRatio;
    public final int maxWeight;
    public final int maxPrice;
    public final double k;
    public final int d;

    public GeneratorConfiguration( int id, int itemCount, int instanceCount, double weightRatio, int maxWeight, int maxPrice, double k, int d ) {
        this.id = id;
        this.itemCount = itemCount;
        this.instanceCount = instanceCount;
        this.weightRatio = weightRatio;
        this.maxWeight = maxWeight;
        this.maxPrice = maxPrice;
        this.k = k;
        this.d = d;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builderFrom( GeneratorConfiguration generatorConfiguration ) {
        return new Builder( generatorConfiguration );
    }

    public static class Builder {

        private static int ID_COUNTER = 1;

        private int id = -1;
        private int itemCount = -1;
        private int instanceCount = -1;
        private double weightRatio = Double.NaN;
        private int maxWeight = -1;
        private int maxPrice = -1;
        private double k = Double.NaN;
        private int d = -2;

        public Builder() {
            this.id = ID_COUNTER++;
        }

        public Builder( GeneratorConfiguration config ) {
            this.id = config.id;
            this.itemCount = config.itemCount;
            this.instanceCount = config.instanceCount;
            this.weightRatio = config.weightRatio;
            this.maxWeight = config.maxWeight;
            this.maxPrice = config.maxPrice;
            this.k = config.k;
            this.d = config.d;
        }

        public Builder setId( int id ) {
            this.id = id;
            return this;
        }

        public Builder setItemCount( int itemCount ) {
            this.itemCount = itemCount;
            return this;
        }

        public Builder setInstanceCount( int instanceCount ) {
            this.instanceCount = instanceCount;
            return this;
        }

        public Builder setWeightRatio( double weightRatio ) {
            this.weightRatio = weightRatio;
            return this;
        }

        public Builder setMaxWeight( int maxWeight ) {
            this.maxWeight = maxWeight;
            return this;
        }

        public Builder setMaxPrice( int maxPrice ) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder setK( double k ) {
            this.k = k;
            return this;
        }

        public Builder setGranularityExponentK( double k ) {
            this.k = k;
            return this;
        }

        public Builder setD( int d ) {
            this.d = d;
            return this;
        }

        public Builder setGranularityParameterD( int d ) {
            this.d = d;
            return this;
        }

        public GeneratorConfiguration build() {
            if ( ( id == -1 )
                 || ( itemCount == -1 )
                 || ( instanceCount == -1 )
                 || ( weightRatio == Double.NaN )
                 || ( maxWeight == -1 )
                 || ( maxPrice == -1 )
                 || ( k == Double.NaN ) ) {
                throw new IllegalStateException( "Undefined parameters." );
            }
            return new GeneratorConfiguration( id, itemCount, instanceCount, weightRatio, maxWeight, maxPrice, k, d );
        }

    }
}
