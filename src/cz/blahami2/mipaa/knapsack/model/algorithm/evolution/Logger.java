package cz.blahami2.mipaa.knapsack.model.algorithm.evolution;

import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.algorithm.SimulatedEvolution;
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

    private final Map<InputData, List<List<LogStruct>>> log = new HashMap<>();

    public Logger log( InputData id, int step, SimulatedEvolution.BitArrayEvaluable best, SimulatedEvolution.BitArrayEvaluable worst, double average ) {
        List<List<LogStruct>> list = getList( id );
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

    private List<List<LogStruct>> getList( InputData id ) {
        if ( log.containsKey( id ) ) {
            return log.get( id );
        } else {
            List<List<LogStruct>> list = new ArrayList<>();
            log.put( id, list );
            return list;
        }
    }

    public List<LogStruct> get( InputData id, int step ) {
        return getList( id ).get( step );
    }

    public LogResult getAverage( InputData id, int step ) {
        double bestSum = 0;
        double worstSum = 0;
        double avgSum = 0;
        List<LogStruct> list = get( id, step );
        for ( LogStruct ls : list ) {
            bestSum += ls.best.evaluateCost();
            worstSum += ls.worst.evaluateCost();
            avgSum += ls.averageCost;
        }
        return new LogResult( bestSum / list.size(), worstSum / list.size(), avgSum / list.size() );
    }

    public int getLowestPrice( InputData id ) {
        List<List<LogStruct>> list = getList( id );
        List<LogStruct> last = list.get( list.size() - 1 );
        return last.stream().map( x -> x.best.evaluateCost() ).min( Integer::compare ).get();
    }

    public int getAveragePrice( InputData id ) {
        List<List<LogStruct>> list = getList( id );
        List<LogStruct> last = list.get( list.size() - 1 );
        int total = last.stream().map( x -> x.best.evaluateCost() ).reduce( 0, (x, y) -> x + y );
        return total / last.size();
    }

    public int getStepsCount( InputData id ) {
        return getList( id ).size();
    }

    public Set<InputData> getAllInputs() {
        return log.keySet();
    }

    @Override
    public String toString() {
        return "Logger{" + "log=" + log + '}';
    }

    public static class LogStruct {

        public final SimulatedEvolution.BitArrayEvaluable best;
        public final SimulatedEvolution.BitArrayEvaluable worst;
        public final double averageCost;

        public LogStruct( SimulatedEvolution.BitArrayEvaluable best, SimulatedEvolution.BitArrayEvaluable worst, double averageCost ) {
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
