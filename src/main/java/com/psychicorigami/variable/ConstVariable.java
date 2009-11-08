package com.psychicorigami.variable;

public class ConstVariable<T> implements Variable<T> {
    private final T val;
    
    public ConstVariable(T val) {
        this.val = val;
    }
    
    public T val() {
        return val;
    }
    
}