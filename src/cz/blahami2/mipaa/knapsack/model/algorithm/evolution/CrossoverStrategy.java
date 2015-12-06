package cz.blahami2.mipaa.knapsack.model.algorithm.evolution;

import cz.blahami2.mipaa.knapsack.utils.RandomUtils;

/**
 * TODO: see MutationStrategy for fix!!!
 *
 *
 * @author Michael Bláha
 * @param <T>
 */
public interface CrossoverStrategy<T> {

    public static final CrossoverStrategy<BitArray> ONE_POINT = new CrossoverStrategy<BitArray>() {
        @Override
        public String name() {
            return "One-point";
        }

        @Override
        public Duo<BitArray> crossover( BitArray aArray, BitArray bArray, Creator<BitArray> creator ) {
            if ( aArray.size() != bArray.size() ) {
                throw new IllegalArgumentException( "Incompatible sizes" );
            }
            System.out.println( "CROSSOVER:" );
            System.out.println( "aArray: " + aArray );
            System.out.println( "bArray: " + bArray );
            int size = aArray.size();
            int position = RandomUtils.nextInt( size - 2 ) + 1; // from 1 to size-1
            System.out.println( "position = " + position + "( from 1 to " + (size - 2) + ")" );
            BitArray aSibling = creator.createCopyFrom( aArray );
            BitArray bSibling = creator.createCopyFrom( bArray );
            for ( int i = 0; i < position; i++ ) {
                aSibling.set( i, bArray.get( i ) );
                bSibling.set( i, aArray.get( i ) );
            }
            return new Duo<>( aSibling, bSibling );
        }
    };

    public static final CrossoverStrategy<BitArray> TWO_POINT = new CrossoverStrategy<BitArray>() {
        @Override
        public String name() {
            return "Two-point";
        }

        @Override
        public Duo<BitArray> crossover( BitArray aArray, BitArray bArray, Creator<BitArray> creator ) {
            if ( aArray.size() != bArray.size() ) {
                throw new IllegalArgumentException( "Incompatible sizes" );
            }
            int size = aArray.size();
            int firstPosition = RandomUtils.nextInt( size - 3 ) + 1; // from 1 to size-2
            int secondPosition = RandomUtils.nextInt( size - firstPosition - 1 ) + firstPosition; // from firstPosition to size - 1
            BitArray aSibling = creator.createCopyFrom( aArray );
            BitArray bSibling = creator.createCopyFrom( bArray );
            for ( int i = firstPosition; i < secondPosition; i++ ) {
                aSibling.set( i, bArray.get( i ) );
                bSibling.set( i, aArray.get( i ) );
            }
            return new Duo<>( aSibling, bSibling );
        }
    };
//    = (BitArray aArray, BitArray bArray, Creator<BitArray> creator) -> {
//    };

    public static final CrossoverStrategy<BitArray> UNIFORM = new CrossoverStrategy<BitArray>() {
        @Override
        public String name() {
            return "Uniform";
        }

        @Override
        public Duo<BitArray> crossover( BitArray a, BitArray b, Creator<BitArray> creator ) {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
        }
    };

    public String name();

    public Duo<T> crossover( T a, T b, Creator<T> creator );   
    

    public static class Duo<T> {

        public final T a;
        public final T b;

        public Duo( T a, T b ) {
            this.a = a;
            this.b = b;
        }
    }

    public interface Creator<T> {

        T createCopyFrom( T original );
    }
}
