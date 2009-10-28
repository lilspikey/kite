package com.psychicorigami.physics;

import java.util.List;

public interface MultiBody<B extends Body<V>, V extends Vector<V>> {
    
    public List<B> getBodies();
    
    public List<Constraint> getConstraints();
    
}