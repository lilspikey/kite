package com.psychicorigami.physics;

public class StickConstraint<B extends Body<V>, V extends Vector<V>> extends DistanceConstraint<B,V> {
    
    public StickConstraint(B b1, B b2) {
        super(b1, b2);
    }
    
    public StickConstraint(B b1, B b2, double length) {
        super(b1, b2, length);
    }
    
    public double distanceError(double currentLength) {
        return currentLength - getLength();
    }
    
}