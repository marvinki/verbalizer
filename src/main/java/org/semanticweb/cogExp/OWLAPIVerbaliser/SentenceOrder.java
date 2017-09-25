/**
 * 
 */
package org.semanticweb.cogExp.OWLAPIVerbaliser;

/**
 * @author fpaffrath
 * self explanatory
 */
public enum SentenceOrder {

	is_A_B,
	A_is_B,
	A_B_is, 
	noOrder;
	
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){
		switch (this) {
		case is_A_B:
			return "is_A_B";
			
		case A_is_B:
			return "A_is_B";

			
		case A_B_is:
			return "A_B_is";

		case noOrder:
			return "noOrder";
			
		default:
			return "default";
			
		}
	}
	
}
