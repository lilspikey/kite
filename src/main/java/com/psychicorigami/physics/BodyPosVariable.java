package com.psychicorigami.physics;

import com.psychicorigami.variable.Variable;

import java.util.List;
import java.util.Arrays;

public class BodyPosVariable<B extends Body<V>, V extends Vector<V>> implements Variable<V> {
    private final B body;
    
    public BodyPosVariable(B body) {
        this.body = body;
    }
    
    public V val() {
        return body.getPos();
    }
    
}