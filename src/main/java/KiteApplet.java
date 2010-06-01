import javax.swing.JApplet;
import java.io.IOException;

public class KiteApplet extends JApplet {
    
    public void init() {
        try {
            add(new KiteCanvas());
        }
        catch(IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
}