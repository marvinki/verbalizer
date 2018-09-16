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

package org.semanticweb.cogExp.FormulaConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.cogExp.OWLAPIVerbaliser.OWLAPIManagerManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLFormulas.OWLAtom;
import org.semanticweb.cogExp.OWLFormulas.OWLClassName;
import org.semanticweb.cogExp.OWLFormulas.OWLDataRan;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.OWLFormulas.OWLIndividualName;
import org.semanticweb.cogExp.OWLFormulas.OWLInteger;
import org.semanticweb.cogExp.OWLFormulas.OWLLiteralType;
import org.semanticweb.cogExp.OWLFormulas.OWLLiteralValue;
import org.semanticweb.cogExp.OWLFormulas.OWLRoleName;
import org.semanticweb.cogExp.OWLFormulas.OWLDataPropertyName;
import org.semanticweb.cogExp.OWLFormulas.OWLObjectPropertyName;
import org.semanticweb.cogExp.OWLFormulas.OWLSymb;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintOWLObjectVisitor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

public enum ConversionManager {
	INSTANCE;
	
	public static List<OWLObject> toOWLAPI(List<OWLFormula> formulas){
		List<OWLObject> newlist = new ArrayList<OWLObject>();
		for (OWLFormula form : formulas){
			newlist.add(toOWLAPI(form));
		}
		return newlist;
	}
	
	private static ConvertOWLObjectToOWLFormulaVisitor fromOWLAPIvisitor = new ConvertOWLObjectToOWLFormulaVisitor();
	
	/** Translates OWLAPI OWLObjects to (internal) OWLFormula representation
	 * 
	 * @param owlobject 			an OWLObject from OWLAPI
	 * @return the corresponding OWLFormula object
	 */
	public static OWLFormula fromOWLAPI(OWLObject owlobject){
		return owlobject.accept(fromOWLAPIvisitor);
	}
	
	/** converts the OWLFormula to OWL-API formula 
	 * 
	 * @param formula		TODO add description	
	 * @return	the formula as (OWL-API) OWLObject
	 * 
	 */
	
	/*
	 * TODO add description
	 */
	public static OWLObject toOWLAPI(OWLFormula formula){
		OWLAtom head = formula.getHead();
		List<OWLFormula> tail = formula.getArgs();
		// System.out.println("TRANSFORMING:  " + this);
		OWLObject result;
		// OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		if (head.isSymb()){
			switch ((OWLSymb) head){
			case EQUIV:  	
				OWLClassExpression class1e = (OWLClassExpression) toOWLAPI(tail.get(0));	
				OWLClassExpression class2e = (OWLClassExpression) toOWLAPI(tail.get(1));
				result = dataFactory.getOWLEquivalentClassesAxiom(class1e, class2e);
				break;
			case SUBCL:  	
				OWLClassExpression class1 = (OWLClassExpression) toOWLAPI(tail.get(0));
				// System.out.println(class1);
				OWLClassExpression class2 = (OWLClassExpression) toOWLAPI(tail.get(1));
				// System.out.println(class2);
				result = dataFactory.getOWLSubClassOfAxiom(class1, class2);
				break;
			case INT: 
				// System.out.println("case inter!");
				// System.out.println("head " + head + "   tail: " + tail);
				// OWLClassExpression class11 = (OWLClassExpression) tail.get(0).toOWLAPI();
				// OWLClassExpression class12 = (OWLClassExpression) tail.get(1).toOWLAPI();
				// result = dataFactory.getOWLObjectIntersectionOf(class11, class12);
				Set<OWLClassExpression> exprs = new TreeSet<OWLClassExpression>();
				for (int i = 0; i<tail.size();i++){
					OWLClassExpression ce = (OWLClassExpression) toOWLAPI(tail.get(i));
					exprs.add(ce);
				}
				result = dataFactory.getOWLObjectIntersectionOf(exprs);
				break;
			case UNION:  
				// System.out.println("case union!");
				// OWLClassExpression class21 = (OWLClassExpression) tail.get(0).toOWLAPI();
				// OWLClassExpression class22 = (OWLClassExpression) tail.get(1).toOWLAPI();
				// result = dataFactory.getOWLObjectUnionOf(class21, class22);
				Set<OWLClassExpression> exprs2 = new TreeSet<OWLClassExpression>();
				for (int i = 0; i<tail.size();i++){
					OWLClassExpression ce = (OWLClassExpression) toOWLAPI(tail.get(i));
					exprs2.add(ce);
				}
				result = dataFactory.getOWLObjectUnionOf(exprs2);
				break;	
			case EXISTS:
				// System.out.println("Propertytail: " + tail.get(0));
				// System.out.println("Expressiontail" + tail.get(1));
				// System.out.println("Propertytail translated: " + toOWLAPI(tail.get(0)));
				// System.out.println("Expressiontail translated: " +  toOWLAPI(tail.get(1)));
				OWLObject propertyobject = toOWLAPI(tail.get(0));
				// System.out.println("Propertyobject: " +  propertyobject);
				OWLObjectProperty rname1 = (OWLObjectProperty) propertyobject;
				OWLObject classobject = toOWLAPI(tail.get(1));
				// System.out.println("Classobject: " +  classobject);
				OWLClassExpression cname1 = (OWLClassExpression) classobject;
				result = dataFactory.getOWLObjectSomeValuesFrom(rname1, cname1);
				// System.out.println(" Result " + result);
				// System.out.println("got there!");
				break;	
			case FORALL:
				// System.out.println("case forall!");
				OWLObjectProperty rname2 = (OWLObjectProperty) toOWLAPI(tail.get(0));
				OWLClassExpression cname2 = (OWLClassExpression) toOWLAPI(tail.get(1));
				result = dataFactory.getOWLObjectAllValuesFrom(rname2, cname2);
				break;	
			case TOP:
				result = dataFactory.getOWLThing();
				break;	
			case TRANSITIVE:
				OWLObjectProperty rname3 = (OWLObjectProperty) toOWLAPI(tail.get(0));
				result = dataFactory.getOWLTransitiveObjectPropertyAxiom(rname3);
				break;
			case FUNCTIONAL:
				OWLObjectProperty rname4= (OWLObjectProperty) toOWLAPI(tail.get(0));
				result = dataFactory.getOWLFunctionalObjectPropertyAxiom(rname4);
				break;
			case FUNCTIONALDATA:
				OWLDataProperty rname5= (OWLDataProperty) toOWLAPI(tail.get(0));
				result = dataFactory.getOWLFunctionalDataPropertyAxiom(rname5);
				break;
			case NEG:
				// System.out.println("case neg");
				OWLClassExpression classexp = (OWLClassExpression) toOWLAPI(tail.get(0));
				result = classexp.getObjectComplementOf();
				break;	
			case DATAHASVALUE:
				OWLFormula propname = (OWLFormula) tail.get(0);
				OWLDataPropertyExpression prop = (OWLDataPropertyExpression) toOWLAPI((OWLDataPropertyName) propname.getHead());
				OWLAtom tailhead = tail.get(1).getHead();
				// System.out.println("tailhead " + tailhead);
				OWLLiteralValue literalvalue = (OWLLiteralValue) tailhead;
				OWLLiteral lit = (OWLLiteral) toOWLAPI(literalvalue);
				OWLDataHasValue expr = dataFactory.getOWLDataHasValue(prop,lit);
				result = expr;
				break;
			case OBJECTHASVALUE:
				OWLFormula propname2 = (OWLFormula) tail.get(0);
				// System.out.println("DEBUG " + propname2);
				OWLObjectPropertyExpression prop2 = (OWLObjectPropertyExpression) toOWLAPI(((OWLObjectPropertyName) propname2.getHead()));
				// System.out.println("DEBUG (2) " + prop2);
				OWLIndividualName indiv = (OWLIndividualName) tail.get(1).getHead();
				OWLIndividual ni = (OWLIndividual) toOWLAPI(indiv);
				OWLObjectHasValue expr2 = dataFactory.getOWLObjectHasValue(prop2,ni);
				result = expr2;
				break;
			case DISJ:
				// TODO: Attention, this only handles the binary case!
				// System.out.println("case neg");
				OWLClassExpression classexp1 = (OWLClassExpression) toOWLAPI(tail.get(0));
				OWLClassExpression classexp2 = (OWLClassExpression) toOWLAPI(tail.get(1));
				result = dataFactory.getOWLDisjointClassesAxiom(classexp1,classexp2);
				break;
			case DOMAIN:
				OWLObjectPropertyExpression propD = (OWLObjectPropertyExpression) toOWLAPI(tail.get(0));
				OWLClassExpression classD = (OWLClassExpression) toOWLAPI(tail.get(1));
				result = dataFactory.getOWLObjectPropertyDomainAxiom(propD,classD);
				break;	
			case RANGE:
				OWLObjectPropertyExpression propR = (OWLObjectPropertyExpression) toOWLAPI(tail.get(0));
				OWLClassExpression classR = (OWLClassExpression) toOWLAPI(tail.get(1));
				result = dataFactory.getOWLObjectPropertyRangeAxiom(propR,classR);
				break;	
			case OBJECTEXACTCARDINALITY:
				OWLObjectPropertyExpression propEx = (OWLObjectPropertyExpression) toOWLAPI(tail.get(1));
				OWLFormula owlintform = tail.get(0);
				OWLInteger owlint = (OWLInteger) owlintform.getHead();
				int cardinality = owlint.getValue();
				result = dataFactory.getOWLObjectExactCardinality(cardinality, propEx);
				break;
			case OBJECTMINCARDINALITY:
				OWLObjectPropertyExpression propEx2 = (OWLObjectPropertyExpression) toOWLAPI(tail.get(1));
				OWLFormula owlintform2 = tail.get(0);
				OWLInteger owlint2 = (OWLInteger) owlintform2.getHead();
				int cardinality2 = owlint2.getValue();
				result = dataFactory.getOWLObjectMinCardinality(cardinality2, propEx2);
				break;
			case OBJECTMAXCARDINALITY:
				OWLObjectPropertyExpression propEx3 = (OWLObjectPropertyExpression) toOWLAPI(tail.get(1));
				OWLFormula owlintform3 = tail.get(0);
				OWLInteger owlint3 = (OWLInteger) owlintform3.getHead();
				int cardinality3 = owlint3.getValue();
				result = dataFactory.getOWLObjectMaxCardinality(cardinality3, propEx3);
				break;
			case DATAEXACTCARDINALITY:
				OWLDataPropertyExpression datapropEx = (OWLDataPropertyExpression) toOWLAPI(tail.get(1));
				OWLFormula owlintform4 = tail.get(0);
				OWLInteger owlint4 = (OWLInteger) owlintform4.getHead();
				int cardinality4 = owlint4.getValue();
				result = dataFactory.getOWLDataExactCardinality(cardinality4, datapropEx);
				break;
			case DATAMINCARDINALITY:
				OWLDataPropertyExpression datapropEx2 = (OWLDataPropertyExpression) toOWLAPI(tail.get(1));
				OWLFormula owlintform5 = tail.get(0);
				OWLInteger owlint5 = (OWLInteger) owlintform5.getHead();
				int cardinality5 = owlint5.getValue();
				result = dataFactory.getOWLDataExactCardinality(cardinality5, datapropEx2);
				break;
			case DATAMAXCARDINALITY:
				OWLDataPropertyExpression datapropEx3 = (OWLDataPropertyExpression) toOWLAPI(tail.get(1));
				OWLFormula owlintform6 = tail.get(0);
				OWLInteger owlint6 = (OWLInteger) owlintform6.getHead();
				int cardinality6 = owlint6.getValue();
				result = dataFactory.getOWLDataMaxCardinality(cardinality6, datapropEx3);
                                break;
			case CLASSASSERTION:
				OWLClassExpression classexpCA = (OWLClassExpression) toOWLAPI(tail.get(0));
				OWLNamedIndividual indivCA = (OWLNamedIndividual) toOWLAPI(tail.get(1));
				result = dataFactory.getOWLClassAssertionAxiom(classexpCA, indivCA);
				break;
			case ONEOF:
				Set<OWLIndividual> individuallist = new HashSet<OWLIndividual>();
				// System.out.println("tail " + tail);
				for (OWLFormula form : tail){
					OWLIndividual individ = (OWLIndividual) toOWLAPI(form);
					individuallist.add(individ);
				}
				result = dataFactory.getOWLObjectOneOf(individuallist);
				// System.out.println("oneof conversion " + result);
				break;
			case OBJECTPROPERTYASSERTION:
				OWLObjectPropertyExpression propExp = (OWLObjectPropertyExpression) toOWLAPI(tail.get(0));
				OWLIndividual indivA = (OWLIndividual) toOWLAPI(tail.get(1));
				OWLIndividual indivB = (OWLIndividual) toOWLAPI(tail.get(2));
				result = dataFactory.getOWLObjectPropertyAssertionAxiom(propExp,indivA, indivB);
				break;
			case EQUIVOBJPROP: 
				Set<OWLObjectPropertyExpression> exprsObj = new TreeSet<OWLObjectPropertyExpression>();
				for (int i = 0; i<tail.size();i++){
					OWLObjectPropertyExpression ce = (OWLObjectPropertyExpression) toOWLAPI(tail.get(i));
					exprsObj.add(ce);
				}
				result = dataFactory.getOWLEquivalentObjectPropertiesAxiom(exprsObj);
				break;
			case DATAPROPERTYASSERTION:
				OWLDataPropertyExpression dataExp = (OWLDataPropertyExpression) toOWLAPI(tail.get(0));
				OWLIndividual indivC = (OWLIndividual) toOWLAPI(tail.get(1));
				OWLLiteralValue literalvalueB = (OWLLiteralValue) tail.get(2).getHead();
				OWLLiteral litB = (OWLLiteral) toOWLAPI(literalvalueB);
				// System.out.println(tail.get(2));
				// System.out.println(literalvalueB);
				result = dataFactory.getOWLDataPropertyAssertionAxiom(dataExp,indivC, litB);
				break;
			case SUBPROPERTYOF:
				if (tail.get(0).getHead().equals(OWLSymb.SUBPROPERTYCHAIN)){
					List<OWLObjectPropertyExpression> props = new ArrayList<OWLObjectPropertyExpression>();
					for (OWLFormula form : tail.get(0).getArgs()){
						OWLObjectPropertyExpression sub = (OWLObjectPropertyExpression) toOWLAPI(form);
						props.add(sub);
					}
					OWLObjectPropertyExpression subp2 = (OWLObjectPropertyExpression) toOWLAPI(tail.get(1));
					result = dataFactory.getOWLSubPropertyChainOfAxiom(props, subp2);
				}
				else{
					System.out.println("DBG conversion, converting : " + formula);
					OWLObjectPropertyExpression subp1 = (OWLObjectPropertyExpression) toOWLAPI(tail.get(0));
					OWLObjectPropertyExpression subp2 = (OWLObjectPropertyExpression) toOWLAPI(tail.get(1));
					result = dataFactory.getOWLSubObjectPropertyOfAxiom(subp1,subp2);
				}
				break;		
			default: 
				result = dataFactory.getOWLNothing();
			}
		} else {
			if (head instanceof OWLInteger){
				return null;
			}
			else
			result = toOWLAPI(head);
		}
		return result;
	}
	
	public static OWLObject toOWLAPI(OWLAtom atom){
		OWLObject result = null;
		if (atom instanceof OWLClassName)
			result = toOWLAPI((OWLClassName) atom);
		if (atom instanceof OWLSymb)
			result = toOWLAPI((OWLSymb) atom);
		if (atom instanceof OWLRoleName)
			result = toOWLAPI((OWLRoleName) atom);
		if (atom instanceof OWLIndividualName)
			result = toOWLAPI((OWLIndividualName) atom);
		if (atom instanceof OWLDataPropertyName)
			result = toOWLAPI((OWLDataPropertyName) atom);
		if (atom instanceof OWLObjectPropertyName)
			result = toOWLAPI((OWLObjectPropertyName) atom);
		return result;
	}
	
	
	private static OWLObject toOWLAPI(OWLSymb symb){
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		return dataFactory.getOWLThing();
		
	}
	
	
	private static OWLObject toOWLAPI(OWLClassName classname){
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		String name = classname.getOntologyname();
		IRI iri = IRI.create(name);
		OWLClass cl = dataFactory.getOWLClass(iri);
		return cl;
	}
	
	private static OWLObject toOWLAPI(OWLRoleName rolename){
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		// if (dataproperty)
		//	return dataFactory.getOWLDataProperty(IRI.create(ontologyname));
		// else 
		return dataFactory.getOWLObjectProperty(IRI.create(rolename.getOntologyname()));
	}
	
	private static OWLObject toOWLAPI(OWLIndividualName name){
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		return dataFactory.getOWLNamedIndividual(IRI.create(name.getOntologyname()));	
	}
	
	private static OWLObject toOWLAPI(OWLDataPropertyName name){
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		return dataFactory.getOWLDataProperty(IRI.create(name.getOntologyname()));
	}
	
	private static OWLObject toOWLAPI(OWLObjectPropertyName name){
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		return dataFactory.getOWLObjectProperty(IRI.create(name.getOntologyname()));
	}
	
	private static OWLObject toOWLAPI(OWLLiteralValue val){
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		OWLLiteral lit = dataFactory.getOWLLiteral(val.getValue(),ConversionManager.literaltypeToDatatype(val.getType()));
		return lit;	
	}
	
	public static OWLDataRan datarangeToDataran(OWLDataRange range){
		OWLDatatype datatype = range.asOWLDatatype();
		if (datatype.isBoolean()){
			return OWLDataRan.BOOLEAN;
		}
		if (datatype.isDouble()){
			return OWLDataRan.DOUBLE;
		}
		if (datatype.isFloat()){
			return OWLDataRan.FLOAT;
		}
		if (datatype.isInteger()){
			return OWLDataRan.INTEGER;
		}
		return OWLDataRan.STRING;
	}
	
	public static OWLLiteralType datatypeToLiteraltype(OWLDatatype datatype){
		if (datatype.isBoolean()){
			return OWLLiteralType.BOOLEAN;
		}
		if (datatype.isDouble()){
			return OWLLiteralType.DOUBLE;
		}
		if (datatype.isFloat()){
			return OWLLiteralType.FLOAT;
		}
		if (datatype.isInteger()){
			return OWLLiteralType.INTEGER;
		}
		return OWLLiteralType.STRING;
	}
	
	public static OWLDatatype literaltypeToDatatype(OWLLiteralType littype){
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		if (littype.equals(OWLLiteralType.BOOLEAN)){
			return dataFactory.getBooleanOWLDatatype();
		}
		if (littype.equals(OWLLiteralType.DOUBLE)){
			return dataFactory.getDoubleOWLDatatype();
		}
		if (littype.equals(OWLLiteralType.FLOAT)){
			return dataFactory.getFloatOWLDatatype();
		}
		if (littype.equals(OWLLiteralType.INTEGER)){
			return dataFactory.getIntegerOWLDatatype();
		}
		// OWLDatatype string = dataFactory.getOWLDatatype(XSDVocabulary.parseShortName("xsd:string"));
		OWLDatatype string = dataFactory.getOWLDatatype(XSDVocabulary.STRING.getIRI());
		return string;
	}
	
	
}
