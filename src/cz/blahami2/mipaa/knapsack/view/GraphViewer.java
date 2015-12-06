package cz.blahami2.mipaa.knapsack.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author Michael Blaha
 */
public class GraphViewer extends JFrame {

    private BufferedImage graph = null;

    public GraphViewer() throws HeadlessException {
    }

    public void display() {
        setTitle( "graph viewer" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setPreferredSize( new Dimension( 800, 600 ) );
        setVisible( true );
        pack();
    }

    public void setGraph( BufferedImage graph ) {
        this.graph = graph;
        pack();
    }

    @Override
    public void paint( Graphics g ) {
        if ( graph == null ) {
            super.paint( g );
        } else {
            g.drawImage( graph, 0, 0, null );
        }
    }

}
