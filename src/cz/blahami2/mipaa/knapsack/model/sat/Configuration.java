package cz.blahami2.mipaa.knapsack.model.sat;

import cz.blahami2.mipaa.knapsack.model.sat.formulas.Literal;

/**
 *
 * @author Michael Bláha
 */
public class Configuration {

    private final boolean[] literalValues;

    public Configuration( int size ) {
        literalValues = new boolean[size];
        for ( int i = 0; i < size; i++ ) {
            literalValues[i] = false;
        }
    }

    public void configureLiterals( Literal[] literals ) {
        if ( literals.length != literalValues.length ) {
            throw new IllegalArgumentException( "wrong size" );
        }
        for ( int i = 0; i < literals.length; i++ ) {
            literals[i].setValue( literalValues[i] );
        }
    }

    public Configuration set( int index, boolean value ) {
        Configuration c = new Configuration( literalValues.length );
        for ( int i = 0; i < literalValues.length; i++ ) {
            c.literalValues[i] = literalValues[i];
        }
        c.literalValues[index] = value;
        return c;
    }

    public boolean[] getResult() {
        return literalValues;
    }

    public String getPrintable() {
        String str = "";
        for ( boolean b : getResult() ) {
            str += " " + ( ( b ) ? "1" : "0" );
        }
        return str;
    }

}
