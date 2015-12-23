package cz.blahami2.mipaa.knapsack.model.sat.formulas;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.AbstractFormula;
import cz.blahami2.mipaa.knapsack.model.sat.operands.AbstractOperand;

/**
 *
 * @author Michael Bláha
 */
public class Formula extends AbstractFormula {

    private final AbstractFormula a;
    private final AbstractFormula b;
    private final AbstractOperand operand;

    public Formula( AbstractFormula a, AbstractFormula b, AbstractOperand operand ) {
        this.a = a;
        this.b = b;
        this.operand = operand;
    }

    @Override
    public boolean getValue() {
        return operand.evaluate( a, b );
    }

    @Override
    public String getPrintable() {
        return " (" + a.getPrintable() + " " + operand.getClass().getSimpleName() + b.getPrintable() + " )";
    }

}
