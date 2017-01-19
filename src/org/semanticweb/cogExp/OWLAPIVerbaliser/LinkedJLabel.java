package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.renderer.LinkedObjectComponent;
import org.protege.editor.owl.ui.renderer.LinkedObjectComponentMediator;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * @author marvinki, doc by felixp
 * 
 * this LinkedJLabel implements the possibilities for
 * linking protege context to e.g. class elements
 *
 */
public class LinkedJLabel extends JLabel implements LinkedObjectComponent{

	
	private static final long serialVersionUID = 8173047877677087878L;
	
	private LinkedObjectComponentMediator mediator;
	  	private OWLEditorKit ek;

	    public LinkedJLabel(OWLEditorKit owlEditorKit) {
	        // super(owlEditorKit);
	        this.mediator = new LinkedObjectComponentMediator(owlEditorKit, this);
	        ek = owlEditorKit;
	        initializeHandCursor();
	        
	        
	    }
	    
	    public LinkedJLabel(String str){
	    	super(str);
	    	initializeHandCursor();
	    	
	    }
	    
	    private void initializeHandCursor(){
       	 this.setCursor(new Cursor(Cursor.HAND_CURSOR));
       }
	    
	    /**
         * Gets the location of the mouse relative to the rendering cell that it is
         * over.
         */
        /* (non-Javadoc)
         * @see org.protege.editor.owl.ui.renderer.LinkedObjectComponent#getMouseCellLocation()
         */
        public Point getMouseCellLocation() {
            Point mouseLoc = getMousePosition();
            if (mouseLoc == null) {
                return null;
            }
            Rectangle cellRect = getBounds();
            return new Point(mouseLoc.x - cellRect.x, mouseLoc.y - cellRect.y);
        }

        
        /* (non-Javadoc)
         * @see org.protege.editor.owl.ui.renderer.LinkedObjectComponent#getMouseCellRect()
         */
        public Rectangle getMouseCellRect() {
            Point loc = getMousePosition();
            if (loc == null) {
                return null;
            }
            return getBounds();
        }
    

    //    public Object getCellObject();
    /* (non-Javadoc)
     * @see org.protege.editor.owl.ui.renderer.LinkedObjectComponent#setLinkedObject(org.semanticweb.owlapi.model.OWLObject)
     */
    public void setLinkedObject(OWLObject object) {
        mediator.setLinkedObject(object);
    }


    /* (non-Javadoc)
     * @see org.protege.editor.owl.ui.renderer.LinkedObjectComponent#getLinkedObject()
     */
    public OWLObject getLinkedObject() {
    	String tooltip = this.getToolTipText();
    	OWLDataFactory factory = ek.getOWLModelManager().getOWLDataFactory();
		OWLClass cl = factory.getOWLClass(IRI.create(tooltip));
		System.out.println(cl.toString());
    	mediator.setLinkedObject(cl);	
        return mediator.getLinkedObject();
    }


    /* (non-Javadoc)
     * @see org.protege.editor.owl.ui.renderer.LinkedObjectComponent#getComponent()
     */
    public JComponent getComponent() {
        return this;
    }


	    
    
	
	
}
