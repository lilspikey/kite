import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KiteCanvas extends JPanel {
    private Space2D space = new Space2D();
    
    public KiteCanvas() {
        space.setGravity( new Vector2D(0, -10) );
        space.addGlobalConstraint(new FloorConstraint());
        
        space.add( new Body2D(50, 50) );
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