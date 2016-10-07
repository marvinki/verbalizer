/**
 * 
 */
package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * a CustomJPanels are properly sized Jpanels for the
 * TextExplanationResult ...
 * @author fpaffrath
 *
 */
public class CustomJPanel extends JPanel {

	/**
	 * a CustomJPanels are properly sized Jpanels for the
	 * TextExplanationResult
	 */
	private static final long serialVersionUID = -5704255319127525196L;


	/**
	 * inherited
	 */
	public CustomJPanel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * inherited
	 * @param layout inherited
	 */
	public CustomJPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	/**
	 * inherited
	 * @param isDoubleBuffered inherited
	 */
	public CustomJPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	/**
	 * inherited
	 * @param layout inherited
	 * @param isDoubleBuffered inherited
	 */
	public CustomJPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * computes the needed size for the width of the Container cont.
	 * 
	 * @param cont Container of the CustomJPanel 
	 * @return the Dimension with the same Width of the Container and 
	 * 		the adapted Height.
	 *
	 */
	public Dimension computeBestSize(Container cont){
	
	
	int vspace = 3;	// upper and lower spacing
	int fontHeight = getFontMetrics(getFont()).getHeight()+ // height of a line
			2*vspace;
	int height = fontHeight; // actual (assumed to be best) height
		
	int maxWidth = cont.getWidth(); // desired width (width of the container)
	int width = 0; // actual width of a line
	int rspace = 50; // spacing to the right 
	
	Dimension d = new Dimension(maxWidth, height);
		
	// count components and add new line (add height to the panel) when 
	// needed.
	for(int i=0; i<getComponentCount(); i++){
		setSize(d);
		Component comp = getComponent(i);
				
		width += Math.ceil(comp.getMaximumSize().getWidth());
		
		if(width >= maxWidth-rspace){
			System.out.println("longer");
			height += fontHeight;
			d.setSize(maxWidth, height);
			width = 0;
		}	
	}
	
	return d;	
	}

}
