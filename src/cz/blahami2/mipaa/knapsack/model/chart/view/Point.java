package cz.blahami2.mipaa.knapsack.model.chart.view;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Michael Blaha
 */
public class Point implements Paintable {

    private Color color;
    private int size = 6;

    public Point( Color color ) {
        this.color = color;
    }

    public Point setColor( Color color ) {
        this.color = color;
        return this;
    }

    public Point setSize( int size ) {
        this.size = size;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height, int x, int y ) {
        Color c = graphics.getColor();
        graphics.setColor( color );
//        System.out.println( "Painting point: color[" + color.toString() + "] to [" + x + "," + y + "]" );
        graphics.fillOval( x - (size / 2), y - (size / 2), size, size );
        graphics.setColor( c );
    }

}
