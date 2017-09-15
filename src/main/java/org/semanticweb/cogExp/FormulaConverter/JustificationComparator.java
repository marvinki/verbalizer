package org.semanticweb.cogExp.FormulaConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class JustificationComparator {
	
	
	public static boolean classExpressionReplacementEqual(OWLClassExpression ce1, OWLClassExpression ce2, HashMap<OWLClassExpression, OWLClassExpression> hashsetClasses, HashMap<OWLObjectPropertyExpression, OWLObjectPropertyExpression> hashsetProperty){
		// System.out.println("call with " + ce1 + " AND " + ce2);	
		if (!ce1.getClassExpressionType().equals(ce2.getClassExpressionType()))
			return false;
		if (ce1.isOWLThing() && ce2.isOWLThing())
			return true;
		if (ce1.isOWLNothing() && ce2.isOWLNothing())
			return true;
		if (ce1 instanceof OWLClass && ce2 instanceof OWLClass){
			// System.out.println("checking " + hashsetClasses.get((OWLClass) ce1) + " AND " + ce2);
			 if (hashsetClasses.get((OWLClass) ce1).equals(ce2)){
				//  System.out.println("classes match ");
				 return true;
			 }
			return false;
		}
		if (ce1 instanceof OWLSubClassOfAxiom && ce2 instanceof OWLSubClassOfAxiom){
			return 
					classExpressionReplacementEqual(((OWLSubClassOfAxiom) ce1).getSubClass(), 
							((OWLSubClassOfAxiom) ce2).getSubClass(), hashsetClasses, hashsetProperty)
					&& classExpressionReplacementEqual(((OWLSubClassOfAxiom) ce1).getSuperClass(), 
							((OWLSubClassOfAxiom) ce2).getSuperClass(), hashsetClasses, hashsetProperty);
		}
		if (ce1 instanceof OWLObjectSomeValuesFrom && ce2 instanceof OWLObjectSomeValuesFrom){
			if (!hashsetProperty.get(((OWLObjectSomeValuesFrom) ce1).getProperty()).equals(((OWLObjectSomeValuesFrom) ce2).getProperty()))
					return false;
			return 
					classExpressionReplacementEqual(((OWLObjectSomeValuesFrom) ce1).getFiller(), 
							((OWLObjectSomeValuesFrom) ce2).getFiller(), hashsetClasses, hashsetProperty);			
		}
		if (ce1 instanceof OWLObjectAllValuesFrom && ce2 instanceof OWLObjectAllValuesFrom){
			if (!hashsetProperty.get(((OWLObjectAllValuesFrom) ce1).getProperty()).equals(((OWLObjectAllValuesFrom) ce2).getProperty()))
					return false;
			return 
					classExpressionReplacementEqual(((OWLObjectAllValuesFrom) ce1).getFiller(), 
							((OWLObjectAllValuesFrom) ce2).getFiller(), hashsetClasses, hashsetProperty);			
		}
		
		
		return false;
	}
	
	public static boolean axiomReplacementEqual(OWLSubClassOfAxiom ax1, OWLSubClassOfAxiom ax2, HashMap<OWLClassExpression, OWLClassExpression> hashsetClasses, HashMap<OWLObjectPropertyExpression, OWLObjectPropertyExpression> hashsetProperty){
		if (classExpressionReplacementEqual(ax1.getSubClass(), ax2.getSubClass(), hashsetClasses, hashsetProperty) 
				&& classExpressionReplacementEqual(ax1.getSuperClass(), ax2.getSuperClass(), hashsetClasses, hashsetProperty)) 
			return true;
		return false;
	}
	
	public static boolean axiomReplacementEqual(OWLObject ax1, OWLObject ax2, HashMap<OWLClassExpression, OWLClassExpression> hashsetClasses, HashMap<OWLObjectPropertyExpression, OWLObjectPropertyExpression> hashsetProperty){
		if (ax1 instanceof OWLSubClassOfAxiom && ax2 instanceof OWLSubClassOfAxiom)
			return axiomReplacementEqual((OWLSubClassOfAxiom) ax1, (OWLSubClassOfAxiom) ax2, hashsetClasses, hashsetProperty);
		if (ax1 instanceof OWLEquivalentClassesAxiom && ax2 instanceof OWLEquivalentClassesAxiom){
			List<OWLClassExpression> exprs1 = ((OWLEquivalentClassesAxiom) ax1).getClassExpressionsAsList();
			List<OWLClassExpression> exprs2 = ((OWLEquivalentClassesAxiom) ax2).getClassExpressionsAsList();
			if (exprs1.size()!=exprs2.size())
				return false;
			boolean equal = true;
			int index = 0;
			for (OWLClassExpression cl : exprs1){
				// System.out.println("checking if equal " + cl + " AND " + exprs1.get(index));
				if (!classExpressionReplacementEqual(cl,exprs2.get(index),hashsetClasses, hashsetProperty)){
					equal=false;
				}
				index++;
			}
			return equal;
		}
		if (ax1 instanceof OWLDisjointClassesAxiom && ax2 instanceof OWLDisjointClassesAxiom){
			List<OWLClassExpression> exprs1 = ((OWLDisjointClassesAxiom) ax1).getClassExpressionsAsList();
			List<OWLClassExpression> exprs2 = ((OWLDisjointClassesAxiom) ax2).getClassExpressionsAsList();
			if (exprs1.size()!=exprs2.size())
				return false;
			boolean equal = true;
			int index = 0;
			for (OWLClassExpression cl : exprs1){
				// System.out.println("checking if equal " + cl + " AND " + exprs1.get(index));
				if (!classExpressionReplacementEqual(cl,exprs2.get(index),hashsetClasses, hashsetProperty)){
					equal=false;
				}
				index++;
			}
			return equal;
		}
		// all other cases
		return false;
	}
	
	public static HashMap<OWLClass,OWLClass> setsReplacementEqual(Set<OWLObject> set1, Set<OWLObject> set2){
		if (set1.size()!=set2.size())
			return null;
		Set<HashMap<OWLClass,OWLClass>> generatedMappingsClasses = generateClassMappings(set1,set2);
		// System.out.println("generatedMappingsClasses " + generatedMappingsClasses);
		if (generatedMappingsClasses==null) 
			return null;
		Set<HashMap<OWLObjectProperty,OWLObjectProperty>> generatedMappingsProperties = generatePropertyMappings(set1,set2);
		HashMap winnerMapping = null;
		for (HashMap mapping1 : generatedMappingsClasses){
			for (HashMap mapping2 : generatedMappingsProperties){
				boolean allFound = true;
				for (OWLObject ax: set1){
					boolean found = false;
					for (OWLObject target : set2){
						// System.out.println("comparing "  + ax + " AND  " + target);
						
						if (axiomReplacementEqual(ax, target, mapping1, mapping2)){
							// System.out.println("yup");
							found = true;
							break;
						}
					}
					if (!found)
						allFound = false;
				}
			
				for (OWLObject target: set2){
					boolean found = false;
					for (OWLObject ax : set1){
						if (axiomReplacementEqual(ax, target, mapping1, mapping2)){
							found = true; 
							break;
						}
					}
					if (!found)
						allFound = false;
				}	
				
				if (allFound)
				winnerMapping = mapping1;
			}
		}
		return winnerMapping;
	}
	
	
	public static Set<HashMap<OWLClass,OWLClass>> generateClassMappings(Set<OWLObject> formulaset1, Set<OWLObject> formulaset2){
		Set<HashMap<OWLClass,OWLClass>> results = new HashSet<HashMap<OWLClass,OWLClass>>();
		
		// collect all class names from all input objects in the first set of OWL formulas
		Set<OWLClass> classes1 = new HashSet<OWLClass>();
		for (OWLObject candidate : formulaset1){
			if (candidate instanceof OWLClassExpression){
				OWLClassExpression ce = (OWLClassExpression) candidate;
				classes1.addAll(ce.getClassesInSignature());
			}
			if (candidate instanceof OWLAxiom){
				OWLAxiom ce = (OWLAxiom) candidate;
				classes1.addAll(ce.getClassesInSignature());
			}
		}
		
		// collect all class names from all input objects in the first set of OWL formulas
		Set<OWLClass> classes2 = new HashSet<OWLClass>();
		for (OWLObject candidate : formulaset2){
			if (candidate instanceof OWLClassExpression){
				OWLClassExpression ce = (OWLClassExpression) candidate;
				classes2.addAll(ce.getClassesInSignature());
			}
			if (candidate instanceof OWLAxiom){
				OWLAxiom ce = (OWLAxiom) candidate;
				classes2.addAll(ce.getClassesInSignature());
			}
		}
		
		// proceed only if the sheer number is identical
		if (classes1.size()==classes2.size()){
			List<Object> classes2List = new ArrayList<Object>();
			for (OWLClass cl : classes2){
				classes2List.add(cl);
			}
			
			List<List<Object>> result = generatePermutations(classes2List);
			for (List<Object> permutation : result){
				HashMap<OWLClass,OWLClass> mapping = new HashMap<OWLClass,OWLClass>();
				int index = 0;
				for (Object cl1 : classes1){
					mapping.put((OWLClass) cl1, (OWLClass) permutation.get(index));
					index++;
				}
				results.add(mapping);
			}
			return results;
		}
		
		// we have failed to produce a mapping
		return null;
	} 
	
	
	public static Set<HashMap<OWLObjectProperty,OWLObjectProperty>> generatePropertyMappings(Set<OWLObject> formulaset1, Set<OWLObject> formulaset2){
		Set<HashMap<OWLObjectProperty,OWLObjectProperty>> results = new HashSet<HashMap<OWLObjectProperty,OWLObjectProperty>>();
		
		// collect all class names from all input objects in the first set of OWL formulas
		Set<OWLObjectProperty> classes1 = new HashSet<OWLObjectProperty>();
		for (OWLObject candidate : formulaset1){
			if (candidate instanceof OWLClassExpression){
				OWLClassExpression ce = (OWLClassExpression) candidate;
				classes1.addAll(ce.getObjectPropertiesInSignature());
			}
			if (candidate instanceof OWLAxiom){
				OWLAxiom ce = (OWLAxiom) candidate;
				classes1.addAll(ce.getObjectPropertiesInSignature());
			}
		}
		
		// System.out.println(classes1.size());
		
		// collect all class names from all input objects in the first set of OWL formulas
		Set<OWLObjectProperty> classes2 = new HashSet<OWLObjectProperty>();
		for (OWLObject candidate : formulaset2){
			if (candidate instanceof OWLClassExpression){
				OWLClassExpression ce = (OWLClassExpression) candidate;
				classes2.addAll(ce.getObjectPropertiesInSignature());
			}
			if (candidate instanceof OWLAxiom){
				OWLAxiom ce = (OWLAxiom) candidate;
				classes2.addAll(ce.getObjectPropertiesInSignature());
			}
		}
		
		// proceed only if the sheer number is identical
		if (classes1.size()==classes2.size()){
			List<Object> classes2List = new ArrayList<Object>();
			for (OWLObjectProperty cl : classes2){
				classes2List.add(cl);
			}
			
			List<List<Object>> result = generatePermutations(classes2List);
			for (List<Object> permutation : result){
				HashMap<OWLObjectProperty,OWLObjectProperty> mapping = new HashMap<OWLObjectProperty,OWLObjectProperty>();
				int index = 0;
				for (Object cl1 : classes1){
					mapping.put((OWLObjectProperty) cl1, (OWLObjectProperty) permutation.get(index));
					index++;
				}
				results.add(mapping);
			}
			
			if(classes1.size()==0) {
				HashMap<OWLObjectProperty,OWLObjectProperty> idmap = new HashMap<OWLObjectProperty,OWLObjectProperty>();
				OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
			  	String ontologyuri = "http://empty#";
			  	OWLObjectProperty emptyprop  = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "emptyproperty"));
				idmap.put(emptyprop,emptyprop);
				results.add(idmap);
				// we have failed to produce a mapping
				System.out.println(results);
			}
			
			return results;
		}
		
		
		return results;
	} 
	
	public static List<List<Object>> generatePermutations(List<Object> remaining){
		List<List<Object>> result = new ArrayList<List<Object>>();
		
		if (remaining.size()<2 ){
			if (remaining.size()==1)
				result.add(remaining);
			return result;
		}
		
		for (Object obj : remaining){
				List<Object> newremaining = new ArrayList<Object>(remaining);
				newremaining.remove(obj);
				// recursion 
				List<List<Object>> recursiveResult = generatePermutations(newremaining);
				for (List<Object> newsequence : recursiveResult){
					newsequence.add(obj);
					result.add(newsequence);
				}
		}
		return result;
		
	}
	
	public static void main(String[] args){
		
		List<Object> str = new ArrayList<Object>();
		str.add("a");
		// str.add("b");
		// str.add("c");
		// str.add("d");
		// str.add("e");
		// str.add("f");
		// str.add("g");
		// str.add("h");
		// str.add("i");
		
		
		List<List<Object>> results = generatePermutations(str);
		for (List<Object> seq : results){
			System.out.println(seq.toString());
		}
		OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
	  	String ontologyuri = "http://www.semanticweb.org/dennis/ontologies/2013/9/complete-ontology-20#";
	  	OWLClass AClass  = dataFactory.getOWLClass(IRI.create(ontologyuri + "AClass"));
	 	OWLClass BClass = dataFactory.getOWLClass(IRI.create(ontologyuri + "BClass"));
	 	OWLClass CClass = dataFactory.getOWLClass(IRI.create(ontologyuri + "CClass"));
	 	OWLClass DClass = dataFactory.getOWLClass(IRI.create(ontologyuri + "DClass"));
	 	
	 	OWLObjectProperty Aprop  = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "Aprop"));
	 	OWLObjectProperty Bprop  = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "Bprop"));
	 	
	 	
	 	OWLClass aClass  = dataFactory.getOWLClass(IRI.create(ontologyuri + "aClass"));
	 	OWLClass bClass = dataFactory.getOWLClass(IRI.create(ontologyuri + "bClass"));
	 	OWLClass cClass = dataFactory.getOWLClass(IRI.create(ontologyuri + "cClass"));
	 	OWLClass dClass = dataFactory.getOWLClass(IRI.create(ontologyuri + "dClass"));
	 	
	 	OWLObjectProperty aprop  = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "aprop"));
	 	OWLObjectProperty bprop  = dataFactory.getOWLObjectProperty(IRI.create(ontologyuri + "bprop"));
	 	
		OWLSubClassOfAxiom subclax1 = dataFactory.getOWLSubClassOfAxiom(AClass,dataFactory.getOWLThing());
		OWLSubClassOfAxiom subclax2 = dataFactory.getOWLSubClassOfAxiom(BClass,dataFactory.getOWLObjectSomeValuesFrom(Aprop, DClass));
		OWLEquivalentClassesAxiom subclax3 = dataFactory.getOWLEquivalentClassesAxiom(CClass,DClass);
		OWLSubClassOfAxiom subclax4 = dataFactory.getOWLSubClassOfAxiom(BClass,dataFactory.getOWLObjectSomeValuesFrom(Aprop, DClass));
		OWLDisjointClassesAxiom disjax = dataFactory.getOWLDisjointClassesAxiom(AClass,BClass,CClass);
		
		OWLSubClassOfAxiom subclax1b = dataFactory.getOWLSubClassOfAxiom(aClass,dataFactory.getOWLThing());
		OWLSubClassOfAxiom subclax2b = dataFactory.getOWLSubClassOfAxiom(bClass,dataFactory.getOWLObjectSomeValuesFrom(aprop, dClass));
		OWLEquivalentClassesAxiom subclax3b = dataFactory.getOWLEquivalentClassesAxiom(cClass,dClass);
		OWLSubClassOfAxiom subclax4b = dataFactory.getOWLSubClassOfAxiom(bClass,dataFactory.getOWLObjectSomeValuesFrom(aprop, dClass));
		OWLDisjointClassesAxiom disjaxb = dataFactory.getOWLDisjointClassesAxiom(aClass,bClass,cClass);
		
		Set<OWLObject> axioms1 = new HashSet<OWLObject>();
		Set<OWLObject> axioms2 = new HashSet<OWLObject>();
		axioms1.add(subclax1);
		axioms1.add(subclax2);
		axioms1.add(subclax3);
		axioms1.add(subclax4);
		axioms1.add(disjax);
		
		
		axioms2.add(subclax3b);
		axioms2.add(subclax2b);
		axioms2.add(subclax1b);
		axioms2.add(subclax4b);
		axioms2.add(disjaxb);
		
		Set<HashMap<OWLClass,OWLClass>> mappings = generateClassMappings(axioms1,axioms2);
		
		for (HashMap map : mappings){
			System.out.println("MAPPING");
			for (Object cl : map.keySet()){
				System.out.println(" " + cl + map.get(cl));
			}
		}
		
		Set<HashMap<OWLObjectProperty,OWLObjectProperty>> mappings2 = generatePropertyMappings(axioms1,axioms2);
		
		for (HashMap map : mappings2){
			System.out.println("MAPPING");
			for (Object cl : map.keySet()){
				System.out.println(" " + cl + map.get(cl));
			}
		}
		
		HashMap<OWLClass,OWLClass> result = setsReplacementEqual(axioms1,axioms2);
		 for (Object cl : result.keySet()){
		 	System.out.println(" " + cl + result.get(cl));
		 }
		 
		 Set<OWLAxiom> originalSet1 = new HashSet<OWLAxiom>();
		 for (OWLObject ax : axioms1){
			 originalSet1.add((OWLAxiom) ax);
		 }
		 
		 Set<OWLAxiom> replaced = replaceClassnames(originalSet1,result);
		for (OWLAxiom rep : replaced){
			System.out.println(rep);
		}
		 
		
	}
	
	public static OWLClassExpression replaceClassnames(OWLClassExpression ce,HashMap<OWLClass,OWLClass> map){
		if (ce instanceof OWLClass)
			return replaceClassnames((OWLClass) ce, map);
		if (ce instanceof OWLObjectSomeValuesFrom)
			return replaceClassnames((OWLObjectSomeValuesFrom) ce, map);
		if (ce instanceof OWLObjectAllValuesFrom)
			return replaceClassnames((OWLObjectAllValuesFrom) ce, map);
		if (ce instanceof OWLObjectIntersectionOf)
			return replaceClassnames((OWLObjectIntersectionOf) ce, map);
		if (ce instanceof OWLObjectUnionOf)
			return replaceClassnames((OWLObjectUnionOf) ce, map);
		return ce;
	}
	
	public static OWLClassExpression replaceClassnames(OWLClass owlclass,HashMap<OWLClass,OWLClass> map){
		if (map.get(owlclass)!=null)
			return map.get(owlclass);
		return owlclass;
	}
	
	public static OWLClassExpression replaceClassnames(OWLObjectSomeValuesFrom somevaluesfrom,HashMap<OWLClass,OWLClass> map){
		if (map.get(somevaluesfrom.getFiller())!=null){
			OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
			somevaluesfrom = dataFactory.getOWLObjectSomeValuesFrom(somevaluesfrom.getProperty(),map.get(somevaluesfrom.getFiller()));
		}	
		return somevaluesfrom;
	}
	
	public static OWLClassExpression replaceClassnames(OWLObjectAllValuesFrom somevaluesfrom,HashMap<OWLClass,OWLClass> map){
		if (map.get(somevaluesfrom.getFiller())!=null){
			OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
			somevaluesfrom = dataFactory.getOWLObjectAllValuesFrom(somevaluesfrom.getProperty(),map.get(somevaluesfrom.getFiller()));
		}	
		return somevaluesfrom;
	}
	
	public static OWLClassExpression replaceClassnames(OWLObjectIntersectionOf intersection,HashMap<OWLClass,OWLClass> map){
		List<OWLClassExpression> operands = intersection.getOperandsAsList();
		Set<OWLClassExpression> newoperands = new HashSet<OWLClassExpression>();
		for (OWLClassExpression ce : operands){
			if (map.get(ce)!=null){
				newoperands.add(map.get(ce));
			} else{
				newoperands.add(ce);
			}
		}
		OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
		return dataFactory.getOWLObjectIntersectionOf(newoperands);
	}
	
	public static OWLClassExpression replaceClassnames(OWLObjectUnionOf intersection,HashMap<OWLClass,OWLClass> map){
		List<OWLClassExpression> operands = intersection.getOperandsAsList();
		Set<OWLClassExpression> newoperands = new HashSet<OWLClassExpression>();
		for (OWLClassExpression ce : operands){
			if (map.get(ce)!=null){
				newoperands.add(map.get(ce));
			} else{
				newoperands.add(ce);
			}
		}
		OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
		return dataFactory.getOWLObjectUnionOf(newoperands);
	}
	
	public static OWLAxiom replaceClassnamesInAxioms(OWLSubClassOfAxiom axiom,HashMap<OWLClass,OWLClass> map){
		OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
		OWLClassExpression sub = (OWLClassExpression) axiom.getSubClass();
		OWLClassExpression supercl = (OWLClassExpression) axiom.getSuperClass();
		OWLClassExpression subnew = replaceClassnames(sub, map);
		OWLClassExpression supernew = replaceClassnames(supercl, map);
		return dataFactory.getOWLSubClassOfAxiom(subnew,supernew);			
	}
	
	public static OWLAxiom replaceClassnamesInAxioms(OWLEquivalentClassesAxiom axiom,HashMap<OWLClass,OWLClass> map){
		OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
		List<OWLClassExpression> ces = axiom.getClassExpressionsAsList();
		Set<OWLClassExpression> newces = new HashSet<OWLClassExpression>();
		for (OWLClassExpression ce : ces){
			OWLClassExpression newce = replaceClassnames(ce, map);
			newces.add(newce);
		}
		return dataFactory.getOWLEquivalentClassesAxiom(newces);
	}
	
	public static OWLAxiom replaceClassnamesInAxioms(OWLDisjointClassesAxiom axiom,HashMap<OWLClass,OWLClass> map){
		OWLDataFactory dataFactory=OWLManager.createOWLOntologyManager().getOWLDataFactory();
		List<OWLClassExpression> ces = axiom.getClassExpressionsAsList();
		Set<OWLClassExpression> newces = new HashSet<OWLClassExpression>();
		for (OWLClassExpression ce : ces){
			OWLClassExpression newce = replaceClassnames(ce, map);
			newces.add(newce);
		}
		return dataFactory.getOWLDisjointClassesAxiom(newces);
	}
	
	public static OWLAxiom replaceClassnamesInAxioms(OWLAxiom axiom, HashMap<OWLClass,OWLClass> map){
		if (axiom instanceof OWLSubClassOfAxiom)
			return replaceClassnamesInAxioms ((OWLSubClassOfAxiom) axiom, map);
		if (axiom instanceof OWLDisjointClassesAxiom)
			return replaceClassnamesInAxioms ((OWLDisjointClassesAxiom) axiom, map);
		if (axiom instanceof OWLEquivalentClassesAxiom)
			return replaceClassnamesInAxioms ((OWLEquivalentClassesAxiom) axiom, map);
		return axiom;
	}
	
	public static Set<OWLAxiom> replaceClassnames(Set<OWLAxiom> axioms, HashMap<OWLClass,OWLClass> map){
		Set<OWLAxiom> newaxioms = new HashSet<OWLAxiom>();
		for (OWLAxiom ax : axioms){
			newaxioms.add(replaceClassnamesInAxioms(ax, map));
		}
		return newaxioms;
	}
	
	
}
