package org.semanticweb.cogExp.core;

import java.util.List;

public class SequentList {

	private enum type{OR,AND};
	
	private type thistype;
	private List<Sequent> sequentlist;
	private List<java.lang.Class> rulelist;
	
	public SequentList(List<Sequent> list, type t){
		thistype = t;
		sequentlist = list;
	}
	
	public SequentList(List<Sequent> list, type t, List<java.lang.Class> rlist){
		thistype = t;
		sequentlist = list;
		rulelist = rlist;
	}
	
	public static SequentList makeORSequentList(List<Sequent> list){
		return new SequentList(list, type.OR);
	}
	
	public static SequentList makeANDSequentList(List<Sequent> list){
		// System.out.println("LIST CONTAINED IN AND:  " +  list);
		return new SequentList(list, type.AND);
	}
	
	public boolean isTypeOR(){
		return (thistype == type.OR);
	}
	
	public boolean isTypeAND(){
		return (thistype == type.AND);
	}
	
	public List<Sequent> getSequents(){
		return sequentlist;
	}
	
	public List<java.lang.Class> getRulelist(){
		return rulelist;
	}
	
	public void setRulelist(List<java.lang.Class> classlist){
		rulelist = classlist;
	}
	
	

	
}
