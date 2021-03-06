import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.geom.*;

import java.util.ArrayList;
import java.io.*;

import com.psychicorigami.physics.*;
import com.psychicorigami.scene.PhysicsShape;
import com.psychicorigami.scene.ImageShape;
import com.psychicorigami.scene.RopeShape;
import com.psychicorigami.scene.Scene;
import com.psychicorigami.scene.ColoriseLayerStyle;
import com.psychicorigami.scene.HighlightLayerStyle;
import com.psychicorigami.scene.DropShadowLayerStyle;
import com.psychicorigami.scene.PhysicsShapeDragger;

import com.psychicorigami.variable.Variable;

import static com.psychicorigami.variable.ConstVariable.const_var;

public class KiteCanvas extends JPanel {
    private Space2D space = new Space2D();
    private Scene scene = new Scene();
    private ColoriseLayerStyle layerTint = new ColoriseLayerStyle();
    private HighlightLayerStyle highlight = new HighlightLayerStyle();
    
    private final int SCENE_BACKGROUND = 0;
    private final int SCENE_MIDDLEGROUND = 1;
    private final int SCENE_FOREGROUND = 2;
    
    private Kite kite = null;
    private PhysicsShape kiteShape = null;
    private ArrayList<RopeShape> ropeShapes = new ArrayList<RopeShape>();
    
    private Rope2D mainRope = null;
    private ArrayList<Rope2D> kiteRopes = new ArrayList<Rope2D>();
    
    private Figure figure = null;
    
    private Body2D baseLeft = null;
    private Body2D baseRight = null;
    
    private int updateCount = 2;
    private double dt = 0.005/updateCount;
    private double rope_weight = 1;
    private double kiteMass = 10;
    private double WIND_SPEED = 1000;
    private double KITE_INITIAL_SPEED=100;
    
    private Vector2D force = new Vector2D();
    
    private PhysicsShapeDragger dragger = new PhysicsShapeDragger();
    private Vector2D currentMousePos = new Vector2D(0, 0);
    private PhysicsShape currentShapeAtMousePos = null;
    private PhysicsShape dragShape    = null;
    private double targetRopeLength = 0;
    
    private GravityForce<Body2D, Vector2D> gravity = new GravityForce<Body2D, Vector2D>(new Vector2D(0, -10*10));
    
    private Sun sun = null;
    private Wind wind = null;
    
    public KiteCanvas() throws IOException {
        
        scene.addLayerStyle(new DropShadowLayerStyle(), SCENE_FOREGROUND);
        scene.addLayerStyle(layerTint, SCENE_FOREGROUND);
        scene.addLayerStyle(highlight, SCENE_FOREGROUND);
        scene.addLayerStyle(highlight, SCENE_MIDDLEGROUND);
        
        sun = new Sun(new Vector2D(300, 300));
        space.add(sun);
        scene.add(sun, SCENE_MIDDLEGROUND);
        
        wind = new Wind(new Vector2D(60, 250), WIND_SPEED);
        space.add(wind);
        scene.add(wind, SCENE_FOREGROUND);
        
        layerTint.setOpacity(new Variable<Float>() {
            public Float val() {
                Vector2D pos = sun.getPos();
                double range = pos.y/150.0;
                
                return (float)(1.0 - Math.min(1.0, Math.max(0.0, range)));
            }
        });
        
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                scene.setSize(getWidth(), getHeight());
            }
        });
        
        NudgeForce leftEdge  = new NudgeForce(const_var(0), true);
        NudgeForce rightEdge = new NudgeForce(
            new Variable<Integer>() {
                public Integer val() {
                    return getWidth();
                }
            }
        , false);
        
        FloorConstraint floor = new FloorConstraint();
        space.addGlobalConstraint(floor);
        space.addForce(floor);
        space.addForce(gravity);
        space.addForce(leftEdge);
        space.addForce(rightEdge);
        
        figure = new Figure(new Vector2D(20, 50));
        space.add(figure);
        
        leftEdge.add(figure);
        rightEdge.add(figure);
        
        gravity.add(figure);
        
        Body2D hand = figure.getRightHand();
        
        mainRope = new Rope2D(new Vector2D(hand.getPos().x+0.01, hand.getPos().y), new Vector2D(300, 120), 10, rope_weight);
        space.add(mainRope);
        gravity.add(mainRope);
        
        targetRopeLength = mainRope.calcLength();
        
        kiteRopes.add(mainRope);
        
        space.addConstraint(new RopeConstraint<Body2D, Vector2D>(hand, mainRope.getStart()));
        
        Body2D joint = mainRope.getEnd();
        Vector2D center = joint.getPos().add(new Vector2D(20, -10));
        
        kite = new Kite(center, kiteMass);
        kite.setInitialVelocity(new Vector2D(0, KITE_INITIAL_SPEED), dt);
        
        space.add(kite);
        gravity.add(kite);
        space.addForce(kite.createWindForce(wind.getWindForce()));
        
        // now add rigging
        Rope2D topRope = new Rope2D(joint.getPos().add(new Vector2D(0.01,0.01)),
                                    kite.getTopHook().getPos().add(new Vector2D(-0.01,-0.01)),
                                    2, rope_weight);
        Rope2D bottomRope = new Rope2D(joint.getPos().add(new Vector2D(0.01,0.01)),
                                    kite.getBottomHook().getPos().add(new Vector2D(-0.01,-0.01)),
                                    2, rope_weight);
        
        space.add(topRope);
        kiteRopes.add(topRope);
        
        space.add(bottomRope);
        kiteRopes.add(bottomRope);
        
        gravity.add(topRope);
        gravity.add(bottomRope);
        
        space.addConstraint(new RopeConstraint<Body2D, Vector2D>(joint, topRope.getStart()));
        space.addConstraint(new RopeConstraint<Body2D, Vector2D>(kite.getTopHook(), topRope.getEnd()));
        
        space.addConstraint(new RopeConstraint<Body2D, Vector2D>(joint, bottomRope.getStart()));
        space.addConstraint(new RopeConstraint<Body2D, Vector2D>(kite.getBottomHook(), bottomRope.getEnd()));
        
        space.addForce(dragger);
        
        space.addForce(new Force() {
            public void update(double dt) {
                Vector2D center = new Vector2D(getWidth()/2, getHeight()/2);
                Vector2D diff = center.subtract(sun.getPos()).unit();
                for ( Body2D b: sun.getBodies() ) {
                    if ( !b.isImmovable() ) {
                        b.applyForce(diff.divide(b.getMassInv()).multiply(100));
                    }
                }
            }
        });
        
        // add a tail
        BufferedImage kiteImg = ImageIO.read(getClass().getResource("/images/kite.png"));
        
        kiteShape = new PhysicsShape(kiteImg, kite.getTopCorner(), kite.getBottomCorner());
        scene.add(kiteShape, SCENE_FOREGROUND);
        
        Color ropeColor = new Color(0xFF664411);
        for ( Rope2D r: kiteRopes ) {
            RopeShape rope = new RopeShape(ropeColor);
            ropeShapes.add(rope);
            scene.add(rope, SCENE_FOREGROUND);
        }
        
        scene.add(figure, SCENE_FOREGROUND);
        
        BufferedImage skyImg = ImageIO.read(getClass().getResource("/images/sky.jpg"));
        scene.setBackground(skyImg, SCENE_BACKGROUND);
        
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                requestFocus();
                updateMousePosition(me, true);
            }
            
            public void mouseReleased(MouseEvent me) {
                dragger.stopDragging();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent me) {
                updateMousePosition(me, false);
            }
            
            public void mouseDragged(MouseEvent me) {
                Vector2D v = transformedPosition(me);
                dragger.updateMousePos(v);
                currentMousePos = v;
            }
        });
        
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                int rotated = mwe.getWheelRotation();
                changeRopeLength(-rotated);
            }
        });
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                double dx = 0;
                switch(ke.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        dx = -25;
                        break;
                    case KeyEvent.VK_RIGHT:
                        dx = 25;
                        break;
                    case KeyEvent.VK_UP:
                        changeRopeLength(2);
                        return;
                    case KeyEvent.VK_DOWN:
                        changeRopeLength(-2);
                        return;
                }
                //base.setPos(new Vector2D(base.getPos().x+dx, base.getPos().y));
                //baseLeft.setVelocity(new Vector2D(dt*dx, 0));
                //baseRight.setVelocity(new Vector2D(dt*dx, 0));
            }
            public void keyReleased(KeyEvent ke) {
                //baseLeft.setVelocity(new Vector2D(0, 0));
                //baseRight.setVelocity(new Vector2D(0, 0));
            }
        });
        
        final KiteCanvas canvas = this;
        new Timer(40, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                canvas.tick();
            }
        }).start();
    }
    
    public Vector2D transformedPosition(MouseEvent me) {
        AffineTransform tx = createTransform();
        Point2D p = tx.transform(me.getPoint(), null);
        return new Vector2D(p.getX(), p.getY());
    }
    
    public MultiBody<Body2D, Vector2D> findMatchingBody(PhysicsShape shape) {
        if ( shape == kiteShape ) {
            return kite;
        }
        else {
            if ( figure.getShapes().contains(shape) ) {
                return figure;
            }
            else if ( sun.getShapes().contains(shape) ) {
                return sun;
            }
            else if ( wind.getShapes().contains(shape) ) {
                return wind;
            }
        }
        return null;
    }
    
    public void updateMousePosition(MouseEvent me, boolean pressed) {
        currentMousePos = transformedPosition(me);
        updateCursor();
        if ( pressed && currentShapeAtMousePos != null ) {
            dragger.dragShape(findMatchingBody(currentShapeAtMousePos), currentMousePos);
        }
    }
    
    private void updateCursor() {
        if ( dragger.isDragging() ) {
            return;
        }
        
        com.psychicorigami.scene.Shape shape = scene.findShapeAt((int)currentMousePos.x, (int)currentMousePos.y);
        if ( (shape != null && (shape instanceof PhysicsShape)) ) {
            if ( shape != null ) {
                currentShapeAtMousePos = (PhysicsShape)shape;
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        else {
            currentShapeAtMousePos = null;
            setCursor(null);
        }
        highlight.setHighlighted(currentShapeAtMousePos);
    }
    
    public void changeRopeLength(double dx) {
        double newLength = targetRopeLength + dx;
        targetRopeLength = Math.max(50, Math.min(400, newLength));
    }
    
    public void tick() {
        for ( int i = 0; i < 10*updateCount; i++ ) {
            double ropeDx = targetRopeLength - mainRope.calcLength();
            double maxDx = 0.5;
            ropeDx = Math.max(-maxDx, Math.min(maxDx, ropeDx));
            mainRope.changeLength(mainRope.calcLength() + ropeDx);
            space.update(dt);
        }
        updateScene();
        updateCursor();
        repaint();
    }
    
    public void updateScene() {
        
        for ( int i = 0; i < kiteRopes.size(); i++ ) {
            Rope2D rope = kiteRopes.get(i);
            RopeShape shape = ropeShapes.get(i);
            shape.clear();
            for ( Body2D b: rope.getBodies() ) {
                shape.add(b.getPos());
            }
        }
        
    }
    
    public AffineTransform createTransform() {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -getHeight());
        return tx;
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        AffineTransform tx = createTransform();
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
    }
    
}