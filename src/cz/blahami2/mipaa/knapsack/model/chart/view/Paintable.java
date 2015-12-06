package cz.blahami2.mipaa.knapsack.model.chart.view;

import java.awt.Graphics2D;

/**
 *
 * @author Michael Blaha
 */
public interface Paintable {
    
    public void paint(Graphics2D graphics, int width, int height, int x, int y);
    
}
