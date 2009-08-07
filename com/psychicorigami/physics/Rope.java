package com.psychicorigami.physics;

import java.util.List;
import java.util.ArrayList;

public abstract class Rope<B extends Body<V>, V extends Vector<V>> implements Shape<B,V> {
    private List<B> bodies = new ArrayList<B>();
    private List<StickConstraint<B,V>> constraints = new ArrayList<StickConstraint<B,V>>();
    
    public Rope(V start, V end, int numBodies, double bodyMass) {
        B prev = null;
        for ( int i = 0; i < numBodies; i++ ) {
            double dx = i/(double)(numBodies-1);
            V pos = start.multiply(1.0-dx).add( end.multiply(dx) );
            B body = createBody(pos, bodyMass);
            bodies.add(body);
            if ( prev != null ) {
                constraints.add(new StickConstraint<B,V>(prev, body));
            }
            prev = body;
        }
    }
    
    public abstract B createBody(V pos, double mass);
    
    public B getStart() {
        return bodies.get(0);
    }
    
    public B getEnd() {
        return bodies.get(bodies.size()-1);
    }
    
    public List<B> getBodies() {
        return bodies;
    }
    
    public double calcLength() {
        double length = 0;
        for ( StickConstraint<B,V> constraint: constraints ) {
            length += constraint.getLength();
        }
        return length;
    }
    
    public void changeLength(double length) {
        double constraintLength = length/constraints.size();
        for ( StickConstraint<B,V> constraint: constraints ) {
            constraint.setLength(constraintLength);
        }
    }
    
    public List<Constraint> getConstraints() {
        return new ArrayList<Constraint>(constraints);
    }
    
}