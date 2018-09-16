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

/**
 * 
 */
package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * a CustomJPanels are properly sized JPanels for the
 * TextExplanationResult. There is only use for it
 * in the GUI for the Protege plugin.
 * @author fpaffrath
 *
 */

public class CustomJPanel extends JPanel {

	/**
	 * a CustomJPanels are properly sized JPanels for the
	 * TextExplanationResult
	 */
	private static final long serialVersionUID = -5704255319127525196L;

	private int vspace = 3;	// upper and lower spacing
	private int fontHeight = getFontMetrics(getFont()).getHeight()+ // height of a line
			2*vspace;
	private int height = fontHeight; // actual (assumed to be best) height
		
	private int maxWidth ; // desired width (width of the container)
	private int width = 0; // actual width of a line
	private int rspace = getInsets().left+getInsets().right; // spacing to the right 
	
	Dimension d = new Dimension(maxWidth, height);
	

	/**
	 * inherited
	 */
	public CustomJPanel() {
		super(new FlowLayout(FlowLayout.LEFT));
	}

	/**
	 * computes the needed size for the width of its Container.
	 * 
	 * @param cont Container of the CustomJPanel 
	 * @return the Dimension with the same Width of the Container and 
	 * 		the adapted Height.
	 *
	 */
	public Dimension computeBestSize(Container cont){
	// take the containers width as maximal width of this panel
	maxWidth = cont.getWidth();
	// count components and add new line (add height to the panel) when 
	// needed.	
		
	for(int i=0; i<getComponentCount(); i++){
					
		Component comp = getComponent(i);
		// TODO revise this implementation (following line)
		width += Math.ceil(comp.getPreferredSize().getWidth())+5; // the +5 incorporates the spaces between each Label- this is poor implementation and has to be revised
		
		if(width >= maxWidth-rspace){
			
			height += fontHeight;
			d.setSize(maxWidth, height);
			
			width = (int) Math.ceil(comp.getSize().getWidth());
		}
		else{
			d.setSize(maxWidth, height);
		}	
	}
	
	return d;	
	}

	
	/**
	 * inherited @see JLabel.add()
	 * @param label the JLabel that is to be added.
	 */
	public void add(JLabel label) {
		// TODO Auto-generated method stub
		super.add(label);
	}
}
