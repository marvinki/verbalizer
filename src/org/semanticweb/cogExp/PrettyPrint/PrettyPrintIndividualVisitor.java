package org.semanticweb.cogExp.PrettyPrint;

import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLIndividual;

import org.semanticweb.owlapi.model.OWLIndividualVisitorEx;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

/** Pretty Printing for individuals -- currently just uses toString()
 * 
 * @author marvin
 *
 */
public class PrettyPrintIndividualVisitor implements OWLIndividualVisitorEx<String>{

	public String visit(OWLIndividual arg0) {
		return arg0.toString();
	}

	public String visit(OWLNamedIndividual arg0) {
		return arg0.toString();
	}
	public String visit(OWLAnonymousIndividual arg0) {
		return arg0.toString();
	}
}
