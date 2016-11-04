package org.semanticweb.cogExp.PrettyPrint;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;


/** Pretty printing for OWLAxioms (OWL-API)
 * 
 * @author marvin
 *
 */
public class PrettyPrintOWLAxiomVisitor implements OWLAxiomVisitorEx<String>{

	private PrettyPrintClassExpressionVisitor ppClassExpressionVisit = new PrettyPrintClassExpressionVisitor();
	private PrettyPrintPropertyExpressionVisitor ppPropertyExpressionVisit = new PrettyPrintPropertyExpressionVisitor();
	private PrettyPrintIndividualVisitor ppIndividualVisit = new PrettyPrintIndividualVisitor();
	
    public static final char SUBCLSYMB =  Character.toChars(0x2291)[0];
    public static final char EQUIVSYMB =  Character.toChars(0x2261)[0];
    public static final char INTSYMB =  Character.toChars(0x2293)[0];
    public static final char UNIONSYMB =  Character.toChars(0x2294)[0];
    public static final char EXISTSSYMB =  Character.toChars(0x2203)[0];
    public static final char FORALLSYMB =  Character.toChars(0x2200)[0];
    public static final char CIRCSYMB =  Character.toChars(0x25CB)[0];
	
public String visit(OWLSubAnnotationPropertyOfAxiom arg0) {
	OWLAnnotationProperty subProp = arg0.getSubProperty();
	OWLAnnotationProperty supProp = arg0.getSuperProperty();
	return "subAnnot(" + subProp + "," + supProp + ")";
}

public String visit(OWLAnnotationPropertyDomainAxiom arg0) {
	IRI dom = arg0.getDomain();
	OWLAnnotationProperty prop = arg0.getProperty();
	return "annPropDom(" + prop + "," + dom + ")";
}

public String visit(OWLAnnotationPropertyRangeAxiom arg0) {
	IRI ran = arg0.getRange();
	OWLAnnotationProperty prop = arg0.getProperty();
	return "annPropRan(" + prop + "," + ran + ")";
}

public String visit(OWLSubClassOfAxiom arg0) {
	return  arg0.getSubClass().accept(ppClassExpressionVisit)  
			+ SUBCLSYMB
			+  arg0.getSuperClass().accept(ppClassExpressionVisit);
}

public String visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
	OWLObjectPropertyExpression prop = arg0.getProperty();
	OWLIndividual indiv = arg0.getObject();
	return "negObjPropAss(" + prop.accept(ppPropertyExpressionVisit) 
			+ "," 
			+ indiv.accept(ppIndividualVisit) + ")";
}

public String visit(OWLAsymmetricObjectPropertyAxiom arg0) {
	return "asymmetric(" 
			+ arg0.getProperty().accept(ppPropertyExpressionVisit) 
			+ ")";
}

public String visit(OWLReflexiveObjectPropertyAxiom arg0) {
	return "reflexive(" 
			+ arg0.getProperty().accept(ppPropertyExpressionVisit) 
			+ ")";
}

public String visit(OWLDisjointClassesAxiom arg0) {
	List<OWLClassExpression> expressions = arg0.getClassExpressionsAsList();
	String result = "disjoint(";
	boolean first = true;
	for (OWLClassExpression expr : expressions){
		if (!first){
			result = result + ",";
		}
		else 
		{
			first = false;
		}
		result = result + expr.accept(ppClassExpressionVisit);
	}
	result = result + ")";
	return result;
}

public String visit(OWLDataPropertyDomainAxiom arg0) {
	return "domain(" +  
			arg0.getProperty().accept(ppPropertyExpressionVisit)
			+  "," 
			+ arg0.getDomain().accept(ppClassExpressionVisit) 
			+ ")";
}

public String visit(OWLObjectPropertyDomainAxiom arg0) {
	return "domain(" +  
			arg0.getProperty().accept(ppPropertyExpressionVisit) 
			+  "," 
			+ arg0.getDomain().accept(ppClassExpressionVisit) 
			+ ")";
}

public String visit(OWLEquivalentObjectPropertiesAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLNegativeDataPropertyAssertionAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLDifferentIndividualsAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLDisjointDataPropertiesAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLDisjointObjectPropertiesAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLObjectPropertyRangeAxiom arg0) {
	return "range(" +  arg0.getProperty().accept(ppPropertyExpressionVisit) 
			+  "," + arg0.getRange().accept(ppClassExpressionVisit) + ")";
}

public String visit(OWLObjectPropertyAssertionAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLFunctionalObjectPropertyAxiom arg0) {
	return "functional(" + arg0.getProperty().accept(ppPropertyExpressionVisit) + ")";
}

public String visit(OWLSubObjectPropertyOfAxiom arg0) {
	return arg0.getSubProperty().accept(ppPropertyExpressionVisit) 
			+  SUBCLSYMB + arg0.getSuperProperty().accept(ppPropertyExpressionVisit);
}

public String visit(OWLDisjointUnionAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLDeclarationAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLAnnotationAssertionAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLSymmetricObjectPropertyAxiom arg0) {
	return "symmetricobjectproperty(" + arg0.getProperty().accept(ppPropertyExpressionVisit) +")";
}

public String visit(OWLDataPropertyRangeAxiom arg0) {
	return "datapropertyrange(" + arg0.getProperty().accept(ppPropertyExpressionVisit) 
			+"," + arg0.getRange() + ")";
}

public String visit(OWLFunctionalDataPropertyAxiom arg0) {
	return "functional(" + arg0.getProperty().accept(ppPropertyExpressionVisit) + ")";
}

public String visit(OWLEquivalentDataPropertiesAxiom arg0) {
	String resultstring = "";
	Set<OWLDataPropertyExpression> exprs =  ((OWLEquivalentDataPropertiesAxiom) arg0).getProperties();
    boolean firstp = true;
	for (OWLDataPropertyExpression exp : exprs){
		if (!firstp) {resultstring = resultstring + EQUIVSYMB;}
		firstp = false;
		resultstring = resultstring + ppPropertyExpressionVisit.visit(exp);
	}
	return resultstring;
}

public String visit(OWLClassAssertionAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLEquivalentClassesAxiom arg0) {
	String resultstring = "";
	List<OWLClassExpression> exprs =  ((OWLEquivalentClassesAxiom) arg0).getClassExpressionsAsList();
    boolean firstp = true;
	for (OWLClassExpression exp : exprs){
		if (!firstp) {resultstring = resultstring + EQUIVSYMB;}
		firstp = false;
		resultstring = resultstring + exp.accept(ppClassExpressionVisit);
	}
	return resultstring;
}

public String visit(OWLDataPropertyAssertionAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLTransitiveObjectPropertyAxiom arg0) {
	return "transitive (" 
			+ arg0.getProperty().getNamedProperty().getIRI().getFragment() 
			+ ")";
}

public String visit(OWLIrreflexiveObjectPropertyAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLSubDataPropertyOfAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLInverseFunctionalObjectPropertyAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLSameIndividualAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLSubPropertyChainOfAxiom arg0) {
	String result = "";
	List<OWLObjectPropertyExpression> propertylist = arg0.getPropertyChain();
	OWLObjectPropertyExpression superexpression = arg0.getSuperProperty();
	boolean needscirc = false;
	for (OWLObjectPropertyExpression expr : propertylist){
		String exprString = expr.accept(ppPropertyExpressionVisit);
		if (needscirc)
			result = result + CIRCSYMB;
		needscirc=true;
		result = result + exprString;
	}
	result = result + SUBCLSYMB + superexpression.accept(ppPropertyExpressionVisit);
	return result;
}

public String visit(OWLInverseObjectPropertiesAxiom arg0) {
	Set<OWLObjectPropertyExpression> props = arg0.getProperties();
	String result = "inverses(";
	boolean first = true;
	for (OWLObjectPropertyExpression expr : props){
		if (!first){
			result = result + ",";
		}
		else 
		{
			first = false;
		}
		result = result + expr.accept(ppPropertyExpressionVisit);
	}
	return result + ")";
}

public String visit(OWLHasKeyAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(OWLDatatypeDefinitionAxiom arg0) {
	// TODO Auto-generated method stub
	return null;
}

public String visit(SWRLRule arg0) {
	// TODO Auto-generated method stub
	return null;
}

}
