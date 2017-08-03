package org.semanticweb.cogExp.coverageEvaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.semanticweb.owlapi.model.OWLOntologyManager;

public class BioportalConnector {

	static final String REST_URL = "http://data.bioontology.org";
	static final String API_KEY = "25490678-2ee2-47dc-85dc-71151cbb4516";
	static final ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {
		
		// Connect to own mysql database
		DatabaseManager.INSTANCE.connect();
		
		// Get the available resources
		String resourcesString = get(REST_URL + "/");
		JsonNode resources = jsonToNode(resourcesString);

		// Follow the ontologies link by looking for the media type in the list
		// of links
		String link = resources.get("links").findValue("ontologies").asText();

		// Get the ontologies from the link we found
		JsonNode ontologies = jsonToNode(get(link));

		// Get the name and ontology id from the returned list
		List<String> ontNames = new ArrayList<String>();
		for (JsonNode ontology : ontologies) {
			// donies
			if (ontology.get("acronym").asText().contains("ICO"))
				continue;
			if (ontology.get("acronym").asText().contains("TEO"))
				continue;
			if (ontology.get("acronym").asText().contains("TMO"))
				continue;
			if (ontology.get("acronym").asText().contains("HEIO"))
				continue;
			if (ontology.get("acronym").asText().contains("IDO"))
				continue;
			if (ontology.get("acronym").asText().contains("FB-CV"))
				continue;
			if (ontology.get("acronym").asText().contains("SIO"))
				continue;
			if (ontology.get("acronym").asText().contains("BIRNLEX"))
				continue;
			if (ontology.get("acronym").asText().contains("HIVO0004"))
				continue;
			if (ontology.get("acronym").asText().contains("RPO"))
				continue;
			if (ontology.get("acronym").asText().contains("CHEAR"))
				continue;
			if (ontology.get("acronym").asText().contains("NCRO"))
				continue;
			
			
			if (ontology.get("acronym").asText().contains("MATRCOMPOUND"))
				continue;
			if (ontology.get("acronym").asText().contains("EGO"))
				continue;
			if (ontology.get("acronym").asText().contains("NIFDYS"))
				continue;
			if (ontology.get("acronym").asText().contains("RCTV2"))
				continue;
			if (ontology.get("acronym").asText().contains("ONTOAD")) // <--- simply takes long, try this again with more time!
				continue;
			if (ontology.get("acronym").asText().contains("PSIMOD")) // <---  Ontology already exists. OntologyID(OntologyIRI(<http://purl.obolibrary.org/obo/TEMP>) VersionIRI(<null>))
				continue;
			if (ontology.get("acronym").asText().contains("PW")) // <---  Ontology already exists. OntologyID(OntologyIRI(<http://purl.obolibrary.org/obo/TEMP>) VersionIRI(<null>))
				continue;
			if (ontology.get("acronym").asText().contains("BIPON")) // <--- simply takes long, try this again with more time!
				continue;
			if (ontology.get("acronym").asText().contains("OGSF")) // <--- could not load imported ontology
				continue;
			if (ontology.get("acronym").asText().contains("OBOREL")) // <---  Ontology already exists. OntologyID(OntologyIRI(<http://purl.obolibrary.org/obo/TEMP>) VersionIRI(<null>))
				continue;
			if (ontology.get("acronym").asText().contains("MOOCCIADO")) // <---  tmp
				continue;
			if (ontology.get("acronym").asText().contains("AURA")) // <--- obtaining the ontology takes very long
				continue;
			
			ontNames.add(ontology.get("name").asText() + "\n" + ontology.get("@id").asText() + "\n\n");

			System.out.println(ontology.get("acronym").asText());
			System.out.println("[getting ontology]");
			String ontologyString = get(REST_URL + "/ontologies/" + ontology.get("acronym").asText() + "/download");
			System.out.println("[gotten ontology]");
			// System.out.println(ontologyString.length());
			
			try{
			Path ontologyOut = Paths.get("/Users/marvin/marvin_work_ulm/resources/ontologies/bio/" +ontology.get("acronym").asText() + ".owl");
			File ontologyOutFile = new File(ontologyOut.toString());
			if(!ontologyOutFile.exists()){
				ontologyOutFile.createNewFile();
				FileWriter ontologyOutWriter = new FileWriter(ontologyOutFile, true);
				ontologyOutWriter.write(ontologyString);
				ontologyOutWriter.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			CoverageStoreEvaluatorCompressionDB.runBioportalOntology(ontologyString);
		}

		// Print the names and ids
		for (String ontName : ontNames) {
			System.out.println(ontName);
		}
	}

	private static JsonNode jsonToNode(String json) {
		JsonNode root = null;
		try {
			root = mapper.readTree(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	private static String get(String urlToGet) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			url = new URL(urlToGet);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "apikey token=" + API_KEY);
			conn.setRequestProperty("Accept", "application/json");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
