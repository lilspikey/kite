import java.util.List;
import java.util.ArrayList;


public abstract class Space<B extends Body<V>, V extends Vector<V>> {
    private static final double DAMPING = 0.01;
    private List<B> bodies = new ArrayList<B>();
    private List<Constraint> constraints = new ArrayList<Constraint>();
    private List<GlobalConstraint<B,V>> globalConstraints = new ArrayList<GlobalConstraint<B,V>>();
    
    private V gravity = null;
    
    public V getGravity() {
        return gravity;
    }
    
    public void setGravity(V gravity) {
        this.gravity = gravity;
    }
    
    public void add(B body) {
        bodies.add(body);
        for ( GlobalConstraint<B,V> gc: globalConstraints ) {
            gc.add(body);
        }
    }
    
    public void remove(B body) {
        bodies.remove(body);
        for ( GlobalConstraint<B,V> gc: globalConstraints ) {
            gc.remove(body);
        }
    }
    
    public Iterable<B> getBodies() {
        return bodies;
    }
    
    public void add(Constraint constraint) {
        constraints.add(constraint);
    }
    
    public void addGlobalConstraint(GlobalConstraint<B,V> constraint) {
        add(constraint);
        globalConstraints.add(constraint);
    }
    
    public double applyConstraints() {
        double error = 0;
        for ( Constraint c: constraints ) {
            error = Math.max(error, c.constrain());
        }
        return error;
    }
    
    public void add(Shape<B,V> shape) {
        for ( B body: shape.getBodies() ) {
            add(body);
        }
        
        for ( Constraint contraint: shape.getConstraints() ) {
            add(contraint);
        }
    }
    
    public void integrate(double dt) {
        for ( B b: bodies ) {
            if ( gravity != null && !b.isImmovable() ) {
                b.applyForce(gravity.divide(b.getMassInv()));
            }
            integrate(b, dt);
        }
    }
    
    /**
     * integrate using verlet method
     **/
    public void integrate(B body, double dt) {
        V forces = body.getForces();
        double massInv = body.getMassInv();
        
        V pos = body.getPos();
        V posPrev = body.getPosPrev();
        
        V a = forces.multiply(massInv);
        V next = pos.multiply(2-DAMPING).subtract(posPrev.multiply(1-DAMPING)).add( a.multiply(dt*dt) );
        
        body.updatePosition(next);
        body.zeroForces();
    }
    
    public void update(double dt) {
        integrate(dt);
        double error = 0;
        int count = 0;
        do {
            error = applyConstraints();
            count++;
        }
        while( count < 5 || (error > 0.25 && count < 200) );
    }
    
}