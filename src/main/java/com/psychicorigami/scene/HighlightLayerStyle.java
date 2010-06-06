package com.psychicorigami.scene;

import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

public class HighlightLayerStyle implements LayerStyle {
    private Shape highlighted = null;
    
    public void setHighlighted(Shape highlighted) {
        this.highlighted = highlighted;
    }
    
    public void preRender(Layer layer, BufferedImage image) {
        
    }
    
    public void postRender(Layer layer, BufferedImage image) {
        if ( highlighted != null && layer.contains(highlighted) ) {
            Graphics2D g = image.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.XOR, 0.5f));
            highlighted.paint(g);
            g.dispose();
        }
    }
    
    public void shapeAdded(Shape shape) {}
    
    public void shapeRemoved(Shape shape) {}
    
}