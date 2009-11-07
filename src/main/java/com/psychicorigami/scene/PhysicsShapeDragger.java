package com.psychicorigami.scene;

import com.psychicorigami.physics.Vector2D;
import com.psychicorigami.physics.Body2D;
import com.psychicorigami.physics.MultiBody;
import com.psychicorigami.physics.Constraint;

import java.util.List;
import java.util.ArrayList;

public class PhysicsShapeDragger implements Constraint {
    private MultiBody<Body2D, Vector2D> body = null;
    private List<Vector2D> distances = null;
    private Vector2D mousePos  = null;
    
    public void dragShape(MultiBody<Body2D, Vector2D> body, Vector2D mousePos) {
        if ( body == null ) {
            stopDragging();
            return;
        }
        
        this.body     = body;
        this.mousePos = mousePos;
        this.distances = new ArrayList<Vector2D>();
        for ( Body2D b: body.getBodies() ) {
            Vector2D d = b.getPos().subtract(mousePos);
            this.distances.add(d);
        }
    }
    
    public void stopDragging() {
        this.body    = null;
        this.mousePos = null;
    }
    
    public void updateMousePos(Vector2D pos) {
        this.mousePos = pos;
    }
    
    private void moveBody(Body2D body, Vector2D d) {
        Vector2D target = mousePos.add(d);
        body.addConstrainedPosition(target);
    }
    
    public double constrain() {
        if ( body != null && mousePos != null ) {
            List<Body2D> bodies = body.getBodies();
            for ( int i = 0; i < bodies.size(); i++ ) {
                moveBody(bodies.get(i), distances.get(i));
            }
        }
        return 0;
    }
    
}