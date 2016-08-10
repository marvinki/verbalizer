package org.semanticweb.cogExp.ProofBasedExplanation;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ImagePanel extends JPanel{

private BufferedImage image;

	 public ImagePanel(String path) {
	       try {                
	    	   System.out.println("trying to read : " + path);
	          image = ImageIO.read(new File(path));
	       } catch (IOException ex) {
	            // handle exception...
	       }
	  }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        // g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters          
	        g.drawImage(image.getScaledInstance(1600, -1, Image.SCALE_SMOOTH), 0, 0, null);
	        
	    }
	    
	    public int getWidth(){
	    	return image.getScaledInstance(1600, -1, Image.SCALE_SMOOTH).getWidth(null);
	    	// return image.getWidth();
	    }
	    
	    public int getHeight(){
	    	return image.getScaledInstance(1600, -1, Image.SCALE_SMOOTH).getHeight(null);
	    	// return image.getHeight();
	    }
	    

	}
	
	

