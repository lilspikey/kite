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

import com.psychicorigami.variable.Variable;
import com.psychicorigami.variable.ConstVariable;

public class ColoriseLayerStyle implements LayerStyle {
    private Variable<Float> opacity = new ConstVariable<Float>(0.5f);
    private Variable<Color> color   = new ConstVariable<Color>(new Color(0x000000));
    
    public void setOpacity(float opacity) {
        setOpacity(new ConstVariable<Float>(opacity));
    }
    
    public void setOpacity(Variable<Float> opacity) {
        this.opacity = opacity;
    }
    
    public float getOpacity() {
        return opacity.val();
    }
    
    public Color getColor() {
        return color.val();
    }
    
    public void setColor(Color color) {
        setColor(new ConstVariable<Color>(color));
    }
    
    public void setColor(Variable<Color> color) {
        this.color = color;
    }
    
    public void shapeAdded(Shape shape) {
        
    }
    
    public void shapeRemoved(Shape shape) {
        
    }
    
    public void preRender(Layer layer, BufferedImage image) {
        
    }
    
    public void postRender(Layer layer, BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, getOpacity()));
        g.setColor(getColor());
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        
        g.dispose();
    }
    
}