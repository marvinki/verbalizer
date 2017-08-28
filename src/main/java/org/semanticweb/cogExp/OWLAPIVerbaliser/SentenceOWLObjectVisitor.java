package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
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

// import com.google.common.base.Optional;

public class SentenceOWLObjectVisitor implements OWLObjectVisitorEx<Sentence>{


//	private Language lang = Language.GERMAN;
	private Locale lang = VerbaliseTreeManager.locale;
	private static ResourceBundle LogicLabels = ResourceBundle.getBundle("LogicLabels", VerbaliseTreeManager.locale);
	
	
//	for Debuging reasons only
	private boolean debug = VerbaliseTreeManager.debug;
	
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
				existsexprs.addAll(SentenceOWLObjectVisitor.collectAndExpressions(expr));
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
	public Sentence visit(OWLSubClassOfAxiom arg0) {
		// System.out.println("-------");
		// define some elements that will be used later
		LogicElement somethingthatElement = new LogicElement(LogicLabels.getString("somethingThat"));
		LogicElement thatElement =  new LogicElement(LogicLabels.getString("that"));
		LogicElement commaElement =  new LogicElement(",");
		LogicElement isElement =  new LogicElement(LogicLabels.getString("is"));
		
		// System.out.println("visit subclassof called with " + arg0);
		// Left hand side
		Sentence leftstringSentence = arg0.getSubClass().accept(this);
		List<TextElement> leftstring = leftstringSentence.toTextElementSequence().getTextElements();
		List<TextElement> somethingthat = new ArrayList<TextElement>();
		somethingthat.add(somethingthatElement);
		
		
		if (arg0.getSubClass() instanceof OWLObjectSomeValuesFrom){
			OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) arg0.getSubClass();
			OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
//		 System.out.println("SUBCLASS SOMEOF DEBUG " + some1 + " " + cl);
			if (cl!=null){
				//somethingthat = cl.toString() + " that ";
				somethingthat =cl.accept(this).toTextElementSequence().getTextElements(); 
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
						somethingthat =cl.accept(this).toTextElementSequence().getTextElements(); 
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
		List<TextElement> rightstring = new ArrayList<TextElement>();
		
		
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
//			System.out.println("DEBUG! " + arg0.getSuperClass());
			
			//TODO implement this better
			// if(lang == Locale.ENGLISH)
			// 	middlestring.add(isElement);
					
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
					middle.addAll(cl.accept(this).toTextElementSequence().getTextElements());
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
		
			//System.out.println(" DEBUG (1) -- " +  filler.accept(this));
			fillerstrs.add(filler.accept(this).toTextElementSequence().getTextElements());
			Sentence propsentence = VerbalisationManager.textualisePropertyAsSentence(property, fillerstrs, middle);
			
			// System.out.println("leftstring " + leftstring);
			// System.out.println("middlestring " + middlestring);
			// System.out.println("rightstring " + rightstring);
			
			Sentence sentence = new Sentence(new TextElementSequence(leftstring),
					new TextElementSequence(),
					new TextElementSequence());
			sentence.concat(propsentence);
			
			return sentence;
			// return // leftstring
					// + " " 
					// + VerbalisationManager.textualiseProperty(property, fillerstrs, middle);
		}
		// Multiple Exists Pattern
		if (arg0.getSuperClass() instanceof OWLObjectIntersectionOf 
				&& VerbalisationManager.checkMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSuperClass())){
			// System.out.println("DEBUG : case of multiple exists patterns");
			middlestring.addAll(VerbalisationManager.textualiseMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSuperClass()));
			
			Sentence sentence = new Sentence(new TextElementSequence(leftstring),
					new TextElementSequence(middlestring),
					new TextElementSequence(rightstring));
			
			return sentence;
			// 		+ " " +
			// VerbalisationManager.pseudoNLStringMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSuperClass());
		}
		if (arg0.getSuperClass() instanceof OWLObjectIntersectionOf 
				&& checkMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass())){
			TextElementSequence seq = VerbalisationManager.textualiseMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass());
			middlestring.addAll(seq.getTextElements());
			
			// System.out.println("leftstring (2) " + leftstring);
			// System.out.println("middlestring (2) " + middlestring);
			// System.out.println("rightstring (2) " + rightstring);
			
			Sentence sentence = new Sentence(new TextElementSequence(leftstring),
					new TextElementSequence(middlestring),
					new TextElementSequence(rightstring));
			
			return sentence;
			// 		leftstring + " " +
			// VerbalisationManager.pseudoNLStringMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass());
		}
		
		//TODO implement this better
		// if(lang == Locale.GERMAN)
		// 	middlestring.add(isElement);

		middlestring.add(isElement);
		
		rightstring.addAll(arg0.getSuperClass().accept(this).toTextElementSequence().getTextElements());
		
		// System.out.println("leftstring (3) " + leftstring);
		// System.out.println("middlestring (3) " + middlestring);
		 // System.out.println("rightstring (3) " + rightstring);
		
		// System.out.println(arg0.getSuperClass().accept(this).toTextElementSequence());
		
		Sentence sentence = new Sentence(new TextElementSequence(leftstring),
				new TextElementSequence(middlestring),
				new TextElementSequence(rightstring));
		
		return  sentence; // + 
				//middlestring
				// + arg0.getSuperClass().accept(this);
	}
	
	// VERBALIZE OWLCLASS
	public Sentence visit(OWLClass ce) {
		// System.out.println("visiting OWL Class "  + ce);
		String clstr = VerbalisationManager.INSTANCE.getClassNLString(ce);
		String tooltiptext = ce.getIRI().toString();
		ClassElement clelem = new ClassElement(clstr,tooltiptext);

		Sentence sentence = new Sentence();
		sentence.setSubjekt(clelem);
		return sentence;
   }
	
	public Sentence verbaliseComplexIntersection(OWLObjectIntersectionOf arg0, Obfuscator obfuscator){
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
			seq.add(new LogicElement(LogicLabels.getString("that")));
			// head += " that ";
		}
		if (dataHasValueExps.size()==1){
			head += dataHasValueExps.get(0).accept(this);
			ClassElement headElement2 = new ClassElement(head);
			Sentence sentence = new Sentence();
			sentence.setSubjekt(headElement2);
			return sentence;
		}
		if (someExps.size()>0 && allExps.size()==0){
			head = VerbalisationManager.INSTANCE.verbaliseComplexIntersection(arg0,obfuscator);
			ClassElement headElement3 = new ClassElement(head);
			Sentence sentence = new Sentence();
			sentence.setSubjekt(headElement3);
			return sentence;
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
					seq4.add(new LogicElement(LogicLabels.getString("and"))) ;
				seq4.concat(new TextElementSequence(all.accept(this).toTextElementSequence().getTextElements()));
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
					seq4.add(new LogicElement(LogicLabels.getString(("and")))) ;
				List<TextElement> allelseString = allelse.accept(this).toTextElementSequence().getTextElements();
				if (allelseString == null){
					seq.add(new LogicElement("{NULL}"));
				}else{
					seq4.concat(new TextElementSequence(allelse.accept(this).toTextElementSequence().getTextElements()));
				}
				isFirst = false;
			}
		}
		// System.out.println(" fourth case, returning: " + seq4);
		Sentence sentence = new Sentence();
		sentence.addToSubject(seq4);
		return sentence;
	}
	

	public Sentence visit(OWLObjectIntersectionOf arg0) {
		// System.out.println(" intersection text visitor called with: " + arg0);
		Sentence result = new Sentence();
		LogicElement somethingthatElement = new LogicElement(LogicLabels.getString("somethingThat"));
		if (VerbalisationManager.checkMultipleExistsPattern(arg0)){
			result.setSubjekt(somethingthatElement);
			result.addToSubject(new TextElementSequence(VerbalisationManager.textualiseMultipleExistsPattern(arg0)));
			// System.out.println("visit intersect (1): " + new TextElementSequence(resultList).toString());
			return result;
		} else{
			if (checkMultipleExistsAndForallPattern(arg0)){
				result.setSubjekt(somethingthatElement);
				TextElementSequence seq = VerbalisationManager.textualiseMultipleExistsAndForallPattern(arg0);
				result.addToSubject(seq);
				// System.out.println("visit intersect (2): " + new TextElementSequence(resultList).toString());
				return result;
			} else{
					boolean onlysimpleclasses = true;
					for (OWLClassExpression exp: ((OWLObjectIntersectionOf) arg0).getOperandsAsList()){
						if (!(exp instanceof OWLClass)){onlysimpleclasses = false;}
					}
					if (onlysimpleclasses){
						String str = VerbalisationManager.INSTANCE.getSimpleIntersectionNLString(arg0);
						String tooltiptext = multipleTooltip(arg0.getOperandsAsList());
						result.setSubjekt(new ClassElement(str,tooltiptext));
						// System.out.println("visit intersect (3): " + new TextElementSequence(resultList).toString());
						return result;
						}
					result.concat(verbaliseComplexIntersection(arg0,obfuscator));
				}
			}
		// System.out.println("visit intersect (4): " + new TextElementSequence(resultList).toString());
		if(debug)
			System.out.println("_OWLObjectIntersectionOf arg0_");
		return result;
	}

	public Sentence visit(OWLObjectUnionOf arg0) {
		Sentence result = new Sentence();
		List<TextElement> resultList = new ArrayList<TextElement>();
		if (((OWLObjectUnionOf) arg0).getOperandsAsList().size()==2){
			List<OWLClassExpression> exprs = ((OWLObjectUnionOf) arg0).getOperandsAsList();
			// String res = VerbalisationManager.aggregateRepeated(exprs.get(0).accept(this), exprs.get(1).accept(this), " or ");
			return new Sentence();
					// VerbalisationManager.aggregateRepeated(exprs.get(0).accept(this), exprs.get(1).accept(this), " or ");
		}
		boolean firstp = true;
		for (OWLClassExpression exp: ((OWLObjectUnionOf) arg0).getOperandsAsList()){
			if (!firstp){
				LogicElement orElement = new LogicElement(LogicLabels.getString("or"));
				resultList.add(orElement);}
			firstp = false;
			result.addToSubject(exp.accept(this).toTextElementSequence());
			// resultstring = resultstring + exp.accept(this);
		}
		System.out.println("_OWLObjectUnionOf arg0_");
		return result;
	}

	
	public Sentence visit(OWLObjectComplementOf arg0) {
		Sentence result = new Sentence();
		LogicElement notElement = new LogicElement(LogicLabels.getString("not"));
		result.setSubjekt(notElement);
		result.addToSubject(arg0.getOperand().accept(this).toTextElementSequence());
		System.out.println("_OWLObjectComplementOf arg0_");
		return result;
	}

	public Sentence visit(OWLObjectAllValuesFrom existsexpr) {
		Sentence result = new Sentence();
		OWLObjectPropertyExpression property = existsexpr.getProperty();
		OWLClassExpression filler = existsexpr.getFiller();
		List<List<TextElement>> fillerstrs = new ArrayList<List<TextElement>>();
		List<TextElement> middlestring = new ArrayList<TextElement>();
		middlestring.add(new LogicElement(LogicLabels.getString("nothing but")));
		if (filler instanceof OWLObjectSomeValuesFrom)
			middlestring.add(new LogicElement(LogicLabels.getString("somethingThat")));
		fillerstrs.add(filler.accept(this).toTextElementSequence().getTextElements());
		
		System.out.println("_OWLObjectAllValuesFrom existsexpr_");

		
		List<TextElement> resultseq = VerbalisationManager.textualiseProperty(property, fillerstrs, middlestring);
		result.addToSubject(new TextElementSequence(resultseq));
		
		return result;
	}
	
	
	public Sentence visit(OWLObjectSomeValuesFrom existsexpr) {
		Sentence result = new Sentence();
		// System.out.println("DEBUG -- " + existsexpr);
		OWLObjectPropertyExpression property = existsexpr.getProperty();
		String propfragment = property.getNamedProperty().getIRI().getFragment();
		// Optional<String> propfragment = property.getNamedProperty().getIRI().getRemainder();
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
			middle.add(new LogicElement(LogicLabels.getString("somethingThat")));
			}
		}
		fillerstrs.add(filler.accept(this).toTextElementSequence().getTextElements());
		List<TextElement> resultseq = VerbalisationManager.textualiseProperty(property,fillerstrs,middle);
		// String str = VerbalisationManager.verbaliseProperty(property,fillerstrs,middle);
		// System.out.println("DEBUG visit " + str);
//		result.add(new LogicElement("_blabla_"));
		result.addToSubject(new TextElementSequence(resultseq));
		return result;
	}
	
	public Sentence visit(OWLEquivalentClassesAxiom arg0) {
		Sentence result = new Sentence();
		// List<TextElement> result = new ArrayList<TextElement>();
		OWLClass classexp = null;
		List<OWLClassExpression> exprs =  ((OWLEquivalentClassesAxiom) arg0).getClassExpressionsAsList();
		for (OWLClassExpression ex: exprs){
			if (ex instanceof OWLClass){
				classexp = (OWLClass) ex;
				break;
			}
		}
		if (classexp!=null){
			if(lang == Locale.ENGLISH){
				result.setSubjekt(new LogicElement(LogicLabels.getString("AccordingToItsDefinition")));
				result.addToSubject(classexp.accept(this).toTextElementSequence());
				result.setPraedikat(new LogicElement("is"));
				boolean firstp = true;
				for (OWLClassExpression ex:exprs){
					if (!ex.equals(classexp)){
							if (!firstp){
								result.setObjekt(new LogicElement(LogicLabels.getString("and")));
								firstp = false;
							}
							if (ex instanceof OWLObjectSomeValuesFrom)
								result.setObjekt(new LogicElement("something that"));
								result.addToObject(ex.accept(this).toTextElementSequence());
								
					}
				}
			}
			if(lang == Locale.GERMAN){
				result.setSubjekt(new LogicElement(LogicLabels.getString("AccordingToItsDefinition")));
				
				result.setPraedikat(new LogicElement(LogicLabels.getString("is")));
				result.addToObject(classexp.accept(this).toTextElementSequence());
				
				boolean firstp = true;
				for (OWLClassExpression ex:exprs){
					if (!ex.equals(classexp)){
							if (!firstp){
								result.setObjekt(new LogicElement(LogicLabels.getString("and")));
								firstp = false;
							}
							if (ex instanceof OWLObjectSomeValuesFrom)
								result.setObjekt(new LogicElement(LogicLabels.getString("something that")));
								result.addToSubject(ex.accept(this).toTextElementSequence());
								
					}
				}
				result.setOrder(SentenceOrder.A_is_B);;
			}
			else{}
			
			
		} else{
			result.addToSubject(exprs.get(0).accept(this).toTextElementSequence());
			// result += exprs.get(0).accept(this);
			result.setPraedikat(new LogicElement("is"));
			result.setObjekt(new LogicElement("the same as"));
			boolean firstp = true;
			for (OWLClassExpression ex:exprs){
				if (!ex.equals(exprs.get(0))){
						if (!firstp){
							result.setObjekt(new LogicElement("and"));
							firstp = false;
							}
						if (ex instanceof OWLObjectSomeValuesFrom)
							result.setObjekt(new LogicElement("something that"));
							result.addToObject(ex.accept(this).toTextElementSequence());
				}	
			}	
		}
		return result;
	}

	public Sentence visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("neg obj prop("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(arg0.getObject().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(arg0.getSubject().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLAsymmetricObjectPropertyAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("asymmetr("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLReflexiveObjectPropertyAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("reflexive("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLDisjointClassesAxiom arg0) {
		Sentence result = new Sentence();
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
			result.setSubjekt(new LogicElement("No"));
			List<TextElement> str = classexp.accept(this).toTextElementSequence().getTextElements();
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
			result.setPraedikat(new LogicElement("is"));
			// string += str + " is ";
			boolean firstp = true;
			for (OWLClassExpression exp : exprs){
				if (!exp.equals(classexp)){
				if (!firstp) {
					result.setObjekt(new LogicElement("or"));
					}
				firstp = false;
				result.addToObject(exp.accept(this).toTextElementSequence());
				}
			}
		}
		else{
			result.setSubjekt(new LogicElement("Nothing that is"));
			// resultstring += "Nothing that is ";
			boolean firstp = true;
			boolean twop = true;
			for (OWLClassExpression exp : exprs){	
				if (!twop) {
					result.setSubjekt(new LogicElement("or"));
					}
				result.addToSubject(exp.accept(this).toTextElementSequence());	
				if (firstp)
					{firstp = false;
					result.setPraedikat(new LogicElement("is"));
					}
				if (firstp=false)
					twop=false;
			}
		}
		return result;
	}

	public Sentence visit(OWLDataPropertyDomainAxiom arg0) {
		// TODO: treat this as syntactic sugar, convert this to the form \exists r. \top \sqsubseteq X and output
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("dom("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(arg0.getDomain().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLObjectPropertyDomainAxiom arg0) {
		// treat this as syntactic sugar, convert this to the form \exists r. \top \sqsubseteq X and output
		OWLObjectPropertyExpression prop = arg0.getProperty();
		OWLClassExpression classexp = arg0.getDomain();
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		OWLSubClassOfAxiom subclax = dataFactory.getOWLSubClassOfAxiom(
				dataFactory.getOWLObjectSomeValuesFrom(prop,dataFactory.getOWLThing()), classexp);
		return  subclax.accept(this);
	}

	public Sentence visit(OWLEquivalentObjectPropertiesAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("equivalent("));
		Set<OWLObjectPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLPropertyExpression exp : exprs){
		if (!firstp) {
			result.setSubjekt(new LogicElement(","));
			// resultstring = resultstring + ",";
			}
			firstp = false;
			result.addToSubject(exp.accept(this).toTextElementSequence());
			}
		result.setSubjekt(new LogicElement(")"));
		return result;
		
	}

	public Sentence visit(OWLNegativeDataPropertyAssertionAxiom arg0) {
		Sentence result = new Sentence();
		Sentence propSentence = arg0.getProperty().accept(this);
		Sentence objSentence = arg0.getObject().accept(this);
		Sentence subjSentence = arg0.getSubject().accept(this);
		result.setSubjekt(new LogicElement("neg dat prop: "));
		result.addToSubject(subjSentence.toTextElementSequence());
		result.addToPredicate(propSentence.toTextElementSequence());
		result.addToObject(objSentence.toTextElementSequence());
		return result;
	}

	public Sentence visit(OWLDifferentIndividualsAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("different individuals: "));
		List<OWLIndividual> exprs =  arg0.getIndividualsAsList();
		boolean firstp = true;
		for (OWLIndividual exp : exprs){
		if (!firstp) {
			result.setSubjekt(new LogicElement(","));
			}
			firstp = false;
			result.addToSubject(exp.accept(this).toTextElementSequence());
			}
		return result;
	}

	public Sentence visit(OWLDisjointDataPropertiesAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("disjoint: "));
		Set<OWLDataPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLDataPropertyExpression exp : exprs){
		if (!firstp) {
			result.setSubjekt(new LogicElement(","));
		}
			firstp = false;
			result.addToSubject(exp.accept(this).toTextElementSequence());
			}
		return result;
	}

	public Sentence visit(OWLDisjointObjectPropertiesAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("disjoint: "));
		Set<OWLObjectPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLObjectPropertyExpression exp : exprs){
		if (!firstp) {
			result.setSubjekt(new LogicElement(","));
		}
			firstp = false;
			result.addToSubject(exp.accept(this).toTextElementSequence());
			}
		return result;
	}

	public Sentence visit(OWLObjectPropertyRangeAxiom arg0) {
		Sentence result = new Sentence();
		OWLObjectPropertyExpression prop1 = arg0.getProperty();
		OWLClassExpression prop2 = arg0.getRange();
		result.setSubjekt(new LogicElement("range("));
		result.addToSubject(prop1.accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(prop2.accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
		
	}

	public Sentence visit(OWLObjectPropertyAssertionAxiom arg0) {
		Sentence result = new Sentence();
		OWLIndividual subject = arg0.getSubject();
		OWLIndividual object = arg0.getObject();
		result.addToSubject(subject.accept(this).toTextElementSequence());
		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(arg0.getProperty());
		TextElement propElement = new RoleElement(propstring);
		result.setSubjekt((propElement));
		result.addToSubject(object.accept(this).toTextElementSequence());
		return result;
		// return "{IMPLEMENT ME obj prop ass : " 
	    // + arg0.getProperty().accept(this) +  arg0.getObject().accept(this) 
	    // + "}";
	}

	public Sentence visit(OWLFunctionalObjectPropertyAxiom arg0) {
		Sentence result = new Sentence();
		return result;
		// return "functional" + "(" + arg0.getProperty().accept(this) + ")";		
	}

	public Sentence visit(OWLSubObjectPropertyOfAxiom arg0) {
		Sentence result = new Sentence();
		return result;
		// return "to " + arg0.getSubProperty().accept(this) + " is a subproperty of " + arg0.getSuperProperty().accept(this); 
	}

	public Sentence visit(OWLDisjointUnionAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("disjoint-union("));
		Set<OWLClassExpression> exprs =  arg0.getClassExpressions();
		boolean firstp = true;
		for (OWLClassExpression exp : exprs){
		if (!firstp) {
			result.setSubjekt(new LogicElement(","));
		}
			firstp = false;
			result.addToSubject(exp.accept(this).toTextElementSequence());
			}
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLDeclarationAxiom arg0) {
		Sentence result = new Sentence();
		return result;
		// return "{IMPLEMENT ME decl ax : "  + arg0.getEntity() + "}";
	}

	public Sentence visit(OWLAnnotationAssertionAxiom arg0) {
		Sentence result = new Sentence();
		return result;
		// return "{IMPLEMENT ME annot ax : " + arg0.getSubject().accept(this)  
		// 		+ arg0.getProperty().accept(this) + arg0.getValue().accept(this)  + "}";
	}

	public Sentence visit(OWLSymmetricObjectPropertyAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("symm("));
		result.addToSubject((arg0.getProperty().accept(this)).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLDataPropertyRangeAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("has range"));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(" -- "));
		result.addToSubject(arg0.getRange().accept(this).toTextElementSequence());
		return result;
		// return arg0.getProperty().accept(this) + " has range " + arg0.getRange().accept(this); 
	}

	public Sentence visit(OWLFunctionalDataPropertyAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("functional("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLEquivalentDataPropertiesAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("equivalent-dataprops("));
		Set<OWLDataPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLDataPropertyExpression exp : exprs){
		if (!firstp) {
			result.setSubjekt(new LogicElement(","));
			}
			firstp = false;
			result.addToSubject(exp.accept(this).toTextElementSequence());
			}
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLClassAssertionAxiom arg0) {
		Sentence result = new Sentence();
		result.addToSubject(arg0.getIndividual().accept(this).toTextElementSequence());
		result.setPraedikat(new LogicElement(LogicLabels.getString("is")));
		result.addToObject(arg0.getClassExpression().accept(this).toTextElementSequence());
		return result;
	} 

	public Sentence visit(OWLDataPropertyAssertionAxiom arg0) {
		Sentence result = new Sentence();
		result.addToSubject(arg0.getSubject().accept(this).toTextElementSequence());
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		return result;
	}

	public Sentence visit(OWLTransitiveObjectPropertyAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("trans("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLIrreflexiveObjectPropertyAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("irrefl("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLSubDataPropertyOfAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("to"));
		result.addToSubject(arg0.getSubProperty().accept(this).toTextElementSequence());
		result.setPraedikat(new LogicElement("is"));
		result.setObjekt(new LogicElement("a subproperty of"));
		result.addToObject(arg0.getSuperProperty().accept(this).toTextElementSequence());
		return result;
	}

	public Sentence visit(OWLInverseFunctionalObjectPropertyAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("inv-funct("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLSameIndividualAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("same-individual("));
		List<OWLIndividual> exprs =  arg0.getIndividualsAsList();
		boolean firstp = true;
		for (OWLIndividual exp : exprs){
		if (!firstp) {
			result.setSubjekt(new LogicElement(","));
		}
			firstp = false;
			result.addToSubject(exp.accept(this).toTextElementSequence());
			}
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLSubPropertyChainOfAxiom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("property-chain("));
		List<OWLObjectPropertyExpression> exprs =  arg0.getPropertyChain();
		boolean firstp = true;
		for (OWLObjectPropertyExpression exp : exprs){
		if (!firstp) {
			result.setSubjekt(new LogicElement(","));
			}
			firstp = false;
			result.addToSubject(exp.accept(this).toTextElementSequence());
			}
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLInverseObjectPropertiesAxiom arg0) {
			Sentence result = new Sentence();
			OWLObjectPropertyExpression expr1 =   arg0.getFirstProperty();
			OWLObjectPropertyExpression expr2 =   arg0.getSecondProperty();
			result.setSubjekt(new LogicElement("inverse("));
			result.addToSubject(expr1.accept(this).toTextElementSequence());
			result.setSubjekt(new LogicElement(","));
			result.addToSubject(expr2.accept(this).toTextElementSequence());
			result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLHasKeyAxiom arg0) {
		return new Sentence();
	}

	public Sentence visit(OWLDatatypeDefinitionAxiom arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence visit(SWRLRule arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence visit(OWLSubAnnotationPropertyOfAxiom arg0) {
		Sentence result = new Sentence();
		result.addToSubject((arg0.getSubProperty().accept(this)).toTextElementSequence());
		result.setPraedikat(new LogicElement("is"));
		result.setObjekt(new LogicElement("sub-annotiation property of"));
		result.addToSubject(arg0.getSuperProperty().accept(this).toTextElementSequence());
		return result;
	}

	public Sentence visit(OWLAnnotationPropertyDomainAxiom arg0) {
		Sentence result = new Sentence();
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setPraedikat(new LogicElement("has domain"));
		result.setObjekt(new LogicElement("domain"));
		result.addToSubject(arg0.getDomain().accept(this).toTextElementSequence());
		return result;
	}

	public Sentence visit(OWLAnnotationPropertyRangeAxiom arg0) {
		Sentence result = new Sentence();
		OWLAnnotationProperty prop1 = arg0.getProperty();
		IRI prop2 = arg0.getRange();
		result.setSubjekt(new LogicElement("range("));
		result.addToSubject(prop1.accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(prop2.accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLObjectHasValue arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("has value("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(arg0.getValue().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result; 
	}

	public Sentence visit(OWLObjectMinCardinality arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement(">="));
		String card = "" + arg0.getCardinality();
		result.setSubjekt(new LogicElement(card));
		result.setSubjekt(new LogicElement("("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(arg0.getFiller().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLObjectExactCardinality arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("="));
		String card = "" + arg0.getCardinality();
		result.setSubjekt(new LogicElement(card));
		result.setSubjekt(new LogicElement("("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(arg0.getFiller().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLObjectMaxCardinality arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("<="));
		String card = "" + arg0.getCardinality();
		result.setSubjekt(new LogicElement(card));
		result.setSubjekt(new LogicElement("("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(arg0.getFiller().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLObjectHasSelf arg0) {
		// TODO Auto-generated method stub
				return new Sentence();
	}

	public Sentence visit(OWLObjectOneOf arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence visit(OWLDataSomeValuesFrom arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("something that"));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.addToSubject(arg0.getFiller().accept(this).toTextElementSequence());
		return result;
	}

	public Sentence visit(OWLDataAllValuesFrom arg0) {
		Sentence result = new Sentence();
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.addToSubject(arg0.getFiller().accept(this).toTextElementSequence());
		return result;
	}

	public Sentence visit(OWLDataHasValue arg0) {
		Sentence result = new Sentence();
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(arg0.getValue().getLiteral()));
		return result;
	}

	public Sentence visit(OWLDataMinCardinality arg0) {
		String resultstring = "";
		resultstring = resultstring + "min#" + "(" + arg0.getCardinality() 
				+ ","+ arg0.getProperty().accept(this);
		resultstring = resultstring + "," +  arg0.getFiller().accept(this) +")";
		return new Sentence();
	}

	public Sentence visit(OWLDataExactCardinality arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("="));
		String card = "" + arg0.getCardinality();
		result.setSubjekt(new LogicElement(card));
		result.setSubjekt(new LogicElement("("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(arg0.getFiller().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLDataMaxCardinality arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("<="));
		String card = "" + arg0.getCardinality();
		result.setSubjekt(new LogicElement(card));
		result.setSubjekt(new LogicElement("("));
		result.addToSubject(arg0.getProperty().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(","));
		result.addToSubject(arg0.getFiller().accept(this).toTextElementSequence());
		result.setSubjekt(new LogicElement(")"));
		return result;
	}

	public Sentence visit(OWLDatatype arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("as indicated by a"));
		if (arg0.isBoolean()){
			if (arg0.toString().equals("xsd:boolean")){
				result.setSubjekt(new LogicElement("boolean"));
				return result;
			}
		}
		result.setSubjekt(new LogicElement(arg0.toString()));
		return result;
	}

	public Sentence visit(OWLDataComplementOf arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("NI-1002"));
		return result;
	}

	public Sentence visit(OWLDataOneOf arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("of one of the following:"));
		// TODO Auto-generated method stub
		Set<OWLLiteral> literals = arg0.getValues();
		boolean firstP = true;
		for (OWLLiteral lit: literals){
			if (!firstP)
				result.setSubjekt(new LogicElement(","));
			result.addToSubject(lit.accept(this).toTextElementSequence());
			firstP = false;
		}
		return result;
	}

	public Sentence visit(OWLDataIntersectionOf arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("NI-1003"));
		return result;
	}

	public Sentence visit(OWLDataUnionOf arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("NI-1004"));
		return result;
	}

	public Sentence visit(OWLDatatypeRestriction arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("NI-1005"));
		return result;
	}

	public Sentence visit(OWLLiteral arg0) {
		Sentence result = new Sentence();
		TextElementSequence seq = new TextElementSequence();
		seq.add(new LogicElement(arg0.getLiteral()));
		result.addToSubject(seq);
		return result;
	}

	public Sentence visit(OWLFacetRestriction arg0) {
		Sentence result = new Sentence();
		result.setSubjekt(new LogicElement("NI-1006"));
		return result;
	}

	public Sentence visit(OWLObjectProperty arg0) {
		Sentence result = new Sentence();
		TextElementSequence seq = new TextElementSequence();
		seq.add(new RoleElement(arg0.getNamedProperty().toString()));
		result.addToSubject(seq);
		return result;
	}

	public Sentence visit(OWLObjectInverseOf arg0) {
		Sentence result = new Sentence();
		TextElementSequence seq = new TextElementSequence();
		seq.add(new LogicElement("not"));
		Sentence innerseq = arg0.getInverse().accept(this);
		seq.concat(innerseq.toTextElementSequence());
		result.addToSubject(seq);
		return result;
	}

	public Sentence visit(OWLDataProperty arg0) {
		Sentence result = new Sentence();
		String str = VerbalisationManager.INSTANCE.getDataPropertyNL(arg0);
		result.setSubjekt(new LogicElement(str));
		return result;
		// return arg0.getIRI().getFragment().toString();
	}

	public Sentence visit(OWLNamedIndividual arg0) {
		Sentence result = new Sentence();
		if (VerbaliseTreeManager.locale.equals(Locale.GERMAN)){
			if (VerbalisationManager.INSTANCE.getLabel(arg0,"de")!=null){
				LogicElement elem = new LogicElement(VerbalisationManager.INSTANCE.getLabel(arg0,"de"));
				result.setSubjekt(elem);
				return result;
				}
			}
		result.setSubjekt(new LogicElement(arg0.getIRI().getFragment()));
		return result;
	}

	public Sentence visit(OWLAnnotationProperty arg0) {
		Sentence sentence = new Sentence();
		sentence.setSubjekt(new LogicElement(arg0.getIRI().getFragment()));
		return sentence;
	}

	public Sentence visit(OWLAnnotation arg0) {
		
		Sentence sen1 = arg0.getProperty().accept(this);
		Sentence sen2 = arg0.getValue().accept(this);
		
		Sentence sentence = new Sentence(
				new TextElementSequence(sen1.toTextElementSequence().getTextElements()),
				new TextElementSequence(sen2.toTextElementSequence().getTextElements()),
				(new TextElementSequence())
				);
		return sentence;
	}

	public Sentence visit(IRI arg0) {
		Sentence sentence = new Sentence();
		sentence.setSubjekt(new LogicElement(arg0.getFragment()));
		return sentence;
	}

	public Sentence  visit(OWLAnonymousIndividual arg0) {
		Sentence sentence = new Sentence();
		sentence.setSubjekt(new LogicElement("not-implemented-1008"));
		return sentence;		
	}

	public Sentence  visit(SWRLClassAtom arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(SWRLDataRangeAtom arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(SWRLObjectPropertyAtom arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(SWRLDataPropertyAtom arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(SWRLBuiltInAtom arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(SWRLVariable arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(SWRLIndividualArgument arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(SWRLLiteralArgument arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(SWRLSameIndividualAtom arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(SWRLDifferentIndividualsAtom arg0) {
		// TODO Auto-generated method stub
		return new Sentence();
	}

	public Sentence  visit(OWLOntology arg0) {
		Sentence sentence = new Sentence();
		sentence.setSubjekt(new LogicElement("ontology: "));
		sentence.setSubjekt(new LogicElement(arg0.getOntologyID().toString()));
		return sentence;
		
	}

    public static String multipleTooltip(List<OWLClassExpression> args){
    	String result = "";
    	for (OWLClassExpression cls : args){
    		result += cls.asOWLClass().getIRI().toString();
    	}
    	return result;
    }   
	
}
