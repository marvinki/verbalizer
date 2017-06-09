package org.semanticweb.cogExp.GentzenTree;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

// import com.github.jabbalaci.graphviz.GraphViz;

public class GraphvizUtil {

	
	/*
	public static void handleDotRequest(String dotTree){
		try{
		// Temporary file for graphics
			String property = "java.io.tmpdir";
		    String tempDir = System.getProperty(property);
		    File newTempfile = new File(tempDir + File.separator + "graph.png");
		    
		    Path dotpath = Paths.get(tempDir + File.separator + "graph.dot");
		    File dotfile = new File(dotpath.toString());
		    System.out.println("dotfile generated in: " + dotfile);
		    if(!dotfile.exists()){
				dotfile.createNewFile();
			}
		    Writer dotWriter = new BufferedWriter(new OutputStreamWriter(
		    	    new FileOutputStream(dotfile.toString()), "UTF-8"));
		    
		    // PrintWriter dotWriter = new PrintWriter(dotfile.toString());
		    dotWriter.write(dotTree);
		    dotWriter.close();
		    
		    // Temporary file for png
		    Path pngpath = Paths.get(tempDir + File.separator + "graph.png");
		    File pngfile = new File(pngpath.toString());
		    if(!pngfile.exists()){
				pngfile.createNewFile();
			}
		    
		    // make the external call
		    createGraph(dotfile,pngfile);
		      
		   // System.out.println("pngfile: " + pngpath.toString());
		    
		    // Image_Panel panel = new Image_Panel(pngfile.toString());
		    System.out.println("Trying to access png image at path: " + pngfile.getPath());
		    // Image_Panel panel = new Image_Panel(pngfile.getPath());
	        // ImageZoom zoom = new ImageZoom(panel,panel.getScale());
		    BufferedImage picture = ImageIO.read(new File(pngfile.getPath()));
		    JLabel picLabel = new JLabel(new ImageIcon(picture));
	        JFrame f = new JFrame();
	        f.add(picLabel);
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       //  f.getContentPane().add(zoom.getUIPanel(), "North");
	       //f.getContentPane().add(new JScrollPane(panel));
	        int initHeight = (int) (picLabel.getPreferredSize().getHeight() * 1.1);
	        int initWidth = (int) (picLabel.getPreferredSize().getWidth() * 1.1);
	        // System.out.println(panel.getPreferredSize().getHeight());
	        f.setSize(initWidth,initHeight);
	        f.setLocation(200,200);
	        f.setVisible(true);
		} catch (Exception e){
			e.printStackTrace();
		}
		return;
		
	}

private static File createGraph(File source, File out){
		Runtime rt = Runtime.getRuntime();
		String[] args = {"dot", "-Tpng", source.getAbsolutePath() ,"-o", out.getAbsolutePath()};
		Process p;
		try {
			p = rt.exec(args);
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}

private static File generateGraph(String dot, String path) {

        GraphViz gv = new GraphViz();
        
          gv.add(dot);
          String type = "png";
          String representationType="dot";
          System.out.println(path);
          File out = new File(path);   
          System.out.println(out);

          String getsource = gv.getDotSource();
          
          byte[] graph = gv.getGraph( gv.getDotSource(), type,  representationType);
          
      
        return out;

    }
    */
	
}

