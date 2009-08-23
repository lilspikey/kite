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

public class DropShadowLayerStyle implements LayerStyle {
    private BufferedImage backing = null;
    private BufferedImage shadow = null;
    
    private int shadowSize;
    private float shadowOpacity = 0.5f;
    private Color shadowColor = new Color(0x000000);
    private int distX;
    private int distY;
    
    public DropShadowLayerStyle() {
        this(9, 2, -2);
    }
    
    public DropShadowLayerStyle(int shadowSize, int distX, int distY) {
        this.shadowSize = shadowSize;
        this.distX = distX;
        this.distY = distY;
    }
    
    private boolean hasBacking(Layer layer) {
        if ( backing == null )
            return false;
        if ( backing.getWidth() != layer.getWidth() || backing.getHeight() != layer.getHeight() )
            return false;
        return true;
    }
    
    private void initBacking(Layer layer) {
        int width  = layer.getWidth(),
            height = layer.getHeight();
        
        backing = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    public void paint(Layer layer, Graphics2D g) {
        if ( !hasBacking(layer) ) {
            initBacking(layer);
        }
        
        Graphics2D gb = backing.createGraphics();
        gb.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // set to all transparent
        gb.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        gb.fillRect(0, 0, backing.getWidth(), backing.getHeight());
        
        // now render normally
        gb.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        
        layer.paintShapes(gb);
        
        gb.dispose();
        
        Graphics2D gs = shadow.createGraphics();
        gs.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        gs.drawImage(backing, 0, 0, null);
        gs.dispose();
        applyShadow(shadow);
        
        // render shadow
        g.drawImage(shadow, distX, distY, null);
        
        // draw original
        g.drawImage(backing, 0, 0, null);
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