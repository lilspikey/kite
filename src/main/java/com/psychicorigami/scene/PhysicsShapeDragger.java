package com.psychicorigami.scene;

import com.psychicorigami.physics.Vector2D;
import com.psychicorigami.physics.Body2D;
import com.psychicorigami.physics.MultiBody;
import com.psychicorigami.physics.Force;

import java.util.List;
import java.util.ArrayList;

public class PhysicsShapeDragger implements Force {
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
    
    public boolean isDragging() {
        return this.body != null && this.mousePos != null;
    }
    
    public void updateMousePos(Vector2D pos) {
        this.mousePos = pos;
    }
    
    private void applyForceToBody(Body2D body, Vector2D d) {
        if ( !body.isImmovable() ) {
            Vector2D target = mousePos.add(d);
            Vector2D diff = target.subtract(body.getPos());
            double length = diff.length();
            if ( length > 0 ) {
                //System.out.println(diff);
                Vector2D force = diff.divide(length*body.getMassInv()).multiply(75);
                //System.out.println(force.length());
                body.applyForce(force);
                //body.addConstrainedPosition(target);
            }
        }
    }
    
    public void update() {
        if ( body != null && mousePos != null ) {
            List<Body2D> bodies = body.getBodies();
            for ( int i = 0; i < bodies.size(); i++ ) {
                applyForceToBody(bodies.get(i), distances.get(i));
            }
        }
        //return 0;
    }
    
}