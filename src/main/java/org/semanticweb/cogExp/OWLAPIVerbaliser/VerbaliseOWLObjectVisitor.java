package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;
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

import com.google.common.base.Optional;

public class VerbaliseOWLObjectVisitor implements OWLObjectVisitorEx<String>{
	
	private static final String _space = VerbalisationManager.INSTANCE._space;
	
	private Obfuscator obfuscator;
	
	private static ResourceBundle LogicLabels = ResourceBundle.getBundle("LogicLabels", VerbaliseTreeManager.locale);

	
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
				existsexprs.addAll(VerbaliseOWLObjectVisitor.collectAndExpressions(expr));
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
		if (str.length()<1) 
			return "";
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
		if (str.length()>u && Character.isUpperCase(str.charAt(u+1)))
				return false; // <-- acronym case
		for (int i=u+2; i<str.length(); i++){
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
		boolean second = false;
		for (int i=0; i<str.length(); i++){
			if (Character.isUpperCase(str.charAt(i))){
				u = i;
				break;
			}
		}
		for (int j=u+2; j<str.length(); j++){
			if (Character.isUpperCase(str.charAt(j))){
				second = true;
				break;
			}
		}
		if (!second){
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
	
	public static boolean detectAcronym(String str){
		if (str.length()>1 
				&& Character.isUpperCase(str.charAt(0))
				&& Character.isUpperCase(str.charAt(1))
				){
			return true;
		}
		return false;
	}
	
	// VERBALIZE SUBCLASSOFAXIOM
	public String visit(OWLSubClassOfAxiom arg0) {
		// System.out.println("visit subclassof called ");
		// Left hand side
		String leftstring = "";
		leftstring = arg0.getSubClass().accept(this);
		String somethingthat = "something that ";
		if (arg0.getSubClass() instanceof OWLObjectSomeValuesFrom){
			OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) arg0.getSubClass();
			OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
			if (cl!=null){
				//somethingthat = cl.toString() + " that ";
				somethingthat = cl.accept(this) + " that ";
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
						somethingthat = cl.accept(this) + " that ";
					}
				}
			}
		}	
		if (arg0.getSubClass() instanceof OWLObjectIntersectionOf 
				&& VerbalisationManager.checkMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSubClass())){
			leftstring = somethingthat 
				 + VerbalisationManager.pseudoNLStringMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSubClass(),obfuscator) ;
		}
		if (arg0.getSubClass() instanceof OWLObjectIntersectionOf 
				&& checkMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSubClass())){
			leftstring = somethingthat 
				+ VerbalisationManager.pseudoNLStringMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSubClass()) 
				+ ",";
		}
		if (arg0.getSubClass() instanceof OWLObjectSomeValuesFrom){
			leftstring = somethingthat + leftstring;
		}
		String middlestring = " ";
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
			leftstring = leftstring + " is";
		}
		// this catches the simple case where the superclass is only a single existential
		// ... in this case, the "something that" is skipped.
		if (arg0.getSuperClass() instanceof OWLObjectSomeValuesFrom){
			OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) arg0.getSuperClass();
			OWLClassExpression filler = some.getFiller();
			OWLObjectPropertyExpression property = some.getProperty();
			List<String> fillerstrs = new ArrayList<String>();
			String middle = "";
			if (filler instanceof OWLObjectSomeValuesFrom){
				OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) filler;
				OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
				if (cl!=null){
					// middle = cl.toString() + " that ";
					middle = cl.accept(this) + " that ";
				}
				else
				middle = "something that ";
			}
			if (arg0.getSuperClass() instanceof OWLObjectIntersectionOf 
					&& checkMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass())){
				OWLObjectIntersectionOf intsect =  (OWLObjectIntersectionOf) arg0.getSuperClass();
				OWLClassExpression clexpr = intsect.getOperandsAsList().get(0);
				List<OWLClassExpression> exprs = collectAndExpressions(clexpr);
				middle = "something that ";
				for (OWLClassExpression expr : exprs){
					if (expr instanceof OWLObjectSomeValuesFrom){
						OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) expr;
						OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
						if (cl!=null){
							middle = cl.toString() + " that ";
						}
					}
				}
			}	
			fillerstrs.add(filler.accept(this));
			return leftstring
					+ " " 
					+ VerbalisationManager.verbaliseProperty(property, fillerstrs, middle,obfuscator);
		}
		if (arg0.getSuperClass() instanceof OWLObjectIntersectionOf 
				&& VerbalisationManager.checkMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSuperClass())){
			return leftstring
					+ " " +
			VerbalisationManager.pseudoNLStringMultipleExistsPattern((OWLObjectIntersectionOf) arg0.getSuperClass(),obfuscator);
		}
		if (arg0.getSuperClass() instanceof OWLObjectIntersectionOf 
				&& checkMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass())){
			return 
					leftstring + " " +
			VerbalisationManager.pseudoNLStringMultipleExistsAndForallPattern((OWLObjectIntersectionOf) arg0.getSuperClass());
		}
		return  leftstring + 
				middlestring
				+ arg0.getSuperClass().accept(this);
	}
	
	// VERBALIZE OWLCLASS
	public String visit(OWLClass ce) {
		// System.out.println("visiting OWL Class "  + ce);
		return VerbalisationManager.INSTANCE.getClassNLString(ce);
   }
	
	public String verbaliseComplexIntersection(OWLObjectIntersectionOf arg0){
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
		String head = VerbalisationManager.INSTANCE.getSimpleIntersectionNLString(classExps);
		// is there more?
		if (someExps.size()>0 || allExps.size()>0 || allelseExps.size()>0 || someDataExps.size()>0 || dataHasValueExps.size()>0){
			head += " that ";
		}
		if (dataHasValueExps.size()==1){
			head += dataHasValueExps.get(0).accept(this);
			return head;
		}
		if (someExps.size()>0 && allExps.size()==0){
			head = VerbalisationManager.INSTANCE.verbaliseComplexIntersection(arg0,obfuscator);
			return head;
			/*
			// TODO: this is not aggregating in case there is a multiple exists pattern
			boolean isFirst = true;
			for(OWLObjectSomeValuesFrom some : someExps){
				if (!isFirst)
					head += " and ";
				head += some.accept(this);
				isFirst = false;
			}
			*/
		}
		if (someExps.size()>0 && allExps.size()>0)
					head += " and ";
		if (allExps.size()>0){
			// TODO: this is not aggregating in case there is a multiple exists pattern
			boolean isFirst = true;
			for(OWLObjectAllValuesFrom all : allExps){
				if (!isFirst)
					head += " and ";
				head += all.accept(this);
				isFirst = false;
			}
		}
		if (allExps.size()>0 && allelseExps.size()>0)
					head += " and ";
		if (allelseExps.size()>0){
			head += "is ";
			// TODO: this is not aggregating in case there is a multiple exists pattern
			boolean isFirst = true;
			for(OWLClassExpression allelse : allelseExps){
				if (!isFirst)
					head += " and ";
				head += allelse.accept(this);
				isFirst = false;
			}
		}
		return head;
	}
	

	public String visit(OWLObjectIntersectionOf arg0) {
		String resultstring = "";
		if (VerbalisationManager.checkMultipleExistsPattern(arg0)){
			return " something that " + VerbalisationManager.pseudoNLStringMultipleExistsPattern(arg0,obfuscator);
		} else{
			if (checkMultipleExistsAndForallPattern(arg0)){
				return " something that " + VerbalisationManager.pseudoNLStringMultipleExistsAndForallPattern(arg0);
			} else{
					boolean onlysimpleclasses = true;
					for (OWLClassExpression exp: ((OWLObjectIntersectionOf) arg0).getOperandsAsList()){
						if (!(exp instanceof OWLClass)){onlysimpleclasses = false;}
					}
					if (onlysimpleclasses){
						return VerbalisationManager.INSTANCE.getSimpleIntersectionNLString(arg0);}
					resultstring = verbaliseComplexIntersection(arg0);
				}
			}
		return resultstring;
	}

	public String visit(OWLObjectUnionOf arg0) {
		String resultstring = "";
		if (((OWLObjectUnionOf) arg0).getOperandsAsList().size()==2){
			List<OWLClassExpression> exprs = ((OWLObjectUnionOf) arg0).getOperandsAsList();
			return 
					VerbalisationManager.aggregateRepeated(exprs.get(0).accept(this), exprs.get(1).accept(this), " or ");
		}
		boolean firstp = true;
		for (OWLClassExpression exp: ((OWLObjectUnionOf) arg0).getOperandsAsList()){
			if (!firstp){ resultstring = resultstring + " or ";}
			firstp = false;
			resultstring = resultstring + exp.accept(this);
		}
		return resultstring;
	}

	
	public String visit(OWLObjectComplementOf arg0) {
		String resultstring = "not " + 
				arg0.getOperand().accept(this); 
		return resultstring;
	}

	public String visit(OWLObjectAllValuesFrom existsexpr) {
		OWLObjectPropertyExpression property = existsexpr.getProperty();
		OWLClassExpression filler = existsexpr.getFiller();
		List<String> fillerstrs = new ArrayList<String>();
		String middlestring = "nothing but ";
		if (filler instanceof OWLObjectSomeValuesFrom)
			middlestring = middlestring + "something that ";
		fillerstrs.add(filler.accept(this));
		return VerbalisationManager.verbaliseProperty(property, fillerstrs, middlestring,obfuscator);
	}
	
	
	public String visit(OWLObjectSomeValuesFrom existsexpr) {
		// System.out.println("DEBUG -- " + existsexpr);
		OWLObjectPropertyExpression property = existsexpr.getProperty();
		String propfragment = property.getNamedProperty().getIRI().getFragment();
		// Optional<String> propfragment = property.getNamedProperty().getIRI().getRemainder();
		OWLClassExpression filler = existsexpr.getFiller();
		List<String> fillerstrs = new ArrayList<String>();
		String middle = "";
		if(filler instanceof OWLObjectSomeValuesFrom){
			OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) filler;
			OWLClassExpression cl = VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
			if (cl !=null){
				middle = cl.toString();
			}else{
			middle = "something that ";
			}
		}
		fillerstrs.add(filler.accept(this));
		
		String str = VerbalisationManager.verbaliseProperty(property,fillerstrs,middle,obfuscator);
		// System.out.println("DEBUG visit " + str);
		return str;
	}
	
	public String visit(OWLEquivalentClassesAxiom arg0) {
		String result = "";
		OWLClass classexp = null;
		List<OWLClassExpression> exprs =  ((OWLEquivalentClassesAxiom) arg0).getClassExpressionsAsList();
		for (OWLClassExpression ex: exprs){
			if (ex instanceof OWLClass){
				classexp = (OWLClass) ex;
				break;
			}
		}
		if (classexp!=null){
			result += LogicLabels.getString("AccordingToItsDefinition");
			result += classexp.accept(this);
			result += " is ";
			boolean firstp = true;
			for (OWLClassExpression ex:exprs){
				if (!ex.equals(classexp)){
						if (!firstp){
							result += "and ";
							firstp = false;
							}
						if (ex instanceof OWLObjectSomeValuesFrom)
							result += "something that ";
						result += ex.accept(this);
				}	
			}		
		} else{
			result += exprs.get(0).accept(this);
			result += LogicLabels.getString("isTheSameAs");
			boolean firstp = true;
			for (OWLClassExpression ex:exprs){
				if (!ex.equals(exprs.get(0))){
						if (!firstp){
							result += "and ";
							firstp = false;
							}
						if (ex instanceof OWLObjectSomeValuesFrom)
							result += "something that ";
						result += ex.accept(this);
				}	
			}	
		}
		return result;
	}

	public String visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
		return "neg obj prop(" + arg0.getProperty().accept(this) + "," 
						+ arg0.getObject().accept(this) + "," 
						+ arg0.getSubject().accept(this) + ")";
	}

	public String visit(OWLAsymmetricObjectPropertyAxiom arg0) {
		return "asymmetr" + "(" + arg0.getProperty().accept(this) + ")";
	}

	public String visit(OWLReflexiveObjectPropertyAxiom arg0) {
		return "reflexive" + "(" + arg0.getProperty().accept(this) + ")";
	}

	public String visit(OWLDisjointClassesAxiom arg0) {
		String resultstring = "";
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
			resultstring += "No ";
			String str = classexp.accept(this);
			if (str.indexOf("a ")==0)
				str = str.substring(2);
			if (str.indexOf("an ")==0)
				str = str.substring(3);
			resultstring += str + " is ";
			boolean firstp = true;
			for (OWLClassExpression exp : exprs){
				if (!exp.equals(classexp)){
				if (!firstp) {resultstring = resultstring + " or ";}
				firstp = false;
				resultstring = resultstring + exp.accept(this);
				}
			}
		}
		else{
			resultstring += "Nothing that is ";
			boolean firstp = true;
			boolean twop = true;
			for (OWLClassExpression exp : exprs){	
				if (!twop) {resultstring = resultstring + " or ";}
				resultstring = resultstring + exp.accept(this);		
				if (firstp)
					{firstp = false;
					resultstring = resultstring + " is ";
					}
				if (firstp=false)
					twop=false;
			}
		}
		return resultstring;
	}

	public String visit(OWLDataPropertyDomainAxiom arg0) {
		// TODO: treat this as syntactic sugar, convert this to the form \exists r. \top \sqsubseteq X and output
		return "dom(" + arg0.getProperty().accept(this) + "," + arg0.getDomain() + ")";
	}

	public String visit(OWLObjectPropertyDomainAxiom arg0) {
		// treat this as syntactic sugar, convert this to the form \exists r. \top \sqsubseteq X and output
		/*
		OWLObjectPropertyExpression prop = arg0.getProperty();
		OWLClassExpression classexp = arg0.getDomain();
		OWLDataFactory dataFactory=OWLAPIManagerManager.INSTANCE.getDataFactory();
		OWLSubClassOfAxiom subclax = dataFactory.getOWLSubClassOfAxiom(
				dataFactory.getOWLObjectSomeValuesFrom(prop,dataFactory.getOWLThing()), classexp);
		return  subclax.accept(this);
		*/
		OWLObjectPropertyExpression prop = arg0.getProperty();
		OWLClassExpression classexp = arg0.getDomain();
		return "anything that " + VerbalisationManager.INSTANCE.getPropertyNLString(prop) + " is " + classexp.accept(this);
	}

	public String visit(OWLEquivalentObjectPropertiesAxiom arg0) {
		String resultstring = "equivalent(";
		Set<OWLObjectPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLPropertyExpression exp : exprs){
		if (!firstp) {resultstring = resultstring + ",";}
			firstp = false;
			resultstring = resultstring + exp.accept(this);
			}
		return resultstring + ")";
		
	}

	public String visit(OWLNegativeDataPropertyAssertionAxiom arg0) {
		return "neg dat prop(" + arg0.getProperty().accept(this) + "," 
				+ arg0.getObject().accept(this) + "," 
				+ arg0.getSubject().accept(this) + ")";
	}

	public String visit(OWLDifferentIndividualsAxiom arg0) {
		String resultstring = "different(";
		List<OWLIndividual> exprs =  arg0.getIndividualsAsList();
		boolean firstp = true;
		for (OWLIndividual exp : exprs){
		if (!firstp) {resultstring = resultstring + ",";}
			firstp = false;
			resultstring = resultstring + exp.accept(this);
			}
		return resultstring + ")";
	}

	public String visit(OWLDisjointDataPropertiesAxiom arg0) {
		String resultstring = "disjoint(";
		Set<OWLDataPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLDataPropertyExpression exp : exprs){
		if (!firstp) {resultstring = resultstring + ",";}
			firstp = false;
			resultstring = resultstring + exp.accept(this);
			}
		return resultstring + ")";
	}

	public String visit(OWLDisjointObjectPropertiesAxiom arg0) {
		String resultstring = "disjoint(";
		Set<OWLObjectPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLObjectPropertyExpression exp : exprs){
		if (!firstp) {resultstring = resultstring + ",";}
			firstp = false;
			resultstring = resultstring + exp.accept(this);
			}
		return resultstring + ")";
	}

	public String visit(OWLObjectPropertyRangeAxiom arg0) {
		String resultstring = "";
		OWLObjectPropertyExpression prop1 = arg0.getProperty();
		OWLClassExpression prop2 = arg0.getRange();
		// resultstring = resultstring + "range(" +  prop1.accept(this) +  "," 
		// + prop2.accept(this) + ")";
		resultstring = resultstring + "what " +  VerbalisationManager.INSTANCE.getPropertyNLString(prop1) +  " is " 
				+ prop2.accept(this) + "";
		return resultstring;
		
	}

	public String visit(OWLObjectPropertyAssertionAxiom arg0) {
		return "{IMPLEMENT ME obj prop ass : " 
	    + arg0.getProperty().accept(this) +  arg0.getObject().accept(this) 
	    + "}";
	}

	public String visit(OWLFunctionalObjectPropertyAxiom arg0) {
		return "functional" + "(" + arg0.getProperty().accept(this) + ")";		
	}

	public String visit(OWLSubObjectPropertyOfAxiom arg0) {
		return "to " + arg0.getSubProperty().accept(this) + " is a subproperty of " + arg0.getSuperProperty().accept(this); 
	}

	public String visit(OWLDisjointUnionAxiom arg0) {
		String resultstring = "disjoint-union(";
		Set<OWLClassExpression> exprs =  arg0.getClassExpressions();
		boolean firstp = true;
		for (OWLClassExpression exp : exprs){
		if (!firstp) {resultstring = resultstring + ",";}
			firstp = false;
			resultstring = resultstring + exp.accept(this);
			}
		return resultstring + ")";
	}

	public String visit(OWLDeclarationAxiom arg0) {
		return "{IMPLEMENT ME decl ax : "  + arg0.getEntity() + "}";
	}

	public String visit(OWLAnnotationAssertionAxiom arg0) {
		return "{IMPLEMENT ME annot ax : " + arg0.getSubject().accept(this)  
				+ arg0.getProperty().accept(this) + arg0.getValue().accept(this)  + "}";
	}

	public String visit(OWLSymmetricObjectPropertyAxiom arg0) {
		String resultstring = "";
		resultstring = resultstring + "symm" + "(" + arg0.getProperty().accept(this);
		resultstring = resultstring + ")";
		return resultstring;
	}

	public String visit(OWLDataPropertyRangeAxiom arg0) {
		return arg0.getProperty().accept(this) + " has range " + arg0.getRange().accept(this); 
	}

	public String visit(OWLFunctionalDataPropertyAxiom arg0) {
		return "functional" + "(" + arg0.getProperty().accept(this) + ")";
	}

	public String visit(OWLEquivalentDataPropertiesAxiom arg0) {
		String resultstring = "equivalent-dataprops(";
		Set<OWLDataPropertyExpression> exprs =  arg0.getProperties();
		boolean firstp = true;
		for (OWLDataPropertyExpression exp : exprs){
		if (!firstp) {resultstring = resultstring + ",";}
			firstp = false;
			resultstring = resultstring + exp.accept(this);
			}
		return resultstring + ")";
	}

	public String visit(OWLClassAssertionAxiom arg0) {
		return arg0.getIndividual().accept(this) + " is " + arg0.getClassExpression().accept(this);
	} 

	public String visit(OWLDataPropertyAssertionAxiom arg0) {
		return arg0.getSubject() + arg0.getProperty().accept(this);
	}

	public String visit(OWLTransitiveObjectPropertyAxiom arg0) {
		return "trans" + "(" + arg0.getProperty().accept(this) + ")";
	}

	public String visit(OWLIrreflexiveObjectPropertyAxiom arg0) {
		return "irrefl" + "(" + arg0.getProperty().accept(this) + ")";
	}

	public String visit(OWLSubDataPropertyOfAxiom arg0) {
		return "to " + arg0.getSubProperty().accept(this) + " is a subproperty of " + arg0.getSuperProperty().accept(this); 
		
	}

	public String visit(OWLInverseFunctionalObjectPropertyAxiom arg0) {
		return "inv-funct" + "(" + arg0.getProperty().accept(this) + ")";
	}

	public String visit(OWLSameIndividualAxiom arg0) {
		String resultstring = "same-individual(";
		List<OWLIndividual> exprs =  arg0.getIndividualsAsList();
		boolean firstp = true;
		for (OWLIndividual exp : exprs){
		if (!firstp) {resultstring = resultstring + ",";}
			firstp = false;
			resultstring = resultstring + exp.accept(this);
			}
		return resultstring + ")";
	}

	public String visit(OWLSubPropertyChainOfAxiom arg0) {
		
		String resultstring = "property-chain(";
		List<OWLObjectPropertyExpression> exprs =  arg0.getPropertyChain();
		boolean firstp = true;
		for (OWLObjectPropertyExpression exp : exprs){
		if (!firstp) {resultstring = resultstring + ",";}
			firstp = false;
			resultstring = resultstring + exp.accept(this);
			}
		return resultstring + ")";
	}

	public String visit(OWLInverseObjectPropertiesAxiom arg0) {
			String resultstring = "";
			OWLObjectPropertyExpression expr1 =   arg0.getFirstProperty();
			OWLObjectPropertyExpression expr2 =   arg0.getSecondProperty();
			resultstring = resultstring + "inverse(" +  expr1.accept(this) + ","+ expr2.accept(this) + ")";
		return resultstring;
	}

	public String visit(OWLHasKeyAxiom arg0) {
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

	public String visit(OWLSubAnnotationPropertyOfAxiom arg0) {
		return arg0.getSubProperty().accept(this) + " is sub-annotiation property of " + arg0.getSuperProperty().accept(this);
	}

	public String visit(OWLAnnotationPropertyDomainAxiom arg0) {
		return arg0.getProperty().accept(this) + " has domain " + arg0.getDomain().accept(this);
	}

	public String visit(OWLAnnotationPropertyRangeAxiom arg0) {
		String resultstring = "";
		OWLAnnotationProperty prop1 = arg0.getProperty();
		IRI prop2 = arg0.getRange();
		resultstring = resultstring + "range(" +  prop1.accept(this) +  "," 
		+ prop2.accept(this) + ")";
		return resultstring;
	}

	public String visit(OWLObjectHasValue arg0) {
		VerbalisationManager.INSTANCE.includesHasValue = true;
		return "hasValue" + "(" + arg0.getProperty().accept(this) +  "," + arg0.getValue() + ")"; 
	}

	public String visit(OWLObjectMinCardinality arg0) {
		String resultstring = "";
		resultstring = resultstring 
					+ ">=" + arg0.getCardinality() 
					+ "(" + arg0.getProperty().accept(this) + "." 
					+ arg0.getFiller().accept(this);
		resultstring = resultstring + ")";
		return resultstring;
	}

	public String visit(OWLObjectExactCardinality arg0) {
		String resultstring = "";
		resultstring = resultstring + "=" + arg0.getCardinality() + "(" + arg0.getProperty().accept(this) + "." 
		+ arg0.getFiller().accept(this);
		resultstring = resultstring + ")";
		return resultstring;
	}

	public String visit(OWLObjectMaxCardinality arg0) {
		String resultstring = "";
		resultstring = resultstring 
					+ "<=" + arg0.getCardinality() 
					+ "(" + arg0.getProperty().accept(this) + "." 
					+ arg0.getFiller().accept(this);
		resultstring = resultstring + ")";
		return null;
	}

	public String visit(OWLObjectHasSelf arg0) {
		// TODO Auto-generated method stub
				return null;
	}

	public String visit(OWLObjectOneOf arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String visit(OWLDataSomeValuesFrom arg0) {
		String propertyname = arg0.getProperty().accept(this);
		OWLDataRange r = arg0.getFiller();
		return "something that " + propertyname + " " + r.accept(this);
	}

	public String visit(OWLDataAllValuesFrom arg0) {
		return arg0.getProperty().accept(this) + " " + arg0.getFiller().accept(this);
	}

	public String visit(OWLDataHasValue arg0) {
		VerbalisationManager.INSTANCE.includesHasValue = true;
		return arg0.getProperty().accept(this) + _space +  arg0.getValue().getLiteral();
		// return "hasValue" + "(" + arg0.getProperty().accept(this) +  "," + arg0.getValue() + ")"; 
	}

	public String visit(OWLDataMinCardinality arg0) {
		String resultstring = "";
		resultstring = resultstring + "min#" + "(" + arg0.getCardinality() 
				+ ","+ arg0.getProperty().accept(this);
		resultstring = resultstring + "," +  arg0.getFiller().accept(this) +")";
		return null;
	}

	public String visit(OWLDataExactCardinality arg0) {
		return "=" + arg0.getCardinality() + "(" + arg0.getProperty().accept(this) + "." 
				+ arg0.getFiller().accept(this)+ ")";
	}

	public String visit(OWLDataMaxCardinality arg0) {
		return "<=" + arg0.getCardinality() + "(" + arg0.getProperty().accept(this) + "." 
				+ arg0.getFiller().accept(this)+ ")";
	}

	public String visit(OWLDatatype arg0) {
		// TODO Auto-generated method stub
		if (arg0.isBoolean()){
			if (arg0.toString().equals("xsd:boolean")){
				return "as indicated by a boolean";
			}
		}
		return "as indicated by a " + arg0.toString();
	}

	public String visit(OWLDataComplementOf arg0) {
		// TODO Auto-generated method stub
		return "NI-1002";
	}

	public String visit(OWLDataOneOf arg0) {
		String result = "of one of the following: ";
		// TODO Auto-generated method stub
		Set<OWLLiteral> literals = arg0.getValues();
		boolean firstP = true;
		for (OWLLiteral lit: literals){
			if (!firstP)
				result += ", ";
			result += lit.accept(this);
			firstP = false;
		}
		return result;
	}

	public String visit(OWLDataIntersectionOf arg0) {
		// TODO Auto-generated method stub
		return "NI-1003";
	}

	public String visit(OWLDataUnionOf arg0) {
		// TODO Auto-generated method stub
		return "NI-1004";
	}

	public String visit(OWLDatatypeRestriction arg0) {
		// TODO Auto-generated method stub
		return "NI-1005";
	}

	public String visit(OWLLiteral arg0) {
		return arg0.getLiteral();
	}

	public String visit(OWLFacetRestriction arg0) {
		// TODO Auto-generated method stub
		return "NI-1006";
	}

	public String visit(OWLObjectProperty arg0) {
		return arg0.getNamedProperty().toString();
	}

	public String visit(OWLObjectInverseOf arg0) {
		return "not " + arg0.getInverse().accept(this);
	}

	public String visit(OWLDataProperty arg0) {
		return VerbalisationManager.INSTANCE.getDataPropertyNL(arg0);
		// return VerbalisationManager.treatCamelCaseAndUnderscores(arg0.getIRI().getFragment().toString());
		// return arg0.getIRI().getFragment().toString();
	}

	public String visit(OWLNamedIndividual arg0) {
		return arg0.getIRI().getFragment();
	}

	public String visit(OWLAnnotationProperty arg0) {
		return arg0.getIRI().getFragment();
	}

	public String visit(OWLAnnotation arg0) {
		return arg0.getProperty().accept(this) + arg0.getValue().accept(this);
	}

	public String visit(IRI arg0) {
		return arg0.getFragment();
	}

	public String visit(OWLAnonymousIndividual arg0) {
		// TODO Auto-generated method stub
		return "NI-1008";
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
		return "ontology " + arg0.getOntologyID();
	}

       
	
}
