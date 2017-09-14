package org.semanticweb.cogExp.FormulaConverter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class JustificationComparator {
	
	/*
	public static boolean classExpressionReplacementEqual(OWLClassExpression ce1, OWLClassExpression ce2, HashMap<OWLClassExpression, OWLClassExpression> hashsetClasses, HashMap<OWLObjectPropertyExpression, OWLObjectPropertyExpression> hashsetProperty){
		if (ce1.getClassExpressionType().equals(ce2.getClassExpressionType()))
			return false;
		if (ce1.isOWLThing() && ce2.isOWLThing())
			return true;
		if (ce1.isOWLNothing() && ce2.isOWLNothing())
			return true;
		if (ce1 instanceof OWLClass && ce2 instanceof OWLClass)
			return (hashsetClasses.get((OWLClass) ce1).equals(ce2));
		if (ce1 instanceof OWLSubClassOfAxiom && ce2 instanceof OWLSubClassOfAxiom){
			return 
					classExpressionReplacementEqual(((OWLSubClassOfAxiom) ce1).getSubClass(), 
							((OWLSubClassOfAxiom) ce2).getSubClass(), hashsetClasses, hashsetProperty)
					&& classExpressionReplacementEqual(((OWLSubClassOfAxiom) ce1).getSuperClass(), 
							((OWLSubClassOfAxiom) ce2).getSuperClass(), hashsetClasses, hashsetProperty);
		}
		if (ce1 instanceof OWLObjectSomeValuesFrom && ce2 instanceof OWLObjectSomeValuesFrom){
			if (!hashsetProperty.get(((OWLObjectSomeValuesFrom) ce1).getProperty()).equals(((OWLObjectSomeValuesFrom) ce2).getProperty()))
					return false;
			return 
					classExpressionReplacementEqual(((OWLObjectSomeValuesFrom) ce1).getFiller(), 
							((OWLObjectSomeValuesFrom) ce2).getFiller(), hashsetClasses, hashsetProperty);			
		}
		if (ce1 instanceof OWLObjectAllValuesFrom && ce2 instanceof OWLObjectAllValuesFrom){
			if (!hashsetProperty.get(((OWLObjectAllValuesFrom) ce1).getProperty()).equals(((OWLObjectAllValuesFrom) ce2).getProperty()))
					return false;
			return 
					classExpressionReplacementEqual(((OWLObjectAllValuesFrom) ce1).getFiller(), 
							((OWLObjectAllValuesFrom) ce2).getFiller(), hashsetClasses, hashsetProperty);			
		}
		
		
		return false;
	}
	
	public static Set<HashMap<OWLClass,OWLClass>> generateClassMappings(Set<OWLObject> formulaset1, Set<OWLObject> formulaset2){
		Set<HashMap<OWLClass,OWLClass>> results = new HashSet<HashMap<OWLClass,OWLClass>>();
		
		Set<OWLClass> classes1 = new HashSet<OWLClass>();
		for (OWLObject candidate : formulaset1){
			if (candidate instanceof OWLClassExpression){
				OWLClassExpression ce = (OWLClassExpression) candidate;
				classes1.addAll(ce.getClassesInSignature());
			}
			if (candidate instanceof OWLAxiom){
				OWLAxiom ce = (OWLAxiom) candidate;
				classes1.addAll(ce.getClassesInSignature());
			}
		}
		
		Set<OWLClass> classes2 = new HashSet<OWLClass>();
		for (OWLObject candidate : formulaset2){
			if (candidate instanceof OWLClassExpression){
				OWLClassExpression ce = (OWLClassExpression) candidate;
				classes2.addAll(ce.getClassesInSignature());
			}
			if (candidate instanceof OWLAxiom){
				OWLAxiom ce = (OWLAxiom) candidate;
				classes2.addAll(ce.getClassesInSignature());
			}
		}
		
		if (classes1.size()==classes2.size()){
			
		}
		
	} 
	*/

}
