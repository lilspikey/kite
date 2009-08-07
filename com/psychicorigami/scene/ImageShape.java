package com.psychicorigami.scene;

import com.psychicorigami.physics.Body2D;
import com.psychicorigami.physics.Vector2D;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageShape extends AbstractShape {
    private BufferedImage image;
    
    public ImageShape(BufferedImage image, Body2D b1, Body2D b2) {
        super(b1, b2);
        this.image = image;
    }
    
    public void paintShape(Graphics2D g) {
        double width  = image.getWidth();
        double height = image.getHeight();
        g.drawRenderedImage(image, AffineTransform.getTranslateInstance(-(width/2.0), -(height/2.0)));
    }
    
}
