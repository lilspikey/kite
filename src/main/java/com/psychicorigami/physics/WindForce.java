package com.psychicorigami.physics;

import com.psychicorigami.variable.Variable;
import static com.psychicorigami.variable.ConstVariable.const_var;

import java.util.List;
import java.util.Arrays;

public class WindForce<B extends Body<V>, V extends Vector<V>> implements Force {
    private Variable<Double> AIR_RESISTANCE = const_var(40.0);
    private Variable<V> windForce;
    private Variable<V> surfaceNormal;
    private final B centreOfPressure;
    
    public WindForce(B centreOfPressure, Variable<V> surfaceNormal, Variable<V> windForce) {
        this.centreOfPressure = centreOfPressure;
        this.surfaceNormal    = surfaceNormal;
        this.windForce        = windForce;
    }
    
    public void update(double dt) {
        V surfaceNormal = this.surfaceNormal.val();
        V windForce     = this.windForce.val();
        
        V airSpeed = centreOfPressure.getVelocity(dt);
        
        airSpeed = airSpeed.add(windForce);
        
        V force = surfaceNormal.multiply(surfaceNormal.dotProduct(airSpeed)).multiply(AIR_RESISTANCE.val());
        
        centreOfPressure.applyForce(force);
    }

}