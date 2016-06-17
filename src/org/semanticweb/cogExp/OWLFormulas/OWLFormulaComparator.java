package org.semanticweb.cogExp.OWLFormulas;

import java.util.Comparator;

public class OWLFormulaComparator implements Comparator<OWLFormula>{

	@Override
	public int compare(OWLFormula o1, OWLFormula o2) {
		return o1.toString().compareTo(o2.toString());
	}
	
}
