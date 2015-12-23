package cz.blahami2.mipaa.knapsack.model.sat.operands;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.AbstractFormula;
import cz.blahami2.mipaa.knapsack.model.sat.operands.AbstractOperand;

/**
 *
 * @author MBlaha
 */
public class OrOperand extends AbstractOperand {

    private static final OrOperand instance = new OrOperand();
    
    public static OrOperand getInstance(){
        return instance;
    }
    
    private OrOperand(){
        
    }

    @Override
    public boolean evaluate( AbstractFormula a, AbstractFormula b ) {
        return a.getValue() || b.getValue();
    }
    
}
