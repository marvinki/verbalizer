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

import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataRangeVisitorEx;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;

/** Pretty Printing for OWLDataRange Expressions (OWL-API) -- currently just applies toString()
 * 
 * @author marvin
 *
 */
public class PrettyPrintDataRangeVisitor implements OWLDataRangeVisitorEx<String>{

	    private PrettyPrintPropertyExpressionVisitor ppPropVisit = new PrettyPrintPropertyExpressionVisitor();

	    public String visit(OWLDataRange arg0) {
			// TODO Auto-generated method stub
			return arg0.toString();
		}
	    
		public String visit(OWLDatatype arg0) {
			// TODO Auto-generated method stub
			return arg0.toString();
		}

		public String visit(OWLDataOneOf arg0) {
			// TODO Auto-generated method stub
			return arg0.toString();
		}

		public String visit(OWLDataComplementOf arg0) {
			// TODO Auto-generated method stub
			return arg0.toString();
		}

		public String visit(OWLDataIntersectionOf arg0) {
			// TODO Auto-generated method stub
			return arg0.toString();
		}

		public String visit(OWLDataUnionOf arg0) {
			// TODO Auto-generated method stub
			return arg0.toString();
		}

		public String visit(OWLDatatypeRestriction arg0) {
			// TODO Auto-generated method stub
			return arg0.toString();
		}
		
     
	
}
