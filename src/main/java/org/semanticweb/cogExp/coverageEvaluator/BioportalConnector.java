package org.semanticweb.cogExp.coverageEvaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
		// DatabaseManager.INSTANCE.connect();
		
		String storagelocation = args[0];
		
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
			
			// if (ontology.get("acronym").asText().contains("AURA")) // <--- obtaining the ontology takes very long
			// 	continue;
			
			ontNames.add(ontology.get("name").asText() + "\n" + ontology.get("@id").asText() + "\n\n");

			System.out.println(ontology.get("acronym").asText());
			
			// System.out.println(ontologyString.length());
			
			try{  // "/Users/marvin/marvin_work_ulm/resources/ontologies/bio/"
			Path ontologyOut = Paths.get(storagelocation + "/" +ontology.get("acronym").asText() + ".owl");
			File ontologyOutFile = new File(ontologyOut.toString());
			if(!ontologyOutFile.exists()){
				
				ontologyOutFile.createNewFile();
				FileWriter ontologyOutWriter = new FileWriter(ontologyOutFile, true);
				// ontologyOutWriter.write(ontologyString);
				
				System.out.println("[getting ontology]");
				String ontologyString = getWrite(REST_URL + "/ontologies/" + ontology.get("acronym").asText() + "/download?download_format=rdf" ,ontologyOutWriter);
				System.out.println("[gotten ontology]");
				
				
				ontologyOutWriter.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			// CoverageStoreEvaluatorCompressionDB.runBioportalOntology(ontologyString);
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
			
			int i = 0;
			
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
				i++;
				if (i%1000==0){
					System.out.println(i);
				}
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String getWrite(String urlToGet, FileWriter writer) {
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
			
			int i = 0;
			
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				writer.write(line);
				// result += line;
				i++;
				if (i%1000==0){
					System.out.println(i);
				}
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
