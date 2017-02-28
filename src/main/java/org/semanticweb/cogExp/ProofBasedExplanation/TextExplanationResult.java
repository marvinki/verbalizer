 package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.semanticweb.cogExp.OWLAPIVerbaliser.ContentJPanel;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementSequence;

/**
 * Contains the result (with proper layout) of the verbalized Axioms. 
 * @author fpaffrath
 *
 */
public class TextExplanationResult extends ExplanationResult{ // implements OWLModelManagerListener{

	private static final long serialVersionUID = 1944836708820525384L;

	private ContentJPanel content;
	private JPanel result = new JPanel();
	
	public TextExplanationResult(JPanel panel,OWLEditorKit ek){
		this.result = panel;
		
//		content.setLayout(contentLayout);
		// try to set the width and height of the content panel depending on the size of
		// the parent if the parent exists.
		// else, set it depending on the Workspace size
//		try{
//			
//			contentSize = getPreferredSize();
//			if(contentSize.getHeight()<=0||contentSize.getWidth()<=0){
//				contentSize = new Dimension(100, 100);
//			}
//	
//		}catch(Exception e){
//			
//			contentSize = new Dimension(ProtegeManager
//					.getInstance()
//					.getFrame(editorKit.getWorkspace())
//					.getSize());
//			contentSize.setSize(contentSize.getWidth()*0.8, contentSize.getHeight());
//	
//			
//		}
//		content.setSize(contentSize);
	    
		
		// must be set s.t. the scroll pane works properly
		this.setLayout(new BorderLayout(0,0));
	}
	
	
	/**
	 * returns a proper sized JPanel (depending on the size of the screen
	 * and on the length of the sequence)
	 * @param sequence generated by the verbalizer
	 * @param ek OWLEditorKit
	 * @return TextExplanationResult with proper size
	 */
	public TextExplanationResult getResult(TextElementSequence sequence, OWLEditorKit ek){
//		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		
		
		content = new ContentJPanel(ek, sequence);
		result.add(content);
		this.add(getScrollPane(result));
//		contentSize.setSize(contentSize.getWidth(), height/*+fac*getFontMetrics(getFont()).getHeight()*/);

		return this;
	}
	
	
	/**
	 * 
	 * @param panel
	 * @return ScrollPane for panel
	 */
	private JScrollPane getScrollPane(JPanel panel){		
			JScrollPane scrollPane = new JScrollPane (panel, 
		            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
			return scrollPane;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
    public Dimension getPreferredSize() {
		Dimension d = new Dimension();
		d.setSize(content.getSize().getWidth()+getFontMetrics(getFont()).getMaxAdvance(),
				content.getSize().getHeight()+getFontMetrics(getFont()).getHeight());
		
		return d;
}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub	
	}

	

}