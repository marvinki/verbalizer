package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintClassExpressionVisitor;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintOWLAxiomVisitor;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintOWLObjectVisitor;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;

import com.clarkparsia.owlapi.explanation.BlackBoxExplanation;
import com.clarkparsia.owlapi.explanation.HSTExplanationGenerator;
import com.google.common.base.Optional;

import edu.smu.tspell.wordnet.SynsetType;

public enum VerbalisationManager {
	INSTANCE;
	
	static final VerbaliseOWLObjectVisitor verbOWLObjectVisit = new VerbaliseOWLObjectVisitor();
	static final TextElementOWLObjectVisitor textOWLObjectVisit = new TextElementOWLObjectVisitor();
	static final PrettyPrintClassExpressionVisitor ppCEvisit = new PrettyPrintClassExpressionVisitor();
	static final PrettyPrintOWLAxiomVisitor ppOAvisit = new PrettyPrintOWLAxiomVisitor();
	static final PrettyPrintOWLObjectVisitor ppOOvisit = new PrettyPrintOWLObjectVisitor();
	
	private OWLOntology ontology;
	private boolean ontologyLabelsIncludeDeterminers = false;
	public final static String _space = " "; // SETTING SPACER!
	
	public boolean featureRoleAgg = false;
	public boolean featureClassAgg = false;
	public boolean featureAttribute = false;
	
	public boolean featuresOFF =false;;
	
	public boolean includesHasValue =false;
	
	public static String verbalise(OWLObject ob){
		return ob.accept(verbOWLObjectVisit);
	}
	
	public static TextElementSequence textualise(OWLObject ob){
		TextElementSequence seq = new TextElementSequence( ob.accept(textOWLObjectVisit));
		return seq;
	}
	
	public static TextElementSequence textualise(OWLObject ob, Obfuscator obfuscator){
		textOWLObjectVisit.setObfuscator(obfuscator);
		verbOWLObjectVisit.setObfuscator(obfuscator);
		TextElementSequence seq = new TextElementSequence( ob.accept(textOWLObjectVisit));
		return seq;
	}
	
	/** Indicate to the VerbalisationManager the current ontology where to find information
	 * 
	 * @param ontology -- the current ontology
	 */
	public void setOntology (OWLOntology ontology){
		this.ontology = ontology;
		ontologyLabelsIncludeDeterminers = detectDeterminerLabels();
	}
	
	/** Retrieve the current ontology
	 * 
	 * @return the current ontology
	 */
	public OWLOntology getOntology (){
		return ontology;
	}
	
	/** Pretty print an OWL-API class expression (to DL syntax)
	 * 
	 * @param class expression
	 * @return pretty printed string for class expression
	 */
	public static String prettyPrint(OWLClassExpression ce){
			return ce.accept(ppCEvisit);	
	}
	
	/** Pretty print an OWL-API subclass relationship (to DL syntax)
	 * 
	 * @param subclass axiom
	 * @return pretty printed string for subclass axiom
	 */
	public static String prettyPrint(OWLSubClassOfAxiom oa){
		return ppOAvisit.visit(oa);
	}
	
	/** Pretty print an OWL-API Object (to DL syntax)
	 * 
	 * @param object
	 * @return pretty printed string for object
	 */
	public static String prettyPrint(OWLObject oo){
		return oo.accept(ppOOvisit);
	}	
	
	/** Pretty print an OWLFormula (to DL syntax)
	 * 
	 * @param formula
	 * @return pretty printed string for formula
	 */
	public static String prettyPrint(OWLFormula form){
		return prettyPrint(ConversionManager.toOWLAPI(form));
	}
	
	/** Obtain the input string with the first character in lowercase
	 * 
	 * @param s the original string
	 * @return the original string with the first character in lowercase
	 */
	public static String lowerCaseFirstLetter(String s){
	if (s.length()>0 && Character.isUpperCase(s.charAt(0)) && !VerbaliseOWLObjectVisitor.detectAcronym(s))
		return s.substring(0,1).toLowerCase() + s.substring(1,s.length());
		else
		return s;
	}
	
	public java.lang.String getPropertyNLString(OWLObjectPropertyExpression property){
		// System.out.println("DEBUG : " + property);
		OWLProperty namedproperty = property.getNamedProperty();
		// System.out.println("DEBUG - named : " + namedproperty);
		java.lang.String str="";
		if (namedproperty!=null){
			if (this.ontology==null){ // if no ontology is provided, simply return the classname
				// System.out.println("DEBUG + " + property.getNamedProperty().getIRI().getFragment());
				String result =  property.getNamedProperty().getIRI().getFragment();
				if (textOWLObjectVisit.getObfuscator()!=null){
					result = textOWLObjectVisit.getObfuscator().obfuscateRole(result);
				}
				return result;
			}
			// collect annotation axioms
			Set<OWLOntology> imported = ontology.getImports();
			Set<OWLAnnotationAssertionAxiom> annotationaxioms = new HashSet<OWLAnnotationAssertionAxiom>();
			for (OWLOntology ont : imported){
				// 
				annotationaxioms.addAll(EntitySearcher.getAnnotationAssertionAxioms(namedproperty, this.ontology));
				// annotationaxioms.addAll(namedproperty.getAnnotationAssertionAxioms(ont));
			}
			// 
			annotationaxioms.addAll(EntitySearcher.getAnnotationAssertionAxioms(namedproperty, this.ontology));
			// annotationaxioms.addAll(namedproperty.getAnnotationAssertionAxioms(ontology));
			if (annotationaxioms !=null){
				for (OWLAnnotationAssertionAxiom axiom : annotationaxioms){
					if (axiom.getAnnotation().getProperty().getIRI().getFragment().equals("label")){
						// System.out.println("DEBUG TO STRING " + axiom.getAnnotation().getValue());
						// SimpleRenderer renderer = new SimpleRenderer();			
						Optional<OWLLiteral> opt = axiom.getAnnotation().getValue().asLiteral();
						OWLLiteral literal = opt.orNull();
						str = literal.getLiteral();
						// str = axiom.getAnnotation().getValue().toString();
					}
				}
			} 
		}// Obfuscate
	
		// shortcut for labels with [X]
		if (str.indexOf("[X]")>-1){
			if (textOWLObjectVisit.getObfuscator()!=null){
				str = textOWLObjectVisit.getObfuscator().obfuscateRole(property.getNamedProperty().getIRI().getFragment());
			}
			return str;}
		// remove language tags
		if (str.indexOf("@en")>0){
			str = str.substring(1,str.length()-4);
		}
		// In case there are no annotations
		// -- detect if camel case
		if (str==""){
			str = namedproperty.getIRI().getFragment();
			// System.out.println("DEBUG " + str);
		}
		// Obfuscate!
				if (textOWLObjectVisit.getObfuscator()!=null){
					str = textOWLObjectVisit.getObfuscator().obfuscateRole(str);
				}
		if (VerbaliseOWLObjectVisitor.detectLowCamelCase(str))
			str = VerbaliseOWLObjectVisitor.removeCamelCase(str);
		// heuristic!
		if(str.indexOf("_of")>=0)
			str =  VerbaliseOWLObjectVisitor.removeUnderscores(str);
		if(str.indexOf("_")>=0)
			str =  VerbaliseOWLObjectVisitor.removeUnderscores(str);
		if(str.indexOf("^^xsd:string")>=0)
			str = str.substring(1,str.length()-13);
		// detect if name is of the form "_ of" 
		if (detect_noun_of(str) && !str.contains("is"))
			str = "is" + _space + str;
		return str;
	}
	
	public static String verbaliseProperty(OWLObjectPropertyExpression property, List<String> fillerstrs, String middle, Obfuscator obfuscator){
		String result = "";
		
		//
		if (fillerstrs.size()>1){
			// hey, we certainly got a feature!
			// System.out.println("feature debug " + fillerstrs);
			VerbalisationManager.INSTANCE.featureRoleAgg = true;
		}
		
		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(property);
		
		// Obfuscate, if needed
		if (obfuscator !=null){
			propstring = obfuscator.obfuscateRole(propstring);
		}
		
		// check case where string contains a pattern.
		if(propstring.indexOf("[X]")>=0){
			String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(property);
			String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
			result += part1;
			if (part2.equals("") && part1.equals("") || part1==null && part2==null){
				result +=  "has as" + _space + property.getNamedProperty().getIRI().getFragment() + "-successor ";;
			}
		} else{
			result += propstring + " ";
		}
		result += middle;
		boolean needsep = false;
		for (java.lang.String str : fillerstrs){
			if (needsep){
				result += _space + "and" + _space;
			}
			result += str;
			needsep = true;
		}
		if (VerbalisationManager.INSTANCE.getPropertyNLString(property).indexOf("[X]")>=0)
			result += VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
		return result;
	}
	
	public static List<TextElement> textualiseProperty(OWLObjectPropertyExpression property, List<List<TextElement>> fillerelements, List<TextElement> middle){
		// String result = "";
		List<TextElement> result = new ArrayList<TextElement>();
		//
		if (fillerelements.size()>1){
			// hey, we certainly got a feature!
			VerbalisationManager.INSTANCE.featureRoleAgg = true;
		}
		
		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(property);
		// check case where string contains a pattern.
		if(propstring.indexOf("[X]")>=0){
			String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(property);
			String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
			result.add(new RoleElement(part1)); // += part1;
			if (part2.equals("") && part1.equals("") || part1==null && part2==null){
				result.add(new RoleElement("has as" + _space + property.getNamedProperty().getIRI().getFragment() + "-successor "));
				// result +=  "has as" + _space + property.getNamedProperty().getIRI().getFragment() + "-successor ";;
			}
		} else{
			result.add(new RoleElement(propstring));
			// result += propstring + " ";
		}
		
		result.addAll(middle);
		boolean needsep = false;
		for (List<TextElement> str : fillerelements){
			if (needsep){
				result.add(new LogicElement("and"));
				// result += _space + "and" + _space;
			}
			result.addAll(str);
			needsep = true;
		}
		if (VerbalisationManager.INSTANCE.getPropertyNLString(property).indexOf("[X]")>=0)
			result.add(new RoleElement(VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property)));
			// result += VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
		return result;
	}
	
	
	public static String aOrAnIfy(String str){
		// System.out.println("a or an " + str);
		// first check if there are any determiners present; if so, leave unchanged
		if (     (str.length()>1 && str.substring(0,2).equals("a" + _space))
			 ||  (str.length()>2 && str.substring(0,3).equals("an" + _space))
			 ||  (str.length()>3 && str.substring(0,4).equals("the" + _space)))
			return str;
		if (str.length()<1) return str; // discard empty string
		// check if we are dealing with something that is not a noun, in this case leave unchanged
		// check known reasons to believe something is a noun
		boolean isNoun = false;
		if (str.contains("oid"))
			isNoun = true;
		if (str.contains("publisher"))
			isNoun = true;
		if (!WordNetQuery.INSTANCE.isDisabled() && !(WordNetQuery.INSTANCE.isType(str,SynsetType.NOUN)>0) &&!isNoun){
			// System.out.println("NOT A NOUN");
			str = lowerCaseFirstLetter(str);
			// System.out.println("not using article");
			return str;
		}
		// Check if gerund, if so, leave unchanged!
		if (str.indexOf("ing")>0 && !str.equals("Ring")  && !str.equals("ring") && !str.contains(" ring") && !str.contains("Ring"))
			return str.toLowerCase();
		if (str.substring(0,1).equals("a") 
		    || str.substring(0,1).equals("u") 
			|| str.substring(0,1).equals("e")
			|| str.substring(0,1).equals("i")
			|| str.substring(0,1).equals("o")
			|| str.substring(0,1).equals("A") 
		    || str.substring(0,1).equals("U") 
			|| str.substring(0,1).equals("E")
			|| str.substring(0,1).equals("I")
			|| str.substring(0,1).equals("O")
				)
			str = "an" + _space + str;
		else{
			// System.out.println("NOUN");
			str = "a" + _space + str;
		}
		return str.toLowerCase();
	}
		
	
	public static String treatCamelCaseAndUnderscores(String str){
		if (VerbaliseOWLObjectVisitor.detectUnderCamel(str))
				return VerbaliseOWLObjectVisitor.removeUnderCamel(str);
		if (VerbaliseOWLObjectVisitor.detectLowCamelCase(str) || VerbaliseOWLObjectVisitor.detectCamelCase(str)){
			return VerbaliseOWLObjectVisitor.removeCamelCase(str);
		}
		return VerbaliseOWLObjectVisitor.removeUnderscores(str);
	}
	
	
	public java.lang.String getClassNLString(OWLClass classname){
		// System.out.println("get class NL String" + this.ontology);
		// System.out.println("get class NL String " + classname);
		// catch some special cases straight away!
		if (classname.isOWLThing())
			return "something";
		if (classname.isOWLNothing())
			return "non-existant";
		java.lang.String str="";
		java.lang.String str2="";
		boolean hasLabel = false;
		if (this.ontology!=null) {
			Set<OWLOntology> imported = ontology.getImports();
			Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>();
			annotations.addAll(EntitySearcher.getAnnotationObjects(classname, this.ontology));
			// classname.getAnnotations(this.ontology);
			for (OWLOntology ont : imported){
				//
				  annotations.addAll(EntitySearcher.getAnnotationObjects(classname, ont));
				// annotations.addAll(classname.getAnnotations(ont));
			}
		for (OWLAnnotation annotation : annotations){
			if (annotation.getProperty().getIRI().getFragment().equals("label")){
				str = annotation.getValue().asLiteral().orNull().getLiteral();// annotation.getValue().toString();
				// System.out.println("dbg " +annotation.getValue().asLiteral().orNull().getLiteral());
			}
			if (annotation.getProperty().getIRI().getFragment().equals("label2")){
				str2 = annotation.getValue().asLiteral().orNull().getLiteral();
						// annotation.getValue().toString();
			}
		}
		// remove unnecessary stuff
					if (str.indexOf("@e")>0)
						str = str.substring(0,str.length()-3);
		}
		if (!str.equals("") || !str2.equals(""))
			hasLabel = true;
		// search for the type tag of the annotation (e.g. ^^xsd:string)
		if (str!=""){
			int i = str.indexOf("\"");
		if (str.contains("^")){
			int j = str.indexOf("^");
			str= str.substring(i+1,j-1);
		} else{
			// str= str.substring(i+1,str.length()-1);
		}
			System.out.println("returning (1) " + str);
			return str;
		}
		if (str==""){
			str = ppCEvisit.visit(classname);
			// System.out.println("DBG " + str);
			if (verbOWLObjectVisit.getObfuscator()!=null){
				str = verbOWLObjectVisit.getObfuscator().obfuscateName(str);
			}
			// System.out.println("DEBUG after prettyprint " + str);
			// check if camelcasing was used
			str = treatCamelCaseAndUnderscores(str);
			boolean isUncountable = false;
			// heuristically check cases where noun might be uncountable
			if (str.contains("ium"))
				isUncountable = true;
			if (str.contains("ies"))
				isUncountable = true;
			if (str.contains("red") || str.contains("Red") || str.contains("green") || str.contains("Green"))
				isUncountable = true;
			// put indeterminate determiner
			if (!isUncountable)
				str = aOrAnIfy(str);
			return str;
		}
		if (!str2.equals("")){
			str=str2;
		}
		if (verbOWLObjectVisit.getObfuscator()!=null){
			str = verbOWLObjectVisit.getObfuscator().obfuscateName(str);
		}
		// System.out.println("ontologyLabelsIncludeDeterminers " + ontologyLabelsIncludeDeterminers);
		if (!hasLabel || ontologyLabelsIncludeDeterminers==false)
			str = aOrAnIfy(str);
		return str;
	};
	
	public static String removeStringSuffix(String str){
		int i = str.indexOf("\"");
		int j = str.indexOf("^");
		if (i>= 0 && j>= 0)
			return str.substring(i+1,j-1);
		else return str;
	}
	
	public String getSimpleIntersectionNLString(OWLObjectIntersectionOf ints){
			List<OWLClassExpression> exprs = ints.getOperandsAsList();
			return getSimpleIntersectionNLString(exprs);
	}
	
	public String getSimpleIntersectionNLString(List<OWLClassExpression> exprs){
		String result = "";
		List<OWLObject> noun_concepts = new ArrayList<OWLObject>();
		List<OWLObject> attribute_concepts = new ArrayList<OWLObject>();
		List<OWLObject> noun_or_attribute_concepts = new ArrayList<OWLObject>();
		List<String> noun_concepts_strings = new ArrayList<String>();
		List<String> noun_or_attribute_concepts_strings = new ArrayList<String>();
		List<String> attribute_concepts_strings = new ArrayList<String>();
		// loop for all elements, sort them into the right buckets
		for (OWLClassExpression classexp : exprs){
			/* first strategy: try annotations */
			Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>();
			if (this.ontology!=null)
			{
				annotations.addAll(EntitySearcher.getAnnotationObjects((OWLClass) classexp, this.ontology));
			}
				// annotations = ((OWLClass) classexp).getAnnotations(this.ontology);
			// need to find the "right" annotation
			OWLAnnotation labelAnnot = null;
			OWLAnnotation defAnnot = null;
			OWLAnnotation attrAnnot = null;
			String labelAnnotString = null;
			String defAnnotString = null;
			String attrAnnotString = null;
			String genericClassname = classexp.accept(verbOWLObjectVisit);
			if (genericClassname.length()>2 && 
				genericClassname.substring(0, 2).equals("a" + _space)){
				genericClassname = genericClassname.substring(2,genericClassname.length());
			}
			if (genericClassname.length()>3 && 
					genericClassname.substring(0, 3).equals("an" + _space)){
					genericClassname = genericClassname.substring(3,genericClassname.length());
				}
			boolean isWordnetNoun = false;
			boolean isWordnetAttribute = false;
			boolean isWordnetAdjectiveSattelite = false;
			for (OWLAnnotation annotation : annotations){
			if (annotation.getProperty().getIRI().getFragment().equals("attributive")){
				attrAnnot = annotation;
				attrAnnotString = removeStringSuffix(annotation.getValue().toString());
			}
			if (annotation.getProperty().getIRI().getFragment().equals("definitory")){
				defAnnot = annotation;
				defAnnotString = removeStringSuffix(annotation.getValue().toString());
			}
			if (annotation.getProperty().getIRI().getFragment().equals("label")){
				labelAnnot = annotation;
				labelAnnotString = removeStringSuffix(annotation.getValue().toString());
			}
			} // end loop for collecting annotations
			int []types = null;
			if (!WordNetQuery.INSTANCE.isDisabled() && ( attrAnnotString==null || attrAnnotString.equals(""))  
					&& (attrAnnotString == null || attrAnnotString.equals(""))){
				types = WordNetQuery.INSTANCE.getTypes(genericClassname);
			}
			/* fill buckets */
			// Attributes and Nouns
			if (attrAnnotString!=null && !attrAnnotString.equals("") 
					&& defAnnotString!=null & !defAnnotString.equals("")){
				noun_or_attribute_concepts.add(classexp);
				noun_or_attribute_concepts_strings.add(labelAnnotString);
				continue;
			}
			if // (isWordnetNoun && isWordnetAttribute){
			    (types!=null && types[0]>0 && (types[2]>0 || types[4]>0) ){ // adjective code is 3 (index 2), adjective satellite is code is 5
				noun_or_attribute_concepts.add(classexp);
				noun_or_attribute_concepts_strings.add(genericClassname);
				continue;
			}
			// Attributes only
			if (attrAnnotString !=null && !attrAnnotString.equals("")){
				attribute_concepts.add(classexp);
				attribute_concepts_strings.add(attrAnnotString);
				continue;
			}
			// if (isWordnetAttribute){
			if (types!=null && types[0]==0 && (types[2]>0 || types[4]>0) ){
				attribute_concepts.add(classexp);
				attribute_concepts_strings.add(genericClassname);
				continue;
			}
			// Nouns only
			if (defAnnotString!= null && !defAnnotString.equals("") && !defAnnotString.contains("false")){
				noun_concepts.add(classexp);
				noun_concepts_strings.add(attrAnnotString);
				continue;
			}
			//if (isWordnetNoun){
			if (types!=null && types[0]>0){
				noun_concepts.add(classexp);
				noun_concepts_strings.add(genericClassname);
				continue;
			}
			// make the default assumption that if we get here, and there is no category found, it must be a noun
			noun_concepts.add(classexp);
			noun_concepts_strings.add(genericClassname);
		} // end loop for collecting expressions
		// feature notification
		if (noun_or_attribute_concepts_strings.size()>0 || attribute_concepts_strings.size()>0
					){
			// System.out.println("setting" + exprs.toString());
			// System.out.println("setting" + exprs.get(1));
			featureAttribute = true;
		}
		/* heuristic re-arrange*/
		// if no proper noun, look for gerunds
		if (noun_concepts_strings.size()==0 
				&& noun_or_attribute_concepts_strings.size()==0){
			for (OWLClassExpression classexp : exprs){
				String genericClassname = classexp.accept(verbOWLObjectVisit);
				if (genericClassname.indexOf("ing")>0){
					noun_concepts.add(classexp);
					noun_concepts_strings.add(genericClassname.toLowerCase());
				}
			}
		}
		// debug
		// System.out.println("attribute concepts  " + attribute_concepts_strings);
		// System.out.println("noun or attribute concept string " + noun_or_attribute_concepts_strings);
		// System.out.println("noun concept string " + noun_concepts_strings);
		/* feature? */
		if ((noun_concepts_strings.size()>1 || 
				noun_concepts_strings.size() 
				+ noun_or_attribute_concepts_strings.size()
				+ attribute_concepts_strings.size() >1) && exprs.size()>1
				// exclusion
				&& !(exprs.size()==2 && (exprs.get(0).isOWLNothing()|| exprs.get(1).isOWLNothing()))
				){
			System.out.println("setting " + exprs.toString());
		    System.out.println("setting " + exprs.get(1));
		    System.out.println("setting " + exprs.get(1).isOWLNothing());
		    System.out.println("setting " + !(exprs.size()==2 && (exprs.get(0).isOWLNothing()|| exprs.get(1).isOWLNothing())));
			VerbalisationManager.INSTANCE.featureClassAgg=true;
		}
		/* now arrange */
		boolean firstToken = true;
		// first check if we have more than one noun
		if (noun_concepts_strings.size()>1){
			// first case: more than one noun
			// start with nouns, then use "that is"
			for (String str: noun_concepts_strings){
				String conjoiner = _space + "and" + _space;
				if (firstToken==true){
					conjoiner = _space + "that is" + _space;
					firstToken=false;
					str = aOrAnIfy(str);
					// System.out.println("A OR ANIFIED " + str);
				}
				result = result + str + _space + "that is" + _space;
				noun_or_attribute_concepts_strings.remove(str);
			}
			if (noun_concepts_strings.size()>2)
				result = result.substring(0,result.length()-4); // remove the last "and"
				else
				result = result.substring(0,result.length()-8); // remove the last "that is"
			if (noun_or_attribute_concepts_strings.size()>0 || attribute_concepts_strings.size()>0) 
			result = result + "that is" + _space;
			for (String str: noun_or_attribute_concepts_strings){
				result = result + str + _space;
				attribute_concepts_strings.remove(str);
			}
			for (String str: attribute_concepts_strings){
				result = result + str + _space;
			}
		} else{ 
	    // second case: at most one pure noun
		// attributes first
		for (String str: attribute_concepts_strings){
			result = result + str + _space;
			noun_or_attribute_concepts_strings.remove(str);
		}
		// if several items can be used either way, order them
		float attributiveness = 0;
		while (noun_or_attribute_concepts_strings.size()>0){
			attributiveness = 0;
			String current = "";
			for (String str: noun_or_attribute_concepts_strings){
				if (!WordNetQuery.INSTANCE.isDisabled()){
				// result = result + str + " ";
				// noun_concepts_strings.remove(str);
				int[] types = WordNetQuery.INSTANCE.getTypes(str);
				// System.out.println(Arrays.toString(types));
				// float currentattr = ((float) types[2]+types[4])/ ((float) types[0]+types[2]+types[4]);
				float currentattr = types[2]+types[4];
				// do not consider type 4
				if (currentattr >= attributiveness){
					attributiveness = currentattr;
					current = str;
				}
				}
			}
			noun_or_attribute_concepts_strings.remove(current);
			result = result + current + _space; // + " ";
			noun_concepts_strings.remove(current);
		}
		for (String str: noun_concepts_strings){
			result = result + str + _space;
		}
		}
		
		//
		if (VerbalisationManager.INSTANCE.featureClassAgg){
			// System.out.println(exprs);
			System.out.println("FEATURE CLASS AGG: " + result.toString());
			System.out.println(exprs.toString());
		}
		if (VerbalisationManager.INSTANCE.featureAttribute){
			System.out.println("FEATURE ATTRIBUTE: " + result.toString());
		}
		
		if (result.length()>0)
			result = result.substring(0, result.length()-1); // need to remove last spacer
		return aOrAnIfy(result);
	}
	
	public java.lang.String getPropertyNLStringPart1 (OWLObjectPropertyExpression property){
		java.lang.String str = getPropertyNLString(property);
		if (str.length()<4){
			return "";
		}
		int i = str.indexOf("\"");
		int j = str.indexOf("[X]");
		return str.substring(i+1,j);
	}
	
	public java.lang.String getPropertyNLStringPart2 (OWLObjectPropertyExpression property){
		java.lang.String str = getPropertyNLString(property);
		if (str.length()<4){
			return "";
		}
		int i = str.indexOf("[X]");
		// search for the type tag of the annotation (e.g. ^^xsd:string)
		if (str.contains("^")){
			int j = str.indexOf("^");
			return str.substring(i+3,j-1);
		} else{
			if (str.substring(i+3).contains("\"")){
				return str.substring(i+3,str.substring(i+3).indexOf("\"")+i+3);
			} else
			return str.substring(i+3);
		}
	}
	
	public static String commonPrefix(String str1, String str2){
		int minlength = str1.length();
		if (minlength> str2.length()){ minlength = str2.length();}
		for (int i=0; i<=minlength;i++){
			if (!str1.substring(0,i).equals(str2.substring(0,i))){
				return str1.substring(0,i-1);
			}
		}
		return str1;
	}
	
	public static String commonSuffix(String str1, String str2){
		String str1rev = new StringBuilder(str1).reverse().toString();
		String str2rev = new StringBuilder(str2).reverse().toString();
		String str3rev = commonPrefix(str1rev,str2rev);
		String str3 = new StringBuilder(str3rev).reverse().toString();
		return str3;
	}
	
	public static String strip(String str, String prefix,String suffix){
		String newstr = new String(str);
		newstr = newstr.replace(prefix, "");
		newstr = newstr.replace(suffix, "");
		return newstr;
	}
	
	public static String aggregateRepeated(String str1, String str2, String connector){
		String prefix = commonPrefix(str1,str2);
		String suffix = commonSuffix(str1,str2);
		if (suffix.length()>0){
			String strip1 = strip(str1,prefix,suffix);
			String strip2 = strip(str2,prefix,suffix);
			return prefix + strip1 + connector + strip2 + suffix;
		} else 
		{
			return str1 + connector + str2;
		}
	}
	
	
	/**
	 * 
	 * @param exprs
	 * @return a list of groups of existential expressions with the same property expressions
	 */
	public static List<List<OWLClassExpression>> groupMultipleExistsPatterns(List<OWLClassExpression> exprs){
		List<List<OWLClassExpression>> buckets = new ArrayList<List<OWLClassExpression>>();
		for(OWLClassExpression ex : exprs){
			if (ex instanceof OWLObjectSomeValuesFrom){
				OWLObjectPropertyExpression propexpr = ((OWLObjectSomeValuesFrom) ex).getProperty();
				// check if there is already a bucket
				boolean found = false;
				for(List<OWLClassExpression> bucket : buckets){
					if (!VerbalisationManager.INSTANCE.featuresOFF
						&&	bucket.size()>0 
					    && bucket.get(0) instanceof OWLObjectSomeValuesFrom
					    && ((OWLObjectSomeValuesFrom) bucket.get(0)).getProperty().equals(propexpr)){
						bucket.add(ex);
						found = true;
						VerbalisationManager.INSTANCE.featureRoleAgg=true;
						break;
					}
				} 
				if (found==false){
					List<OWLClassExpression> newlist = new ArrayList<OWLClassExpression>();
					newlist.add(ex);
					buckets.add(newlist);
				}
			}
		}
		return buckets;
	}
	
	public String verbaliseComplexIntersection(OWLObjectIntersectionOf inter,Obfuscator obfuscator){
		// System.out.println("complex intersection called with " + inter);
		String result = "";
		List<OWLClassExpression> simpleExpressions = new ArrayList<OWLClassExpression>();
		List<OWLClassExpression> existsExpressions = new ArrayList<OWLClassExpression>();
		List<OWLClassExpression> otherExpressions = new ArrayList<OWLClassExpression>();
		// seperate repeated exist subexpressions from the rest
		for (OWLClassExpression ex : inter.getOperandsAsList()){
			if (ex instanceof OWLObjectSomeValuesFrom){
				existsExpressions.add(ex);
			} else 
			if (ex instanceof OWLClass){
				simpleExpressions.add(ex);
			}
			else{
				otherExpressions.add(ex);
			}
		}
		// if (otherExpressions.size()>0)
		//	System.out.println("verbaliseComplexIntersection --- CAN'T HANDLE THIS! " + otherExpressions);
		// Verbalise the "easy" part
		result = result + getSimpleIntersectionNLString(simpleExpressions) + _space;
		// Verbalise the existential part
		List<List<OWLClassExpression>> buckets = groupMultipleExistsPatterns(existsExpressions);
		for (int i=0; i< buckets.size();i++){
			OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) (buckets.get(i).get(0));
			OWLObjectPropertyExpression propexpr = someexpr.getProperty();
			List<String> substrings = new ArrayList<String>();
			for (Object someobj : buckets.get(i)){
				OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) someobj;
				String somefillertext = some.getFiller().accept(verbOWLObjectVisit);
				if (some.getFiller() instanceof OWLObjectSomeValuesFrom){
					somefillertext = "something that " + somefillertext; 
				}
				substrings.add(somefillertext);
			}
			result = result + "that" + _space  + verbaliseProperty(propexpr, substrings,"", obfuscator);
			if (i!=buckets.size()-1){
				result = result + _space + "and" + _space;
			}
		}
		return result;	
	}
	
	
	public static boolean checkMultipleExistsPattern(OWLObjectIntersectionOf ints){
		if (VerbalisationManager.INSTANCE.featuresOFF==true){
			// System.out.println("refused to detect multiple exists pattern");
			return false;
		}
		List<OWLClassExpression> exprs = ints.getOperandsAsList();
		OWLObjectPropertyExpression commonpropexpr = null;
		for (OWLClassExpression expr: exprs){
			if (!(expr instanceof OWLObjectSomeValuesFrom)){
				return false;
			} else{
				OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
				OWLObjectPropertyExpression propexpr = someexpr.getProperty();
				if (commonpropexpr==null){
					commonpropexpr = propexpr;
				}
				if (!commonpropexpr.equals(propexpr)){
					return false;
				}
			}
		}
		return true;
	}
	
	public static java.lang.String pseudoNLStringMultipleExistsPattern (OWLObjectIntersectionOf ints, Obfuscator obfuscator){
		java.lang.String result = "";
		List<OWLClassExpression> exprs = ints.getOperandsAsList();
		List<java.lang.String> substrings = new ArrayList<java.lang.String>();
		OWLObjectPropertyExpression commonpropexpr = null;
		for (OWLClassExpression expr: exprs){
				OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
				OWLObjectPropertyExpression propexpr = someexpr.getProperty();
				substrings.add(someexpr.getFiller().accept(verbOWLObjectVisit));
				if (commonpropexpr==null){
					commonpropexpr = propexpr;
				}
		}
		// result = "something that " + verbaliseProperty(commonpropexpr, substrings,"");
		 result = verbaliseProperty(commonpropexpr, substrings,"something that" + _space, obfuscator);
		return result;
	}
	
	public static List<TextElement> textualiseMultipleExistsPattern (OWLObjectIntersectionOf ints){
		 List<TextElement> result;
		List<OWLClassExpression> exprs = ints.getOperandsAsList();
		List<List<TextElement>> substrings = new ArrayList<List<TextElement>>();
		OWLObjectPropertyExpression commonpropexpr = null;
		for (OWLClassExpression expr: exprs){
				OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
				OWLObjectPropertyExpression propexpr = someexpr.getProperty();
				substrings.add(someexpr.getFiller().accept(textOWLObjectVisit));
				if (commonpropexpr==null){
					commonpropexpr = propexpr;
				}
		}
		// result = "something that " + verbaliseProperty(commonpropexpr, substrings,"");
		List<TextElement> st = new ArrayList<TextElement>();
		st.add(new LogicElement("something that"));
		result = textualiseProperty(commonpropexpr, substrings,st);
		return result;
	}
	
	public static java.lang.String pseudoNLStringMultipleExistsAndForallPattern (OWLObjectIntersectionOf ints){
		// System.out.println("ints " + ints);
		java.lang.String result = "";
		List<OWLClassExpression> exprs = VerbaliseOWLObjectVisitor.collectAndExpressions(ints);
		List<java.lang.String> substrings = new ArrayList<java.lang.String>();
		OWLObjectPropertyExpression commonpropexpr = null;
		// recursive call for subexpressions
		for (OWLClassExpression expr: exprs){
				if (expr instanceof  OWLObjectSomeValuesFrom){
					OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
					OWLObjectPropertyExpression propexpr = someexpr.getProperty();
					String str = someexpr.getFiller().accept(verbOWLObjectVisit);
					// System.out.println("str " + str);
					if (someexpr.getFiller() instanceof OWLObjectSomeValuesFrom){
						String somethingstr = "something that ";
						OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) someexpr.getFiller();
						OWLClass cl = (OWLClass) VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
						VerbaliseOWLObjectVisitor visitor = new VerbaliseOWLObjectVisitor();
						if (cl!=null){somethingstr = cl.accept(visitor) + " that ";}
						str = somethingstr + str;
					}
					substrings.add(str);
					if (commonpropexpr==null){
						commonpropexpr = propexpr;
					}
				}
				
		}
		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(commonpropexpr);
		String middlepart = "";
		boolean needsep = false;
	    boolean innersep = false;
	    if (exprs.indexOf(exprs)<exprs.size()-1)
			innersep = true;
		for (java.lang.String str : substrings){
			if (needsep && !innersep){
				middlepart += "and" + _space;
			}
			if (needsep && innersep){
				middlepart += "," + _space;
			}
			middlepart+= str;
			needsep = true;
		}
		if(propstring.indexOf("[X]")<1){
			result += propstring + _space;
			result +=middlepart;
		}
		else {
		java.lang.String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(commonpropexpr);
		result += part1;
		result +=middlepart;
		// if pattern was used, need to end the expression
		java.lang.String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(commonpropexpr);
		result += part2;
		}
		return result + ", but nothing else";
	}
	
	
	public static List<TextElement> textualiseMultipleExistsAndForallPattern (OWLObjectIntersectionOf ints){
		// System.out.println("ints " + ints);
		List<TextElement> result = new ArrayList<TextElement>();
		List<OWLClassExpression> exprs = VerbaliseOWLObjectVisitor.collectAndExpressions(ints);
		List<List<TextElement>> substrings = new ArrayList<List<TextElement>>();
		OWLObjectPropertyExpression commonpropexpr = null;
		// recursive call for subexpressions
		for (OWLClassExpression expr: exprs){
				if (expr instanceof  OWLObjectSomeValuesFrom){
					OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
					OWLObjectPropertyExpression propexpr = someexpr.getProperty();
					List<TextElement> str = someexpr.getFiller().accept(textOWLObjectVisit);
					// System.out.println("str " + str);
					if (someexpr.getFiller() instanceof OWLObjectSomeValuesFrom){
						LogicElement somethingst = new LogicElement("something that");
						List<TextElement> somethingstr = new ArrayList<TextElement>();
						OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) someexpr.getFiller();
						OWLClass cl = (OWLClass) VerbalisationManager.INSTANCE.getDomain(some1.getProperty().getNamedProperty());
						if (cl!=null){
							somethingstr.addAll(cl.accept(textOWLObjectVisit));
							somethingstr.add(new LogicElement("that"));
							}
						str.addAll(0,somethingstr);
					}
					substrings.add(str);
					if (commonpropexpr==null){
						commonpropexpr = propexpr;
					}
				}
				
		}
		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(commonpropexpr);
		List<TextElement> middlepart = new ArrayList<TextElement>();
		boolean needsep = false;
	    boolean innersep = false;
	    if (exprs.indexOf(exprs)<exprs.size()-1)
			innersep = true;
		for (List<TextElement> str : substrings){
			if (needsep && !innersep){
				middlepart.add(new LogicElement("and"));
			}
			if (needsep && innersep){
				middlepart.add(new LogicElement(","));
			}
			middlepart.addAll(str);
			needsep = true;
		}
		if(propstring.indexOf("[X]")<1){
			result.add(new RoleElement(propstring));
			result.addAll(middlepart);
		}
		else {
		java.lang.String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(commonpropexpr);
		result.add(new RoleElement(part1));
		result.addAll(middlepart);
		// if pattern was used, need to end the expression
		java.lang.String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(commonpropexpr);
		result.add(new RoleElement(part2));
		}
		result.add(new LogicElement("but nothing else"));
		return result;
	}
	
	
	public OWLClassExpression getDomain(OWLObjectProperty prop){
		if (ontology==null){
			return null;
		}
		Set<OWLObjectPropertyAxiom> axioms = ontology.getAxioms(prop);
		OWLClassExpression cl = null;
		// first loop: find potential subobjectpropertyof axioms
		Set<OWLObjectProperty> props = new HashSet<OWLObjectProperty>();
		props.add(prop);
		for (OWLObjectPropertyAxiom axiom : axioms){
			if (axiom instanceof OWLSubObjectPropertyOfAxiom){
				OWLSubObjectPropertyOfAxiom ax = (OWLSubObjectPropertyOfAxiom) axiom;
				props.add(ax.getSuperProperty().getNamedProperty());
			}
		}
		// second loop: now find also all other axioms
		for (OWLObjectProperty prop1 : props){
			axioms = ontology.getAxioms(prop1);
		}
		// now actually hunt for the class expression
		for (OWLObjectPropertyAxiom axiom : axioms){
			if (axiom instanceof OWLObjectPropertyDomainAxiom){
				OWLObjectPropertyDomainAxiom ax = (OWLObjectPropertyDomainAxiom) axiom;
				cl = ax.getDomain();
			}
		}
		if (cl instanceof OWLObjectUnionOf){
			cl = ((OWLObjectUnionOf) cl).getOperandsAsList().get(0);
		}
		if (cl instanceof OWLObjectIntersectionOf){
			cl = ((OWLObjectIntersectionOf) cl).getOperandsAsList().get(0);
		}
		return cl;
	}
	
	
	private boolean detectDeterminerLabels(){
		Set<OWLAxiom> axioms = ontology.getAxioms();
		for (OWLAxiom ax : axioms){
			if (ax instanceof OWLAnnotationAssertionAxiom){
				OWLAnnotationAssertionAxiom annotax = (OWLAnnotationAssertionAxiom) ax;
				OWLAnnotation annot = annotax.getAnnotation();
				if (annot.getProperty().getIRI().getFragment().toString().equals("label")){
						// System.out.println("assessing label  " + annot.getValue().toString());
						if (annot.getValue().toString().length()>3 && annot.getValue().toString().substring(0,4).equals("the ")
								|| annot.getValue().toString().length()>2 && annot.getValue().toString().substring(0,3).equals("an ")
								|| annot.getValue().toString().length()>1 && annot.getValue().toString().substring(0,2).equals("a ")
								){
							return true;
						}
						;
				}
				}
		}
		return false;
	}
	
	/* Reformulation */
	
	public boolean detect_noun_of(String str){
		int i = str.indexOf(" of");
		if (i>0){
			String prefix = str.substring(0,i);
			if (!WordNetQuery.INSTANCE.isDisabled() && WordNetQuery.INSTANCE.isType(prefix,SynsetType.NOUN)>0)
				return true;
		}
		return false;
	}
	
	public static String verbalizeAxiom(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory, OWLOntology ontology, boolean asHTML, boolean obf){
		return  verbalizeAxiom(axiom, reasoner, factory, ontology,5000,20000,"EL", asHTML, obf);
	}
	
	public static GentzenTree computeGentzenTree(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory, OWLOntology ontology, int maxsteps, long maxtime, String ruleset){
		VerbalisationManager.INSTANCE.setOntology(ontology);
		 BlackBoxExplanation bBexplanator=new BlackBoxExplanation(ontology, factory,reasoner);
		 HSTExplanationGenerator explanationGenerator=new HSTExplanationGenerator(bBexplanator);
		 OWLDataFactory dataFactory= OWLAPIManagerManager.INSTANCE.getDataFactory();
		 
		 Set<OWLAxiom> explanation = new HashSet<OWLAxiom>();	  
		 // System.out.println("checking axiom " + axiom);
	 if (axiom instanceof OWLSubClassOfAxiom){
		   explanation = explanationGenerator.getExplanation(dataFactory.getOWLObjectIntersectionOf(((OWLSubClassOfAxiom) axiom).getSubClass(), ( (OWLSubClassOfAxiom) axiom).getSuperClass().getObjectComplementOf()));
	 }
	 if (axiom instanceof OWLObjectPropertyDomainAxiom){
		 OWLObjectPropertyDomainAxiom propDomainAx = (OWLObjectPropertyDomainAxiom) axiom;
		   explanation = 
				   explanationGenerator.getExplanation(
						   dataFactory.getOWLObjectIntersectionOf(
								   dataFactory.getOWLObjectSomeValuesFrom(propDomainAx.getProperty(), dataFactory.getOWLThing()),
								   propDomainAx.getDomain().getObjectComplementOf()));
	 }
	 if (explanation.size()==0){
		 return null;
	 }
	 
	
	 
	// convert to internal format
	 OWLFormula axiomFormula;
	 List<OWLFormula> justificationFormulas = new ArrayList<OWLFormula>();
	 try {
		 axiomFormula = ConversionManager.fromOWLAPI(axiom);
		 
		 for (OWLAxiom ax: explanation){
			 justificationFormulas.add(ConversionManager.fromOWLAPI(ax));
		 }
			} catch (Exception e) {
				return null;
			}
				
	 	GentzenTree tree;
		
		try {  // Settings!
			tree = InferenceApplicationService.computeProofTree(axiomFormula, justificationFormulas, maxsteps, maxtime, ruleset);
					// 1000, 20000, "EL");
	} catch(Exception e){
		e.printStackTrace();
		return null;}

	return tree;
	}
	
	public static String verbalizeAxiom(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory, OWLOntology ontology, int maxsteps, long maxtime, String ruleset, boolean asHTML, boolean obf){
		
		 VerbalisationManager.INSTANCE.setOntology(ontology);
		 if (ontology==null){
			 return "failure! null ontology received!";
		 }
		 
		 GentzenTree tree = computeGentzenTree(axiom,  reasoner, factory, ontology, maxsteps, maxtime, ruleset);
		 if (tree==null){
			 System.out.println("EMPTY TREE!");
		 }
		
		 OWLFormula axiomFormula;
		try {  
			axiomFormula = ConversionManager.fromOWLAPI(axiom);
			List<SequentInferenceRule> rules = tree.getInfRules();
			if (rules.size()==0){
				String result = "That's already stated in the ontology. ";
				result += VerbaliseTreeManager.makeUppercaseStart(VerbalisationManager.verbalise(ConversionManager.toOWLAPI(axiomFormula))) + ".";
				// for (OWLFormula just : justificationFormulas){
				// 	result += just.toString() + "; ";
				// }
				return result;
			}
			
			//Obfuscation
			
			Obfuscator obfuscator = null;
			if (obf){
				BlackBoxExplanation bBexplanator=new BlackBoxExplanation(ontology, factory,reasoner);
				 HSTExplanationGenerator explanationGenerator=new HSTExplanationGenerator(bBexplanator);
				 OWLDataFactory dataFactory= OWLAPIManagerManager.INSTANCE.getDataFactory();
				 
				 Set<OWLAxiom> explanation = new HashSet<OWLAxiom>();	
				 if (axiom instanceof OWLSubClassOfAxiom){
					   explanation = explanationGenerator.getExplanation(dataFactory.getOWLObjectIntersectionOf(((OWLSubClassOfAxiom) axiom).getSubClass(), ( (OWLSubClassOfAxiom) axiom).getSuperClass().getObjectComplementOf()));
				 }
				 
			// Fantasy animal name
			List<String> obClassnames = Arrays.asList("Leog", "Graurs", "Octaseo", "Radaps", "vladerpillar", "koursatee",
					"ksrilla", "riquito", "slounea", "ceoti", "Hexapon", "Buffa","Barrib","Penpe","wauperine","blumate",
					"wacseeling","kloopstile","kreebraut",""
					);
			List<String> obRolenames = Arrays.asList("moddles", "usterizes", "nolds", "helmbengs", "urds");
			
			
			obfuscator = new Obfuscator(explanation,obRolenames,obClassnames);
			}
			
	
			// Params
			// 1: tree 
			// 2: show rule names
			// 3: HTML/text
			
			String result = VerbaliseTreeManager.verbaliseNL(tree, false,asHTML,obfuscator); // parameter for rule names!
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "failure";	
	}
	
	public static TextElementSequence verbalizeAxiomAsSequence(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory, OWLOntology ontology, int maxsteps, long maxtime, String ruleset, boolean asHTML, boolean obf){
		
		TextElementSequence resultSequence = new TextElementSequence();
		
		 VerbalisationManager.INSTANCE.setOntology(ontology);
		 if (ontology==null){
			 LogicElement element = new LogicElement("Failure! Null ontology received!");
			 resultSequence.add(element);
			 return resultSequence;
		 }
		 
		 GentzenTree tree = computeGentzenTree(axiom,  reasoner, factory, ontology, maxsteps, maxtime, ruleset);
		 if (tree==null){
			 LogicElement element = new LogicElement("Failure! Empty tree!");
			 resultSequence.add(element);
			 return resultSequence;
		 }
		
		 OWLFormula axiomFormula;
		try {  
			axiomFormula = ConversionManager.fromOWLAPI(axiom);
			List<SequentInferenceRule> rules = tree.getInfRules();
			if (rules.size()==0){
				String result = "That's already stated in the ontology. ";
				result += VerbaliseTreeManager.makeUppercaseStart(VerbalisationManager.verbalise(ConversionManager.toOWLAPI(axiomFormula))) + ".";
				// for (OWLFormula just : justificationFormulas){
				// 	result += just.toString() + "; ";
				// }
				LogicElement element = new LogicElement(result);
				resultSequence.add(element);
				return resultSequence;
			}
			
			//Obfuscation
			
			Obfuscator obfuscator = null;
			if (obf){
				BlackBoxExplanation bBexplanator=new BlackBoxExplanation(ontology, factory,reasoner);
				 HSTExplanationGenerator explanationGenerator=new HSTExplanationGenerator(bBexplanator);
				 OWLDataFactory dataFactory= OWLAPIManagerManager.INSTANCE.getDataFactory();
				 
				 Set<OWLAxiom> explanation = new HashSet<OWLAxiom>();	
				 if (axiom instanceof OWLSubClassOfAxiom){
					   explanation = explanationGenerator.getExplanation(dataFactory.getOWLObjectIntersectionOf(((OWLSubClassOfAxiom) axiom).getSubClass(), ( (OWLSubClassOfAxiom) axiom).getSuperClass().getObjectComplementOf()));
				 }
				 
			// Fantasy animal name
			List<String> obClassnames = Arrays.asList("Leog", "Graurs", "Octaseo", "Radaps", "vladerpillar", "koursatee",
					"ksrilla", "riquito", "slounea", "ceoti", "Hexapon", "Buffa","Barrib","Penpe","wauperine","blumate",
					"wacseeling","kloopstile","kreebraut",""
					);
			List<String> obRolenames = Arrays.asList("moddles", "usterizes", "nolds", "helmbengs", "urds");
			
			
			obfuscator = new Obfuscator(explanation,obRolenames,obClassnames);
			}
			
	
			// Params
			// 1: tree 
			// 2: show rule names
			// 3: HTML/text
			
			TextElementSequence textElementSequence = VerbaliseTreeManager.verbaliseTextElementSequence(tree, false, obfuscator);
			
			//String result = VerbaliseTreeManager.verbaliseNL(tree, false,asHTML,obfuscator); // parameter for rule names!
			//LogicElement element = new LogicElement(result);
			//resultSequence.add(element);
			return textElementSequence;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String result = e.toString();
			LogicElement element = new LogicElement(result);
			resultSequence.add(element);
			return resultSequence;
		} 
		
	}
	
	
}
