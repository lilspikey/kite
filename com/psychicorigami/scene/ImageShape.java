package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageShape extends AbstractShape {
    private BufferedImage image;
    
    public ImageShape(BufferedImage image) {
        this(image, 0, 0);
    }
    
    public ImageShape(BufferedImage image, double x, double y) {
        this(image, x, y, 0);
    }
    
    public ImageShape(BufferedImage image, double x, double y, double angle) {
        super(x, y, angle);
        this.image = image;
    }
    
    public void paintShape(Graphics2D g) {
        double width  = image.getWidth();
        double height = image.getHeight();
        
        AffineTransform tx = new AffineTransform();
        tx.scale(1,-1);
        tx.translate(-(width/2.0), -(height/2.0));
        
        g.drawRenderedImage(image, tx);
    }
    
}
