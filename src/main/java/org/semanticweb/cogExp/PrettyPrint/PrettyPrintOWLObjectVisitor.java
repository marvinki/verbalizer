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

package org.semanticweb.cogExp.PrettyPrint;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi.model.SWRLVariable;

public class PrettyPrintOWLObjectVisitor implements OWLObjectVisitorEx<String>{

	    private PrettyPrintPropertyExpressionVisitor ppPropVisit = new PrettyPrintPropertyExpressionVisitor();
	    private PrettyPrintDataRangeVisitor ppDataRangeVisit = new PrettyPrintDataRangeVisitor();
	    private PrettyPrintClassExpressionVisitor ppClassExpressionVisit = new PrettyPrintClassExpressionVisitor();
	    private PrettyPrintOWLAxiomVisitor ppOWLAxiomVisit = new PrettyPrintOWLAxiomVisitor();
	    
	    // AXIOMS
		public String visit(OWLSubClassOfAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLAsymmetricObjectPropertyAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLReflexiveObjectPropertyAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDisjointClassesAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDataPropertyDomainAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLObjectPropertyDomainAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLEquivalentObjectPropertiesAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLNegativeDataPropertyAssertionAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDifferentIndividualsAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDisjointDataPropertiesAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDisjointObjectPropertiesAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLObjectPropertyRangeAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLObjectPropertyAssertionAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLFunctionalObjectPropertyAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLSubObjectPropertyOfAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDisjointUnionAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDeclarationAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLAnnotationAssertionAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLSymmetricObjectPropertyAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDataPropertyRangeAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLFunctionalDataPropertyAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLEquivalentDataPropertiesAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLClassAssertionAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLEquivalentClassesAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDataPropertyAssertionAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLTransitiveObjectPropertyAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLIrreflexiveObjectPropertyAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLSubDataPropertyOfAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLInverseFunctionalObjectPropertyAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLSameIndividualAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLSubPropertyChainOfAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLInverseObjectPropertiesAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLHasKeyAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLDatatypeDefinitionAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		
		// SWRLRule
		public String visit(SWRLRule arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLSubAnnotationPropertyOfAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLAnnotationPropertyDomainAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		public String visit(OWLAnnotationPropertyRangeAxiom arg0) {
			return ppOWLAxiomVisit.visit(arg0);
		}
		
		// Class Expressions
		public String visit(OWLClass arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectIntersectionOf arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectUnionOf arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectComplementOf arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectSomeValuesFrom arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectAllValuesFrom arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectHasValue arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectMinCardinality arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectExactCardinality arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectMaxCardinality arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLObjectHasSelf arg0) {
			return null;
		}
		public String visit(OWLObjectOneOf arg0) {
			return ppClassExpressionVisit.visit(arg0);
		}
		public String visit(OWLDataSomeValuesFrom arg0) {
			// TODO Auto-generated method stub
			// return ppDataRangeVisit.visit(arg0);
			return null;
		}
		public String visit(OWLDataAllValuesFrom arg0) {
			// TODO Auto-generated method stub
			// return ppDataRangeVisit.visit(arg0);
				return null;
		}
		public String visit(OWLDataHasValue arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDataMinCardinality arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDataExactCardinality arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDataMaxCardinality arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDatatype arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDataComplementOf arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDataOneOf arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDataIntersectionOf arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDataUnionOf arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDatatypeRestriction arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLLiteral arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLFacetRestriction arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		// Property	
		public String visit(OWLObjectProperty arg0) {
			// TODO Auto-generated method stub
			return ppPropVisit.visit(arg0);
		}
		public String visit(OWLObjectInverseOf arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLDataProperty arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLNamedIndividual arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLAnnotationProperty arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLAnnotation arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(IRI arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLAnonymousIndividual arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLClassAtom arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLDataRangeAtom arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLObjectPropertyAtom arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLDataPropertyAtom arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLBuiltInAtom arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLVariable arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLIndividualArgument arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLLiteralArgument arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLSameIndividualAtom arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(SWRLDifferentIndividualsAtom arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		public String visit(OWLOntology arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	   
		
      
	
}
