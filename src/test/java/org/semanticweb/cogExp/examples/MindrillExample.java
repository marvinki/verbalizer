package org.semanticweb.cogExp.examples;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.semanticweb.cogExp.GentzenTree.GentzenTree;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbaliseTreeManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;
import org.semanticweb.cogExp.ProofBasedExplanation.ProofBasedExplanationService;
import org.semanticweb.cogExp.ProofBasedExplanation.WordnetTmpdirManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.jfact.JFactFactory;

public class MindrillExample {




		public static void main(String[] args) throws OWLOntologyCreationException {
			/**
			 * this is the very smallest example one can think of
			 * 
			 */
			
			
			String tmpdir = "";
			try {
				tmpdir = WordnetTmpdirManager.makeTmpdir();
			} catch (IOException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
			WordNetQuery.INSTANCE.setDict(tmpdir);
			
			OWLReasonerFactory reasonerFactory = new JFactFactory();

			File ontologyfile = new java.io.File("/home/fpaffrath/svn/Bosch-intern/ontologies/mindrill04.owl");
			
			OWLOntology boschOntology = 
					OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(ontologyfile);	
			OWLReasoner reasoner = reasonerFactory.createReasoner(boschOntology);
			
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			

			Locale locale = Locale.GERMAN;
			VerbaliseTreeManager.setLocale(locale);
			VerbaliseTreeManager.setLogicLabels(ResourceBundle.getBundle("LogicLabels", locale));
			
			System.out.println("Classes : ");
			
			
			String a = "Drill";
			String b = "MaterialsAndTools";//"MaterialsAndTools";
			String result = getResult(a, b, ontologyfile.getPath(), reasonerFactory, reasoner );
			
			System.out.println(result);
			
			
			OWLReasonerConfiguration config = new SimpleConfiguration(50000);
			OWLReasoner jfact = reasonerFactory.createReasoner(boschOntology,config);
		
			
			OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
			String ontologyuri = "http://www.semanticweb.org/powertools#";
		  	
			/*
			OWLClass correctlyDrillingInWood = dataFactory.getOWLClass(IRI.create(ontologyuri + "CorrectlyDrillingInWood"));
		  	OWLNamedIndividual drillingEvent1 = dataFactory.getOWLNamedIndividual(IRI.create(ontologyuri + "drillingEvent1"));
		  	OWLClassAssertionAxiom classAxiom = dataFactory.getOWLClassAssertionAxiom(correctlyDrillingInWood, drillingEvent1);
			*/
			
//			OWLClass drillingInWoodWithTooMuchSpeed = dataFactory.getOWLClass(IRI.create(ontologyuri + "DrillingInWoodWithRotationalSpeedAbove1000Umin"));
//		  	OWLNamedIndividual drillingEvent2 = dataFactory.getOWLNamedIndividual(IRI.create(ontologyuri + "drillingEvent2"));
//		  	OWLClassAssertionAxiom classAxiom = dataFactory.getOWLClassAssertionAxiom(drillingInWoodWithTooMuchSpeed, drillingEvent2);
			
			OWLClass drillingInWoodWithTooMuchSpeed = dataFactory.getOWLClass(IRI.create(ontologyuri + "ActivitiesUsingWoodDrillingBits"));
		  	OWLNamedIndividual drillingEvent2 = dataFactory.getOWLNamedIndividual(IRI.create(ontologyuri + "drilling-config-1"));
		  	OWLClassAssertionAxiom classAxiom = dataFactory.getOWLClassAssertionAxiom(drillingInWoodWithTooMuchSpeed, drillingEvent2);
			
		  	System.out.println("Individuals: ");
			String resulttext = ProofBasedExplanationService.getExplanationResult(classAxiom, jfact, reasonerFactory,  boschOntology, false, 1000, 50000, "OP", false, false);
			System.out.println(resulttext);
	
			
			
		}
		
		
		
		
		
		
		

		private static String getResult(String a, String b, String path, OWLReasonerFactory reasonerFactory,
				OWLReasoner reasoner) {
		
			GentzenTree tree = ProofBasedExplanationService.computeTree(a, 
					b, 
					path, 
					reasonerFactory, 
					reasoner);
			
//			System.out.println("\n");
			String result = VerbaliseTreeManager.verbaliseNL(tree, false, false, null);
			return (result);
			
		}


	}