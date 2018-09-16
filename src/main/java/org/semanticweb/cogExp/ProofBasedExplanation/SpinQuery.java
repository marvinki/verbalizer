/*
 *     Copyright 2012-2018 Ulm University, AI Institute
 *     Main author: Marvin Schiller, contributors: Felix Paffrath, Chunhui Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.semanticweb.cogExp.ProofBasedExplanation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ext.com.google.common.graph.Graph;
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
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
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
		System.out.println("ontologyfile " + ontologyfile + " classname "  + classname);
	
		List result = new ArrayList<String>();
		
	// Initialize system functions and templates
    SPINModuleRegistry.get().init();
    // Load main file
    Model baseModel = ModelFactory.createDefaultModel();
    
   
    
    // System.out.println(ontologyfile);
    LocationMapper locMap = new LocationMapper();
    // locMap.addAltEntry("http://www.semanticweb.org/diy-domain", "file:///Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-domain.owl");
   
    // FileManager fileManager = FileManager.get();
    // fileManager.setLocationMapper(locMap);
    
    // FileManager fileManager = new FileManager(locMap);
    // fileManager.addLocatorClassLoader(SpinQuery.class.getClassLoader()); 
    // fileManager.addLocatorClassLoader(SpinQuery.class.getClassLoader());
    // baseModel = fileManager.loadModel(ontologyfile); 
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
   
    // OntModel model = JenaUtil.createOntologyModel(OntModelSpec.OWL_MEM,
    //          baseModel);
   
    OntModel model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
   
    
    // model.write(System.out);
    
    
   // model.setDynamicImports(true);
   // OntDocumentManager docMan = model.getDocumentManager();
  //  docMan.setFileManager(fileManager);
   // docMan.setProcessImports(true);
  //  docMan.loadImports(model);
   
  
   OntDocumentManager dm = model.getDocumentManager();
   // dm.addAltEntry( "http://www.semanticweb.org/diy-domain",
// 	   "file:///Users/marvin/work/ki-ulm-repository/miscellaneous/Bosch/Modelle/Ontologien/diy-domain.owl" );
   
   File inputfile = new File(ontologyfile);
  // System.out.println("inputfile " + inputfile.getAbsoluteFile());
  try {
	System.out.println("inputfile " + inputfile.getCanonicalPath());
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
  
   
  String inputfileAbsolute = "";
try {
	inputfileAbsolute = inputfile.getCanonicalPath().toString();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
  
   String filetobeopened = inputfileAbsolute .substring(0,inputfileAbsolute.length()-20) + "diy-domain.owl";
   
   System.out.println("Attempting to load " + filetobeopened);
   
   dm.addAltEntry( "http://www.semanticweb.org/diy-domain",
		    filetobeopened);
		   // "file://" + filetobeopened);
		   
  //  model.read("http://www.semanticweb.org/diy-domain" );
   model.setDynamicImports(true);
   
   InputStream in = FileManager.get().open(ontologyfile);
   
   System.out.println("before read");
   model.read(in,null);
   // model.read(ontologyfile);
   System.out.println("after read");
   
// list the statements in the Model
StmtIterator iter = model.listStatements();

// print out the predicate, subject and object of each statement
/*
while (iter.hasNext()) {
    Statement stmt      = iter.nextStatement();  // get next statement
    Resource  subject   = stmt.getSubject();     // get the subject
    Property  predicate = stmt.getPredicate();   // get the predicate
    RDFNode   object    = stmt.getObject();      // get the object

    System.out.print(subject.toString());
    System.out.print(" " + predicate.toString() + " ");
    if (object instanceof Resource) {
       System.out.print(object.toString());
    } else {
        // object is a literal
        System.out.print(" \"" + object.toString() + "\"");
    }

    System.out.println(" .");
} 
*/
   
   // model.read("http://www.semanticweb.org/diy-domain");
   List<org.apache.jena.graph.Graph> subgraphs =  model.getSubGraphs();
   for (org.apache.jena.graph.Graph gr : subgraphs){
	   System.out.println(gr.size());
   }
   
   // model.write(System.out); 
   
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
    										"SELECT DISTINCT ?dev WHERE{ ?devclass rdfs:subClassOf* diy:" + classname + ". "
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
