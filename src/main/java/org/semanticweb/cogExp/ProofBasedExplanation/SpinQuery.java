package org.semanticweb.cogExp.ProofBasedExplanation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.LocationMapper;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.inference.SPINExplanations;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.system.SPINImports;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.JenaUtil;

public class SpinQuery {

	// "file:///Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-domain.owl"
	public static List<String> getInstancesOfClass(String ontologyfile, String classname){
	
		List result = new ArrayList<String>();
		
	// Initialize system functions and templates
    SPINModuleRegistry.get().init();
    // Load main file
    Model baseModel = ModelFactory.createDefaultModel();
    
   
    
    System.out.println(ontologyfile);
    LocationMapper locMap = new LocationMapper();
    locMap.addAltEntry("http://www.semanticweb.org/diy-domain", "file:///Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-domain.owl");
   
    FileManager fileManager = FileManager.get();
    fileManager.setLocationMapper(locMap);
    
    // FileManager fileManager = new FileManager(locMap);
    // fileManager.addLocatorClassLoader(SpinQuery.class.getClassLoader()); 
    // fileManager.addLocatorClassLoader(SpinQuery.class.getClassLoader());
    baseModel = fileManager.loadModel(ontologyfile); 
    // baseModel = fileManager.loadModel("http://www.semanticweb.org/diy-domain"); 
   
    
    
    /*
    try {
		baseModel = SPINImports.get().getImportsModel(baseModel);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
    
    // baseModel.write(System.out);
    
    // baseModel = fileManager.readModel(baseModel, "/Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-instructions.owl");
    
    // baseModel.read("file:///Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-instructions.owl");
    // baseModel.read(ontologyfile);
    OntModel model = JenaUtil.createOntologyModel(OntModelSpec.OWL_MEM,
            baseModel);
   
	
   
    
    // model.write(System.out);
    
    
   model.setDynamicImports(true);
   OntDocumentManager docMan = model.getDocumentManager();
   docMan.setFileManager(fileManager);
   docMan.setProcessImports(true);
   docMan.loadImports(model);
  
  
   
   model.write(System.out); 
   
    // INFERENCES
    Model newTriples = ModelFactory.createDefaultModel();
    model.addSubModel(newTriples);
    newTriples.setNsPrefixes(model.getNsPrefixMap()); 
   //  newTriples.setNsPrefixes(baseModel.getNsPrefixMap()); 
    // Register locally defined functions
    SPINModuleRegistry.get().registerAll(model, null);
    // Run all inferences
    SPINExplanations explain = new SPINExplanations();
    SPINInferences.run(model, newTriples, explain, null, false, null);
    
    // Find all drill drivers
    Query query = ARQFactory.get().createQuery(newTriples,"PREFIX diy:<http://www.semanticweb.org/diy-domain#> " +
    										"SELECT DISTINCT ?dev WHERE{ ?devclass rdfs:subClassOf* :" + classname + ". "
    										+ "						  ?dev rdf:type ?devclass}");
    
  
    QueryExecution qe = ARQFactory.get().createQueryExecution(query, model);
    ResultSet results = qe.execSelect();
    while(results.hasNext()){
    	QuerySolution sol = results.next();
 		Resource dev = (Resource) sol.get("dev");
 		System.out.println(dev.toString());
 		result.add(dev.toString());
    	// Literal lit = sol.getLiteral("dev");
    	// String str = lit.getString();
    	// System.out.println("str: " + str);
    }
    // ResultSetFormatter.out(System.out, results, query);
    qe.close();
	
	return result;
	}
	
}
