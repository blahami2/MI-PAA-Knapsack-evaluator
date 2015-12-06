package cz.blahami2.mipaa.knapsack.model.algorithm.evolution;

import cz.blahami2.mipaa.knapsack.model.Knapsack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Michael Bláha
 */
public class Logger {

    private final Map<Knapsack, List<List<LogStruct>>> log = new HashMap<>();

    public Logger log( Knapsack knapsack, int step, KnapsackConfig best, KnapsackConfig worst, double average ) {
        List<List<LogStruct>> list = getListForKnapsack( knapsack );
        while ( list.size() <= step ) {
            list.add( null );
        }
        List<LogStruct> logList;
        if ( list.get( step ) == null ) {
            logList = new ArrayList<>();
            list.set( step, logList );
        } else {
            logList = list.get( step );
        }
        logList.add( new LogStruct( best, worst, average ) );
        return this;
    }

    private List<List<LogStruct>> getListForKnapsack( Knapsack knapsack ) {
        if ( log.containsKey( knapsack ) ) {
            return log.get( knapsack );
        } else {
            List<List<LogStruct>> list = new ArrayList<>();
            log.put( knapsack, list );
            return list;
        }
    }

    public List<LogStruct> get( Knapsack knapsack, int step ) {
        return getListForKnapsack( knapsack ).get( step );
    }

    public LogResult getAverage( Knapsack knapsack, int step ) {
        double bestSum = 0;
        double worstSum = 0;
        double avgSum = 0;
        List<LogStruct> list = get( knapsack, step );
        for ( LogStruct ls : list ) {
            bestSum += ls.best.getPrice();
            worstSum += ls.worst.getPrice();
            avgSum += ls.averageCost;
        }
        return new LogResult( bestSum / list.size(), worstSum / list.size(), avgSum / list.size() );
    }

    public int getStepsCount( Knapsack knapsack ) {
        return getListForKnapsack( knapsack ).size();
    }

    public Set<Knapsack> getAllKnapsacks() {
        return log.keySet();
    }

    @Override
    public String toString() {
        return "Logger{" + "log=" + log + '}';
    }

    public static class LogStruct {

        public final KnapsackConfig best;
        public final KnapsackConfig worst;
        public final double averageCost;

        public LogStruct( KnapsackConfig best, KnapsackConfig worst, double averageCost ) {
            this.best = best;
            this.worst = worst;
            this.averageCost = averageCost;
        }
    }

    public static class LogResult {

        public final double best;
        public final double worst;
        public final double average;

        public LogResult( double best, double worst, double average ) {
            this.best = best;
            this.worst = worst;
            this.average = average;
        }
    }
}
