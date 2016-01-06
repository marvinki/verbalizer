package org.semanticweb.cogExp.PrettyPrint;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLPropertyRange;

public class PrettyPrintPropertyExpressionVisitor implements OWLPropertyExpressionVisitorEx<String>{

	public String visit(OWLProperty arg0) {
		return visit(arg0);
	}
	
	public String visit(OWLPropertyRange arg0) {
		return "na1";
	}
	
	public String visit(OWLPropertyExpression arg0) {
		return "na2";
	}
	
	public String visit(OWLObjectPropertyExpression arg0) {
		return arg0.getNamedProperty().getIRI().getFragment();
	}
	
	public String visit(OWLDataPropertyExpression arg0) {
		return "na4";
	}
	
	public String visit(OWLObjectProperty arg0) {
		return arg0.getIRI().getFragment();
	}

	public String visit(OWLObjectInverseOf arg0) {
		return arg0.getInverseProperty().accept(this) + "^-1";
	}

	public String visit(OWLDataProperty arg0) {
		return arg0.getIRI().getFragment();
	}

		
       
	
}
