package com.psychicorigami.physics;

import com.psychicorigami.variable.Variable;
import static com.psychicorigami.variable.ConstVariable.const_var;

import java.util.List;
import java.util.ArrayList;

public class GravityForce<B extends Body<V>, V extends Vector<V>> implements Force {
    private List<B> bodies = new ArrayList<B>();
    private Variable<V> gravity;
    
    public GravityForce(V gravity) {
        this(const_var(gravity));
    }
    
    public GravityForce(Variable<V> gravity) {
        this.gravity = gravity;
    }
    
    public void add(B body) {
        bodies.add(body);
    }
    
    public void add(MultiBody<B,V> multiBody) {
        for ( B body: multiBody.getBodies() ) {
            add(body);
        }
    }
    
    public void update(double dt) {
        for ( B b: bodies ) {
            if ( !b.isImmovable() ) {
                b.applyForce(gravity.val().divide(b.getMassInv()));
            }
        }
    }

}