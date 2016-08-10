package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
  
public class ZoomWindow
{
    public static void main(String[] args)
    {
        Image_Panel panel = new Image_Panel("/var/folders/v0/3ytrm6vj49l0h238wgg66x7w0000gn/T/graph.png");
        ImageZoom zoom = new ImageZoom(panel,panel.getScale());
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(zoom.getUIPanel(), "North");
        f.getContentPane().add(new JScrollPane(panel));
        int initHeight = (int) (panel.getPreferredSize().getHeight() * 1.2);
        int initWidth = (int) (panel.getPreferredSize().getWidth() * 1.2);
        System.out.println(panel.getPreferredSize().getHeight());
        f.setSize(initWidth,initHeight);
        f.setLocation(200,200);
        f.setVisible(true);
        
    }
}
  
class Image_Panel extends JPanel
{
    BufferedImage image;
    double scale;
  
    public Image_Panel(String filename)
    {
        loadImage(filename);
        // scale = 1.0;
        // System.out.println(image.getWidth());
        scale = 1600.0/image.getWidth();
        // System.out.println("scale: " +scale);
        setBackground(Color.white);
    }
  
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int w = getWidth();
        int h = getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double x = (w - scale * imageWidth)/2;
        double y = (h - scale * imageHeight)/2;
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
    }
  
    /**
     * For the scroll pane.
     */
    public Dimension getPreferredSize()
    {
        int w = (int)(scale * image.getWidth());
        int h = (int)(scale * image.getHeight());
        return new Dimension(w, h);
    }
  
    public void setScale(double s)
    {
        scale = s;
        revalidate();      // update the scroll pane
        repaint();
    }
    
    public double getScale(){
    	return scale;
    }
  
    private void loadImage(String fileName)
    {
        // String fileName = "/var/folders/v0/3ytrm6vj49l0h238wgg66x7w0000gn/T/graph.png";
        try
        {
        	image = ImageIO.read(new File(fileName));
        }
        catch(MalformedURLException mue)
        {
            System.out.println("URL trouble: " + mue.getMessage());
        }
        catch(IOException ioe)
        {
            System.out.println("read trouble: " + ioe.getMessage());
        }
    }
}
  
class ImageZoom
{
    Image_Panel Image_Panel;
    double scale;
  
    public ImageZoom(Image_Panel ip, double scale)
    {
        Image_Panel = ip;
        this.scale = scale;
    }
  
    public JPanel getUIPanel()
    {
        SpinnerNumberModel model = new SpinnerNumberModel(scale, 0.1, 1.4, .01);
        final JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(45, spinner.getPreferredSize().height));
        spinner.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                float scale = ((Double)spinner.getValue()).floatValue();
                Image_Panel.setScale(scale);
            }
        });
        JPanel panel = new JPanel();
        panel.add(new JLabel("scale"));
        panel.add(spinner);
        return panel;
    }
}