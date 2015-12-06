package cz.blahami2.mipaa.knapsack.model.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Blaha
 */
public class PointSet {

    private String title = "";
    private final List<Point> points = new ArrayList<>();
    private Color color;

    private static int clrCounter = 0;
//    private static final Color[] COLORS = {
//        new Color( 91, 155, 213 ) // blue
//        , new Color( 237, 125, 49 ) // orange
//        , new Color( 112, 173, 71 ) // green
//        , new Color( 68, 114, 196 ) // dark blue
//        , new Color( 255, 192, 0 ) // yellow
//        , new Color( 165, 165, 165 ) // gray
//        , new Color( 158, 72, 14 ) // dark orange
//        , new Color( 99, 99, 99 ) // dark gray
//        , new Color( 153, 115, 0 ) // swamp green
//        , new Color( 67, 104, 43 ) // dark green
//        , new Color( 124, 175, 221 ) // light blue
//        , new Color( 241, 151, 90 ) // light orange
//        , new Color( 0, 0, 0 ) // black
//    };
    private static final Color[] COLORS = {
        new Color( 112, 110, 178 ), new Color( 255, 92, 3 ), new Color( 7, 0, 255 ), new Color( 130, 204, 112 ), new Color( 36, 178, 0 ), new Color( 165, 165, 165 ), new Color( 110, 175, 178 ), new Color( 255, 23, 172 ), new Color( 0, 243, 255 ), new Color( 204, 191, 112 ), new Color( 178, 153, 0 ), new Color( 88, 88, 88 ), new Color( 110, 178, 145 ), new Color( 172, 2, 255 ), new Color( 0, 255, 130 ), new Color( 204, 173, 112 ), new Color( 178, 118, 0 ), new Color( 0, 0, 0 )
    };

    private PointSet() {
        this.color = COLORS[clrCounter++ % COLORS.length];
    }

    public String getTitle() {
        return title;
    }

    public List<Point> getPoints() {
        return new ArrayList<>( points );
    }

    public Color getColor() {
        return color;
    }

    public static <X, Y> PointSet fromData( String title, List<X> xValues, ExtractDataStrategy<X> xStrategy, List<Y> yValues, ExtractDataStrategy<Y> yStrategy ) {
        if ( xValues.size() != yValues.size() ) {
            throw new IllegalArgumentException( "Incompatible lists:" + xValues.size() + " vs " + yValues.size() );
        }
        Builder b = new Builder();
        b.setTitle( title );
        for ( int i = 0; i < xValues.size(); i++ ) {
            b.addPoint( new Point(
                    xStrategy.extractData( xValues.get( i ) ),
                    yStrategy.extractData( yValues.get( i ) ) )
            );
        }
        return b.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "PointSet{" + "title=" + title + ", points=" + points + ", color=" + color + '}';
    }

    static void resetColors() {
        clrCounter = 0;
    }

    public static class Builder {

        private final PointSet valueSet;

        public Builder() {
            this.valueSet = new PointSet();
        }

        public Builder setTitle( String title ) {
            valueSet.title = title;
            return this;
        }

        public Builder setColor( Color color ) {
            valueSet.color = color;
            return this;
        }

        public Builder addPoint( Point p ) {
            valueSet.points.add( p );
            return this;
        }

        public PointSet build() {
            return valueSet;
        }

    }

}
