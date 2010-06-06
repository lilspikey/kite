package com.psychicorigami.physics;

import java.util.List;
import java.util.ArrayList;

public abstract class Body<V extends Vector<V>> {
    private V pos = null;
    private V posPrev = null;
    private V velocity = null;
    private V constrainedPositionSum = null;
    private int constrainedPositionCount = 0;
    
    private V forces = null;
    private double massInv = 1.0;
    
    public Body(V pos, V posPrev) {
        this.pos = pos;
        this.posPrev = posPrev;
        
        velocity = pos.zero();
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
        if ( constrainedPositionSum != null ) {
            this.pos = constrainedPositionSum.divide(constrainedPositionCount);
        }
        constrainedPositionSum = null;
        constrainedPositionCount = 0;
    }
    
    public void addConstrainedPosition(V pos) {
        if ( constrainedPositionSum == null ) {
            constrainedPositionSum = pos;
        }
        else {
            constrainedPositionSum = constrainedPositionSum.add(pos);
        }
        constrainedPositionCount++;
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
    
    public void updateVelocity(double dt) {
        this.velocity = pos.subtract(posPrev).divide(dt);
    }
    
    public V getVelocity() {
        return velocity;
    }
    
    public double getMassInv() {
        return massInv;
    }
    
    public double getMass() {
        return 1.0/massInv;
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