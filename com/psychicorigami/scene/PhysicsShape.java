package com.psychicorigami.scene;

import com.psychicorigami.physics.Vector2D;
import com.psychicorigami.physics.Body2D;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PhysicsShape extends ImageShape {
    private final Body2D b1;
    private final Body2D b2;
    
    public PhysicsShape(BufferedImage image, Body2D b1, Body2D b2) {
        super(image);
        this.b1 = b1;
        this.b2 = b2;
    }
    
    private Vector2D calcPosition(Body2D b1, Body2D b2) {
        Vector2D p1 = b1.getPos();
        Vector2D p2 = b2.getPos();
        return p1.add(p2).multiply(0.5);
    }

    private double calcAngle(Body2D b1, Body2D b2) {
        Vector2D p1 = b1.getPos();
        Vector2D p2 = b2.getPos();
        
        Vector2D angle = p1.subtract(p2).unit();
    
        return Math.atan2(angle.y, angle.x);
    }
    
    public void update() {
        Vector2D pos = calcPosition(b1, b2);
        double angle = calcAngle(b1, b2);
        setX(pos.x);
        setY(pos.y);
        setAngle(angle);
    }
    
}
