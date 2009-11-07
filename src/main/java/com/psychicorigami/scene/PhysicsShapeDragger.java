package com.psychicorigami.scene;

import com.psychicorigami.physics.Vector2D;
import com.psychicorigami.physics.Body2D;
import com.psychicorigami.physics.Constraint;


public class PhysicsShapeDragger implements Constraint {
    private PhysicsShape shape = null;
    private Vector2D d1, d2;
    private Vector2D mousePos  = null;
    
    public void dragShape(PhysicsShape shape, Vector2D mousePos) {
        this.shape    = shape;
        this.mousePos = mousePos;
        this.d1 = shape.getBody1().getPos().subtract(mousePos);
        this.d2 = shape.getBody2().getPos().subtract(mousePos);
    }
    
    public void stopDragging() {
        this.shape    = null;
        this.mousePos = null;
    }
    
    public void updateMousePos(Vector2D pos) {
        this.mousePos = pos;
    }
    
    public double constrain() {
        if ( shape != null && mousePos != null ) {
            shape.getBody1().setPos(mousePos.add(d1));
            shape.getBody2().setPos(mousePos.add(d2));
        }
        return 0;
    }
    
}