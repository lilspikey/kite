package com.psychicorigami.scene;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferInt;

import java.util.Map;
import java.util.LinkedHashMap;

public class DropShadowLayerStyle implements LayerStyle {
    
    private int shadowSize;
    private float shadowOpacity = 0.5f;
    private Color shadowColor = new Color(0x000000);
    private int distX;
    private int distY;
    
    private Map<ImageShape, ImageShape> shadows = new LinkedHashMap<ImageShape, ImageShape>();
    
    public DropShadowLayerStyle() {
        this(9, 2, -2);
    }
    
    public DropShadowLayerStyle(int shadowSize, int distX, int distY) {
        this.shadowSize = shadowSize;
        this.distX = distX;
        this.distY = distY;
    }
    
    public void shapeAdded(Shape shape) {
        if ( shape instanceof ImageShape ) {
            ImageShape imageShape = (ImageShape)shape;
            ImageShape shadow = new ImageShape(createShadow(imageShape.getImage()));
            shadows.put(imageShape, shadow);
        }
    }
    
    public void shapeRemoved(Shape shape) {
        
    }
    
    public void preRender(Layer layer, BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(distX, distY);
        
        for ( Map.Entry<ImageShape,ImageShape> entry: shadows.entrySet() ) {
            ImageShape shape = entry.getKey();
            ImageShape shadow = entry.getValue();
            
            shadow.setX(shape.getX());
            shadow.setY(shape.getY());
            shadow.setAngle(shape.getAngle());
            
            shadow.paint(g);
        }
        
        g.dispose();
    }
    
    public void postRender(Layer layer, BufferedImage image) {
        
    }
    
    private BufferedImage createShadow(BufferedImage image) {
        BufferedImage shadow = new BufferedImage(image.getWidth()+shadowSize, image.getHeight()+shadowSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = shadow.createGraphics();
        
        gs.drawImage(image, shadowSize/2, shadowSize/2, null);
        gs.dispose();
        
        applyShadow(shadow);
        
        return shadow;
    }
    
    // from http://www.jroller.com/gfx/entry/fast_or_good_drop_shadows
    private void applyShadow(BufferedImage image) {
        
        int dstWidth = image.getWidth();
        int dstHeight = image.getHeight();

        int left = (shadowSize - 1) >> 1;
        int right = shadowSize - left;
        int xStart = left;
        int xStop = dstWidth - right;
        int yStart = left;
        int yStop = dstHeight - right;

        int shadowRgb = shadowColor.getRGB() & 0x00FFFFFF;
        
        int[] aHistory = new int[shadowSize];
        int historyIdx = 0;

        int aSum;

        int[] dataBuffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int lastPixelOffset = right * dstWidth;
        float sumDivider = shadowOpacity / shadowSize;

        // horizontal pass

        for (int y = 0, bufferOffset = 0; y < dstHeight; y++, bufferOffset = y * dstWidth) {
            aSum = 0;
            historyIdx = 0;
            for (int x = 0; x < shadowSize; x++, bufferOffset++) {
                int a = dataBuffer[bufferOffset] >>> 24;
                dataBuffer[bufferOffset] = 0;
                aHistory[x] = a;
                aSum += a;
            }

            bufferOffset -= right;

            for (int x = xStart; x < xStop; x++, bufferOffset++) {
                int a = (int) (aSum * sumDivider);
                dataBuffer[bufferOffset] = a << 24 | shadowRgb;

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];

                // get the lastest pixel
                a = dataBuffer[bufferOffset + right] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;

                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }

        // vertical pass
        for (int x = 0, bufferOffset = 0; x < dstWidth; x++, bufferOffset = x) {
            aSum = 0;
            historyIdx = 0;
            for (int y = 0; y < shadowSize; y++, bufferOffset += dstWidth) {
                int a = dataBuffer[bufferOffset] >>> 24;
                dataBuffer[bufferOffset] = 0;
                aHistory[y] = a;
                aSum += a;
            }

            bufferOffset -= lastPixelOffset;

            for (int y = yStart; y < yStop; y++, bufferOffset += dstWidth) {
                int a = (int) (aSum * sumDivider);
                dataBuffer[bufferOffset] = a << 24 | shadowRgb;

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];

                // get the lastest pixel
                a = dataBuffer[bufferOffset + lastPixelOffset] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;

                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }
    }
    
}