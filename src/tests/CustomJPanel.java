/**
 * 
 */
package tests;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fasterxml.jackson.core.io.SegmentedStringWriter;

/**
 * @author fpaffrath
 *
 */
public class CustomJPanel extends JPanel {

	/**
	 * 
	 */
	public CustomJPanel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param layout
	 */
	public CustomJPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param isDoubleBuffered
	 */
	public CustomJPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public CustomJPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}
	
	
	public Dimension computeBestSize(Container cont){
	
	int maxWidth = cont.getWidth();
	System.out.println("Max Width: " +maxWidth);
	int vspace = 3;
	int fontHeight = getFontMetrics(getFont()).getHeight()+
			2*vspace;
	int Height = fontHeight;
	
	Dimension d = new Dimension(maxWidth, Height);
	
	int n = getComponentCount();
	int totalWidth = 0;
	
	for(int i=0; i<n; i++){
		setSize(d);
		Component comp = getComponent(i);
		
		totalWidth += Math.ceil(comp.getPreferredSize().getWidth());
		//System.out.println("Total Width: " +totalWidth);
		if(totalWidth >= maxWidth){
			System.out.println("longer");
			Height += fontHeight;
			d.setSize(maxWidth, Height);
			totalWidth = 0;
			continue;
		}
	}
	
	return d;	
	}

}
