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
