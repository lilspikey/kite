package com.psychicorigami.scene;

import java.awt.image.BufferedImage;

public interface LayerStyle extends LayerListener {
    
    public void preRender(Layer layer, BufferedImage image);
    
    public void postRender(Layer layer, BufferedImage image);
    
}