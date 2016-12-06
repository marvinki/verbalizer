package org.semanticweb.cogExp.ProofBasedExplanation;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.list.OWLObjectList;
import org.protege.editor.owl.ui.renderer.LinkedObjectComponent;
import org.protege.editor.owl.ui.renderer.LinkedObjectComponentMediator;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;

public class LinkedJLabel extends JLabel implements LinkedObjectComponent{

	  private LinkedObjectComponentMediator mediator;
	  private OWLEditorKit ek;

	    public LinkedJLabel(OWLEditorKit owlEditorKit) {
	        // super(owlEditorKit);
	        this.mediator = new LinkedObjectComponentMediator(owlEditorKit, this);
	        ek = owlEditorKit;
	    }

	    /**
         * Gets the location of the mouse relative to the rendering cell that it is
         * over.
         */
        public Point getMouseCellLocation() {
            Point mouseLoc = getMousePosition();
            if (mouseLoc == null) {
                return null;
            }
            Rectangle cellRect = getBounds();
            return new Point(mouseLoc.x - cellRect.x, mouseLoc.y - cellRect.y);
        }

        public Rectangle getMouseCellRect() {
            Point loc = getMousePosition();
            if (loc == null) {
                return null;
            }
            return getBounds();
        }
    

    //    public Object getCellObject();
    public void setLinkedObject(OWLObject object) {
        mediator.setLinkedObject(object);
    }


    public OWLObject getLinkedObject() {
    	String tooltip = this.getToolTipText();
    	OWLDataFactory factory = ek.getOWLModelManager().getOWLDataFactory();
		OWLClass cl = factory.getOWLClass(IRI.create(tooltip));
		System.out.println(cl.toString());
    	mediator.setLinkedObject(cl);	
        return mediator.getLinkedObject();
    }


    public JComponent getComponent() {
        return this;
    }
	    

	
	
}
