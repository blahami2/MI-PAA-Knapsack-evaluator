package cz.blahami2.mipaa.knapsack.model.sat.formulas;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.AbstractFormula;

/**
 *
 * @author Michael Bláha
 */
public class Literal extends AbstractFormula {
    private boolean value;
    private final String name;

    public Literal(String name) {
        this.name = name;
    }

    @Override
    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPrintable() {
        return " " + name;
    }
}
