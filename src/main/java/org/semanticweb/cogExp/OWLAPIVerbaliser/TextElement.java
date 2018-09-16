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
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

// import org.protege.editor.owl.OWLEditorKit;
/**
 * Represents any textual element of verbalizer.
 * 
 * @author marvinki (doc by fp)
 *
 */
public class TextElement {
	
	protected String content;
	/**
	 * 
	 * @param content the content of the TextElement
	 */
	public TextElement(String content){
		this.content = content;
	}
	
	public TextElement(){
		this.content = "";
	}
	
	/**
	 * @return returns the content as plain string 
	 */
	public String toString(){
		return content;
	}

	/**
	 * @return the content of the TextElement as String. You could add 
	 * some HTML-tags here for formatting. 
	 */
	public String toHTML(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public void addToDocument(JTextPane textPane){
		StyledDocument document = textPane.getStyledDocument();
		try {
			document.insertString(document.getLength(), content, null);
		} catch (BadLocationException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param ek OWLEditorKit
	 * @return returns the content of the TextElement as list of JLabels,
	 * the elements have no white spaces at their ending or beginning
	 */
	/*
	public List<JLabel> toJLabel(OWLEditorKit ek){
		List<JLabel> result = new ArrayList<JLabel>();
		String name = content;
		if (content.endsWith(" "))
			 name = content.substring(0,name.length()-1);
		if (name.startsWith(" "))
			 name = name.substring(1,name.length());
		
		JLabel label = new JLabel(name);
		result.add(label);
		return result;
	}
	*/
	
	/**
	 * @return TODO add description
	 */
	public boolean isLinebreak(){
		return false;
	}
	
	/**
	 * @return TODO add description
	 */
	public boolean isConclusionMarker(){
		return false;
	}
	
	public void makeUppercase(){
		content = VerbaliseTreeManager.makeUppercaseStart(content);
		return;
	}
	
}