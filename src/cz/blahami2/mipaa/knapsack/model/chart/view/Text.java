package cz.blahami2.mipaa.knapsack.model.chart.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Michael Blaha
 */
public class Text implements Paintable {

    private Font font = new Font( "Calibri", Font.PLAIN, 9 );
    private String string;
    private int margin;
    private Color color;
    private Orientation orientation = Orientation.HORIZONTAL;

    public static final int CENTER = -1;
    public static final int LEFT = -2;
    public static final int RIGHT = -3;
    public static final int BOTTOM = -4;
    public static final int TOP = -5;

    public Text( String string ) {
        this.string = string;
    }

    public Text setColor( Color color ) {
        this.color = color;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public Text setFont( Font font ) {
        this.font = font;
        return this;
    }

    public String getString() {
        return string;
    }

    public Text setString( String string ) {
        this.string = string;
        return this;
    }

    public int getMargin() {
        return margin;
    }

    public Text setMargin( int margin ) {
        this.margin = margin;
        return this;
    }

    public Text setOrientation( Orientation orientation ) {
        this.orientation = orientation;
        return this;
    }

    public Text setFontSize( int size ) {
        font = new Font( font.getFontName(), font.getStyle(), size );
        return this;
    }

    public int getWidth( Graphics graphics ) {
        return graphics.getFontMetrics( font ).stringWidth( string );
    }

    public int getHeight( Graphics graphics ) {
        return graphics.getFontMetrics( font ).getHeight();
    }

    @Override
    public void paint( Graphics2D g, int width, int height, int x, int y ) {
        if ( x == BOTTOM || x == TOP || y == LEFT || y == RIGHT ) {
            throw new IllegalArgumentException( "x cannot be TOP or Bottom, y cannot be LEFT or RIGHT" );
        }
        AffineTransform transform = g.getTransform();
        g.setFont( font );
        int strWidth = getWidth( g );
        int strHeight = getHeight( g );
        if ( orientation.equals( Orientation.BOTTOM_UP ) ) {
            int fix = ( width - height ) / 2;
            AffineTransform at = new AffineTransform();
            at.setToRotation( -Math.PI / 2, width / 2.0, height / 2.0 );
            g.setTransform( at );
            int origY = y;
            switch ( x ) {
                case CENTER:
                    y = ( width - strHeight ) / 2;
                    break;
                case LEFT:
                    y = margin + strHeight - fix;
                    break;
                case RIGHT:
                    y = height - margin + fix;
                    break;
                default:
                    y = x - fix;
            }
            switch ( origY ) {
                case CENTER:
                    x = ( width - strWidth ) / 2;
                    break;
                case TOP:
                    x = margin + fix;
                    break;
                case BOTTOM:
                    x = width - strWidth - margin - fix;
                    break;
                default:
                    x = origY + fix;
            }
        } else if ( orientation.equals( Orientation.TOP_DOWN ) ) {
            throw new UnsupportedOperationException();
        } else {
            switch ( x ) {
                case CENTER:
                    x = ( width - strWidth ) / 2;
                    break;
                case LEFT:
                    x = margin;
                    break;
                case RIGHT:
                    x = width - strWidth - margin;
                    break;
            }
            switch ( y ) {
                case CENTER:
                    y = ( height - strHeight ) / 2;
                    break;
                case TOP:
                    y = margin + strHeight;
                    break;
                case BOTTOM:
                    y = height - margin;
                    break;
            }
        }
//        System.out.println( "Drawing: '" + string + "' to [" + x + "," + y + "]" );
        Color c = g.getColor();
        if ( color != null ) {
            g.setColor( color );
        }
        g.drawString( string, x, y );
        g.setColor( c );
        g.setTransform( transform );
    }

    public enum Orientation {

        HORIZONTAL, TOP_DOWN, BOTTOM_UP;
    }

}
