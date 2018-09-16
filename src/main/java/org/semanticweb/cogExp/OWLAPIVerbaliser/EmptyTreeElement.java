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

import javax.swing.JLabel;
import javax.swing.JTextPane;

// import org.protege.editor.owl.OWLEditorKit;

/**
 * Represents a special TextElement that indicates that the Gentzen tree is empty. 
 * It comes in action if e.g. the proof can't be found within the limit.
 * It is supposed to facilitate coloring and such like.
 * There is no language support yet. (and there may be no need for that)
 * @author fpaffrath
 *
 */
public class EmptyTreeElement extends TextElement{


	/*TODO maybe an extra "Failure!" Element should be implemented
	 * 
	 */

	/**
	 * this constructor loads the TextElement with a special string
	 * to indicates that the Gentzen tree is empty. 
	 */
	public EmptyTreeElement() {	
		super("Failure! Empty tree!");
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toString()
	 */
	@Override
	public String toString() {
		return super.toString();
	}
	
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#toHTML()
	 */
	@Override
	public String toHTML() {
				return super.toHTML();
	}
	

	/* (non-Javadoc)
	 * @see org.semanticweb.cogExp.OWLAPIVerbaliser.TextElement#addToDocument(javax.swing.JTextPane)
	 */
	@Override
	public void addToDocument(JTextPane textPane) {
		super.addToDocument(textPane);
	}


}
