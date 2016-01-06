package org.semanticweb.cogExp.coverageEvaluator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.coode.owlapi.functionalparser.OWLFunctionalSyntaxParser;
import org.coode.owlapi.functionalparser.ParseException;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;

import org.semanticweb.cogExp.core.InferenceApplicationService;
import org.semanticweb.cogExp.core.ProofNode;
import org.semanticweb.cogExp.core.ProofTree;
import org.semanticweb.cogExp.core.RuleBinding;
import org.semanticweb.cogExp.core.RuleBindingForNode;
import org.semanticweb.cogExp.core.RuleSetManager;
import org.semanticweb.cogExp.core.Sequent;
import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.cogExp.core.SequentPosition;
import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLFormulas.OWLFormula;
import org.semanticweb.cogExp.PrettyPrint.PrettyPrintOWLObjectVisitor;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.elk.util.logging.Statistics;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
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
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import com.clarkparsia.owlapi.explanation.BlackBoxExplanation;
import com.clarkparsia.owlapi.explanation.HSTExplanationGenerator;

import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;

/*
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;
import uk.ac.manchester.cs.jfact.JFactFactory;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
*/

public class JustificationComputation {
	
	public static void runOntology(String filestring, String reasoner_select, FileWriter writer, boolean equivsonly) throws OWLOntologyCreationException{
		try {
			writer.write(filestring + "\n");
			writer.write(reasoner_select + "\n");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		java.io.File file = new java.io.File(filestring);
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
		VerbalisationManager.INSTANCE.setOntology(ontology);
		
		// compute all inferred subsumptions
		
		OWLReasonerFactory reasonerFactory = null;
		OWLReasoner reasoner = null;
		
		
		
		// To generate an inferred ontology we use implementations of
        // inferred axiom generators
        List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
        gens.add(new InferredSubClassAxiomGenerator());
        gens.add(new InferredEquivalentClassAxiomGenerator());

        // Put the inferred axioms into a fresh empty ontology.
        OWLOntologyManager outputOntologyManager = OWLManager.createOWLOntologyManager();
        OWLOntology infOnt = outputOntologyManager.createOntology();
        Set<OWLAxiom> previousaxioms = ontology.getAxioms();
        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner,
                        gens);
        iog.fillOntology(outputOntologyManager, infOnt);
		
		Set<OWLAxiom> newaxioms = infOnt.getAxioms();
		
		
		newaxioms.removeAll(previousaxioms);
		System.out.println(newaxioms.size() + " axioms inferred.");
		
		Set<OWLAxiom> realaxioms = new HashSet<OWLAxiom>();
		
		List<OWLAxiom> failedJustAxioms = new ArrayList<OWLAxiom>();
		
		ManchesterOWLSyntaxOWLObjectRendererImpl rendering = new ManchesterOWLSyntaxOWLObjectRendererImpl();
		ShortFormProvider shortFormProvider = new DefaultPrefixManager();
		// ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
		rendering.setShortFormProvider(shortFormProvider);
		
		// check for the presence of trivial axioms
		for(OWLAxiom prem : newaxioms){
			if (prem instanceof OWLSubClassOfAxiom 
					&& ((OWLSubClassOfAxiom) prem).getSubClass().equals(((OWLSubClassOfAxiom) prem).getSuperClass())
				|| (prem instanceof OWLSubClassOfAxiom) && ((OWLSubClassOfAxiom) prem).getSuperClass().isOWLThing()	
				|| (prem instanceof OWLSubClassOfAxiom) && ((OWLSubClassOfAxiom) prem).getSubClass().isOWLNothing()	
				// exclude troublemakers
					){
						// System.out.println(rendering.render(prem));
						// trivial
					} else {
						if (previousaxioms.contains(prem)){
							// trivial
						} else{
							realaxioms.add(prem);
						}
					}
		}
		
		OWLDataFactory dataFactory=manager.getOWLDataFactory();
		
		System.out.println("REALAXIOMS SIZE " + realaxioms.size());
		
		
			
			Set<OWLAxiom> equivaxioms = new HashSet<OWLAxiom>();
			
			for(OWLAxiom prem : newaxioms){
				if (prem instanceof OWLEquivalentClassesAxiom){
					System.out.println("EQUIV INFERRED! " + prem);
					List<OWLClassExpression> exprs = ((OWLEquivalentClassesAxiom) prem).getClassExpressionsAsList();
					for (int i = 0; i<exprs.size();i++){
						for (int j =i+1; j<exprs.size();j++){
							OWLAxiom ax1 = dataFactory.getOWLSubClassOfAxiom(exprs.get(i),exprs.get(j));
							OWLAxiom ax2 = dataFactory.getOWLSubClassOfAxiom(exprs.get(j),exprs.get(i));
							System.out.println("Construct " + ax1);
							System.out.println("Construct " + ax2);
							boolean contained1 = false;
							boolean contained2 = false;
							for (OWLAxiom check : realaxioms){
								// System.out.println(" comparing " + check + " with " + ax1 + " and " + ax2);
								if (check.equals(ax1))
									contained1= true;
								if (check.equals(ax2))
										contained2= true;
							} // end loop for check
							if (!contained1
									&& !((ax1 instanceof OWLSubClassOfAxiom) && ((OWLSubClassOfAxiom) ax1).getSuperClass().isOWLThing())	
									&& !((ax1 instanceof OWLSubClassOfAxiom) && ((OWLSubClassOfAxiom) ax1).getSubClass().isOWLNothing())	
									){
								equivaxioms.add(ax1);
								System.out.println("not contained " + ax1);
							} else{
								System.out.println("contained " + ax1);
							}
							if (!contained2
									&& !((ax2 instanceof OWLSubClassOfAxiom) && ((OWLSubClassOfAxiom) ax2).getSuperClass().isOWLThing())	
									&& !((ax2 instanceof OWLSubClassOfAxiom) && ((OWLSubClassOfAxiom) ax2).getSubClass().isOWLNothing())	
									){
								System.out.println("not contained " + ax2);
								equivaxioms.add(ax2);
							} else{
								System.out.println("contained " + ax2);
							}
						}
						
					}
				}
			}
			
			
			if (equivsonly){
				realaxioms = equivaxioms;
			} else{
				realaxioms.addAll(equivaxioms);
			}
		
		System.out.println(realaxioms.size() + " nontrivial axioms inferred.");
	
	
		
		System.out.println(realaxioms.size() + " nontrivial axioms sampled.");
		
		BlackBoxExplanation bBexplanator=new BlackBoxExplanation(ontology, reasonerFactory,reasoner);
	    HSTExplanationGenerator explanationGenerator=new HSTExplanationGenerator(bBexplanator);
	   
	    
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
	    List<String> verbalizations = new ArrayList<String>();
	    List<OWLAxiom> unprovenConclusions = new ArrayList<OWLAxiom>();
	    List<Set<OWLAxiom>> justsForUnprovenConclusions = new ArrayList<Set<OWLAxiom>>();
	    
	    int remainingAxioms = realaxioms.size();
	    
	    for(OWLAxiom ax: realaxioms){
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
	    		System.out.println("OBTAINING JUSTS for " + conclstring);
	    		// getting *1* explanation
	    		long juststarttime = System.currentTimeMillis();
	    		// Set<Set<OWLAxiom>> explanations = explanationGenerator.getExplanations(dataFactory.getOWLObjectIntersectionOf(subAx.getSubClass(), subAx.getSuperClass().getObjectComplementOf()),1);
	    		Set<OWLAxiom> explanation = explanationGenerator.getExplanation(dataFactory.getOWLObjectIntersectionOf(subAx.getSubClass(), subAx.getSuperClass().getObjectComplementOf()));
	    		long justendtime = System.currentTimeMillis();
	    		justTimes.add(justendtime-juststarttime);
	    		System.out.println("DONE OBTAINING JUSTS");
	    		// List<Set<OWLAxiom>> explanationsList = new ArrayList<Set<OWLAxiom>>(explanations);	
	    		if (explanation!=null && explanation.size()>0){					
	    			boolean subpropofcontained = false;
	    			boolean unioncontained = false;
	    			boolean disjcontained = false;
	    			boolean unhandledPremise = false;
	    			Sequent<OWLObject> sequent = new Sequent<OWLObject>();
	    			
	    			for (OWLAxiom prem: explanation){
	    				if (prem instanceof OWLSubClassOfAxiom)
	    					prem = removeAndThing((OWLSubClassOfAxiom) prem);
	    				if (prem instanceof OWLEquivalentClassesAxiom)
	    					prem = removeAndThing((OWLEquivalentClassesAxiom) prem);
	    				if (prem.accept(ppvisitor)==null)
	    					unhandledPremise = true;
	    				try {
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
						// sequent.addSuccedent(ax);
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
	    			
	    			nontrivCounter +=1;
	    		
	    			
	    			ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition> node = new ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>(sequent,null,0);
	    			ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String, SequentPosition>> prooftree = new ProofTree<ProofNode<Sequent<OWLObject>,java.lang.String,SequentPosition>>(node);
		          
	    			List<SequentInferenceRule> allInferenceRules = RuleSetManager.INSTANCE.getRuleSet("EL");
	    		 
	    			sequent.setHighestInitAxiomid(1000);
	    			
	    			long starttime = System.currentTimeMillis();
	    			
	    			 			
	    			// dumping!
	    			try {
						writer.write("CONCLUSION\n");
						writer.write(ax.toString() + "\n");
						writer.write("JUSTS\n");
						for (OWLAxiom just: explanation){
							writer.write(just.toString()+ "\n");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    		}
	    		else{
	    			failedJustAxioms.add(ax);
	    			System.out.println("Explanations " + explanation);
	    			System.out.println("justification finding failed!");
	 
		    		
	    			
	    		}
	    	} else {
	    		// System.out.println(ax);
	    	}
	    }// end loop
	    System.out.println("nontriv " + nontrivCounter);
 	    
	    int avgJustTime = avgInt(justTimes);
	    for (OWLAxiom failedjust : failedJustAxioms){
	    	try {
				writer.write("FAILED\n");
				writer.write(failedjust.toString() + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } 
	  
	    try {
			writer.write("STATS\n");
			writer.write("Generated: " + nontrivCounter + "\n");
			writer.write("Failed: " + failedJustAxioms.size() + "\n");
			writer.write("Avg time: " + avgJustTime + "\n");
			writer.write("Total time: " + (avgJustTime * nontrivCounter / 1000) + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return;
		
	    
	} 

	public static void main(String[] args) throws IOException{
		// Read list of files with EL ontologies
		
		System.out.println(args[0]);
		System.out.println(args[1]);
		System.out.println(args[2]);
		System.out.println(args[3]);
		System.out.println(args[4]);
		// System.out.println(args[5]);
		// System.out.println(args[6]);
		
		String order  = args[0];
		String stem = args[1];
		String outstem = args[2];
		String reasoner_select = args[3];
		String singlefile = args[4];
		
		
		Path path = Paths.get(order);
		
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
			// outlog = args[1] + singlefile + "-" + reasoner_select; 
			
		}
		
		
		for(String line: lines){
		try {
			
			
			// create output file
			Path outpath = Paths.get(outstem + reasoner_select + "-" + line + ".dump");
			// Path outpath = Paths.get("/Users/marvin/marvin_work_ulm/resources/ontologies/verblog.txt");
			File outFile = new File(outpath.toString());
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			// empty file
			PrintWriter pw = new PrintWriter(outpath.toString());
			pw.close();
			
			System.out.println("LOADING ONTOLOGY  " + line);
			
			// Statistic stats = runOntology("/Users/marvin/marvin_work_ulm/resources/ontologies/ore2015_pool_sample/el/pool/" + line);
			
			FileWriter writer = new FileWriter(outFile.toString(), true);
			runOntology(stem + line,reasoner_select,writer,false);
			
			
			writer.close();
			
		
			// runOntology("/Users/marvin/marvin_work_ulm/notes/langgen-paper/ontologies/flu.owl");
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 } //for
		
	
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
