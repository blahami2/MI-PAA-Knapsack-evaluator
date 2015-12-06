package cz.blahami2.mipaa.knapsack.model.algorithm.evolution;

/**
 *
 * @author Michael Blaha
 */
public interface BitArray {
    public void set(int idx, boolean value);
    public boolean get(int idx);
    public int size();
}
