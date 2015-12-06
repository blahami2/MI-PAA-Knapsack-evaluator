/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model.algorithm;

import java.util.ArrayList;
import java.util.List;
import cz.blahami2.mipaa.knapsack.model.Knapsack;
import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.model.SimulatedAnnealingConfiguration;
import cz.blahami2.mipaa.knapsack.utils.RandomUtils;

/**
 *
 * @author MBlaha
 */
public class SimulatedAnnealing extends Algorithm<SimulatedAnnealingConfiguration> {

    public static final SimulatedAnnealingConfiguration DEFAULT_CONFIG = new SimulatedAnnealingConfiguration( 10, 0.02, 0.995, 50 );

//    private static final double VARIATION_RATIO = 0.1;
    private SimulatedAnnealingConfiguration config;

//    private int numberOfTemperatureCycles;
    private List<Double> xArray = null; // prices over calculation

    public SimulatedAnnealing( int warmupCycles, int runCycles ) {
        super( warmupCycles, runCycles );
        init();
    }

    private void init() {
//        numberOfTemperatureCycles = (int) Math.round( Formula.log( config.TEMPERATURE_FINAL / config.TEMPERATURE_INITIAL, config.TEMPERATURE_MULTIPLIER ) );
    }

    @Override
    public KnapsackConfig solve( Knapsack knapsack, SimulatedAnnealingConfiguration data ) {
        this.config = data;
        // preparation for automatic adjustment
//        double pricePerWeight = knapsack.getPriceSum() / (double) knapsack.getWeightSum();
//        double capacityPrice = pricePerWeight * knapsack.getCapacity();
//        double variation = capacityPrice * VARIATION_RATIO;
//        int numberOfSavedValues = (int) ( VARIATION_RATIO * numberOfTemperatureCycles );
//        xArray = new ArrayList<>( numberOfSavedValues );

//        System.out.println( "NEW KNAPSACK" );
        KnapsackConfig bestValidConfig = new KnapsackConfig( knapsack );
        KnapsackConfig currentConfig = new KnapsackConfig( knapsack );
        KnapsackConfig workConfig = new KnapsackConfig( knapsack );
        for ( double temperature = config.TEMPERATURE_INITIAL; temperature > config.TEMPERATURE_FINAL; temperature *= config.TEMPERATURE_MULTIPLIER ) {
            for ( int innerLoopIndex = 0; innerLoopIndex < config.INNER_LOOP; innerLoopIndex++ ) {
                int randomItemIndex = getRandomItemIndex( knapsack.getItemCount() );
                workConfig.switchItem( randomItemIndex );
                int costDifference = currentConfig.getPrice() - workConfig.getPrice();
//                System.out.println( "checking item [" + randomItemIndex + "][" + item.getWeight() + ", " + item.getPrice() + "]" );
//                System.out.println( "randomItem = " + randomItemIndex + ", result = " + resultConfig.getPrice() + ", current = " + workConfig.getPrice() + ", temp = " + temperature + ", inner = " + innerLoopIndex );
//                System.out.println( "result weight = " + resultConfig.getWeight() + ", current weight = " + workConfig.getWeight() + ", capacity = " + knapsack.getCapacity() );
//                System.out.println( "result = " + resultConfig.toString() );
//                System.out.println( "current = " + workConfig.toString() );
                if ( isAccepted( knapsack, workConfig, costDifference, temperature ) ) {
                    currentConfig.switchItem( randomItemIndex );
                    if ( xArray != null ) {
                        xArray.add( (double) currentConfig.getPrice() );
                    }
                    if ( currentConfig.isValid() && ( currentConfig.getPrice() > bestValidConfig.getPrice() ) ) {
                        bestValidConfig = new KnapsackConfig( currentConfig );
                    }
//                    System.out.println( ((resultConfig.get( randomItemIndex)) ? "added" : "removed") + " item [" + randomItemIndex + "][" + item.getWeight() + ", " + item.getPrice() + "] with final price = " + resultConfig.getPrice() + ", and weight = " + resultConfig.getWeight() + " and capacity = " + knapsack.getCapacity() );
                } else {
                    workConfig.switchItem( randomItemIndex );
                }
            }
        }

        return bestValidConfig;
    }

    public void newProgressArray() {
        xArray = new ArrayList<>();
    }

    public List<Double> getProgressArray() {
        List<Double> returnArray = xArray;
        xArray = null;
        return returnArray;
    }

    private int getRandomItemIndex( int itemCount ) {
        return RandomUtils.nextInt( itemCount );
    }

    private boolean isAccepted( Knapsack knapsack, KnapsackConfig workConfig, int costDifference, double temperature ) {
        // infeasable states
        if ( workConfig.getWeight() > knapsack.getMaxWeight() ) {
            costDifference += 1000 * ( workConfig.getWeight() - knapsack.getMaxWeight() );
        }
        // first improvement
        if ( costDifference < 0 ) {
            return true;
        }
        // threshold
        return RandomUtils.nextDouble() < ( Math.pow( Math.E, -costDifference / temperature ) );
    }

    private double countVariation() {
        return 0;
    }

}
