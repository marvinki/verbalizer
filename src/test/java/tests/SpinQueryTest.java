package tests;

import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.semanticweb.cogExp.ProofBasedExplanation.SpinQuery;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.inference.SPINExplanations;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.JenaUtil;



// $ "kennedys:ArnoldSchwarzenegger ?r ?x";


public class SpinQueryTest {

	public static void main(String[] args){
	
	 // Initialize system functions and templates
    SPINModuleRegistry.get().init();
    // Load main file
    Model baseModel = ModelFactory.createDefaultModel();
    // works: baseModel.read("http://topbraid.org/examples/kennedysSPIN");
   //  works: baseModel.read("file:///Users/marvin/kennedysSPIN.owl");
   baseModel.read("file:///Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-domain.owl");
    // Create OntModel with imports
    OntModel model = JenaUtil.createOntologyModel(OntModelSpec.OWL_MEM,
            baseModel);
	
    // INFERENCES
    Model newTriples = ModelFactory.createDefaultModel();
    model.addSubModel(newTriples);
    newTriples.setNsPrefixes(baseModel.getNsPrefixMap()); // Improve readability of newTriples.write(System.out, "N3")
    // Register locally defined functions
    SPINModuleRegistry.get().registerAll(model, null);
    // Run all inferences
    SPINExplanations explain = new SPINExplanations();
    SPINInferences.run(model, newTriples, explain, null, false, null);
    
    
	// Query query = ARQFactory.get().createQuery(model,"PREFIX diy:<http://www.semanticweb.org/diy-domain#> SELECT ?c ?r ?master WHERE{ ?c ?r ?master}");
    
    // Find all saws
    // Query query = ARQFactory.get().createQuery(model,"PREFIX diy:<http://www.semanticweb.org/diy-domain#> SELECT ?dev ?s WHERE{ ?dev rdf:type :PSR18Li2}");
    
 // Find all drill drivers
    Query query = ARQFactory.get().createQuery(newTriples,"PREFIX diy:<http://www.semanticweb.org/diy-domain#> " +
    										"SELECT ?dev ?r ?s WHERE{ ?devclass rdfs:subClassOf :DrillDriver. "
    										+ "						  ?dev rdf:type ?devclass}");
    
   //  Query query = ARQFactory.get().createQuery(model,"PREFIX domain: <http://www.semanticweb.org/diy-domain#> SELECT ?master WHERE{ ?config domain:master ?master}");
   
    // works: Query query = ARQFactory.get().createQuery(model,"PREFIX kennedys:<http://topbraid.org/examples/kennedys#> SELECT ?x WHERE{kennedys:ArnoldSchwarzenegger ?r ?x }");
    QueryExecution qe = ARQFactory.get().createQueryExecution(query, model);
    ResultSet results = qe.execSelect();
    ResultSetFormatter.out(System.out, results, query);
    qe.close();
    
    List<String> instances =  SpinQuery.getInstancesOfClass("file:///Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-domain.owl", "Tool");
    // SpinQuery.getInstancesOfClass("file:///Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-domain.owl", "Tool");
    
    
	}
}
