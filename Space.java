import java.util.List;
import java.util.ArrayList;


public abstract class Space<B extends Body<V>, V extends Vector<V>> {
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
    
    public void applyConstraints() {
        for ( Constraint c: constraints ) {
            c.constrain();
        }
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
            b.integrate(dt);
        }
    }
    
    public void update(double dt) {
        integrate(dt);
        for ( int i = 0; i < 10; i++ ) {
            applyConstraints();
        }
    }
    
}