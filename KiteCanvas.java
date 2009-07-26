import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KiteCanvas extends JPanel {
    private Space2D space = new Space2D();
    
    public KiteCanvas() {
        space.setGravity( new Vector2D(0, -10) );
        space.addGlobalConstraint(new FloorConstraint());
        
        Body2D b1 = new Body2D(50, 50);
        Body2D b2 = new Body2D(50 + 36, 50 + 77);
        Body2D b3 = new Body2D(b2.getPos().x, b2.getPos().y+50);
        Body2D b4 = new Body2D(b2.getPos().x+50, b2.getPos().y);
        
        space.add(b1);
        space.add(b2);
        space.add(b3);
        space.add(b4);
        space.add(new StickConstraint<Body2D, Vector2D>(b1, b2));
        space.add(new StickConstraint<Body2D, Vector2D>(b2, b3));
        space.add(new StickConstraint<Body2D, Vector2D>(b2, b4));
        space.add(new StickConstraint<Body2D, Vector2D>(b3, b4));
    }
    
    public void tick() {
        space.update(0.04);
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        for ( Body2D b: space.getBodies() ) {
            Vector2D pos = b.getPos();
            g.drawOval((int)(pos.x-2), (int)(pos.y-2), 4, 4);
        }
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