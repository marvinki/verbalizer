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

package org.semanticweb.cogExp.PrettyPrint;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLPropertyRange;

public class PrettyPrintPropertyExpressionVisitor implements OWLPropertyExpressionVisitorEx<String>{

	public String visit(OWLProperty arg0) {
		return visit(arg0);
	}
	
	public String visit(OWLPropertyRange arg0) {
		return "na1";
	}
	
	public String visit(OWLPropertyExpression arg0) {
		return "na2";
	}
	
	public String visit(OWLObjectPropertyExpression arg0) {
		return arg0.getNamedProperty().getIRI().getFragment();
	}
	
	public String visit(OWLDataPropertyExpression arg0) {
		OWLDataProperty dataprop = arg0.asOWLDataProperty();
		String frag = dataprop.getIRI().getFragment();
		return frag;
	}
	
	public String visit(OWLObjectProperty arg0) {
		return arg0.getIRI().getFragment();
	}

	public String visit(OWLObjectInverseOf arg0) {
		return arg0.getInverseProperty().accept(this) + "^-1";
	}

	public String visit(OWLDataProperty arg0) {
		return arg0.getIRI().getFragment();
	}

	public String visit(OWLAnnotationProperty arg0) {
		// TODO Auto-generated method stub
		return null;
	}

		
       
	
}
