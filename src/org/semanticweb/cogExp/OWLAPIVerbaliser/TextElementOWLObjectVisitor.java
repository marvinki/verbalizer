package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
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
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
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
import org.semanticweb.owlapi.model.OWLObject;
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
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyRange;
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

import com.google.common.base.Optional;

public class TextElementOWLObjectVisitor implements OWLObjectVisitorEx<List<TextElement>>{
	
	private static final String _space = VerbalisationManager.INSTANCE._space;

	private Obfuscator obfuscator;
	
	public void setObfuscator(Obfuscator obfuscator){
		this.obfuscator = obfuscator;
	}
	
	public Obfuscator getObfuscator(){
		return obfuscator;
	}

	public static List<OWLClassExpression> collectAndExpressions(OWLObject ob){
		List<OWLClassExpression> result = new ArrayList<OWLClassExpression>();
		if (ob instanceof OWLObjectIntersectionOf){
			OWLObjectIntersectionOf ints = (OWLObjectIntersectionOf) ob;
			List<OWLClassExpression> subexprs = ints.getOperandsAsList();
			for(OWLClassExpression subexpr : subexprs){
				result.addAll(collectAndExpressions(subexpr));
			}
		} else{
			result.add((OWLClassExpression) ob);
		}
		return result;
	}
	
	public static boolean checkMultipleExistsAndForallPattern(OWLObjectIntersectionOf ints){
		List<OWLClassExpression> exprs = ints.getOperandsAsList();
		List<OWLClassExpression> existsexprs = new ArrayList<OWLClassExpression>();
		List<OWLClassExpression> fillers = new ArrayList<OWLClassExpression>();
		if (exprs.size()<2){
			return false;
		}
		if (!(exprs.get(exprs.size()-1) instanceof OWLObjectAllValuesFrom)){
			return false;
		}
		OWLObjectPropertyExpression commonpropexpr = null;
		for (OWLClassExpression expr: exprs){
			if (expr!=exprs.get(exprs.size()-1)){
				existsexprs.addAll(TextElementOWLObjectVisitor.collectAndExpressions(expr));
			}
		}
		for (OWLClassExpression expr: existsexprs){
			if (!(expr instanceof OWLObjectSomeValuesFrom)){
				return false;
			}
			OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
			OWLObjectPropertyExpression propexpr = someexpr.getProperty();
			fillers.add(someexpr.getFiller());
			if (commonpropexpr==null){
				commonpropexpr = propexpr;
			}
			if (!commonpropexpr.equals(propexpr)){
				return false;
			}
			OWLClassExpression forallexp = ((OWLObjectAllValuesFrom) exprs.get(exprs.size()-1)).getFiller();
			List<OWLClassExpression> fillers2 = new ArrayList<OWLClassExpression>();
			if (forallexp instanceof OWLObjectUnionOf){
				fillers2.addAll(((OWLObjectUnionOf) forallexp).getOperandsAsList());
			} else
				fillers2.add(forallexp);
			if (!fillers2.contains(someexpr.getFiller())){
				return false;
			}	
		}
		return true;
	}
	
	public static boolean detectUnderCamel(String str){
		// look for one occurrence of "_\Uppercase"
		int i = str.indexOf("_");
		if (i>-1 && str.length()>i+1){
			if (Character.isUpperCase(str.charAt(i+1))){
				return true;
			}
		}
		return false;
	}
	
	public static String removeUnderCamel(String str){
		List<String> results = new ArrayList<String>();
		String resultstring = "";
		while(str.length()>0){
			int i = str.indexOf("_");
			if (i>=0){
				results.add(str.substring(0,i));
				str = str.substring(i+1);
			}
			else{
				results.add(str);
				str="";
			}
		}
		// return results;
		for (String s : results){
			if (s.length()>0 && Character.isUpperCase(s.charAt(0)))
				resultstring = resultstring + s.substring(0,1).toLowerCase() + s.substring(1,s.length()) + _space;
				else
				resultstring = resultstring + s + _space;
		}
		if (resultstring.length()>0 && resultstring.substring(resultstring.length()-1).equals(_space))
			resultstring = resultstring.substring(0, resultstring.length()-1);
		return resultstring;
	}
	
	public static String removeUnderscores(String str){
		if (str.substring(0,1).equals("\"")){
			str = str.substring(1);
		}
		List<String> results = new ArrayList<String>();
		String resultstring = "";
		while(str.length()>0){
			int i = str.indexOf("_");
			if (i>=0){
				results.add(str.substring(0,i));
				str = str.substring(i+1);
			}
			else{
				i = str.indexOf("\"");
				if (i>=0){ 
					results.add(str.substring(0,i));
				}
				else
				results.add(str);
				str = "";
			}
		}
		// return results;
		for (int i = 0; i< results.size(); i++){
			resultstring = resultstring + results.get(i);
			if (i!=results.size()-1)
				resultstring = resultstring +  _space;

		}
		return resultstring;
	}
	
	public static boolean detectCamelCase(String str){
		if (str.length()<2) return false;
		// find first uppercase
		int u = -1;
		for (int i=0; i<str.length(); i++){
			if (Character.isUpperCase(str.charAt(i))){
				u = i;
				break;
			}
		}
		if (u==-1){
			return false;
		}
		int j = -1;
		// find second uppercase
		for (int i=u+1; i<str.length(); i++){
			if (Character.isUpperCase(str.charAt(i))){
				j = i;
			}
		}
		if (j==-1){
			return false;
		}
		return true;
	}
	
	public static boolean detectLowCamelCase(String str){
		if (str.length()<2) return false;
		// find first uppercase
		if (Character.isUpperCase(str.charAt(0)))
				return false;
		int u = -1;
		for (int i=0; i<str.length(); i++){
			if (Character.isUpperCase(str.charAt(i))){
				u = i;
				break;
			}
		}
		if (u==-1){
			return false;
		}
		return true;
	}
	
	public static String removeCamelCase(String str){
		String resultstring = "";
		for (int i=0; i<str.length(); i++){
			if (Character.isUpperCase(str.charAt(i))){
				if (i==0) 
					resultstring = str.substring(i,i+1).toLowerCase() ;
				else
				resultstring = resultstring + " " + str.substring(i,i+1).toLowerCase() ;
			}
			else{
				resultstring = resultstring + str.substring(i,i+1);
			}
		}
		return resultstring;
	}
	
	// VERBALIZE SUBCLASSOFAXIOM
	public List<TextElement> visit(OWLSubClassOfAxiom arg0) {
		// System.out.println("-------");
		// define some elements that will be used later
		LogicElement somethingthatElement = new LogicElement("something that");
		LogicElement thatElement =  new LogicElement("that");
		LogicElement commaElement =  new LogicElement(",");
		LogicElement isElement =  new LogicElement("is");
		
		// System.out.println("visit subclassof called with " + arg0);
		// Left hand side
		List<TextElement> leftstring = new ArrayList<TextElement>();
		leftstring = arg0.getSubClass().accept(this);
		List<TextElement> somethingthat = new ArrayList<TextElement>();
		somethingthat.add(somethingthatElement);
		if (arg0.getSubClass() instanceof OWLObjectSomeValuesFrom){
			OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) arg0.getSubClass();
			OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
			// System.out.println("SUBCLASS SOMEOF DEBUG " + some1 + " " + cl);
			if (cl!=null){
				//somethingthat = cl.toString() + " that ";
				somethingthat =cl.accept(this); 
				somethingthat.add(thatElement);
			}
		}
		if (arg0.getSubClass() instanceof OWLObjectIntersectionOf){
			OWLObjectIntersectionOf intsect =  (OWLObjectIntersectionOf) arg0.getSubClass();
			OWLClassExpression clexpr = intsect.getOperandsAsList().get(0);
			List<OWLClassExpression> exprs = collectAndExpressions(clexpr);
			for (OWLClassExpression expr : exprs){
				if (expr instanceof OWLObjectSomeValuesFrom){
					OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) expr;
					OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
					if (cl!=null){
						somethingthat =cl.accept(this); 
						somethingthat.add(thatElement);
					}
				}
			}
		}	
		if (arg0.getSubClass() instanceof OWLObjectIntersectionOf 
				&& VerbalisationManager.checkMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSubClass())){
			leftstring = new ArrayList<TextElement>();
			leftstring.add(somethingthatElement); 
			leftstring.addAll(VerbalisationManager.textualiseMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSubClass())) ;
		}
		if (arg0.getSubClass() instanceof OWLObjectIntersectionOf 
				&& checkMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSubClass())){
			leftstring = new ArrayList<TextElement>();
			leftstring.add(somethingthatElement);
			TextElementSequence seq = VerbalisationManager.textualiseMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSubClass());
			leftstring.addAll(seq.getTextElements()); 
			leftstring.add(commaElement);
		}
		if (arg0.getSubClass() instanceof OWLObjectSomeValuesFrom){
			// leftstring = new ArrayList<TextElement>();
			// System.out.println("leftstring " + leftstring);
			leftstring.addAll(0,somethingthat);
			// leftstring.add(0,somethingthatElement);
		}
		List<TextElement> middlestring = new ArrayList<TextElement>();
		if (!(arg0.getSuperClass() instanceof OWLObjectSomeValuesFrom) 
				&& !(arg0.getSuperClass() instanceof OWLObjectAllValuesFrom)
				&& !(arg0.getSuperClass() instanceof OWLObjectIntersectionOf
						&& checkMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass())
						)
				&& !(arg0.getSuperClass() instanceof OWLObjectIntersectionOf
						&& VerbalisationManager.checkMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSuperClass())
						)
				&& !(arg0.getSuperClass() instanceof OWLObjectHasValue) 
				&& !(arg0.getSuperClass() instanceof OWLDataHasValue) 
				){
			// System.out.println("DEBUG! " + arg0.getSuperClass());
			leftstring.add(isElement);
		}
		// this catches the simple case where the superclass is only a single existential
		// ... in this case, the "something that" is skipped.
		if (arg0.getSuperClass() instanceof OWLObjectSomeValuesFrom){
			OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) arg0.getSuperClass();
			OWLClassExpression filler = some.getFiller();
			OWLObjectPropertyExpression property = some.getProperty();
			List<List<TextElement>> fillerstrs = new ArrayList<List<TextElement>>();
			List<TextElement> middle = new ArrayList<TextElement>();
			if (filler instanceof OWLObjectSomeValuesFrom){
				OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) filler;
				OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
				if (cl!=null){
					// middle = cl.toString() + " that ";
					middle.addAll(cl.accept(this));
					middle.add(thatElement);
				}
				else
				middle.add(somethingthatElement);
			}
			// Multiple Exists and Forall Pattern
			if (arg0.getSuperClass() instanceof OWLObjectIntersectionOf 
					&& checkMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass())){
				// System.out.println("DEBUG: exists and forall case");
				OWLObjectIntersectionOf intsect =  (OWLObjectIntersectionOf) arg0.getSuperClass();
				OWLClassExpression clexpr = intsect.getOperandsAsList().get(0);
				List<OWLClassExpression> exprs = collectAndExpressions(clexpr);
				middle = new ArrayList<TextElement>();
				middle.add(somethingthatElement);
				for (OWLClassExpression expr : exprs){
					if (expr instanceof OWLObjectSomeValuesFrom){
						OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) expr;
						OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
						if (cl!=null){
							String tooltiptext = cl.asOWLClass().getIRI().toString();
							ClassElement clElement = new ClassElement(cl.toString(),tooltiptext);
							middle = new ArrayList<TextElement>();
							middle.add(clElement);
							middle.add(thatElement);
						}
					}
				}
			}	
			fillerstrs.add(filler.accept(this));
			leftstring.addAll(VerbalisationManager.textualiseProperty(property, fillerstrs, middle));
			return leftstring;
			// return // leftstring
					// + " " 
					// + VerbalisationManager.textualiseProperty(property, fillerstrs, middle);
		}
		// Multiple Exists Pattern
		if (arg0.getSuperClass() instanceof OWLObjectIntersectionOf 
				&& VerbalisationManager.checkMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSuperClass())){
			// System.out.println("DEBUG : case of multiple exists patterns");
			leftstring.addAll(VerbalisationManager.textualiseMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSuperClass()));
			return leftstring;
			// 		+ " " +
			// VerbalisationManager.pseudoNLStringMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSuperClass());
		}
		if (arg0.getSuperClass() instanceof OWLObjectIntersectionOf 
				&& checkMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass())){
			TextElementSequence seq = VerbalisationManager.textualiseMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass());
			leftstring.addAll(seq.getTextElements());
			return leftstring;
			// 		leftstring + " " +
			// VerbalisationManager.pseudoNLStringMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass());
		}
		leftstring.addAll(middlestring);
		// System.out.println("DEBUG -- in the default case!");
		leftstring.addAll(arg0.getSuperClass().accept(this));  // <-- Default>
		// System.out.println("DEBUG default case: " + leftstring);
		// System.out.println("DEBUG default case: " + new TextElementSequence(leftstring));
		return  leftstring; // + 
				//middlestring
				// + arg0.getSuperClass().accept(this);
	}
	
	// VERBALIZE OWLCLASS
	public List<TextElement> visit(OWLClass ce) {
		// System.out.println("visiting OWL Class "  + ce);
		String clstr = VerbalisationManager.INSTANCE.getClassNLString(ce);
		String tooltiptext = ce.getIRI().toString();
		ClassElement clelem = new ClassElement(clstr,tooltiptext);
		List<TextElement> textElement = new ArrayList<TextElement>();
		textElement.add(clelem);
		return textElement;
   }
	
	public TextElementSequence verbaliseComplexIntersection(OWLObjectIntersectionOf arg0, Obfuscator obfuscator){
		// System.out.println("complx int " + arg0);
		List<OWLClassExpression> operands = arg0.getOperandsAsList();
		// distinguish operands by their types and order them appropriately
		List<OWLClassExpression> classExps = new ArrayList<OWLClassExpression>();
		List<OWLObjectSomeValuesFrom> someExps = new ArrayList<OWLObjectSomeValuesFrom>();
		List<OWLDataSomeValuesFrom> someDataExps = new ArrayList<OWLDataSomeValuesFrom>();
		List<OWLDataHasValue> dataHasValueExps = new ArrayList<OWLDataHasValue>();
		List<OWLObjectAllValuesFrom> allExps = new ArrayList<OWLObjectAllValuesFrom>();
		List<OWLClassExpression> allelseExps = new ArrayList<OWLClassExpression>();
		// loop and sort
		for (OWLClassExpression exp : operands){
			if (exp instanceof OWLClass)
				classExps.add((OWLClassExpression) exp);
			else if (exp instanceof OWLObjectSomeValuesFrom)
				someExps.add((OWLObjectSomeValuesFrom) exp);
			else if (exp instanceof OWLDataSomeValuesFrom)
				someDataExps.add((OWLDataSomeValuesFrom) exp);
			else if (exp instanceof OWLDataHasValue)
				dataHasValueExps.add((OWLDataHasValue) exp);
			else if (exp instanceof OWLObjectAllValuesFrom)
				allExps.add((OWLObjectAllValuesFrom) exp);
			else allelseExps.add(exp);		
		} // end of sorting loop
		// verbalise the "head" with the simple aggregator
		TextElementSequence seq = new TextElementSequence();
		String head = VerbalisationManager.INSTANCE.getSimpleIntersectionNLString(classExps);
		String tooltiptext = multipleTooltip(classExps);
		ClassElement headElement = new ClassElement(head,tooltiptext);
		seq.add(headElement);
		// is there more?
		if (someExps.size()>0 || allExps.size()>0 || allelseExps.size()>0 || someDataExps.size()>0 || dataHasValueExps.size()>0){
			seq.add(new LogicElement("that"));
			// head += " that ";
		}
		if (dataHasValueExps.size()==1){
			head += dataHasValueExps.get(0).accept(this);
			ClassElement headElement2 = new ClassElement(head);
			TextElementSequence seq2 = new TextElementSequence();
			seq2.add(headElement2);
			return seq2;
		}
		if (someExps.size()>0 && allExps.size()==0){
			head = VerbalisationManager.INSTANCE.verbaliseComplexIntersection(arg0,obfuscator); // <-- TODO - nothing to do
			ClassElement headElement3 = new ClassElement(head);
			TextElementSequence seq3 = new TextElementSequence();
			seq3.add(headElement3);
			return seq3;
			/*
			// TODO -this is not aggregating in case there is a multiple exists pattern
			boolean isFirst = true;
			for(OWLObjectSomeValuesFrom some : someExps){
				if (!isFirst)
					head += " and ";
				head += some.accept(this);
				isFirst = false;
			}
			*/
		}
		TextElementSequence seq4 = seq;// new TextElementSequence();
		// if (someExps.size()>0 && allExps.size()>0 && (classExps.size()>0 || someExps.size()>0))
		// if (seq4.getTextElements().size()>0)
		//  		seq4.add(new LogicElement("and")) ;
		if (allExps.size()>0){
			// TODO: this is not aggregating in case there is a multiple exists pattern
			boolean isFirst = true;
			for(OWLObjectAllValuesFrom all : allExps){
				if (!isFirst)
					seq4.add(new LogicElement("and")) ;
				seq4.concat(new TextElementSequence(all.accept(this)));
				isFirst = false;
			}
		}
		// if (allExps.size()>0 && allelseExps.size()>0)
		// 	seq4.add(new LogicElement("and")) ;
		if (allelseExps.size()>0){
			if (allExps.size()==0)
			seq4.add(new LogicElement("is")) ;
			// TODO: this is not aggregating in case there is a multiple exists pattern
			boolean isFirst = true;
			for(OWLClassExpression allelse : allelseExps){
				if (!isFirst)
					seq4.add(new LogicElement("and")) ;
				List<TextElement> allelseString = allelse.accept(this);
				if (allelseString == null){
					seq.add(new LogicElement("{NULL}"));
				}else{
					seq4.concat(new TextElementSequence(allelse.accept(this)));
				}
				isFirst = false;
			}
		}
		// System.out.println(" fourth case, returning: " + seq4);
		return seq4;
	}
	

	public List<TextElement> visit(OWLObjectIntersectionOf arg0) {
		// System.out.println(" intersection text visitor called with: " + arg0);
		LogicElement somethingthatElement = new LogicElement("something that");
		List<TextElement> resultList = new ArrayList<TextElement>();
		if (VerbalisationManager.checkMultipleExistsPattern(arg0)){
			resultList.add(somethingthatElement);
			resultList.addAll(VerbalisationManager.textualiseMultipleExistsPattern(arg0));
			// System.out.println("visit intersect (1): " + new TextElementSequence(resultList).toString());
			return resultList;
		} else{
			if (checkMultipleExistsAndForallPattern(arg0)){
				resultList.add(somethingthatElement);
				TextElementSequence seq = VerbalisationManager.textualiseMultipleExistsAndForallPattern(arg0);
				resultList.addAll(seq.getTextElements());
				// System.out.println("visit intersect (2): " + new TextElementSequence(resultList).toString());
				return resultList;
			} else{
					boolean onlysimpleclasses = true;
					for (OWLClassExpression exp: ((OWLObjectIntersectionOf) arg0).getOperandsAsList()){
						if (!(exp instanceof OWLClass)){onlysimpleclasses = false;}
					}
					if (onlysimpleclasses){
						String str = VerbalisationManager.INSTANCE.getSimpleIntersectionNLString(arg0);
						String tooltiptext = multipleTooltip(arg0.getOperandsAsList());
						resultList.add(new ClassElement(str,tooltiptext));
						// System.out.println("visit intersect (3): " + new TextElementSequence(resultList).toString());
						return resultList;}
					resultList.addAll(verbaliseComplexIntersection(arg0,obfuscator).getTextElements());
				}
			}
		// System.out.println("visit intersect (4): " + new TextElementSequence(resultList).toString());
		return resultList;
	}

	public List<TextElement> visit(OWLObjectUnionOf arg0) {
		List<TextElement> resultList = new ArrayList<TextElement>();
		if (((OWLObjectUnionOf) arg0).getOperandsAsList().size()==2){
			List<OWLClassExpression> exprs = ((OWLObjectUnionOf) arg0).getOperandsAsList();
			// String res = VerbalisationManager.aggregateRepeated(exprs.get(0).accept(this), exprs.get(1).accept(this), " or ");
			return null;
					// VerbalisationManager.aggregateRepeated(exprs.get(0).accept(this), exprs.get(1).accept(this), " or ");
		}
		boolean firstp = true;
		for (OWLClassExpression exp: ((OWLObjectUnionOf) arg0).getOperandsAsList()){
			if (!firstp){
				LogicElement orElement = new LogicElement("or");
				resultList.add(orElement);}
			firstp = false;
			resultList.addAll(exp.accept(this));
			// resultstring = resultstring + exp.accept(this);
		}
		return resultList;
	}

	
	public List<TextElement> visit(OWLObjectComplementOf arg0) {
		List<TextElement> resultList = new ArrayList<TextElement>();
		LogicElement notElement = new LogicElement("not");
		resultList.add(notElement);
		resultList.addAll(arg0.getOperand().accept(this));
		return resultList;
	}

	public List<TextElement> visit(OWLObjectAllValuesFrom existsexpr) {
		OWLObjectPropertyExpression property = existsexpr.getProperty();
		OWLClassExpression filler = existsexpr.getFiller();
		List<List<TextElement>> fillerstrs = new ArrayList<List<TextElement>>();
		List<TextElement> middlestring = new ArrayList<TextElement>();
		middlestring.add(new LogicElement("nothing but"));
		if (filler instanceof OWLObjectSomeValuesFrom)
			middlestring.add(new LogicElement("something that"));
		fillerstrs.add(filler.accept(this));
		return VerbalisationManager.textualiseProperty(property, fillerstrs, middlestring);
	}
	
	
	public List<TextElement> visit(OWLObjectSomeValuesFrom existsexpr) {
		// System.out.println("DEBUG -- " + existsexpr);
		OWLObjectPropertyExpression property = existsexpr.getProperty();
		// changed! String propfragment = property.getNamedProperty().getIRI().getFragment();
		Optional<String> propfragment = property.getNamedProperty().getIRI().getRemainder();
		OWLClassExpression filler = existsexpr.getFiller();
		List<List<TextElement>> fillerstrs = new ArrayList<List<TextElement>>();
		List<TextElement> middle = new ArrayList<TextElement>();
		if(filler instanceof OWLObjectSomeValuesFrom){
			OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) filler;
			OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
			if (cl !=null){
				String tooltiptext = cl.asOWLClass().getIRI().toString();
				middle.add(new ClassElement(cl.toString()));// = cl.toString();
			}else{
			middle.add(new LogicElement("something that"));
			}
		}
		fillerstrs.add(filler.accept(this));
		List<TextElement> result = VerbalisationManager.textualiseProperty(property,fillerstrs,middle);
		// String str = VerbalisationManager.verbaliseProperty(property,fillerstrs,middle);
		// System.out.println("DEBUG visit " + str);
		return result;
	}
	
	public List<TextElement> visit(OWLEquivalentClassesAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		OWLClass classexp = null;
		List<OWLClassExpression> exprs =  ((OWLEquivalentClassesAxiom) arg0).getClassExpressionsAsList();
		for (OWLClassExpression ex: exprs){
			if (ex instanceof OWLClass){
				classexp = (OWLClass) ex;
				break;
			}
		}
		if (classexp!=null){
			result.add(new LogicElement("According to its definition,"));
			result.addAll(classexp.accept(this));
			result.add(new LogicElement("is"));
			boolean firstp = true;
			for (OWLClassExpression ex:exprs){
				if (!ex.equals(classexp)){
						if (!firstp){
							result.add(new LogicElement("and"));
							firstp = false;
							}
						if (ex instanceof OWLObjectSomeValuesFrom)
							result.add(new LogicElement("something that"));
							result.addAll(ex.accept(this));
				}	
			}		
		} else{
			result.addAll(exprs.get(0).accept(this));
			// result += exprs.get(0).accept(this);
			result.add(new LogicElement("is the same as"));
			boolean firstp = true;
			for (OWLClassExpression ex:exprs){
				if (!ex.equals(exprs.get(0))){
						if (!firstp){
							result.add(new LogicElement("and"));
							firstp = false;
							}
						if (ex instanceof OWLObjectSomeValuesFrom)
							result.add(new LogicElement("something that"));
							result.addAll(ex.accept(this));
				}	
			}	
		}
		return result;
	}

	public List<TextElement> visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
		List<TextElement> result = new ArrayList<>();
		result.add(new LogicElement("neg obj prop("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getObject().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getSubject().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLAsymmetricObjectPropertyAxiom arg0) {
		List<TextElement> result = new ArrayList<>();
		result.add(new LogicElement("asymmetr("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLReflexiveObjectPropertyAxiom arg0) {
		List<TextElement> result = new ArrayList<>();
		result.add(new LogicElement("reflexive("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLDisjointClassesAxiom arg0) {
		List<TextElement> result = new ArrayList<>();
		List<OWLClassExpression> exprs =  arg0.getClassExpressionsAsList();
		// check if there is an atomic class expression in there, and put it in a special variable
		OWLClass classexp = null;
		for (OWLClassExpression candidate : exprs){
			if (candidate instanceof OWLClass){
				classexp= (OWLClass) candidate;
				break;
			}
		}
		if (classexp!=null){
			result.add(new LogicElement("No"));
			List<TextElement> str = classexp.accept(this);
			TextElement t = str.get(0);
			String firstt = t.content;
			if (firstt.indexOf("a ")==0)
				// oh, hier müssen wir richtig umbauen... 
				t.setContent(firstt.substring(2));
				// str = str.substring(2);
			if (firstt.indexOf("an ")==0)
				// oh, hier müssen wir richtig umbauen... 
				t.setContent(firstt.substring(3));
				// str = str.substring(2);
			result.add(new LogicElement("is"));
			// string += str + " is ";
			boolean firstp = true;
			for (OWLClassExpression exp : exprs){
				if (!exp.equals(classexp)){
				if (!firstp) {
					result.add(new LogicElement("or"));
					}
				firstp = false;
				result.addAll(exp.accept(this));
				}
			}
		}
		else{
			result.add(new LogicElement("Nothing that is"));
			// resultstring += "Nothing that is ";
			boolean firstp = true;
			boolean twop = true;
			for (OWLClassExpression exp : exprs){	
				if (!twop) {
					result.add(new LogicElement("or"));
					}
				result.addAll(exp.accept(this));	
				if (firstp)
					{firstp = false;
					result.add(new LogicElement("is"));
					}
				if (firstp=false)
					twop=false;
			}
		}
		return result;
	}

	public List<TextElement> visit(OWLDataPropertyDomainAxiom arg0) {
		// TODO: treat this as syntactic sugar, convert this to the form \exists r. \top \sqsubseteq X and output
		List<TextElement> result = new ArrayList<>();
		result.add(new LogicElement("dom("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getDomain().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLObjectPropertyDomainAxiom arg0) {
		// treat this as syntactic sugar, convert this to the form \exists r. \top \sqsubseteq X and output
		OWLObjectPropertyExpression prop = arg0.getProperty();
		OWLClassExpression classexp = arg0.getDomain();
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		OWLSubClassOfAxiom subclax = dataFactory.getOWLSubClassOfAxiom(
				dataFactory.getOWLObjectSomeValuesFrom(prop,dataFactory.getOWLThing()), classexp);
		return  subclax.accept(this);
	}

	public List<TextElement> visit(OWLEquivalentObjectPropertiesAxiom arg0) {
		List<TextElement> result = new ArrayList<>();
		result.add(new LogicElement("equivalent("));
		Set<OWLObjectPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLPropertyExpression exp : exprs){
		if (!firstp) {
			result.add(new LogicElement(","));
			// resultstring = resultstring + ",";
			}
			firstp = false;
			result.addAll(exp.accept(this));
			}
		result.add(new LogicElement(")"));
		return result;
		
	}

	public List<TextElement> visit(OWLNegativeDataPropertyAssertionAxiom arg0) {
		List<TextElement> result = new ArrayList<>();
		result.add(new LogicElement("neg dat prop("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getObject().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getSubject().accept(this));
		return result;
	}

	public List<TextElement> visit(OWLDifferentIndividualsAxiom arg0) {
		List<TextElement> result = new ArrayList<>();
		result.add(new LogicElement("different("));
		List<OWLIndividual> exprs =  arg0.getIndividualsAsList();
		boolean firstp = true;
		for (OWLIndividual exp : exprs){
		if (!firstp) {
			result.add(new LogicElement(","));
			}
			firstp = false;
			result.addAll(exp.accept(this));
			}
		result.add(new LogicElement(","));
		return result;
	}

	public List<TextElement> visit(OWLDisjointDataPropertiesAxiom arg0) {
		List<TextElement> result = new ArrayList<>();
		result.add(new LogicElement("disjoint("));
		Set<OWLDataPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLDataPropertyExpression exp : exprs){
		if (!firstp) {
			result.add(new LogicElement(","));
		}
			firstp = false;
			result.addAll(exp.accept(this));
			}
		result.add(new LogicElement(","));
		return result;
	}

	public List<TextElement> visit(OWLDisjointObjectPropertiesAxiom arg0) {
		List<TextElement> result = new ArrayList<>();
		result.add(new LogicElement("disjoint("));
		Set<OWLObjectPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLObjectPropertyExpression exp : exprs){
		if (!firstp) {
			result.add(new LogicElement(","));
		}
			firstp = false;
			result.addAll(exp.accept(this));
			}
		result.add(new LogicElement(","));
		return result;
	}

	public List<TextElement> visit(OWLObjectPropertyRangeAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		OWLObjectPropertyExpression prop1 = arg0.getProperty();
		OWLClassExpression prop2 = arg0.getRange();
		result.add(new LogicElement("range("));
		result.addAll(prop1.accept(this));
		result.add(new LogicElement(","));
		result.addAll(prop2.accept(this));
		result.add(new LogicElement(")"));
		return result;
		
	}

	public List<TextElement> visit(OWLObjectPropertyAssertionAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		return result;
		// return "{IMPLEMENT ME obj prop ass : " 
	    // + arg0.getProperty().accept(this) +  arg0.getObject().accept(this) 
	    // + "}";
	}

	public List<TextElement> visit(OWLFunctionalObjectPropertyAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		return result;
		// return "functional" + "(" + arg0.getProperty().accept(this) + ")";		
	}

	public List<TextElement> visit(OWLSubObjectPropertyOfAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		return result;
		// return "to " + arg0.getSubProperty().accept(this) + " is a subproperty of " + arg0.getSuperProperty().accept(this); 
	}

	public List<TextElement> visit(OWLDisjointUnionAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("disjoint-union("));
		Set<OWLClassExpression> exprs =  arg0.getClassExpressions();
		boolean firstp = true;
		for (OWLClassExpression exp : exprs){
		if (!firstp) {
			result.add(new LogicElement(","));
		}
			firstp = false;
			result.addAll(exp.accept(this));
			}
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLDeclarationAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		return result;
		// return "{IMPLEMENT ME decl ax : "  + arg0.getEntity() + "}";
	}

	public List<TextElement> visit(OWLAnnotationAssertionAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		return result;
		// return "{IMPLEMENT ME annot ax : " + arg0.getSubject().accept(this)  
		// 		+ arg0.getProperty().accept(this) + arg0.getValue().accept(this)  + "}";
	}

	public List<TextElement> visit(OWLSymmetricObjectPropertyAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("symm("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLDataPropertyRangeAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement("has range"));
		result.addAll(arg0.getRange().accept(this));
		return result;
		// return arg0.getProperty().accept(this) + " has range " + arg0.getRange().accept(this); 
	}

	public List<TextElement> visit(OWLFunctionalDataPropertyAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("functional("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLEquivalentDataPropertiesAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("equivalent-dataprops("));
		Set<OWLDataPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLDataPropertyExpression exp : exprs){
		if (!firstp) {
			result.add(new LogicElement(","));
			}
			firstp = false;
			result.addAll(exp.accept(this));
			}
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLClassAssertionAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.addAll(arg0.getIndividual().accept(this));
		result.add(new LogicElement("is"));
		result.addAll(arg0.getClassExpression().accept(this));
		return result;
	} 

	public List<TextElement> visit(OWLDataPropertyAssertionAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.addAll(arg0.getSubject().accept(this));
		result.addAll(arg0.getProperty().accept(this));
		return result;
	}

	public List<TextElement> visit(OWLTransitiveObjectPropertyAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("trans("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLIrreflexiveObjectPropertyAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("irrefl("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLSubDataPropertyOfAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("to"));
		result.addAll(arg0.getSubProperty().accept(this));
		result.add(new LogicElement("is a subproperty of"));
		result.addAll(arg0.getSuperProperty().accept(this));
		return result;
	}

	public List<TextElement> visit(OWLInverseFunctionalObjectPropertyAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("inv-funct("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLSameIndividualAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("same-individual("));
		List<OWLIndividual> exprs =  arg0.getIndividualsAsList();
		boolean firstp = true;
		for (OWLIndividual exp : exprs){
		if (!firstp) {
			result.add(new LogicElement(","));
		}
			firstp = false;
			result.addAll(exp.accept(this));
			}
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLSubPropertyChainOfAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("property-chain("));
		List<OWLObjectPropertyExpression> exprs =  arg0.getPropertyChain();
		boolean firstp = true;
		for (OWLObjectPropertyExpression exp : exprs){
		if (!firstp) {
			result.add(new LogicElement(","));
			}
			firstp = false;
			result.addAll(exp.accept(this));
			}
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLInverseObjectPropertiesAxiom arg0) {
			List<TextElement> result = new ArrayList<TextElement>();
			OWLObjectPropertyExpression expr1 =   arg0.getFirstProperty();
			OWLObjectPropertyExpression expr2 =   arg0.getSecondProperty();
			result.add(new LogicElement("inverse("));
			result.addAll(expr1.accept(this));
			result.add(new LogicElement(","));
			result.addAll(expr2.accept(this));
			result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLHasKeyAxiom arg0) {
		return null;
	}

	public List<TextElement> visit(OWLDatatypeDefinitionAxiom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement> visit(SWRLRule arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement> visit(OWLSubAnnotationPropertyOfAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.addAll(arg0.getSubProperty().accept(this));
		result.add(new LogicElement("is sub-annotiation property of"));
		result.addAll(arg0.getSuperProperty().accept(this));
		return result;
	}

	public List<TextElement> visit(OWLAnnotationPropertyDomainAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement("has domain"));
		result.addAll(arg0.getDomain().accept(this));
		return result;
	}

	public List<TextElement> visit(OWLAnnotationPropertyRangeAxiom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		OWLAnnotationProperty prop1 = arg0.getProperty();
		IRI prop2 = arg0.getRange();
		result.add(new LogicElement("range("));
		result.addAll(prop1.accept(this));
		result.add(new LogicElement(","));
		result.addAll(prop2.accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLObjectHasValue arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("has value("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getValue().accept(this));
		result.add(new LogicElement(")"));
		return result; 
	}

	public List<TextElement> visit(OWLObjectMinCardinality arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement(">="));
		String card = "" + arg0.getCardinality();
		result.add(new LogicElement(card));
		result.add(new LogicElement("("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getFiller().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLObjectExactCardinality arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("="));
		String card = "" + arg0.getCardinality();
		result.add(new LogicElement(card));
		result.add(new LogicElement("("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getFiller().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLObjectMaxCardinality arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("<="));
		String card = "" + arg0.getCardinality();
		result.add(new LogicElement(card));
		result.add(new LogicElement("("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getFiller().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLObjectHasSelf arg0) {
		// TODO Auto-generated method stub
				return null;
	}

	public List<TextElement> visit(OWLObjectOneOf arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement> visit(OWLDataSomeValuesFrom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("something that"));
		result.addAll(arg0.getProperty().accept(this));
		result.addAll(arg0.getFiller().accept(this));
		return result;
	}

	public List<TextElement> visit(OWLDataAllValuesFrom arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.addAll(arg0.getProperty().accept(this));
		result.addAll(arg0.getFiller().accept(this));
		return result;
	}

	public List<TextElement> visit(OWLDataHasValue arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(arg0.getValue().getLiteral()));
		return result;
	}

	public List<TextElement> visit(OWLDataMinCardinality arg0) {
		String resultstring = "";
		resultstring = resultstring + "min#" + "(" + arg0.getCardinality() 
				+ ","+ arg0.getProperty().accept(this);
		resultstring = resultstring + "," +  arg0.getFiller().accept(this) +")";
		return null;
	}

	public List<TextElement> visit(OWLDataExactCardinality arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("="));
		String card = "" + arg0.getCardinality();
		result.add(new LogicElement(card));
		result.add(new LogicElement("("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getFiller().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLDataMaxCardinality arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("<="));
		String card = "" + arg0.getCardinality();
		result.add(new LogicElement(card));
		result.add(new LogicElement("("));
		result.addAll(arg0.getProperty().accept(this));
		result.add(new LogicElement(","));
		result.addAll(arg0.getFiller().accept(this));
		result.add(new LogicElement(")"));
		return result;
	}

	public List<TextElement> visit(OWLDatatype arg0) {
		// TODO Auto-generated method stub
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("as indicated by a"));
		if (arg0.isBoolean()){
			if (arg0.toString().equals("xsd:boolean")){
				result.add(new LogicElement("boolean"));
				return result;
			}
		}
		result.add(new LogicElement(arg0.toString()));
		return result;
	}

	public List<TextElement> visit(OWLDataComplementOf arg0) {
		// TODO Auto-generated method stub
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("NI-1002"));
		return result;
	}

	public List<TextElement> visit(OWLDataOneOf arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("of one of the following:"));
		// String result = "of one of the following: ";
		// TODO Auto-generated method stub
		Set<OWLLiteral> literals = arg0.getValues();
		boolean firstP = true;
		for (OWLLiteral lit: literals){
			if (!firstP)
				result.add(new LogicElement(","));
			result.addAll(lit.accept(this));
			firstP = false;
		}
		return result;
	}

	public List<TextElement> visit(OWLDataIntersectionOf arg0) {
		// TODO Auto-generated method stub
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("NI-1003"));
		return result;
	}

	public List<TextElement> visit(OWLDataUnionOf arg0) {
		// TODO Auto-generated method stub
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("NI-1004"));
		return result;
	}

	public List<TextElement> visit(OWLDatatypeRestriction arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("NI-1005"));
		return result;
	}

	public List<TextElement> visit(OWLLiteral arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement(arg0.getLiteral()));
		return result;
	}

	public List<TextElement> visit(OWLFacetRestriction arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("NI-1006"));
		return result;
	}

	public List<TextElement> visit(OWLObjectProperty arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new RoleElement(arg0.getNamedProperty().toString()));
		return result;
	}

	public List<TextElement> visit(OWLObjectInverseOf arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("not"));
		result.addAll(arg0.getInverse().accept(this));
		return result;
	}

	public List<TextElement> visit(OWLDataProperty arg0) {
		String str = VerbalisationManager.treatCamelCaseAndUnderscores(arg0.getIRI().getFragment().toString());
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement(str));
		return result;
		// return arg0.getIRI().getFragment().toString();
	}

	public List<TextElement> visit(OWLNamedIndividual arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement(arg0.getIRI().getFragment()));
		return result;
	}

	public List<TextElement> visit(OWLAnnotationProperty arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement(arg0.getIRI().getFragment()));
		return result;
	}

	public List<TextElement> visit(OWLAnnotation arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.addAll(arg0.getProperty().accept(this));
		result.addAll(arg0.getValue().accept(this));
		return result;
	}

	public List<TextElement> visit(IRI arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement(arg0.getFragment()));
		return result;
	}

	public List<TextElement>  visit(OWLAnonymousIndividual arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("NI-1008"));
		return result;		
	}

	public List<TextElement>  visit(SWRLClassAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(SWRLDataRangeAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(SWRLObjectPropertyAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(SWRLDataPropertyAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(SWRLBuiltInAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(SWRLVariable arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(SWRLIndividualArgument arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(SWRLLiteralArgument arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(SWRLSameIndividualAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(SWRLDifferentIndividualsAtom arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TextElement>  visit(OWLOntology arg0) {
		List<TextElement> result = new ArrayList<TextElement>();
		result.add(new LogicElement("ontology"));
		LogicElement ontElem = new LogicElement(arg0.getOntologyID().toString());
		return result;
		
	}

    public static String multipleTooltip(List<OWLClassExpression> args){
    	String result = "";
    	for (OWLClassExpression cls : args){
    		result += cls.asOWLClass().getIRI().toString();
    	}
    	return result;
    }   
	
}
