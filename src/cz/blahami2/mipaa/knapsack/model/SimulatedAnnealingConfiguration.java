/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.model;

/**
 *
 * @author MBlaha
 */
public class SimulatedAnnealingConfiguration {    
    public final double TEMPERATURE_INITIAL;
    public final double TEMPERATURE_FINAL;
    public final double TEMPERATURE_MULTIPLIER;
    public final int INNER_LOOP;

    public SimulatedAnnealingConfiguration( double TEMPERATURE_INITIAL, double TEMPERATURE_FINAL, double TEMPERATURE_MULTIPLIER, int INNER_LOOP ) {
        this.TEMPERATURE_INITIAL = TEMPERATURE_INITIAL;
        this.TEMPERATURE_FINAL = TEMPERATURE_FINAL;
        this.TEMPERATURE_MULTIPLIER = TEMPERATURE_MULTIPLIER;
        this.INNER_LOOP = INNER_LOOP;
    }
}
