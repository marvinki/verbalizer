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
	
	int maxWidth = cont.getWidth();
	int vspace = 3;
	int fontHeight = getFontMetrics(getFont()).getHeight()+
			2*vspace;
	int Height = fontHeight;
	
	Dimension d = new Dimension(maxWidth, Height);
	
	int n = getComponentCount();
	int totalWidth = 0;
	int rigthPadding = 50;
	
	for(int i=0; i<n; i++){
		setSize(d);
		Component comp = getComponent(i);
				
		totalWidth += Math.ceil(comp.getMaximumSize().getWidth());
		if(totalWidth >= maxWidth-rigthPadding){
			System.out.println("longer");
			Height += fontHeight;
			d.setSize(maxWidth, Height);
			totalWidth = 0;
		}
		
	}
	
	return d;	
	}

}
