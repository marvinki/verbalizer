package org.semanticweb.cogExp.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.semanticweb.cogExp.inferencerules.AdditionalDLRules;
import org.semanticweb.cogExp.inferencerules.INLG2012NguyenEtAlRules;
import org.semanticweb.cogExp.inferencerules.SequentTerminationAxiom;


public enum RuleSetManager {
	INSTANCE;
	private HashMap<java.lang.String,List<SequentInferenceRule>> rulesets = new HashMap<java.lang.String,List<SequentInferenceRule>>(); 
	
	private RuleSetManager(){
		// generate ALC ruleset
		
		/*
		ArrayList<SequentInferenceRule> alcrules = new ArrayList<SequentInferenceRule>();
		alcrules.add(SequentTerminationAxiom.INSTANCE); //
		alcrules.add(BoxRightSequentInferenceRule.INSTANCE);
	    alcrules.add(DiamondLeftSequentInferenceRule.INSTANCE); //
	    alcrules.add(DoubleNegLeftSequentInferenceRule.INSTANCE);
	    alcrules.add(DoubleNegRightSequentInferenceRule.INSTANCE);
	    alcrules.add(IntersectionLeftSequentInferenceRule.INSTANCE);
	    alcrules.add(IntersectionRightSequentInferenceRule.INSTANCE);
	    alcrules.add(NegBoxLeftSequentInferenceRule.INSTANCE);
	    alcrules.add(NegDiamondRightSequentInferenceRule.INSTANCE);
	    alcrules.add(NegIntersectionLeftSequentInferenceRule.INSTANCE);
	    alcrules.add(NegIntersectionRightSequentInferenceRule.INSTANCE);	    
	    alcrules.add(NegUnionLeftSequentInferenceRule.INSTANCE);
	    alcrules.add(UnionRightSequentInferenceRule.INSTANCE);
	    alcrules.add(UnionLeftSequentInferenceRule.INSTANCE);
	    alcrules.add(DisjointLeftSequentInferenceRule.INSTANCE); //
	    alcrules.add(DisjointRightSequentInferenceRule.INSTANCE);
	    alcrules.add(SubClassRightSequentInferenceRule.INSTANCE); //
	    alcrules.add(SubClassLeftSequentInferenceRule.INSTANCE); //
	    alcrules.add(DomainLeftSequentInferenceRule.INSTANCE); //
	    alcrules.add(DomainRightSequentInferenceRule.INSTANCE);
	    rulesets.put("ALC",alcrules);
	    */
	    
	    INLG2012NguyenEtAlRules[] inlg2012rulesInit = {
	    		INLG2012NguyenEtAlRules.RULE1,
	    		INLG2012NguyenEtAlRules.RULE2,
	    		// INLG2012NguyenEtAlRules.RULE3,
	    		// INLG2012NguyenEtAlRules.RULE4,
	    		INLG2012NguyenEtAlRules.RULE5,
	    		/*
	    		INLG2012NguyenEtAlRules.RULE6,
	    		INLG2012NguyenEtAlRules.RULE7,
	    		INLG2012NguyenEtAlRules.RULE8,
	    		INLG2012NguyenEtAlRules.RULE9,
	    		INLG2012NguyenEtAlRules.RULE10,
	    		INLG2012NguyenEtAlRules.RULE11,
	    		*/
	    		INLG2012NguyenEtAlRules.RULE12,
	    		/*
	    		INLG2012NguyenEtAlRules.RULE13,
	    		INLG2012NguyenEtAlRules.RULE14,
	    		*/
	    		INLG2012NguyenEtAlRules.RULE15,
	    		/*
	    		INLG2012NguyenEtAlRules.RULE16,
	    		INLG2012NguyenEtAlRules.RULE17,
	    		INLG2012NguyenEtAlRules.RULE18,
	    		INLG2012NguyenEtAlRules.RULE19,
	    		INLG2012NguyenEtAlRules.RULE20,
	    		INLG2012NguyenEtAlRules.RULE21,
	    		INLG2012NguyenEtAlRules.RULE22,
	    		*/
	    		INLG2012NguyenEtAlRules.RULE23,
	    		/*
	    		INLG2012NguyenEtAlRules.RULE24,
	    		INLG2012NguyenEtAlRules.RULE25,
	    		INLG2012NguyenEtAlRules.RULE26,
	    		INLG2012NguyenEtAlRules.RULE27,
	    		INLG2012NguyenEtAlRules.RULE28,
	    		INLG2012NguyenEtAlRules.RULE29,
	    		INLG2012NguyenEtAlRules.RULE30,
	    		INLG2012NguyenEtAlRules.RULE31,
	    		INLG2012NguyenEtAlRules.RULE32,
	    		INLG2012NguyenEtAlRules.RULE33,
	    		*/
	    		INLG2012NguyenEtAlRules.RULE34,
	    		INLG2012NguyenEtAlRules.RULE34nary,
	    		INLG2012NguyenEtAlRules.RULE35nary,
	    		// INLG2012NguyenEtAlRules.RULE35,
	    		// INLG2012NguyenEtAlRules.RULE36,
	    		INLG2012NguyenEtAlRules.RULE37,
	    		/*
	    		INLG2012NguyenEtAlRules.RULE38,
	    		INLG2012NguyenEtAlRules.RULE39,
	    		INLG2012NguyenEtAlRules.RULE40,
	    		INLG2012NguyenEtAlRules.RULE41,
	    		INLG2012NguyenEtAlRules.RULE42,
	    		INLG2012NguyenEtAlRules.RULE43,
	    		INLG2012NguyenEtAlRules.RULE44,
	    		INLG2012NguyenEtAlRules.RULE45,
	    		INLG2012NguyenEtAlRules.RULE46,
	    		INLG2012NguyenEtAlRules.RULE47,
	    		INLG2012NguyenEtAlRules.RULE48,
	    		*/
	    		// INLG2012NguyenEtAlRules.RULE49,
	    		// INLG2012NguyenEtAlRules.RULE50,
	    		// INLG2012NguyenEtAlRules.RULE51,
	    		// INLG2012NguyenEtAlRules.RULE52,
	    };
	    
	    ArrayList<SequentInferenceRule> inlg2012rules = new ArrayList<SequentInferenceRule>(Arrays.asList(inlg2012rulesInit));
	    rulesets.put("INLG2012NguyenEtAl",inlg2012rules);
	    
	    // AdditionalDLRules[] additionalInit = {
	    		// AdditionalDLRules.TOPCLASSCASES,
	    		// AdditionalDLRules.FORALLWEAKENING};
	    
	   //  ArrayList<SequentInferenceRule> additionalRules = new ArrayList<SequentInferenceRule>(Arrays.asList(additionalInit));
	   //  rulesets.put("AdditionalDLRules",additionalRules);
	    
	    SequentInferenceRule[] testedRulesInit = {
	    		// AdditionalDLRules.EQUIVINTROLEFT,           
	    		// AdditionalDLRules.COMB,
	    		// AdditionalDLRules.DEFDOMAIN,
	    		INLG2012NguyenEtAlRules.RULE1,				// forward eliminiation of equiv into one subset expression (with each application)
	    		// INLG2012NguyenEtAlRules.RULE4,      		// disjunct in subclass can be discarded
	    		INLG2012NguyenEtAlRules.RULE12,             // transitivity of subset
	    		// INLG2012NguyenEtAlRules.RULE17,             // case distinction (involving domain) for concluding that class is universal
	    		// AdditionalDLRules.DISJOINTWITHTOPSYNONYM,
	    		// AdditionalDLRules.SWAPSUBSUMPTIONOFNEGCONCEPTLEFT,
	    		SequentTerminationAxiom.INSTANCE
	    };
	    
	    ArrayList<SequentInferenceRule> testedRules = new ArrayList<SequentInferenceRule>(Arrays.asList(testedRulesInit));
	    rulesets.put("TestedRules",testedRules);
	    
	    /*
	    SequentInferenceRule[] elRulesInit = {
	    		AdditionalDLRules.TOPISONTOPRIGHT,
	    		AdditionalDLRules.REFLEXIVITYOFSUBCONCEPTRIGHT,
	    		INLG2012NguyenEtAlRules.RULE2,
	    		INLG2012NguyenEtAlRules.RULE12,
	    	    INLG2012NguyenEtAlRules.RULE5,
	    		INLG2012NguyenEtAlRules.RULE15,
	    		AdditionalDLRules.TOPISONTOPLEFT,
	    		SequentTerminationAxiom.INSTANCE
	    };
	    */
	    
	    SequentInferenceRule[] elRulesInit = {
	    		// AdditionalDLRules.TOPISONTOPRIGHT,
	    		// AdditionalDLRules.REFLEXIVITYOFSUBCONCEPTRIGHT,
	    		INLG2012NguyenEtAlRules.RULE1,
	    		INLG2012NguyenEtAlRules.RULE2,
	    		INLG2012NguyenEtAlRules.RULE12,
	    		INLG2012NguyenEtAlRules.RULE15,
	    		INLG2012NguyenEtAlRules.RULE5,
	    		// AdditionalDLRules.TOPISONTOPLEFT,
	    		SequentTerminationAxiom.INSTANCE
	    };
	    
	    ArrayList<SequentInferenceRule> elRules = new ArrayList<SequentInferenceRule>(Arrays.asList(elRulesInit));
	    rulesets.put("ELRules",elRules);
	    
	    SequentInferenceRule[] ELInit = {
	    		AdditionalDLRules.TOPINTRO, // introduce fact that every expression is subsumed by top
	    		AdditionalDLRules.SIMPLETERMINATION,
	    		SequentTerminationAxiom.INSTANCE,
    			AdditionalDLRules.EQUIVEXTRACT,
    			INLG2012NguyenEtAlRules.RULE1neo,
    			INLG2012NguyenEtAlRules.RULE1,
    			AdditionalDLRules.SUBCLANDEQUIVELIM,
    			INLG2012NguyenEtAlRules.RULE12new,
    			// AdditionalDLRules.FORALLUNION,
    			// INLG2012NguyenEtAlRules.RULE23Repeat,
    			// INLG2012NguyenEtAlRules.RULE23,
    			INLG2012NguyenEtAlRules.RULE2,
    			INLG2012NguyenEtAlRules.RULE3,
    			// // // allInferenceRules.add(AdditionalDLRules.UNIONINTRO);
    			AdditionalDLRules.RULE5MULTI,
    			// INLG2012NguyenEtAlRules.RULE5new,  
    			INLG2012NguyenEtAlRules.RULE6neo,
    			AdditionalDLRules.ELEXISTSMINUS,
    			INLG2012NguyenEtAlRules.RULE34, // handle disjointness
    			INLG2012NguyenEtAlRules.RULE34nary,
    			INLG2012NguyenEtAlRules.RULE35, // handle disjointness
    			INLG2012NguyenEtAlRules.RULE35nary,
    			AdditionalDLRules.APPLRANGE,
    			AdditionalDLRules.BOTINTRO, // introduce fact that every expression subsumes bot (needed to show that unsatisfiable concepts subsume each other)
    			// AdditionalDLRules.R0, // introduce trivial subsumptions
    			AdditionalDLRules.DEFDOMAIN, // translate domain definition 
    			INLG2012NguyenEtAlRules.RULE37, // handle subpropertyof
    			INLG2012NguyenEtAlRules.RULE42,
    			INLG2012NguyenEtAlRules.RULE23Repeat, // transitivity
    			INLG2012NguyenEtAlRules.RULE23, // transitivity
    			INLG2012NguyenEtAlRules.RULE15,
    			AdditionalDLRules.PROPCHAIN,
    			AdditionalDLRules.INDIVIDUAL,
    			AdditionalDLRules.INVERSEOBJECTPROPERTY,
    			AdditionalDLRules.OBJPROPASSERIONEXISTS,
    			AdditionalDLRules.INDIVTOPINTRO
    			
	    };
	    
	 
	    
	    
	//     allInferenceRules.add(SequentTerminationAxiom.INSTANCE);
		// allInferenceRules.add(AdditionalDLRules.ONLYSOME);
	//	 allInferenceRules.add(AdditionalDLRules.EQUIVEXTRACT);
	//	 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE1);
	//	 allInferenceRules.add(AdditionalDLRules.SUBCLANDEQUIVELIM);
	//	 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE12);
	//	 allInferenceRules.add(AdditionalDLRules.FORALLUNION);
	//	 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE23Repeat);
		 
	//	 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE23);
	//	 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE2);
	//	 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE15);
	//	 allInferenceRules.add(AdditionalDLRules.RULE5MULTI);
	//	 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE5);
	//	 allInferenceRules.add(AdditionalDLRules.ELEXISTSMINUS);
	//	 allInferenceRules.add(AdditionalDLRules.FORALLMINUS);
	//	 allInferenceRules.add(AdditionalDLRules.UNIONINTRO);
	    
	    
	    ArrayList<SequentInferenceRule> EL = new ArrayList<SequentInferenceRule>(Arrays.asList(ELInit));
	    rulesets.put("EL",EL);
	    
	    
	   /*
	    AdditionalDLRules.SIMPLETERMINATION,
		SequentTerminationAxiom.INSTANCE,
		AdditionalDLRules.EQUIVEXTRACT,
		INLG2012NguyenEtAlRules.RULE1neo,
		INLG2012NguyenEtAlRules.RULE1,
		AdditionalDLRules.SUBCLANDEQUIVELIM,
		INLG2012NguyenEtAlRules.RULE12new,
		INLG2012NguyenEtAlRules.RULE2,
		INLG2012NguyenEtAlRules.RULE3,
		AdditionalDLRules.RULE5MULTI,
		INLG2012NguyenEtAlRules.RULE5new,  
		INLG2012NguyenEtAlRules.RULE6neo,
		AdditionalDLRules.ELEXISTSMINUS,
		INLG2012NguyenEtAlRules.RULE34, // handle disjointness
		AdditionalDLRules.APPLRANGE,
		AdditionalDLRules.TOPINTRO, // introduce fact that every expression is subsumed by top
		AdditionalDLRules.BOTINTRO, // introduce fact that every expression subsumes bot (needed to show that unsatisfiable concepts subsume each other)
		AdditionalDLRules.R0, // introduce trivial subsumptions
		AdditionalDLRules.DEFDOMAIN, // translate domain definition 
		INLG2012NguyenEtAlRules.RULE37, // handle subpropertyof
		INLG2012NguyenEtAlRules.RULE42,
		INLG2012NguyenEtAlRules.RULE23Repeat, // transitivity
		INLG2012NguyenEtAlRules.RULE23, // transitivity
		INLG2012NguyenEtAlRules.RULE15,
		AdditionalDLRules.PROPCHAIN
		*/
	    
	    
	    SequentInferenceRule[] ELInitNonredundant = {
	    		AdditionalDLRules.TOPINTRO, // introduce fact that every expression is subsumed by top
	    		AdditionalDLRules.SIMPLETERMINATION,
	    		SequentTerminationAxiom.INSTANCE,
    			// AdditionalDLRules.EQUIVEXTRACT,   // <--- should be off
    			INLG2012NguyenEtAlRules.RULE1neo,
    			INLG2012NguyenEtAlRules.RULE1,
    			// AdditionalDLRules.SUBCLANDEQUIVELIM, // REDUNDANT
    			INLG2012NguyenEtAlRules.RULE12new,
    			// AdditionalDLRules.FORALLUNION,
    			INLG2012NguyenEtAlRules.RULE2,
    			INLG2012NguyenEtAlRules.RULE3,
    			// // // allInferenceRules.add(AdditionalDLRules.UNIONINTRO);
    			// AdditionalDLRules.RULE5BIN,
    			INLG2012NguyenEtAlRules.RULE5new,
    			INLG2012NguyenEtAlRules.RULE6neo,
    			AdditionalDLRules.ELEXISTSMINUS,
    			INLG2012NguyenEtAlRules.RULE34, // handle disjointness
    			INLG2012NguyenEtAlRules.RULE34nary,
    			INLG2012NguyenEtAlRules.RULE35, // handle disjointness
    			INLG2012NguyenEtAlRules.RULE35nary,
    			AdditionalDLRules.BOTINTRO, // introduce fact that every expression subsumes bot (needed to show that unsatisfiable concepts subsume each other)
    			// AdditionalDLRules.R0, // introduce trivial subsumptions  // <--- off?
    		    AdditionalDLRules.DEFDOMAIN, // translate domain definition 
    			INLG2012NguyenEtAlRules.RULE37, // handle subpropertyof
    			INLG2012NguyenEtAlRules.RULE42,
    			INLG2012NguyenEtAlRules.RULE23Repeat,
    			INLG2012NguyenEtAlRules.RULE23,
    			INLG2012NguyenEtAlRules.RULE15,
    			AdditionalDLRules.PROPCHAIN,
    			AdditionalDLRules.APPLRANGE,  // <---- extra
    			AdditionalDLRules.INDIVIDUAL,
    			AdditionalDLRules.INVERSEOBJECTPROPERTY,
    			AdditionalDLRules.OBJPROPASSERIONEXISTS,
    			AdditionalDLRules.INDIVTOPINTRO
	    };    
	    
	    ArrayList<SequentInferenceRule> ELnonredundant = new ArrayList<SequentInferenceRule>(Arrays.asList(ELInitNonredundant));
	    rulesets.put("ELnonredundant",ELnonredundant);
	    
	
	
	
	SequentInferenceRule[] ontopandInit = {
    		AdditionalDLRules.SIMPLETERMINATION,
    		SequentTerminationAxiom.INSTANCE,
			AdditionalDLRules.EQUIVEXTRACT,
			INLG2012NguyenEtAlRules.RULE1neo,
			INLG2012NguyenEtAlRules.RULE1,
			AdditionalDLRules.SUBCLANDEQUIVELIM,
			INLG2012NguyenEtAlRules.RULE12new,
			AdditionalDLRules.FORALLUNION,
			INLG2012NguyenEtAlRules.RULE23Repeat,
			INLG2012NguyenEtAlRules.RULE23,
			INLG2012NguyenEtAlRules.RULE2,
			INLG2012NguyenEtAlRules.RULE15,
			// // // allInferenceRules.add(AdditionalDLRules.UNIONINTRO);
			AdditionalDLRules.RULE5MULTI,
			INLG2012NguyenEtAlRules.RULE5,
			INLG2012NguyenEtAlRules.RULE6neo,
			AdditionalDLRules.ELEXISTSMINUS,
			INLG2012NguyenEtAlRules.RULE34, // handle disjointness
			INLG2012NguyenEtAlRules.RULE34nary,
			INLG2012NguyenEtAlRules.RULE35nary,
			AdditionalDLRules.TOPINTRO, // introduce fact that every expression is subsumed by top
			AdditionalDLRules.BOTINTRO, // introduce fact that every expression subsumes bot (needed to show that unsatisfiable concepts subsume each other)
			AdditionalDLRules.R0, // introduce trivial subsumptions
			AdditionalDLRules.DEFDOMAIN, // translate domain definition 
			INLG2012NguyenEtAlRules.RULE37, // handle subpropertyof
			INLG2012NguyenEtAlRules.RULE42,
			AdditionalDLRules.ONLYSOME,
            AdditionalDLRules.UNIONINTRO, // <--- is slow
            AdditionalDLRules.SUBCLCHAIN,
            // Rules for dealing with the A-Box/Individuals
			AdditionalDLRules.INDIVIDUAL,
			AdditionalDLRules.INVERSEOBJECTPROPERTY,
			AdditionalDLRules.OBJPROPASSERIONEXISTS,
			AdditionalDLRules.OBJPROPASSERIONASWITNESS,
			AdditionalDLRules.ANDIVIDUAL,
			AdditionalDLRules.INDIVTOPINTRO,
			AdditionalDLRules.ONEOFINTRO
    };
    
    ArrayList<SequentInferenceRule> ontopand = new ArrayList<SequentInferenceRule>(Arrays.asList(ontopandInit));
    rulesets.put("OP",ontopand);
    
	} //////////
	
	
	
	
	   public static boolean isVerbalisedELRule(SequentInferenceRule rule){
	    	if (
	    	rule.equals(AdditionalDLRules.EQUIVEXTRACT) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE1neo) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE1) ||
	    	rule.equals(AdditionalDLRules.SUBCLANDEQUIVELIM) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE12new) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE12) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE5new) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE5) ||
	    	rule.equals(AdditionalDLRules.RULE5MULTI) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE15) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE6neo) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE34) || // handle disjointness
	    	rule.equals(INLG2012NguyenEtAlRules.RULE34nary) || // handle disjointness
	    	rule.equals(INLG2012NguyenEtAlRules.RULE35) || // handle disjointness
	    	rule.equals(INLG2012NguyenEtAlRules.RULE35nary) || // handle disjointness
			// AdditionalDLRules.TOPINTRO, // introduce fact that every expression is subsumed by top
			// AdditionalDLRules.BOTINTRO, // introduce fact that every expression subsumes bot (needed to show that unsatisfiable concepts subsume each other)
			// AdditionalDLRules.R0, // introduce trivial subsumptions
			// AdditionalDLRules.DEFDOMAIN, // translate domain definition 
	    	rule.equals(INLG2012NguyenEtAlRules.RULE37) || // || // handle subpropertyof
	    	// rule.equals(INLG2012NguyenEtAlRules.RULE42))
	    	rule.equals(AdditionalDLRules.PROPCHAIN) ||
	    	rule.equals(AdditionalDLRules.APPLRANGE) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE23Repeat) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE23) ||
	    	rule.equals(INLG2012NguyenEtAlRules.RULE42) ||
	    	rule.equals(AdditionalDLRules.SUBCLCHAIN) ||
            // Rules for dealing with the A-Box/Individuals
	    	rule.equals(AdditionalDLRules.INDIVIDUAL) ||
	    	rule.equals(AdditionalDLRules.INVERSEOBJECTPROPERTY) ||
	    	rule.equals(AdditionalDLRules.OBJPROPASSERIONEXISTS) ||
	    	rule.equals(AdditionalDLRules.OBJPROPASSERIONASWITNESS) ||
	    	rule.equals(AdditionalDLRules.ANDIVIDUAL) ||
	    	rule.equals(AdditionalDLRules.INDIVTOPINTRO) ||
	    	rule.equals(AdditionalDLRules.ONEOFINTRO)
	    	)
	    		return true;
	    	else return false;
	    }
	
	public List<SequentInferenceRule> getRuleSet(java.lang.String str){
		return rulesets.get(str);
	}
	
	public void removeRule(String rulesetstr, SequentInferenceRule rule){
		List<SequentInferenceRule> ruleset = rulesets.get(rulesetstr);
		ruleset.remove(rule);
		return;
	}
	
	public void addRule(String rulesetstr, SequentInferenceRule rule){
		List<SequentInferenceRule> ruleset = rulesets.get(rulesetstr);
		if (!ruleset.contains(rule))
			ruleset.add(rule);
		return;
	}
	

}
