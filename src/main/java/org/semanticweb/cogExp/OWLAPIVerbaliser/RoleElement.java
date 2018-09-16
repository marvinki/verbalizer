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

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
/**
 * Represents any role.
 *
 * @author marvinki (doc by fp)
 *
 */

public class RoleElement extends TextElement{

	/**
	 * @param content of the JLabel 
	 */
	public RoleElement(String content){
		
		super(content);
	}

	@Override
	public String toHTML(){
		return "<font color=Maroon>" + content + "</font>";  // <-- there was a space there on purpose 
	}
	
	@Override
	public String toString(){
		return content;
	}
	
	@Override
	public void addToDocument(JTextPane textPane){
		StyledDocument document = textPane.getStyledDocument();
		String str = "<font color=Maroon>" + content + "</font>";
		try {
			document.insertString(document.getLength(), str, null);
		} catch (BadLocationException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}