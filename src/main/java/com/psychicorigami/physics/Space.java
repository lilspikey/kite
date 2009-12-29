package com.psychicorigami.physics;

import java.util.List;
import java.util.ArrayList;

import com.psychicorigami.variable.Variable;
import static com.psychicorigami.variable.ConstVariable.const_var;

public abstract class Space<B extends Body<V>, V extends Vector<V>> {
    private Variable<Double> damping = const_var(0.005);
    private List<B> bodies = new ArrayList<B>();
    private List<Constraint> constraints = new ArrayList<Constraint>();
    private List<GlobalConstraint<B,V>> globalConstraints = new ArrayList<GlobalConstraint<B,V>>();
    private List<Force> forces = new ArrayList<Force>();
    
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
    
    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }
    
    public void addGlobalConstraint(GlobalConstraint<B,V> constraint) {
        addConstraint(constraint);
        globalConstraints.add(constraint);
    }
    
    public void addForce(Force force) {
        forces.add(force);
    }
    
    public double applyConstraints() {
        double error = 0;
        for ( Constraint c: constraints ) {
            double cerror = c.constrain();
            if ( cerror > error ) {
                error = cerror;
            }
        }
        
        for ( B body: bodies ) {
            body.updatePositionFromConstraints();
        }
        
        return error;
    }
    
    public void add(MultiBody<B,V> multiBody) {
        for ( B body: multiBody.getBodies() ) {
            add(body);
        }
        
        for ( Constraint contraint: multiBody.getConstraints() ) {
            addConstraint(contraint);
        }
    }
    
    public void updateForces(double dt) {
        for ( Force force: forces ) {
            force.update(dt);
        }
    }
    
    public void integrate(double dt) {
        for ( B b: bodies ) {
            integrate(b, dt);
        }
    }
    
    /**
     * integrate using verlet method
     **/
    public void integrate(B body, double dt) {
        double damping = this.damping.val();
        
        V forces = body.getForces();
        double massInv = body.getMassInv();
        
        V pos = body.getPos();
        V posPrev = body.getPosPrev();
        
        V a = forces.multiply(massInv);
        V next = pos.multiply(2-damping).subtract(posPrev.multiply(1-damping)).add( a.multiply(dt*dt) );
        
        body.updatePosition(next);
        body.zeroForces();
    }
    
    public void update(double dt) {
        updateForces(dt);
        integrate(dt);
        double error = 0;
        int count = 0;
        do {
            error = applyConstraints();
            count++;
        }
        while( count < 10 || (error > 1 && count < 100) );
    }
    
}