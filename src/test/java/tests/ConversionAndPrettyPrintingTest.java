package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.semanticweb.cogExp.core.Pair;
import org.semanticweb.cogExp.inferencerules.AdditionalDLRules;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.SentenceOWLObjectVisitor;
import org.semanticweb.cogExp.OWLAPIVerbaliser.TextElementOWLObjectVisitor;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class ConversionAndPrettyPrintingTest {
	public static void main(String[] args) {
		
		OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
		OWLClass class1 = dataFactory.getOWLClass(IRI.create("http://foo#" + "Class1"));
		OWLClass class2 = dataFactory.getOWLClass(IRI.create("http://foo#" + "Class2"));
		OWLClass class3 = dataFactory.getOWLClass(IRI.create("http://foo#" + "Class3"));
		// OWLDataRange range1 = dataFactory.getOWL
		// OWLDataRange range1 = dataFactory.getOWLDataProperty(IRI.create("http://foo#" + "Range1"));
		
		OWLDatatype datatype = dataFactory.getBooleanOWLDatatype();
		OWLObjectProperty prop1 = dataFactory.getOWLObjectProperty(IRI.create("http://foo#" + "prop1"));
		OWLDataProperty prop2 = dataFactory.getOWLDataProperty(IRI.create("http://foo#" + "prop2"));
		OWLObjectExactCardinality cardExpr1 = dataFactory.getOWLObjectExactCardinality(1, prop1,class1);
		OWLDataExactCardinality cardExpr2 = dataFactory.getOWLDataExactCardinality(1, prop2,datatype);
		
		// Object Exact Cardinality
		
		OWLFormula cardForm1 = ConversionManager.fromOWLAPI(cardExpr1);
		OWLObject cardExpr1_b = ConversionManager.toOWLAPI(cardForm1);
		
		System.out.println(cardForm1);
		System.out.println(VerbalisationManager.prettyPrint(cardExpr1_b));
		
		// Data Exact Cardinality
		
		OWLFormula cardForm2 = ConversionManager.fromOWLAPI(cardExpr2);
		OWLObject cardExpr2_b = ConversionManager.toOWLAPI(cardForm2);
				
		System.out.println(cardForm2);
		System.out.println(VerbalisationManager.prettyPrint(cardExpr2_b));
		
		
		// Sentences
		System.out.println("\n\n English sentences...\n");
		WordNetQuery.INSTANCE.disableDict();
		VerbaliseTreeManager.setLocale(Locale.GERMAN);
		SentenceOWLObjectVisitor sentenceOWLObjectVisit = new SentenceOWLObjectVisitor();
		
		OWLSubClassOfAxiom subcl1 =  dataFactory.getOWLSubClassOfAxiom(class1,class2);
		System.out.println(subcl1.accept(sentenceOWLObjectVisit));
		
		OWLSubClassOfAxiom subcl2 =  dataFactory.getOWLSubClassOfAxiom(class1,dataFactory.getOWLObjectSomeValuesFrom(prop1,class2));
		System.out.println(subcl2.accept(sentenceOWLObjectVisit));
		
		OWLSubClassOfAxiom subcl3 =  dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLObjectSomeValuesFrom(prop1,class1),class2);
		System.out.println(subcl3.accept(sentenceOWLObjectVisit));
		
		OWLSubClassOfAxiom subcl4 =  dataFactory.getOWLSubClassOfAxiom(class1,dataFactory.getOWLObjectIntersectionOf(class2,class3));
		System.out.println(subcl4.accept(sentenceOWLObjectVisit));
		
		OWLSubClassOfAxiom subcl5 =  dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLObjectIntersectionOf(class1,class2),class3);
		System.out.println(subcl5.accept(sentenceOWLObjectVisit));
		
		
		List<Pair> lst = new ArrayList<Pair>();
		Pair p1 = new Pair(1,2);
		Pair p2 = new Pair(2,3);
		Pair p3 = new Pair(3,4);
		
		
		lst.add(p3);
		lst.add(p1);
		lst.add(p2);
		AdditionalDLRules.findAllChains(lst);
		
		
	}
	
	
	
}
