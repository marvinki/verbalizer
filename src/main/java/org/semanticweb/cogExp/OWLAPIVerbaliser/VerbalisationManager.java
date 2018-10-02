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

package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintClassExpressionVisitor;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintOWLAxiomVisitor;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintOWLObjectVisitor;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.api.ExplanationManager;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
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
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

// import org.apache.log4j.Level;
// import org.apache.log4j.Logger;


import com.clarkparsia.owlapi.explanation.BlackBoxExplanation;
import com.clarkparsia.owlapi.explanation.HSTExplanationGenerator;



public enum VerbalisationManager {
	INSTANCE;

//	static final TextElementOWLObjectVisitor textOWLObjectVisit = new TextElementOWLObjectVisitor();
	static final SentenceOWLObjectVisitor sentenceOWLObjectVisit = new SentenceOWLObjectVisitor();
	
	static final PrettyPrintClassExpressionVisitor ppCEvisit = new PrettyPrintClassExpressionVisitor();
	static final PrettyPrintOWLAxiomVisitor ppOAvisit = new PrettyPrintOWLAxiomVisitor();
	static final PrettyPrintOWLObjectVisitor ppOOvisit = new PrettyPrintOWLObjectVisitor();

	
	private OWLOntology ontology;
	private boolean ontologyLabelsIncludeDeterminers = false;
	public final static String _space = " "; // SETTING SPACER!

	public boolean featureRoleAgg = false;
	public boolean featureClassAgg = false;
	public boolean featureAttribute = false;

	public boolean featuresOFF = false;;

	public boolean includesHasValue = false;

//	private Language lang = Language.GERMAN;
	private Locale lang = VerbaliseTreeManager.locale;
	
	private static URL[] getUrls(){
		try {
			URL[] urls = {file.toURI().toURL()};
			return urls;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static File file = new File("./resource");
	private static URL[] urls = getUrls();
	public static ClassLoader loader = new URLClassLoader(urls);
	// public final static ResourceBundle LogicLabels = ResourceBundle.getBundle("LogicLabels", Locale.getDefault(), loader)
	public static ResourceBundle LogicLabels = ResourceBundle.getBundle("LogicLabels", VerbaliseTreeManager.locale, loader);
	
	// public final static String logiclabelsPath = "resource.LogicLabels";

	// private static ResourceBundle LogicLabels = ResourceBundle.getBundle(logiclabelsPath, VerbaliseTreeManager.locale);
	
	private static boolean verbalisationManagerdebug = VerbaliseTreeManager.verbalisationManagerdebug;
	
	

	public static TextElementSequence textualise(OWLObject ob) {
		TextElementSequence seq = new TextElementSequence(ob.accept(sentenceOWLObjectVisit).toList());
		if(verbalisationManagerdebug) seq.add(new LogicElement("textualise(OWLObject ob)"));
		return seq;
	}

	public static TextElementSequence textualise(OWLObject ob, Obfuscator obfuscator) {
		sentenceOWLObjectVisit.setObfuscator(obfuscator);
		TextElementSequence seq = new TextElementSequence(ob.accept(sentenceOWLObjectVisit).toList());
//		System.out.println("<c"+ seq.toString() +"c>");
		
		if(verbalisationManagerdebug) seq.add(new LogicElement("textualise(OWLObject ob, Obfuscator obfuscator)"));
		return seq;
	}
	
	public static TextElementSequence textualise(OWLObject ob, Obfuscator obfuscator, SentenceOrder order) {
		TextElementSequence seq = new TextElementSequence(ob.accept(sentenceOWLObjectVisit).toList());
		if(verbalisationManagerdebug) seq.add(new LogicElement("--textualise(ob, order)--"));
		switch(order){
		case A_B_is:			
		case is_A_B:	
		case A_is_B:
			sentenceOWLObjectVisit.setSentenceOrder(order);
			sentenceOWLObjectVisit.setObfuscator(obfuscator);
			return seq;	

		default:
			sentenceOWLObjectVisit.setObfuscator(obfuscator);
//			 System.out.println("dealing with owl object " + ob);
			return seq;	
		}
				
	}

	/**
	 * Indicate to the VerbalisationManager the current ontology where to find
	 * information
	 * 
	 * @param ontology
	 *            -- the current ontology
	 */
	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
		ontologyLabelsIncludeDeterminers = detectDeterminerLabels();
	}

	/**
	 * Retrieve the current ontology
	 * 
	 * @return the current ontology
	 */
	public OWLOntology getOntology() {
		return ontology;
	}

	/**
	 * Pretty print an OWL-API class expression (to DL syntax)
	 * 
	 * @param ce
	 *            class expression
	 * @return pretty printed string for class expression
	 */
	public static String prettyPrint(OWLClassExpression ce) {
		return ce.accept(ppCEvisit);
	}

	/**
	 * Pretty print an OWL-API subclass relationship (to DL syntax)
	 * 
	 * @param oa
	 *            subclass axiom
	 * @return pretty printed string for subclass axiom
	 */
	public static String prettyPrint(OWLSubClassOfAxiom oa) {
		return ppOAvisit.visit(oa);
	}
	
	public static List<String> prettyPrint(List<OWLObject> lst){
		List<String> stringlist = new ArrayList<String>();
		for (OWLObject l: lst){
			stringlist.add(prettyPrint(l));
		}
		return stringlist;
	}
	

	/**
	 * Pretty print an OWL-API Object (to DL syntax)
	 * 
	 * @param oo
	 *            object
	 * @return pretty printed string for object
	 */
	public static String prettyPrint(OWLObject oo) {
		return oo.accept(ppOOvisit);
	}

	/**
	 * Pretty print an OWLFormula (to DL syntax)
	 * 
	 * @param form
	 *            formula
	 * @return pretty printed string for formula
	 */
	public static String prettyPrint(OWLFormula form) {
		return prettyPrint(ConversionManager.toOWLAPI(form));
	}

	/**
	 * Obtain the input string with the first character in lowercase
	 * 
	 * @param s
	 *            the original string
	 * @return the original string with the first character in lowercase
	 */
	public static String lowerCaseFirstLetter(String s) {
		if (s.length() > 0 && Character.isUpperCase(s.charAt(0))){
			// count the uppercase in the letter to avoid the Machine Type being lowercased 
			int countUppercase = 0;
			for(int i = 0; i<s.length(); i++){
				if (Character.isUpperCase(s.charAt(i)))
					countUppercase++;
			};
			
			if(countUppercase>=2){
				System.out.println(s);
				return s;
			}
			else{
			return s.substring(0, 1).toLowerCase() + s.substring(1, s.length());
			}}
		else{
			return s;}
	}

	public String getPropertyNLString(OWLObjectPropertyExpression property) {
		if (VerbaliseTreeManager.locale.equals(Locale.GERMAN)){
			if (getLabel(property.asOWLObjectProperty(),"de")!=null)
				System.out.println("VerbalisationManager returning property : " + getLabel(property.asOWLObjectProperty(),"de"));
				return getLabel(property.asOWLObjectProperty(),"de");
		}
		// adding english label version
		if (VerbaliseTreeManager.locale.equals(Locale.ENGLISH)){
			if (getLabel(property.asOWLObjectProperty(),"en")!=null)
				System.out.println("VerbalisationManager returning property : " + getLabel(property.asOWLObjectProperty(),"en"));
				return getLabel(property.asOWLObjectProperty(),"en");
		}
		OWLProperty namedproperty = property.getNamedProperty();
		System.out.println("DEBUG - named : " + namedproperty);
		String str = "";
		if (namedproperty != null) {
			if (this.ontology == null) { // if no ontology is provided, simply
											// return the classname
				// System.out.println("DEBUG + " +
				// property.getNamedProperty().getIRI().getFragment());
				String result = property.getNamedProperty().getIRI().getFragment();
				if (sentenceOWLObjectVisit.getObfuscator() != null) {
					result = sentenceOWLObjectVisit.getObfuscator().obfuscateRole(result);
				}
				return result;
			}
			// collect annotation axioms
			Set<OWLOntology> imported = ontology.getImports();
			Set<OWLAnnotationAssertionAxiom> annotationaxioms = new HashSet<OWLAnnotationAssertionAxiom>();
			for (OWLOntology ont : imported) {
				//
				/*
				 * Stream<OWLAnnotationAssertionAxiom> aaa =
				 * EntitySearcher.getAnnotationAssertionAxioms(namedproperty,
				 * this.ontology); OWLAnnotationAssertionAxiom[] aaa2 =
				 * (OWLAnnotationAssertionAxiom[]) aaa.toArray();
				 * annotationaxioms.addAll(Arrays.asList(aaa2));
				 */
				annotationaxioms.addAll(EntitySearcher.getAnnotationAssertionAxioms(namedproperty, this.ontology));
				// annotationaxioms.addAll(namedproperty.getAnnotationAssertionAxioms(ont));
				// annotationaxioms.addAll(namedproperty.getAnnotationAssertionAxioms(ont))
			}
			//

			/*
			 * Stream<OWLAnnotationAssertionAxiom> aaa3 =
			 * EntitySearcher.getAnnotationAssertionAxioms(namedproperty,
			 * this.ontology); OWLAnnotationAssertionAxiom[] aaa4 =
			 * (OWLAnnotationAssertionAxiom[]) aaa3.toArray();
			 * annotationaxioms.addAll(Arrays.asList(aaa4));
			 */
			annotationaxioms.addAll(EntitySearcher.getAnnotationAssertionAxioms(namedproperty, this.ontology));
			// annotationaxioms.addAll(namedproperty.getAnnotationAssertionAxioms(ontology));
			if (annotationaxioms != null) {
				for (OWLAnnotationAssertionAxiom axiom : annotationaxioms) {
					if (axiom.getAnnotation().getProperty().getIRI().getFragment().equals("label")) {
						// System.out.println("DEBUG TO STRING " +
						// axiom.getAnnotation().getValue());
						// SimpleRenderer renderer = new SimpleRenderer();
						// OWLLiteral literal =
						// axiom.getAnnotation().getValue().asLiteral().get();
						
						// 
						OWLLiteral literal = OWLAPICompatibility.getLiteral(axiom); // axiom.getAnnotation().getValue().asLiteral().get();
								
						str = literal.getLiteral();
						
						
						// str = axiom.getAnnotation().getValue().toString();
					}
				}
			}
		} // Obfuscate

		// shortcut for labels with [X]
		if (str.indexOf("[X]") > -1) {
			if (sentenceOWLObjectVisit.getObfuscator() != null) {
				str = sentenceOWLObjectVisit.getObfuscator()
						.obfuscateRole(property.getNamedProperty().getIRI().getFragment());
			}
			return str;
		}
		// remove language tags
		if (str.indexOf("@en") > 0) {
			str = str.substring(1, str.length() - 4);
		}
		// In case there are no annotations
		// -- detect if camel case
		if (str == "") {
			str = namedproperty.getIRI().getFragment();
			// System.out.println("DEBUG " + str);
		}
		// Obfuscate!
		if (sentenceOWLObjectVisit.getObfuscator() != null) {
			str = sentenceOWLObjectVisit.getObfuscator().obfuscateRole(str);
		}
		/*
		 * if (TextElementOWLObjectVisitor.detectLowCamelCase(str)) str =
		 * TextElementOWLObjectVisitor.removeCamelCase(str); // heuristic!
		 * if(str.indexOf("_of")>=0) str =
		 * TextElementOWLObjectVisitor.removeUnderscores(str);
		 * if(str.indexOf("_")>=0) str =
		 * TextElementOWLObjectVisitor.removeUnderscores(str);
		 */
		str = treatCamelCaseAndUnderscores(str);
		if (str.indexOf("^^xsd:string") >= 0)
			str = str.substring(1, str.length() - 13);
		// detect if name is of the form "_ of"
		if (detect_noun_of(str) && !str.contains("is"))
			str = LogicLabels.getString("is") + _space + str;
		return str;
	}
	
	public String getPropertyNLString(OWLDataPropertyExpression property) {
		if (VerbaliseTreeManager.locale.equals(Locale.GERMAN)){
			if (getLabel(property.asOWLDataProperty(),"de")!=null)
				return getLabel(property.asOWLDataProperty(),"de");
		}
		if (VerbaliseTreeManager.locale.equals(Locale.ENGLISH)){
			if (getLabel(property.asOWLDataProperty(),"en")!=null)
				return getLabel(property.asOWLDataProperty(),"en");
		}
		OWLProperty namedproperty  = property.asOWLDataProperty();
		// System.out.println("DEBUG - named : " + namedproperty);
		String str = "";
		if (namedproperty != null) {
			if (this.ontology == null) { // if no ontology is provided, simply
											// return the classname
				// System.out.println("DEBUG + " +
				// property.getNamedProperty().getIRI().getFragment());
				String result = property.asOWLDataProperty().getIRI().getFragment();
				/*
				if (textOWLObjectVisit.getObfuscator() != null) {
					result = textOWLObjectVisit.getObfuscator().obfuscateRole(result);
				}
				*/
				return result;
			}
			// collect annotation axioms
			Set<OWLOntology> imported = ontology.getImports();
			Set<OWLAnnotationAssertionAxiom> annotationaxioms = new HashSet<OWLAnnotationAssertionAxiom>();
			for (OWLOntology ont : imported) {
				//
				/*
				 * Stream<OWLAnnotationAssertionAxiom> aaa =
				 * EntitySearcher.getAnnotationAssertionAxioms(namedproperty,
				 * this.ontology); OWLAnnotationAssertionAxiom[] aaa2 =
				 * (OWLAnnotationAssertionAxiom[]) aaa.toArray();
				 * annotationaxioms.addAll(Arrays.asList(aaa2));
				 */
				annotationaxioms.addAll(EntitySearcher.getAnnotationAssertionAxioms(namedproperty, this.ontology));
				// annotationaxioms.addAll(namedproperty.getAnnotationAssertionAxioms(ont));
				// annotationaxioms.addAll(namedproperty.getAnnotationAssertionAxioms(ont))
			}
			//

			/*
			 * Stream<OWLAnnotationAssertionAxiom> aaa3 =
			 * EntitySearcher.getAnnotationAssertionAxioms(namedproperty,
			 * this.ontology); OWLAnnotationAssertionAxiom[] aaa4 =
			 * (OWLAnnotationAssertionAxiom[]) aaa3.toArray();
			 * annotationaxioms.addAll(Arrays.asList(aaa4));
			 */
			annotationaxioms.addAll(EntitySearcher.getAnnotationAssertionAxioms(namedproperty, this.ontology));
			// annotationaxioms.addAll(namedproperty.getAnnotationAssertionAxioms(ontology));
			if (annotationaxioms != null) {
				for (OWLAnnotationAssertionAxiom axiom : annotationaxioms) {
					if (axiom.getAnnotation().getProperty().getIRI().getFragment().equals("label")) {
						// System.out.println("DEBUG TO STRING " +
						// axiom.getAnnotation().getValue());
						// SimpleRenderer renderer = new SimpleRenderer();
						// OWLLiteral literal =
						// axiom.getAnnotation().getValue().asLiteral().get();
						
						// redo!
						OWLLiteral literal = OWLAPICompatibility.getLiteral(axiom); //  axiom.getAnnotation().getValue().asLiteral().get();
						str = literal.getLiteral();
						
						
						// str = axiom.getAnnotation().getValue().toString();
					}
				}
			}
		} // Obfuscate

		// shortcut for labels with [X]
		if (str.indexOf("[X]") > -1) {
			/*
			if (textOWLObjectVisit.getObfuscator() != null) {
				str = textOWLObjectVisit.getObfuscator()
						.obfuscateRole(property.asOWLDataProperty().getIRI().getFragment());
			}
			*/
			return str;
		}
		// remove language tags
		if (str.indexOf("@en") > 0) {
			str = str.substring(1, str.length() - 4);
		}
		// In case there are no annotations
		// -- detect if camel case
		if (str == "") {
			str = namedproperty.getIRI().getFragment();
			// System.out.println("DEBUG " + str);
		}
		// Obfuscate!
		/*
		if (textOWLObjectVisit.getObfuscator() != null) {
			str = textOWLObjectVisit.getObfuscator().obfuscateRole(str);
		}
		*/
		/*
		 * if (VerbaliseOWLObjectVisitor.detectLowCamelCase(str)) str =
		 * VerbaliseOWLObjectVisitor.removeCamelCase(str); // heuristic!
		 * if(str.indexOf("_of")>=0) str =
		 * VerbaliseOWLObjectVisitor.removeUnderscores(str);
		 * if(str.indexOf("_")>=0) str =
		 * VerbaliseOWLObjectVisitor.removeUnderscores(str);
		 */
		str = treatCamelCaseAndUnderscores(str);
		if (str.indexOf("^^xsd:string") >= 0)
			str = str.substring(1, str.length() - 13);
		// detect if name is of the form "_ of"
		if (detect_noun_of(str) && !str.contains("is"))
			str = LogicLabels.getString("is") + _space + str;
		return str;
	}
	

	public static String verbaliseProperty(OWLObjectPropertyExpression property, List<String> fillerstrs, String middle,
			Obfuscator obfuscator) {
		String result = "";

		//
		if (fillerstrs.size() > 1) {
			// hey, we certainly got a feature!
			// System.out.println("feature debug " + fillerstrs);
			VerbalisationManager.INSTANCE.featureRoleAgg = true;
		}

		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(property);

		// Obfuscate, if needed
		if (obfuscator != null) {
			propstring = obfuscator.obfuscateRole(propstring);
		}

		// check case where string contains a pattern.
		if (propstring.indexOf("[X]") >= 0) {
			String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(property);
			String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
			result += part1;
			
			// TODO has as --> LogicLabel???
			if (part2.equals("") && part1.equals("") || part1 == null && part2 == null) {
				result += "has as" + _space + property.getNamedProperty().getIRI().getFragment() + "-successor ";
				;
			}
		} else {
			result += propstring + " ";
		}
		result += middle;
		boolean needsep = false;
		for (String str : fillerstrs) {
			if (needsep) {
				result += _space + LogicLabels.getString("and") + _space;
			}
			result += str;
			needsep = true;
		}
		if (VerbalisationManager.INSTANCE.getPropertyNLString(property).indexOf("[X]") >= 0)
			result += VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
		result = treatCamelCaseAndUnderscores(result);

		// System.out.println("DEBUG PROPERTY |" + result + "|");
		result.trim();
		return result;
	}

	public List<TextElement> textualiseProperty(OWLObjectPropertyExpression property,
			List<List<TextElement>> fillerelements, List<TextElement> middle) {
		// String result = "";
		List<TextElement> result = new ArrayList<TextElement>();
		//
		if (fillerelements.size() > 1) {
			// hey, we certainly got a feature!
			VerbalisationManager.INSTANCE.featureRoleAgg = true;
		}

		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(property);
		// check case where string contains a pattern.
		if (propstring.indexOf("[X]") >= 0) {
			String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(property);
			String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
			if (part1.endsWith(" ")) {
				part1 = part1.substring(0, part1.length() - 1);
			}
			if (part2.startsWith(" ")) {
				part2 = part2.substring(1, part2.length());
			}
			result.add(new RoleElement(part1)); // += part1;
			if (part2.equals("") && part1.equals("") || part1 == null && part2 == null) {
				result.add(new RoleElement(
						"has as" + _space + property.getNamedProperty().getIRI().getFragment() + "-successor "));
				// result += "has as" + _space +
				// property.getNamedProperty().getIRI().getFragment() +
				// "-successor ";;
			}
		} else {
			result.add(new RoleElement(propstring));
		}

		result.addAll(middle);
		boolean needsep = false;
		for (List<TextElement> str : fillerelements) {
			if (needsep) {
				if (lang.equals(Locale.ENGLISH))
					result.add(new LogicElement("and"));
				else
					result.add(new LogicElement("und"));
				// result += _space + "and" + _space;
			}
			result.addAll(str);
			needsep = true;
		}
		if (VerbalisationManager.INSTANCE.getPropertyNLString(property).indexOf("[X]") >= 0) {
			String p2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
			if (p2.startsWith(" ")) {
				p2 = p2.substring(1, p2.length());
			}
			result.add(new RoleElement(p2));
		}
		// result +=
		// VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);

		return result;
	}
	
	public static Sentence textualisePropertyAsSentence(OWLObjectPropertyExpression property,
			List<List<TextElement>> fillerelements, List<TextElement> middle) {
		Sentence result = new Sentence();
		
		//
		if (fillerelements.size() > 1) {
			// hey, we certainly got a feature!
			VerbalisationManager.INSTANCE.featureRoleAgg = true;
		}

		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(property);
		// check case where string contains a pattern.
		if (propstring.indexOf("[X]") >= 0) {
			String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(property);
			String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
			if (part1.endsWith(" ")) {
				part1 = part1.substring(0, part1.length() - 1);
			}
			if (part2.startsWith(" ")) {
				part2 = part2.substring(1, part2.length());
			}
			result.setPraedikat(new RoleElement(part1)); // += part1;
			if (part2.equals("") && part1.equals("") || part1 == null && part2 == null) {
				result.setPraedikat(new RoleElement(
						"has as" + _space + property.getNamedProperty().getIRI().getFragment() + "-successor "));
				// result += "has as" + _space +
				// property.getNamedProperty().getIRI().getFragment() +
				// "-successor ";;
			}
		} else {
			result.setPraedikat(new RoleElement(propstring));
		}

		result.addToObject(new TextElementSequence(middle));
		boolean needsep = false;
		for (List<TextElement> str : fillerelements) {
			if (needsep) {
				result.setObjekt(new LogicElement("and"));
				// result += _space + "and" + _space;
			}
			result.addToObject(new TextElementSequence(str));
			needsep = true;
		}
		if (VerbalisationManager.INSTANCE.getPropertyNLString(property).indexOf("[X]") >= 0) {
			String p2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
			if (p2.startsWith(" ")) {
				p2 = p2.substring(1, p2.length());
			}
			result.setObjekt(new RoleElement(p2));
		}
		// result +=
		// VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);

		return result.getSentence();
	}
	
	public static Sentence textualiseDataPropertyAsSentence(OWLDataPropertyExpression property,
			List<List<TextElement>> fillerelements, List<TextElement> middle) {
		Sentence result = new Sentence();
		
		//
		if (fillerelements.size() > 1) {
			// hey, we certainly got a feature!
			VerbalisationManager.INSTANCE.featureRoleAgg = true;
		}

		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(property);
		System.out.println("propstring " + propstring);
		// check case where string contains a pattern.
		if (propstring.indexOf("[X]") >= 0) {
			String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(property);
			String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
			if (part1.endsWith(" ")) {
				part1 = part1.substring(0, part1.length() - 1);
			}
			if (part2.startsWith(" ")) {
				part2 = part2.substring(1, part2.length());
			}
			result.setPraedikat(new RoleElement(part1)); // += part1;
			if (part2.equals("") && part1.equals("") || part1 == null && part2 == null) {
				result.setPraedikat(new RoleElement(
						"has as" + _space + property.asOWLDataProperty().getIRI().getFragment() + "-successor "));
				// result += "has as" + _space +
				// property.getNamedProperty().getIRI().getFragment() +
				// "-successor ";;
			}
		} else {
			result.setPraedikat(new RoleElement(propstring));
		}

		result.addToObject(new TextElementSequence(middle));
		boolean needsep = false;
		for (List<TextElement> str : fillerelements) {
			if (needsep) {
				result.setSubjekt(new LogicElement("and"));
				// result += _space + "and" + _space;
			}
			result.addToObject(new TextElementSequence(str));
			needsep = true;
		}
		if (VerbalisationManager.INSTANCE.getPropertyNLString(property).indexOf("[X]") >= 0) {
			String p2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
			if (p2.startsWith(" ")) {
				p2 = p2.substring(1, p2.length());
			}
			result.setObjekt(new RoleElement(p2));
		}
		// result +=
		// VerbalisationManager.INSTANCE.getPropertyNLStringPart2(property);
System.out.println("textualiseDataPropertyAsSentence returns " + result);
		
		return result; // .getSentence();
	}
	

	public static String aOrAnIfy(String str) {
		// System.out.println("a or an |" + str);
		// first check if there are any determiners present; if so, leave
		// unchanged
		if ((str.length() > 1 && str.substring(0, 2).equals("a" + _space))
				|| (str.length() > 2 && str.substring(0, 3).equals("an" + _space))
				|| (str.length() > 3 && str.substring(0, 4).equals("the" + _space)))
			return str;
		if (str.length() < 1)
			return str; // discard empty string
		// check if we are dealing with something that is not a noun, in this
		// case leave unchanged
		// check known reasons to believe something is a noun
		boolean isNoun = false;
		if (str.contains("oid"))
			isNoun = true;
		if (str.contains("publisher"))
			isNoun = true;
		// System.out.println(WordNetQuery.INSTANCE.isType(str,SynsetType.NOUN)>0);
		/*
		 * System.out.println("0:" + WordNetQuery.INSTANCE.getTypes(str)[0]); //
		 * noun System.out.println("1:" +
		 * WordNetQuery.INSTANCE.getTypes(str)[1]); // verb
		 * System.out.println("2:" + WordNetQuery.INSTANCE.getTypes(str)[2]); //
		 * adjective System.out.println("3:" +
		 * WordNetQuery.INSTANCE.getTypes(str)[3]); // adverb
		 * System.out.println("4:" + WordNetQuery.INSTANCE.getTypes(str)[4]); //
		 * adjective_satellite
		 */

		int[] types = null;
		// if (!WordNetQuery.INSTANCE.isDisabled())
		// 	types = WordNetQuery.INSTANCE.getTypes(str);
		// if (!WordNetQuery.INSTANCE.isDisabled() && !(types[0] > 0) && !isNoun) {
		// 	// System.out.println("NOT A NOUN");
		// 	str = lowerCaseFirstLetter(str);
		// 	// System.out.println("not using article");
		// 	return str;
		// }
		// Check if gerund, if so, leave unchanged!
		if (str.indexOf("ing") > 0 && !str.equals("Ring") && !str.equals("ring") && !str.contains(" ring")
				&& !str.contains("Ring"))
			return str.toLowerCase();
		// Plural does not get an article

		if (false) {// (!WordNetQuery.INSTANCE.isDisabled() && WordNetQuery.INSTANCE.isPlural(str)) {

			return str;
		}
		
		// TODO an/a --> LogicLabel???
		if (str.substring(0, 1).equals("a") || str.substring(0, 1).equals("u") || str.substring(0, 1).equals("e")
				|| str.substring(0, 1).equals("i") || str.substring(0, 1).equals("o") || str.substring(0, 1).equals("A")
				|| str.substring(0, 1).equals("U") || str.substring(0, 1).equals("E") || str.substring(0, 1).equals("I")
				|| str.substring(0, 1).equals("O"))
			str = "an" + _space + str;
		else {
			// System.out.println("NOUN");
			str = "a" + _space + str;
		}
		// System.out.println("returning >>" + str);
		return str.toLowerCase();
	}

	public static String treatCamelCaseAndUnderscores(String str) {
		if (str.contains("+") || str.contains("-"))
			return str;
		
		String resultstring = "";
		
		if(VerbaliseTreeManager.locale == Locale.GERMAN) return str;
		
		if(str.length() <= 0 || str.isEmpty() || str.equals("") ) 
			return resultstring;

			
		List<String> tokens = new ArrayList<String>();
		// detect tokens delineated by ' ', '_' and camelcasing "aA"
		String currenttoken = "";
		String lastChar = "";
		for (int i = 0; i < str.length(); i++) {
			// detect THE END
			if (i == str.length() - 1) {
				currenttoken = currenttoken + str.substring(i, i + 1);
				// System.out.println("adding token (1) >" + currenttoken + "<");
				tokens.add(currenttoken);
				break;
			}
			// detect seperator
			if (str.substring(i, i + 1).equals("_") || str.substring(i, i + 1).equals(" ")) {
				// System.out.println("adding token (2) >" + currenttoken + "<");
				tokens.add(currenttoken);
				currenttoken = "";
				lastChar = "";
				continue;
			}
			// detect camelcasing
			if (Character.isUpperCase(str.charAt(i)) && lastChar.length() > 0
					&& Character.isLowerCase(lastChar.charAt(0))) {
				// System.out.println("adding token (3) >" + currenttoken + "<");
				tokens.add(currenttoken);
				currenttoken = str.substring(i, i + 1);
				lastChar = currenttoken;
				continue;
			}
			currenttoken = currenttoken + str.substring(i, i + 1);
			lastChar = str.substring(i, i + 1);
		}
		// System.out.println("postprocessing");
		// now postprocess all tokens
		List<String> processedtokens = new ArrayList<String>();
		boolean tokenEmpty = false;
		for (String token : tokens) {
			// System.out.println(">" + token + "<");
			// if acronym (both first letters are capitals), do nothing
			
			if(token.length() <= 0 || token.isEmpty() || token.equals("")){
				tokenEmpty =true;
			}
			
			if (!tokenEmpty && token.length() > 1 && Character.isUpperCase(str.charAt(0)) && Character.isUpperCase(str.charAt(1))) {
				processedtokens.add(token);
				continue;
			}
			// now lowercase
			if (!tokenEmpty){ // token.length()>0)
				int countUppercase = 0;
				for(int i = 0; i<token.length(); i++){
					if (Character.isUpperCase(token.charAt(i)))
						countUppercase++;
				};
				
				if(countUppercase>=2){
					System.out.println(token);
				}else{
					token = token.substring(0, 1).toLowerCase() + token.substring(1, token.length());
				}
			}
			// now find hypens and lowercase
			int ind = 0;
			// System.out.println("working on token" + token);
			while (ind < token.length()) {
				// System.out.println("ind " + ind + " token.length() " + token.length());
				int foundint = token.substring(ind).indexOf("-") + ind;
				//System.out.println(token.charAt(foundint+1) + " " + foundint + " " + ind);
				if (foundint < 0 || foundint < ind || foundint + 1 > token.length())
					break;
				token = token.substring(0, foundint) + "-" + Character.toLowerCase(token.charAt(foundint + 1))
						+ token.substring(foundint + 2);
				// System.out.println("token " + token);
				ind = foundint + 1;
			}
			
			processedtokens.add(token);
		}
		// System.out.println("joining");
		// now join all tokens together
		for (int i = 0; i < processedtokens.size(); i++) {
			if (i == processedtokens.size() - 1) {
				resultstring += processedtokens.get(i);
			} else {
				resultstring += processedtokens.get(i) + _space;
			}
		}
		
		//System.out.println("DEBUG TREATED " + resultstring);
		return resultstring;

	}

	/*
	 * OLD--- in case the new thing does not work as well public static String
	 * treatCamelCaseAndUnderscores(String str){ if
	 * (TextElementOWLObjectVisitor.detectUnderCamel(str)) return
	 * TextElementOWLObjectVisitor.removeUnderCamel(str); if
	 * (TextElementOWLObjectVisitor.detectLowCamelCase(str) ||
	 * TextElementOWLObjectVisitor.detectCamelCase(str)){ return
	 * TextElementOWLObjectVisitor.removeCamelCase(str); } return
	 * TextElementOWLObjectVisitor.removeUnderscores(str); }
	 */
	
	public String getLabel(OWLEntity obj, String lang){
		// System.out.println("called getLabel for " + obj + " lang " + lang);
		String label = "";
		String fallback = "";
		Set<OWLAnnotation> annotations = collectAnnotations(obj);
		for (OWLAnnotation annotation : annotations) {
			// System.out.println("annotation : " + annotation);
			// System.out.println(annotation.getProperty().getIRI().getFragment().equals("label"));
			// System.out.println(annotation.getValue().asLiteral().orNull().hasLang(lang));
			
			
			if (annotation.getProperty().getIRI().getFragment().equals("label") 
					&& OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().hasLang(lang)
					) {
			
			// if (annotation.getProperty().getIRI().getFragment().equals("label") && annotation.getValue().asLiteral().orNull().hasLang(lang)
			// 		) {
				
				
				// System.out.println("DBG: " +  annotation.getValue().asLiteral());
				
				// label = annotation.getValue().asLiteral().orNull().getLiteral();// annotation.getValue().toString()
				
				label = OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().getLiteral();// annotation.getValue().toString()
			
				
			}
			// fallback = annotation.getValue().asLiteral().orNull().getLiteral();
			fallback = OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().getLiteral();
		}
		if (label.equals("") && !fallback.equals(""))
			return fallback;
		if (label.equals(""))
			return null;
		return label;
	}
	
	
	
	public String getDataPropertyNL(OWLDataProperty arg0){
//		if (lang.equals(Locale.ENGLISH))
		if (VerbaliseTreeManager.locale.equals(Locale.ENGLISH))
			return VerbalisationManager.treatCamelCaseAndUnderscores(arg0.getIRI().getFragment().toString());
		Set<OWLAnnotation> annotations = collectAnnotations(arg0);
		String str = "";
		String str2 = "";
		String germanlabel = "";
		for (OWLAnnotation annotation : annotations) {
			// System.out.println("DEBUG --- annotation: " + annotation);
			if (annotation.getProperty().getIRI().getFragment().equals("label")
					) {
				 str = OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().getLiteral();// annotation.getValue().toString()
				// str = annotation.getValue().asLiteral().orNull().getLiteral();// annotation.getValue().toString()
			}
			if (annotation.getProperty().getIRI().getFragment().equals("label") 
					&& OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().hasLang("de")
					) {
				germanlabel = OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().getLiteral();// annotation.getValue().toString()
			}
			if (annotation.getProperty().getIRI().getFragment().equals("label2")) {
				str2 = OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().getLiteral();
				// str2= annotation.getValue().toString();
			}
		}
		if (VerbaliseTreeManager.locale.equals(Locale.GERMAN) && !germanlabel.equals(""))
			return germanlabel ;
		return str;
	}
	
	public Set<OWLAnnotation> collectAnnotations(OWLEntity entity){
		Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>();
		if (this.ontology != null) {
			Set<OWLOntology> imported = ontology.getImportsClosure();
			/*
			 * Stream<OWLAnnotationAssertionAxiom> aaa =
			 * EntitySearcher.getAnnotationAssertionAxioms(classname,
			 * this.ontology); OWLAnnotation[] aaa2 = (OWLAnnotation[])
			 * aaa.toArray(); annotations.addAll(Arrays.asList(aaa2));
			 */
			annotations.addAll(EntitySearcher.getAnnotationObjects(entity, this.ontology));
			// classname.getAnnotations(this.ontology);
			for (OWLOntology ont : imported) {
				//
				/*
				 * Stream<OWLAnnotationAssertionAxiom> aaa3 =
				 * EntitySearcher.getAnnotationAssertionAxioms(classname,
				 * this.ontology); OWLAnnotation[] aaa4 = (OWLAnnotation[])
				 * aaa3.toArray(); annotations.addAll(Arrays.asList(aaa4));
				 */
				annotations.addAll(EntitySearcher.getAnnotationObjects(entity, ont));
				// annotations.addAll(classname.getAnnotations(ont));
			}
		}
		return annotations;
	}

	public String getClassNLString(OWLClass classname) {
		// System.out.println("get class NL String" + this.ontology);
		// System.out.println("get class NL String " + classname);
		// catch some special cases straight away!
		if (classname.isOWLThing())
			return LogicLabels.getString("something");
			
//			switch (VerbaliseTreeManager.locale.toString()) {
//
//			case GERMAN:
//				return "etwas";
//
//			case ENGLISH:
//				return "something";
//
//			default:
//				return "something";
//
//			}

		if (classname.isOWLNothing())
			return "non-existant";

		String str = "";
		String str2 = "";
		boolean hasLabel = false;
		boolean labelFound = false;
		boolean labelLabelFound = false;
		String labelStr = "";
		
		if (this.ontology != null) {
			// System.out.println("before collecting annotations");
			Set<OWLAnnotation> annotations = collectAnnotations(classname);
			// System.out.println("after collecting annotations " + annotations.size());
			
			Set<String> stringsFound = new HashSet<String>();
			for (OWLAnnotation annotation : annotations) {
				
				if (VerbaliseTreeManager.locale==Locale.GERMAN) {
					System.out.println("----- GERMAN LOCALE --------");
					if(OWLAPICompatibility.asLiteral(annotation.getValue()).isPresent() 
							&& OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().hasLang("de")){ //is locale german ?
						str = OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().getLiteral() ;// annotation.getValue().toString()
						System.out.println("prop " + annotation.getProperty());
						if (annotation.getProperty().isLabel())
							labelStr = str;
						if (!labelStr.equals(""))
							str = labelStr; // always override str with labels we have found
						labelFound = true;
					}
					if(!labelFound){
						str = "\"" + OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().getLiteral() + "\"";
						labelFound = true;// annotation.getValue().toString()
					}
				}
				
//				annotation.getProperty().getIRI().getFragment().equals("@en")
				
				
				if (VerbaliseTreeManager.locale==Locale.ENGLISH) { // is locale english ?

					System.out.println("----- ENGLISH LOCALE -----");
					if(OWLAPICompatibility.asLiteral(annotation.getValue()).isPresent() 
							//&& OWLAPICompatibility.asLiteral(annotation.getValue()).isPresent() 
							&& OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().hasLang("en")){
						str = OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().getLiteral() ;// annotation.getValue().toString()
						
		
						
						System.out.println(" prop (1): " + annotation.getProperty());
						System.out.println(" prop (1): " + annotation.getProperty().getIRI());
						System.out.println(" prop (1): " + annotation.getProperty().getIRI().equals(OWLRDFVocabulary.RDFS_LABEL.getIRI()));
						System.out.println(" name is " + str); 
						labelFound = true;
					}if(OWLAPICompatibility.asLiteral(annotation.getValue()).isPresent()
							&& !OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().hasLang("de")	// <-- ignore this if english local and german label is found
							){
						// Marvin: using quotes makes Mrs Koelle's structural cueing module crash.
				
						System.out.println(" Annotation object " + annotation);
						
				
						str = OWLAPICompatibility.asLiteral(annotation.getValue()).orNull().getLiteral();
						System.out.println("prop " + annotation.getProperty().getEntityType());
						System.out.println(" prop (2): " + annotation.getProperty().getIRI());
						System.out.println(" prop (2): " + annotation.getProperty().getIRI().equals(OWLRDFVocabulary.RDFS_LABEL.getIRI()));
						if (annotation.getProperty().asOWLAnnotationProperty().getIRI().toString().equals("http://www.w3.org/2000/01/rdf-schema#label"))
						// if (annotation.getProperty().isLabel())
							labelStr = str;
						if (!labelStr.equals(""))
							str = labelStr; // always override str with labels we have found
						stringsFound.add(str);
						// str = "\"" + annotation.getValue().asLiteral().orNull().getLiteral() + "\"" ;
						labelFound = true;// annotation.getValue().toString()

					}
						// labelFound = true;
					// System.out.println("examining str (2)! "+ str);
					if (annotation.getProperty().isLabel() && str.equals(""))
						str = annotation.getValue().asLiteral().orNull().getLiteral() ;// annotation.getValue().toString()
						labelFound = true;
				}				
					
//				
//					if (annotation.getProperty().getIRI().getFragment().equals("label")) {
//						str = annotation.getValue().asLiteral().orNull().getLiteral() + "(hier)";// annotation.getValue().toString()
//					}
//					if (annotation.getProperty().getIRI().getFragment().equals("label2")) {
//						str2 = annotation.getValue().asLiteral().orNull().getLiteral();
//						// str2= annotation.getValue().toString();
//					}
//				} 
				
			}  // end for annotations 
			
			// if we have many strings, choose one.
			// TODO
			
			
			// remove unnecessary stuff
			if (str.indexOf("@e") > 0)
				str = str.substring(0, str.length() - 3);
		}
		// System.out.println("get class NL String (2) " + classname);
		if (!str.equals("") || !str2.equals(""))
			hasLabel = true;
		// search for the type tag of the annotation (e.g. ^^xsd:string)
		if (str != "") {
			int i = str.indexOf("\"");
			if (str.contains("^")) {
				int j = str.indexOf("^");
				str = str.substring(i + 1, j - 1);
			} else {
				// str= str.substring(i+1,str.length()-1);
			}
			if (lang.equals(Locale.ENGLISH))
				// System.out.println("str (A) " + str);
				str = treatCamelCaseAndUnderscores(str);
				// System.out.println("str (B) " + str);
			// System.out.println("returning (1) " + str);
			// Cheating
			if (str.equals("exercise")) {
				return "an exercise";
			}
			
			// Reduce length
			// if (str.length()>40)
			// 	str = str.substring(0, 40) + "[...]";
			return str;
		}
		// System.out.println("get class NL String (3) " + classname);
		if (str == "") {
				str = classname.getIRI().getFragment();
			
			// str = ppCEvisit.visit(classname);
			// System.out.println("DBG after pretty print visit --- " + str);
			// if (verbOWLObjectVisit.getObfuscator() != null) {
			// 	str = verbOWLObjectVisit.getObfuscator().obfuscateName(str);
			// }
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
			//
			// System.out.println("DEBGU -- " + str);
			if (!isUncountable)
				str = aOrAnIfy(str);
			
			// if (str.length()>40)
			// 	str = str.substring(0, 40) + "[...]";
			
			return str;
		}
		if (!str2.equals("")) {
			str = str2;
		}

		if (!hasLabel || ontologyLabelsIncludeDeterminers == false)
			str = aOrAnIfy(str);
		
		// if (str.length()>40)
		// 	str = str.substring(0, 40) + "[...]";
		
		return str;
	};

	public static String removeStringSuffix(String str) {
		int i = str.indexOf("\"");
		int j = str.indexOf("^");
		if (i >= 0 && j >= 0)
			return str.substring(i + 1, j - 1);
		else
			return str;
	}

	public String getSimpleIntersectionNLString(OWLObjectIntersectionOf ints) {
		List<OWLClassExpression> exprs = ints.getOperandsAsList();
		return getSimpleIntersectionNLString(exprs);
	}

	public String getSimpleIntersectionNLString(List<OWLClassExpression> exprs) {
		// System.out.println("<><> getSimpleIntersectionNLString called");
		String result = "";
		List<OWLObject> noun_concepts = new ArrayList<OWLObject>();
		List<OWLObject> attribute_concepts = new ArrayList<OWLObject>();
		List<OWLObject> noun_or_attribute_concepts = new ArrayList<OWLObject>();
		List<String> noun_concepts_strings = new ArrayList<String>();
		List<String> noun_or_attribute_concepts_strings = new ArrayList<String>();
		List<String> attribute_concepts_strings = new ArrayList<String>();
		// loop for all elements, sort them into the right buckets
		for (OWLClassExpression classexp : exprs) {
			/* first strategy: try annotations */
			Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>();
			if (this.ontology != null) {
				/*
				 * Stream<OWLAnnotation> aaa =
				 * EntitySearcher.getAnnotationObjects( (OWLClass) classexp,
				 * this.ontology); OWLAnnotation[] aaa2 = (OWLAnnotation[])
				 * aaa.toArray(); annotations.addAll(Arrays.asList(aaa2));
				 */
				annotations.addAll(EntitySearcher.getAnnotationObjects((OWLClass) classexp, this.ontology));
			}
			String labelAnnotString = null;
			String defAnnotString = null;
			String attrAnnotString = null;
			String genericClassname = classexp.accept(sentenceOWLObjectVisit).toString();
			// System.out.println("generic---" + genericClassname + "--");
			if (genericClassname.length() > 2 && genericClassname.substring(0, 2).equals("a" + _space)) {
				genericClassname = genericClassname.substring(2, genericClassname.length());
			}
			if (genericClassname.length() > 3 && genericClassname.substring(0, 3).equals("an" + _space)) {
				genericClassname = genericClassname.substring(3, genericClassname.length());
			}
			for (OWLAnnotation annotation : annotations) {
				if (annotation.getProperty().getIRI().getFragment().equals("attributive")) {
					attrAnnotString = removeStringSuffix(annotation.getValue().toString());
				}
				if (annotation.getProperty().getIRI().getFragment().equals("definitory")) {
					defAnnotString = removeStringSuffix(annotation.getValue().toString());
				}
				if (annotation.getProperty().getIRI().getFragment().equals("label")) {
					labelAnnotString = removeStringSuffix(annotation.getValue().toString());
				}
			} // end loop for collecting annotations
			int[] types = null;
			// if (!WordNetQuery.INSTANCE.isDisabled() && (attrAnnotString == null || attrAnnotString.equals(""))
			// 		&& (attrAnnotString == null || attrAnnotString.equals(""))) {
			// 	types = WordNetQuery.INSTANCE.getTypes(genericClassname);
			// }
			/* fill buckets */
			// Attributes and Nouns
			if (attrAnnotString != null && !attrAnnotString.equals("")
					&& defAnnotString != null & !defAnnotString.equals("")) {
				noun_or_attribute_concepts.add(classexp);
				noun_or_attribute_concepts_strings.add(labelAnnotString);
				continue;
			}
			if // (isWordnetNoun && isWordnetAttribute){
			(types != null && types[0] > 0 && (types[2] > 0 || types[4] > 0)) { // adjective
																				// code
																				// is
																				// 3
																				// (index
																				// 2),
																				// adjective
																				// satellite
																				// is
																				// code
																				// is
																				// 5
				noun_or_attribute_concepts.add(classexp);
				noun_or_attribute_concepts_strings.add(genericClassname);
				continue;
			}
			// Attributes only
			if (attrAnnotString != null && !attrAnnotString.equals("")) {
				attribute_concepts.add(classexp);
				attribute_concepts_strings.add(attrAnnotString);
				continue;
			}
			// if (isWordnetAttribute){
			if (types != null && types[0] == 0 && (types[2] > 0 || types[4] > 0)) {
				attribute_concepts.add(classexp);
				attribute_concepts_strings.add(genericClassname);
				continue;
			}
			// Nouns only
			if (defAnnotString != null && !defAnnotString.equals("") && !defAnnotString.contains("false")) {
				noun_concepts.add(classexp);
				noun_concepts_strings.add(attrAnnotString);
				continue;
			}
			// if (isWordnetNoun){
			if (types != null && types[0] > 0) {
				noun_concepts.add(classexp);
				noun_concepts_strings.add(genericClassname);
				continue;
			}
			// make the default assumption that if we get here, and there is no
			// category found, it must be a noun
			noun_concepts.add(classexp);
			noun_concepts_strings.add(genericClassname);
		} // end loop for collecting expressions
			// feature notification
		if (noun_or_attribute_concepts_strings.size() > 0 || attribute_concepts_strings.size() > 0) {
			// System.out.println("setting" + exprs.toString());
			// System.out.println("setting" + exprs.get(1));
			featureAttribute = true;
		}
		/* heuristic re-arrange */
		// if no proper noun, look for gerunds
		if (noun_concepts_strings.size() == 0 && noun_or_attribute_concepts_strings.size() == 0) {
			for (OWLClassExpression classexp : exprs) {
				String genericClassname = classexp.accept(sentenceOWLObjectVisit).toString();
				if (genericClassname.indexOf("ing") > 0) {
					noun_concepts.add(classexp);
					noun_concepts_strings.add(genericClassname.toLowerCase());
				}
			}
		}
		// debug
		// System.out.println("attribute concepts " +
		// attribute_concepts_strings);
		// System.out.println("noun or attribute concept string " +
		// noun_or_attribute_concepts_strings);
		// System.out.println("noun concept string " + noun_concepts_strings);
		/* feature? */
		if ((noun_concepts_strings.size() > 1 || noun_concepts_strings.size()
				+ noun_or_attribute_concepts_strings.size() + attribute_concepts_strings.size() > 1) && exprs.size() > 1
		// exclusion
				&& !(exprs.size() == 2 && (exprs.get(0).isOWLNothing() || exprs.get(1).isOWLNothing()))) {
			// System.out.println("setting " + exprs.toString());
			// System.out.println("setting " + exprs.get(1));
			// System.out.println("setting " + exprs.get(1).isOWLNothing());
			// System.out.println(
			// 		"setting " + !(exprs.size() == 2 && (exprs.get(0).isOWLNothing() || exprs.get(1).isOWLNothing())));
			VerbalisationManager.INSTANCE.featureClassAgg = true;
		}
		/* now arrange */
		boolean firstToken = true;
		// first check if we have more than one noun
		if (noun_concepts_strings.size() > 1) {
			// first case: more than one noun
			// start with nouns, then use "that is"
			for (String str : noun_concepts_strings) {
				System.out.println("}" + str + "{");
				if (firstToken == true) {
					firstToken = false;
					str = aOrAnIfy(str);
					// System.out.println("A OR ANIFIED " + str);
				}
				result = result + str + _space + LogicLabels.getString("that_is") + _space;
				noun_or_attribute_concepts_strings.remove(str);
			}
			if (noun_concepts_strings.size() > 2)
				result = result.substring(0, result.length() - 4); // remove the
																	// last
																	// "and"
			else
				result = result.substring(0, result.length() - 8); // remove the
																	// last
																	// "that is"
			if (noun_or_attribute_concepts_strings.size() > 0 || attribute_concepts_strings.size() > 0)
				result = result + LogicLabels.getString("that_is") + _space;
			for (String str : noun_or_attribute_concepts_strings) {
				result = result + str + _space;
				attribute_concepts_strings.remove(str);
			}
			for (String str : attribute_concepts_strings) {
				result = result + str + _space;
			}
		} else {
			// second case: at most one pure noun
			// attributes first
			for (String str : attribute_concepts_strings) {
				result = result + str + _space;
				// System.out.println("result-}" + result + "{");
				noun_or_attribute_concepts_strings.remove(str);
			}
			// if several items can be used either way, order them
			float attributiveness = 0;
			while (noun_or_attribute_concepts_strings.size() > 0) {
				attributiveness = 0;
				String current = "";
				for (String str : noun_or_attribute_concepts_strings) {
					if // (!WordNetQuery.INSTANCE.isDisabled()) {
					(false) {
						 //int[] types = WordNetQuery.INSTANCE.getTypes(str);
						 float currentattr=0; // = types[2] + types[4];
						// do not consider type 4
					    if (currentattr >= attributiveness) {
							attributiveness = currentattr;
						 	current = str;
						}
					}
				}
				noun_or_attribute_concepts_strings.remove(current);
				result = result + current + _space;
				// System.out.println("result-}" + result + "{");
				noun_concepts_strings.remove(current);
			}
			for (String str : noun_concepts_strings) {
				result = result + str + _space;
			}
		}

		//
		if (VerbalisationManager.INSTANCE.featureClassAgg) {
			// System.out.println(exprs);
			// System.out.println("FEATURE CLASS AGG: " + result.toString());
			// System.out.println(exprs.toString());
		}
		if (VerbalisationManager.INSTANCE.featureAttribute) {
			// System.out.println("FEATURE ATTRIBUTE: " + result.toString());
		}

		if (result.length() > 0)
			result = result.substring(0, result.length() - 1); // need to remove
			// last spacer
		
		// System.out.println("last step: +" + result + "+++");
		return aOrAnIfy(result);
	}

	public String getPropertyNLStringPart1(OWLObjectPropertyExpression property) {
		String str = getPropertyNLString(property);
		if (str.length() < 4) {
			return "";
		}
		int i = str.indexOf("\"");
		int j = str.indexOf("[X]");
		return str.substring(i + 1, j);
	}

	public String getPropertyNLStringPart2(OWLObjectPropertyExpression property) {
		String str = getPropertyNLString(property);
		if (str.length() < 4) {
			return "";
		}
		int i = str.indexOf("[X]");
		// search for the type tag of the annotation (e.g. ^^xsd:string)
		if (str.contains("^")) {
			int j = str.indexOf("^");
			return str.substring(i + 3, j - 1);
		} else {
			if (str.substring(i + 3).contains("\"")) {
				return str.substring(i + 3, str.substring(i + 3).indexOf("\"") + i + 3);
			} else
				return str.substring(i + 3);
		}
	}
	
	public String getPropertyNLStringPart1(OWLDataPropertyExpression property) {
		String str = getPropertyNLString(property);
		if (str.length() < 4) {
			return "";
		}
		int i = str.indexOf("\"");
		int j = str.indexOf("[X]");
		return str.substring(i + 1, j);
	}

	public String getPropertyNLStringPart2(OWLDataPropertyExpression property) {
		String str = getPropertyNLString(property);
		if (str.length() < 4) {
			return "";
		}
		int i = str.indexOf("[X]");
		// search for the type tag of the annotation (e.g. ^^xsd:string)
		if (str.contains("^")) {
			int j = str.indexOf("^");
			return str.substring(i + 3, j - 1);
		} else {
			if (str.substring(i + 3).contains("\"")) {
				return str.substring(i + 3, str.substring(i + 3).indexOf("\"") + i + 3);
			} else
				return str.substring(i + 3);
		}
	}

	public static String commonPrefix(String str1, String str2) {
		int minlength = str1.length();
		if (minlength > str2.length()) {
			minlength = str2.length();
		}
		for (int i = 0; i <= minlength; i++) {
			if (!str1.substring(0, i).equals(str2.substring(0, i))) {
				return str1.substring(0, i - 1);
			}
		}
		return str1;
	}

	public static String commonSuffix(String str1, String str2) {
		String str1rev = new StringBuilder(str1).reverse().toString();
		String str2rev = new StringBuilder(str2).reverse().toString();
		String str3rev = commonPrefix(str1rev, str2rev);
		String str3 = new StringBuilder(str3rev).reverse().toString();
		return str3;
	}

	public static String strip(String str, String prefix, String suffix) {
		String newstr = new String(str);
		newstr = newstr.replace(prefix, "");
		newstr = newstr.replace(suffix, "");
		return newstr;
	}

	public static String aggregateRepeated(String str1, String str2, String connector) {
		String prefix = commonPrefix(str1, str2);
		String suffix = commonSuffix(str1, str2);
		if (suffix.length() > 0) {
			String strip1 = strip(str1, prefix, suffix);
			String strip2 = strip(str2, prefix, suffix);
			return prefix + strip1 + connector + strip2 + suffix;
		} else {
			return str1 + connector + str2;
		}
	}

	/**
	 * 
	 * @param exprs
	 *            List of expressions
	 * @return a list of groups of existential expressions with the same
	 *         property expressions
	 */
	public static List<List<OWLClassExpression>> groupMultipleExistsPatterns(List<OWLClassExpression> exprs) {
		List<List<OWLClassExpression>> buckets = new ArrayList<List<OWLClassExpression>>();
		for (OWLClassExpression ex : exprs) {
			if (ex instanceof OWLObjectSomeValuesFrom) {
				OWLObjectPropertyExpression propexpr = ((OWLObjectSomeValuesFrom) ex).getProperty();
				// check if there is already a bucket
				boolean found = false;
				for (List<OWLClassExpression> bucket : buckets) {
					if (!VerbalisationManager.INSTANCE.featuresOFF && bucket.size() > 0
							&& bucket.get(0) instanceof OWLObjectSomeValuesFrom
							&& ((OWLObjectSomeValuesFrom) bucket.get(0)).getProperty().equals(propexpr)) {
						bucket.add(ex);
						found = true;
						VerbalisationManager.INSTANCE.featureRoleAgg = true;
						break;
					}
				}
				if (found == false) {
					List<OWLClassExpression> newlist = new ArrayList<OWLClassExpression>();
					newlist.add(ex);
					buckets.add(newlist);
				}
			}
		}
		return buckets;
	}

	public String verbaliseComplexIntersection(OWLObjectIntersectionOf inter, Obfuscator obfuscator) {
		// System.out.println(" <><> complex intersection called with " + inter);
		String result = "";
		List<OWLClassExpression> simpleExpressions = new ArrayList<OWLClassExpression>();
		List<OWLClassExpression> existsExpressions = new ArrayList<OWLClassExpression>();
		List<OWLClassExpression> otherExpressions = new ArrayList<OWLClassExpression>();
		// seperate repeated exist subexpressions from the rest
		for (OWLClassExpression ex : inter.getOperandsAsList()) {
			if (ex instanceof OWLObjectSomeValuesFrom) {
				existsExpressions.add(ex);
			} else if (ex instanceof OWLClass) {
				simpleExpressions.add(ex);
			} else {
				otherExpressions.add(ex);
			}
		}
		// if (otherExpressions.size()>0)
		// System.out.println("verbaliseComplexIntersection --- CAN'T HANDLE
		// THIS! " + otherExpressions);
		// Verbalise the "easy" part
		result = LogicLabels.getString("something") + result + getSimpleIntersectionNLString(simpleExpressions) + _space;
		// Verbalise the existential part
		List<List<OWLClassExpression>> buckets = groupMultipleExistsPatterns(existsExpressions);
		for (int i = 0; i < buckets.size(); i++) {
			OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) (buckets.get(i).get(0));
			OWLObjectPropertyExpression propexpr = someexpr.getProperty();
			List<String> substrings = new ArrayList<String>();
			for (Object someobj : buckets.get(i)) {
				OWLObjectSomeValuesFrom some = (OWLObjectSomeValuesFrom) someobj;
				String somefillertext = some.getFiller().accept(sentenceOWLObjectVisit).toString();
				if (some.getFiller() instanceof OWLObjectSomeValuesFrom) {
					// System.out.println("<><> inserting 'something that' ");
					somefillertext = LogicLabels.getString("somethingThat") + somefillertext;
				}
				substrings.add(somefillertext);
			}
			result = result + LogicLabels.getString("that") + _space + verbaliseProperty(propexpr, substrings, "", obfuscator);
			if (i != buckets.size() - 1) {
				result = result + _space + LogicLabels.getString("and") + _space;
			}
		}
		return result;
	}

	public static boolean checkMultipleExistsPattern(OWLObjectIntersectionOf ints) {
		if (VerbalisationManager.INSTANCE.featuresOFF == true) {
			// System.out.println("refused to detect multiple exists pattern");
			return false;
		}
		List<OWLClassExpression> exprs = ints.getOperandsAsList();
		OWLObjectPropertyExpression commonpropexpr = null;
		for (OWLClassExpression expr : exprs) {
			if (!(expr instanceof OWLObjectSomeValuesFrom)) {
				return false;
			} else {
				OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
				OWLObjectPropertyExpression propexpr = someexpr.getProperty();
				if (commonpropexpr == null) {
					commonpropexpr = propexpr;
				}
				if (!commonpropexpr.equals(propexpr)) {
					return false;
				}
			}
		}
		return true;
	}

	public static String pseudoNLStringMultipleExistsPattern(OWLObjectIntersectionOf ints,
			Obfuscator obfuscator) {
		// System.out.println("<><> multiple exists called (1)");
		String result = "";
		List<OWLClassExpression> exprs = ints.getOperandsAsList();
		List<String> substrings = new ArrayList<String>();
		OWLObjectPropertyExpression commonpropexpr = null;
		for (OWLClassExpression expr : exprs) {
			OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
			OWLObjectPropertyExpression propexpr = someexpr.getProperty();
			substrings.add(someexpr.getFiller().accept(sentenceOWLObjectVisit).toString());
			if (commonpropexpr == null) {
				commonpropexpr = propexpr;
			}
		}
		// result = "something that " + verbaliseProperty(commonpropexpr,
		// substrings,"");
		result = verbaliseProperty(commonpropexpr, substrings, "something that" + _space, obfuscator);
		return result;
	}

	public List<TextElement> textualiseMultipleExistsPattern(OWLObjectIntersectionOf ints) {
		// System.out.println("<><> multiple exists called (2)");
		List<TextElement> result;
		List<OWLClassExpression> exprs = ints.getOperandsAsList();
		List<List<TextElement>> substrings = new ArrayList<List<TextElement>>();
		OWLObjectPropertyExpression commonpropexpr = null;
		for (OWLClassExpression expr : exprs) {
			OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
			OWLObjectPropertyExpression propexpr = someexpr.getProperty();
			substrings.add(someexpr.getFiller().accept(sentenceOWLObjectVisit).toList());
			if (commonpropexpr == null) {
				commonpropexpr = propexpr;
			}
		}
		// result = "something that " + verbaliseProperty(commonpropexpr,
		// substrings,"");
		List<TextElement> st = new ArrayList<TextElement>();
		// if (!commonpropexpr.toString().contains("[X]"))
		// 	st.add(new LogicElement("something that"));
		result = textualiseProperty(commonpropexpr, substrings, st);
		return result;
	}

	public static String pseudoNLStringMultipleExistsAndForallPattern(OWLObjectIntersectionOf ints) {
		// System.out.println("ints " + ints);
		String result = "";
		List<OWLClassExpression> exprs = SentenceOWLObjectVisitor.collectAndExpressions(ints);
		List<String> substrings = new ArrayList<String>();
		OWLObjectPropertyExpression commonpropexpr = null;
		// recursive call for subexpressions
		for (OWLClassExpression expr : exprs) {
			if (expr instanceof OWLObjectSomeValuesFrom) {
				OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
				OWLObjectPropertyExpression propexpr = someexpr.getProperty();
				String str = someexpr.getFiller().accept(sentenceOWLObjectVisit).toString();
				// System.out.println("str " + str);
				if (someexpr.getFiller() instanceof OWLObjectSomeValuesFrom) {
					String somethingstr = "something that ";
					OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) someexpr.getFiller();
					// System.out.println("DEBUG (1) -- getting domain");
					OWLClass cl = (OWLClass) VerbalisationManager.INSTANCE
							.getDomain(some1.getProperty().getNamedProperty());
					SentenceOWLObjectVisitor visitor = new SentenceOWLObjectVisitor();
					if (cl != null) {
						somethingstr = cl.accept(visitor) + " that ";
					}
					str = somethingstr + str;
				}
				substrings.add(str);
				if (commonpropexpr == null) {
					commonpropexpr = propexpr;
				}
			}

		}
		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(commonpropexpr);
		String middlepart = "";
		boolean needsep = false;
		boolean innersep = false;
		if (exprs.indexOf(exprs) < exprs.size() - 1)
			innersep = true;
		for (String str : substrings) {
			if (needsep && !innersep) {
				middlepart += "and" + _space;
			}
			if (needsep && innersep) {
				middlepart += "," + _space;
			}
			middlepart += str;
			needsep = true;
		}
		if (propstring.indexOf("[X]") < 1) {
			result += propstring + _space;
			result += middlepart;
		} else {
			String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(commonpropexpr);
			result += part1;
			result += middlepart;
			// if pattern was used, need to end the expression
			String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(commonpropexpr);
			result += part2;
		}
		result = treatCamelCaseAndUnderscores(result);
		return result + ", but nothing else";
	}

	public static TextElementSequence textualiseMultipleExistsAndForallPattern(OWLObjectIntersectionOf ints) {
		// System.out.println("ints " + ints);
		TextElementSequence result = new TextElementSequence();
		List<OWLClassExpression> exprs = SentenceOWLObjectVisitor.collectAndExpressions(ints);
		List<List<TextElement>> substrings = new ArrayList<List<TextElement>>();
		OWLObjectPropertyExpression commonpropexpr = null;
		// recursive call for subexpressions
		for (OWLClassExpression expr : exprs) {
			if (expr instanceof OWLObjectSomeValuesFrom) {
				OWLObjectSomeValuesFrom someexpr = (OWLObjectSomeValuesFrom) expr;
				OWLObjectPropertyExpression propexpr = someexpr.getProperty();
				List<TextElement> str = someexpr.getFiller().accept(sentenceOWLObjectVisit).toList();
				// System.out.println("str " + str);
				if (someexpr.getFiller() instanceof OWLObjectSomeValuesFrom) {
					LogicElement somethingst = new LogicElement("something that");
					List<TextElement> somethingstr = new ArrayList<TextElement>();
					OWLObjectSomeValuesFrom some1 = (OWLObjectSomeValuesFrom) someexpr.getFiller();
					// System.out.println("DEBUG (2) -- getting domain for " +
					// some1.getProperty().getNamedProperty());
					OWLClass cl = (OWLClass) VerbalisationManager.INSTANCE
							.getDomain(some1.getProperty().getNamedProperty());
					// System.out.println(cl);
					if (cl != null) {
						somethingstr.addAll(cl.accept(sentenceOWLObjectVisit).toList());
						somethingstr.add(new LogicElement("that"));
					} else
						somethingstr.add(somethingst);
					str.addAll(0, somethingstr);
				}
				substrings.add(str);
				if (commonpropexpr == null) {
					commonpropexpr = propexpr;
				}
			}

		}

		String propstring = VerbalisationManager.INSTANCE.getPropertyNLString(commonpropexpr);
		// List<TextElement> middlepart = new ArrayList<TextElement>();
		/**
		 * attempt to test TextSequenceList
		 */

		TextSequenceList middlepart = new TextSequenceList();
		if (exprs.indexOf(exprs) < exprs.size() - 1) {
		}
		for (List<TextElement> str : substrings) {

			TextElementSequence str_seq = new TextElementSequence(str);

			middlepart.add(str_seq);

		}

		if (propstring.indexOf("[X]") < 1) {
			result.add(new RoleElement(propstring));
			result.add(middlepart);
		} else {
			String part1 = VerbalisationManager.INSTANCE.getPropertyNLStringPart1(commonpropexpr);
			result.add(new RoleElement(part1));
			result.add(middlepart);
			// if pattern was used, need to end the expression
			String part2 = VerbalisationManager.INSTANCE.getPropertyNLStringPart2(commonpropexpr);
			result.add(new RoleElement(part2));
		}

		LogicElement le = new LogicElement("but nothing else");
		result.add(le);

		return result;
	}

	public OWLClassExpression getDomain(OWLObjectProperty prop) {
		if (ontology == null) {
			return null;
		}
		Set<OWLObjectPropertyAxiom> axioms = ontology.getAxioms(prop);
		OWLClassExpression cl = null;
		// first loop: find potential subobjectpropertyof axioms
		Set<OWLObjectProperty> props = new HashSet<OWLObjectProperty>();
		props.add(prop);
		for (OWLObjectPropertyAxiom axiom : axioms) {
			if (axiom instanceof OWLSubObjectPropertyOfAxiom) {
				OWLSubObjectPropertyOfAxiom ax = (OWLSubObjectPropertyOfAxiom) axiom;
				props.add(ax.getSuperProperty().getNamedProperty());
			}
		}
		// System.out.println("props :" + props);
		// second loop: now find also all other axioms
		for (OWLObjectProperty prop1 : props) {
			axioms.addAll(OWLAPICompatibility.getAxioms(ontology,prop1, true));
		}
		// System.out.println(axioms);
		// now actually hunt for the class expression
		for (OWLObjectPropertyAxiom axiom : axioms) {
			if (axiom instanceof OWLObjectPropertyDomainAxiom) {
				OWLObjectPropertyDomainAxiom ax = (OWLObjectPropertyDomainAxiom) axiom;
				cl = ax.getDomain();
			}
		}
		if (cl instanceof OWLObjectUnionOf) {
			cl = ((OWLObjectUnionOf) cl).getOperandsAsList().get(0);
		}
		if (cl instanceof OWLObjectIntersectionOf) {
			cl = ((OWLObjectIntersectionOf) cl).getOperandsAsList().get(0);
		}
		return cl;
	}

	private boolean detectDeterminerLabels() {
		Set<OWLAxiom> axioms = ontology.getAxioms();
		for (OWLAxiom ax : axioms) {
			if (ax instanceof OWLAnnotationAssertionAxiom) {
				OWLAnnotationAssertionAxiom annotax = (OWLAnnotationAssertionAxiom) ax;
				OWLAnnotation annot = annotax.getAnnotation();
				
				
				if (annot.getProperty().getIRI().getFragment().toString().equals("label")) {
					// System.out.println("assessing label " +
					// annot.getValue().toString());
					if (annot.getValue().toString().length() > 3
							&& annot.getValue().toString().substring(0, 4).equals("the ")
							|| annot.getValue().toString().length() > 2
									&& annot.getValue().toString().substring(0, 3).equals("an ")
							|| annot.getValue().toString().length() > 1
									&& annot.getValue().toString().substring(0, 2).equals("a ")) {
						return true;
					}
					;
				}
			}
		}
		return false;
	}

	/* Reformulation */

	public boolean detect_noun_of(String str) {
		int i = str.indexOf(" of");
		if (i > 0) {
			String prefix = str.substring(0, i);
			// if (!WordNetQuery.INSTANCE.isDisabled() && WordNetQuery.INSTANCE.isType(prefix, SynsetType.NOUN) > 0)
			//	return true;
		}
		return false;
	}

	public static String verbalizeAxiom(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory,
			OWLOntology ontology, boolean asHTML, boolean obf) {
		return verbalizeAxiom(axiom, reasoner, factory, ontology, 5000, 20000, "EL", asHTML, obf);
	}

	public static GentzenTree computeGentzenTree(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory,
			OWLOntology ontology, int maxsteps, long maxtime, String ruleset) {
		VerbalisationManager.INSTANCE.setOntology(ontology);

		ExplanationGeneratorFactory<OWLAxiom> genFac = ExplanationManager.createExplanationGeneratorFactory(factory);
		// Now create the actual explanation generator for our ontology
		ExplanationGenerator<OWLAxiom> gen = genFac.createExplanationGenerator(ontology);

		Set<OWLAxiom> explanation = new HashSet<OWLAxiom>();
		// System.out.println("checking axiom " + axiom);
		// BRANCH FOR DIFFERENT TYPES OF AXIOMS

		System.out.println("DBG: before generating explanation");
		if (axiom instanceof OWLSubClassOfAxiom || axiom instanceof OWLObjectPropertyDomainAxiom
				|| axiom instanceof OWLClassAssertionAxiom) {
			Set<Explanation<OWLAxiom>> explanations = gen.getExplanations(axiom, 1);
			if (explanations.size() > 0) {
				Object expArr[] = explanations.toArray();
				Explanation exp = (Explanation) expArr[0];
				explanation = exp.getAxioms();
			}
		}
		System.out.println("DBG: after generating explanation");

		if (explanation.size() == 0) {
			System.out.println("no justification found!");
			return null;
		}

		

		// System.out.println("Justification finding took: " + (endJustfinding -
		// startJustfinding) + "ms");

		// convert to internal format
		OWLFormula axiomFormula;
		List<OWLFormula> justificationFormulas = new ArrayList<OWLFormula>();
		try {
			System.out.println("DEBUG --- to be proven " + axiom);
			axiomFormula = ConversionManager.fromOWLAPI(axiom);

			for (OWLAxiom ax : explanation) {
				// System.out.println("DEBUG --- Trying to add " + ax);
				justificationFormulas.add(ConversionManager.fromOWLAPI(ax));

				System.out.println("VerbalisationManager: adding (1): " +
						  ax);
				System.out.println("VerbalisationManager: adding (2): " +
				  ConversionManager.fromOWLAPI(ax).prettyPrint());
			}
		} catch (Exception e) {
			return null;
		}

		GentzenTree tree;

		try { // Settings!
			tree = InferenceApplicationService.computeProofTree(axiomFormula, justificationFormulas, maxsteps, maxtime,
					ruleset);
			// 1000, 20000, "EL");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		long endTreecompute = System.currentTimeMillis();

		// System.out.println("Tree computation took: " + (endTreecompute -
		// startTreecompute) + "ms");

		return tree;
	}

	/**
	 * 
	 * @param axiom
	 *            axiom that is to be verbalised
	 * @param reasoner
	 *            respective reasoner
	 * @param factory
	 *            respective reasoner factory
	 * @param ontology
	 *            respective ontology
	 * @param maxsteps
	 *            maximum steps to be taken when evaluating the result
	 * @param maxtime
	 *            maximum time to be taken when evaluating the result
	 * @param ruleset
	 *            the ruleset to be used
	 * @param asHTML
	 *            <code>true</code> if the output string should contain HTML
	 *            tags
	 * @param obf
	 *            <code>true</code> if the output is to be obfuscated
	 * @return returns the verbalised axiom as string
	 */

	/*
	 * TODO for marvin: double check the javadoc section
	 */
	public static String verbalizeAxiom(OWLAxiom axiom, OWLReasoner reasoner, OWLReasonerFactory factory,
			OWLOntology ontology, int maxsteps, long maxtime, String ruleset, boolean asHTML, boolean obf) {

		VerbalisationManager.INSTANCE.setOntology(ontology);
		if (ontology == null) {
			return "failure! null ontology received!";
		}
		
		System.out.println("verbalizeAxiom called with axiom: " + axiom);

		GentzenTree tree = computeGentzenTree(axiom, reasoner, factory, ontology, maxsteps, maxtime, ruleset);
		if (tree == null) {
			System.out.println("EMPTY TREE!");
		}

		OWLFormula axiomFormula;
		try {
			axiomFormula = ConversionManager.fromOWLAPI(axiom);
			List<SequentInferenceRule> rules = tree.getInfRules();
			if (rules.size() == 0) {
				System.out.println("Zero rules");
			// TODO LogicLabels should be used here:
			//example:	String result = LogicLabels.getString("thatsAlreadyStated");

				
			
			String result = LogicLabels.getString("thatsAlreadyStated");
				result += VerbaliseTreeManager.makeUppercaseStart(
						VerbalisationManager.textualise(ConversionManager.toOWLAPI(axiomFormula)).toString()) + ".";
				// for (OWLFormula just : justificationFormulas){
				// result += just.toString() + "; ";
				// }
				return result;
			}

			// Obfuscation

			Obfuscator obfuscator = null;
			if (obf) {
				BlackBoxExplanation bBexplanator = new BlackBoxExplanation(ontology, factory, reasoner);
				HSTExplanationGenerator explanationGenerator = new HSTExplanationGenerator(bBexplanator);
				OWLDataFactory dataFactory = OWLAPIManagerManager.INSTANCE.getDataFactory();

				Set<OWLAxiom> explanation = new HashSet<OWLAxiom>();
				if (axiom instanceof OWLSubClassOfAxiom) {
					explanation = explanationGenerator.getExplanation(
							dataFactory.getOWLObjectIntersectionOf(((OWLSubClassOfAxiom) axiom).getSubClass(),
									((OWLSubClassOfAxiom) axiom).getSuperClass().getObjectComplementOf()));
				}

				// Fantasy animal name
				List<String> obClassnames = Arrays.asList("Leog", "Graurs", "Octaseo", "Radaps", "vladerpillar",
						"koursatee", "ksrilla", "riquito", "slounea", "ceoti", "Hexapon", "Buffa", "Barrib", "Penpe",
						"wauperine", "blumate", "wacseeling", "kloopstile", "kreebraut", "");
				List<String> obRolenames = Arrays.asList("moddles", "usterizes", "nolds", "helmbengs", "urds");

				obfuscator = new Obfuscator(explanation, obRolenames, obClassnames);
			}

			// Params
			// 1: tree
			// 2: show rule names
			// 3: HTML/text

			TextElementSequence seq = VerbaliseTreeManager.verbaliseTextElementSequence(tree, false, obfuscator);
			String result = seq.toString();
			// String result = VerbaliseTreeManager.verbaliseNL(tree, false, false, asHTML, obfuscator); // parameter
																								// for
																								// rule
																								// names!
			return result;
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return "failure";
	}

	public static TextElementSequence verbalizeAxiomAsSequence(OWLAxiom axiom, OWLReasoner reasoner,
			OWLReasonerFactory factory, OWLOntology ontology, int maxsteps, long maxtime, String ruleset,
			boolean asHTML, boolean obf) {
		System.out.println("Axiom: " + axiom);

		TextElementSequence resultSequence = new TextElementSequence();

		VerbalisationManager.INSTANCE.setOntology(ontology);

		// System.out.println("DEBUG (1)");

		/*
		 * TODO add a TextElement for Null ontology received as soon as there is
		 * an proper test or delete this if statement
		 */
		if (ontology == null) {
			// System.out.println("DEBUG (2)");
			LogicElement element = new LogicElement("Failure! Null ontology received!");
			resultSequence.add(element);
			resultSequence.add(new LinebreakElement());
			return resultSequence;
		}

		//System.out.println(ontology.getAxioms().size());
		for (OWLAxiom ax: ontology.getAxioms()){
			if (ax.toString().equals(axiom.toString())){
				// System.out.println("DEBUG (3)");
				resultSequence.concat(VerbalisationManager.textualise(axiom));
				return resultSequence;
			}
				
		}
		
		if (ontology.getAxioms().contains(axiom)) {
			// System.out.println("DEBUG (3)");	
			// LogicElement element = new LogicElement("Axiom contained.");
			// resultSequence.add(element);
			// resultSequence.add(new LinebreakElement());
			resultSequence.concat(VerbalisationManager.textualise(axiom));
			return resultSequence;
		}

		// System.out.println("DEBUG (4)");
		GentzenTree tree = computeGentzenTree(axiom, reasoner, factory, ontology, maxsteps, maxtime, ruleset);
		// System.out.println("DEBUG (5)");
		if (tree == null) {
			// System.out.println("DEBUG (6)");
			// resultSequence.add(new LogicElement("fooooo"));
			resultSequence.add(new EmptyTreeElement());
			return resultSequence;
		}

		OWLFormula axiomFormula;
		try {
			// System.out.println("DEBUG (1)");
			axiomFormula = ConversionManager.fromOWLAPI(axiom);
			List<SequentInferenceRule> rules = tree.getInfRules();
			if (rules.size() == 0) {
				// String result = "That's already stated in the ontology. ";
				// result +=
				// VerbaliseTreeManager.makeUppercaseStart(VerbalisationManager.verbalise(ConversionManager.toOWLAPI(axiomFormula)))
				// + ".";
				// for (OWLFormula just : justificationFormulas){
				// result += just.toString() + "; ";
				// }
				System.out.println("NoRuleElement");
				NoRulesElement element = new NoRulesElement(VerbaliseTreeManager
						.makeUppercaseStart(VerbalisationManager.textualise(ConversionManager.toOWLAPI(axiomFormula)).toString()));
				resultSequence.add(element);
				System.out.println(resultSequence.toString());
				return resultSequence;
			}

			// Obfuscation

			Obfuscator obfuscator = null;
			if (obf) {
				BlackBoxExplanation bBexplanator = new BlackBoxExplanation(ontology, factory, reasoner);
				HSTExplanationGenerator explanationGenerator = new HSTExplanationGenerator(bBexplanator);
				OWLDataFactory dataFactory = OWLAPIManagerManager.INSTANCE.getDataFactory();

				Set<OWLAxiom> explanation = new HashSet<OWLAxiom>();
				if (axiom instanceof OWLSubClassOfAxiom) {
					explanation = explanationGenerator.getExplanation(
							dataFactory.getOWLObjectIntersectionOf(((OWLSubClassOfAxiom) axiom).getSubClass(),
									((OWLSubClassOfAxiom) axiom).getSuperClass().getObjectComplementOf()));
				}

				// Fantasy animal name
				List<String> obClassnames = Arrays.asList("Leog", "Graurs", "Octaseo", "Radaps", "vladerpillar",
						"koursatee", "ksrilla", "riquito", "slounea", "ceoti", "Hexapon", "Buffa", "Barrib", "Penpe",
						"wauperine", "blumate", "wacseeling", "kloopstile", "kreebraut", "");
				List<String> obRolenames = Arrays.asList("moddles", "usterizes", "nolds", "helmbengs", "urds");

				obfuscator = new Obfuscator(explanation, obRolenames, obClassnames);
			}

			// Params
			// 1: tree
			// 2: show rule names
			// 3: HTML/text

			TextElementSequence textElementSequence = VerbaliseTreeManager.verbaliseTextElementSequence(tree, false,
					obfuscator);

			// String result = VerbaliseTreeManager.verbaliseNL(tree,
			// false,asHTML,obfuscator); // parameter for rule names!
			// LogicElement element = new LogicElement(result);
			// resultSequence.add(element);
			return textElementSequence;
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
			String result = e.toString();
			LogicElement element = new LogicElement(result);
			resultSequence.add(element);
			return resultSequence;
		}

	}

	public static OWLClass retrieveClassByName(String classname, OWLOntology ontology) {
		Set<OWLClass> classes = ontology.getClassesInSignature();
		OWLClass resultclass = null;
		for (OWLClass cl : classes) {
			// System.out.println(cl.toString());
			if (cl.getIRI().getFragment().equals(classname)) {
				resultclass = cl;
			}
		}
		if (resultclass == null) {
			System.out.println("Class not found in ontology: " + classname);
			return null;
		}
		;
		return resultclass;
	}

	/*
	 * public static GentzenTree computeTree(OWLAxiom axiom, String
	 * ontologyname){ // Logger rootlogger = (Logger)
	 * LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME); //
	 * rootlogger.setLevel(Level.OFF);
	 * 
	 * GentzenTree tree = null;
	 * 
	 * // load ontology OWLOntologyManager manager =
	 * OWLManager.createOWLOntologyManager(); java.io.File file = new
	 * java.io.File(ontologyname); FileDocumentSource source = new
	 * FileDocumentSource(file); OWLOntologyLoaderConfiguration loaderconfig =
	 * new OWLOntologyLoaderConfiguration();
	 * loaderconfig.setMissingImportHandlingStrategy(
	 * MissingImportHandlingStrategy.SILENT); loaderconfig =
	 * loaderconfig.setMissingImportHandlingStrategy(
	 * MissingImportHandlingStrategy.valueOf("SILENT"));
	 * 
	 * OWLOntology ontology; try { ontology =
	 * manager.loadOntologyFromOntologyDocument(source,loaderconfig);
	 * VerbalisationManager.INSTANCE.setOntology(ontology);
	 * 
	 * // construct axiom OWLDataFactory
	 * dataFactory=manager.getOWLDataFactory(); // OWLSubClassOfAxiom axiom =
	 * dataFactory.getOWLSubClassOfAxiom(subclass, superclass);
	 * 
	 * // get reasoner
	 * 
	 * 
	 * OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
	 * Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF); OWLReasoner
	 * reasoner = reasonerFactory.createReasoner(ontology);
	 * 
	 * 
	 * 
	 * tree = VerbalisationManager.computeGentzenTree(axiom, reasoner,
	 * reasonerFactory, ontology, 50000, 60000, "OP"); // } catch
	 * (OWLOntologyCreationException e) { } catch (Exception e) { //
	 * Auto-generated catch block e.printStackTrace(); return null; }
	 * 
	 * return tree; }
	 */

	public static String computeVerbalization(GentzenTree tree, boolean asHTML, boolean longtext, Obfuscator obfuscator) {
		// WordNetQuery.INSTANCE.disableDict();
		TextElementSequence textElementSequence = VerbaliseTreeManager.verbaliseTextElementSequence(tree, false,
				obfuscator);
		String result = textElementSequence.toString();
		return result;
	}
	
	public static String germanGrammarify(String input){
		System.out.println("grammarify called with " + input);
		input = input.replaceAll("mit die übliche", "mit der üblichen");
		input = input.replaceAll("mit die", "mit der");
		return input;
	}
	
	public static TextElementSequence germanGrammarify(TextElementSequence input){
		System.out.println("grammarify called with " + input);
		List<TextElement> seq = input.getTextElements();
		for (int i = 0; i+1<input.size();i++){
			TextElement e1 = seq.get(i);
			TextElement e2 = seq.get(i+1);
			if (e1.toString().equals("mit") && e2.toString().equals("die")){
				e2.setContent("der");
				if (i+2<input.size() && seq.get(i+2).toString().equals("übliche")){
					seq.get(i+2).setContent("üblichen");
				}
			}
			// System.out.println("reading element (1): " + e1);
			// System.out.println("reading element (2): " + e2);
		}
		return input;
	}

}
