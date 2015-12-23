package cz.blahami2.mipaa.knapsack.model.sat;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.Literal;
import cz.blahami2.mipaa.knapsack.model.sat.formulas.AbstractFormula;

/**
 *
 * @author Michael Bláha
 */
public class SatSolver {

    public Configuration solve( Sat sat ) {
        Configuration configuration = new Configuration( sat.literals.length );
        return solve( configuration, sat.formula, sat.literals, 0 );
    }

    private Configuration solve( Configuration configuration, AbstractFormula formula, Literal[] literals, int swapIndex ) {
        if ( swapIndex == literals.length ) {
            configuration.configureLiterals( literals );
//            System.out.println( "trying configuration[" + swapIndex + "]: " + configuration.getPrintable() + " with result: " + formula.getValue() );
            if ( formula.getValue() ) {
                return configuration;
            } else {
                return null;
            }
        } else {
//            System.out.println( "calling configuration[" + swapIndex + "]: " + configuration.getPrintable() + " with result: " + formula.getValue() );
            Configuration result = solve( configuration.set( swapIndex, true ), formula, literals, swapIndex + 1 );
            if ( result != null ) {
                return result;
            }
            result = solve( configuration.set( swapIndex, false ), formula, literals, swapIndex + 1 );
            return result;
        }
    }

}
