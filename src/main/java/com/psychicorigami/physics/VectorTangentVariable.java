package com.psychicorigami.physics;

import com.psychicorigami.variable.Variable;

import java.util.List;
import java.util.Arrays;

public class VectorTangentVariable<V extends Vector<V>> implements Variable<V> {
    private Variable<V> v1;
    private Variable<V> v2;
    
    public VectorTangentVariable(Variable<V> v1, Variable<V> v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
    
    public<B extends Body<V>> VectorTangentVariable(B b1, B b2) {
        this(new BodyPosVariable<B,V>(b1), new BodyPosVariable<B,V>(b2));
    }
    
    public V val() {
        return v1.val().subtract( v2.val() ).unit();
    }
    
}