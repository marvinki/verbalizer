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

import javax.swing.JTextPane;

import org.json.JSONArray;

public class ConclusionMarkerElement extends TextElement{
	
	private List<TextElement> content;
	
	ConclusionMarkerElement(List<TextElement> content){
		this.content = content;
	}
	
	ConclusionMarkerElement(TextElementSequence content){
		this(content.getTextElements());
	}
	
	ConclusionMarkerElement(TextElement content){
		List<TextElement> wrap = new ArrayList<TextElement>();
		this.content = wrap;
		wrap.add(content);
	}
	
	
	
	
	
	public String toHTML(){
		TextElementSequence seq = new TextElementSequence(content);
		String output = seq.toHTML();
		return output;
	}
	
	@Override
	public void addToDocument(JTextPane textPane){
		for (TextElement element : content){
			element.addToDocument(textPane);
		}
	}
	
	public String toString(){
		TextElementSequence sequence = new TextElementSequence(content);
		return sequence.toString();
	}
	
	@Override
	public boolean isConclusionMarker(){
		return true;
	}
		
	public JSONArray toJSON(){
		TextElementSequence seq = new TextElementSequence(content);
		return seq.toJSON();
	}
	
	public String toWikiFormat(){
		TextElementSequence seq = new TextElementSequence(content);
		return seq.toWikiFormat();
	}
	
}
