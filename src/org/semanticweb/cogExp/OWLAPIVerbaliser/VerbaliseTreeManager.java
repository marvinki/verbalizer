package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenStep;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.core.Sequent;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.inferencerules.AdditionalDLRules;
import org.semanticweb.cogExp.inferencerules.INLG2012NguyenEtAlRules;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public enum VerbaliseTreeManager {
	INSTANCE;
	
	public static String verbaliseNL(GentzenTree tree, boolean withrulenames){
		List<Integer> order = tree.computePresentationOrder();
		String result = "";
		// Variables to remember previous states' information
		int previous_step = -1;
		OWLFormula previousconclusion = null;
		OWLFormula before_previousconclusion = null;
		Object printed_previousconclusion = null;
		String previoustext = "";
		boolean singletonStep = false;
		if (order.size()==1) 
			singletonStep = true;
		for(int i: order){
			// Get all information on this step
			GentzenStep step = tree.getTreesteps().get(i);
			List<Integer> premiseids = step.getPremises();
			List<Integer> axiompremiseids = step.getAxiomPremises();
			List<OWLFormula> premises = tree.idsToFormulas(premiseids);
			OWLFormula conclusion = tree.getFormulas().get(step.getConclusion());
			// System.out.println("DEBUG -- premises " + premises);
			// System.out.println("DEBUG -- conclusion " + conclusion);
			SequentInferenceRule infrule = step.getInfrule();
			//
			// Debug
			// result = result +=  " <<  " + infrule.getName() + " >> ";
			//
			// Check if rule is to be skipped
			if(infrule.equals(INLG2012NguyenEtAlRules.RULE23Repeat) && !singletonStep){
				continue; // do not even advance the conclusions
			}
			if (infrule.equals(INLG2012NguyenEtAlRules.RULE15) 
					&& premises.contains(before_previousconclusion)
					&& !singletonStep) 
					{	
				continue;
			}
			if (!singletonStep && (infrule.equals(AdditionalDLRules.ELEXISTSMINUS) 
					|| infrule.equals(AdditionalDLRules.UNIONINTRO)
					|| infrule.equals(AdditionalDLRules.DEFDOMAIN)
					|| infrule.equals(INLG2012NguyenEtAlRules.RULE2)
					|| (infrule.equals(INLG2012NguyenEtAlRules.RULE5) && previous_step!=-1)
					|| (infrule.equals(AdditionalDLRules.R0) && !singletonStep)
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
			// Debug
			// System.out.println("DEBUG");
			// System.out.println(infrule);
			// System.out.println(premiseformulas);
			// System.out.println(additions_to_antecedent);
			// System.out.println("DEBUG -- additions_to_antecedent " + additions_to_antecedent);
			String output = 
					verbaliseStatementNL(tree, infrule,
					premiseformulas,
					additions_to_antecedent,
					additions_to_succedent,prevconc,beforeprevconc);
			if (!output.equals(previoustext)) // Avoid duplicate use of equivextract, if different conclusions are extracted
			if (withrulenames)
				result = result + infrule.getName() + ">: " + output + ".\n";
			else
				result = result +  output + ".\n";
			// System.out.println(output);
			// System.out.println(previoustext);
			// Update the "previous" information
			before_previousconclusion = previousconclusion;
			previousconclusion = conclusion;
			previoustext = output;
			
		}
		return result;
	}
	
	/** outputs verbalization of one proof step as a string. TODO: implement verbalization rules as rules (with their proper interface, etc)
	 * 
	 * @param rule							employed SequentInferenceRule
	 * @param premiseformulas				list of premise formulas
	 * @param additions_to_antecedent		list of formulas added to antecedent in current step
	 * @param additions_to_succedent		list of formulas added to succedent in current step
	 * @param previousconclusion			(TODO: add description)
	 * @param before_previousconclusion		(TODO: add description)
	 * @return
	 */
	public static String verbaliseStatementNL(GentzenTree tree, SequentInferenceRule rule, 
			List<Object> premiseformulas, 
			List<Object> additions_to_antecedent,
			List<Object> additions_to_succedent,
			Object previousconclusion,
			Object before_previousconclusion
			){
			String resultstring = "";
			// Catch particular rules and use schematic output for them
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
				if (concept2.isClassExpressionLiteral()){
					definedconcept = concept2;
				} else{
					definedconcept = concept1;
				}
				OWLSubClassOfAxiom conclusion = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				String definedconceptname = VerbalisationManager.verbalise(definedconcept); 
				return  // "By definition of " + VerbalisationManager.verbalise(definedconcept) + ", "
						// + VerbalisationManager.verbalise(additions_to_antecedent.get(0));
						// "Since " + VerbalisationManager.verbalise(subclpremise) + ", by definition it is " +  VerbalisationManager.verbalise(definedconcept); 
						"Thus, " + VerbalisationManager.verbalise(conclusion.getSubClass()) + " is by definition " // " according to the definition of " 
						+ definedconceptname;
			}
			if (rule.equals(AdditionalDLRules.ONLYSOME)){
				String result = "";
				result = result + "Thus, " + VerbalisationManager.verbalise((OWLSubClassOfAxiom) additions_to_antecedent.get(0));
				return result;
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE5)){
				OWLObject addition = (OWLObject) additions_to_antecedent.get(0);
				return makeUppercaseStart(VerbalisationManager.verbalise(addition));
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
				return  // "By definition of " + VerbalisationManager.verbalise(definedconcept) + ", "
						// + VerbalisationManager.verbalise(additions_to_antecedent.get(0));
						"Since " + VerbalisationManager.verbalise(subclpremise) + ", by definition it is " +  VerbalisationManager.verbalise(definedconcept); 
						// VerbalisationManager.verbalise(additions_to_antecedent.get(0)) + " according to the definition of ";
			}
			if (rule.equals(AdditionalDLRules.EQUIVEXTRACT)){
				OWLEquivalentClassesAxiom premiseformula = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
				String firstpart = VerbalisationManager.verbalise(premiseformula);
				firstpart = makeUppercaseStart(firstpart);
				return firstpart;
				// return  firstpart + ". Thus, in particular, "
				// 		+ VerbalisationManager.verbalise(additions_to_antecedent.get(0));
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE1)){
				OWLEquivalentClassesAxiom premiseformula = (OWLEquivalentClassesAxiom) premiseformulas.get(0);
				OWLClassExpression definedconcept = premiseformula.getClassExpressionsAsList().get(0);
				if (((OWLSubClassOfAxiom) additions_to_antecedent.get(0)).getSubClass().equals(definedconcept)){
					return "According to its definition, "
							+ VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0));
				}
				return "According to the definition of " + VerbalisationManager.verbalise(definedconcept) + ", "
						+ VerbalisationManager.verbalise( (OWLObject) additions_to_antecedent.get(0));
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE23) && premiseformulas.contains(previousconclusion)){
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				return "Consequently, " + VerbalisationManager.verbalise( (OWLObject) additions_to_antecedent.get(0));
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE23) && !premiseformulas.contains(previousconclusion)){
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				return VerbalisationManager.verbalise(subcl1) + " and therefore " + VerbalisationManager.verbalise(subcl2.getSuperClass());
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE42) && 
					(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				String str = VerbalisationManager.verbalise(subcl.getSubClass());
				if (str.indexOf("a ")==0)
					str = str.substring(2);
				if (str.indexOf("an ")==0)
					str = str.substring(3);
				return ". Thus, nothing is " + VerbalisationManager.verbalise(subcl1.getSubClass());
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE42) && 
					!(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				String str = VerbalisationManager.verbalise(subcl.getSubClass());
				if (str.indexOf("a ")==0)
					str = str.substring(2);
				if (str.indexOf("an ")==0)
					str = str.substring(3);
				return "Since " + VerbalisationManager.verbalise(subcl) + ", which does not exist, nothing is " + VerbalisationManager.verbalise(subcl1.getSubClass());
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE34) && 
					(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				String str = VerbalisationManager.verbalise(subcl.getSubClass());
				if (str.indexOf("a ")==0)
					str = str.substring(2);
				if (str.indexOf("an ")==0)
					str = str.substring(3);
				return "However, no " + str 
						+ " is " +  VerbalisationManager.verbalise(subcl.getSuperClass()) + 
						". Thus, nothing is " + VerbalisationManager.verbalise(subcl.getSubClass());
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE6neo)){ // || premiseformulas.contains(before_previousconclusion))){
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) additions_to_antecedent.get(0);
				return "Since everything is " + VerbalisationManager.verbalise(subcl1.getSuperClass()) 
						+ ", " + VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); // + " Previousconclusion " + previousconclusion;
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE15) && 
					(premiseformulas.contains(previousconclusion))){ // || premiseformulas.contains(before_previousconclusion))){
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				return makeUppercaseStart(VerbalisationManager.verbalise(subcl1)) 
						+ ", thus " + VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); // + " Previousconclusion " + previousconclusion;
			}
			if (rule.equals(INLG2012NguyenEtAlRules.RULE15)){ //  && !premiseformulas.contains(previousconclusion)){
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				OWLClassExpression superExpr = ((OWLSubClassOfAxiom) subcl2).getSuperClass();
				String is = "is ";
				if (superExpr instanceof OWLObjectSomeValuesFrom)
					is = "";
				return makeUppercaseStart(VerbalisationManager.verbalise(subcl1)) 
						+ " which " + is +  VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl2).getSuperClass()) // ; 
						+ ". Therefore, " + VerbalisationManager.verbalise(((OWLSubClassOfAxiom) additions_to_antecedent.get(0)));  
			}
			if (rule.equals(AdditionalDLRules.FORALLUNION)){ 
				OWLSubClassOfAxiom subcl1 = (OWLSubClassOfAxiom) premiseformulas.get(0);
				OWLSubClassOfAxiom subcl2 = (OWLSubClassOfAxiom) premiseformulas.get(1);
				OWLSubClassOfAxiom subcl3 = (OWLSubClassOfAxiom) premiseformulas.get(2);
				String superclassstring = VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl3).getSuperClass());
				if (((OWLSubClassOfAxiom) subcl3).getSuperClass() instanceof OWLObjectSomeValuesFrom){
					superclassstring = "something that " + superclassstring;
				}
				return "Since both " + VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl1).getSubClass()) 
				                     + " and " + VerbalisationManager.verbalise(((OWLSubClassOfAxiom) subcl3).getSubClass()) + 
				                     " are " + superclassstring + 
				                     ", " + VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0));
				                     
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
				String result = makeUppercaseStart(VerbalisationManager.verbalise((OWLObject) newformula)); 
				String intString = VerbalisationManager.verbalise(superclass);
				if (superclass instanceof OWLObjectSomeValuesFrom)
					intString = "something that " + intString; 
				if (intString.length()>14){
					if (intString.substring(0,14).equals("something that")){
						intString = intString.substring(15);
					}
				}
				result += ", therefore being " + intString; 
				return result;
				}
				else{
					// we are in the case where the source concept inclusion has been presented previously, 
				    // and the target concept is new
					String result = "Given that " + VerbalisationManager.verbalise((OWLObject) newformula) + ", "; 
					result += VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); 
					return result;
					
				}
			}
			
			if (rule.equals(INLG2012NguyenEtAlRules.RULE12) && !premiseformulas.contains(previousconclusion) &&  premiseformulas.size()==2){
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) additions_to_antecedent.get(0); 
				OWLClassExpression superclass = subcl.getSuperClass();
				OWLClassExpression subclass = subcl.getSubClass();
				OWLSubClassOfAxiom prem1 = null;
				OWLSubClassOfAxiom prem2 = null;
				for (Object formula : premiseformulas){
					System.out.println(formula);
					System.out.println(subcl);
					OWLSubClassOfAxiom candidate = (OWLSubClassOfAxiom) formula;
					if (candidate.getSubClass().equals(subclass)){
						prem1 = candidate;
					}
						else prem2 = candidate;
				}
				String result = "";
				result += "Since " + VerbalisationManager.verbalise(prem1);
				result += ", which ";
				result += "is "; // TODO -- should only be there if needed!
				result += VerbalisationManager.verbalise(prem2.getSuperClass());
				result += ", "; 
				result += VerbalisationManager.verbalise((OWLObject) additions_to_antecedent.get(0)); 
				return result;
				}
			
			// Generic output
			if (!(premiseformulas==null)){
				if (premiseformulas.contains(previousconclusion) 
						&& premiseformulas.size()==2 
						&& premiseformulas.contains(before_previousconclusion)
						&& !previousconclusion.equals(before_previousconclusion)
						){
					resultstring += rule + "Thus, we have established that ";
				} else {
					if (premiseformulas.contains(previousconclusion) && premiseformulas.size()>1){
						resultstring += "Furthermore, since ";
					} else{
						if (premiseformulas.contains(previousconclusion)){
							resultstring += "Therefore "; 
						}else{
							if (premiseformulas.size()==0){
							resultstring += "";
							}
							else {
								resultstring += "Since ";
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
					if (needsep){ resultstring += " and ";};
					needsep = true;
					resultstring += VerbalisationManager.verbalise( (OWLObject) formula);
				}
			}
			
			if (additions_to_antecedent.size() + additions_to_succedent.size()>1){
				resultstring += " consider that ";
			}
			if (additions_to_antecedent.size() + additions_to_succedent.size()==1 
					&& premiseformulas.size()>0
					&& !(premiseformulas.contains(previousconclusion) 
					     && premiseformulas.size()==2 
					     && premiseformulas.contains(before_previousconclusion)
					     && !previousconclusion.equals(before_previousconclusion)
					)){
				resultstring += ", it follows that ";
			}
			if (additions_to_antecedent.size() + additions_to_succedent.size()==0){
				resultstring += "Done ";
			}
			
			boolean needorsep= false;
			for(Object ob : additions_to_antecedent){
				   if (needorsep) resultstring += " and ";
					resultstring += VerbalisationManager.verbalise((OWLObject) ob);
					needorsep = true;
			}
			
			if (additions_to_succedent.size()>0){
				resultstring += "we need to show ";
			}
			needorsep= false;
			for(Object ob : additions_to_succedent){
				   if (needorsep) resultstring += " and ";
					resultstring += VerbalisationManager.verbalise((OWLObject) ob);
					needorsep = true;
			}
		
			return resultstring;
	}
	
	public static String makeUppercaseStart(String str){
		String firstletter = str.substring(0, 1);
		String rest = str.substring(1);
		return firstletter.toUpperCase() + rest;
	}
	
}
