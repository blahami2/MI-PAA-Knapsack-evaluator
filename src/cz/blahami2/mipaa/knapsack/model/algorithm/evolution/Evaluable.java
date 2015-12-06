package cz.blahami2.mipaa.knapsack.model.algorithm.evolution;

/**
 *
 * @author Michael Bláha
 */
public interface Evaluable extends Comparable {

    public int evaluateCost();

    public int getCost();
    
    public boolean isValid();
    
    public double getInvalidRatio();

    default public double evaluateFitness( double averageCost, double invalidRate ) {
        double f = getCost() / averageCost;
        if(!isValid()){
            f *= invalidRate / getInvalidRatio();
        }
        setFitness( f );
        return f;
    }

    public void setFitness( double fitness );

    public double getFitness();

    @Override
    default public int compareTo( Object o ) {
        return ComparatorNatural.INSTANCE.compare( this, (Evaluable) o );
    }

    public enum ComparatorNatural implements java.util.Comparator<Evaluable> {
        INSTANCE;

        @Override
        public int compare( Evaluable o1, Evaluable o2 ) {
            return Double.compare( o1.getFitness(), o2.getFitness() );
        }

    }

    public enum ComparatorReversed implements java.util.Comparator<Evaluable> {
        INSTANCE;

        @Override
        public int compare( Evaluable o1, Evaluable o2 ) {
            return Double.compare( o2.getFitness(), o1.getFitness() );
        }

    }
}
