package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferInt;

import java.util.Map;
import java.util.LinkedHashMap;

public class ColoriseLayerStyle implements LayerStyle {
    private float opacity = 0.25f;
    private Color color = new Color(0x990000);
        
    public void shapeAdded(Shape shape) {
        
    }
    
    public void shapeRemoved(Shape shape) {
        
    }
    
    public void preRender(Layer layer, BufferedImage image) {
        
    }
    
    public void postRender(Layer layer, BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        
        g.dispose();
    }
    
}