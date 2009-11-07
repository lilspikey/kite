package com.psychicorigami.physics;

/**
 * Constraint to control the distance between two bodies
 **/
public abstract class DistanceConstraint<B extends Body<V>, V extends Vector<V>> implements Constraint {
    private final B b1;
    private final B b2;
    private double length;
    
    public DistanceConstraint(B b1, B b2) {
        this(b1, b2, b1.getPos().subtract(b2.getPos()).length());
    }
    
    public DistanceConstraint(B b1, B b2, double length) {
        this.b1 = b1;
        this.b2 = b2;
        this.length = length;
    }
    
    public double getLength() {
        return length;
    }
    
    public void setLength(double length) {
        this.length = length;
    }
    
    public abstract double distanceError(double currentLength);
    
    public double constrain() {
        V p1 = b1.getPos(),
          p2 = b2.getPos();
        double invM1 = b1.getMassInv(),
               invM2 = b2.getMassInv();
        
        V delta = p2.subtract(p1);
        double deltaLength = delta.length();
        double error = distanceError(deltaLength);
                
        double diff = error/(deltaLength*(invM1+invM2));
        
        p1 = delta.multiply_add(  invM1*diff, p1);
        p2 = delta.multiply_add( -invM2*diff, p2);
        
        b1.addConstrainedPosition(p1);
        b2.addConstrainedPosition(p2);
        
        return error;
    }
    
}