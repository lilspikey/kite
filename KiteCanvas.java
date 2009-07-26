import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class KiteCanvas extends JPanel {
    private Space2D space = new Space2D();
    
    private ArrayList<Body2D> kiteString = new ArrayList<Body2D>();
    private ArrayList<Body2D> kite = new ArrayList<Body2D>();
    
    public KiteCanvas() {
        space.setGravity( new Vector2D(0, -10) );
        space.addGlobalConstraint(new FloorConstraint());
        
        Body2D prev = new Body2D(1, 10);
        prev.setMass(1000);
        space.add(prev);
        kiteString.add(prev);
        
        for ( int i = 0; i < 150; i++ ) {
            Body2D bi = new Body2D(prev.getPos().x + 2, prev.getPos().y + 0.5);
            space.add(bi);
            space.add(new StickConstraint<Body2D, Vector2D>(bi, prev));
            prev = bi;
            prev.setMass(1);
            kiteString.add(prev);
        }
        
        Body2D center = prev;
        
        Body2D c1 = new Body2D(prev.getPos().x, prev.getPos().y + 20);
        Body2D c2 = new Body2D(prev.getPos().x + 30, prev.getPos().y);
        Body2D c3 = new Body2D(prev.getPos().x, prev.getPos().y - 40);
        Body2D c4 = new Body2D(prev.getPos().x - 30, prev.getPos().y);
        
        kite.add(c1);
        kite.add(c2);
        kite.add(c3);
        kite.add(c4);
        
        space.add(c1);
        space.add(c2);
        space.add(c3);
        space.add(c4);
        
        // constraints from center to corners
        space.add(new StickConstraint<Body2D, Vector2D>(center, c1));
        space.add(new StickConstraint<Body2D, Vector2D>(center, c2));
        space.add(new StickConstraint<Body2D, Vector2D>(center, c3));
        space.add(new StickConstraint<Body2D, Vector2D>(center, c4));
        
        // constraints between corners
        space.add(new StickConstraint<Body2D, Vector2D>(c1, c2));
        space.add(new StickConstraint<Body2D, Vector2D>(c2, c3));
        space.add(new StickConstraint<Body2D, Vector2D>(c3, c4));
        space.add(new StickConstraint<Body2D, Vector2D>(c4, c1));
        // cross
        space.add(new StickConstraint<Body2D, Vector2D>(c1, c3));
        space.add(new StickConstraint<Body2D, Vector2D>(c2, c4));
        
        // give it initial velocity upwards
        for ( Body2D b: kite ) {
            b.setVelocity(new Vector2D(0, 10));
            b.setMass(10);
        }
    }
    
    public void tick() {
        //applyWindForce();
        space.update(0.04);
        repaint();
    }
    
    public void applyWindForce() {
        Vector2D wind = new Vector2D(10, 1);
        
        Vector2D crossBar = kite.get(1).getPos().subtract( kite.get(3).getPos() ).unit();
        Vector2D force = wind.multiply(crossBar);
        
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
        drawPolygon(g, kiteString, false);
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