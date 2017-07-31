package org.semanticweb.cogExp.PrettyPrint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

/** Pretty Printer for OWLClassExpressions (OWL-API)
 * 
 * @author marvin
 *
 */
public class PrettyPrintClassExpressionVisitor implements OWLClassExpressionVisitorEx<String>{

	    private PrettyPrintPropertyExpressionVisitor ppPropVisit = new PrettyPrintPropertyExpressionVisitor();
	    private PrettyPrintDataRangeVisitor ppDataRangeVisit = new PrettyPrintDataRangeVisitor();
	
		public String visit(OWLObjectIntersectionOf arg0) {
			String resultstring = "";
			boolean firstp = true;
			resultstring = resultstring + "(";
			for (OWLClassExpression exp: arg0.getOperandsAsList()){
				if (!firstp){ resultstring = resultstring + PrettyPrintOWLAxiomVisitor.INTSYMB;}
				firstp = false;
				resultstring = resultstring + exp.accept(this);
			}
			resultstring = resultstring + ")"; 
			return resultstring;
		}
		
		public String visit(OWLObjectUnionOf arg0) {
			String resultstring = "";
			boolean firstp = true;
			resultstring = resultstring + "(";
			for (OWLClassExpression exp: arg0.getOperandsAsList()){
				if (!firstp){ resultstring = resultstring + PrettyPrintOWLAxiomVisitor.UNIONSYMB;}
				firstp = false;
				resultstring = resultstring + exp.accept(this);
			}
			resultstring = resultstring + ")"; 
			return resultstring;
		}

		
		public String visit(OWLObjectComplementOf arg0) {
			return PrettyPrintOWLAxiomVisitor.INTSYMB + "(" + arg0.getOperand().accept(this) + ")";
		}

		public String visit(OWLObjectSomeValuesFrom svf) {
			String resultstring = PrettyPrintOWLAxiomVisitor.EXISTSSYMB
					+ svf.getProperty().accept(ppPropVisit) // .visit(svf.getProperty()) 
					+ "." + svf.getFiller().accept(this);
			return resultstring;
		}
		
		
		public String visit(OWLObjectAllValuesFrom arg0) {
			String resultstring = PrettyPrintOWLAxiomVisitor.FORALLSYMB
					+ arg0.getProperty().accept(ppPropVisit) 
					+ "." + arg0.getFiller().accept(this);
			return resultstring;
		}

		public String visit(OWLObjectHasValue arg0) {
			return "hasValue" + "(" 
					+ ppPropVisit.visit(arg0.getProperty()) 
					+ "," + arg0.getValue() + ")";
		}

		public String visit(OWLObjectMinCardinality arg0) {
			String resultstring =  "min#" 
					+ "(" 
					+ arg0.getCardinality() // returns an int
					+ "," 
					+ ppPropVisit.visit(arg0.getProperty())
					+ "," 
					+  arg0.getFiller().accept(this) 
					+")";
			return resultstring;
		}

		public String visit(OWLObjectExactCardinality arg0) {
			String resultstring =  "=" 
					+ "(" 
					+ arg0.getCardinality() // returns an int
					+ "," 
					+ ppPropVisit.visit(arg0.getProperty())
					+ "," 
					+  arg0.getFiller().accept(this) 
					+")";
			return resultstring;
		}

		public String visit(OWLObjectMaxCardinality arg0) {
			String resultstring =  "max#" 
					+ "(" 
					+ arg0.getCardinality() // returns an int
					+ "," 
					+ ppPropVisit.visit(arg0.getProperty())
					+ "," 
					+  arg0.getFiller().accept(this) 
					+")";
			return resultstring;
		}

		public String visit(OWLObjectHasSelf arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public String visit(OWLObjectOneOf arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public String visit(OWLDataSomeValuesFrom arg0) {
			// TODO Auto-generated method stub
			OWLDataPropertyExpression propExpr = arg0.getProperty();
			return propExpr.accept(ppPropVisit) + ": " + arg0.getFiller().accept(ppDataRangeVisit);
		}

		public String visit(OWLDataAllValuesFrom arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		
		public String visit(OWLDataHasValue arg0) {
			return "hasValue" + "(" 
					+ ppPropVisit.visit(arg0.getProperty()) 
					+ "," + arg0.getValue() + ")";
		}

		public String visit(OWLDataMinCardinality arg0) {
			String resultstring =  "min#" 
									+ "(" 
									+ arg0.getCardinality() // returns an int
									+ "," 
									+ ppPropVisit.visit(arg0.getProperty())
									+ "," 
									+  ppDataRangeVisit.visit(arg0.getFiller()) 
									+")";
			return resultstring;
		}

		public String visit(OWLDataExactCardinality arg0) {
			String resultstring =  "=" 
					+ "(" 
					+ arg0.getCardinality() // returns an int
					+ "," 
					+ ppPropVisit.visit(arg0.getProperty())
					+ "," 
					+ ppDataRangeVisit.visit(arg0.getFiller()) 
					+")";
			return resultstring;
		}

		public String visit(OWLDataMaxCardinality arg0) {
			String resultstring =  "max#" 
					+ "(" 
					+ arg0.getCardinality()
					+ "," 
					+ ppPropVisit.visit(arg0.getProperty())
					+ "," 
					+  ppDataRangeVisit.visit(arg0.getFiller()) 
					+")";
			return resultstring;
		}

		public String visit(OWLClass arg0) {
			// System.out.println(arg0.toString());
			String fragment = arg0.getIRI().getFragment();
			if (arg0.toString().contains("/") && arg0.toString().contains("#")){
				// System.out.println("DEBUG " + arg0.toString());
				Pattern p = Pattern.compile("#(.*?)>");
				Matcher m = p.matcher(arg0.toString());
				boolean b = m.find();
				// System.out.println("Matcher " + m.group(1));
				if (!b) return fragment;
				return m.group(1);
			}
				
			// System.out.println("pretty print visiting : " + arg0);
			// System.out.println("Short form: " + arg0.getIRI().getScheme());
			return fragment;
		}
	
}
