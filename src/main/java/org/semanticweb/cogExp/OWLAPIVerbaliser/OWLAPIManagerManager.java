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

package org.semanticweb.cogExp.OWLAPIVerbaliser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.semanticweb.cogExp.core.SequentInferenceRule;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.functional.parser.OWLFunctionalSyntaxOWLParser;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public enum OWLAPIManagerManager {

	INSTANCE;
	
	private final OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
	private final OWLDataFactory dataFactory=ontologyManager.getOWLDataFactory();
	private final OWLFunctionalSyntaxOWLParser functionalSyntaxParser = new OWLFunctionalSyntaxOWLParser();

	
	public OWLOntologyManager getOntologyManager(){
		return ontologyManager;
	} 
	
	public OWLDataFactory getDataFactory(){
		return dataFactory;
	}
	
	public OWLFunctionalSyntaxOWLParser getFunctionalSyntaxParser(){
		return functionalSyntaxParser;
	}
	

}
