package org.semanticweb.cogExp.coverageEvaluator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.coode.owlapi.functionalparser.OWLFunctionalSyntaxParser;
import org.coode.owlapi.functionalparser.ParseException;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintOWLObjectVisitor;
import org.semanticweb.cogExp.core.RuleSetManager;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/*
import org.semanticweb.HermiT.Configuration;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;
import uk.ac.manchester.cs.jfact.JFactFactory;
*/


public class CoverageStoreEvaluator {
	
	public static Statistic runOntology(String filestring, String reasoner_select) throws OWLOntologyCreationException{
		// Read file
		Path path = Paths.get(filestring);
		List<String> lines = new ArrayList<String>();	
		try {
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<OWLAxiom> conclusions = new ArrayList<OWLAxiom>();
		List<List<OWLAxiom>> justifications = new ArrayList<List<OWLAxiom>>();
		
		String ontologyfile = lines.get(0);
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		java.io.File file = new java.io.File(ontologyfile);
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
		VerbalisationManager.INSTANCE.setOntology(ontology);
		
		// reading...!
		boolean inJustReadingMode = false;
		boolean inConclusionReadingMode = false;
		List<OWLAxiom> currentjusts = null;
		for (String line : lines){
			System.out.println("Line: " + line);
			// System.out.println("Just reading mode: " + inJustReadingMode);
			if (line.contains("CONCLUSION")){
				inJustReadingMode=false;
				inConclusionReadingMode=true;
				continue;
			}
			if (inConclusionReadingMode){
				conclusions.add(parseAxiomFunctional(line,ontology));
				inJustReadingMode=false;
				inConclusionReadingMode=false;
				continue;
			}
			if (line.contains("JUSTS")){
				inJustReadingMode=true;
				currentjusts = new ArrayList<OWLAxiom>();
				justifications.add(currentjusts);
				continue;
			}
			if (line.contains("STATS")){
				inJustReadingMode=false;
			}
			if (line.length()<2){
				inJustReadingMode=false;
			}
			if (inJustReadingMode)
				currentjusts.add(parseAxiomFunctional(line,ontology));
		}
		
		for (int i = 0; i<conclusions.size();i++)
		{
			System.out.println("concl " + conclusions.get(i));
			System.out.println("justs " + justifications.get(i));
		}
		
		
		
		OWLReasonerFactory reasonerFactory = null;
		OWLReasoner reasoner = null;
		
		
		if (reasoner_select!=null && reasoner_select.equals("elk")){
			reasonerFactory = new ElkReasonerFactory();
			// OWLReasonerConfiguration config = new SimpleConfiguration(10000);
	        reasoner = reasonerFactory.createReasoner(ontology);
		}
		
		/*
		if (reasoner_select!=null && reasoner_select.equals("jfact")){
			reasonerFactory = new JFactFactory();
			OWLReasonerConfiguration config = new SimpleConfiguration(30000);
			
			reasoner = reasonerFactory.createReasoner(ontology,config);
		}
		
		if (reasoner_select!=null && reasoner_select.equals("factpp")){
		 try {
		    	System.load("/Users/marvin/software/libFaCTPlusPlusJNI.jnilib");
		   } catch (UnsatisfiedLinkError e) {
		      System.err.println("Native code library failed to load.\n" + e);
		      // System.exit(1);
		    }
		 try {
		    	System.load("/gdisk/ki/home/mschiller/software/remote/libFaCTPlusPlusJNI.jnilib");
		   } catch (UnsatisfiedLinkError e) {
		      System.err.println("Native code library failed to load.\n" + e);
		      // System.exit(1);
		    }
		  reasonerFactory = new FaCTPlusPlusReasonerFactory();
		  reasoner = reasonerFactory.createReasoner(ontology);
		}
		
		if (reasoner_select!=null && reasoner_select.equals("hermit")){
				 Configuration config = new Configuration();
				 config.throwInconsistentOntologyException=false;
				 config.individualTaskTimeout = 600000;
				 reasonerFactory = new org.semanticweb.HermiT.Reasoner.ReasonerFactory();
				 OWLReasoner hermit=reasonerFactory.createReasoner(ontology,config);
		
				 
				 reasoner = hermit;
		}
		*/
		
		List<OWLAxiom> realaxioms = conclusions;
	   
	    
	    System.out.println("Obtained explanation generator.");
	    
	    int nontrivCounter = 0;
	    
	    int subsumptionCounter = 0;
	    int verbalizedSubsumptionCounter = 0;
	    int timedoutCounter = 0;
	    List<Long> times = new ArrayList<Long>();
	    List<Long> justTimes = new ArrayList<Long>();
	    int noSteps = 0;
	    int subPropOfProblems = 0;
	    int unionProblems = 0;
	    int disjProblems = 0;
	    List<Integer> noStepsList = new ArrayList<Integer>();
	    List<Integer> noVerbalizedStepsList = new ArrayList<Integer>();
	    List<String> verbalizations = new ArrayList<String>();
	    List<OWLAxiom> unprovenConclusions = new ArrayList<OWLAxiom>();
	    List<Set<OWLAxiom>> justsForUnprovenConclusions = new ArrayList<Set<OWLAxiom>>();
	    Integer[][] computationtimes = new Integer[100][500000];
	    for(int g=0; g<computationtimes.length;g++){
	    	Arrays.fill(computationtimes[g], -1);
	    }
	    
	    int remainingAxioms = realaxioms.size();
	    
	    /*
	    List<OWLAxiom> axiomsTmp = new ArrayList<OWLAxiom>(realaxioms); 
	    OWLAxiom axTmp = axiomsTmp.get(8);
	    realaxioms = new HashSet<OWLAxiom>();
	    realaxioms.add(axTmp);
	    */
	    
	    for(int i = 0; i<realaxioms.size(); i++){
	    	OWLAxiom ax = realaxioms.get(i);
	    	System.out.println("Working on conclusion " + ax);
	    	remainingAxioms--;
	    	if (remainingAxioms%10==0)
	    	System.out.println("REMAINING AXIOMS " + remainingAxioms);
	    	if (!(ax instanceof OWLSubClassOfAxiom)
	    			|| ((OWLSubClassOfAxiom) ax).getSuperClass().isOWLThing()
	    			){
	    		PrettyPrintOWLObjectVisitor ppvisitor = new PrettyPrintOWLObjectVisitor();
	    		System.out.println("COVERAGE EVALUATOR: THROWING AWAY THIS AXIOM : " + ax.accept(ppvisitor));
	    	}
	    	if (ax instanceof OWLSubClassOfAxiom
	    			&& !((OWLSubClassOfAxiom) ax).getSuperClass().isOWLThing()
	    			){
	    		subsumptionCounter++;
	    		OWLSubClassOfAxiom subAx = (OWLSubClassOfAxiom) ax;
	    		PrettyPrintOWLObjectVisitor ppvisitor = new PrettyPrintOWLObjectVisitor();
	    		String conclstring = subAx.accept(ppvisitor);
	    		// long juststarttime = System.currentTimeMillis();
	    		// Set<Set<OWLAxiom>> explanations = explanationGenerator.getExplanations(dataFactory.getOWLObjectIntersectionOf(subAx.getSubClass(), subAx.getSuperClass().getObjectComplementOf()),1);
	    		Set<OWLAxiom> explanation = new HashSet<OWLAxiom>(justifications.get(i));
	    		// long justendtime = System.currentTimeMillis();
	    		// justTimes.add(justendtime-juststarttime);
	    		// List<Set<OWLAxiom>> explanationsList = new ArrayList<Set<OWLAxiom>>(explanations);	
	    		if (explanation!=null && explanation.size()>0){					
	    			boolean subpropofcontained = false;
	    			boolean unioncontained = false;
	    			boolean disjcontained = false;
	    			boolean unhandledPremise = false;
	    			// Sequent<OWLObject> sequent = new Sequent<OWLObject>();
	    			org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula> sequent = new org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula>();
	    			// PrettyPrintOWLObjectVisitor ppvisitor = new PrettyPrintOWLObjectVisitor();
	    			// System.out.println("DEBUG adding conclusion axiom : " + subAx.accept(ppvisitor) + "\n");
	    			// System.out.println("DEBUG rendered conclusion axiom : " + rendering.render(subAx) + "\n");
	    			// OWLAxiom parsedAx = parseAxiom(rendering.render(subAx), ontology);
	    			// System.out.println("DEBUG rendered conclusion axiom : " + subAx.toString() + "\n");
	    			// OWLAxiom parsedAx = parseAxiom(subAx.toString(), ontology);
	    			// System.out.println("PARSED AX: " + parsedAx);
	    			// OWLAxiom ax = ManchesterOWLSyntaxEditorParser.
	    			for (OWLAxiom prem: explanation){
	    				if (prem instanceof OWLSubClassOfAxiom)
	    					prem = removeAndThing((OWLSubClassOfAxiom) prem);
	    				if (prem instanceof OWLEquivalentClassesAxiom)
	    					prem = removeAndThing((OWLEquivalentClassesAxiom) prem);
	    				System.out.println("DEBUG adding premise axiom : " + prem.accept(ppvisitor));
	    				if (prem.accept(ppvisitor)==null)
	    					unhandledPremise = true;
	    				try {
	    					// System.out.println("DBG COV ADDING " + prem);
	    					OWLFormula antform = ConversionManager.fromOWLAPI(prem);
	    					sequent.addAntecedent(antform);
							
							// checking
							if (prem instanceof OWLSubObjectPropertyOfAxiom)
								subpropofcontained = true;
							if (prem instanceof OWLDisjointClassesAxiom)
								disjcontained = true;
							if (containsUnion(prem))
								unioncontained = true;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							unhandledPremise = true;
							e.printStackTrace();
						}
	    			}		
	    			try {
	    				OWLFormula succform = ConversionManager.fromOWLAPI(ax);
						sequent.addSuccedent(succform);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			// 
	    			if (subpropofcontained)
	    				subPropOfProblems = subPropOfProblems + 1;
	    			if (unioncontained)
	    				unionProblems = unionProblems + 1;
	    			if (disjcontained)
	    				disjProblems = disjProblems + 1;
	    			// System.out.println(sequent);
	    			if (sequent.getAllAntecedentOWLFormulas().size()==1 &&
	    					sequent.getAllAntecedentOWLFormulas().containsAll(sequent.getAllSuccedentOWLFormulas())){
	    				// System.out.println("TRIVIAL");
	    				continue;
	    			}	
	    			
	    			nontrivCounter +=1;
	    			
	    			// System.out.println(explanationsList.get(0).size());
	    			org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition> node 
	    			= new org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition>(sequent,null,0);
	    			org.semanticweb.cogExp.core.ProofTree<org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<OWLFormula>,java.lang.String, org.semanticweb.cogExp.core.SequentPosition>> prooftree = new org.semanticweb.cogExp.core.ProofTree<org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition>>(node);
		          
	    			List<org.semanticweb.cogExp.core.SequentInferenceRule> allInferenceRules = org.semanticweb.cogExp.core.RuleSetManager.INSTANCE.getRuleSet("EL");
	    		 
	    			// ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition> node = new ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>(sequent,null,0);
	    			// ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String, SequentPosition>> prooftree = new ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>>(node);
		          
	    			// List<SequentInferenceRule> allInferenceRules = RuleSetManager.INSTANCE.getRuleSet("EL");
	    		 
	    			sequent.setHighestInitAxiomid(1000);
	    			
	    			long starttime = System.currentTimeMillis();
	    			
	    			if (!unhandledPremise)
	    				InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, allInferenceRules, 1000); // default: 1000
		    	 
		    	
	    			if(prooftree.getOpenNodes().size()>0){
	    				System.out.println("open nodes remain.");
	    				System.out.println("unproved sub ax: " + subAx);
	    				System.out.println("Explanations : " + explanation);
	    				unprovenConclusions.add(subAx);
	    				justsForUnprovenConclusions.add(explanation);
	    				// prooftree.print(); 
	    				timedoutCounter++;
	    				continue;
	    			};
		    	 
		    	 
	    			try {
	    				org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree, node.getId());
	    			} catch (Exception e) {
	    				e.printStackTrace();
				 		}
			    	 // 
		      
	    			org.semanticweb.cogExp.GentzenTree.GentzenTree gentzenTree;
					try {
						gentzenTree = prooftree.toGentzen();
						String result = VerbaliseTreeManager.verbaliseNL(gentzenTree, true);
						// String result = gentzenTree.verbaliseNL();
		    			System.out.println(result);
		    			long endtime = System.currentTimeMillis();
		    			times.add(endtime-starttime);
		    			verbalizedSubsumptionCounter++;
		    			noSteps = gentzenTree.getLastKey();
		    			// BIG TODO BELOW!!!
		    			List<SequentInferenceRule> rules = new ArrayList<SequentInferenceRule>(); 
		    			rules = gentzenTree.getInfRules();
		    			int noVerbalizedSteps = 0;
		    			for (SequentInferenceRule rule: rules){
		    				if (RuleSetManager.INSTANCE.isVerbalisedELRule(rule)){
		    					noVerbalizedSteps++;
		    				}
		    			}
		    			noStepsList.add(noSteps);
		    			noVerbalizedStepsList.add(noVerbalizedSteps);
		    			verbalizations.add(result);
		    			for (int k=0;k<500000;k++){
		    				if (computationtimes[noSteps][k]==-1){
		    					Long time = endtime-starttime;
		    					computationtimes[noSteps][k]=time.intValue();
		    					break;
		    				}
		    			}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    		}
	    		else{
	    			
	    			// OWLReasonerFactory reasonerFactory2 = new JFactFactory();
	    			// OWLReasonerConfiguration config = new SimpleConfiguration(30000);
	    			// OWLReasoner reasonerJFact = reasonerFactory2.createReasoner(ontology,config);
	    			
	    			// BlackBoxExplanation bBexplanator2=new BlackBoxExplanation(ontology, reasonerFactory2,reasonerJFact);
	    		    // HSTExplanationGenerator explanationGenerator2=new HSTExplanationGenerator(bBexplanator2);
	    		    
	    		    // Set<Set<OWLAxiom>> explanations = explanationGenerator2.getExplanations(dataFactory.getOWLObjectIntersectionOf(subAx.getSubClass(), subAx.getSuperClass().getObjectComplementOf()));
		    		// System.out.println(explanations.size());
		    		// System.out.println(explanations);
		    		
	    			
	    		}
	    	} else {
	    		// System.out.println(ax);
	    	}
	    }// end loop
	    System.out.println("nontriv " + nontrivCounter);
	    
	    int intAvg = avgInt(times);
	    int avgJustTime = avgInt(justTimes);
	    
	    Integer[] avgcomputationtime =new Integer[100];
	    for (int l=0;l<100;l++){
	    	int sum=0;
	    	int count = 0;
	    	for (int m=0;m<500000;m++){
	    		if (computationtimes[l][m]!=-1){
	    			sum += computationtimes[l][m];
	    			count++;
	    		}
	    		else break;
	    	}
	    	if (count>0){
	    		System.out.println("Average computation sum " + sum + " count " + count + " result " + sum/count);
	    		avgcomputationtime[l]=sum/count;
	    	}
		}
	    
	    
	    Statistic resultStatistic = new Statistic(ontologyfile, nontrivCounter, 
	    		verbalizedSubsumptionCounter, timedoutCounter, intAvg, 
	    		avgJustTime,
	    		subPropOfProblems,
	    		unionProblems,
	    		disjProblems,
	    		noStepsList, 
	    		noVerbalizedStepsList,
	    		null,unprovenConclusions, justsForUnprovenConclusions, verbalizations, avgcomputationtime);
	    System.out.println(resultStatistic.getAvgTime() + " " + resultStatistic.getAvgJustTime() + " " 
	    		       +  resultStatistic.getNoSubsumptions() + " " + resultStatistic.getNoSolvedSubsumptions() + " "
	    		       +  resultStatistic.getNoSubPropOfProblems() + " " + resultStatistic.getNoUnionProblems() + " "
	    		       + resultStatistic.getNoDisjProblems() + " "
	    		       +   resultStatistic.cardinalityNoSteps(1) + " "
	    		       +  resultStatistic.cardinalityNoSteps(2) + " " + resultStatistic.cardinalityNoSteps(3) + " "
	    		       +  resultStatistic.cardinalityNoSteps(4) + " " + resultStatistic.cardinalityNoSteps(5) + " "
	    		       +  resultStatistic.cardinalityNoSteps(6) + " " + resultStatistic.cardinalityNoSteps(7) + " "
	    		       +  resultStatistic.cardinalityNoSteps(8) + " " + resultStatistic.cardinalityNoSteps(9) + " "
	    		       + resultStatistic.cardinalityNoSteps(10));
	    System.out.println("avg time " + resultStatistic.getAvgTime() + "ms");
	    System.out.println("avg just finding time " + resultStatistic.getAvgJustTime() + "ms");
	    System.out.println("considered subsumptions " + resultStatistic.getNoSubsumptions());
	    System.out.println("successfully processed subsumptions " + resultStatistic.getNoSolvedSubsumptions());
	    System.out.println("Justs with subpropertyof " + resultStatistic.getNoSubPropOfProblems());
	    System.out.println("Justs with union " + resultStatistic.getNoUnionProblems());
	    System.out.println("Justs with disj " + resultStatistic.getNoDisjProblems());
	    System.out.println("failed justs axioms " + resultStatistic.getFailedJustsAxioms());
	    System.out.println("steps " + resultStatistic.getNoSteps());
	    System.out.print("1 steps " + resultStatistic.cardinalityNoSteps(1) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(1)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    		System.out.print("2 steps " + resultStatistic.cardinalityNoSteps(2) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(2)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    		System.out.print("3 steps " + resultStatistic.cardinalityNoSteps(3) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(3)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    		System.out.print("4 steps " + resultStatistic.cardinalityNoSteps(4) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(4)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    		System.out.print("5 steps " + resultStatistic.cardinalityNoSteps(5) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(5)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    		System.out.print("6 steps " + resultStatistic.cardinalityNoSteps(6) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(6)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    		System.out.print("7 steps " + resultStatistic.cardinalityNoSteps(7) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(7)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    		System.out.print("8 steps " + resultStatistic.cardinalityNoSteps(8) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(8)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    		System.out.print("9 steps " + resultStatistic.cardinalityNoSteps(9) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(9)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    		System.out.print("10 steps " + resultStatistic.cardinalityNoSteps(10) + " "); 
	    		System.out.print((double) resultStatistic.cardinalityNoSteps(10)/ (double) resultStatistic.getNoSubsumptions());
	    		System.out.println("%");
	    List<String> verbs = resultStatistic.getVerbalizations(); 	
	    // for (String st : verbs){
	    // 	System.out.println(st);
	    // }
	    List<OWLAxiom> unproven = resultStatistic.getUnprovenConclusions();
	    List<Set<OWLAxiom>> justs = resultStatistic.getJustsForUnprovenConclusions();
	    for(int i = 0; i<unproven.size();i++){
	    	PrettyPrintOWLObjectVisitor ppvisitor = new PrettyPrintOWLObjectVisitor();
	    	System.out.println("unproven " + unproven.get(i).accept(ppvisitor));
	    	System.out.println("justs " );
	    	for (OWLAxiom axi : justs.get(i)){
	    		System.out.println(axi.accept(ppvisitor));
	    	}
	    	for (OWLAxiom axi : justs.get(i)){
	    		System.out.println(axi);
	    	}
	    }
	    

	    
	    /*
	    // SPecial!
	    Sequent<OWLObject> sequent = new Sequent<OWLObject>();
		for (OWLAxiom prem: justs.get(0)){
			sequent.addAntecedent(prem);
		}		
		sequent.addSuccedent(unproven.get(0));
		ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition> node = new ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>(sequent,null,0);
		ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String, SequentPosition>> prooftree = new ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>>(node);
		sequent.setHighestInitAxiomid(1000);
		
		long starttime = System.currentTimeMillis();
		
		// List<SequentInferenceRule> allInferenceRules = RuleSetManager.INSTANCE.getRuleSet("EL");
		List<SequentInferenceRule> allInferenceRules = new ArrayList<SequentInferenceRule>(); 
		 allInferenceRules.add(SequentTerminationAxiom.INSTANCE);
		// allInferenceRules.add(AdditionalDLRules.ONLYSOME);
		 allInferenceRules.add(AdditionalDLRules.EQUIVEXTRACT);
		 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE1);
		 allInferenceRules.add(AdditionalDLRules.SUBCLANDEQUIVELIM);
		 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE12);
		 allInferenceRules.add(AdditionalDLRules.FORALLUNION);
		 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE23Repeat);
		 
		 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE23);
		 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE2);
		 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE15);
		 allInferenceRules.add(AdditionalDLRules.RULE5MULTI);
		 allInferenceRules.add(INLG2012NguyenEtAlRules.RULE5);
		 allInferenceRules.add(AdditionalDLRules.ELEXISTSMINUS);
		 allInferenceRules.add(AdditionalDLRules.FORALLMINUS);
		 allInferenceRules.add(AdditionalDLRules.UNIONINTRO);
		 // allInferenceRules.add(AdditionalDLRules.UNIONMINUS);
		// InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, allInferenceRules, 5000);
	    
	    */
	    return resultStatistic;
		// return null;
	    // System.out.println("2 steps " + resultStatistic.cardinalityNoSteps(2));
	    // System.out.println("3 steps " + resultStatistic.cardinalityNoSteps(3));
	    // System.out.println("4 steps " + resultStatistic.cardinalityNoSteps(4));
	    
	} 

	public static void main(String[] args) throws IOException{
		// Read list of files with EL ontologies
		
		System.out.println("Stored file list " + args[0]);   
		System.out.println("Path to stored files " + args[1]);  
		System.out.println("Reasoner " + args[2]);
		System.out.println("Shortlog file " + args[3]);
		System.out.println("Single file " + args[4]);
		// System.out.println(" " + args[3]);
		// System.out.println(args[4]);
		// System.out.println(args[5]);
		// System.out.println(args[6]);
		
		String storedfiles  = args[0];
		String storedfilesStem  = args[1];
		String reasoner_select = args[2]; 
		String shortlogFileName = args[3];
		String singlefile = args[4];
		// String boring = args[3];
		// String stem = args[4];
		// String reasoner_select = args[5];
		// String singlefile = args[6];
		
		// String order = "/Users/marvin/marvin_work_ulm/resources/ontologies/ore2015_pool_sample/el/classification/fileorder1.txt";
		// String order = "/gdisk/ki/home/mschiller/logs/verb/fileorder1_part.txt";
		// /Users/marvin/marvin_work_ulm/resources/ontologies/TONES_list.txt
		// String outlog = "/gdisk/ki/home/mschiller/logs/verb/verblogPart.txt";
		// /Users/marvin/marvin_work_ulm/resources/ontologies/verblogTONES.txt
		// String outlog = "/Users/marvin/marvin_work_ulm/resources/ontologies/verblog.txt";
		// String interesting = "/Users/marvin/marvin_work_ulm/resources/ontologies/interestingEL.txt";
		// /Users/marvin/marvin_work_ulm/resources/ontologies/interestingTONES.txt
		// String interesting = "/gdisk/ki/home/mschiller/logs/verb/interestingELPart.txt";
		//String boring = "/Users/marvin/marvin_work_ulm/resources/ontologies/boringEL.txt";
		// /Users/marvin/marvin_work_ulm/resources/ontologies/boringTONES.txt
		// String boring = "/gdisk/ki/home/mschiller/logs/verb/boringELPart.txt";
		// String stem = "/Users/marvin/work/assembla-repository/trunk/marvin_work_ulm/resources/ontologies/ore2015_pool_sample/el/pool/";
		// String stem = "/gdisk/ki/home/mschiller/data/ore2015el/";
		// "/Users/marvin/Downloads/TONES/" 
		
		Path path = Paths.get(storedfiles);
		
		List<String> lines = new ArrayList<String>();
		List<Statistic> statistics = new ArrayList<Statistic>();
		
		if (singlefile.contains("none")){
		
		try {
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		} else{
			lines.add(singlefile);
		}
		
		// create shortlog file
		Path shortlogPath = Paths.get(shortlogFileName);
		// Path outpath = Paths.get("/Users/marvin/marvin_work_ulm/resources/ontologies/verblog.txt");
		File shortlogFile = new File(shortlogPath.toString());
		if(!shortlogFile.exists()){
			shortlogFile.createNewFile();
		}
		FileWriter shortlogWriter = new FileWriter(shortlogFile, true);
		
		
		
				
		
		for(String line: lines){
		try {
			
			System.out.println("PROCESSING FILE  " + line);
			
		
			
			// Statistic stats = runOntology("/Users/marvin/marvin_work_ulm/resources/ontologies/ore2015_pool_sample/el/pool/" + line);
			Statistic stats = runOntology(storedfilesStem + line,reasoner_select);
			
			
			// create individual detailed log file	
			
			Path dumppath = Paths.get(stats.getOntologyname());
			String dumpname = dumppath.getFileName().toString();
			dumpname = dumpname.substring(0, dumpname.indexOf(".owl"));
			
		       Path longLogPath = Paths.get(storedfilesStem + "VERBS-" + dumpname + "-" + reasoner_select + ".verb");
				System.out.println("long log creation at " + longLogPath.toString());
		       // Path outpath = Paths.get("/Users/marvin/marvin_work_ulm/resources/ontologies/verblog.txt");
				File longlogFile = new File(longLogPath.toString());
				if(!longlogFile.exists()){
					longlogFile.createNewFile();
				}
				// empty file
				PrintWriter pw = new PrintWriter(longLogPath.toString());
				pw.close();
				
			FileWriter longlogWriter = new FileWriter(longlogFile, true);
	
			
			statistics.add(stats);
			longlogWriter.write("=== Ontology " + stats.getOntologyname() + "\n");
			longlogWriter.write("reasoner " +reasoner_select + "\n");
			longlogWriter.write("avg time " + stats.getAvgTime() + "ms" + "\n");
			longlogWriter.write("considered subsumptions " + stats.getNoSubsumptions() + "\n");
			longlogWriter.write("successfully processed subsumptions " + stats.getNoSolvedSubsumptions() + "\n");
			// longlogWriter.write("failed justs axioms " + stats.getFailedJustsAxioms() + "\n");
			longlogWriter.write("Justs with subpropertyof " + stats.getNoSubPropOfProblems()  + "\n");
			longlogWriter.write("Justs with union " + stats.getNoUnionProblems()  + "\n");
			longlogWriter.write("Justs with disj " + stats.getNoDisjProblems()  + "\n");
			// longlogWriter.write("steps " + stats.getNoSteps() + "\n");
			longlogWriter.write("Steps\n");
			longlogWriter.write("      1|     2 |      3|      4|      5|      6|      7|      8|      9|     10\n");	
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(1)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(2)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(3)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(4)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(5)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(6)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(7)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(8)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(9)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoSteps(10))+ ",");
			longlogWriter.write("\n");
			longlogWriter.write("Verbalised Steps\n");
			longlogWriter.write("      1|     2 |      3|      4|      5|      6|      7|      8|      9|     10\n");	
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(1)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(2)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(3)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(4)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(5)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(6)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(7)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(8)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(9)) + ",");
			longlogWriter.write(String.format("%7d", stats.cardinalityNoVerbalisedSteps(10))+ ",");
			longlogWriter.write("\n");
			
			longlogWriter.write("Times\n");
			Integer[] times = stats.getComputationtimes();
			for(int l=0; l<100;l++){
				if (times[l]!=null){
					longlogWriter.write("Steps " + l + ": " + times[l] + "\n");
				}
			}
			longlogWriter.write("\n");
			List<String> verbs = stats.getVerbalizations(); 
			 for (String st : verbs){
				 longlogWriter.write(st);
				 longlogWriter.write("%\n");
			    }
			
			 
			shortlogWriter.write(stats.getOntologyname() + " ");
			shortlogWriter.write(stats.getNoSubsumptions() + " ");
			shortlogWriter.write(stats.getNoSolvedSubsumptions() + "\n");
			 
			 longlogWriter.close();
			
		
			// runOntology("/Users/marvin/marvin_work_ulm/notes/langgen-paper/ontologies/flu.owl");
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 } //for
		shortlogWriter.close();
	
	}
	
	public static int avgInt(List<Long> times){
		Long avg = 0l;
	    for (Long l : times){
	    	avg += l;
	    }
	    if (times.size()>0)
	    avg = avg/times.size();	
	    else avg=0l;
	    return avg.intValue();
	}
	
	public static boolean containsUnion(OWLAxiom ax){
		if (ax instanceof OWLSubClassOfAxiom)
			return containsUnion((OWLSubClassOfAxiom) ax);
		if (ax instanceof OWLEquivalentClassesAxiom)
			return containsUnion((OWLEquivalentClassesAxiom) ax);
		return false;
	}
	
	public static boolean containsUnion(OWLSubClassOfAxiom ax){
		return containsUnion(ax.getSubClass()) || containsUnion(ax.getSuperClass());
	}
	
	public static boolean containsUnion(OWLObjectUnionOf exp){
		return true;
	}
	
	public static boolean containsUnion(OWLEquivalentClassesAxiom exp){
		for (OWLClassExpression ce : exp.getClassExpressionsAsList()){
			if (containsUnion(ce))
				return true;
		}
		return false;
	}
	
	public static boolean containsUnion(OWLObjectIntersectionOf exp){
		for (OWLClassExpression ce : exp.getOperandsAsList()){
			if (containsUnion(ce))
				return true;
		}
		return false;
	}
	
	public static boolean containsUnion(OWLObjectSomeValuesFrom exp){
		return containsUnion(exp.getFiller());
	}
	
	public static boolean containsUnion(OWLObjectAllValuesFrom exp){
		return containsUnion(exp.getFiller());
	}
	
	public static boolean containsUnion(OWLClassExpression exp){
		if (exp instanceof OWLObjectUnionOf)
			return containsUnion((OWLObjectUnionOf) exp);
		if (exp instanceof OWLObjectIntersectionOf)
			return containsUnion((OWLObjectIntersectionOf) exp);
		if (exp instanceof OWLObjectSomeValuesFrom)
			return containsUnion((OWLObjectSomeValuesFrom) exp);
		if (exp instanceof OWLObjectAllValuesFrom)
			return containsUnion((OWLObjectAllValuesFrom) exp);
		return false;
	}
	
	public static OWLClassExpression removeAndThing(OWLClassExpression ce){
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory=manager.getOWLDataFactory();
		if (ce instanceof OWLObjectIntersectionOf){
			// System.out.println("remove and thing called, intersection case " + ce);
			OWLObjectIntersectionOf in = (OWLObjectIntersectionOf) ce;
			List<OWLClassExpression> exps = in.getOperandsAsList();
			HashSet<OWLClassExpression> newargs = new HashSet<OWLClassExpression>();
			for (OWLClassExpression ex: exps){
				if (!ex.isOWLThing()){
					newargs.add(ex);
				}
			}
			if (newargs.size()>1)
				return dataFactory.getOWLObjectIntersectionOf(newargs);
			else if (newargs.size()==1){
				List<OWLClassExpression> newarg = new ArrayList<OWLClassExpression>(newargs);
				return newarg.get(0);
			} 
			else
			return ce;
		}
		else{
			if (ce instanceof OWLObjectSomeValuesFrom){
				OWLObjectSomeValuesFrom exp = (OWLObjectSomeValuesFrom) ce;
				return dataFactory.getOWLObjectSomeValuesFrom(exp.getProperty(),removeAndThing(exp.getFiller()));
			}
			if (ce instanceof OWLObjectAllValuesFrom){
				OWLObjectAllValuesFrom exp = (OWLObjectAllValuesFrom) ce;
				return dataFactory.getOWLObjectAllValuesFrom(exp.getProperty(),removeAndThing(exp.getFiller()));
			}
			return ce;
		}
	}
	
	public static OWLAxiom removeAndThing(OWLSubClassOfAxiom ax){
		// System.out.println("remove and thing called with + " + ax);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory=manager.getOWLDataFactory();
		return dataFactory.getOWLSubClassOfAxiom(removeAndThing(ax.getSubClass()),removeAndThing(ax.getSuperClass()));
	}
	
	public static OWLAxiom removeAndThing(OWLEquivalentClassesAxiom ax){
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory=manager.getOWLDataFactory();
		List<OWLClassExpression> exps = ax.getClassExpressionsAsList();
		HashSet<OWLClassExpression> newargs = new HashSet<OWLClassExpression>();
		for (OWLClassExpression ex : exps){
			newargs.add(removeAndThing(ex));
		}
		return dataFactory.getOWLEquivalentClassesAxiom(newargs);
	}
	
	public static OWLAxiom parseAxiom(String str, OWLOntology ontology) {
        OWLDataFactory dataFactory = ontology.getOWLOntologyManager()
                .getOWLDataFactory();
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(
                dataFactory, str);
        parser.setDefaultOntology(ontology);
        Set<OWLOntology> importsClosure = ontology.getImportsClosure();
        BidirectionalShortFormProvider bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(ontology.getOWLOntologyManager(),
                importsClosure, new DefaultPrefixManager());
        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
        parser.setOWLEntityChecker(entityChecker);
        try {
			return parser.parseAxiom();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        // return parser.parseClassExpression();
        }
	
	public static OWLAxiom parseAxiomFunctional(String str, OWLOntology ont){
		OWLFunctionalSyntaxParser parser = new OWLFunctionalSyntaxParser(new StringReader(str));
	    parser.setUp(ont, new OWLOntologyLoaderConfiguration());
		 OWLAxiom a = null;
		try {
        a = parser.Axiom();
       } catch (ParseException e) {
         System.err.println(e.getMessage());
         System.err.println("Error parsing axiom: " + str);
        }
    return a;
	}
 
	
}
