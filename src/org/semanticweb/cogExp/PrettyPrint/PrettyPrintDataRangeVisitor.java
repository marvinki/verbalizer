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
