package cz.blahami2.mipaa.knapsack.model.sat.formulas;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.AbstractFormula;

/**
 *
 * @author Michael Bláha
 */
public class NegationFormula extends AbstractFormula {

    private final AbstractFormula formula;

    public NegationFormula( AbstractFormula formula ) {
        this.formula = formula;
    }

    @Override
    public boolean getValue() {
        return !formula.getValue();
    }

    @Override
    public String getPrintable() {
        return " NegationFormula(" + formula.getPrintable() + " )";
    }

}
