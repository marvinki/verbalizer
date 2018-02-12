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
