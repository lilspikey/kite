package com.psychicorigami.scene;

import java.awt.image.BufferedImage;

public interface LayerStyle {
    
    public BufferedImage apply(Layer layer, BufferedImage image);
    
}