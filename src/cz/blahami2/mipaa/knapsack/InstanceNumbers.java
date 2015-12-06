package cz.blahami2.mipaa.knapsack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Michael Blaha
 */
public enum InstanceNumbers {

    SMALL( 15 ),
    MEDIUM( 25 ),
    LARGE( 30 ),
    FULL( 40 ),
    LARGE_ONLY(30,40),
    SINGLE_NUMBER_40(40, 40),
    SINGLE_NUMBER_4(4, 4);

    private static final List<Integer> instanceNumbers = Arrays.asList( 4, 10, 15, 20, 22, 25, 27, 30, 32, 35, 37, 40 );

    private final int min;
    private final int max;

    private InstanceNumbers( int limit ) {
        this.min = 0;
        this.max = limit;
    }    
    
    private InstanceNumbers( int min, int max ) {
        this.min = min;
        this.max = max;
    }

    public List<Integer> getInstanceNumbers() {
        return instanceNumbers.stream().filter(n -> {
            return (min <= n && n <= max);
        } ).sorted().collect( Collectors.toList() );
    }

}
