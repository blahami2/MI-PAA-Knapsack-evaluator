package cz.blahami2.mipaa.knapsack.utils;

import cz.blahami2.mipaa.knapsack.model.Result;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 *
 * @author Michael Blaha
 */
public class FormatUtils {

    private static final String DECIMAL_FORMAT = "%.03f";
    private static final int DECIMAL_DIVISOR = 1000;

    public static String formatTimeInNanos( long time ) {
        return formatDecimal( nanosToMillis( time ) );
    }

    public static String formatDecimal( double decimal ) {
        return String.format( DECIMAL_FORMAT, decimal ).replace( '.', ',' );
    }

    public static String formatNumber( double decimal ) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator( ' ' );
        symbols.setDecimalSeparator( ',' );
        DecimalFormat formatter = new DecimalFormat( "###,###.###", symbols );
        formatter.setMaximumFractionDigits( 3 );
        formatter.setMinimumFractionDigits( 3 );
        return formatter.format( decimal );
    }

    public static double nanosToMillis( long nanos ) {
        return nanos * 0.000001;
    }
}
