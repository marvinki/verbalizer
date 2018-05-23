package org.semanticweb.cogExp.OWLAPIVerbaliser;



import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;

import com.google.common.base.Optional;

public class OWLAPICompatibility {

	public static  OWLLiteral getLiteral(OWLAnnotationAssertionAxiom axiom){
		return axiom.getAnnotation().getValue().asLiteral().get();
		
	}
	
	public static  Optional<OWLLiteral> asLiteral(OWLAnnotationValue value){
		return value.asLiteral();
		
	}
	
	public static Set<OWLObjectPropertyAxiom> getAxioms(OWLOntology ont, OWLObjectProperty p, boolean b){
		// return ont.getAxioms(p,b);
		return ont.getAxioms(p);
	}
	
	public static Set<OWLClassAxiom> getAxioms(OWLOntology ont, OWLClass p, boolean b){
		 return ont.getAxioms(p,b);
		
	}
	
	public static Set<OWLAxiom> getAxioms(OWLOntology ont, boolean b){
		 return ont.getAxioms(b);
		
	}
	
	public static Optional<OWLLiteral> asLiteral (IRI iri){
		// return iri.asLiteral();
		return null;
	}
	
	public static void fill (InferredOntologyGenerator iog, OWLDataFactory dataFactory, OWLOntology ont ){
		iog.fillOntology(dataFactory, ont);
	}
	
}
