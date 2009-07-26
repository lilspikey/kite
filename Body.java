
public abstract class Body<V extends Vector<V>> {
    private static final double DRAG = 0.005;
    private V pos = null;
    private V posPrev = null;
    
    private V forces = null;
    private double massInv = 1.0;
    
    public Body(V pos, V posPrev) {
        this.pos = pos;
        this.posPrev = posPrev;
        
        forces = pos.zero();
    }
    
    /**
     * integrate using verlet method
     **/
    public void integrate(double dt) {
        V a = forces.multiply(massInv);
        V next = pos.multiply(2-DRAG).subtract(posPrev.multiply(1-DRAG)).add( a.multiply(dt*dt) );
        
        // update positions and zero forces
        posPrev = pos;
        pos = next;
        forces = forces.zero();
    }
    
    public void applyForce(V f) {
        forces = forces.add(f);
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
    
    public void setVelocity(V v) {
        posPrev = pos.subtract(v);
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