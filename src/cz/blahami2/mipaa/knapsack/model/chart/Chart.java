package cz.blahami2.mipaa.knapsack.model.chart;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Blaha
 */
public class Chart {

    private String title = "";
    private Axis xAxis;
    private Axis yAxis;
    private final List<PointSet> pointSets = new ArrayList<>();

    private Chart() {
    }

    public String getTitle() {
        return title;
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }

    public Chart adjustLimitsToPoints() {
        int xMax = Integer.MIN_VALUE;
        int xMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE;
        for ( PointSet pointSet : pointSets ) {
            for ( Point point : pointSet.getPoints() ) {
//                System.out.println( "Point: " + point );
                xMax = ( xMax > point.getX() ) ? xMax : point.getX();
                xMin = ( xMin < point.getX() ) ? xMin : point.getX();
                yMax = ( yMax > point.getY() ) ? yMax : point.getY();
                yMin = ( yMin < point.getY() ) ? yMin : point.getY();
            }
        }
        if ( xMax < 0 || xMax == Integer.MIN_VALUE ) {
            xMax = 0;
        }
        if ( xMin > 0 || xMin == Integer.MAX_VALUE ) {
            xMin = 0;
        }
        if ( yMax < 0 || yMax == Integer.MIN_VALUE ) {
            yMax = 0;
        }
        if ( yMin > 0 || yMin == Integer.MAX_VALUE ) {
            yMin = 0;
        }
        xAxis.setMax( xMax );
        xAxis.setMin( xMin );
        yAxis.setMax( yMax );
        yAxis.setMin( yMin );

        int xStep = 1;
        int tmpMax = xMax;
        while ( tmpMax / 10 > 0 ) {
            xStep *= 10;
            tmpMax /= 10;
        }
        if(xStep > 1){
            xStep /= 2;
        }
        xAxis.setStep( xStep );
        int yStep = 1;
        tmpMax = yMax;
        while ( tmpMax / 10 > 0 ) {
            yStep *= 10;
            tmpMax /= 10;
        }
        if(yStep > 1){
            yStep /= 2;
        }
        yAxis.setStep( yStep );
        return this;
    }

    public List<PointSet> getPointSets() {
        return new ArrayList<>( pointSets );
    }
    
    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private final Chart chart;

        public Builder() {
            this.chart = new Chart();
        }

        public Builder setTitle( String title ) {
            chart.title = title;
            return this;
        }

        public Builder setXAxis( Axis axis ) {
            chart.xAxis = axis;
            return this;
        }

        public Builder setYAxis( Axis axis ) {
            chart.yAxis = axis;
            return this;
        }

        public Builder addPointSet( PointSet pointSet ) {
            chart.pointSets.add( pointSet );
            return this;
        }

        public Chart build() {
            if ( chart.xAxis == null || chart.yAxis == null ) {
                throw new IllegalStateException( "Missing at least one axis" );
            }
            PointSet.resetColors();
            return chart;
        }
    }
}
