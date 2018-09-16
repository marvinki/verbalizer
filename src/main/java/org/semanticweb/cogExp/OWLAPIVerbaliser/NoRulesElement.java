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

package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JTextPane;

// import org.protege.editor.owl.OWLEditorKit;

/**
 * Special TextElement that indicates that there's nothing to explain. 
 * It comes in action if e.g. there is no SequentInferenceRule for 
 * the axiom.
 * It is supposed to facilitate coloring and such like.
 * @author fpaffrath
 *
 */
public class NoRulesElement extends TextElement{
		
	
	/*TODO maybe an extra "Failure!" Element should be implemented
	 * 
	 */

	/**
	 * This constructor loads the TextElement with the string
	 * "That's already stated in the ontology. " and adds an explanation at the end. 
	 * @param explanation is added at the end
	 */
	public NoRulesElement(String explanation) {
		ResourceBundle LogicLabels = ResourceBundle.getBundle("LogicLabels", VerbaliseTreeManager.locale);
		this.content = LogicLabels.getString("thatsAlreadyStated") + explanation;
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toHTML()
	 */
	@Override
	public String toHTML() {
		// TODO Auto-generated method stub
		return super.toHTML();
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#setContent(java.lang.String)
	 */
	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		super.setContent(content);
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#addToDocument(javax.swing.JTextPane)
	 */
	@Override
	public void addToDocument(JTextPane textPane) {
		// TODO Auto-generated method stub
		super.addToDocument(textPane);
	}
	

	

}
