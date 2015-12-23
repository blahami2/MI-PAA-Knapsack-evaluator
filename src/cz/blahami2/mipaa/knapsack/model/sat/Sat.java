package cz.blahami2.mipaa.knapsack.model.sat;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.Literal;
import cz.blahami2.mipaa.knapsack.model.sat.formulas.AbstractFormula;

/**
 *
 * @author Michael Bláha
 */
public class Sat {
    public final Literal[] literals;
    public final AbstractFormula formula;

    public Sat(Literal[] literals, AbstractFormula formula) {
        this.literals = literals;
        this.formula = formula;
    }
    
    
}
