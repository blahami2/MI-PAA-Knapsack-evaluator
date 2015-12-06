package cz.blahami2.mipaa.knapsack.model.chart.view;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Blaha
 */
public class RectArea implements Paintable {

    private final int width;
    private final int height;
    private final List<PaintableMessenger> paintables = new ArrayList<>();

    public RectArea( int width, int height ) {
//        System.out.println( "Rect created: " + width + " x " + height );
        this.width = width;
        this.height = height;
    }

    public RectArea addPaintable( Paintable p, int x, int y ) {
        paintables.add( new PaintableMessenger( p, x, y ) );
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height, int x, int y ) {
//        System.out.println( "Translating: " + x + ", " + y );
        graphics.translate( x, y );
        for ( PaintableMessenger paintable : paintables ) {
//            System.out.println( "Painting to: [" + paintable.x + "," + paintable.y + "]" );
            paintable.p.paint( graphics, this.width, this.height, paintable.x, paintable.y );
        }
        graphics.translate( -x, -y );
    }

    private static class PaintableMessenger {

        Paintable p;
        int x;
        int y;

        public PaintableMessenger( Paintable p, int x, int y ) {
            this.p = p;
            this.x = x;
            this.y = y;
        }
    }

}
