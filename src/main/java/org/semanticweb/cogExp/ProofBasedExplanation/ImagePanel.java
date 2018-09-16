/*
 *     Copyright 2012-2018 Ulm University, AI Institute
 *     Main author: Marvin Schiller, contributors: Felix Paffrath, Chunhui Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

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
	
	

