package cz.blahami2.mipaa.knapsack.model.chart;

/**
 *
 * @author Michael Blaha
 */
public class Axis {

    private String label = "";
    private int min = 0;
    private int max = 100;
    private int step = 1;
    private int divideBy = 1;
    private String format = "%.0f";
    private ValueToStringStrategy valueToStringStrategy;

    private Axis() {
    }

    public String getLabel() {
        return label;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getStep() {
        return step;
    }

    public int getDivideBy() {
        return divideBy;
    }

    void setMin( int min ) {
        this.min = min;
    }

    void setMax( int max ) {
        this.max = max;
    }

    void setStep( int step ) {
        this.step = step;
    }

    public String valueToString( int value ) {
        if ( valueToStringStrategy != null ) {
            return valueToStringStrategy.valueToString( value );
        }
//        System.out.println( "valueToString returning: " + String.format( format, value / (double) divideBy ) + " format(\"" + format + "\", " + value / (double) divideBy + "), " + value + " / " + divideBy );
        return String.format( format, value / (double) divideBy );
    }

    @Override
    public String toString() {
        return "Axis{" + "label=" + label + ", min=" + min + ", max=" + max + ", step=" + step + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public interface ValueToStringStrategy {

        String valueToString( int value );
    }

    public static class Builder {

        private final Axis axis;
        private int multiplier = 1;

        public Builder() {
            this.axis = new Axis();
        }

        public Builder setLabel( String label ) {
            axis.label = label;
            return this;
        }

        public Builder setMinimumValue( int minValue ) {
            axis.min = minValue;
            return this;
        }

        public Builder setMaximumValue( int maxValue ) {
            axis.max = maxValue;
            return this;
        }

        public Builder setStep( int step ) {
            axis.step = step;
            return this;
        }

        public Builder setDecimalStep( double step ) {
            if ( step <= 1 ) {
                axis.step = (int) ( 1 / step );
            } else {
                throw new UnsupportedOperationException();
            }
            return this;
        }

        public Builder divideBy( int divisor ) {
            axis.divideBy = divisor;
            return this;
        }

        public Builder setFormat( String format ) {
            axis.format = format;
            return this;
        }

        public Builder setValueToStringStrategy( ValueToStringStrategy valueToStringStrategy ) {
            axis.valueToStringStrategy = valueToStringStrategy;
            return this;
        }

        public Axis build() {
            return axis;
        }

    }

}
