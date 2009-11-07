package com.psychicorigami.physics;

import java.util.List;
import java.util.ArrayList;

public abstract class Body<V extends Vector<V>> {
    private V pos = null;
    private V posPrev = null;
    private final List<V> constrainedPositions = new ArrayList<V>();
    
    private V forces = null;
    private double massInv = 1.0;
    
    public Body(V pos, V posPrev) {
        this.pos = pos;
        this.posPrev = posPrev;
        
        forces = pos.zero();
    }
    
    public void applyForce(V f) {
        forces = forces.add(f);
    }
    
    public void zeroForces() {
        forces = forces.zero();
    }
    
    public V getForces() {
        return forces;
    }
    
    public V getPos() {
        return pos;
    }
    
    public void setPos(V pos) {
        this.pos = pos;
    }
    
    public void updatePositionFromConstraints() {
        int count = constrainedPositions.size();
        V sum = null;
        for ( V pos: constrainedPositions ) {
            if ( sum == null ) {
                sum = pos;
            }
            else {
                sum = sum.add(pos);
            }
        }
        if ( sum != null ) {
            this.pos = sum.divide(count);
        }
        constrainedPositions.clear();
    }
    
    public void addConstrainedPosition(V pos) {
        constrainedPositions.add(pos);
    }
    
    public V getPosPrev() {
        return posPrev;
    }
    
    public void setPosPrev(V pos) {
        this.posPrev = pos;
    }
    
    public void updatePosition(V pos) {
        this.posPrev = this.pos;
        this.pos = pos;
    }
    
    public void setVelocity(V v) {
        posPrev = pos.subtract(v);
    }
    
    public V getVelocity() {
        return pos.subtract(posPrev);
    }
    
    public double getMassInv() {
        return massInv;
    }
    
    public void setMass(double mass) {
        this.massInv = 1.0/mass;
    }
    
    public void setImmovable() {
        massInv = 0;
    }
    
    public boolean isImmovable() {
        return massInv == 0;
    }
    
}