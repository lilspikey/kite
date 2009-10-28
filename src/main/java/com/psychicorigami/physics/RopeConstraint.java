package com.psychicorigami.physics;

public class RopeConstraint<B extends Body<V>, V extends Vector<V>> extends DistanceConstraint<B,V> {
    
    public RopeConstraint(B b1, B b2) {
        super(b1, b2);
    }
    
    public RopeConstraint(B b1, B b2, double length) {
        super(b1, b2, length);
    }
    
    public double distanceError(double currentLength) {
        double error = (currentLength - getLength());
        if ( error > 0 ) {
            return 0.1*error;
        }
        return 0;//error*0.001;
    }
    
}