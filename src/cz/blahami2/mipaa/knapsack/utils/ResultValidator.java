/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.blahami2.mipaa.knapsack.utils;

import cz.blahami2.mipaa.knapsack.model.KnapsackConfig;
import cz.blahami2.mipaa.knapsack.model.KnapsackResult;
import java.util.List;

/**
 *
 * @author MBlaha
 */
public class ResultValidator {
    
    public static boolean validate(List<KnapsackResult> results, List<KnapsackConfig> expectedConfigs){
        if(results.size() != expectedConfigs.size()){
            throw new IllegalArgumentException("Incompatible comparison: different size");
        }
        for(int i = 0; i < results.size(); i++){
            if(!results.get( i ).getConfig().equals( expectedConfigs.get( i))){
                return false;
            }
        }
        return true;
    }

    public static boolean validate( KnapsackConfig a, KnapsackConfig b ) {

        return a.equals( b );
    }
}
