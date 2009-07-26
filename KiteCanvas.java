import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class KiteCanvas extends JPanel {
    private Space2D space = new Space2D();
    
    private ArrayList<Body2D> kite = new ArrayList<Body2D>();
    
    private ArrayList<Rope2D> kiteRopes = new ArrayList<Rope2D>();
    
    private double dt = 0.04/10;
    
    public KiteCanvas() {
        space.setGravity( new Vector2D(0, -10) );
        space.addGlobalConstraint(new FloorConstraint());
        
        Body2D base = new Body2D(1, 10);
        base.setImmovable();
        space.add(base);
        
        Rope2D mainRope = new Rope2D(new Vector2D(base.getPos().x+1, base.getPos().y), new Vector2D(300, 70), 150, 1.0);
        space.add(mainRope);
        kiteRopes.add(mainRope);
        
        space.add(new StickConstraint<Body2D, Vector2D>(base, mainRope.getStart()));
        
        Body2D joint = mainRope.getEnd();
        Vector2D center = joint.getPos().add(new Vector2D(50, 0));
        
        Body2D c1 = new Body2D(center.x, center.y + 20);
        Body2D c2 = new Body2D(center.x + 30, center.y);
        Body2D c3 = new Body2D(center.x, center.y - 40);
        Body2D c4 = new Body2D(center.x - 30, center.y);
        
        Body2D middle = new Body2D(center.x, center.y+10);
        Body2D lower = new Body2D(center.x, center.y-30);
        
        c1.setMass(40);
        c2.setMass(40);
        c3.setMass(100);
        c4.setMass(40);
        
        kite.add(c1);
        kite.add(c2);
        kite.add(c3);
        kite.add(c4);
        
        
        space.add(c1);
        space.add(c2);
        space.add(c3);
        space.add(c4);
        space.add(middle);
        space.add(lower);
        
        // constraints between corners
        space.add(new StickConstraint<Body2D, Vector2D>(c1, c2));
        space.add(new StickConstraint<Body2D, Vector2D>(c2, c3));
        space.add(new StickConstraint<Body2D, Vector2D>(c3, c4));
        space.add(new StickConstraint<Body2D, Vector2D>(c4, c1));
        // cross
        space.add(new StickConstraint<Body2D, Vector2D>(c1, c3));
        space.add(new StickConstraint<Body2D, Vector2D>(c2, c4));
        // middle
        space.add(new StickConstraint<Body2D, Vector2D>(c1, middle));
        space.add(new StickConstraint<Body2D, Vector2D>(middle, c3));
        space.add(new StickConstraint<Body2D, Vector2D>(lower, c3));
        space.add(new StickConstraint<Body2D, Vector2D>(lower, c1));
        
        // give it initial velocity upwards
        for ( Body2D b: kite ) {
            b.setVelocity(new Vector2D(0, 5).multiply(dt));
        }
        
        // now add rigging
        Rope2D topRope = new Rope2D(joint.getPos().add(new Vector2D(0.01,0.01)),
                                    middle.getPos().add(new Vector2D(-0.01,-0.01)),
                                    10, 1.0);
        Rope2D bottomRope = new Rope2D(joint.getPos().add(new Vector2D(0.01,0.01)),
                                    lower.getPos().add(new Vector2D(-0.01,-0.01)),
                                    10, 1.0);
        
        space.add(topRope);
        kiteRopes.add(topRope);
        
        space.add(bottomRope);
        kiteRopes.add(bottomRope);
        
        space.add(new StickConstraint<Body2D, Vector2D>(joint, topRope.getStart()));
        space.add(new StickConstraint<Body2D, Vector2D>(middle, topRope.getEnd()));
        
        space.add(new StickConstraint<Body2D, Vector2D>(joint, bottomRope.getStart()));
        space.add(new StickConstraint<Body2D, Vector2D>(lower, bottomRope.getEnd()));
        
        // add a tail
        /*Rope2D tail = new Rope2D(c3.getPos().add(new Vector2D(0.01, 0.01)),
                                 c3.getPos().add(new Vector2D(100, 0)),
                                 100, 4.0);
        
        space.add(tail);
        space.add(new StickConstraint<Body2D, Vector2D>(c3, tail.getStart()));
        kiteRopes.add(tail);*/
    }
    
    public void tick() {
        for ( int i = 0; i < 10; i++ ) {
            applyWindForce();
            space.update(dt);
        }
        repaint();
    }
    
    public void applyWindForce() {
        Vector2D wind = new Vector2D(10000, 0);
        Vector2D crossBar = kite.get(1).getPos().subtract( kite.get(3).getPos() ).unit();
        
        System.out.println(crossBar.dotProduct(wind));
        
        Vector2D force = crossBar.multiply(crossBar.dotProduct(wind));
        
        System.out.println(crossBar + " - " + force);
        
        //crossBar = kite.get(0).getPos().subtract( kite.get(2).getPos() ).unit();
        //force = force.add(wind.multiply(crossBar));
        
        for ( Body2D b: kite ) {
            b.applyForce(force);
        }
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int height = getHeight();
        g.clearRect(0, 0, getWidth(), height);
        g.translate(0, -10);
        
        g.setColor(Color.RED);
        drawPolygon(g, kite, true);
        
        g.setColor(Color.BLACK);
        drawLine(g, kite.get(0).getPos(), kite.get(2).getPos());
        drawLine(g, kite.get(1).getPos(), kite.get(3).getPos());
        
        g.setColor(Color.GRAY);
        
        for ( Rope2D kiteRope: kiteRopes ) {
            drawRope(g, kiteRope);
        }
    }
    
    private void drawRope(Graphics g, Rope2D rope) {
        drawPolygon(g, rope.getBodies(), false);
    }
    
    private void drawPolygon(Graphics g, java.util.List<Body2D> bodies, boolean join) {
        Vector2D prevPos = null;
        if ( join && bodies.size() > 0 ) {
            prevPos = bodies.get(bodies.size()-1).getPos();
        }
        for ( Body2D b: bodies ) {
            Vector2D pos = b.getPos();
            if ( prevPos != null ) {
                drawLine(g, pos, prevPos);
            }
            prevPos = pos;
        }
    }
    
    private void drawLine(Graphics g, Vector2D p1, Vector2D p2) {
        int height = getHeight();
        g.drawLine((int)(p1.x), (int)(height - p1.y), (int)(p2.x), (int)(height - p2.y));
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(480, 480);
    }
    
    public static void main(String[] args) {
        final KiteCanvas canvas = new KiteCanvas();
        JFrame frame = new JFrame("Kite");
        frame.add(canvas);
        frame.pack();
        
        frame.setVisible(true);
        
        new Timer(40, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                canvas.tick();
            }
        }).start();
    }
    
}