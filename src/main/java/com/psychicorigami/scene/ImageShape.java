package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
    
    public BufferedImage getImage() {
        return image;
    }
    
    public void paintShape(Graphics2D g) {
        double width  = image.getWidth();
        double height = image.getHeight();
        
        AffineTransform tx = new AffineTransform();
        tx.scale(1,-1);
        tx.translate(-(width/2.0), -(height/2.0));
        
        g.drawRenderedImage(image, tx);
    }
    
    public boolean contains(int x, int y) {
        double width  = image.getWidth();
        double height = image.getHeight();
        
        AffineTransform tx = new AffineTransform();
        
        tx.translate(-getX(), -getY());
        tx.rotate(-(getAngle() - Math.PI/2));
        tx.scale(1,-1);
        tx.translate((width/2.0), (height/2.0));
        
        // transform point into same space as rectangle
        Point2D p = tx.transform(new Point2D.Double(x, y), null);
        
        
        return new Rectangle2D.Double(0, 0, width, height).contains(p);
    }
    
}
