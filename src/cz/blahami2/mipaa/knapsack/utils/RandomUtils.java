
package cz.blahami2.mipaa.knapsack.utils;

import java.util.Random;

/**
 *
 * @author Michael Blaha
 */
public class RandomUtils {
    private static final Random RANDOM = new Random();
    
    public static int nextInt(){
        return RANDOM.nextInt();
    }
    
    public static int nextInt(int max){
        return RANDOM.nextInt( max );
    }
    
    public static int nextInt(int from, int to){
        int bound = to - from;
        return RANDOM.nextInt( bound ) + from;
    }
    
    public static double nextDouble(){
        return RANDOM.nextDouble();
    }
}
