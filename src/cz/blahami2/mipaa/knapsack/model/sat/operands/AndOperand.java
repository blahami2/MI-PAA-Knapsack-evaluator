package cz.blahami2.mipaa.knapsack.model.sat.operands;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.AbstractFormula;
import cz.blahami2.mipaa.knapsack.model.sat.operands.AbstractOperand;

/**
 *
 * @author MBlaha
 */
public class AndOperand extends AbstractOperand {
    private static final AndOperand instance = new AndOperand();
    
    public static AndOperand getInstance(){
        return instance;
    }
    
    private AndOperand(){
        
    }

    @Override
    public boolean evaluate( AbstractFormula a, AbstractFormula b ) {
        return a.getValue() && b.getValue();
    }
    
}
