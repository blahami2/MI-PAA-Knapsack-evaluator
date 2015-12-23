package cz.blahami2.mipaa.knapsack.model.sat;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.NegationFormula;
import cz.blahami2.mipaa.knapsack.model.sat.formulas.Literal;
import cz.blahami2.mipaa.knapsack.model.sat.formulas.Formula;
import cz.blahami2.mipaa.knapsack.model.sat.formulas.AbstractFormula;
import cz.blahami2.mipaa.knapsack.model.sat.operands.OrOperand;
import cz.blahami2.mipaa.knapsack.model.sat.operands.AndOperand;
import cz.blahami2.mipaa.knapsack.model.sat.operands.AbstractOperand;

/**
 *
 * @author Michael Bláha
 */
public class SatParser {

    private final Literal[] literals = new Literal[100];
    private int counter;

    public Sat parse( String input ) {
        counter = 0;
        String[] splitInput = input.split( " " );
        AbstractFormula formula = parseStringIntoFormula( splitInput, 0, splitInput.length );
        Literal[] returnLiterals = new Literal[counter];
        for ( int i = 0; i < counter; i++ ) {
            returnLiterals[i] = literals[i];
            literals[i] = null;
        }
        return new Sat( returnLiterals, formula );
    }

    public AbstractFormula parseStringIntoFormula( String[] input, int start, int end ) {
//        System.out.println( "parsing:" );
//        for ( int i = start; i < end; i++ ) {
//            System.out.print( " " + input[i] );
//        }
//        System.out.println( "" );

        AbstractFormula formula = null;
        AbstractFormula currentFormula = null;
        int bracket = -1;
        int bracketDepth = 0;
        AbstractOperand operand = null;
        boolean neg = false;
        for ( int i = start; i < end; i++ ) {
            switch ( input[i] ) {
                case "(": // pair it
                    if ( bracket < 0 ) {
                        bracket = i;
                    }
                    bracketDepth++;
                    break;
                case ")":
                    if ( bracketDepth == 1 ) { // paired brackets
                        currentFormula = parseStringIntoFormula( input, bracket + 1, i );
                        if ( neg ) {
                            currentFormula = new NegationFormula( currentFormula );
                            neg = false;
                        }
                        if ( operand != null ) {
//                            System.out.println( "crating new formula: " + operand.getClass().getSimpleName() );
                            formula = new Formula( formula, currentFormula, operand );
                            operand = null;
                        } else if ( formula != null && operand == null ) {
                            throw new IllegalArgumentException( "Invalid input - operand missing after " + formula.getPrintable() );
                        } else {
//                            System.out.println( "gonna go deeper: [" + (bracket + 1) + "][" + i + "]" );
                            formula = currentFormula;
                        }
                    }
                    bracketDepth--;
                    break;
                case "+":
                    if ( bracket >= 0 ) {
                        break;
                    }
                    operand = OrOperand.getInstance();
                    if ( formula == null ) {
                        throw new IllegalArgumentException( "Invalid input - no left side for " + operand.getClass().getSimpleName() );
                    }
                    break;
                case "*":
                    if ( bracket >= 0 ) {
                        break;
                    }
                    operand = AndOperand.getInstance();
                    if ( formula == null ) {
                        throw new IllegalArgumentException( "Invalid input - no left side for " + operand.getClass().getSimpleName() );
                    }
                    break;
                case "!":
                    if ( bracket >= 0 ) {
                        break;
                    }
                    neg = !neg;
                    break;
                default:
                    if ( bracket >= 0 ) {
                        break;
                    }
                    currentFormula = getLiteral( input[i] );
                    if ( neg ) {
                        currentFormula = new NegationFormula( currentFormula );
                        neg = false;
                    }
                    if ( operand != null ) {
//                            System.out.println( "crating new formula: " + operand.getClass().getSimpleName() );
                        formula = new Formula( formula, currentFormula, operand );
                        operand = null;
                    } else if ( formula != null && operand == null ) {
                        throw new IllegalArgumentException( "Invalid input - operand missing after " + formula.getPrintable() );
                    } else {
                        formula = currentFormula;
                    }
            }
        }
        if ( operand != null ) {
            throw new IllegalArgumentException( "Invalid input - missing right side for " + operand.getClass().getSimpleName() );
        }
        if ( neg ) {
            throw new IllegalArgumentException( "Invalid input - missing content for " + NegationFormula.class.getSimpleName() );
        }
        if ( bracketDepth > 0 ) {
            throw new IllegalArgumentException( "Invalid input - brackets do not match: " + bracketDepth );
        }
        return formula;
    }

    private Literal getLiteral( String literalName ) {
        for ( int i = 0; i < counter; i++ ) {
            if ( literals[i].getName().equals( literalName ) ) {
                return literals[i];
            }
        }
//        System.out.println( "adding literal: " + literalName + " on position " + getCounter() );
        literals[counter] = new Literal( literalName );
        return literals[counter++];
    }
}
