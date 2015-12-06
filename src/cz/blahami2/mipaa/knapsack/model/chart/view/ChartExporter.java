package cz.blahami2.mipaa.knapsack.model.chart.view;

import cz.blahami2.mipaa.knapsack.model.chart.Chart;
import cz.blahami2.mipaa.knapsack.model.chart.PointSet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Michael Blaha
 */
public class ChartExporter {

    private static final int TITLE_MARGIN = 10;
    private static final int TITLE_FONT_SIZE = 20;
    private static final Color TITLE_COLOR = new Color( 0, 0, 0 );

    private static final int AXIS_MARGIN = 5;
    private static final int AXIS_FONT_SIZE = 14;
    private static final Color AXIS_COLOR = new Color( 155, 155, 155 );

    private static final int CHART_MARGIN = 20;

    private static final int LEGEND_MARGIN = 5;

    private static final Color GRID_COLOR = new Color( 217, 217, 217 );

    public static void export( Chart chart, int width, int height, File outputFile ) throws IOException {
        ImageIO.write( createImage( chart, width, height ), "png", outputFile );
    }

    public static BufferedImage createImage( Chart chart, int width, int height ) {
        Color c;
        chart.adjustLimitsToPoints();
        BufferedImage surface = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        Graphics2D graphics = (Graphics2D) surface.getGraphics();
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB );
        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        graphics.setBackground( Color.WHITE );
        graphics.setColor( Color.WHITE );
        graphics.fillRect( 0, 0, width, height );
        graphics.setColor( Color.BLACK );
        Text titleText = new Text( chart.getTitle() ).setFontSize( TITLE_FONT_SIZE ).setMargin( TITLE_MARGIN ).setColor( TITLE_COLOR );
        titleText.paint( graphics, width, height, Text.CENTER, Text.TOP );
//        ;
//        new Text( chart.getYAxis().getLabel() ).setMargin( AXIS_MARGIN ).setFontSize( AXIS_FONT_SIZE ).setOrientation( Text.Orientation.BOTTOM_UP ).paint( graphics, width, height, Text.LEFT, Text.CENTER );

        int rectAreaLeft = 0;
        int rectAreaRight = width;
        int rectAreaTop = titleText.getHeight( graphics ) + 2 * titleText.getMargin();
        int rectAreaBottom = height;
//        System.out.println( "Rect: left = " + rectAreaLeft + ", right = " + rectAreaRight + ", top = " + rectAreaTop + ", bottom = " + rectAreaBottom );
        RectArea r = new RectArea( rectAreaRight - rectAreaLeft, rectAreaBottom - rectAreaTop );
        Text axisText;
        r.addPaintable( axisText = new Text( chart.getXAxis().getLabel() ).setMargin( AXIS_MARGIN + 5 ).setFontSize( AXIS_FONT_SIZE ), Text.CENTER, Text.BOTTOM );
        r.addPaintable( new Text( chart.getYAxis().getLabel() ).setMargin( AXIS_MARGIN ).setFontSize( AXIS_FONT_SIZE ).setOrientation( Text.Orientation.BOTTOM_UP ), Text.LEFT, Text.CENTER );

        /* legend */
        int legendWidth = 0;
        int legendHeight = 0;
        for ( PointSet pointSet : chart.getPointSets() ) {
            Text pointSetText = new Text( pointSet.getTitle() ).setFontSize( AXIS_FONT_SIZE );
            Point pointSetPoint = new Point( pointSet.getColor() );
            legendWidth = Math.max( legendWidth, pointSetPoint.getSize() + LEGEND_MARGIN + pointSetText.getWidth( graphics ) );
            legendHeight += pointSetText.getHeight( graphics ) + LEGEND_MARGIN;
        }
        int legendTop = rectAreaTop + ( r.getHeight() - legendHeight ) / 2;
        int legendLeft = rectAreaRight - CHART_MARGIN - legendWidth;
        int currentLegendY = legendTop;
        for ( PointSet pointSet : chart.getPointSets() ) {
            Text pointSetText = new Text( pointSet.getTitle() ).setFontSize( AXIS_FONT_SIZE );
            Point pointSetPoint = new Point( pointSet.getColor() );
            pointSetPoint.paint( graphics, width, height, legendLeft + pointSetPoint.getSize() / 2, currentLegendY + pointSetText.getHeight( graphics ) - pointSetPoint.getSize() / 2 );
            pointSetText.paint( graphics, width, height, legendLeft + LEGEND_MARGIN + pointSetPoint.getSize(), currentLegendY + pointSetText.getHeight( graphics ) );
            currentLegendY += pointSetText.getHeight( graphics ) + LEGEND_MARGIN;
        }

        Text yAxisNumberText = new Text( chart.getYAxis().valueToString( chart.getYAxis().getMax() ) ).setFontSize( AXIS_FONT_SIZE );
        int chartAreaLeft = axisText.getHeight( graphics ) + 2 * AXIS_MARGIN + yAxisNumberText.getWidth( graphics ) + 2 * AXIS_MARGIN;
        int chartAreaRight = legendLeft - CHART_MARGIN;
        int chartAreaTop = 0;
        int chartAreaBottom = r.getHeight() - axisText.getHeight( graphics ) - 2 * AXIS_MARGIN - yAxisNumberText.getHeight( graphics ) - 2 * AXIS_MARGIN;
//        System.out.println( "Chart: left = " + chartAreaLeft + ", right = " + chartAreaRight + ", top = " + chartAreaTop + ", bottom = " + chartAreaBottom );
        RectArea chartArea = new RectArea( chartAreaRight - chartAreaLeft, chartAreaBottom - chartAreaTop );
        r.addPaintable( chartArea, chartAreaLeft, chartAreaTop );
        double xRatio = getChartRatio( chart.getXAxis().getMin(), chart.getXAxis().getMax(), chart.getXAxis().getStep(), chartArea.getWidth() );
        double yRatio = getChartRatio( chart.getYAxis().getMin(), chart.getYAxis().getMax(), chart.getYAxis().getStep(), chartArea.getHeight() );
        chart.getPointSets().stream().forEach( (pointSet) -> {
            if ( pointSet.getPoints().isEmpty() ) {
                throw new IllegalArgumentException( "PointSet '" + pointSet.getTitle() + "' is empty." );
            }
//            System.out.println( "Painting pointset: " + pointSet.getTitle() );
            pointSet.getPoints().stream().forEach( (point) -> {
//                System.out.println( "painting point:[" + point.getX() + "," + point.getY() + "] => ["
//                                    + getChartValue( xRatio, point.getX() ) + "," + ( chartArea.getHeight() - getChartValue( yRatio, point.getY() ) ) + "]" );
                chartArea.addPaintable( new Point( pointSet.getColor() ),
                                        getChartValue( xRatio, point.getX() ),
                                        chartArea.getHeight() - getChartValue( yRatio, point.getY() ) );
            } );
        } );

//        System.out.println( "Xaxis = " + chart.getXAxis() );
//        System.out.println( "Yaxis = " + chart.getYAxis() );

        /* axis measuring */
        int yAxisLabelRight = rectAreaLeft + chartAreaLeft - AXIS_MARGIN;
        int yAxisYBottom = rectAreaTop + chartAreaBottom;
        int yAxisYTop = rectAreaTop + chartAreaBottom;
        for ( int i = yAxisYBottom, step = 0; i >= rectAreaTop + chartAreaTop; step += chart.getYAxis().getStep(), i = yAxisYBottom - getChartValue( yRatio, step ) ) {
            yAxisYTop = i;
        }
//        System.out.println( "Y: top = " + yAxisYTop + ", bottom = " + yAxisYBottom );
        int xAxisLabelTop = rectAreaTop + chartAreaBottom + AXIS_MARGIN;
        int xAxisXLeft = rectAreaLeft + chartAreaLeft;
        int xAxisXRight = rectAreaLeft + chartAreaLeft;
        for ( int i = xAxisXLeft, step = 0; i <= rectAreaLeft + chartAreaRight; step += chart.getXAxis().getStep(), i = xAxisXLeft + getChartValue( xRatio, step ) ) {
            xAxisXRight = i;
        }
//        System.out.println( "X: left = " + xAxisXLeft + ", right = " + xAxisXRight );

        /* Y axis */
        for ( int i = yAxisYBottom, step = 0; i >= rectAreaTop + chartAreaTop; step += chart.getYAxis().getStep(), i = yAxisYBottom - getChartValue( yRatio, step ) ) {
            c = graphics.getColor();
            graphics.setColor( GRID_COLOR );
            graphics.drawLine( xAxisXLeft, i, xAxisXRight, i );
            graphics.setColor( c );
            Text lblText = new Text( chart.getYAxis().valueToString( step ) ).setFontSize( AXIS_FONT_SIZE );
            lblText.paint( graphics, width, height, yAxisLabelRight - lblText.getWidth( graphics ), i + lblText.getHeight( graphics ) / 2 );
        }
        /* X axis */
        for ( int i = xAxisXLeft, step = 0; i <= rectAreaLeft + chartAreaRight; step += chart.getXAxis().getStep(), i = xAxisXLeft + getChartValue( xRatio, step ) ) {
            c = graphics.getColor();
            graphics.setColor( GRID_COLOR );
            graphics.drawLine( i, yAxisYBottom, i, yAxisYTop );
            graphics.setColor( c );
            Text lblText = new Text( chart.getXAxis().valueToString( step ) ).setFontSize( AXIS_FONT_SIZE );
            lblText.paint( graphics, width, height, i - lblText.getWidth( graphics ) / 2, xAxisLabelTop + lblText.getHeight( graphics ) );
        }
        c = graphics.getColor();
        graphics.setColor( AXIS_COLOR );
        graphics.drawLine( xAxisXLeft, yAxisYBottom, xAxisXRight, yAxisYBottom );
        graphics.drawLine( xAxisXLeft, yAxisYTop, xAxisXLeft, yAxisYBottom );
        graphics.setColor( c );

        r.paint( graphics, width, height, rectAreaLeft, rectAreaTop );
        return surface;
    }

    private static double getChartRatio( int min, int max, int step, int length ) {
        if ( min % step != 0 ) {
            min -= step - min % step;
        }
        while ( max % step != 0 ) {
            max += step - max % step;
        }
        int range = max - min;
        return (double) length / range;
    }

    private static int getChartValue( double ratio, int value ) {
        return (int) ( value * ratio );
    }

    private static int getChartValue( int min, int max, int step, int length, int value ) {
        double ratio = getChartRatio( min, max, step, length );
        return (int) ( value * ratio );
    }

}
