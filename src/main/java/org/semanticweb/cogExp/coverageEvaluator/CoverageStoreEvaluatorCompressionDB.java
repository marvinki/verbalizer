package org.semanticweb.cogExp.coverageEvaluator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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

// https://sourceforge.net/p/owlapi/mailman/message/24109072/

// import org.coode.owlapi.functionalparser.OWLFunctionalSyntaxParser;
// import org.coode.owlapi.functionalparser.ParseException;

import org.semanticweb.owlapi.functional.parser.OWLFunctionalSyntaxOWLParser;
import org.semanticweb.owlapi.functional.parser.OWLFunctionalSyntaxOWLParserFactory;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.cogExp.core.IncrementalSequent;
import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.OWLAPIManagerManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintOWLObjectVisitor;
import org.semanticweb.cogExp.core.RuleSetManager;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.inferencerules.AdditionalDLRules;
import org.semanticweb.cogExp.inferencerules.INLG2012NguyenEtAlRules;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.functional.parser.OWLFunctionalSyntaxOWLParser;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
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
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import uk.ac.manchester.cs.jfact.JFactFactory;



public class CoverageStoreEvaluatorCompressionDB {
	// static private OWLOntologyManager manager =OWLManager.createOWLOntologyManager();
	static private OWLOntologyManager manager = getImportKnowledgeableOntologyManger();
			// OWLManager.createOWLOntologyManager();
	static private OWLDataFactory dataFactory=manager.getOWLDataFactory();
	static private OWLOntology tmpOntology = createOntology();
	
	
	public static OWLOntology createOntology(){
		OWLOntology ontology = null;
		try {
			ontology = manager.createOntology();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ontology;
	}
	
	
	
	public static OWLOntology fetchOntology(String ontologyfile){
		
		// OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		java.io.File file = new java.io.File(ontologyfile);
		FileDocumentSource source = new FileDocumentSource(file);
			
		OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration(); 
		
		
		OWLOntology ontology = null;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(source,loaderconfig);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Ontology loaded. Reading justifications.");
		
		return ontology;
	}
	
	public static String readJustifications(String filestring, List<OWLAxiom> conclusions, List<List<OWLAxiom>> justifications){
		// Read file
				Path path = Paths.get(filestring);
				List<String> lines = new ArrayList<String>();	
				try {
					lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String ontologyfile = lines.get(0);
				OWLOntology ontology = fetchOntology(ontologyfile);
				
				
				// OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
				VerbalisationManager.INSTANCE.setOntology(ontology);
				
				// reading...!
				boolean inJustReadingMode = false;
				boolean inConclusionReadingMode = false;
				List<OWLAxiom> currentjusts = null;
				int parsedcount = 0;
				int percentachieved = 0;
				System.out.println("Lines to read: " + lines.size());
				
				int originalsize = lines.size();
				
				for (String line : lines){
					// String line = lines.get(0);
					// lines.remove(0);
					// System.out.println("Line: " + line);
					// System.out.println("Just reading mode: " + inJustReadingMode);
					// System.out.println("parsedcount: " + parsedcount);
					if ((parsedcount*10000)/(originalsize*100)>percentachieved){
						percentachieved = (parsedcount*10000)/(originalsize*100);
						System.out.printf("\r");
						System.out.print(percentachieved + "%");
					}
					parsedcount++;
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
					if (line.contains("FAILED")){
						break; // we're done!
					}
					if (line.length()<2){
						inJustReadingMode=false;
					}
					if (inJustReadingMode)
						currentjusts.add(parseAxiomFunctional(line,ontology));
				}
				// return ontology;
				return ontologyfile;
	}
	
	public static Statistic runOntology(String filestring, int timelimit1) throws OWLOntologyCreationException{
		// Read file
		
		List<OWLAxiom> conclusions = new ArrayList<OWLAxiom>();
		List<List<OWLAxiom>> justifications = new ArrayList<List<OWLAxiom>>();
		
		String ontologyfile = readJustifications(filestring,conclusions,justifications);
		
		
		String corpus = "unknown";
		if (ontologyfile.contains("ore_ont"))
			corpus = "ore2015";
		if (ontologyfile.contains("Ontology-"))
			corpus = "TONES";
		
		/*
		for (int i = 0; i<conclusions.size();i++)
		{
			System.out.println("concl " + conclusions.get(i));
			System.out.println("justs " + justifications.get(i));
		}
		*/
		
		String pathstring = "";
		File f = new File("/Users/marvin/software/wordnet/WordNet-3.0/dict");
		if (f.exists())
			pathstring = "/Users/marvin/software/wordnet/WordNet-3.0/dict";
		f = new File("/gdisk/ki/home/mschiller/software/remote/wordnet/WordNet-3.0/dict");
		if (f.exists())
			pathstring = "/gdisk/ki/home/mschiller/software/remote/wordnet/WordNet-3.0/dict";
		if (pathstring.equals(""))
			WordNetQuery.INSTANCE.disableDict();
		else	
			WordNetQuery.INSTANCE.setDict(pathstring);
		
		System.out.println("Conclusions read " + conclusions.size());
		List<OWLAxiom> realaxioms = conclusions;
	   
	    
	    // System.out.println("Obtained explanation generator.");
	    
	    int nontrivCounter = 0;
	    
	    // int subsumptionCounter = 0;
	    int verbalizedSubsumptionCounter = 0;
	    int timedoutCounter = 0;
	    Long sumVerbTimes = 0l;
	    int sumN = 0;
	    // List<Long> justTimes = new ArrayList<Long>();
	    int noSteps = 0;
	    int noSteps2 = 0;
	    int noSteps2calculated = 0;
	    int subPropOfProblems = 0;
	    int unionProblems = 0;
	    int disjProblems = 0;
	    List<Integer> noStepsList = new ArrayList<Integer>();
	    List<Integer> noNoncompressedStepsList = new ArrayList<Integer>();
	    List<Integer> noNoncompressedStepsCalculatedList =  new ArrayList<Integer>();
	    List<Integer> noVerbalizedStepsList = new ArrayList<Integer>();
	    List<String> verbalizations = new ArrayList<String>();
	    List<String> proofListings = new ArrayList<String>();
	    List<String> longVerbalizations = new ArrayList<String>();
	    List<String> longProofListings = new ArrayList<String>();
	    List<List<String>> infrulesUsedReport = new ArrayList<List<String>>();
	    List<List<String>>longInfrulesUsedReport = new ArrayList<List<String>>();
	    List<OWLAxiom> unprovenConclusions = new ArrayList<OWLAxiom>();
	    List<Set<OWLAxiom>> justsForUnprovenConclusions = new ArrayList<Set<OWLAxiom>>();
	    Set<String> usedrules = new HashSet<String>();
	    List<Integer> compressions = new ArrayList<Integer>();
	    
	    Long[] computationtimes = new Long[100];
	    Long[] computationtimesN = new Long[100];
	    Arrays.fill(computationtimes, 0l);
	    Arrays.fill(computationtimesN, 0l);
	    
	    boolean featClAgg = false;
	    boolean featRAgg = false;
	    boolean featAtt = false;
	    
	    /*
	    Integer[][] computationtimes = new Integer[100][500000];
	    for(int g=0; g<computationtimes.length;g++){
	    	Arrays.fill(computationtimes[g], -1);
	    }
	    */
	    
	    int remainingAxioms = realaxioms.size();
	    PrettyPrintOWLObjectVisitor ppvisitor = new PrettyPrintOWLObjectVisitor();
	    
	    // List<org.semanticweb.cogExp.core.SequentInferenceRule> allInferenceRules = RuleSetManager.INSTANCE.getRuleSet("ELnonredundant");
	    
	    List<SequentInferenceRule> allInferenceRules = RuleSetManager.INSTANCE.getRuleSet("EL");
	    List<SequentInferenceRule> nonredundantInferenceRules = RuleSetManager.INSTANCE.getRuleSet("ELnonredundant");
	    
	    
	    int nonredundantProblems = 0;
	    
	   for(int i = 0; i<realaxioms.size(); i++){
	   //  for(OWLAxiom ax : realaxioms){  //int i = 0; i<realaxioms.size(); i++){
	    	OWLAxiom ax = realaxioms.get(i);
	    	System.out.println("Problematic nonredundant axioms so far: " +  nonredundantProblems);
	    	// System.out.println("Working on conclusion " + ax);
	    	remainingAxioms--;
	    	if (remainingAxioms%10==0){
	    	System.out.println("REMAINING AXIOMS " + remainingAxioms);
	    	}
	    	INLG2012NguyenEtAlRules.RULE12.clearCaches();
			INLG2012NguyenEtAlRules.RULE15.clearCaches();
	    	
	    	if (!(ax instanceof OWLSubClassOfAxiom)
	    			|| ((OWLSubClassOfAxiom) ax).getSuperClass().isOWLThing()
	    			){
	    		System.out.println("COVERAGE EVALUATOR: THROWING AWAY THIS AXIOM : " + ax.accept(ppvisitor));
	    	}
	    	if (ax instanceof OWLSubClassOfAxiom
	    			&& !((OWLSubClassOfAxiom) ax).getSuperClass().isOWLThing()
	    			){
	    		// subsumptionCounter++;
	    		OWLSubClassOfAxiom subAx = (OWLSubClassOfAxiom) ax;
	    		
	    		// check database before starting
				List<String> queryResult = DatabaseManager.INSTANCE.getExplanation(
    					subAx.getSubClass().toString(), 
    					subAx.getSuperClass().toString(), 
    					ontologyfile);
    			
    			boolean solved = DatabaseManager.INSTANCE.getSolved(queryResult);
    			// System.out.println("solved? " + solved);
    			
    			// if this has been done before
    			if (solved){
    				System.out.println("Found as solved in database: " + subAx.accept(ppvisitor));
    				// System.out.println("already in database: " + subAx.toString());
    				// System.out.println(queryResult);
    				continue;
    			}
    			
    			// if this has timed out before when given at least the same time
    			if (!solved &&  DatabaseManager.INSTANCE.getTime(queryResult)>0){
    				if (DatabaseManager.INSTANCE.getTime(queryResult) >= timelimit1 * 1000){
    					System.out.println("Timeout in DB: " + subAx.accept(ppvisitor));
    					System.out.println("had timed out before at: " + DatabaseManager.INSTANCE.getTime(queryResult)  + "ms" );
    					continue;
    				}
    			}
				
    			// make GCs work easier
    			queryResult = null;
    			
    			/****** WE ARE BEING WASTEFUL!
    			
    			// if this is identical to an axiom in a different database
    			String occurenceResult = DatabaseManager.INSTANCE.findOccurence(subAx.getSubClass().toString(),subAx.getSuperClass().toString());
	    		if (occurenceResult!=null && occurenceResult.length()>0){
	    			System.out.println("found another occurence of: " + subAx.toString());
	    			System.out.println("in database: " + occurenceResult);
	    			System.out.println("checking if " + ontologyfile + " in " + occurenceResult);
	    			if (occurenceResult.contains(ontologyfile)){
	    				// in this case, we do not want to duplicate the existing occurence entry
	    				System.out.println("Found that occurence already recorded.");
	    				continue;
	    			}
	    			DatabaseManager.INSTANCE.addOccurence(subAx.getSubClass().toString(),subAx.getSuperClass().toString(),ontologyfile);
	    			continue;
	    		}
    			
    			*/
	    		// String conclstring = subAx.accept(ppvisitor);
	    		// long juststarttime = System.currentTimeMillis();
	    		// Set<Set<OWLAxiom>> explanations = explanationGenerator.getExplanations(dataFactory.getOWLObjectIntersectionOf(subAx.getSubClass(), subAx.getSuperClass().getObjectComplementOf()),1);
	    		Set<OWLAxiom> explanation = new HashSet<OWLAxiom>(justifications.get(i));
	    		// DEBUG
	    		// System.out.println("Justification axioms: " + explanation.size());
	    		// long justendtime = System.currentTimeMillis();
	    		// justTimes.add(justendtime-juststarttime);
	    		// List<Set<OWLAxiom>> explanationsList = new ArrayList<Set<OWLAxiom>>(explanations);	
	    		if (explanation!=null && explanation.size()>0){	
	    			
	    			boolean subpropofcontained = false;
	    			boolean unioncontained = false;
	    			boolean disjcontained = false;
	    			boolean unhandledPremise = false;
	    			// Sequent<OWLObject> sequent = new Sequent<OWLObject>();
	    			// org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula> sequent = new org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula>();
	    			IncrementalSequent sequent = new IncrementalSequent();
	    			IncrementalSequent sequent2 = new IncrementalSequent();
	    			for (OWLAxiom prem: explanation){
	    				
	    			
	    				long startPreproc = System.currentTimeMillis();
	    				if (prem instanceof OWLSubClassOfAxiom){
	    					OWLSubClassOfAxiom premax = (OWLSubClassOfAxiom) prem;
	    					if (containsThing(premax))
	    						prem = removeAndThing(premax);
	    				}
	    				if (prem instanceof OWLEquivalentClassesAxiom){
	    					OWLEquivalentClassesAxiom premax = (OWLEquivalentClassesAxiom) prem;
	    					if (containsThing(premax))
	    						prem = removeAndThing(premax);
	    				}
	    				if (prem==null || prem.accept(ppvisitor)==null){
	    					System.out.println("detected unhandled premise: " + prem);
	    					unhandledPremise = true;
	    					// if (true)
	    					// 	throw new RuntimeException();
	    				}
	    				
	    				long endPreproc = System.currentTimeMillis();
	    				
	    				
	    				
	    				// System.out.println("preproc " + (endPreproc - startPreproc));
	    				try {
	    					// System.out.println("DBG COV ADDING " + prem);
	    					OWLFormula antform = ConversionManager.fromOWLAPI(prem);
	    					sequent.addAntecedent(antform);
	    					sequent2.addAntecedent(antform);
	    					// if (prem instanceof OWLSubObjectPropertyOfAxiom){
	    						// System.out.println(prem);
	    						// System.out.println(antform);
	    					// }
							
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
						sequent2.addSuccedent(succform);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			
	    			// below is for debugging only!
    				/*
    				for(OWLAxiom just:explanation){
    					System.out.println("Prem: " + VerbalisationManager.prettyPrint(just));	
    				}
    				System.out.println("Conclusion: " + VerbalisationManager.prettyPrint(ax));
    					*/
    				
    				// <--- end debugging
	    			
	    			if(unhandledPremise){
	    				System.out.println("unhandled premise.");
	    				System.out.println("unproved sub ax: " + subAx);
	    				System.out.println("Explanations : " + explanation);
	    				unprovenConclusions.add(subAx);
	    				justsForUnprovenConclusions.add(explanation);
	    				// prooftree.print(); 
	    				timedoutCounter++;
	    				continue;
	    			};
	    			
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
	    				System.out.println("TRIVIAL : " +  
	    					sequent.getAllAntecedentOWLFormulas().toString()  + " --   " +  sequent.getAllSuccedentOWLFormulas().toString());
	    				continue;
	    			}	
	    			
	    			// System.out.println("increasing nontrivCounter!");
	    			// System.out.println("subAx: " + subAx);
	    			nontrivCounter +=1;
	    			
	    			// org.semanticweb.cogExp.core.Sequent sequent2 = sequent.clone();
	    			
	    			// System.out.println(explanationsList.get(0).size());
	    			org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition> node 
	    			= new org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition>(sequent,null,0);
	    			org.semanticweb.cogExp.core.ProofTree<org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<OWLFormula>,java.lang.String, org.semanticweb.cogExp.core.SequentPosition>> prooftree = new org.semanticweb.cogExp.core.ProofTree<org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition>>(node);
		          
	    			org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition> node2 
	    			= new org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<org.semanticweb.cogExp.OWLFormulas.OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition>(sequent2,null,0);
	    			org.semanticweb.cogExp.core.ProofTree<org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<OWLFormula>,java.lang.String, org.semanticweb.cogExp.core.SequentPosition>> prooftree2 = new org.semanticweb.cogExp.core.ProofTree<org.semanticweb.cogExp.core.ProofNode<org.semanticweb.cogExp.core.Sequent<OWLFormula>,java.lang.String,org.semanticweb.cogExp.core.SequentPosition>>(node2);
		          
	    		 
	    			System.out.println(prooftree2.toString());
	    			
	    			// ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition> node = new ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>(sequent,null,0);
	    			// ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String, SequentPosition>> prooftree = new ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>>(node);
		          
	    			// List<SequentInferenceRule> allInferenceRules = RuleSetManager.INSTANCE.getRuleSet("EL");
	    		 
	    			sequent.setHighestInitAxiomid(1000);
	    			
	    			sequent2.setHighestInitAxiomid(1000);
	    			
	    			
	    			long starttime = System.currentTimeMillis();
	    			
	    			// System.out.println("before searching");
	    			
	    			// SETTINGS!
	    			if (!unhandledPremise)
	    				// InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, allInferenceRules, 50000,180000); // default: 1000
	    				// InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, allInferenceRules, 300000,80000000); // default: 1000
	    				{
	    				// Optimisation: switch off unneeded rules
	    				
	    				boolean containsIntersection = false;
	    				if (containsIntersection(subAx))
	    					containsIntersection = true;
	    				for (OWLAxiom expl:explanation){
	    					if (containsIntersection(expl)){
	    						containsIntersection = true;
	    						break;
	    					}
	    				}
	    				
	    				List<SequentInferenceRule>  currentrules = new ArrayList<SequentInferenceRule>(allInferenceRules);
	    				
	    				if (!containsIntersection){
	    					// System.out.println("Disabling intersection.");
	    					currentrules.remove(AdditionalDLRules.RULE5MULTI);
	    					currentrules.remove(INLG2012NguyenEtAlRules.RULE5);
	    					currentrules.remove(INLG2012NguyenEtAlRules.RULE2);
	    				}
	    				
	    				
	    				// also disable Rule3 (if no domain) and subclandequiv (if no equiv), and rule 34 (if no disjointness)
	    				 // 412 -- 900000
	    				System.out.println("entering loop no 1.");
	    				InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree, currentrules, 840000, timelimit1 * 1000);
	    				
	    				
	    				try {
							GentzenTree treedebug = prooftree.toGentzen();
							System.out.println("used rules for first tree: " + treedebug.getInfRules());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    				
	    				
	    				INLG2012NguyenEtAlRules.RULE12.clearCaches();
	    				INLG2012NguyenEtAlRules.RULE15.clearCaches();
	    				
	    				if (prooftree.getOpenNodes().size()==0){
	    				System.out.println("entering loop no 2.");
	    				// InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree2, nonredundantInferenceRules, 1840000, timelimit1 * 1000 * 5);
	    				InferenceApplicationService.INSTANCE.runSimpleLoop(prooftree2, nonredundantInferenceRules, 1840000, timelimit1 * 1000 * 120);
	    				}
	    				System.out.println("Done looping.");
	    				
	    				// System.out.println("exiting loop");
	    				}
	    			// System.out.println("else");
		    	 
	    			// System.out.println("after searching");
		    	
	    			if(prooftree.getOpenNodes().size()>0){
	    				System.out.println("open nodes remain.");
	    				System.out.println("unproved sub ax: " + subAx);
	    				System.out.println("Explanations : " + explanation);
	    				// if (true)
	    				// 	throw new RuntimeException();
	    				unprovenConclusions.add(subAx);
	    				justsForUnprovenConclusions.add(explanation);
	    				// prooftree.print(); 
	    				timedoutCounter++;
	    				
	    				/*
	    				System.out.println("Unsolved " + subAx.getSubClass().toString() 
	    											    + " subcl "
	    											    + subAx.getSuperClass().toString() );
	    				System.out.println("Justifications " +  );
	    				*/
	    				
	    				DatabaseManager.INSTANCE.insertExplanation(
		    					subAx.getSubClass().toString(), 
		    					subAx.getSuperClass().toString(), 
		    					ontologyfile,
		    					corpus, 
		    					false, 
		    					"", 
		    					"", 
		    					"", 
		    					0, 
		    					0, 
		    					0, 
		    					timelimit1 * 1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
		    					
	    				
	    				continue;
	    			};
		    	 
	    				
	    			if(prooftree2.getOpenNodes().size()>0)
	    			{
	    				System.out.println("prooftree 2 failed");
	    				System.out.println("... this is strange, because prooftree 1 seems to be ok");
	    				System.out.println("Unproven axiom : " + subAx);
	    				System.out.println("Explanation " + explanation);
	    				System.out.println(prooftree2.toString());
	    				// throw new RuntimeException();
	    				nonredundantProblems = nonredundantProblems + 1;
	    				
	    				
	 
	    				
	    				continue;
	    				/*
	    				
	    				
	    				
	    				try {
							System.out.println("now gentzening the first tree.");
							org.semanticweb.cogExp.GentzenTree.GentzenTree gentzenTree = prooftree.toGentzen();
							System.out.println(VerbaliseTreeManager.listOutput(gentzenTree));
	    				}catch(Exception e){
	    					e.printStackTrace();
	    				}
	    				
	    				continue;
	    				// throw new RuntimeException();
	    				 * 
	    				 * 
	    				 */
	    			}
	    			
		    	 
	    			try {
	    				System.out.println("Eliminating irrelevant parts 1");
	    				org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree, node.getId());
	    			} catch (Exception e) {
	    				System.out.println("Error while eliminating irrelevant parts 1");
	    				e.printStackTrace();
				 		}
			    	 // 
	    			
	    			try {
	    				System.out.println("Eliminating irrelevant parts 2");
	    				org.semanticweb.cogExp.core.InferenceApplicationService.INSTANCE.eliminateIrrelevantParts(prooftree2, node2.getId());
	    			} catch (Exception e) {
	    				System.out.println("Error while eliminating irrelevant parts 2");
	    				e.printStackTrace();
				 		}
	    			
	    			// System.out.println("after eliminating");
		      
	    			org.semanticweb.cogExp.GentzenTree.GentzenTree gentzenTree;
	    			org.semanticweb.cogExp.GentzenTree.GentzenTree gentzenTree2;
					try {
						System.out.println("before gentzening no 1.");
						gentzenTree = prooftree.toGentzen();
						System.out.println("before gentzening no 2.");
						gentzenTree2 = prooftree2.toGentzen();
						System.out.println("after gentzening");
						
						
						VerbalisationManager.INSTANCE.featureClassAgg = false;
						VerbalisationManager.INSTANCE.featureRoleAgg = false;
						VerbalisationManager.INSTANCE.featureAttribute = false;
					
						
						String result = VerbaliseTreeManager.verbaliseNL(gentzenTree, true, false,false,null);
						System.out.println("Done result.");
						String result2 = VerbaliseTreeManager.verbaliseNL(gentzenTree2, true, false,true,null);
						System.out.println("Done result2.");
						String result3 = VerbaliseTreeManager.listOutput(gentzenTree);
						System.out.println("Done result3.");
						String result4 = VerbaliseTreeManager.listOutput(gentzenTree2);
						System.out.println("Done result4.");
						proofListings.add(result3);
						longVerbalizations.add(result2);
						longProofListings.add(result4);
						// System.out.println("obtained nl result");
						// String result = gentzenTree.verbaliseNL();
		    			// System.out.println(result);
						
						int additional = 0;
						
						System.out.println("Verbalization done.");
						
						
						// CALCULATE ADDITIONAL R5MULTI STEPS
						if (result.contains("[args:")){
							String rest = result;
							while(rest.contains("[args:")){
								int indx = rest.indexOf("[args: ");
								rest = rest.substring(indx+7);
								int indx2 = rest.indexOf("]");
								String number = rest.substring(0, indx2);
								System.out.println("NUMBER {" + number + "}");
								int no = Integer.parseInt(number);
								if (no==3){
									additional = additional + 1;
								}
								if (no==4){
									additional = additional + 2;
								}
								if (no==5){
									additional = additional + 3;
								}
							}
						}
						
						/* DEBUG
						System.out.println("calculate additional after 5multi:" + additional);
						*/
						
						// CALCULATE ADDITIONAL EQUIVEXTRACT
						if (result.contains("EQUIVEXTRACT")){
							String rest = result;
							while(rest.contains("EQUIVEXTRACT")){
								int indx = rest.indexOf("EQUIVEXTRACT");
								rest = rest.substring(indx+7);
								additional = additional + 1;
							}
						}
						
						/* DEBUG
						System.out.println("calculate additional after equivextrct:" + additional);
						*/
						
						// CALCULATE ADDITIONAL SubclassAndEquiv
						if (result.contains("AdditionalDLRules-SubclassAndEquiv")){
							String rest = result;
							while(rest.contains("AdditionalDLRules-SubclassAndEquiv")){
								int indx = rest.indexOf("AdditionalDLRules-SubclassAndEquiv");
								rest = rest.substring(indx+7);
								additional = additional + 1;
							}
						}
						
						/* DEBUG
						System.out.println("calculate additional after subclassandequiv:" + additional);
						*/
						
		    			long endtime = System.currentTimeMillis();
		    			
		    			if (VerbalisationManager.INSTANCE.featureClassAgg)
		    				featClAgg=true;
		    			if (VerbalisationManager.INSTANCE.featureRoleAgg)
		    				featRAgg=true;
		    			if (VerbalisationManager.INSTANCE.featureAttribute)
		    				featAtt=true;
		    			// times.add(endtime-starttime);
		    			verbalizedSubsumptionCounter++;
		    			// noSteps = gentzenTree.getLastKey();
		    			// noSteps2 = gentzenTree2.getLastKey();
		    			
		    			noSteps = gentzenTree.computePresentationOrder().size();
		    			noSteps2 = gentzenTree2.computePresentationOrder().size();
		    			
		    			int noListing1 = gentzenTree.computePresentationOrder().size();
		    			String[] outLines = result3.split("\n", -1);
		    			int altNoStepsListing = outLines.length;
		    			
		    			if (altNoStepsListing != noListing1)
		    			{
		    				System.out.println("noSteps: " + noSteps);
		    				System.out.println("altNoSteps: " + altNoStepsListing);
		    				throw new RuntimeException();
		    			}
		    			
		    			
		    			int noListing2 = gentzenTree2.computePresentationOrder().size();
		    			String[] outLines2 = result4.split("\n", -1);
		    			int altNoStepsListing2 = outLines2.length;
		    			
		    			/*
		    			if (altNoStepsListing2 != noListing2)
		    			{
		    				System.out.println("altNoStepsListing2: " + altNoStepsListing2);
		    				System.out.println("noListing2: " + noListing2);
		    				throw new RuntimeException();
		    			}
		    			*/
		    			
		    			
		    			noSteps2calculated = noSteps + additional;
		    			
		    			// if (altNoStepsListing2 != noSteps2calculated)
		    			/*
		    			if (noListing2 != noSteps2calculated)
		    			{
		    				System.out.println("calculated steps mismatch ");
		    				
		    				System.out.println("RESULT " + result + "\n");
		    				System.out.println("LISTING " + result3 + "\n" );
		    				System.out.println("LISTING " + result4 + "\n");
		    				
		    				System.out.println("altNoStepsListing2: " + altNoStepsListing2);
		    				System.out.println("noSteps2calculated : " + noSteps2calculated );
		    				throw new RuntimeException();
		    			}
		    			*/
		    			
		    			List<SequentInferenceRule> rules = new ArrayList<SequentInferenceRule>(); 
		    			rules = gentzenTree.getInfRules();
		    			List<SequentInferenceRule> rules2 = gentzenTree2.getInfRules();
		    			int noVerbalizedSteps = 0;
		    			
		    			for (SequentInferenceRule rule: rules){
		    				if (RuleSetManager.isVerbalisedELRule(rule)){
		    					noVerbalizedSteps++;
		    				}
		    				usedrules.add(rule.getShortName());
		    				
		    			}
		    			
		    			String[] outResult = result.split("\n", -1);
		    			int noOutVerbalized = outResult.length;
		    			
		    			/*
		    			if (noOutVerbalized< noVerbalizedSteps++)  // <---- correction
		    				noVerbalizedSteps = noOutVerbalized;
		    			*/
		    			
		    			ArrayList<String> report1 = new ArrayList<String>();
		    			for (SequentInferenceRule rule: rules){
		    				report1.add(rule.getShortName());
		    			}
		    			
		    			ArrayList<String> report2 = new ArrayList<String>();
		    			for (SequentInferenceRule rule: rules2){
		    				report2.add(rule.getShortName());
		    			}
		    			
		    			infrulesUsedReport.add(report1);
		    			longInfrulesUsedReport.add(report2);
		    			
		    			/* Debug output
		    			System.out.println("\n" + result + "\n");
		    			System.out.println("\n" + result2 + "\n");
		    			System.out.println("nonredundant rules " + rules2);
		    			System.out.println("regular rules " + rules);
		    			*/
		    			int noVerbalizedSteps2 = 0;
		    			for (SequentInferenceRule rule: rules2){
		    				if (RuleSetManager.isVerbalisedELRule(rule)){
		    					noVerbalizedSteps2++;
		    				}
		    			}
		    			
		    			// At least one step is always verbalized!
		    			if (noVerbalizedSteps==0)
		    				noVerbalizedSteps = 1;
		    			
		    			if (noVerbalizedSteps2==0)
		    				noVerbalizedSteps2 = 1;
		    			
		    			// Rule 5
		    			
		    			/* Debug output
		    			System.out.println("noSteps2: " + noSteps2);
		    			System.out.println("noSteps2calculated: " + noSteps2calculated);
		    			*/
		    			
		    			noStepsList.add(noSteps);
		    			
		    			
		    			if (noSteps2calculated < noListing2 || noSteps2calculated < noSteps2){
		    				noSteps2 = noSteps2calculated;
		    				noListing2 = noSteps2calculated;
		    			}
		    			
		    			
		    			noNoncompressedStepsList.add(noListing2);  // <--- 
		    			noNoncompressedStepsCalculatedList.add(noSteps2calculated);
		    			noVerbalizedStepsList.add(noVerbalizedSteps);
		    			compressions.add(noSteps2 - noVerbalizedSteps);
		    			// if (noSteps2<noSteps)
		    			// 	throw new RuntimeException();
		    			
		    			// if (noSteps2!=noSteps2calculated){
		    			
		    			/*
		    			if (noListing2!=noSteps2calculated){
		    				
		    				System.out.println("LISTING: " +  result3  + "\n");
		    				System.out.println("LISTING: " +  result4  + "\n");
		    				
		    				System.out.println("calculation is off!");
		    				Thread.sleep(2000);
		    				throw new RuntimeException();
		    				
		    			}
		    			*/
		   
		    			/* Debug output
		    			
		    			System.out.println("No steps nonredundant: " + noSteps2 + " regular " + noSteps);
		    			System.out.println("No steps verbalized nonredundant: " + noVerbalizedSteps2 + " regular " + noVerbalizedSteps);
		    			
		    			*/
		    			
		    			verbalizations.add(result);
		    			// System.out.println("before the timing stuff");
		    			Long time = endtime-starttime;
		    			sumVerbTimes = sumVerbTimes + time;
		    			sumN = sumN + 1;
		    			if (computationtimesN[noSteps]==0l){
		    				computationtimesN[noSteps] = 1l;
		    				computationtimes[noSteps] = time;
		    			} else{
		    				computationtimes[noSteps] = computationtimes[noSteps] + time;
		    				computationtimesN[noSteps] = 1l + computationtimesN[noSteps];
		    			}
		    			
		    			// System.out.println("Before tidying up");
		    			
		    			
		    			
		    			List<SequentInferenceRule> infRules = gentzenTree.getInfRules(); 
		    			
		    			DatabaseManager.INSTANCE.deleteExplanation(subAx.getSubClass().toString(), 
		    					subAx.getSuperClass().toString(), ontologyfile);
		    			
		    			System.out.println("before DB insert.");
		    			
		    			DatabaseManager.INSTANCE.insertExplanation(
		    					subAx.getSubClass().toString(), 
		    					subAx.getSuperClass().toString(), 
		    					ontologyfile,
		    					corpus, 
		    					true, 
		    					result, 
		    					result3, 
		    					result4, 
		    					noVerbalizedSteps, 
		    					noSteps, 
		    					noSteps2, 
		    					time.intValue(),
		    					countRules(infRules,"EQUIVEXTRACT"), 
		    					countRules(infRules,"SUBCLASSANDEQUIVELIM"),
		    					countRules(infRules,"R0"), 
		    					countRules(infRules,"INLG2012NguyenEtAlRule1"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule1neo"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule2"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule3"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule5"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule5new"),
		    					countRules(infRules,"R5M"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule6"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule6neo"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule12"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule12new"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule15"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule23"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule23Repeat"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule34"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule35"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule37"),
		    					countRules(infRules,"INLG2012NguyenEtAlRule42"),
		    					countRules(infRules,"Botintro"),
		    					countRules(infRules,"Topintro"),
		    					countRules(infRules,"AdditionalDLRules-DefinitionOfDomain"),
		    					countRules(infRules,"ELEXISTSMINUS"),
		    					countRules(infRules,"AdditionalDLRules-ApplicationOfRange"),
		    					countRules(infRules,"AdditionalDLRules-PROPCHAIN"),
		    					countRules(infRules,"Additional-Forall-Union"));
		    			
		    		
		    			
		    			List<String> queryResult2 = DatabaseManager.INSTANCE.getExplanation(
		    					subAx.getSubClass().toString(), 
		    					subAx.getSuperClass().toString(), 
		    					ontologyfile);
		    			
		    			System.out.println("query result obtained + " + queryResult2);
		    			
		    			boolean solved2 = DatabaseManager.INSTANCE.getSolved(queryResult2);
		    			System.out.println("Now solved? " + solved2);
		    			if (!solved2)
		    				throw new RuntimeException();
		    			
		    			/*
		    			int timeQueried = DatabaseManager.INSTANCE.getTime(queryResult);
		    			System.out.println("time? " + timeQueried);
		    			
		    			String explanationQueried = DatabaseManager.INSTANCE.getExplanation(queryResult);
		    			System.out.println("explanation? " + explanationQueried);
		    			
		    			String listingQueried = DatabaseManager.INSTANCE.getListing(queryResult);
		    			System.out.println("listing? " + listingQueried);
		    			
		    			System.out.println(DatabaseManager.INSTANCE.getExplanation(
		    					subAx.getSubClass().toString(), 
		    					subAx.getSuperClass().toString(), 
		    					ontologyfile).toString());
		    					*/
		    			
		    			/*
		    			System.out.println("--: equiv " + countRules(infRules,"EQUIVEXTRACT"));
		    			System.out.println("--: subcl " + countRules(infRules,"SUBCLASSANDEQUIVELIM"));
		    			System.out.println("--: rule1 " + countRules(infRules,"INLG2012NguyenEtAlRule1"));
		    			System.out.println("--: rule2 " + countRules(infRules,"INLG2012NguyenEtAlRule2"));
		    			System.out.println("--: rule5 " + countRules(infRules,"INLG2012NguyenEtAlRule5"));
		    			System.out.println("--: rule12 " + countRules(infRules,"INLG2012NguyenEtAlRule12"));
		    			System.out.println("--: rule42 " + countRules(infRules,"INLG2012NguyenEtAlRule42"));
		    			*/
		    			
		    			// tidying up!
		    			sequent = null;
		    			prooftree = null;
		    			gentzenTree = null;
		    			
		    			
		    			
		    			
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    		}
	    		else{
	    			
	    			
		    		
	    			
	    		}
	    	} else {
	    		// System.out.println(ax);
	    	}
	    }// end loop
	    System.out.println("nontriv " + nontrivCounter);
	    
	    // int intAvg = avgInt(times);
	    
	    Long avg = 0l;
	    if (sumN> 0)
	    	avg = sumVerbTimes / sumN;
	    int intAvg = avg.intValue();
	    int avgJustTime = 0; 
	    
	    Integer[] avgcomputationtime =new Integer[100];
	    
	    for(int m=0;m<100;m++){
	    	if (computationtimesN[m]>0){
	    		Long t = computationtimes[m]/computationtimesN[m];
	    		avgcomputationtime[m] = t.intValue();
	    	}
	    }
	    
	    /*
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
	    */
	    
	    System.out.println("nontrivconter: " + nontrivCounter);
	    System.out.println("verbalised subsumption counter: " + verbalizedSubsumptionCounter);
	    
	    Statistic resultStatistic = new Statistic(ontologyfile, nontrivCounter, 
	    		verbalizedSubsumptionCounter, timedoutCounter, intAvg, 
	    		avgJustTime,
	    		subPropOfProblems,
	    		unionProblems,
	    		disjProblems,
	    		noStepsList, 
	    		noNoncompressedStepsList,
	    		noVerbalizedStepsList,
	    		noNoncompressedStepsCalculatedList,
	    		null,unprovenConclusions, 
	    		justsForUnprovenConclusions, 
	    		verbalizations, 
	    		proofListings,
	    		longVerbalizations,
	    		longProofListings,
	    		avgcomputationtime,
	    		usedrules,
	    		compressions,
	    		featRAgg,
	    		featClAgg,
	    		featAtt,
	    		infrulesUsedReport,
	    		longInfrulesUsedReport
	    		);
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
	    	// PrettyPrintOWLObjectVisitor ppvisitor = new PrettyPrintOWLObjectVisitor();
	    	System.out.println("unproven " + unproven.get(i).accept(ppvisitor));
	    	System.out.println("justs " );
	    	for (OWLAxiom axi : justs.get(i)){
	    		if (axi!=null)
	    		System.out.println(axi.accept(ppvisitor));
	    		else
	    			System.out.println("NULL");
	    	}
	    	for (OWLAxiom axi : justs.get(i)){
	    		System.out.println(axi);
	    		System.out.println("contains intersect " + containsIntersection(axi));
	    	}
	    }
	    

	    
	   
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
		
		
		String storedfiles  = args[0];
		String storedfilesStem  = args[1];
		// String reasoner_select = args[2]; 
		String shortlogFileName = args[3];
		String singlefile = args[4];
		
		
		DatabaseManager.INSTANCE.connect();
		
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
			Statistic stats = runOntology(storedfilesStem + line,10); // <---- time limit (in seconds)
			
			
			// create individual detailed log file	
			
			Path dumppath = Paths.get(stats.getOntologyname());
			String dumpname = dumppath.getFileName().toString();
			dumpname = dumpname.substring(0, dumpname.indexOf(".owl"));
			
		       Path longLogPath = Paths.get(storedfilesStem + "COMPARVERBS-" + dumpname + "-" + ".verb");
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
			// longlogWriter.write("reasoner " +reasoner_select + "\n");
			longlogWriter.write("avg time " + stats.getAvgTime() + "ms" + "\n");
			longlogWriter.write("considered subsumptions " + stats.getNoSubsumptions() + "\n");
			longlogWriter.write("successfully processed subsumptions " + stats.getNoSolvedSubsumptions() + "\n");
			// longlogWriter.write("failed justs axioms " + stats.getFailedJustsAxioms() + "\n");
			longlogWriter.write("Justs with subpropertyof " + stats.getNoSubPropOfProblems()  + "\n");
			longlogWriter.write("Justs with union " + stats.getNoUnionProblems()  + "\n");
			longlogWriter.write("Justs with disj " + stats.getNoDisjProblems()  + "\n");
			// longlogWriter.write("steps " + stats.getNoSteps() + "\n");
			// longlogWriter.write("Steps\n");
			 Set<String> usedRules = stats.getUsedrules();
			 longlogWriter.write("Used rules " + usedRules.toString() + "\n");
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
			
			/*
			longlogWriter.write("n");
			List<String> verbs = stats.getVerbalizations(); 
			 int j = 0;
			 List<Integer> noSteps = stats.getNoSteps();
			 List<Integer> compressions = stats.getCompressions();
			 for (String st : verbs){
				 longlogWriter.write(st);
				 longlogWriter.write("% " + "Steps: " + noSteps.get(j) + " Compr: " + compressions.get(j) + " \n");
				 // longlogWriter.write("% " + "Steps: " + noSteps.get(j) + " \n");
				 j++;
			    }
			    */
			
			 longlogWriter.write("\n");
				List<String> verbs = stats.getVerbalizations(); 
				 int j = 0;
				 List<Integer> noSteps = stats.getNoSteps();
				 List<Integer> noStepsVerb = stats.getNoVerbalizedSteps();
				 List<Integer> noNoncompressedSteps = stats.getNoNoncompressedSteps();
				 List<Integer> noNoncompressedCalculatedSteps = stats.getNoNoncompressedCalculatedSteps();
				 List<Integer> compressions = stats.getCompressions();
				 List<String> listings = stats.getProofListings();
				 List<String> longListings = stats.getLongProofListings();
				 for (int indx = 0; indx < verbs.size();indx++){
				 // for (String st : verbs){
					 longlogWriter.write("--> Verbalisation:\n");
					 longlogWriter.write(verbs.get(indx));
					 longlogWriter.write("\n--> Proof listing:\n");
					 longlogWriter.write(listings.get(indx) + "\n");
					 longlogWriter.write(stats.getRulesReport().get(indx).toString());
					 longlogWriter.write("\n--> Long Proof listing:\n");
					 longlogWriter.write(longListings.get(indx) + "\n");
					 longlogWriter.write(stats.getLongRulesReport().get(indx).toString());
					 longlogWriter.write("\n ----------- \n " + "Steps: " + noSteps.get(j) 
					 + "    Steps (verbalized): " + noStepsVerb.get(j) 
					 + "    Steps (noncompressed): " + noNoncompressedSteps.get(j) 
					 + "    Steps (noncompressed, calculated): " + noNoncompressedCalculatedSteps.get(j) 
					 + "    Compr (long-->verbalized): " + compressions.get(j)
					 + "    Compr (short proof --> verbalized): " + (noSteps.get(j) - noStepsVerb.get(j) )
					 + "    Compr (long proof-->short proof): " + (noNoncompressedSteps.get(j) -  noSteps.get(j)) + " \n\n");
					 // longlogWriter.write("% " + "Steps: " + noSteps.get(j) + " \n");
					 j++;
				    }
				
				/* 	        */
				/* SHORTLOG */
				/*          */
			 
			 
			 int countcompressions = 0;
			 for (int ind : compressions){
				 if (ind >0 )
					 countcompressions++;
			 }
			 
			shortlogWriter.write(stats.getOntologyname() + ", ");
			shortlogWriter.write(stats.getNoSubsumptions() + ", ");
			shortlogWriter.write(stats.getNoSolvedSubsumptions() + ", ");
			
			
			shortlogWriter.write(stats.cardinalityNoSteps(1) + ", ");
			shortlogWriter.write(stats.cardinalityNoSteps(2) + ", ");
			shortlogWriter.write(stats.cardinalityNoSteps(3) + ", ");
			shortlogWriter.write(stats.cardinalityNoSteps(4) + ", ");
			shortlogWriter.write(stats.cardinalityNoSteps(5) + ", ");
			shortlogWriter.write(stats.cardinalityNoSteps(6) + ", ");
			shortlogWriter.write(stats.cardinalityNoSteps(7) + ", ");
			shortlogWriter.write(stats.cardinalityNoSteps(8) + ", ");
			shortlogWriter.write(stats.cardinalityNoSteps(9) + ", ");
			shortlogWriter.write(stats.cardinalityNoSteps(10) + ", ");
			
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(1) + ", ");
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(2) + ", ");
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(3) + ", ");
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(4) + ", ");
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(5) + ", ");
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(6) + ", ");
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(7) + ", ");
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(8) + ", ");
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(9) + ", ");
			shortlogWriter.write(stats.cardinalityNoVerbalisedSteps(10) + ", ");
			
			
			
			
			
			
			Integer[] comptimes = stats.getComputationtimes();
			for (int i = 1; i<11;i++){
				if (comptimes[i]!=null)
				shortlogWriter.write(comptimes[i] + ",");
				else 
					shortlogWriter.write(",");
			}
			shortlogWriter.write(stats.getAvgTime() + ",");
			
			// compressions (long --> verbalized)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoSteps().get(k)==comprno){
									comprtotal = comprtotal + stats.getCompressions().get(k);
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (long-->verbalized) " + comprno + "[ruleset]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}
						
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + stats.getCompressions().get(k);
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (long-->verbalized) " + comprno + "[nonredundant]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							//shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}
						
						
						// compressions (long --> short)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + (noNoncompressedSteps.get(k) -  noSteps.get(k));
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (long-->short) " + comprno + "[noncompressed]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}
			
						// compressions (short --> verbalized)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoSteps().get(k)==comprno){
									comprtotal = comprtotal + (noSteps.get(k) - noStepsVerb.get(k));
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (short-->verbalized) " + comprno + "[ruleset]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}
						
						// compressions (short --> verbalized)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + (noSteps.get(k) - noStepsVerb.get(k));
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (short-->verbalized) " + comprno + "[noncompressed]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}
						
						
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoSteps().get(k)==comprno){
									comprtotal = comprtotal + stats.getCompressions().get(k);
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (long-->verbalized) " + comprno + "[ruleset]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}
						
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + stats.getCompressions().get(k);
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (long-->verbalized) " + comprno + "[nonredundant]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
							
						}
						
						
						// compressions (long --> short)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + (noNoncompressedSteps.get(k) -  noSteps.get(k));
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (long-->short) " + comprno + "[noncompressed]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}
			
						// compressions (short --> verbalized)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoSteps().get(k)==comprno){
									comprtotal = comprtotal + (noSteps.get(k) - noStepsVerb.get(k));
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (short-->verbalized) " + comprno + "[ruleset]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}
						
						// compressions (short --> verbalized)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + (noSteps.get(k) - noStepsVerb.get(k));
									comprcount++;
								}
							}
							if (comprcount>0)
							System.out.println("Compressions (short-->verbalized) " + comprno + "[noncompressed]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}			
		
						/////////////////////////////////////////  LOG WRITING
						// No.1 
						// compressions (long --> verbalized) [ruleset]
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoSteps().get(k)==comprno){
									comprtotal = comprtotal + stats.getCompressions().get(k);
									comprcount++;
								}
							}
							if (comprcount>0)
								shortlogWriter.write(comprtotal + "," + comprcount + ",");
							else
								shortlogWriter.write(",,");
						}
						
						// No.2 
						// compressions (long --> verbalized) [nonredundant]
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + stats.getCompressions().get(k);
									comprcount++;
								}
							}
							if (comprcount>0)
								shortlogWriter.write(comprtotal + "," + comprcount + ",");
							else
								shortlogWriter.write(",,");
							
							// if (comprcount>0)
							// System.out.println("Compressions (long-->verbalized) " + comprno + "[nonredundant]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							
						}
						
						// No.3
						// compressions (long --> short) [nonredundant]
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + (noNoncompressedSteps.get(k) -  noSteps.get(k));
									comprcount++;
								}
							}
							// if (comprcount>0)
							// System.out.println("Compressions (long-->short) " + comprno + "[noncompressed]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							// 
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							if (comprcount>0)
								shortlogWriter.write(comprtotal + "," + comprcount + ",");
							else
								shortlogWriter.write(",,");
							
						}
			
						// No. 4
						// compressions (short --> verbalized)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoSteps().get(k)==comprno){
									comprtotal = comprtotal + (noSteps.get(k) - noStepsVerb.get(k));
									comprcount++;
								}
							}
							// if (comprcount>0)
							// System.out.println("Compressions (short-->verbalized) " + comprno + "[ruleset]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							if (comprcount>0)
								shortlogWriter.write(comprtotal + "," + comprcount + ",");
							else
								shortlogWriter.write(",,");
							
						}
						
						// No.5 
						// compressions (short --> verbalized)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + (noSteps.get(k) - noStepsVerb.get(k));
									comprcount++;
								}
							}
							// if (comprcount>0)
							// System.out.println("Compressions (short-->verbalized) " + comprno + "[noncompressed]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							if (comprcount>0)
								shortlogWriter.write(comprtotal + "," + comprcount + ",");
							else
								shortlogWriter.write(",,");
						}
						
						// No. 6
						// compressions (long --> short)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + (noNoncompressedSteps.get(k) -  noSteps.get(k));
									comprcount++;
								}
							}
							// if (comprcount>0)
							// System.out.println("Compressions (long-->short) " + comprno + "[noncompressed]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							if (comprcount>0)
								shortlogWriter.write(comprtotal + "," + comprcount + ",");
							else
								shortlogWriter.write(",,");
						}
			
						// No. 7
						// compressions (short --> verbalized)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoSteps().get(k)==comprno){
									comprtotal = comprtotal + (noSteps.get(k) - noStepsVerb.get(k));
									comprcount++;
								}
							}
							// if (comprcount>0)
							// System.out.println("Compressions (short-->verbalized) " + comprno + "[ruleset]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							if (comprcount>0)
							shortlogWriter.write(comprtotal + "," + comprcount + ",");
							else
								shortlogWriter.write(",,");
						}
						
						// No. 8
						// compressions (short --> verbalized)
						for (int comprno = 1; comprno <=20; comprno++){
							int comprtotal = 0;
							int comprcount = 0;
							for (int k =0 ; k<stats.getNoSteps().size(); k++){
								if (stats.getNoNoncompressedSteps().get(k)==comprno){
									comprtotal = comprtotal + (noSteps.get(k) - noStepsVerb.get(k));
									comprcount++;
								}
							}
							// if (comprcount>0)
							// System.out.println("Compressions (short-->verbalized) " + comprno + "[noncompressed]: " + ((float) comprtotal/(float) comprcount) + " count: " + comprcount);
							// 
							// shortlogWriter.write(comprtotal + "," + comprcount + ",");
							if (comprcount>0)
								shortlogWriter.write(comprtotal + "," + comprcount + ",");
							else
								shortlogWriter.write(",,");
						}							
						
						
			// average compressions of steps >1
			int compressiontotal = 0;
			int compressionscount = 0;	
			int stepstotal = 0;
			int stepsverbs = 0;
			for (int ind = 0; ind < stats.getNoNoncompressedSteps().size();ind++){
				if (stats.getNoNoncompressedSteps().get(ind)>1){
					compressiontotal = compressiontotal + stats.getCompressions().get(ind);
					stepstotal = stepstotal + stats.getNoNoncompressedSteps().get(ind);
					stepsverbs = stepsverbs + stats.getNoVerbalizedSteps().get(ind);
					compressionscount++;
				}
			}	
			
			
			
			
			System.out.println("average compression (>1) " + ((float) compressiontotal / ((float) compressionscount)) + " at count " + compressionscount);
			System.out.println("Avg noncompressed (>1) " + ((float) stepstotal / ((float) compressionscount)) 
					+ " verbs " + ((float) stepsverbs / ((float) compressionscount)));
			
			
			shortlogWriter.write( "," + ((float) stepstotal / ((float) compressionscount)) + "," 
			                      + ((float) stepsverbs / ((float) compressionscount)) + ","
			                      + compressionscount);
 			
			/*
			
			
			shortlogWriter.write(countcompressions + ",");
			shortlogWriter.write(stepstotal + ",");
			shortlogWriter.write(stepsverbs + ",");
			shortlogWriter.write(compressiontotal + ",");
			shortlogWriter.write(compressionscount + ",");
			
			shortlogWriter.write(usedRules.toString());
			
			*/
			
			shortlogWriter.write(",");
			
			/*
			
			if (stats.getHasRoleaggregation())
				shortlogWriter.write("1,");
				else
				shortlogWriter.write("0,");
			if (stats.getHasClassaggregation())
				shortlogWriter.write("1,");
				else
				shortlogWriter.write("0,");
			if (stats.getHasAttribute())
				shortlogWriter.write("1,");
				else
				shortlogWriter.write("0,");
			
			*/
			
			shortlogWriter.write("\n");
			
			shortlogWriter.flush();
			
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
	
	public static boolean containsIntersection(OWLAxiom ax){
		// System.out.println("contains intersection called for " + ax);
		if (ax instanceof OWLSubClassOfAxiom)
			return containsIntersection((OWLSubClassOfAxiom) ax);
		if (ax instanceof OWLEquivalentClassesAxiom)
			return containsIntersection((OWLEquivalentClassesAxiom) ax);
		return false;
	}
	
	public static boolean containsIntersection(OWLSubClassOfAxiom ax){
		return containsIntersection(ax.getSubClass()) || containsIntersection(ax.getSuperClass());
	}
	
	public static boolean containsIntersection(OWLObjectIntersectionOf exp){
		return true;
	}
	
	public static boolean containsIntersection(OWLEquivalentClassesAxiom exp){
		 // System.out.println("contains intersection equiv cl called for " + exp);
		for (OWLClassExpression ce : exp.getClassExpressionsAsList()){
			if (containsIntersection(ce))
				return true;
		}
		return false;
	}
	
	public static boolean containsIntersection(OWLObjectUnionOf exp){
		for (OWLClassExpression ce : exp.getOperandsAsList()){
			if (containsIntersection(ce))
				return true;
		}
		return false;
	}
	
	public static boolean containsIntersection(OWLObjectSomeValuesFrom exp){
		return containsIntersection(exp.getFiller());
	}
	
	public static boolean containsIntersection(OWLObjectAllValuesFrom exp){
		return containsIntersection(exp.getFiller());
	}
	
	public static boolean containsIntersection(OWLClassExpression exp){
		if (exp instanceof OWLObjectUnionOf)
			return containsIntersection((OWLObjectUnionOf) exp);
		if (exp instanceof OWLObjectIntersectionOf)
			return true;
		if (exp instanceof OWLObjectSomeValuesFrom)
			return containsIntersection((OWLObjectSomeValuesFrom) exp);
		if (exp instanceof OWLObjectAllValuesFrom)
			return containsIntersection((OWLObjectAllValuesFrom) exp);
		return false;
	}
	
	
	public static boolean containsThing(OWLSubClassOfAxiom ax){
		return containsThing(ax.getSubClass()) || containsThing(ax.getSuperClass()); 
	}
	
	public static boolean containsThing(OWLEquivalentClassesAxiom ax){
		Set<OWLClassExpression> exp = ax.getClassExpressions();
		for (OWLClassExpression ex : exp){
			if (containsThing(ex))
				return true;
		}
		return false;
	}
	
	public static boolean containsThing(OWLClassExpression ce){
		if (ce.isOWLThing())
			return true;
		if (ce instanceof OWLObjectIntersectionOf){
			OWLObjectIntersectionOf in = (OWLObjectIntersectionOf) ce;
			List<OWLClassExpression> exps = in.getOperandsAsList();
			for (OWLClassExpression ex: exps){
				if (containsThing(ex))
					return true;
			}
		}
		if (ce instanceof OWLObjectSomeValuesFrom){
			OWLObjectSomeValuesFrom exp = (OWLObjectSomeValuesFrom) ce;
			OWLClassExpression ex = exp.getFiller();
			return containsThing(ex);
		}
		if (ce instanceof OWLObjectAllValuesFrom){
			OWLObjectAllValuesFrom exp = (OWLObjectAllValuesFrom) ce;
			OWLClassExpression ex = exp.getFiller();
			return containsThing(ex);
		}
		return false;
	}
	
	public static OWLClassExpression removeAndThing(OWLClassExpression ce){
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
				return OWLAPIManagerManager.INSTANCE.getDataFactory().getOWLObjectIntersectionOf(newargs);
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
				return OWLAPIManagerManager.INSTANCE.getDataFactory().getOWLObjectSomeValuesFrom(exp.getProperty(),removeAndThing(exp.getFiller()));
			}
			if (ce instanceof OWLObjectAllValuesFrom){
				OWLObjectAllValuesFrom exp = (OWLObjectAllValuesFrom) ce;
				return OWLAPIManagerManager.INSTANCE.getDataFactory().getOWLObjectAllValuesFrom(exp.getProperty(),removeAndThing(exp.getFiller()));
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
		
		//System.out.println("Trying to parse: " + str);
		
		// OWLFunctionalSyntaxOWLParserFactory parserFactory = new OWLFunctionalSyntaxOWLParserFactory();
		OWLFunctionalSyntaxOWLParser parser = OWLAPIManagerManager.INSTANCE.getFunctionalSyntaxParser();
		
		
		OWLOntologyLoaderConfiguration loaderConfiguration = new OWLOntologyLoaderConfiguration();
		
		str = "Ontology(" + str + ")";
		
		
		// System.out.println("Trying to parse: " + str);
		
		InputStream in = new ByteArrayInputStream(str.getBytes());
		StreamDocumentSource streamSource = new StreamDocumentSource(in);
		
		// OWLOntologyDocumentSource toBeParsed = new OWLOntologyDocumentSource();
		
		OWLOntologyManager manager = OWLAPIManagerManager.INSTANCE.getOntologyManager();
		
		
		OWLAxiom a = null;
		
		try {
			
		manager.removeAxioms(tmpOntology, tmpOntology.getAxioms());
		
		OWLDocumentFormat format = parser.parse(streamSource, tmpOntology, loaderConfiguration);
		
		
		Set<OWLAxiom> axioms = tmpOntology.getAxioms();
		for (OWLAxiom ax : axioms){
			a = ax;
			in.close();
			streamSource = null;
			axioms = null;
			break;
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// str = str.replace("ObjectIntersectionOf(","");
			// str = str.substring(9,str.length()-2);
			
			// parseAxiomFunctional(str,newontology);
			
			 if (true)
		    	throw new RuntimeException();
		}
		
		/*
		OWLFunctionalSyntaxParser parser = new OWLFunctionalSyntaxParser(new StringReader(str));
	    parser.setUp(ont, new OWLOntologyLoaderConfiguration());
		 OWLAxiom a = null;
		try {
        a = parser.Axiom();
       } catch (ParseException e) {
         System.err.println(e.getMessage());
         System.err.println("Error parsing axiom: " + str);
        }
        */
		// System.out.println("Parsed: " + a);
		// if (true)
		// throw new RuntimeException();
	
		
    return a;
	}
	
	
public static Set<OWLAxiom> parseAxiomsFunctional(String str, OWLOntology ont){
		
		//System.out.println("Trying to parse: " + str);
		
		// OWLFunctionalSyntaxOWLParserFactory parserFactory = new OWLFunctionalSyntaxOWLParserFactory();
		OWLFunctionalSyntaxOWLParser parser = OWLAPIManagerManager.INSTANCE.getFunctionalSyntaxParser();
		
		
		OWLOntologyLoaderConfiguration loaderConfiguration = new OWLOntologyLoaderConfiguration();
		
		str = "Ontology(" + str + ")";
		
		
		// System.out.println("Trying to parse: " + str);
		
		InputStream in = new ByteArrayInputStream(str.getBytes());
		StreamDocumentSource streamSource = new StreamDocumentSource(in);
		
		// OWLOntologyDocumentSource toBeParsed = new OWLOntologyDocumentSource();
		
		OWLOntologyManager manager = OWLAPIManagerManager.INSTANCE.getOntologyManager();
		
		
		Set<OWLAxiom> axioms = null;
		
		try {
			
		manager.removeAxioms(tmpOntology, tmpOntology.getAxioms());
		
		OWLDocumentFormat format = parser.parse(streamSource, tmpOntology, loaderConfiguration);
		
		
			axioms = tmpOntology.getAxioms();
		} catch(Exception e){}
		
    return axioms;
	}
	
	public static int countRules(List<SequentInferenceRule> rules, String rulename){
		int i = 0;
		for (SequentInferenceRule rule : rules){
			// System.out.println(rule.getName());
			if (rule.getName().equals(rulename)){
				// System.out.println("hit");
				i++;
			}
		}
		return i;
	}
 
	public static OWLOntologyManager getImportKnowledgeableOntologyManger(){
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.loa-cnr.it/ontologies/DUL.owl"),
		           IRI.create("http://www.ontologydesignpatterns.org/ont/dul/DUL.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Hydrology/v2.0/Hydrology.owl"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-Hydrology.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Rabbit/v1.0/Rabbit.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/rabbit.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Topography/v0.1/Topography.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/topography.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/SpatialRelations/v0.2/SpatialRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/spatialrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/MereologicalRelations/v0.2/MereologicalRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/mereologicalrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/NetworkRelations/v0.2/NetworkRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/networkrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Hydrology/v2.0/Hydrology.owl"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-Hydrology.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Rabbit/v1.0/Rabbit.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/rabbit.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/Topography/v0.1/Topography.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/topography.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/SpatialRelations/v0.2/SpatialRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/spatialrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/MereologicalRelations/v0.2/MereologicalRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/mereologicalrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.ordnancesurvey.co.uk/ontology/NetworkRelations/v0.2/NetworkRelations.owl"),
		           IRI.create("file:///Users/marvin/Downloads/hydrology/networkrelations.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/nulo"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-nulo.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/bfo"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-bfo.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/bro"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-bro.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/annotation"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-annotation.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/bro-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-bro-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/biochemistry-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-biochemistry-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/chemistry-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-chemistry-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/organic-compound-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-organic-compound-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/organic-compound-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-organic-compound-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/chemistry-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-chemistry-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/organic-functional-group-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-organic-functional-group-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/atom-common"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-atom-common.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/organic-functional-group-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-organic-functional-group-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/element-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-element-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/periodic-table-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-periodic-table-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/periodic-table-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-periodic-table-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/molecule-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-molecule-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/molecule-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-molecule-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/atom-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-atom-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/property-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-property-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/property-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-property-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/unit-complex"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-unit-complex.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/unit-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-unit-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/yowl-primitive"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-yowl-primitive.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://ontology.dumontierlab.com/goslim"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-goslim.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://sweet.jpl.nasa.gov/1.1/property.owl"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-property.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://sweet.jpl.nasa.gov/1.1/units.owl"),
		           IRI.create("file:///Users/marvin/work/workspace/justifications/originaltones-ontologies/Ontology-units.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://www.co-ode.org/ontologies/lists/2008/09/11/list.owl"),
		           IRI.create("file:///Users/marvin/Downloads/6537658-811aec9d3c507976efd6aba1599ea3c12f8ed61c/list.owl")));
		manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create("http://translationalmedicineontology.googlecode.com/svn/trunk/ontology/tmo-external.owl"),
		           IRI.create("https://raw.githubusercontent.com/micheldumontier/translationalmedicineontology/master/ontology/tmo-external.owl")));
		return manager;
	}
	
	public static Set<OWLAxiom> computeInferredAxioms(OWLOntology ontology){
	// indicate a reasoner and a reasoner factory to be used for justification finding (here we use ELK)
		
		// ELK
		// OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
		// OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		
		OWLReasonerFactory reasonerFactory = new JFactFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		
		// Logger.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
		
		 Logger rootlogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	     rootlogger.setLevel(Level.OFF); 
	    // Logger iogLogger = (Logger) LoggerFactory.getLogger(InferredOntologyGenerator.class.getName());
	    
	     // LogManager.getLogger("org.semanticweb.elk").setLevel(Level.OFF);
		// Classify the ontology.
		// reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		
		// System.out.println("precomputed");

		// To generate an inferred ontology we use implementations of
		// inferred axiom generators
		// List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
		// gens.add(new InferredSubClassAxiomGenerator());
		// gens.add(new InferredEquivalentClassAxiomGenerator());
		
	    
		/*
		Compute the inferrable axioms
		*/
		// InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner,gens);
		InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
		// OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
		Set<OWLAxiom> newaxioms = new HashSet<OWLAxiom>();
		try {
			
			OWLOntology newontology = outputOntologyManager.createOntology();
			
			iog.fillOntology(outputOntologyManager.getOWLDataFactory(), newontology);
			
			System.out.println("filled");
			
		
			newaxioms = newontology.getAxioms();
		} catch(Exception e)
		{e.printStackTrace();
		}
		
		System.out.println("=== Previous Axioms === ");
		for (OWLAxiom old : ontology.getAxioms()){
			if (old instanceof OWLAnnotationAssertionAxiom)
				continue;
			if (old instanceof OWLDeclarationAxiom)
				continue;
			System.out.println(old);
			System.out.println(VerbalisationManager.prettyPrint(old));
		}
	
		
		newaxioms.removeAll(ontology.getAxioms());
		
		Set<OWLAxiom> subclaxs = new HashSet<OWLAxiom>();
		Set<OWLAxiom> equivaxs = new HashSet<OWLAxiom>();
		Set<OWLAxiom> notSubclAxs = new HashSet<OWLAxiom>();
		
		for(OWLAxiom ax: newaxioms){
			if (ax instanceof OWLEquivalentClassesAxiom){
				subclaxs.add(dataFactory.getOWLSubClassOfAxiom(((OWLEquivalentClassesAxiom) ax).getClassExpressionsAsList().get(0), 
						((OWLEquivalentClassesAxiom) ax).getClassExpressionsAsList().get(1)
						));
				subclaxs.add(dataFactory.getOWLSubClassOfAxiom(((OWLEquivalentClassesAxiom) ax).getClassExpressionsAsList().get(1), 
						((OWLEquivalentClassesAxiom) ax).getClassExpressionsAsList().get(0)
						));
				equivaxs.add(ax);
			}
			if (!(ax instanceof OWLSubClassOfAxiom)){
				notSubclAxs.add(ax); 
			}
		}
		
		newaxioms.addAll(subclaxs);
		newaxioms.removeAll(equivaxs);
		newaxioms.removeAll(notSubclAxs);
		
		Set<OWLAxiom> subthings = new HashSet<OWLAxiom>();
		
		for(OWLAxiom ax: newaxioms){
			if (ax instanceof OWLSubClassOfAxiom){
				OWLSubClassOfAxiom subcl = (OWLSubClassOfAxiom) ax;
				if (subcl.getSuperClass().isTopEntity())
					subthings.add(ax);
			}
		}
		newaxioms.removeAll(subthings);
		System.out.println("No. of new axioms: " + (newaxioms.size()));
		
		System.out.println("=== Inferred Axioms === ");
		for (OWLAxiom inf : newaxioms)
			System.out.println(VerbalisationManager.prettyPrint(inf));
		
		return newaxioms;
	}
	
	
	public static void runBioportalOntology(String input){
		OWLOntology ontology = null;
		try {
			OWLOntologyLoaderConfiguration loaderconfig = new OWLOntologyLoaderConfiguration(); 
			OWLOntologyDocumentSource source = new StringDocumentSource(input);
			ontology = manager.loadOntologyFromOntologyDocument(source,loaderconfig);
			System.out.println(" ONTOLOGY INITIALLY CONTAINS " + ontology.getAxioms().size() + " AXIOMS ");
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" Ontology axiom count " + ontology.getAxiomCount());
		
		Set<OWLAxiom> inferredAxioms = computeInferredAxioms(ontology);
		System.out.println("Axioms " + inferredAxioms);
		runBioportalOntology(ontology,inferredAxioms,10);
		
		
		
	}
	
	public static Statistic runBioportalOntology(OWLOntology ontology, Set<OWLAxiom> axioms, int timelimit1){
		
		
		
		System.out.println(" ONTOLOGY NOW CONTAINS " + ontology.getAxioms().size() + " AXIOMS ");
		
		// OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
		// OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		
		OWLReasonerFactory reasonerFactory = new JFactFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
		
		RuleSetManager.INSTANCE.addRule("EL", AdditionalDLRules.SUBCLCHAIN);
		
	
		
		// Preparations for using dictionary
		String pathstring = "";
		File f = new File("/Users/marvin/software/wordnet/WordNet-3.0/dict");
		if (f.exists())
			pathstring = "/Users/marvin/software/wordnet/WordNet-3.0/dict";
		f = new File("/gdisk/ki/home/mschiller/software/remote/wordnet/WordNet-3.0/dict");
		if (f.exists())
			pathstring = "/gdisk/ki/home/mschiller/software/remote/wordnet/WordNet-3.0/dict";
		if (pathstring.equals(""))
			WordNetQuery.INSTANCE.disableDict();
		else	
			WordNetQuery.INSTANCE.setDict(pathstring);
		
		 int nontrivCounter = 0;
		    
		    // int subsumptionCounter = 0;
		    int verbalizedSubsumptionCounter = 0;
		    int timedoutCounter = 0;
		    Long sumVerbTimes = 0l;
		    int sumN = 0;
		    // List<Long> justTimes = new ArrayList<Long>();
		    int noSteps = 0;
		    int noSteps2 = 0;
		    int noSteps2calculated = 0;
		    int subPropOfProblems = 0;
		    int unionProblems = 0;
		    int disjProblems = 0;
		    List<Integer> noStepsList = new ArrayList<Integer>();
		    List<Integer> noNoncompressedStepsList = new ArrayList<Integer>();
		    List<Integer> noNoncompressedStepsCalculatedList =  new ArrayList<Integer>();
		    List<Integer> noVerbalizedStepsList = new ArrayList<Integer>();
		    List<String> verbalizations = new ArrayList<String>();
		    List<String> proofListings = new ArrayList<String>();
		    List<String> longVerbalizations = new ArrayList<String>();
		    List<String> longProofListings = new ArrayList<String>();
		    List<List<String>> infrulesUsedReport = new ArrayList<List<String>>();
		    List<List<String>>longInfrulesUsedReport = new ArrayList<List<String>>();
		    List<OWLAxiom> unprovenConclusions = new ArrayList<OWLAxiom>();
		    List<Set<OWLAxiom>> justsForUnprovenConclusions = new ArrayList<Set<OWLAxiom>>();
		    Set<String> usedrules = new HashSet<String>();
		    List<Integer> compressions = new ArrayList<Integer>();
		    
		    Long[] computationtimes = new Long[100];
		    Long[] computationtimesN = new Long[100];
		    Arrays.fill(computationtimes, 0l);
		    Arrays.fill(computationtimesN, 0l);
		    
		    boolean featClAgg = false;
		    boolean featRAgg = false;
		    boolean featAtt = false;
		
		    List<SequentInferenceRule> allInferenceRules = RuleSetManager.INSTANCE.getRuleSet("EL");
		    List<SequentInferenceRule> nonredundantInferenceRules = RuleSetManager.INSTANCE.getRuleSet("ELnonredundant");
		    
		    int failedProofs = 0;
			List<OWLAxiom> failed = new ArrayList<OWLAxiom>();

			
			boolean identical = false;
			
			for (OWLAxiom ax : axioms){
				OWLSubClassOfAxiom subAx = (OWLSubClassOfAxiom ) ax;
				
				/* 
				OWLSubClassOfAxiom axSub = (OWLSubClassOfAxiom) ax;
				for (OWLAxiom checkAx : ontology.getAxioms()){
					if (checkAx instanceof OWLSubClassOfAxiom){
						OWLSubClassOfAxiom checkAxSub = (OWLSubClassOfAxiom) checkAx;
						System.out.println("comparing " + axSub + " and " + checkAxSub);
						
						
							if (checkAxSub.getSubClass().equals(axSub.getSubClass())
									&& checkAxSub.getSuperClass().equals(axSub.getSuperClass())
									){
								System.out.println("Axiom already contained.");
								identical = true;
						}
					}
				}
				
				if (identical)
					continue;
					*/
				
				// check database before starting
				List<String> queryResult = DatabaseManager.INSTANCE.getExplanation(
    					subAx.getSubClass().toString(), 
    					subAx.getSuperClass().toString(), 
    					ontology.getOntologyID().toString());
    			
    			boolean solved = DatabaseManager.INSTANCE.getSolved(queryResult);
    			// System.out.println("solved? " + solved);
    			
    			// if this has been done before
    			if (solved){
    				System.out.println("Found as solved in database: " + subAx);
    				// System.out.println("already in database: " + subAx.toString());
    				// System.out.println(queryResult);
    				continue;
    			}
    			
    			// if this has timed out before when given at least the same time
    			if (!solved &&  DatabaseManager.INSTANCE.getTime(queryResult)>0){
    				if (DatabaseManager.INSTANCE.getTime(queryResult) >= timelimit1 * 1000){
    					System.out.println("Timeout in DB: " + subAx);
    					System.out.println("had timed out before at: " + DatabaseManager.INSTANCE.getTime(queryResult)  + "ms" );
    					continue;
    				}
    			}
				
    			long starttime = System.currentTimeMillis();
				GentzenTree tree = VerbalisationManager.computeGentzenTree(ax, reasoner, reasonerFactory,
						ontology, 
						100,      // <-- search depth 
						timelimit1 * 1000,    // <-- time in ms
						"EL");
				long endtime = System.currentTimeMillis();
				
				if (tree==null || tree.getInfRules().size()==0){
					System.out.println("OMG we have failed!");
					failed.add(ax);
					failedProofs++;
					
					
	    				System.out.println("open nodes remain.");
	    				System.out.println("unproved sub ax: " + subAx);
	    				// System.out.println("Explanations : " + explanation);
	    				// if (true)
	    				// 	throw new RuntimeException();
	    				unprovenConclusions.add(subAx);
	    				// justsForUnprovenConclusions.add(explanation);
	    				// prooftree.print(); 
	    				timedoutCounter++;
	    				
	    				/*
	    				System.out.println("Unsolved " + subAx.getSubClass().toString() 
	    											    + " subcl "
	    											    + subAx.getSuperClass().toString() );
	    				System.out.println("Justifications " +  );
	    				*/
	    				
	    				DatabaseManager.INSTANCE.insertBioExplanation(
		    					subAx.getSubClass().toString(), 
		    					subAx.getSuperClass().toString(), 
		    					ontology.getOntologyID().getOntologyIRI().toString(),
		    					"bio", 
		    					false, 
		    					"", 
		    					"", 
		    					"", 
		    					0, 
		    					0, 
		    					0, 
		    					timelimit1 * 1000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
		    					
	    			
					
					continue;
				}
				
				try{
					String explanation = VerbalisationManager.computeVerbalization(tree, false, false,null);
					System.out.println("Explanation for \"" + VerbalisationManager.verbalise(ax) + "\":\n");
					System.out.println(VerbaliseTreeManager.listOutput(tree));
					System.out.println(explanation);
					String result = explanation;
					
					GentzenTree tree2 = VerbalisationManager.computeGentzenTree(ax, reasoner, reasonerFactory,
							ontology, 
							100,      // <-- search depth 
							timelimit1 * 1000,    // <-- time in ms
							"ELnonredundant");  
					
					String explanation2 = VerbalisationManager.computeVerbalization(tree2, false, false,null);
					String result3 = VerbaliseTreeManager.listOutput(tree);
					String result4 = VerbaliseTreeManager.listOutput(tree2);
					
					List<SequentInferenceRule> infRules = tree.getInfRules();
					int noVerbalizedSteps = 0;
	    			
	    			for (SequentInferenceRule rule: infRules){
	    				if (RuleSetManager.isVerbalisedELRule(rule)){
	    					noVerbalizedSteps++;
	    				}
	    				usedrules.add(rule.getShortName());
	    				
	    			}
	    			Long time = endtime-starttime;
					
					DatabaseManager.INSTANCE.insertBioExplanation(
	    					subAx.getSubClass().toString(), 
	    					subAx.getSuperClass().toString(), 
	    					ontology.getOntologyID().getOntologyIRI().toString(),
	    					"bio", 
	    					true, 
	    					result, 
	    					result3, 
	    					result4, 
	    					noVerbalizedSteps, 
	    					noSteps, 
	    					noSteps2, 
	    					time.intValue(),
	    					countRules(infRules,"EQUIVEXTRACT"), 
	    					countRules(infRules,"SUBCLASSANDEQUIVELIM"),
	    					countRules(infRules,"R0"), 
	    					countRules(infRules,"INLG2012NguyenEtAlRule1"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule1neo"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule2"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule3"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule5"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule5new"),
	    					countRules(infRules,"R5M"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule6"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule6neo"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule12"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule12new"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule15"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule23"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule23Repeat"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule34"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule35"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule37"),
	    					countRules(infRules,"INLG2012NguyenEtAlRule42"),
	    					countRules(infRules,"Botintro"),
	    					countRules(infRules,"Topintro"),
	    					countRules(infRules,"AdditionalDLRules-DefinitionOfDomain"),
	    					countRules(infRules,"ELEXISTSMINUS"),
	    					countRules(infRules,"AdditionalDLRules-ApplicationOfRange"),
	    					countRules(infRules,"AdditionalDLRules-PROPCHAIN"),
	    					countRules(infRules,"Additional-Forall-Union"),
	    					countRules(infRules,"SUBCLCHAIN")
							);
					
					
					
					} catch (Exception e){
						e.printStackTrace();
						continue;
					}
				
				
			}
		    
		
		return null;
	}
	
	
	
}