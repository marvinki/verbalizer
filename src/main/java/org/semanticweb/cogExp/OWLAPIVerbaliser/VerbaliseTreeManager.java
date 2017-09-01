package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenStep;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.inferencerules.AdditionalDLRules;
import org.semanticweb.cogExp.inferencerules.INLG2012NguyenEtAlRules;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;

import com.clarkparsia.owlapi.modularity.locality.SemanticLocalityEvaluator;

public enum VerbaliseTreeManager {
	INSTANCE;
	
	static boolean debug = false;
	// static boolean debug = true;
	
	
	static Locale locale = Locale.ENGLISH;
	// static Locale locale = Locale.GERMAN;
	
	
		
	static ResourceBundle LogicLabels = ResourceBundle.getBundle("LogicLabels", locale);
	
	
	public static void setLogicLabels(ResourceBundle logicLabels) {
		LogicLabels = logicLabels;
	}
	
	private static boolean laconicDefs = false;
	
	public static void setLaconicDefs(){
		laconicDefs = true;
	}

	public static String listOutput(GentzenTree tree){
		String result = "";
		List<Integer> order = tree.computePresentationOrder();
		boolean needBreak = false;
		
		
		
		for(int i: order){
			if (needBreak)
				result += "\n";
			needBreak = true;
			// Get all information on this step
			GentzenStep step = tree.getTreesteps().get(i);
			SequentInferenceRule rule = step.getInfrule();
			List<Integer> premiseids = step.getPremises();
			List<Integer> axiompremiseids = step.getAxiomPremises();
			List<OWLFormula> premises = tree.idsToFormulas(premiseids);
			OWLFormula conclusion = tree.getFormulas().get(step.getConclusion());
			result += rule.getName() + ": " +  conclusion.prettyPrint() + " from ";
			boolean needsep = false;
			for (OWLFormula prem : premises){
				if (needsep)
					result += " & ";
				result += prem.prettyPrint(); // + "  ";
				needsep = true;
			}
		}
		return result;
	}
	
	public static TextElementSequence verbaliseTextElementSequence(GentzenTree tree, boolean withrulenames, Obfuscator obfuscator){
		List<Integer> order = tree.computePresentationOrder();
		// String result = "";
		TextElementSequence resultsequence = new TextElementSequence();
		// Variables to remember previous states' information
		int previous_step = -1;
		OWLFormula previousconclusion = null;
		OWLFormula before_previousconclusion = null;
		Object printed_previousconclusion = null;
		String previoustext = "";
		List<String> previoustexts = new ArrayList<String>();
		boolean singletonStep = false;
		if (order.size()==1) 
			singletonStep = true;
		for(int i: order){
			// Get all information on this step
			GentzenStep step = tree.getTreesteps().get(i);
			List<Integer> premiseids = step.getPremises();
			
			// Debugging only!
			/*
			if(step.getInfrule().equals(INLG2012NguyenEtAlRules.RULE5)){
					System.out.println("R5 premises " + premiseids);
			} 
			*/
			
			List<Integer> axiompremiseids = step.getAxiomPremises();
			List<OWLFormula> premises = tree.idsToFormulas(premiseids);
			OWLFormula conclusion = tree.getFormulas().get(step.getConclusion());
			// System.out.println("DEBUG -- premises " + premises);
			// System.out.println("DEBUG -- conclusion " + conclusion);
			SequentInferenceRule infrule = step.getInfrule();
			//
			// Debug
			// 
			// result = result +=  " <<  " + infrule.getName() + " >> ";
			//
			// Check if rule is to be skipped
			if(infrule.equals(INLG2012NguyenEtAlRules.RULE23Repeat) && !singletonStep){
				continue; // do not even advance the conclusions
			}
			/*
			if (infrule.equals(INLG2012NguyenEtAlRules.RULE15) 
					&& premises.contains(before_previousconclusion)
					&& !singletonStep) 
					{	
				System.out.println("WE ARE SKIPPING SOMETHING (TEXTUALISATION)");
				continue;
			}
			*/
			if (!singletonStep 
				&& !VerbalisationManager.INSTANCE.featuresOFF	
					&& (infrule.equals(AdditionalDLRules.ELEXISTSMINUS) 
					|| infrule.equals(AdditionalDLRules.UNIONINTRO)
					|| infrule.equals(AdditionalDLRules.DEFDOMAIN)
					|| (infrule.equals(INLG2012NguyenEtAlRules.RULE2) && previous_step!=-1)
					// || (infrule.equals(INLG2012NguyenEtAlRules.RULE5) && previous_step!=-1)
					// || infrule.equals(INLG2012NguyenEtAlRules.RULE5)
					|| (infrule.equals(AdditionalDLRules.R0) && !singletonStep)
					|| (infrule.equals(AdditionalDLRules.TOPINTRO) && !singletonStep)
					// || infrule.equals(AdditionalDLRules.EQUIVEXTRACT)
					)){
				before_previousconclusion = previousconclusion;
				previousconclusion = conclusion;
				continue;
			}
			if (infrule.equals(AdditionalDLRules.ONLYSOME)){
				premiseids.addAll(axiompremiseids);
			}
			// now transform back from OWL Formula to OWLAPI (this should become redundant)
			List<Object> premiseformulas = new ArrayList<Object>();
			for (OWLFormula prem: premises){
				premiseformulas.add(ConversionManager.toOWLAPI(prem));
			}
			List<Object> additions_to_antecedent = new ArrayList<Object>();
			additions_to_antecedent.add(ConversionManager.toOWLAPI(conclusion));
			List<Object> additions_to_succedent = new ArrayList<Object>();
			Object prevconc = null;
			if (previousconclusion!=null)
					prevconc = ConversionManager.toOWLAPI(previousconclusion);
			Object beforeprevconc = null;
			if (before_previousconclusion!=null)
					beforeprevconc = ConversionManager.toOWLAPI(before_previousconclusion);
			
				

				TextElementSequence seq = textualiseStatementNL(tree, infrule,
						premiseformulas,
						additions_to_antecedent,
						additions_to_succedent,prevconc,beforeprevconc,obfuscator);
				seq.makeUppercaseStart();
				if (!seq.toString().equals(previoustext)
						&& ! previoustexts.contains(seq.toString())
						) // Avoid duplicate use of equivextract, if different conclusions are extracted
				{
					resultsequence.concat(seq);
					resultsequence.add(new LogicElement("."));
					resultsequence.add(new LinebreakElement());
					
				}
				// System.out.println(output);
				// System.out.println(previoustext);
				// Update the "previous" information
				before_previousconclusion = previousconclusion;
				previousconclusion = conclusion;
				previoustext =seq.toString();
				previoustexts.add(seq.toString());
				previous_step = i;
				
			}
		
		return resultsequence;
				
	}
			
	
	public static String verbaliseNL(GentzenTree tree, boolean withrulenames, boolean asHTML, Obfuscator obfuscator){
		List<Integer> order = tree.computePresentationOrder();
		String result = "";
		// Variables to remember previous states' information
		int previous_step = -1;
		
		OWLFormula previousconclusion = null;
		OWLFormula before_previousconclusion = null;
		String previoustext = "";
		List<String> previoustexts = new ArrayList<String>();
		boolean singletonStep = false;
		
		if (order.size()==1){
			singletonStep = true;
		}
		
		for(int i: order){
			// Get all information on this step
			GentzenStep step = tree.getTreesteps().get(i);
			List<Integer> premiseids = step.getPremises();
			List<Integer> axiompremiseids = step.getAxiomPremises();
			List<OWLFormula> premises = tree.idsToFormulas(premiseids);
			OWLFormula conclusion = tree.getFormulas().get(step.getConclusion());
			SequentInferenceRule infrule = step.getInfrule();
			
			if(infrule.equals(INLG2012NguyenEtAlRules.RULE23Repeat) && !singletonStep){
				continue; // do not even advance the conclusions
			}
			
			if (!singletonStep 
			&& !VerbalisationManager.INSTANCE.featuresOFF	
			&& (infrule.equals(AdditionalDLRules.ELEXISTSMINUS) 
			|| infrule.equals(AdditionalDLRules.UNIONINTRO)
			|| infrule.equals(AdditionalDLRules.DEFDOMAIN)
			|| (infrule.equals(INLG2012NguyenEtAlRules.RULE2) && previous_step!=-1)
			|| (infrule.equals(AdditionalDLRules.R0) && !singletonStep)
			|| (infrule.equals(AdditionalDLRules.TOPINTRO) && !singletonStep))){
				
				before_previousconclusion = previousconclusion;
				previousconclusion = conclusion;
				continue;
			}
			
			if (infrule.equals(AdditionalDLRules.ONLYSOME)){
				premiseids.addAll(axiompremiseids);
			}
			
			// now transform back from OWL Formula to OWLAPI (this should become redundant)
			List<Object> premiseformulas = new ArrayList<Object>();
			for (OWLFormula prem: premises){
				premiseformulas.add(ConversionManager.toOWLAPI(prem));
			}
			
			List<Object> additions_to_antecedent = new ArrayList<Object>();
			additions_to_antecedent.add(ConversionManager.toOWLAPI(conclusion));
			List<Object> additions_to_succedent = new ArrayList<Object>();
			Object prevconc = null;
			
			if (previousconclusion!=null){
				prevconc = ConversionManager.toOWLAPI(previousconclusion);
			}
			
			Object beforeprevconc = null;
			
			if (before_previousconclusion!=null){
				beforeprevconc = ConversionManager.toOWLAPI(before_previousconclusion);
			}
			
			String output;
			
			// Kölle-Version for the pre-structuring Process
			if (asHTML){
				TextElementSequence seq = textualiseStatementNL(tree, infrule,
																premiseformulas,
																additions_to_antecedent,
																additions_to_succedent,
																prevconc,beforeprevconc,
																obfuscator);
				seq.makeUppercaseStart();
				seq.pluralise(); // <--- introduce plurals
				//output = seq.toString();
				output = seq.toHTML();	
			}
			
			else{
				TextElementSequence seq = textualiseStatementNL(tree, infrule,
																premiseformulas,
																additions_to_antecedent,
																additions_to_succedent,
																prevconc,beforeprevconc,
																obfuscator);
				seq.makeUppercaseStart();
				seq.pluralise(); // <--- introduce plurals
				output = seq.toString();				
			}
				
			if (!output.equals(previoustext)
					&& ! previoustexts.contains(output)
					) // Avoid duplicate use of equivextract, if different conclusions are extracted
			{
				String linebr = "\n";
				if (asHTML)
					linebr = "<br>";

				if (withrulenames)
					result = result + infrule.getName() + ">: " + output + "." + linebr;
				else 
					result = result + output + "." + linebr;
				// result = result + "\t\t• " + output + "." + linebr; // <-- Koelle stuff


			}
			
			before_previousconclusion = previousconclusion;
			previousconclusion = conclusion;
			previoustext = output;
			previoustexts.add(output);
			previous_step = i;
			
		}
		return result;
	}
	
	/** outputs verbalization of one proof step as a string. TODO: implement verbalization rules as rules (with their proper interface, etc)
	 * 
	 * @param tree 							(TODO: add description)
	 * @param rule							employed SequentInferenceRule
	 * @param premiseformulas				list of premise formulas
	 * @param additions_to_antecedent		list of formulas added to antecedent in current step
	 * @param additions_to_succedent		list of formulas added to succedent in current step
	 * @param previousconclusion			(TODO: add description)
	 * @param before_previousconclusion		(TODO: add description)
	 * @return a Statement					(TODO: add description)
	 */
	
	public static TextElementSequence textualiseStatementNL(GentzenTree tree, SequentInferenceRule rule, 
			List<Object> premiseformulas, 
			List<Object> additions_to_antecedent,
			List<Object> additions_to_succedent,
			Object previousconclusion,
			Object before_previousconclusion,
			Obfuscator obfuscator
			){
		// if (additions_to_antecedent==null){
		// 	System.out.println("null addition to antecedent");}
		// else
		//  System.out.println("textualise called with conclusion " + additions_to_antecedent);;
			// String resultstring = "";
			// Catch particular rules and use schematic output for them
		TextElementSequence aSeq = new TextElementSequence();
		
			if(locale == Locale.ENGLISH){
				//check
				if (rule.equals(AdditionalDLRules.SUBCLANDEQUIVELIM) && premiseformulas.contains(previousconclusion)){
					OWLEquivalentClassesAxiom equivpremise;
					OWLSubClassOfAxiom subclpremise;
					if (premiseformulas.get(0) instanceof OWLEquivalentClassesAxiom){
						equivpremise = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
						subclpremise = (OWLSubClassOfAxiom) premiseformulas.get(1);
					} else{
						equivpremise = (OWLEquivalentClassesAxiom) premiseformulas.get(1);
						subclpremise = (OWLSubClassOfAxiom) premiseformulas.get(0);
					}
					OWLClassExpression concept1 = equivpremise.getClassExpressionsAsList().get(0);
					OWLClassExpression concept2 = equivpremise.getClassExpressionsAsList().get(1);
					OWLClassExpression definedconcept;
					OWLClassExpression definition;
					if (concept2.isClassExpressionLiteral()){
						definedconcept = concept2;
						definition = concept1;
					} else{
						definedconcept = concept1;
						definition = concept2;
					}
					OWLSubClassOfAxiom conclusion = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
					String definedconceptname = VerbalisationManager.textualise(definedconcept).toString(); 
					String tooltiptext = definedconcept.asOWLClass().getIRI().asLiteral().toString();
					TextElementSequence defSeq = VerbalisationManager.textualise(definition);
					TextElementSequence seq = new TextElementSequence();
					// [CONCEPTNAME] is defined as [DEFINITION]
					seq.add(new ClassElement(definedconceptname,tooltiptext));
					seq.add(new LogicElement(LogicLabels.getString("is")));
					seq.concat(defSeq);
					seq.add(new LogicElement("."));
					// Thus [SUBCLASS] is by definition [CONCEPTNAME]
					seq.add(new LogicElement(LogicLabels.getString("thus")));
					seq.concat(VerbalisationManager.textualise(conclusion.getSubClass(),obfuscator));
					seq.add(new LogicElement(LogicLabels.getString("is")));
					seq.add(new ClassElement(definedconceptname,tooltiptext));
					return seq;
					// return  // "By definition of " + VerbalisationManager.verbalise(definedconcept) + ", "
							// + VerbalisationManager.verbalise(additions_to_antecedent.get(0));
							// "Since " + VerbalisationManager.verbalise(subclpremise) + ", by definition it is " +  VerbalisationManager.verbalise(definedconcept); 
					//		"Thus, " + VerbalisationManager.verbalise(conclusion.getSubClass()) + " is by definition " // " according to the definition of " 
					//		+ definedconceptname;
				}
				
				//check
				if (rule.equals(AdditionalDLRules.ONLYSOME)){
					// get only axiom that has been added
					OWLSubClassOfAxiom addition = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("thus")));
					seq.concat(VerbalisationManager.textualise(addition,obfuscator));
					// String result = "";
					// result = result + "Thus, " + VerbalisationManager.verbalise((OWLSubClassOfAxiom) additions_to_antecedent.get(0));
					// "Thus, [SUBCLASS]
					return seq;
				}
				
				//check
				if (rule.equals(AdditionalDLRules.PROPCHAIN)){
					TextElementSequence seq = new TextElementSequence();
					
					
					seq.add(new LogicElement(LogicLabels.getString("since")));
					boolean needsep = false;
					for (Object o : premiseformulas){
						if (o instanceof OWLSubObjectPropertyOfAxiom || o instanceof OWLSubPropertyChainOfAxiom)
							continue;
						if (needsep)
							seq.add(new LogicElement(LogicLabels.getString("and")));
						needsep = true;
						OWLSubClassOfAxiom prem = (OWLSubClassOfAxiom) o;
						seq.concat(VerbalisationManager.textualise(prem,obfuscator));
					}
					OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
					seq.add(new LogicElement(","));
					seq.concat(VerbalisationManager.textualise(addition,obfuscator));
					return seq;
				}
				
				//check
				if (rule.equals(AdditionalDLRules.RULE5MULTI)){
					// case 1:
					if (!premiseformulas.contains(previousconclusion)
						&& 	!premiseformulas.contains(before_previousconclusion)){
						// Verbalize all.
						TextElementSequence seq = new TextElementSequence();
						seq.add(new LogicElement(LogicLabels.getString("since")));
						boolean needsep = false;
						for (Object premise:premiseformulas){
							OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premise;
							if (needsep)
								seq.add(new LogicElement(LogicLabels.getString("and")));
							seq.concat(VerbalisationManager.textualise(subcl,obfuscator));
						}
						seq.add(new LogicElement(","));
						OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
						seq.concat(VerbalisationManager.textualise(addition,obfuscator));
						// seq.add(new LogicElement(" [args: " + premiseformulas.size() + "]"));
					}
					else{
						if (premiseformulas.contains(previousconclusion)
								&& 	premiseformulas.contains(before_previousconclusion)){
							// System.out.println("DEBUG " + previousconclusion);
							// System.out.println("DEBUG " + before_previousconclusion);
							
							TextElementSequence seq = new TextElementSequence();
							seq.add(new LogicElement(LogicLabels.getString("therefore")));
							OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
							seq.concat(VerbalisationManager.textualise(addition,obfuscator));
							// seq.add(new LogicElement(" [args: " + premiseformulas.size() + "]"));
							return seq;
							
						} else {
							
							TextElementSequence seq = new TextElementSequence();
							seq.add(new LogicElement(LogicLabels.getString("furthermoreSince")));
							boolean needsep = false;
							for (Object premise:premiseformulas){
								OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premise;
								if (premiseformulas.contains(previousconclusion) && premise.equals(previousconclusion))
									continue;
								if (premiseformulas.contains(before_previousconclusion) && premise.equals(before_previousconclusion))
									continue;
								if (needsep)
									seq.add(new LogicElement(LogicLabels.getString("and")));
								seq.concat(VerbalisationManager.textualise(subcl,obfuscator));
							}
							seq.add(new LogicElement(","));
							OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
							seq.concat(VerbalisationManager.textualise(addition,obfuscator));
							// seq.add(new LogicElement(" [args: " + premiseformulas.size() + "]"));
							return seq;
						}
					}
				}
				
				
				if (rule.equals(INLG2012NguyenEtAlRules.RULE5)){
					// System.out.println(previousconclusion);
					// System.out.println(before_previousconclusion);
					// System.out.println(premiseformulas);
					if(premiseformulas.contains(previousconclusion)
						&& 	premiseformulas.contains(before_previousconclusion) || premiseformulas.size()==2 // this is unlike in the paper!
							){
						OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
						TextElementSequence seq = new TextElementSequence();
						seq.add(new LogicElement(LogicLabels.getString("thus")));
						seq.concat(VerbalisationManager.textualise(addition,obfuscator));
						return seq;
						//return  "Therefore, " + VerbalisationManager.verbalise(addition);
								// makeUppercaseStart(VerbalisationManager.verbalise(addition));
					} else {
						
						if (premiseformulas.contains(before_previousconclusion)){
							Object prem1 = premiseformulas.get(0);
							Object prem2 = premiseformulas.get(1);
							OWLSubClassOfAxiom prem;
							if (before_previousconclusion.equals(prem1))
								prem = (OWLSubClassOfAxiom) prem2;
							else 
								prem = (OWLSubClassOfAxiom) prem1;
							OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
							TextElementSequence seq = new TextElementSequence();
							seq.add(new LogicElement(LogicLabels.getString("furthermoreSince")));
							seq.concat(VerbalisationManager.textualise(addition,obfuscator));
							return seq;
							// return "Furthermore, since " + VerbalisationManager.verbalise(prem) + ", " + VerbalisationManager.verbalise(addition);
						}
						
					// System.out.println(premiseformulas.toString());
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("since")));
					// String result = "Since ";
					boolean needsep = false;
					for (Object prem : premiseformulas){
						if (needsep)
							seq.add(new LogicElement(LogicLabels.getString("and")));
						needsep = true;
						seq.concat(VerbalisationManager.textualise((OWLObject) prem));
						// result += VerbalisationManager.verbalise((OWLObject) prem);
					}
					OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
					seq.concat(VerbalisationManager.textualise(addition,obfuscator));
					return seq;
					// return result + ", " + VerbalisationManager.verbalise(addition);
					// return result + ", " + makeUppercaseStart(VerbalisationManager.verbalise(addition));
					}
				}
				if (rule.equals(AdditionalDLRules.SUBCLANDEQUIVELIM)){
					OWLEquivalentClassesAxiom equivpremise;
					OWLSubClassOfAxiom subclpremise;
					if (premiseformulas.get(0) instanceof OWLEquivalentClassesAxiom){
						equivpremise = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
						subclpremise = (OWLSubClassOfAxiom) premiseformulas.get(1);
					} else{
						equivpremise = (OWLEquivalentClassesAxiom) premiseformulas.get(1);
						subclpremise = (OWLSubClassOfAxiom) premiseformulas.get(0);
					}
					OWLClassExpression concept1 = equivpremise.getClassExpressionsAsList().get(0);
					OWLClassExpression concept2 = equivpremise.getClassExpressionsAsList().get(1);
					OWLClassExpression definedconcept;
					if (concept2.isClassExpressionLiteral()){
						definedconcept = concept2;
					} else{
						definedconcept = concept1;
					}
									
//					TextElementSequence seq = new TextElementSequence();
//					seq.add(new LogicElement(LogicLabels.getString("since")+"4"));
//					seq.concat(VerbalisationManager.textualise(subclpremise,obfuscator));
////					seq.add(new LogicElement("_bla_"));
//					seq.add(new LogicElement(LogicLabels.getString("byDefinitionItIs")));
//					seq.concat(VerbalisationManager.textualise(definedconcept,obfuscator));
					

					
					/*
					 * Modified
					 * */
					
					Sentence mainClause = new Sentence();
					mainClause.setSubjekt(VerbalisationManager.textualise(subclpremise,obfuscator));
					mainClause.setObjekt(new TextElement(""));
					mainClause.setPraedikat(new TextElement(""));
					mainClause.makeSinceSentence();
					
					Sentence sideClause = new Sentence();
					sideClause.setSubjekt(VerbalisationManager.textualise(definedconcept,obfuscator));
					sideClause.makebyDefinitionItIsSentence();
										
					mainClause.concat(sideClause.getSentence());
					
					return mainClause.getSentence();
				}
				
				if  (rule.equals(AdditionalDLRules.INVERSEOBJECTPROPERTY)){
					OWLInverseObjectPropertiesAxiom axInv = (OWLInverseObjectPropertiesAxiom) premiseformulas.get(0);
					OWLObjectPropertyAssertionAxiom ax = (OWLObjectPropertyAssertionAxiom) premiseformulas.get(1);
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement("inverseobjectproperty"));
					seq.concat(VerbalisationManager.textualise(ax,obfuscator));
					return seq;
				}
				if (rule.equals(AdditionalDLRules.OBJPROPASSERIONEXISTS)){
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement("opexists"));
					return seq;
				}
				if (rule.equals(AdditionalDLRules.EQUIVEXTRACT) && !laconicDefs){
					OWLEquivalentClassesAxiom premiseformula = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
					TextElementSequence firstpart = VerbalisationManager.textualise(premiseformula,obfuscator);
					firstpart.makeUppercaseStart();
					return firstpart;
					// return firstpart;
					// return  firstpart + ". Thus, in particular, "
					// 		+ VerbalisationManager.verbalise(additions_to_antecedent.get(0));
				}
				if (rule.equals(AdditionalDLRules.EQUIVEXTRACT) && laconicDefs){
					TextElementSequence firstpart = VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator);
					firstpart.makeUppercaseStart();
					return firstpart;
					// return firstpart;
					// return  firstpart + ". Thus, in particular, "
					// 		+ VerbalisationManager.verbalise(additions_to_antecedent.get(0));
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE2)){
					// System.out.println("DBG! " + premiseformulas.get(0));
					OWLSubClassOfAxiom premiseformula = (OWLSubClassOfAxiom) premiseformulas.get(0);
						// return makeUppercaseStart(VerbalisationManager.verbalise(premiseformula));
					if (previousconclusion==null && before_previousconclusion==null)
					return VerbalisationManager.textualise(premiseformula,obfuscator);
					else{
						TextElementSequence seq = new TextElementSequence();
						seq.add(new LogicElement(LogicLabels.getString("hence")));
						seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
						return seq;
					}
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE1)){
					OWLEquivalentClassesAxiom premiseformula = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
					OWLClassExpression definedconcept = premiseformula.getClassExpressionsAsList().get(0);
					if (((OWLSubClassOfAxiom) additions_to_antecedent.get(0)).getSubClass().equals(definedconcept)){
						TextElementSequence seq = new TextElementSequence();
						seq.add(new LogicElement(LogicLabels.getString("AccordingToItsDefinition")));
	//					seq.add(new LogicElement("Definfitionsgemäß ist "));
	
						seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
						return seq;
						// return "According to its definition, "
						// 		+ VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0));
					}
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("AccordingToTheDefinitionOf")));
					seq.concat(VerbalisationManager.textualise(definedconcept,obfuscator) );
					seq.add(new LogicElement(","));
					seq.concat(VerbalisationManager.textualise( (OWLObject) additions_to_antecedent.get(0)));
					return seq;
					// return "According to the definition of " + VerbalisationManager.verbalise(definedconcept) + ", "
					// 		+ VerbalisationManager.verbalise( (OWLObject) additions_to_antecedent.get(0));
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE23) && premiseformulas.contains(previousconclusion)){
					// System.out.println("PREVIOUS CONCLUSION " + previousconclusion);
					OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(1);
					OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(0);
					TextElementSequence seq = new TextElementSequence();
					// seq.add(new LogicElement("Consequently,"));
					seq.concat(VerbalisationManager.textualise( (OWLObject) additions_to_antecedent.get(0),obfuscator));
					return seq;
					// return "Consequently, " + VerbalisationManager.verbalise( (OWLObject) additions_to_antecedent.get(0));
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE23) && !premiseformulas.contains(previousconclusion)){
					OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(0);
					OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
					// System.out.println("subcl1" + VerbalisationManager.INSTANCE.prettyPrint(subcl1));
					// System.out.println("subcl2" + VerbalisationManager.INSTANCE.prettyPrint(subcl2));
					TextElementSequence seq = new TextElementSequence();
					seq.concat(VerbalisationManager.textualise(subcl1,obfuscator));
					TextElementSequence superseq = VerbalisationManager.textualise(subcl2.getSuperClass(),obfuscator);
					// System.out.println("DEBUG " + seq.getTextElements().get(seq.getTextElements().size()-1).toString());
					// System.out.println("DEBUG " +superseq.getTextElements().get(0).toString().contains("is"));
					// System.out.println("DEBUG " + seq.getTextElements().get(0).toString());
					if (seq.getTextElements().get(0).toString().contains("forearms")
							&& superseq.getTextElements().get(0).toString().contains("is")
							){
						String supStr = superseq.getTextElements().get(0).toString();
						superseq.getTextElements().remove(0);
						RoleElement sr = new RoleElement("are" + supStr.substring(2));
						superseq.getTextElements().add(0,sr);
					}
					seq.add(new LogicElement(LogicLabels.getString("andTherefore)")));
					seq.concat(superseq);
					return seq;
					// return VerbalisationManager.verbalise(subcl1) + " and therefore " + VerbalisationManager.verbalise(subcl2.getSuperClass());
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE42) && 
						(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
					OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(0);
					OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
					String str = VerbalisationManager.textualise(subcl.getSubClass()).toString();
					if (str.indexOf("a ")==0)
						str = str.substring(2);
					if (str.indexOf("an ")==0)
						str = str.substring(3);
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("ThusNothingIs")));
					seq.concat(VerbalisationManager.textualise(subcl1.getSubClass(),obfuscator));
					return seq;
					// return ". Thus, nothing is " + VerbalisationManager.verbalise(subcl1.getSubClass());
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE42) && 
						!(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
					OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(0);
					OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
					String str = VerbalisationManager.textualise(subcl.getSubClass()).toString();
					if (str.indexOf("a ")==0)
						str = str.substring(2);
					if (str.indexOf("an ")==0)
						str = str.substring(3);
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("since")));
					seq.concat(VerbalisationManager.textualise(subcl));
					seq.add(new LogicElement(LogicLabels.getString("whichDoesNotExistNothingIs")));
					seq.concat(VerbalisationManager.textualise(subcl1.getSubClass(),obfuscator));
					// ClassElement clElement = new ClassElement(str);
					return seq;
					// return "Since " + VerbalisationManager.verbalise(subcl) + ", which does not exist, nothing is " + VerbalisationManager.verbalise(subcl1.getSubClass());
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE34) && 
						(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
					OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(0);
					OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
					String str = VerbalisationManager.textualise(subcl.getSubClass()).toString();
					if (str.indexOf("a ")==0)
						str = str.substring(2);
					if (str.indexOf("an ")==0)
						str = str.substring(3);
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("howeverNo")));
					String tooltiptext = subcl.getSubClass().asOWLClass().getIRI().asLiteral().toString();
					ClassElement classElem = new ClassElement(str,tooltiptext);
					// System.out.println("DEBUG!!!! :: " + str);
					seq.add(classElem);
					seq.add(new LogicElement(LogicLabels.getString("is")));
					seq.concat(VerbalisationManager.textualise(subcl.getSuperClass(),obfuscator));
					seq.add(new LogicElement(LogicLabels.getString("thusNothingIs")));
					seq.concat(VerbalisationManager.textualise(subcl.getSubClass(),obfuscator));
					return seq;
					// return "However, no " + str 
					// 		+ " is " +  VerbalisationManager.verbalise(subcl.getSuperClass()) + 
					// 		". Thus, nothing is " + VerbalisationManager.verbalise(subcl.getSubClass());
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE35) ){ // || premiseformulas.contains(before_previousconclusion))){
					OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(1);
					// OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
					TextElementSequence seq = new TextElementSequence();
					seq.concat(VerbalisationManager.textualise(subcl.getSubClass(),obfuscator));
					seq.add(new LogicElement(LogicLabels.getString("is")));
					boolean needsep = false;
					for (Object premiseformula : premiseformulas){
						if (premiseformula instanceof OWLDisjointClassesAxiom){
							continue;
						} else{
						OWLSubClassOfAxiom ax = (OWLSubClassOfAxiom) premiseformula;
						if (needsep)
						{
							seq.add(new LogicElement(LogicLabels.getString("and")));
						}
						else {
							needsep = true;
						}
						seq.concat(VerbalisationManager.textualise(ax.getSuperClass(),obfuscator));
						}
					}
					seq.add(new LogicElement(LogicLabels.getString("butThisCanNotBeTrueAtTheSameTimeThusNothingIs")));
					seq.concat(VerbalisationManager.textualise(subcl.getSubClass(),obfuscator));
					return seq;
					// return "However, no " + str 
					// 		+ " is " +  VerbalisationManager.verbalise(subcl.getSuperClass()) + 
					// 		". Thus, nothing is " + VerbalisationManager.verbalise(subcl.getSubClass());
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE6neo)){ // || premiseformulas.contains(before_previousconclusion))){
					OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("sinceEverythingIs")));
					seq.concat(VerbalisationManager.textualise(subcl1.getSuperClass(),obfuscator));
					seq.add(new LogicElement(","));
					seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
					return seq;
					// return "Since everything is " + VerbalisationManager.verbalise(subcl1.getSuperClass()) 
					// 		+ ", " + VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); // + " Previousconclusion " + previousconclusion;
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE15) && 
						(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
					Object prem1 = premiseformulas.get(0);
					Object prem2 = premiseformulas.get(1);
					OWLSubClassOfAxiom subcl;
					if (previousconclusion.equals(prem1))
						subcl = (OWLSubClassOfAxiom) prem2;
					else	
						subcl = (OWLSubClassOfAxiom) prem1;
					TextElementSequence seq = new TextElementSequence();
					if (subcl.getSubClass().equals(subcl.getSuperClass())){ // <-- if the first axiom is tautological
						seq.add(new LogicElement(LogicLabels.getString("therefore"))); 
						seq.concat(VerbalisationManager.textualise(((OWLSubClassOfAxiom) additions_to_antecedent.get(0)),obfuscator));
					}
					else{
					seq.concat(VerbalisationManager.textualise(subcl,obfuscator));
					seq.makeUppercaseStart();
					seq.add(new LogicElement(LogicLabels.getString("_thus")));
					// System.out.println("RULE15 -- " + additions_to_antecedent.get(0));
					seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
					}
					return seq;
					// return makeUppercaseStart(VerbalisationManager.verbalise(subcl)) 
					// 		+ ", thus " + VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); // + " Previousconclusion " + previousconclusion;
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE15)){ //  && !premiseformulas.contains(previousconclusion)){
					OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(0);
					OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
					OWLClassExpression superExpr = ((OWLSubClassOfAxiom) subcl2).getSuperClass();
					String is = LogicLabels.getString("is");
					if (superExpr instanceof OWLObjectSomeValuesFrom)
						is = "";
					TextElementSequence seq = new TextElementSequence();
					// System.out.println("checking " + subcl1);
					if (subcl1.getSubClass().equals(subcl1.getSuperClass())){ // <-- if the first axiom is tautological
						seq.add(new LogicElement(LogicLabels.getString("furthermore_"))); 
						seq.concat(VerbalisationManager.textualise(((OWLSubClassOfAxiom) additions_to_antecedent.get(0)),obfuscator));
					}
					else{
					seq.concat(VerbalisationManager.textualise(subcl1,obfuscator));
					seq.makeUppercaseStart();
					seq.add(new LogicElement(LogicLabels.getString("whichIs")));
					seq.concat(VerbalisationManager.textualise(((OWLSubClassOfAxiom) subcl2).getSuperClass(),obfuscator));
					seq.add(new LogicElement("."));
					seq.add(new LogicElement(LogicLabels.getString("therefore")));
					seq.concat(VerbalisationManager.textualise(((OWLSubClassOfAxiom) additions_to_antecedent.get(0)),obfuscator));
					}
					return seq;
					// return makeUppercaseStart(VerbalisationManager.verbalise(subcl1)) 
					// 		+ " which " + is +  VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl2).getSuperClass()) // ; 
					// 		+ ". Therefore, " + VerbalisationManager.verbalise(((OWLSubClassOfAxiom) additions_to_antecedent.get(0)));  
				}
				if (rule.equals(AdditionalDLRules.FORALLUNION)){ 
					OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(0);
					OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
					OWLSubClassOfAxiom subcl3 = (OWLSubClassOfAxiom) premiseformulas.get(2);
					OWLClassExpression superclassexp = ((OWLSubClassOfAxiom) subcl3).getSuperClass();
					String superclassstring = VerbalisationManager.textualise(superclassexp).toString();
					if (((OWLSubClassOfAxiom) subcl3).getSuperClass() instanceof OWLObjectSomeValuesFrom){
						superclassstring = LogicLabels.getString("somethingThat") + superclassstring;
					}
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("sinceBoth")));
					seq.concat(VerbalisationManager.textualise(((OWLSubClassOfAxiom) subcl1).getSubClass(),obfuscator));
					seq.add(new LogicElement(LogicLabels.getString("and")));
					seq.concat(VerbalisationManager.textualise(((OWLSubClassOfAxiom) subcl3).getSubClass(),obfuscator));
					seq.add(new LogicElement(LogicLabels.getString("are")));
					String tooltiptext = superclassexp.asOWLClass().getIRI().asLiteral().toString();
					ClassElement classElem = new ClassElement(superclassstring,tooltiptext);
					seq.add(classElem);
					seq.add(new LogicElement(","));
					seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
					return seq;
					// return "Since both " + VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl1).getSubClass()) 
					//                     + " and " + VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl3).getSubClass()) + 
					//                     " are " + superclassstring + 
					//                     ", " + VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0));
					                     
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE12) && premiseformulas.contains(previousconclusion)){
					Object newformula = null;
					for (Object formula : premiseformulas){
						if (!formula.equals(previousconclusion)){
							newformula = formula;
						}
					}
					OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) additions_to_antecedent.get(0); 
					OWLClassExpression superclass = subcl.getSuperClass();
					OWLSubClassOfAxiom premiseformula = (OWLSubClassOfAxiom) newformula;
					if (!superclass.equals(premiseformula.getSuperClass())){
					// we are in the case where the "target" concept inclusion has been presented previously, 
					// and the source concept is new.
						
						
						
					TextElementSequence seq = new TextElementSequence();
					seq.concat(VerbalisationManager.textualise((OWLObject) newformula, obfuscator));
					seq.makeUppercaseStart();
					// String result = makeUppercaseStart(VerbalisationManager.verbalise((OWLObject) newformula)); 
					// System.out.println(" DEBUG left side " + result);
					String intString = VerbalisationManager.textualise(superclass).toString();
					if (intString.length()>14){
						if (intString.substring(0,14).equals("something that")){
							intString = intString.substring(15);
						}
					}
					if (superclass instanceof OWLObjectSomeValuesFrom){
						intString = LogicLabels.getString("somethingThat") + intString; 
						seq.add(new LogicElement(intString));}
					seq.add(new LogicElement(LogicLabels.getString("_thereforeBeing")));
					// result += ", therefore being " + intString; 
					String tooltiptext = superclass.asOWLClass().getIRI().asLiteral().toString();
					seq.add(new ClassElement(intString,tooltiptext));
					return seq;
					// return result;
					}
					else{
						// we are in the case where the source concept inclusion has been presented previously, 
					    // and the target concept is new
						TextElementSequence seq = new TextElementSequence();
						seq.add(new LogicElement(LogicLabels.getString("givenThat")));
						seq.concat(VerbalisationManager.textualise((OWLObject) newformula,obfuscator));
						seq.add(new LogicElement(","));
						seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
						// String result = "Given that " + VerbalisationManager.verbalise((OWLObject) newformula) + ", "; 
						// result += VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); 
						// return result;
						return seq;				
					}
				}
				if (rule.equals(AdditionalDLRules.SUBCLCHAIN)){
					TextElementSequence seq = new TextElementSequence();
					boolean firstprem = true;
					boolean innerprem = false;
					for (Object premiseObj : premiseformulas){
						OWLSubClassOfAxiom prem = (OWLSubClassOfAxiom) premiseObj;
						if (firstprem){
							seq.add(new LogicElement(LogicLabels.getString("Since")));
							seq.concat(VerbalisationManager.textualise(prem.getSubClass()));
							seq.add(new LogicElement(LogicLabels.getString("is")));
							seq.concat(VerbalisationManager.textualise(prem.getSuperClass()));
							innerprem = true;
						}
						if (!firstprem && innerprem){
							seq.add(new LogicElement("which is"));
							seq.concat(VerbalisationManager.textualise(prem.getSuperClass()));
							seq.add(new LogicElement(","));
						}
						firstprem = false;	
					}
					OWLSubClassOfAxiom conclusion =  (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
					
					// seq.add(new LogicElement(LogicLabels.getString("INDIVIDUAL")));
					ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(conclusion,obfuscator).getTextElements());
					// seq.concat(VerbalisationManager.textualise(addition,obfuscator));
					seq.add(conclusionMarker);
					return seq;
				}
				if (rule.equals(INLG2012NguyenEtAlRules.RULE12) && !premiseformulas.contains(previousconclusion) &&  premiseformulas.size()==2){
					
					System.out.println("Case Rule 12 (3)");
					OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) additions_to_antecedent.get(0); 
					OWLClassExpression superclass = subcl.getSuperClass();
					OWLClassExpression subclass = subcl.getSubClass();
					OWLSubClassOfAxiom prem1 = null;
					OWLSubClassOfAxiom prem2 = null;
					for (Object formula : premiseformulas){
						// System.out.println(formula);
						// System.out.println(subcl);
						OWLSubClassOfAxiom candidate = (OWLSubClassOfAxiom) formula;
						if (candidate.getSubClass().equals(subclass)){
							prem1 = candidate;
						}
							else prem2 = candidate;
					}
					TextElementSequence seq = new TextElementSequence();
					
					
					
					seq.add(new LogicElement(LogicLabels.getString("since")));
					seq.concat(VerbalisationManager.textualise(prem1,obfuscator));
					seq.add(new LogicElement(LogicLabels.getString("_which")));
					if (!(superclass instanceof OWLObjectSomeValuesFrom))
						seq.add(new LogicElement(LogicLabels.getString("is")));
					seq.concat(VerbalisationManager.textualise(prem2.getSuperClass(),obfuscator));
					seq.add(new LogicElement(","));
					seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
				// String result = "";
				//	result += "Since " + VerbalisationManager.verbalise(prem1);
				//	result += ", which ";
				//	if (!(superclass instanceof OWLObjectSomeValuesFrom))
				//		result += "is "; // TODO -- should only be there if needed!
				//	result += VerbalisationManager.verbalise(prem2.getSuperClass());
				//	result += ", "; 
				//	result += VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); 
				//	return result;
					return seq;
					}
				
				
				// Generic output
				TextElementSequence seq = new TextElementSequence();
				
				
				if (!(premiseformulas==null)){
					if (premiseformulas.contains(previousconclusion) 
							&& premiseformulas.size()==2 
							&& premiseformulas.contains(before_previousconclusion)
							&& !previousconclusion.equals(before_previousconclusion)
							){
						seq.add(new LogicElement(LogicLabels.getString("thus_WeHaveEstablishedThat")));
						// resultstring += rule + "Thus, we have established that ";
					} else {
						if (premiseformulas.contains(previousconclusion) && premiseformulas.size()>1){
							seq.add(new LogicElement(LogicLabels.getString("furthermore_Since")));
							// resultstring += "Furthermore, since ";
						} else{
							if (premiseformulas.contains(previousconclusion)){
								seq.add(new LogicElement(LogicLabels.getString("therefore")));
								// resultstring += "Therefore "; 
							}else{
								if (premiseformulas.size()==0){
								// resultstring += "";
								}
								else {

									// System.out.println("DEBUG --- Absolute Else case");

									// System.out.println("DEBUG --- Absolute Else case");
										

									seq.add(new LogicElement(LogicLabels.getString("since"))); 
									// resultstring += "Since ";
								};
							}
						}
					}
					boolean needsep = false;
					for (Object formula : premiseformulas){
						if (formula.equals(previousconclusion)){
							continue;
						}
						if (formula.equals(before_previousconclusion)){
							continue;
						}
						if (formula instanceof OWLSubClassOfAxiom 
								&& ((OWLSubClassOfAxiom) formula).getSuperClass().equals(((OWLSubClassOfAxiom) formula).getSubClass())){
							continue;
						}
						if (needsep){ 
							// resultstring += " and ";
							seq.add(new LogicElement(LogicLabels.getString("and"))); 
						};
						needsep = true;
						// resultstring += VerbalisationManager.verbalise( (OWLObject) formula);
						seq.concat(VerbalisationManager.textualise((OWLObject) formula));
						
					}
				}
				
				if (additions_to_antecedent.size() + additions_to_succedent.size()>1){
					seq.add(new LogicElement(LogicLabels.getString("considerThat"))); 
					// resultstring += " consider that ";
				}
				if (additions_to_antecedent.size() + additions_to_succedent.size()==1 
						&& premiseformulas.size()>0
						&& !(premiseformulas.contains(previousconclusion) 
						     && premiseformulas.size()==2 
						     && premiseformulas.contains(before_previousconclusion)
						     && !previousconclusion.equals(before_previousconclusion)
						)){
					seq.add(new LogicElement(LogicLabels.getString("itFollowsThat"))); 
					// resultstring += ", it follows that ";
				}
				if (additions_to_antecedent.size() + additions_to_succedent.size()==0){
					seq.add(new LogicElement(LogicLabels.getString("done0"))); 
					// resultstring += "Done ";
				}
				
				boolean needorsep= false;
				for(Object ob : additions_to_antecedent){
					   if (needorsep){
						   seq.add(new LogicElement(LogicLabels.getString("and"))); 
						   // resultstring += " and ";
					   }
						// resultstring += VerbalisationManager.verbalise((OWLObject) ob);
						seq.concat(VerbalisationManager.textualise((OWLObject) ob));
						needorsep = true;
				}
				
				if (additions_to_succedent.size()>0){
					seq.add(new LogicElement(LogicLabels.getString("weNeedToShow"))); 
					// resultstring += "we need to show ";
				}
				needorsep= false;
				for(Object ob : additions_to_succedent){
					   if (needorsep){ 
						   seq.add(new LogicElement(LogicLabels.getString("and"))); 
						   //resultstring += " and ";
					   }
					   seq.concat(VerbalisationManager.textualise((OWLObject) ob));
					   needorsep = true;
				}
				// TextElementSequence seq = new TextElementSequence();
				// seq.add(new LogicElement(resultstring));
				
				aSeq = seq;
		}
			
		if(locale == Locale.GERMAN){
			
			if (rule.equals(AdditionalDLRules.SUBCLANDEQUIVELIM) && premiseformulas.contains(previousconclusion)){
				OWLEquivalentClassesAxiom equivpremise;
				OWLSubClassOfAxiom subclpremise;
				if (premiseformulas.get(0) instanceof OWLEquivalentClassesAxiom){
					equivpremise = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
					subclpremise = (OWLSubClassOfAxiom) premiseformulas.get(1);
				} else{
					equivpremise = (OWLEquivalentClassesAxiom) premiseformulas.get(1);
					subclpremise = (OWLSubClassOfAxiom) premiseformulas.get(0);
				}
				OWLClassExpression concept1 = equivpremise.getClassExpressionsAsList().get(0);
				OWLClassExpression concept2 = equivpremise.getClassExpressionsAsList().get(1);
				OWLClassExpression definedconcept;
				OWLClassExpression definition;
				if (concept2.isClassExpressionLiteral()){
					definedconcept = concept2;
					definition = concept1;
				} else{
					definedconcept = concept1;
					definition = concept2;
				}
				OWLSubClassOfAxiom conclusion = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				String definedconceptname = VerbalisationManager.textualise(definedconcept).toString(); 
				String tooltiptext = definedconcept.asOWLClass().getIRI().asLiteral().toString();
				TextElementSequence defSeq = VerbalisationManager.textualise(definition);
				TextElementSequence seq = new TextElementSequence();
				// [CONCEPTNAME] is defined as [DEFINITION]
				seq.add(new ClassElement(definedconceptname,tooltiptext));
				seq.add(new LogicElement(LogicLabels.getString("is")));
				seq.concat(defSeq);
				seq.add(new LogicElement("."));
				// Thus [SUBCLASS] is by definition [CONCEPTNAME]
				seq.add(new LogicElement(LogicLabels.getString("thus")));
				
				
				Sentence sentence = new Sentence();			
				
				sentence.setSubjekt((VerbalisationManager.textualise(conclusion.getSubClass(),obfuscator).getTextElements()));
				sentence.setPraedikat(new LogicElement(LogicLabels.getString("is")));
				sentence.setObjekt(new ClassElement(definedconceptname,tooltiptext));
				
				sentence.setOrder(SentenceOrder.is_A_B);
				sentence.makeOrderedSentence();
				
//				conclusionSequence.add(new LogicElement(LogicLabels.getString("is")));
//				conclusionSequence.add(new LogicElement("..."));
//				
//				conclusionSequence.addAll(VerbalisationManager.textualise(conclusion.getSubClass(),obfuscator, SentenceOrder.is_A_B).getTextElements());
//				conclusionSequence.add(new LogicElement("..."));
//				
//				
//				conclusionSequence.add(new ClassElement(definedconceptname,tooltiptext));
//				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(conclusionSequence);
//				seq.add(conclusionMarker);
				// debug
				if(debug) seq.add(new LogicElement("-1-"));

				seq.add( sentence.toTextElementSequence());
				
				return seq;
			}
			
			if (rule.equals(AdditionalDLRules.ONLYSOME)){
				// get only axiom that has been added
				OWLSubClassOfAxiom addition = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("thus")));

				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(addition,obfuscator).getTextElements());
				// seq.concat(VerbalisationManager.textualise(addition,obfuscator));
				seq.add(conclusionMarker);
				// String result = "";
				// result = result + "Thus, " + VerbalisationManager.verbalise((OWLSubClassOfAxiom) additions_to_antecedent.get(0));
				// "Thus, [SUBCLASS]
				
				// debug
				if(debug) seq.add(new LogicElement("-2-"));
				
				return seq;
			}
			if (rule.equals(AdditionalDLRules.INDIVIDUAL)){
				OWLClassAssertionAxiom conclusion =  (OWLClassAssertionAxiom) additions_to_antecedent.get(0);
				OWLClassAssertionAxiom prem1 = null;
				OWLSubClassOfAxiom prem2 = null;
				if (premiseformulas.get(0) instanceof OWLClassAssertionAxiom){
					prem1 = (OWLClassAssertionAxiom) premiseformulas.get(0);
					prem2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				} else {
					prem1 = (OWLClassAssertionAxiom) premiseformulas.get(1);
					prem2 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				}
				System.out.println(prem1);
				System.out.println(prem2);
				// premiseformulas.get(0)
				// get only axiom that has been added
				// OWLSubClassOfAxiom addition = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				TextElementSequence seq = new TextElementSequence();
				// seq.add(new LogicElement("INDIVIDUAL"));
				seq.add(new LogicElement(LogicLabels.getString("since")));
				seq.concat(VerbalisationManager.textualise(prem1,obfuscator));
//				seq.add(new LogicElement("was2"));

				seq.add(new LogicElement(LogicLabels.getString("whichIs")));
				seq.concat(VerbalisationManager.textualise(prem2.getSuperClass(),obfuscator));
				seq.add(new LogicElement(","));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(conclusion,obfuscator).getTextElements());
				seq.add(conclusionMarker);
				// String result = "";
				// result = result + "Thus, " + VerbalisationManager.verbalise((OWLSubClassOfAxiom) additions_to_antecedent.get(0));
				// "Thus, [SUBCLASS]
				return seq;
			}
			if (rule.equals(AdditionalDLRules.ANDIVIDUAL)){
				OWLClassAssertionAxiom conclusion =  (OWLClassAssertionAxiom) additions_to_antecedent.get(0);
				// OWLClassExpression ce = conclusion.getClassExpression();	
				// get only axiom that has been added
				// OWLSubClassOfAxiom addition = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				TextElementSequence seq = new TextElementSequence();
				// seq.add(new LogicElement(LogicLabels.getString("INDIVIDUAL")));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(conclusion,obfuscator).getTextElements());
				// seq.concat(VerbalisationManager.textualise(addition,obfuscator));
				seq.add(conclusionMarker);
				// String result = "";
				// result = result + "Thus, " + VerbalisationManager.verbalise((OWLSubClassOfAxiom) additions_to_antecedent.get(0));
				// "Thus, [SUBCLASS]
				return seq;
			}
			if (rule.equals(AdditionalDLRules.SUBCLCHAIN)){
				System.out.println(" premiseformulas -- " + premiseformulas);
				OWLClassAssertionAxiom conclusion =  (OWLClassAssertionAxiom) additions_to_antecedent.get(0);
				TextElementSequence seq = new TextElementSequence();
				// seq.add(new LogicElement(LogicLabels.getString("INDIVIDUAL")));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(conclusion,obfuscator).getTextElements());
				// seq.concat(VerbalisationManager.textualise(addition,obfuscator));
				seq.add(conclusionMarker);
			}
			if (rule.equals(AdditionalDLRules.OBJPROPASSERIONASWITNESS)){
				OWLClassAssertionAxiom conclusion =  (OWLClassAssertionAxiom) additions_to_antecedent.get(0);
				OWLObjectPropertyAssertionAxiom prem1 = null;
				OWLClassAssertionAxiom prem2 = null;
				if (premiseformulas.get(0) instanceof OWLClassAssertionAxiom){
					prem2 = (OWLClassAssertionAxiom) premiseformulas.get(0);
					prem1 = (OWLObjectPropertyAssertionAxiom) premiseformulas.get(1);
				} else {
					prem2 = (OWLClassAssertionAxiom) premiseformulas.get(1);
					prem1 = (OWLObjectPropertyAssertionAxiom) premiseformulas.get(0);
				}
				System.out.println(prem1);
				System.out.println(prem2);
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("Since")));
				seq.concat(VerbalisationManager.textualise(prem1,obfuscator));
				seq.add(new LogicElement(LogicLabels.getString("and")));
				seq.concat(VerbalisationManager.textualise(prem2,obfuscator));
				seq.add(new LogicElement(","));
				// seq.add(new LogicElement(LogicLabels.getString("INDIVIDUAL")));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(conclusion,obfuscator).getTextElements());
				// seq.concat(VerbalisationManager.textualise(addition,obfuscator));
				seq.add(conclusionMarker);
				// String result = "";
				// result = result + "Thus, " + VerbalisationManager.verbalise((OWLSubClassOfAxiom) additions_to_antecedent.get(0));
				// "Thus, [SUBCLASS]
				return seq;
			}
			
			if (rule.equals(AdditionalDLRules.PROPCHAIN)){
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("since")));
				boolean needsep = false;
				for (Object o : premiseformulas){
					if (o instanceof OWLSubObjectPropertyOfAxiom || o instanceof OWLSubPropertyChainOfAxiom)
						continue;
					if (needsep)
						seq.add(new LogicElement(LogicLabels.getString("and")));
					needsep = true;
					OWLSubClassOfAxiom prem = (OWLSubClassOfAxiom) o;
					seq.concat(VerbalisationManager.textualise(prem,obfuscator));
				}
				OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
				seq.add(new LogicElement(","));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(addition,obfuscator));
				seq.add(conclusionMarker);
				// debug
				if(debug) seq.add(new LogicElement("-3-"));
				return seq;
				
			}
			
			
			if (rule.equals(AdditionalDLRules.RULE5MULTI)){
				// case 1:
				if (!premiseformulas.contains(previousconclusion)
					&& 	!premiseformulas.contains(before_previousconclusion)){
					// Verbalize all.
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("since")));
					boolean needsep = false;
					for (Object premise:premiseformulas){
						OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premise;
						if (needsep)
							seq.add(new LogicElement(LogicLabels.getString("and")));
						seq.concat(VerbalisationManager.textualise(subcl,obfuscator));
					}
					seq.add(new LogicElement(","));
					OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
					ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(addition,obfuscator));
					seq.add(conclusionMarker);
					// seq.add(new LogicElement(" [args: " + premiseformulas.size() + "]"));
				}
				else{
					if (premiseformulas.contains(previousconclusion)
							&& 	premiseformulas.contains(before_previousconclusion)){
						
						TextElementSequence seq = new TextElementSequence();
						seq.add(new LogicElement(LogicLabels.getString("therefore")));
						OWLObject addition = (OWLObject) additions_to_antecedent.get(0);

						ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(addition,obfuscator));
						seq.add(conclusionMarker);
						// seq.add(new LogicElement(" [args: " + premiseformulas.size() + "]"));
						if(debug) seq.add(new LogicElement("-4-"));
						seq.concat(VerbalisationManager.textualise(addition,obfuscator));
						
						return seq;
						
					} else {
						
						TextElementSequence seq = new TextElementSequence();
						seq.add(new LogicElement(LogicLabels.getString("furthermoreSince")));
						boolean needsep = false;
						for (Object premise:premiseformulas){
							OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premise;
							if (premiseformulas.contains(previousconclusion) && premise.equals(previousconclusion))
								continue;
							if (premiseformulas.contains(before_previousconclusion) && premise.equals(before_previousconclusion))
								continue;
							if (needsep)
								seq.add(new LogicElement(LogicLabels.getString("and")));
							seq.concat(VerbalisationManager.textualise(subcl,obfuscator));
						}
						seq.add(new LogicElement(","));
						OWLObject addition = (OWLObject) additions_to_antecedent.get(0);

						ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(addition,obfuscator));
						seq.add(conclusionMarker);
						if(debug) seq.add(new LogicElement("-5-"));
						// seq.add(new LogicElement(" [args: " + premiseformulas.size() + "]"));
						seq.concat(VerbalisationManager.textualise(addition,obfuscator));
						
						return seq;
					}
				}
			}
			
			
			//check
			if (rule.equals(INLG2012NguyenEtAlRules.RULE5)){
				
				if(premiseformulas.contains(previousconclusion)
					&& 	premiseformulas.contains(before_previousconclusion) || premiseformulas.size()==2 // this is unlike in the paper!
						){
					OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("thus")));
					seq.add(new LogicElement(LogicLabels.getString("is"))); 
					ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(addition,obfuscator));
					seq.add(conclusionMarker);
					if(debug) seq.add(new LogicElement("-6-"));
			
					
					return seq;
				
				} else {
					
					if (premiseformulas.contains(before_previousconclusion)){
						Object prem1 = premiseformulas.get(0);
						Object prem2 = premiseformulas.get(1);
						OWLSubClassOfAxiom prem;
						if (before_previousconclusion.equals(prem1))
							prem = (OWLSubClassOfAxiom) prem2;
						else 
							prem = (OWLSubClassOfAxiom) prem1;
						OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
						TextElementSequence seq = new TextElementSequence();
						seq.add(new LogicElement(LogicLabels.getString("furthermoreSince")));
						ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(addition,obfuscator));
						seq.add(conclusionMarker);
						if(debug) seq.add(new LogicElement("-7-"));
						return seq;
					}
					
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("since")));
				boolean needsep = false;
				for (Object prem : premiseformulas){
					if (needsep)
						seq.add(new LogicElement(LogicLabels.getString("and")));
					needsep = true;
					seq.concat(VerbalisationManager.textualise((OWLObject) prem));
				}
				OWLObject addition = (OWLObject) additions_to_antecedent.get(0);

				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(addition,obfuscator));
				seq.add(conclusionMarker);
				if(debug) seq.add(new LogicElement("-8-"));
				return seq;
				}
			}
			
			//check
			if (rule.equals(AdditionalDLRules.SUBCLANDEQUIVELIM)){
				OWLEquivalentClassesAxiom equivpremise;
				OWLSubClassOfAxiom subclpremise;
				if (premiseformulas.get(0) instanceof OWLEquivalentClassesAxiom){
					equivpremise = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
					subclpremise = (OWLSubClassOfAxiom) premiseformulas.get(1);
				} else{
					equivpremise = (OWLEquivalentClassesAxiom) premiseformulas.get(1);
					subclpremise = (OWLSubClassOfAxiom) premiseformulas.get(0);
				}
				OWLClassExpression concept1 = equivpremise.getClassExpressionsAsList().get(0);
				OWLClassExpression concept2 = equivpremise.getClassExpressionsAsList().get(1);
				OWLClassExpression definedconcept;
				if (concept2.isClassExpressionLiteral()){
					definedconcept = concept2;
				} else{
					definedconcept = concept1;
				}
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("since")));
				
				seq.concat(VerbalisationManager.textualise(subclpremise,obfuscator));

//				seq.add(new LogicElement("_" +subclpremise.toString()+ "_" ));
				
				seq.add(new LogicElement(LogicLabels.getString("byDefinitionItIs")));

				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(definedconcept,obfuscator));
				seq.add(conclusionMarker);
				if(debug) seq.add(new LogicElement("-9-"));
				return seq;
			}
			
			//check
			if  (rule.equals(AdditionalDLRules.INVERSEOBJECTPROPERTY)){
				OWLInverseObjectPropertiesAxiom axInv = (OWLInverseObjectPropertiesAxiom) premiseformulas.get(0);
				OWLObjectPropertyAssertionAxiom ax = (OWLObjectPropertyAssertionAxiom) premiseformulas.get(1);
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement("inverseobjectproperty"));
				seq.concat(VerbalisationManager.textualise(ax,obfuscator));
				
				if(debug) seq.add(new LogicElement("-10-"));
				return seq;
			}
			
			//check
			if (rule.equals(AdditionalDLRules.OBJPROPASSERIONEXISTS)){
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement("opexists"));
				
				if(debug) seq.add(new LogicElement("-11-"));
				return seq;
			}
			
			//check
			if (rule.equals(AdditionalDLRules.EQUIVEXTRACT)){
				OWLEquivalentClassesAxiom premiseformula = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
				TextElementSequence firstpart = VerbalisationManager.textualise(premiseformula,obfuscator);
				firstpart.makeUppercaseStart();
				
				if(debug) firstpart.add(new TextElement("_>" +premiseformula.toString()+ "_<"));
				
				if(debug) firstpart.add(new LogicElement("-12-"));
				return firstpart;
			}
			
			//check
			if (rule.equals(INLG2012NguyenEtAlRules.RULE2)){
				OWLSubClassOfAxiom premiseformula = (OWLSubClassOfAxiom) premiseformulas.get(0);
				if (previousconclusion==null && before_previousconclusion==null)
				return VerbalisationManager.textualise(premiseformula,obfuscator);
				else{
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("hence")));

					ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
					
				    seq.add(conclusionMarker);
				    if(debug) seq.add(new LogicElement("-13-"));

					return seq;
				}
			}
			
			
			if (rule.equals(INLG2012NguyenEtAlRules.RULE1)){
				OWLEquivalentClassesAxiom premiseformula = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
				OWLClassExpression definedconcept = premiseformula.getClassExpressionsAsList().get(0);
				if (((OWLSubClassOfAxiom) additions_to_antecedent.get(0)).getSubClass().equals(definedconcept)){
					TextElementSequence seq = new TextElementSequence();
					
					seq.add(new LogicElement(LogicLabels.getString("AccordingToItsDefinition")));
					seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator, SentenceOrder.is_A_B));
					
					if(debug) seq.add(new LogicElement(additions_to_antecedent.get(0).toString()));
					return seq;
				}
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("AccordingToTheDefinitionOf")));
				seq.concat(VerbalisationManager.textualise(definedconcept,obfuscator) );
				seq.add(new LogicElement(","));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise( (OWLObject) additions_to_antecedent.get(0)));
				seq.add(conclusionMarker);
				if(debug) seq.add(new LogicElement("-15-"));

				return seq;
			}
			
			
			if (rule.equals(INLG2012NguyenEtAlRules.RULE23) && premiseformulas.contains(previousconclusion)){
				// System.out.println("PREVIOUS CONCLUSION " + previousconclusion);
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				TextElementSequence seq = new TextElementSequence();
				// seq.add(new LogicElement("Consequently,"));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise( (OWLObject) additions_to_antecedent.get(0),obfuscator));
				seq.add(conclusionMarker);
				if(debug) seq.add(new LogicElement("-16-"));
				return seq;
				// return "Consequently, " + VerbalisationManager.verbalise( (OWLObject) additions_to_antecedent.get(0));
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE23) && !premiseformulas.contains(previousconclusion)){
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				// System.out.println("subcl1" + VerbalisationManager.INSTANCE.prettyPrint(subcl1));
				// System.out.println("subcl2" + VerbalisationManager.INSTANCE.prettyPrint(subcl2));
				TextElementSequence seq = new TextElementSequence();
				seq.concat(VerbalisationManager.textualise(subcl1,obfuscator));
				TextElementSequence superseq = VerbalisationManager.textualise(subcl2.getSuperClass(),obfuscator);
				// System.out.println("DEBUG " + seq.getTextElements().get(seq.getTextElements().size()-1).toString());
				// System.out.println("DEBUG " +superseq.getTextElements().get(0).toString().contains("is"));
				// System.out.println("DEBUG " + seq.getTextElements().get(0).toString());
				if (seq.getTextElements().get(0).toString().contains("forearms")
						&& superseq.getTextElements().get(0).toString().contains("is")
						){
					String supStr = superseq.getTextElements().get(0).toString();
					superseq.getTextElements().remove(0);
					RoleElement sr = new RoleElement("are" + supStr.substring(2));
					superseq.getTextElements().add(0,sr);
				}
				seq.add(new LogicElement(LogicLabels.getString("andTherefore)")));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(superseq);
				seq.add(conclusionMarker);
				if(debug) seq.add(new LogicElement("-17-"));

				return seq;
				// return VerbalisationManager.verbalise(subcl1) + " and therefore " + VerbalisationManager.verbalise(subcl2.getSuperClass());
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE42) && 
					(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				String str = VerbalisationManager.textualise(subcl.getSubClass()).toString();
				if (str.indexOf("a ")==0)
					str = str.substring(2);
				if (str.indexOf("an ")==0)
					str = str.substring(3);
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("ThusNothingIs")));
				seq.concat(VerbalisationManager.textualise(subcl1.getSubClass(),obfuscator));
				
				if(debug) seq.add(new LogicElement("-18-"));
				return seq;
				// return ". Thus, nothing is " + VerbalisationManager.verbalise(subcl1.getSubClass());
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE42) && 
					!(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				String str = VerbalisationManager.textualise(subcl.getSubClass()).toString();
				if (str.indexOf("a ")==0)
					str = str.substring(2);
				if (str.indexOf("an ")==0)
					str = str.substring(3);
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("since")));
				seq.concat(VerbalisationManager.textualise(subcl));
				seq.add(new LogicElement(LogicLabels.getString("whichDoesNotExistNothingIs")));
				seq.concat(VerbalisationManager.textualise(subcl1.getSubClass(),obfuscator));
				// ClassElement clElement = new ClassElement(str);
				
				if(debug) seq.add(new LogicElement("-19-"));
				return seq;
				// return "Since " + VerbalisationManager.verbalise(subcl) + ", which does not exist, nothing is " + VerbalisationManager.verbalise(subcl1.getSubClass());
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE34) && 
					(premiseformulas.contains(previousconclusion))){ 
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				String str = VerbalisationManager.textualise(subcl.getSubClass()).toString();
			
				if (str.indexOf("a ")==0)
					str = str.substring(2);
				if (str.indexOf("an ")==0)
					str = str.substring(3);
			
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("howeverNo")));
				String tooltiptext = subcl.getSubClass().asOWLClass().getIRI().asLiteral().toString();
				ClassElement classElem = new ClassElement(str,tooltiptext);
				seq.add(classElem);
				seq.add(new LogicElement(LogicLabels.getString("is")));
				seq.concat(VerbalisationManager.textualise(subcl.getSuperClass(),obfuscator));
				seq.add(new LogicElement(LogicLabels.getString("thusNothingIs")));
				seq.concat(VerbalisationManager.textualise(subcl.getSubClass(),obfuscator));
				
				if(debug) seq.add(new LogicElement("-20-"));
				return seq;
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE35) ){ 
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(1);
				TextElementSequence seq = new TextElementSequence();
				
				seq.concat(VerbalisationManager.textualise(subcl.getSubClass(),obfuscator));
				seq.add(new LogicElement(LogicLabels.getString("is")));
				
				boolean needsep = false;
				for (Object premiseformula : premiseformulas){
					if (premiseformula instanceof OWLDisjointClassesAxiom){
						continue;
					} else{
					OWLSubClassOfAxiom ax = (OWLSubClassOfAxiom) premiseformula;
					if (needsep)
					{
						seq.add(new LogicElement(LogicLabels.getString("and")));
					}
					else {
						needsep = true;
					}
					seq.concat(VerbalisationManager.textualise(ax.getSuperClass(),obfuscator));
					}
				}
				seq.add(new LogicElement(LogicLabels.getString("butThisCanNotBeTrueAtTheSameTimeThusNothingIs")));
				seq.concat(VerbalisationManager.textualise(subcl.getSubClass(),obfuscator));
			
				if(debug) seq.add(new LogicElement("-21-"));
				return seq;
				// return "However, no " + str 
				// 		+ " is " +  VerbalisationManager.verbalise(subcl.getSuperClass()) + 
				// 		". Thus, nothing is " + VerbalisationManager.verbalise(subcl.getSubClass());
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE6neo)){ // || premiseformulas.contains(before_previousconclusion))){
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("sinceEverythingIs")));
				seq.concat(VerbalisationManager.textualise(subcl1.getSuperClass(),obfuscator));
				seq.add(new LogicElement(","));
				seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
				
				if(debug) seq.add(new LogicElement("-22-"));
				return seq;
				// return "Since everything is " + VerbalisationManager.verbalise(subcl1.getSuperClass()) 
				// 		+ ", " + VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); // + " Previousconclusion " + previousconclusion;
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE15) && 
					(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
				Object prem1 = premiseformulas.get(0);
				Object prem2 = premiseformulas.get(1);
				OWLSubClassOfAxiom subcl;
				if (previousconclusion.equals(prem1))
					subcl = (OWLSubClassOfAxiom) prem2;
				else	
					subcl = (OWLSubClassOfAxiom) prem1;
				TextElementSequence seq = new TextElementSequence();
				if (subcl.getSubClass().equals(subcl.getSuperClass())){ // <-- if the first axiom is tautological
					seq.add(new LogicElement(LogicLabels.getString("therefore"))); 
					ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(((OWLSubClassOfAxiom) additions_to_antecedent.get(0)),obfuscator));
					seq.add(conclusionMarker);
				}
				else{
				seq.concat(VerbalisationManager.textualise(subcl,obfuscator));
				seq.makeUppercaseStart();
				seq.add(new LogicElement(LogicLabels.getString("_thus")));
				// System.out.println("RULE15 -- " + additions_to_antecedent.get(0));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
				seq.add(conclusionMarker);
				//seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
				}
				
				if(debug) seq.add(new LogicElement("-23-"));
				return seq;
				// return makeUppercaseStart(VerbalisationManager.verbalise(subcl)) 
				// 		+ ", thus " + VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); // + " Previousconclusion " + previousconclusion;
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE15)){ //  && !premiseformulas.contains(previousconclusion)){
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				OWLClassExpression superExpr = ((OWLSubClassOfAxiom) subcl2).getSuperClass();
				if (superExpr instanceof OWLObjectSomeValuesFrom) {
				}
				TextElementSequence seq = new TextElementSequence();
				// System.out.println("checking " + subcl1);
				if (subcl1.getSubClass().equals(subcl1.getSuperClass())){ // <-- if the first axiom is tautological
					seq.add(new LogicElement(LogicLabels.getString("furthermore_"))); 
					ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise(((OWLSubClassOfAxiom) additions_to_antecedent.get(0)),obfuscator));
					seq.add(conclusionMarker);
				}
				else{
				seq.concat(VerbalisationManager.textualise(subcl1,obfuscator));
				seq.makeUppercaseStart();
				seq.add(new LogicElement(LogicLabels.getString("whichIs")));
				seq.concat(VerbalisationManager.textualise(((OWLSubClassOfAxiom) subcl2).getSuperClass(),obfuscator));
				seq.add(new LogicElement("."));
				seq.add(new LogicElement(LogicLabels.getString("therefore")));
				TextElementSequence conclusionSequence = VerbalisationManager.textualise(((OWLSubClassOfAxiom) additions_to_antecedent.get(0)),obfuscator);
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(conclusionSequence);
				seq.add(conclusionMarker);
				}
				
				if(debug) seq.add(new LogicElement("-24-"));
				return seq;
				// return makeUppercaseStart(VerbalisationManager.verbalise(subcl1)) 
				// 		+ " which " + is +  VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl2).getSuperClass()) // ; 
				// 		+ ". Therefore, " + VerbalisationManager.verbalise(((OWLSubClassOfAxiom) additions_to_antecedent.get(0)));  
			}
			if (rule.equals(AdditionalDLRules.FORALLUNION)){ 
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				OWLSubClassOfAxiom subcl3 = (OWLSubClassOfAxiom) premiseformulas.get(2);
				OWLClassExpression superclassexp = ((OWLSubClassOfAxiom) subcl3).getSuperClass();
				String superclassstring = VerbalisationManager.textualise(superclassexp).toString();
				if (((OWLSubClassOfAxiom) subcl3).getSuperClass() instanceof OWLObjectSomeValuesFrom){
					superclassstring = LogicLabels.getString("somethingThat") + superclassstring;
				}
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("sinceBoth")));
				seq.concat(VerbalisationManager.textualise(((OWLSubClassOfAxiom) subcl1).getSubClass(),obfuscator));
				seq.add(new LogicElement(LogicLabels.getString("and")));
				seq.concat(VerbalisationManager.textualise(((OWLSubClassOfAxiom) subcl3).getSubClass(),obfuscator));
				seq.add(new LogicElement(LogicLabels.getString("are")));
				String tooltiptext = superclassexp.asOWLClass().getIRI().asLiteral().toString();
				ClassElement classElem = new ClassElement(superclassstring,tooltiptext);
				seq.add(classElem);
				seq.add(new LogicElement(","));
				seq.concat(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
				
				if(debug) seq.add(new LogicElement("-25-"));
				return seq;
				// return "Since both " + VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl1).getSubClass()) 
				//                     + " and " + VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl3).getSubClass()) + 
				//                     " are " + superclassstring + 
				//                     ", " + VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0));
				                     
			}
			if (rule.getName().equals(INLG2012NguyenEtAlRules.RULE12.getName()) && premiseformulas.contains(previousconclusion)){
				Object newformula = null;
				for (Object formula : premiseformulas){
					if (!formula.equals(previousconclusion)){
						newformula = formula;
					}
				}
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) additions_to_antecedent.get(0); 
				OWLClassExpression superclass = subcl.getSuperClass();
				OWLSubClassOfAxiom premiseformula = (OWLSubClassOfAxiom) newformula;
				if (!superclass.equals(premiseformula.getSuperClass())){
				// we are in the case where the "target" concept inclusion has been presented previously, 
				// and the source concept is new.
					
					
					
				TextElementSequence seq = new TextElementSequence();
				
				
				seq.concat(VerbalisationManager.textualise((OWLObject) newformula, obfuscator));
				seq.makeUppercaseStart();
				// String result = makeUppercaseStart(VerbalisationManager.verbalise((OWLObject) newformula)); 
				// System.out.println(" DEBUG left side " + result);
				String intString = VerbalisationManager.textualise(superclass).toString();
				if (intString.length()>14){
					if (intString.substring(0,14).equals("something that")){
						intString = intString.substring(15);
					}
				}
				if (superclass instanceof OWLObjectSomeValuesFrom){
					intString = LogicLabels.getString("somethingThat") + intString; 
					seq.add(new LogicElement(intString));}
				seq.add(new LogicElement(LogicLabels.getString("_thereforeBeing")));
				// result += ", therefore being " + intString; 
				String tooltiptext = superclass.asOWLClass().getIRI().asLiteral().toString();
				seq.add(new ClassElement(intString,tooltiptext));
				
				if(debug) seq.add(new LogicElement("-26-"));
				return seq;
				// return result;
				}
				else{
					// we are in the case where the source concept inclusion has been presented previously, 
				    // and the target concept is new
					TextElementSequence seq = new TextElementSequence();
					seq.add(new LogicElement(LogicLabels.getString("givenThat")));
					seq.concat(VerbalisationManager.textualise((OWLObject) newformula,obfuscator));
					seq.add(new LogicElement(","));
					ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
					seq.add(conclusionMarker);
					// String result = "Given that " + VerbalisationManager.verbalise((OWLObject) newformula) + ", "; 
					// result += VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); 
					// return result;
					
					if(debug) seq.add(new LogicElement("-27-"));
					return seq;				
				}
			}
			
			if (rule.getName().equals(INLG2012NguyenEtAlRules.RULE12.getName()) && !premiseformulas.contains(previousconclusion) &&  premiseformulas.size()==2){
				System.out.println("Case Rule 12 (3)");
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) additions_to_antecedent.get(0); 
				OWLClassExpression superclass = subcl.getSuperClass();
				OWLClassExpression subclass = subcl.getSubClass();
				OWLSubClassOfAxiom prem1 = null;
				OWLSubClassOfAxiom prem2 = null;
				for (Object formula : premiseformulas){
					// System.out.println(formula);
					// System.out.println(subcl);
					OWLSubClassOfAxiom candidate = (OWLSubClassOfAxiom) formula;
					if (candidate.getSubClass().equals(subclass)){
						prem1 = candidate;
					}
						else prem2 = candidate;
				}
				TextElementSequence seq = new TextElementSequence();
				seq.add(new LogicElement(LogicLabels.getString("since")));
				seq.concat(VerbalisationManager.textualise(prem1,obfuscator));
				seq.add(new LogicElement(LogicLabels.getString("_which")));
				
				seq.concat(VerbalisationManager.textualise(prem2.getSuperClass(),obfuscator));
				
				if (!(superclass instanceof OWLObjectSomeValuesFrom))
					seq.add(new LogicElement(LogicLabels.getString("is")));
				
				seq.add(new LogicElement(","));
				ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(VerbalisationManager.textualise((OWLObject) additions_to_antecedent.get(0),obfuscator));
				seq.add(conclusionMarker);
			// String result = "";
			//	result += "Since " + VerbalisationManager.verbalise(prem1);
			//	result += ", which ";
			//	if (!(superclass instanceof OWLObjectSomeValuesFrom))
			//		result += "is "; // TODO -- should only be there if needed!
			//	result += VerbalisationManager.verbalise(prem2.getSuperClass());
			//	result += ", "; 
			//	result += VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); 
			//	return result;
				
				if(debug) seq.add(new LogicElement("-28-"));
				return seq;
				}
			
			
			// Generic output
			TextElementSequence seq = new TextElementSequence();
			
			
			if (!(premiseformulas==null)){
				if (premiseformulas.contains(previousconclusion) 
						&& premiseformulas.size()==2 
						&& premiseformulas.contains(before_previousconclusion)
						&& !previousconclusion.equals(before_previousconclusion)
						){
					seq.add(new LogicElement(LogicLabels.getString("thus_WeHaveEstablishedThat")));
					// resultstring += rule + "Thus, we have established that ";
				} else {
					if (premiseformulas.contains(previousconclusion) && premiseformulas.size()>1){
						seq.add(new LogicElement(LogicLabels.getString("furthermore_Since")));
						// resultstring += "Furthermore, since ";
					} else{
						if (premiseformulas.contains(previousconclusion)){
							seq.add(new LogicElement(LogicLabels.getString("therefore")));
							// resultstring += "Therefore "; 
						}else{
							if (premiseformulas.size()==0){
							// resultstring += "";
							}
							else {
								// System.out.println("DEBUG --- Absolute Else case");
								seq.add(new LogicElement(LogicLabels.getString("since"))); 
								// resultstring += "Since ";
							};
						}
					}
				}
				boolean needsep = false;
				for (Object formula : premiseformulas){
					if (formula.equals(previousconclusion)){
						continue;
					}
					if (formula.equals(before_previousconclusion)){
						continue;
					}
					if (formula instanceof OWLSubClassOfAxiom 
							&& ((OWLSubClassOfAxiom) formula).getSuperClass().equals(((OWLSubClassOfAxiom) formula).getSubClass())){
						continue;
					}
					if (needsep){ 
						// resultstring += " and ";
						seq.add(new LogicElement(LogicLabels.getString("and"))); 
					};
					needsep = true;
					// resultstring += VerbalisationManager.verbalise( (OWLObject) formula);
					seq.concat(VerbalisationManager.textualise((OWLObject) formula));
					
				}
			}
			
			if (additions_to_antecedent.size() + additions_to_succedent.size()>1){
				seq.add(new LogicElement(LogicLabels.getString("considerThat"))); 
				// resultstring += " consider that ";
			}
			if (additions_to_antecedent.size() + additions_to_succedent.size()==1 
					&& premiseformulas.size()>0
					&& !(premiseformulas.contains(previousconclusion) 
					     && premiseformulas.size()==2 
					     && premiseformulas.contains(before_previousconclusion)
					     && !previousconclusion.equals(before_previousconclusion)
					)){
				seq.add(new LogicElement(LogicLabels.getString("itFollowsThat"))); 
				// resultstring += ", it follows that ";
			}
			if (additions_to_antecedent.size() + additions_to_succedent.size()==0){
				seq.add(new LogicElement(LogicLabels.getString("done0"))); 
				// resultstring += "Done ";
			}
			
			boolean needorsep= false;
			
			List<TextElement> textElementsConclusion = new ArrayList<TextElement>();
			
			
			for(Object ob : additions_to_antecedent){
				   if (needorsep){
					   textElementsConclusion.add(new LogicElement(LogicLabels.getString("and"))); 
					   // resultstring += " and ";
				   }
					// resultstring += VerbalisationManager.verbalise((OWLObject) ob);
					textElementsConclusion.addAll(VerbalisationManager.textualise((OWLObject) ob).getTextElements());
					needorsep = true;
			}
			
			ConclusionMarkerElement conclusionMarker = new ConclusionMarkerElement(textElementsConclusion);
			
			// System.out.println("DEBUG --  " + conclusionMarker);
			seq.add(conclusionMarker);
			
			if (additions_to_succedent.size()>0){
				seq.add(new LogicElement(LogicLabels.getString("weNeedToShow"))); 
				// resultstring += "we need to show ";
			}
			needorsep= false;
			for(Object ob : additions_to_succedent){
				   if (needorsep){ 
					   seq.add(new LogicElement(LogicLabels.getString("and"))); 
					   //resultstring += " and ";
				   }
				   seq.concat(VerbalisationManager.textualise((OWLObject) ob));
				   needorsep = true;
			}
			// TextElementSequence seq = new TextElementSequence();
			// seq.add(new LogicElement(resultstring));
			if(debug) seq.add(new LogicElement("-29-"));
			aSeq = seq;
		}
		
			return aSeq;
	
			
	}
	
	public static void setLocale(Locale alocale) {
		VerbaliseTreeManager.locale = alocale;
	}


	public static String makeUppercaseStart(String str){
		if (str.length()<1)
			return str;
		String firstletter = str.substring(0, 1);
		String rest = str.substring(1);
		return firstletter.toUpperCase() + rest;
	}
	
}
