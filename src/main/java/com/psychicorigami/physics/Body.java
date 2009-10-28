package com.psychicorigami.physics;

public abstract class Body<V extends Vector<V>> {
    private V pos = null;
    private V posPrev = null;
    
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