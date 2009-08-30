import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.geom.*;

import java.util.ArrayList;
import java.io.*;

import com.psychicorigami.physics.*;
import com.psychicorigami.scene.ImageShape;
import com.psychicorigami.scene.RopeShape;
import com.psychicorigami.scene.Scene;
import com.psychicorigami.scene.ColoriseLayerStyle;

public class KiteCanvas extends JPanel {
    private Space2D space = new Space2D();
    private Scene scene = new Scene();
    ColoriseLayerStyle layerTint = new ColoriseLayerStyle();
    
    private final int SCENE_BACKGROUND = 0;
    private final int SCENE_MIDDLEGROUND = 1;
    
    private ArrayList<Body2D> kite = new ArrayList<Body2D>();
    private ImageShape kiteShape = null;
    private ArrayList<RopeShape> ropeShapes = new ArrayList<RopeShape>();
    
    private Rope2D mainRope = null;
    private ArrayList<Rope2D> kiteRopes = new ArrayList<Rope2D>();
    
    private Body2D base = null;
    
    private int updateCount = 10;
    private double dt = 0.04/updateCount;
    private double rope_weight = 1;
    private double kiteMass = 10;
    private double windStrength = 2000;
    private Vector2D force = new Vector2D();
    
    public KiteCanvas() throws IOException {
        
        scene.addLayerStyle(layerTint, SCENE_MIDDLEGROUND);
        layerTint.setOpacity(1);
        
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                scene.setSize(getWidth(), getHeight());
            }
        });
        
        space.setGravity( new Vector2D(0, -10) );
        space.addGlobalConstraint(new FloorConstraint());
        
        base = new Body2D(1, 10);
        base.setImmovable();
        space.add(base);
        
        mainRope = new Rope2D(new Vector2D(base.getPos().x+1, base.getPos().y), new Vector2D(300, 70), 10, rope_weight);
        space.add(mainRope);
        kiteRopes.add(mainRope);
        
        space.add(new RopeConstraint<Body2D, Vector2D>(base, mainRope.getStart()));
        
        Body2D joint = mainRope.getEnd();
        Vector2D center = joint.getPos().add(new Vector2D(50, 0));
        
        Body2D c1 = new Body2D(center.x, center.y + 20);
        Body2D c2 = new Body2D(center.x + 30, center.y);
        Body2D c3 = new Body2D(center.x, center.y - 40);
        Body2D c4 = new Body2D(center.x - 30, center.y);
        
        Body2D middle = new Body2D(center.x, center.y+10);
        Body2D lower = new Body2D(center.x, center.y-30);
        
        c1.setMass(kiteMass);
        c2.setMass(kiteMass);
        c3.setMass(kiteMass);
        c4.setMass(kiteMass);
        
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
            b.setVelocity(new Vector2D(0, 200).multiply(dt));
        }
        
        // now add rigging
        Rope2D topRope = new Rope2D(joint.getPos().add(new Vector2D(0.01,0.01)),
                                    middle.getPos().add(new Vector2D(-0.01,-0.01)),
                                    2, rope_weight);
        Rope2D bottomRope = new Rope2D(joint.getPos().add(new Vector2D(0.01,0.01)),
                                    lower.getPos().add(new Vector2D(-0.01,-0.01)),
                                    2, rope_weight);
        
        space.add(topRope);
        kiteRopes.add(topRope);
        
        space.add(bottomRope);
        kiteRopes.add(bottomRope);
        
        space.add(new RopeConstraint<Body2D, Vector2D>(joint, topRope.getStart()));
        space.add(new RopeConstraint<Body2D, Vector2D>(middle, topRope.getEnd()));
        
        space.add(new RopeConstraint<Body2D, Vector2D>(joint, bottomRope.getStart()));
        space.add(new RopeConstraint<Body2D, Vector2D>(lower, bottomRope.getEnd()));
        
        // add a tail
        /*Rope2D tail = new Rope2D(c3.getPos().add(new Vector2D(0.01, 0.01)),
                                 c3.getPos().add(new Vector2D(100, 0)),
                                 100, 4.0);
        
        space.add(tail);
        space.add(new StickConstraint<Body2D, Vector2D>(c3, tail.getStart()));
        kiteRopes.add(tail);*/
        BufferedImage kiteImg = ImageIO.read(getClass().getResource("/images/kite.png"));
        
        kiteShape = new ImageShape(kiteImg);
        scene.add(kiteShape, SCENE_MIDDLEGROUND);
        
        for ( Rope2D r: kiteRopes ) {
            RopeShape rope = new RopeShape(Color.GRAY);
            ropeShapes.add(rope);
            scene.add(rope, SCENE_MIDDLEGROUND);
        }
        
        BufferedImage skyImg = ImageIO.read(getClass().getResource("/images/sky.jpg"));
        scene.setBackground(skyImg, SCENE_BACKGROUND);
        
        
        
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                requestFocus();
            }
        });
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                double dx = 0;
                switch(ke.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        dx = -100;
                        break;
                    case KeyEvent.VK_RIGHT:
                        dx = 100;
                        break;
                    case KeyEvent.VK_UP:
                        changeRopeLength(2);
                        return;
                    case KeyEvent.VK_DOWN:
                        changeRopeLength(-2);
                        return;
                }
                //base.setPos(new Vector2D(base.getPos().x+dx, base.getPos().y));
                base.setVelocity(new Vector2D(dt*dx, 0));
            }
            public void keyReleased(KeyEvent ke) {
                base.setVelocity(new Vector2D(0, 0));
            }
        });
    }
    
    public void changeRopeLength(double dx) {
        double length = mainRope.calcLength();
        mainRope.changeLength(length + dx);
    }
    
    public void tick() {
        for ( int i = 0; i < 2*updateCount; i++ ) {
            applyForces();
            space.update(dt);
        }
        updateScene();
        repaint();
    }
    
    public void applyForces() {
        Vector2D wind = new Vector2D(windStrength, 0);
        Vector2D crossBar = kite.get(1).getPos().subtract( kite.get(3).getPos() ).unit();
        
        Vector2D force = crossBar.multiply(crossBar.dotProduct(wind));
        
        // factor in a bit of drag (speed versus the wind)
        Vector2D speed = new Vector2D();
        for ( Body2D b: kite ) {
            Vector2D v = b.getVelocity().divide(dt);
            speed = speed.add(v);
        }
        
        speed = speed.divide(kite.size());
        
        force = force.subtract(speed);
        
        for ( Body2D b: kite ) {
            b.applyForce(force);
        }
        
        this.force = force;
    }
    
    public void updateScene() {
        Body2D top = kite.get(0);
        Body2D bottom = kite.get(2);
        
        Vector2D p1 = top.getPos();
        Vector2D p2 = bottom.getPos();
        
        Vector2D pos = p1.add(p2).multiply(0.5);
        Vector2D angle = p1.subtract(p2).unit();
        
        kiteShape.setAngle(Math.atan2(angle.y, angle.x));
        kiteShape.setX(pos.x);
        kiteShape.setY(pos.y);
        
        for ( int i = 0; i < kiteRopes.size(); i++ ) {
            Rope2D rope = kiteRopes.get(i);
            RopeShape shape = ropeShapes.get(i);
            shape.clear();
            for ( Body2D b: rope.getBodies() ) {
                shape.add(b.getPos());
            }
        }
        
        float opacity = layerTint.getOpacity();
        opacity -= 0.01;
        if ( opacity < 0 )
            opacity = 0;
        layerTint.setOpacity(opacity);
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int height = getHeight();
        g.clearRect(0, 0, getWidth(), height);
        
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -getHeight());
        g2d.transform(tx);
        
        scene.paint(g2d);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(480, 480);
    }
    
    public static void main(String[] args) throws IOException {
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