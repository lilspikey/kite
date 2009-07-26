
public abstract class Body<V extends Vector<V>> {
    private V pos = null;
    private V posPrev = null;
    
    private V forces = null;
    private double mass = 1.0;
    
    public Body(V pos, V posPrev) {
        this.pos = pos;
        this.posPrev = posPrev;
        
        forces = pos.zero();
    }
    
    /**
     * integrate using verlet method
     **/
    public void integrate(double dt) {
        V a = forces.divide(mass);
        V next = pos.multiply(2).subtract(posPrev).add( a.multiply(dt*dt) );
        
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
    
    public double getMass() {
        return mass;
    }
    
    public void setMass(double mass) {
        this.mass = mass;
    }
    
}