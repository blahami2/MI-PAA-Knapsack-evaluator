package cz.blahami2.mipaa.knapsack.model.algorithm.evolution;

import cz.blahami2.mipaa.knapsack.utils.RandomUtils;

/**
 *
 * @author Michael Bláha
 * @param <T>
 */
public interface MutationStrategy<T> {

    public static final MutationStrategy<BitArray> RANDOM_BIT_INVERSION = new MutationStrategy<BitArray>(){
        @Override
        public BitArray mutate( BitArray array ) {
        int position = RandomUtils.nextInt(array.size());
        array.set(position, !array.get(position));
        return array;
        }

        @Override
        public String name() {
            return "Random bit inversion";
        }
        
    };

    public T mutate(T array);
    
    public String name();
}
