package org.semanticweb.cogExp.coverageEvaluator;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.WordNetQuery;

import edu.smu.tspell.wordnet.SynsetType;

public class ReadDB {

	public static void main(String[] args){
		
		/*
		String strfoo = "123";
		System.out.println(strfoo.substring(0,1));
		System.out.println(VerbalisationManager.INSTANCE.treatCamelCaseAndUnderscores("foo"));
		System.out.println(VerbalisationManager.INSTANCE.treatCamelCaseAndUnderscores("foo bar "));
		System.out.println(VerbalisationManager.INSTANCE.treatCamelCaseAndUnderscores("GARBL_bar "));
		System.out.println(VerbalisationManager.INSTANCE.treatCamelCaseAndUnderscores("fooDetect"));
		System.out.println(VerbalisationManager.INSTANCE.treatCamelCaseAndUnderscores("fooLiFoo"));
		System.out.println(VerbalisationManager.INSTANCE.treatCamelCaseAndUnderscores("Snail-Trail"));
		*/
		
		DatabaseManager.INSTANCE.connect("localhost");
		
		/*
		List<List<String>> results = 
				DatabaseManager.INSTANCE.getExplanationsInclusive(""Ontology-1.2.owl");
		
		for (List<String> line : results){
			for (String item : line){
				System.out.print(item + " ");
			}
			System.out.println();
		}
		*/
		// /Users/marvin/marvin_work_ulm/resources/ontologies/ore2015_pool_sample/el/pool/ore_ont_10175.owl
		// String resultline = DatabaseManager.INSTANCE.constructStatisticLineFromDB(""Ontology-biochemistry-complex.owl");
		// String resultline = DatabaseManager.INSTANCE.constructStatisticLineFromDB("/Users/marvin/marvin_work_ulm/resources/ontologies/ore2015_pool_sample/el/pool/ore_ont_10242.owl");
		// System.out.println(resultline);
		
		
		/*
		List<String> ontologies = Arrays.asList(
				"Ontology-1.2.owl",
				"Ontology-adolena.owl",
				"Ontology-AirSystem.owl",
				"Ontology-amino-acid.owl",
				"Ontology-atom-common.owl",
				"Ontology-biochemistry-complex.owl",
				"Ontology-biopax-level2.owl",
				"Ontology-brokenPizza.owl",
				"Ontology-BuildingsAndPlaces.owl",
				"Ontology-chemical.owl",
				"Ontology-CMT-CONFTOOL.owl",
				"Ontology-CMT-CONFTOOL1.owl",
				"Ontology-CONFTOOL-EKAW1.owl",
				"Ontology-CONFTOOL-EKAW2.owl",
				"Ontology-CONFTOOL-EKAW.owl",
				"Ontology-CRS-CMT.owl",
				"Ontology-CRS-CONFTOOL1.owl",
				"Ontology-CRS-CONFTOOL.owl",
				"Ontology-CRS-EKAW.owl",
				"Ontology-CRS-PCS1.owl",
				"Ontology-CRS-PCS.owl",
				"Ontology-CRS-SIGKDD.owl",
				"Ontology-cton.owl",
				"Ontology-data.owl",
				"Ontology-DOLCE_Lite_397.owl",
				"Ontology-download1.owl",
				"Ontology-earthrealm.owl",
				"Ontology-eukariotic.owl",
				"Ontology-expression.owl",
				"Ontology-foodswap.owl",
				"Ontology-galen1.owl",
				"Ontology-galen.owl",
				"Ontology-generations-minus-same-individual-axioms.owl",
				"Ontology-generations.owl",
				"Ontology-goslim.owl",
				"Ontology-GRO.owl",
				"Ontology-heart.owl",
				"Ontology-Hydrology.owl",
				"Ontology-koala.owl",
				"Ontology-legal-action.owl",
				"Ontology-MGEDOntology.owl",
				"Ontology-molecular_function_xp_chebi.obo.owl",
				"Ontology-molecule-complex.owl",
				"Ontology-Movie.owl",
				"Ontology-mygrid-moby-service.owl",
				"Ontology-mygrid-unclassified.owl",
				"Ontology-nautilus.owl",
				"Ontology-norm.owl",
				"Ontology-numerics.owl",
				"Ontology-Ontology1191594278.owl",
				"Ontology-ontology1.owl",
				"Ontology-opengalen-no-propchains.owl",
				"Ontology-organic-compound-complex.owl",
				"Ontology-organic-functional-group-complex.owl",
				"Ontology-particle.owl",
				"Ontology-PCS-CONFTOOL.owl",
				"Ontology-PCS-EKAW.owl",
				"Ontology-people.owl",
				"Ontology-periodic-table-complex.owl",
				"Ontology-phenomena.owl",
				"Ontology-physics-complex.owl",
				"Ontology-pizza.owl",
				"Ontology-policyContainmentTest.owl",
				"Ontology-process1.owl",
				"Ontology-process2.owl",
				"Ontology-property-complex.owl",
				"Ontology-property.owl",
				"Ontology-reaction.owl",
				"Ontology-ribosome.owl",
				"Ontology-SIGKDD-EKAW.owl",
				"Ontology-software.owl",
				"Ontology-software-ontology.owl",
				"Ontology-so-xp.obo.owl",
				"Ontology-spatial.obo.owl",
				"Ontology-subatomic-particle-complex.owl",
				"Ontology-substance1.owl",
				"Ontology-substance.owl",
				"Ontology-tambis-patched.owl",
				"Ontology-Thesaurus.07.12e.owl",
				"Ontology-Thesaurus.08.02d.owl",
				"Ontology-Thesaurus.08.08d.owl",
				"Ontology-Thesaurus.owl",
				"Ontology-UnsatCook.owl",
				"Ontology-time1.owl",
				"Ontology-time-modification.owl",
				"Ontology-units.owl",
				"Ontology-units1.owl",
				"Ontology-univ-bench.owl",
				"Ontology-university.owl",
				"Ontology-unnamed.owl",
				"Ontology-worm_phenotype_xp.obo.owl",
				"Ontology-yowl-complex.owl" 
				);
				*/
		
		List<String> ontologies = Arrays.asList(
				"ACGT-MO.owl",
				"ADALAB-META.owl",
				"ADALAB.owl",
				"ADAR.owl",
				"ADO.owl",
				"AERO.owl",
				"APOLLO-SV.owl",
				"AURA.owl",
				"BAO.owl",
				"BHO.owl"
				// "BIOMO.owl"
				);
		
		for (String ontology : ontologies){
			String resline = DatabaseManager.INSTANCE.constructStatisticLineFromDB(
					 // "/Users/marvin/marvin_work_ulm/resources/ontologies/bio/" + ontology, "bioexplanations");
					 "/gdisk/ki/home/mschiller/data/bioportal/" + ontology, "bioexplanations");
					// "/Users/marvin/work/workspace/justifications/originaltones-ontologies/" + ontology);
			System.out.println(ontology + "," + resline);
		}
		
		/*
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
		
		System.out.println(VerbalisationManager.aOrAnIfy("large"));
		System.out.println(SynsetType.NOUN.getCode());
		System.out.println(SynsetType.VERB.getCode());
		System.out.println(SynsetType.ADJECTIVE.getCode());
		System.out.println(SynsetType.ADVERB.getCode());
		System.out.println(SynsetType.ADJECTIVE_SATELLITE.getCode());
		
		System.out.println(VerbalisationManager.aOrAnIfy("heart"));
		System.out.println(VerbalisationManager.aOrAnIfy("male"));
		System.out.println(VerbalisationManager.aOrAnIfy("large"));
		System.out.println(VerbalisationManager.aOrAnIfy("snow"));
		System.out.println(VerbalisationManager.aOrAnIfy("has"));
		System.out.println(VerbalisationManager.aOrAnIfy("unit"));
		System.out.println(VerbalisationManager.aOrAnIfy("house"));
		System.out.println(VerbalisationManager.aOrAnIfy("grinding"));
		
		System.out.println("arms plural? " + WordNetQuery.INSTANCE.isPlural("arms"));
		System.out.println("arm plural? " + WordNetQuery.INSTANCE.isPlural("arm"));
		System.out.println("muscles plural? " + WordNetQuery.INSTANCE.isPlural("muscles"));
		System.out.println("muscle plural? " + WordNetQuery.INSTANCE.isPlural("muscle"));
		*/
		
	}
	
	
}
