package cz.blahami2.mipaa.knapsack.model.tasks;

import cz.blahami2.mipaa.knapsack.model.io.DataManager;
import java.io.IOException;

/**
 *
 * @author Michael Blaha
 */
public abstract class HomeWorkTask implements Runnable {

    private static final double EPS = 0.0000001;

    private final int warmup;
    private final int approx;
    protected static final int DECIMAL_DIVISOR = 1000;
    protected static final String DECIMAL_FORMAT = "%.03f";
    private final DataManager dataManager;

    public HomeWorkTask( int warmup, int approx ) throws IOException {
        this.warmup = warmup;
        this.approx = approx;
        this.dataManager = new DataManager();
    }

    protected int getWarmup() {
        return warmup;
    }

    protected int getApprox() {
        return approx;
    }

    protected DataManager getDataManager() {
        return dataManager;
    }

    protected static int compare( double a, double b ) {
        if ( a + EPS < b ) {
            return -1;
        }
        if ( b + EPS < a ) {
            return 1;
        }
        return 0;
    }

    protected static int round( double number ) {
        return (int) Math.round( number );
    }

    protected static int round( double number, int divisor ) {
        return (int) Math.round( divisor * number );
    }
}
