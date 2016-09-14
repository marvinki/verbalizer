package tests;

import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class ConversionAndPrettyPrintingTest {
	public static void main(String[] args) {
		
		OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
		OWLClass class1 = dataFactory.getOWLClass(IRI.create("http://foo#" + "Class1"));
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
		System.out.println(VerbalisationManager.INSTANCE.prettyPrint(cardExpr1_b));
		
		// Data Exact Cardinality
		
		OWLFormula cardForm2 = ConversionManager.fromOWLAPI(cardExpr2);
		OWLObject cardExpr2_b = ConversionManager.toOWLAPI(cardForm2);
				
		System.out.println(cardForm2);
		System.out.println(VerbalisationManager.INSTANCE.prettyPrint(cardExpr2_b));
		
		
		
	}
}
