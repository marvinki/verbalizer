package org.semanticweb.cogExp.FormulaConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.cogExp.OWLFormulas.OWLClassName;
import org.semanticweb.cogExp.OWLFormulas.OWLDataRan;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.OWLFormulas.OWLIndividualName;
import org.semanticweb.cogExp.OWLFormulas.OWLInteger;
import org.semanticweb.cogExp.OWLFormulas.OWLLiteralType;
import org.semanticweb.cogExp.OWLFormulas.OWLLiteralValue;
import org.semanticweb.cogExp.OWLFormulas.OWLRoleName;
import org.semanticweb.cogExp.OWLFormulas.OWLSymb;
import org.semanticweb.owlapi.model.AxiomType;
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
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;
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
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
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
import org.semanticweb.owlapi.model.OWLIndividual;
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
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
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


public class ConvertOWLObjectToOWLFormulaVisitor implements OWLObjectVisitorEx<OWLFormula>{

	public OWLFormula visit(OWLClass cl) {
		String resultstring = cl.getIRI().getFragment();
		OWLClassName name = new OWLClassName(resultstring,cl.getIRI().toString());
		// System.out.println("DEBUG -- NAME: " + name);
		OWLFormula form = new OWLFormula(name);
		if (cl.isOWLThing())
				form = OWLFormula.createFormulaTop();
			if (cl.isOWLNothing())
				form = OWLFormula.createFormulaBot();
		return form;
	}

	public OWLFormula visit(OWLObjectIntersectionOf in) {
		Set<OWLClassExpression> args = in.getOperands();
		ArrayList<OWLFormula> tail =  new ArrayList<OWLFormula>();
		for (OWLClassExpression c : args){
			tail.add(c.accept(this));				// recursion
		}
		return new OWLFormula(OWLSymb.INT,tail);
	}

	public OWLFormula visit(OWLObjectUnionOf arg0) {
		Set<OWLClassExpression> args = arg0.getOperands();
		ArrayList<OWLFormula> tail =  new ArrayList<OWLFormula>();
		for (OWLClassExpression c : args){
			tail.add(c.accept(this));				// recursion
		}
		return new OWLFormula(OWLSymb.UNION,tail);
	}

	public OWLFormula visit(OWLObjectComplementOf arg0) {
		ArrayList<OWLFormula> tail =  new ArrayList<OWLFormula>();
		tail.add(arg0.getOperand().accept(this));	
		return new OWLFormula(OWLSymb.NEG,tail);
	}

	public OWLFormula visit(OWLObjectSomeValuesFrom some) {
		return OWLFormula.createFormula(OWLSymb.EXISTS,
				some.getProperty().accept(this),
				some.getFiller().accept(this));	
	}

	public OWLFormula visit(OWLObjectAllValuesFrom forall) {
		return OWLFormula.createFormula(OWLSymb.FORALL,
				forall.getProperty().accept(this),
				forall.getFiller().accept(this));	
	}

	public OWLFormula visit(OWLObjectHasValue ce) {
		// System.out.println("DEBUG visit OWLObject has value " + ce);
		OWLObjectHasValue odhv = (OWLObjectHasValue) ce;
		OWLObjectPropertyExpression prop = odhv.getProperty();
		OWLIndividual indiv = odhv.getValue();
		OWLFormula formula = OWLFormula.createFormulaObjectProperty(prop.asOWLObjectProperty().getIRI().getFragment(),
									      prop.asOWLObjectProperty().getIRI().toString(),
										  indiv.asOWLNamedIndividual().getIRI().getFragment(),
										  indiv.asOWLNamedIndividual().getIRI().toString()
										  );
		// System.out.println("returning " + formula);
		return formula;
	}

	public OWLFormula visit(OWLObjectMinCardinality card) {
		int cardinality = card.getCardinality();
		OWLFormula cardform = new OWLFormula(new OWLInteger(cardinality));
		OWLClassExpression filler = card.getFiller();
		OWLObjectPropertyExpression property = card.getProperty();
		return OWLFormula.createFormula(OWLSymb.OBJECTMINCARDINALITY,
				cardform,property.accept(this),filler.accept(this));
	}

	public OWLFormula visit(OWLObjectExactCardinality card) {
		int cardinality = card.getCardinality();
		OWLFormula cardform = new OWLFormula(new OWLInteger(cardinality));
		OWLClassExpression filler = card.getFiller();
		OWLObjectPropertyExpression property = card.getProperty();
		return OWLFormula.createFormula(OWLSymb.OBJECTEXACTCARDINALITY,
				cardform,property.accept(this),filler.accept(this));
	}

	public OWLFormula visit(OWLObjectMaxCardinality card) {
		int cardinality = card.getCardinality();
		OWLFormula cardform = new OWLFormula(new OWLInteger(cardinality));
		OWLClassExpression filler = card.getFiller();
		OWLObjectPropertyExpression property = card.getProperty();
		return OWLFormula.createFormula(OWLSymb.OBJECTMAXCARDINALITY,
				cardform,property.accept(this),filler.accept(this));	
	}

	public OWLFormula visit(OWLObjectHasSelf arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLObjectOneOf oneof) {
		System.out.println("construct OWL Formula oneof " + oneof);
		Set<OWLIndividual> individuals = oneof.getIndividuals();
		List<OWLFormula> individualnames = new ArrayList<OWLFormula>();
		for (OWLIndividual indiv : individuals){
			OWLFormula newindividiualFormula = indiv.accept(this);
			individualnames.add(newindividiualFormula);
		}
		return OWLFormula.createFormula(OWLSymb.ONEOF,individualnames);
	}

	public OWLFormula visit(OWLDataSomeValuesFrom arg0) {
		OWLDataPropertyExpression prop = arg0.getProperty();
		OWLDataRange filler = arg0.getFiller();
		OWLFormula formulaprop = OWLFormula.createFormulaDataProperty(prop.asOWLDataProperty().getIRI().getFragment(), 
				prop.asOWLDataProperty().getIRI().toString());
		OWLFormula formulafiller = filler.accept(this);
		System.out.println("returning " + OWLFormula.createFormula(OWLSymb.DATASOMEVALUESFROM,formulaprop, formulafiller));
		return OWLFormula.createFormula(OWLSymb.DATASOMEVALUESFROM,formulaprop, formulafiller);
	}

	public OWLFormula visit(OWLDataAllValuesFrom arg0) {
		OWLDataPropertyExpression prop = arg0.getProperty();
		OWLDataRange filler = arg0.getFiller();
		OWLFormula formulaprop = OWLFormula.createFormulaDataProperty(prop.asOWLDataProperty().getIRI().getFragment(), 
				prop.asOWLDataProperty().getIRI().toString());
		OWLFormula formulafiller = filler.accept(this);
		System.out.println("returning " + OWLFormula.createFormula(OWLSymb.DATAALLVALUESFROM,formulaprop, formulafiller));
		return OWLFormula.createFormula(OWLSymb.DATAALLVALUESFROM,formulaprop, formulafiller);
	}

	public OWLFormula visit(OWLDataHasValue odhv) {
		System.out.println("DEBUG!  conversion visiting " + odhv);
		OWLDataPropertyExpression prop = odhv.getProperty();
		OWLLiteral literal = odhv.getValue();
		prop.asOWLDataProperty().getIRI();
		OWLFormula formula = OWLFormula.createFormulaDataProperty(prop.asOWLDataProperty().getIRI().getFragment(),
										 prop.asOWLDataProperty().getIRI().toString(),
										 literal.getLiteral(),
										 ConversionManager.datatypeToLiteraltype(literal.getDatatype()));
		System.out.println("DEBUG!  conversion returning " + formula);
		return formula;
	}

	public OWLFormula visit(OWLDataMinCardinality arg0) {
		int cardinality = arg0.getCardinality();
		OWLFormula cardform = new OWLFormula(new OWLInteger(cardinality));
		OWLDataRange filler = arg0.getFiller();
		OWLDataPropertyExpression property = arg0.getProperty();
		return OWLFormula.createFormula(OWLSymb.DATAMINCARDINALITY,
				cardform,property.accept(this),filler.accept(this));
	}

	public OWLFormula visit(OWLDataExactCardinality arg0) {
		int cardinality = arg0.getCardinality();
		OWLFormula cardform = new OWLFormula(new OWLInteger(cardinality));
		OWLDataRange filler = arg0.getFiller();
		OWLDataRan dataran = ConversionManager.datarangeToDataran(filler);
		OWLFormula dataranForm = new OWLFormula(dataran);
		OWLDataPropertyExpression property = arg0.getProperty();
		return OWLFormula.createFormula(OWLSymb.DATAEXACTCARDINALITY,
				cardform,property.accept(this),dataranForm);
	}

	public OWLFormula visit(OWLDataMaxCardinality arg0) {
		int cardinality = arg0.getCardinality();
		OWLFormula cardform = new OWLFormula(new OWLInteger(cardinality));
		OWLDataRange filler = arg0.getFiller();
		OWLDataPropertyExpression property = arg0.getProperty();
		return OWLFormula.createFormula(OWLSymb.DATAMAXCARDINALITY,
				cardform,property.accept(this),filler.accept(this));
	}

	public OWLFormula visit(OWLSubClassOfAxiom axiom) {
		return OWLFormula.createFormula(OWLSymb.SUBCL,
				axiom.getSubClass().accept(this),
				axiom.getSuperClass().accept(this)
				);
	}

	public OWLFormula visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLAsymmetricObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLReflexiveObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDisjointClassesAxiom axiom) {
		List<OWLClassExpression> exprs = axiom.getClassExpressionsAsList();
		ArrayList<OWLFormula> tail =  new ArrayList<OWLFormula>();
		for (OWLClassExpression exp : exprs){
			tail.add(exp.accept(this));
		}
		return new OWLFormula(OWLSymb.DISJ,tail);
	}

	public OWLFormula visit(OWLDataPropertyDomainAxiom ax) {
		OWLFormula f3 = OWLFormula.createFormula(OWLSymb.DATADOMAIN,
				ax.getProperty().accept(this),
				ax.getDomain().accept(this)
				);
		return f3;
	}

	public OWLFormula visit(OWLObjectPropertyDomainAxiom ax) {
		OWLFormula f3 = OWLFormula.createFormula(OWLSymb.DOMAIN,
				ax.getProperty().accept(this),
				ax.getDomain().accept(this));
		// System.out.println(f2);
		return f3;
	}

	public OWLFormula visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		Set<OWLObjectPropertyExpression> exprs = axiom.getProperties();
		ArrayList<OWLFormula> tail =  new ArrayList<OWLFormula>();
		for (OWLObjectPropertyExpression exp : exprs){
			tail.add(exp.accept(this));
		}
		return new OWLFormula(OWLSymb.EQUIVOBJPROP,tail);
	}

	public OWLFormula visit(OWLNegativeDataPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDifferentIndividualsAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDisjointDataPropertiesAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDisjointObjectPropertiesAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLObjectPropertyRangeAxiom ax) {
		OWLFormula f = OWLFormula.createFormula(OWLSymb.RANGE,
				ax.getProperty().accept(this),
				ax.getRange().accept(this));
		// System.out.println(f2);
		return f;
	}

	public OWLFormula visit(OWLObjectPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLFunctionalObjectPropertyAxiom arg0) {
		// System.out.println("creating " + OWLFormula.createFormula(OWLSymb.FUNCTIONAL, 
		// 		arg0.getProperty().accept(this)));
		return OWLFormula.createFormula(OWLSymb.FUNCTIONAL, 
				arg0.getProperty().accept(this));
	}

	public OWLFormula visit(OWLSubObjectPropertyOfAxiom ax) {
		OWLFormula f3 = OWLFormula.createFormula(OWLSymb.SUBPROPERTYOF,
				ax.getSubProperty().accept(this),
				ax.getSuperProperty().accept(this));
		// System.out.println(" CREATED " + f3);
		return f3;
	}

	public OWLFormula visit(OWLDisjointUnionAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDeclarationAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLAnnotationAssertionAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLSymmetricObjectPropertyAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDataPropertyRangeAxiom arg0) {
		OWLDataRan ran = ConversionManager.datarangeToDataran(arg0.getRange());
		return OWLFormula.createFormulaDataRan(ran);
	}

	public OWLFormula visit(OWLFunctionalDataPropertyAxiom arg0) {
		 // System.out.println("creating " + OWLFormula.createFormula(OWLSymb.FUNCTIONAL, 
		// 				arg0.getProperty().accept(this)));
		return OWLFormula.createFormula(OWLSymb.FUNCTIONALDATA, 
				arg0.getProperty().accept(this));
	}

	public OWLFormula visit(OWLEquivalentDataPropertiesAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLClassAssertionAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLEquivalentClassesAxiom axiom) {
		List<OWLClassExpression> exprs = axiom.getClassExpressionsAsList();
		ArrayList<OWLFormula> tail =  new ArrayList<OWLFormula>();
		for (OWLClassExpression exp : exprs){
			tail.add(exp.accept(this));
		}
		return new OWLFormula(OWLSymb.EQUIV,tail);
	}

	public OWLFormula visit(OWLDataPropertyAssertionAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLTransitiveObjectPropertyAxiom ax) {
			OWLFormula f2 = OWLFormula.createFormula(OWLSymb.TRANSITIVE,
					ax.getProperty().accept(this));
			return f2;
	}

	public OWLFormula visit(OWLIrreflexiveObjectPropertyAxiom ax) {
		OWLFormula f2 = OWLFormula.createFormula(OWLSymb.IRREFLEXIVE,ax.getProperty().accept(this));
		return f2;
		
	}

	public OWLFormula visit(OWLSubDataPropertyOfAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLInverseFunctionalObjectPropertyAxiom ax) {
		OWLFormula f2 = OWLFormula.createFormula(OWLSymb.INVFUNCTIONAL,
				ax.getProperty().accept(this));
		return f2;
	}

	public OWLFormula visit(OWLSameIndividualAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLSubPropertyChainOfAxiom ax) {
		List<OWLObjectPropertyExpression> propertychain = ax.getPropertyChain();
		OWLObjectPropertyExpression superproperty = ax.getSuperProperty();
		List<OWLFormula> propformulachain = new ArrayList<OWLFormula>();
		for(OWLObjectPropertyExpression propexpr : propertychain){
			propformulachain.add(propexpr.accept(this));
		}
		OWLFormula f2 = OWLFormula.createFormula(OWLSymb.SUBPROPERTYOF,
				OWLFormula.createFormula(OWLSymb.SUBPROPERTYCHAIN,propformulachain),superproperty.accept(this));
		// System.out.println("DEBUG CONVERTER CHAIN " + f2);
		return f2;
	}

	public OWLFormula visit(OWLInverseObjectPropertiesAxiom ax) {
					OWLFormula f3 = OWLFormula.createFormula(OWLSymb.INVERSEOBJPROP,
							ax.getFirstProperty().accept(this),
							ax.getSecondProperty().accept(this));
					// System.out.println(f2);
					return f3;
	}

	public OWLFormula visit(OWLHasKeyAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDatatypeDefinitionAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLRule arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLSubAnnotationPropertyOfAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLAnnotationPropertyDomainAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLAnnotationPropertyRangeAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDatatype arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDataComplementOf arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDataOneOf arg0) {
		System.out.println("convert dataoneof " + arg0);
		Set<OWLLiteral> individuals = arg0.getValues();
		List<OWLFormula> literalformulas = new ArrayList<OWLFormula>();
		for (OWLLiteral indiv : individuals){
			OWLFormula newindividiualFormula = indiv.accept(this);
			literalformulas.add(newindividiualFormula);
		}
		return OWLFormula.createFormula(OWLSymb.DATAONEOF,literalformulas);
	}

	public OWLFormula visit(OWLDataIntersectionOf arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDataUnionOf arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDatatypeRestriction arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLLiteral arg0) {
		OWLLiteralType type = ConversionManager.datatypeToLiteraltype(arg0.getDatatype());
		OWLLiteralValue lit = new OWLLiteralValue(arg0.getLiteral(),type);
		OWLFormula litformula = new OWLFormula(lit);
		return litformula;
	}

	public OWLFormula visit(OWLFacetRestriction arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLObjectProperty expr) {
		OWLFormula form = OWLFormula.createFormulaRole(expr.getNamedProperty().getIRI().getFragment(), 
				expr.getNamedProperty().getIRI().toString());
		return form;
	}

	public OWLFormula visit(OWLObjectInverseOf arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLDataProperty arg0) {
		return OWLFormula.createFormulaDataProperty(arg0.getIRI().getFragment(),arg0.getIRI().toString());
	}

	public OWLFormula visit(OWLNamedIndividual indiv) {
		OWLIndividualName name = new OWLIndividualName(indiv.getIRI().getFragment(),indiv.getIRI().toString());
		return new OWLFormula(name);
	}

	public OWLFormula visit(OWLAnnotationProperty arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLAnnotation arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(IRI arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLAnonymousIndividual arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLClassAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLDataRangeAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLObjectPropertyAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLDataPropertyAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLBuiltInAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLVariable arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLIndividualArgument arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLLiteralArgument arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLSameIndividualAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(SWRLDifferentIndividualsAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public OWLFormula visit(OWLOntology arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
