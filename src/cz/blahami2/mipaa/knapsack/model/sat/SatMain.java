package cz.blahami2.mipaa.knapsack.model.sat;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.Literal;

/**
 *
 * @author Michael Bláha
 */
public class SatMain {

    static String[] testCases = {
        "( a * b )", "( a * ! ( c + b ) )", "( a * ( b + c + ( d * a ) ) )", "( a", "a +", "a + b", "a+b", "a+ b", "( a ) ( b )"
    };

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        SatParser parser = new SatParser();
        SatSolver solver = new SatSolver();
        for ( String test : testCases ) {
            try {
                System.out.println( "input = " + test );
                Sat sat = parser.parse( test );
                System.out.println( sat.formula.getPrintable() );
                System.out.print( "literals: " );
                for ( Literal literal : sat.literals ) {
                    System.out.print( literal.getPrintable() );
                }
                System.out.println( "" );
                Configuration solve = solver.solve( sat );
                if ( solve != null ) {
                    System.out.println( "solution: " + solve.getPrintable() );
                } else {
                    System.out.println( "no solution" );
                }
            } catch ( IllegalArgumentException ex ) {
//                Thread.sleep( 100 );
//                System.err.println( "========================================" );
                System.err.println( "exception thrown for: " + test );
                System.err.println( ex.toString() );
//                System.err.println( "========================================" );
//                Thread.sleep( 100 );
            } finally {
                System.out.println( "haha" );
            }
        }
    }

}
