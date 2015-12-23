package cz.blahami2.mipaa.knapsack.model.sat.operands;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.AbstractFormula;

/**
 *
 * @author Michael Bláha
 */
public abstract class AbstractOperand {

    protected AbstractOperand() {

    }

    public abstract boolean evaluate( AbstractFormula a, AbstractFormula b );
}
