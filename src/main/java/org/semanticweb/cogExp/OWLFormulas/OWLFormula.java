package org.semanticweb.cogExp.OWLFormulas;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.LinkedList;

import org.semanticweb.cogExp.FormulaConverter.ConversionManager;
import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.cogExp.core.Pair;


public class OWLFormula {
	private OWLAtom head;
	private List<OWLFormula> tail = null; // new ArrayList<OWLFormula>();
	// private ConvertOWLObject convertVisitor = new ConvertOWLObject();
	
	public OWLFormula(OWLAtom head, List<OWLFormula> tail){
		this.head = head;
		this.tail = tail;	
	}
	
	public OWLFormula(OWLAtom head){
		this.head = head;
	}
	
	public OWLAtom getHead(){
		return head;
	}
	
	public List<OWLFormula> getArgs(){
		return tail;
	}
	
	/** transform formula to a list of its atoms in breath first order
	 * 
	 * @return atoms of formula in breath first order
	 */
	public LinkedList<OWLAtom> getBFLinearisation(){
		// initialise Queue as Helper Structure
		LinkedList<OWLFormula> queue = new LinkedList<OWLFormula>();
		LinkedList<OWLAtom> accumulator = new LinkedList<OWLAtom>();
		if (head == null && (tail == null || tail.size()==0)){
			return accumulator;
		}
		queue.add(this);
		this.computeBFLinearisation(queue, accumulator);
		return accumulator;
	}
	
	private void computeBFLinearisation(LinkedList<OWLFormula> queue, LinkedList<OWLAtom> accumulator){
		// leaf
		if (queue.size()==0 || queue==null){
			return;
		}
		// pop node from queue
		OWLFormula currentf = queue.remove();
		// recursively add children to queue, left-to-right
		if (currentf.tail != null){
			for (OWLFormula f : currentf.tail){
				queue.add(f);
			}
		}
		// put head into results
		accumulator.add(currentf.head);
		// recursion
		computeBFLinearisation(queue, accumulator);
		return;
	}
	
	
	
	@Override
	public String toString() {
		String result = "";
		if (head!=null)
			result += head.toString();
		if (tail != null){
			result += " (" + tail.toString() + ")" ;
		}
		return result; 
	}
	
	
	@Override
	public OWLFormula clone(){
		ArrayList<OWLFormula> newtail = new ArrayList<OWLFormula>();
		if (tail !=null ){
			for (OWLFormula f : tail){
				OWLFormula newformula = f.clone();
				newtail.add(newformula);
			}
			return new OWLFormula(head, newtail);
		}
		return new OWLFormula(head);
	}
	
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof OWLFormula)){
			return false;
		} else{
			OWLFormula other = (OWLFormula) o;
			if (tail==null || tail.size()==0){
				if (other.tail==null || other.tail.size()==0){
					return other.head.equals(this.head);
				} else{
					return false;
				}
			} else{
				boolean equaltail = true;
				if (other.tail==null || other.tail.size()!=tail.size()){
					return false;
				}
				if (this.head.equals(OWLSymb.INT)
			       || this.head.equals(OWLSymb.UNION)
			       || this.head.equals(OWLSymb.EQUIV)){
					// this should only be relevant for formulas where subformulas are commutative (e.g. int, union)
					if (this.tail.size()!=other.tail.size()){equaltail = false; return false;}
					if (!other.tail.containsAll(this.tail)){equaltail = false; return false;}
					if (!this.tail.containsAll(other.tail)){equaltail = false; return false;}
				}
				else {
					if (!this.tail.equals(other.tail)){
						equaltail = false;
						return false;
					}
				}
			return other.head.equals(this.head) && equaltail;
			}
		}
	}
	
	 @Override
	    public int hashCode() {
	        int hash = 1;
	        hash = hash * 17 + this.head.hashCode();
	        if (tail!=null){
	        	hash = hash * 31 + this.tail.hashCode();
	        // for (OWLFormula form : this.tail){
	        // 	hash = hash * 31 + form.hashCode();
	        // }
	        }
	        return hash;
	    }
	 
	 
	
	public static List<Pair<OWLFormula,OWLFormula>> mergeSubmatchers(List<List<Pair<OWLFormula,OWLFormula>>> submatchers) throws Exception{
		ArrayList<Pair<OWLFormula,OWLFormula>> matcher = new ArrayList<Pair<OWLFormula,OWLFormula>>();
		for (List<Pair<OWLFormula,OWLFormula>> submatcher : submatchers){	
			// for all formulas in the submatcher, check compatibility and add
			for (Pair<OWLFormula,OWLFormula> pair : submatcher){
				boolean already_contained = false;
				for (Pair<OWLFormula,OWLFormula> finalpair : matcher){
					if (pair.t.equals(finalpair.t) && !pair.u.equals(finalpair.u)){
						throw new Exception("formulas do not match");
					}
					// catch the case where one pair is only vars
					if (pair.t.head instanceof OWLVar 
								& pair.u.head instanceof OWLVar 
								& finalpair.t.head instanceof OWLVar){
						if (finalpair.t.head.equals(pair.t.head)){
							matcher.add(new Pair<OWLFormula, OWLFormula>(pair.u, finalpair.u));
						}
						if (finalpair.t.head.equals(pair.u.head)){
							matcher.add(new Pair<OWLFormula, OWLFormula>(pair.t, finalpair.u));
						}
					}
					if (pair.t.head instanceof OWLVar 
							& pair.u.head instanceof OWLVar 
							& finalpair.u.head instanceof OWLVar){
					if (finalpair.u.head.equals(pair.t.head)){
						matcher.add(new Pair<OWLFormula, OWLFormula>(pair.u, finalpair.t));
					}
					if (finalpair.u.head.equals(pair.u.head)){
						matcher.add(new Pair<OWLFormula, OWLFormula>(pair.t, finalpair.t));
					} 
					}// end the case of pairs
					// default case where pair is new
					if (pair.t.equals(finalpair.t)){
						already_contained = true;
					}
				} // end finalpair loop
				if (!already_contained){ 
					matcher.add(new Pair<OWLFormula, OWLFormula>(pair.t, pair.u));
				}
			} // end submatcher loop
		} // end tail index loop
		return matcher;
	}
	
	// the "other" formula needs to be more general (more vars)
	public List<Pair<OWLFormula,OWLFormula>> match(OWLFormula otherformula) throws Exception{
		// System.out.println("trying to match " + this + " and " + otherformula);
		// ArrayList<Pair<OWLFormula,OWLFormula>> matcher = new ArrayList<Pair<OWLFormula,OWLFormula>>();
		// if we are at a variable, we construct matcher
		if (otherformula.head instanceof OWLVar){		
			LinkedList<Pair<OWLFormula,OWLFormula>> matcher = new LinkedList<Pair<OWLFormula,OWLFormula>>();
			Pair<OWLFormula,OWLFormula> matcherpair = new Pair<OWLFormula,OWLFormula>(otherformula, this);
			matcher.add(matcherpair);
			return matcher;
		}
		// if we are at a role variable, likewise, we construct matcher
		if (otherformula.head instanceof OWLRoleVar){
			        
					LinkedList<Pair<OWLFormula,OWLFormula>> matcher = new LinkedList<Pair<OWLFormula,OWLFormula>>();
					
					Pair<OWLFormula,OWLFormula> matcherpair = new Pair<OWLFormula,OWLFormula>(otherformula, this);
					matcher.add(matcherpair);
					
					return matcher;
				}
		// catch all cases where formulas obviously don't match
		if (!this.head.equals(otherformula.head)){
			throw new Exception("formulas do not match!");
		}
		// now the heads match, are the matchers for the subformulas compatible?
		LinkedList<List<Pair<OWLFormula,OWLFormula>>> submatchers = new LinkedList<List<Pair<OWLFormula,OWLFormula>>>();
		if (tail!=null){
			if (this.tail.size()!=otherformula.tail.size())
				throw new Exception("formulas do not match");
			for (int i=0; i<this.tail.size(); i++){
				List<Pair<OWLFormula,OWLFormula>> submatcher = this.tail.get(i).match(otherformula.tail.get(i));
				submatchers.add(submatcher);
			}
		}	
		return mergeSubmatchers(submatchers);		
	}
	
	public static List<Pair<OWLFormula,OWLFormula>> match(List<OWLFormula> formulas,  List<OWLFormula> otherformulas) throws Exception{
		if(formulas.size()!=otherformulas.size()){
			throw new Exception ("error with using matching");}
		LinkedList<List<Pair<OWLFormula,OWLFormula>>> submatchers = new LinkedList<List<Pair<OWLFormula,OWLFormula>>>();
		for (int i = 0; i<formulas.size();i++){
			List<Pair<OWLFormula,OWLFormula>> submatcher = formulas.get(i).match(otherformulas.get(i));
			submatchers.add(submatcher);
		}
		return mergeSubmatchers(submatchers);	
	}
	
	public OWLFormula applyMatcher(List<Pair<OWLFormula,OWLFormula>> matcher){
		if (this.head instanceof OWLVar){
			for(Pair<OWLFormula,OWLFormula> pair : matcher){
				if (pair.t.head.equals(this.head)){
					return pair.u;
				}
			}
			return this;
		} // done with the case were we are at a var
		if (this.head instanceof OWLRoleVar){
			for(Pair<OWLFormula,OWLFormula> pair : matcher){
				if (pair.t.head.equals(this.head)){
					return pair.u;
				}
			}
			return this;
		} // done with the case were we are at a role var
		if (tail == null && tail.size()==0){
			// if we're at a simple formula, just copy its contents
			return new OWLFormula(this.head);
		}
		ArrayList<OWLFormula> taillist = new ArrayList<OWLFormula>();
		// need to apply matcher recursively
		for (OWLFormula formula : tail){
			taillist.add(formula.applyMatcher(matcher));
		}
		return new OWLFormula(this.head,taillist);
	}
	
	public static List<Pair<OWLFormula,OWLFormula>> getMatcher2Ary(OWLFormula candidate1, OWLFormula candidate2, OWLFormula prem1, OWLFormula prem2) throws Exception{
		List<OWLFormula> pattern = new ArrayList<OWLFormula>();
		pattern.add(prem1);
		pattern.add(prem2);
		List<OWLFormula> pattern2 = new ArrayList<OWLFormula>();
		pattern2.add(candidate1);
		pattern2.add(candidate2);
		List<Pair<OWLFormula,OWLFormula>> matcher = OWLFormula.match(pattern2, pattern);
		return matcher;
	}
	
	/** Creates an OWLFormula that represents a single class variable
	 * 
	 * @param name		the name of the variable
	 * @return TODO add description
	 */
	public static OWLFormula createFormulaVar(String name){
		OWLVar varname = new OWLVar(name);
		return new OWLFormula(varname);
	}
	
	/** Creates an OWLFormula that represents a role variable
	 * 
	 * @param name		the name of the role variable
	 * @return add description
	 */
	public static OWLFormula createFormulaRoleVar(String name){
		OWLRoleVar varname = new OWLRoleVar(name);
		return new OWLFormula(varname);
	}
	
	
	/** creates an OWLFormula representing a class (from name and uri)
	 * 
	 * @param name		class name
	 * @param uri		uri (full, including class name)
	 * @return add description
	 */
	public static OWLFormula createFormulaClass(String name, String uri){
		OWLClassName classname = new OWLClassName(name,uri);
		return new OWLFormula(classname);
	}
	
	/** creates an OWLFormula representing a role (from name and uri)
	 * 
	 * @param name	role name
	 * @param uri	uri (full, including role name)
	 * @return add description
	 */
	public static OWLFormula createFormulaRole(String name, String uri){
		OWLRoleName rolename = new OWLRoleName(name,uri);
		return new OWLFormula(rolename);
	}
	
	public static OWLFormula createFormulaDataProperty(String name, String uri){
		OWLDataPropertyName rolename = new OWLDataPropertyName(name,uri);
		return new OWLFormula(rolename);
	}
	
	public static OWLFormula createFormulaObjectProperty(String name, String uri){
		OWLObjectPropertyName rolename = new OWLObjectPropertyName(name,uri);
		return new OWLFormula(rolename);
	}
	
	public static OWLFormula createFormula(OWLAtom head, OWLFormula ... tail){
		ArrayList<OWLFormula> taillist = new ArrayList<OWLFormula>(Arrays.asList(tail));
		return new OWLFormula(head,taillist);
	}
	
	public static OWLFormula createFormula(OWLAtom head, List<OWLFormula> tail){
		return new OWLFormula(head,tail);
	}
	
	public static OWLFormula createFormulaIndividual(String name, String iri){
		OWLIndividualName iname = new OWLIndividualName(name,iri);
		return new OWLFormula(iname);
	}
	
	public static OWLFormula createFormulaTop(){
		return new OWLFormula(OWLSymb.TOP);
	}
	
	public static OWLFormula createFormulaBot(){
		return new OWLFormula(OWLSymb.BOT);
	}
	
	public static OWLFormula createFormulaSubclassOf(OWLFormula sub, OWLFormula sup){
		ArrayList<OWLFormula> taillist = new ArrayList<OWLFormula>();
		taillist.add(sub);
		taillist.add(sup);
		return new OWLFormula(OWLSymb.SUBCL,taillist);
	}
	
	public static OWLFormula createFormulaExists(OWLFormula role, OWLFormula filler){
		ArrayList<OWLFormula> taillist = new ArrayList<OWLFormula>();
		taillist.add(role);
		taillist.add(filler);
		return new OWLFormula(OWLSymb.EXISTS,taillist);
	}
	
	public static OWLFormula createFormulaDataProperty(String propertyname, String propertyuri, String value, OWLLiteralType type){
		OWLFormula prop = createFormulaDataProperty(propertyname,propertyuri);
		ArrayList<OWLFormula> taillist = new ArrayList<OWLFormula>();
		OWLLiteralValue lit = new OWLLiteralValue(value,type);
		OWLFormula litformula = new OWLFormula(lit);
		taillist.add(prop);
		taillist.add(litformula);
		return new OWLFormula(OWLSymb.DATAHASVALUE,taillist);
	}
	
	public static OWLFormula createFormulaObjectProperty(String propertyname, String propertyuri, String indiv, String indiviri){
		OWLFormula prop = createFormulaObjectProperty(propertyname,propertyuri);
		OWLFormula individ = createFormulaIndividual(indiv,indiviri);
		ArrayList<OWLFormula> taillist = new ArrayList<OWLFormula>();
		taillist.add(prop);
		taillist.add(individ);
		return new OWLFormula(OWLSymb.OBJECTHASVALUE,taillist);
	}
	
	public static OWLFormula createFormulaDataRan(OWLDataRan ran){
		return new OWLFormula(ran);
	}
	
	public static OWLFormula createFormulaClassAssertion(OWLFormula classexpression, OWLFormula indiv){
		ArrayList<OWLFormula> taillist = new ArrayList<OWLFormula>();
		taillist.add(classexpression);
		taillist.add(indiv);
		return new OWLFormula(OWLSymb.CLASSASSERTION, taillist);
	}
	
	public static OWLFormula createFormulaObjectPropertyAssertion(OWLFormula propertyexpression, OWLFormula indivSub, OWLFormula indivObj){
		ArrayList<OWLFormula> taillist = new ArrayList<OWLFormula>();
		taillist.add(propertyexpression);
		taillist.add(indivSub);
		taillist.add(indivObj);
		return new OWLFormula(OWLSymb.OBJECTPROPERTYASSERTION, taillist);
	}
	
	public static OWLFormula createFormulaDataPropertyAssertion(OWLFormula propertyexpression, OWLFormula indivSub, OWLFormula litObj){
		ArrayList<OWLFormula> taillist = new ArrayList<OWLFormula>();
		taillist.add(propertyexpression);
		taillist.add(indivSub);
		taillist.add(litObj);
		return new OWLFormula(OWLSymb.DATAPROPERTYASSERTION, taillist);
	}
	
	public boolean containsSubformula(OWLFormula subformula){
		if (this.equals(subformula)){
			return true;
		} 
		if (this.getArgs()!=null && this.getArgs().size()>0){
			for (OWLFormula arg : this.getArgs()){
				if (arg.containsSubformula(subformula)){
					return true;
				}
			}
			return false; // if we get here, there was nothing to be found.
		}
		return false; // if we get here, there was nothing to be found.
	}
	
	public boolean isClassFormula(){
		return (head instanceof OWLClassName);
	}
	
	public boolean isClassExpression(){
		return (head instanceof OWLClassName)
		    || (head.equals(OWLSymb.INT))
		    || (head.equals(OWLSymb.EXISTS))
		    || (head.equals(OWLSymb.FORALL))
		    || (head.equals(OWLSymb.NEG))
		    || (head.equals(OWLSymb.UNION))
		    || (head.equals(OWLSymb.DATAONEOF))
		    || (head.equals(OWLSymb.ONEOF))
				;
	}
	
	public boolean isTop(){
		return (head.equals(OWLSymb.TOP));
	}
	
	public boolean isBot(){
		return (head.equals(OWLSymb.BOT));
	}
	
	public static boolean containsSubformula(OWLFormula exp, OWLFormula subformula){
		if (subformula.equals(exp)) return true;
		List<OWLFormula> subforms = exp.getArgs();
		if (subforms !=null && subforms.size()>0){
			for (OWLFormula arg: subforms){
				if (containsSubformula(arg,subformula))
					return true;
				}
		}
		return false;
	}
	
	public List<OWLFormula> getConjuncts(){
		List<OWLFormula> result = new ArrayList<OWLFormula>();
		if (head.equals(OWLSymb.INT)){
			for (OWLFormula arg : tail){
				result.addAll(arg.getConjuncts());
			}
		} else 
		{
			result.add(this);
		}
		return result;
	}
	
	public String prettyPrint(){
		return VerbalisationManager.prettyPrint(ConversionManager.toOWLAPI(this));
	}
	
	

	
}
