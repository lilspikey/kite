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
import static com.psychicorigami.variable.ConstVariable.const_var;

public class ColoriseLayerStyle implements LayerStyle {
    private Variable<Float> opacity = const_var(0.5f);
    private Variable<Color> color   = const_var(new Color(0x000000));
    
    public void setOpacity(float opacity) {
        setOpacity(const_var(opacity));
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
        setColor(const_var(color));
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