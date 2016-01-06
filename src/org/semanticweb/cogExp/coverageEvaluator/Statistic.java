package org.semanticweb.cogExp.coverageEvaluator;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLAxiom;

public class Statistic {

	private String ontologyname;
	private int noSubsumptions;
	private int noSolvedSubsumptions;
	private int noTimedOut;
	private int avgTime;
	private int avgJustTime;
	private int subPropOfProblems;
	private int unionProblems;
	private int disjProblems;
	private List<Integer> noSteps = new ArrayList<Integer>();
	private List<Integer> noVerbalisedSteps = new ArrayList<Integer>();
	private List<OWLAxiom> inferredAxiomsWithoutJustification = new ArrayList<OWLAxiom>();
	private List<OWLAxiom> unprovenConclusions = new ArrayList<OWLAxiom>();
	private List<Set<OWLAxiom>> justsForUnprovenConclusions = new ArrayList<Set<OWLAxiom>>();
	private List<String> verbalizations = new ArrayList<String>();
	private Integer[] computationtimes;
	
	Statistic(String ontologyname,
			  int noSubsumptions, 
			  int noSolvedSubsumptions,
              int noTimedOut, 
              int avgTime,
              int avgJustTime,
              int subPropOfProblems,
              int unionProblems,
              int disjProblems,
              List<Integer> noSteps, 
              List<Integer> noVerbalisedSteps, 
              List<OWLAxiom> justfailed, 
              List<OWLAxiom> prooffailed, 
              List<Set<OWLAxiom>> justsprooffailed, 
              List<String> verbalizations,
              Integer[] computationtimes
			){
		this.ontologyname = ontologyname;
		this.noSubsumptions = noSubsumptions;
		this.noSolvedSubsumptions = noSolvedSubsumptions;
		this.noTimedOut = noTimedOut;
		this.avgTime =  avgTime;
		this.avgJustTime =  avgJustTime;
		this.subPropOfProblems = subPropOfProblems;
		this.unionProblems = unionProblems;
		this.disjProblems = disjProblems;
		this.noSteps = noSteps;
		this.noVerbalisedSteps = noVerbalisedSteps;
		this.inferredAxiomsWithoutJustification = justfailed;
		this.unprovenConclusions = prooffailed;
		this.justsForUnprovenConclusions = justsprooffailed;
		this.verbalizations = verbalizations;
		this.computationtimes = computationtimes;
	}
	
	public String getOntologyname() {
		return ontologyname;
	}

	public int getNoSubsumptions() {
		return noSubsumptions;
	}

	public int getNoSolvedSubsumptions() {
		return noSolvedSubsumptions;
	}

	public int getNoTimedOut() {
		return noTimedOut;
	}
	
	public int getNoUnionProblems() {
		return unionProblems;
	}
	
	public int getNoDisjProblems() {
		return disjProblems;
	}
	
	public int getNoSubPropOfProblems() {
		return subPropOfProblems;
	}

	public int getAvgTime() {
		return avgTime;
	}
	
	public int getAvgJustTime() {
		return avgJustTime;
	}

	public List<Integer> getNoSteps() {
		return noSteps;
	}
	
	public List<OWLAxiom> getFailedJustsAxioms(){
		return inferredAxiomsWithoutJustification;
	}
	
	public List<Set<OWLAxiom>> getJustsForUnprovenConclusions(){
		return justsForUnprovenConclusions;
	}
	
	public List<OWLAxiom> getUnprovenConclusions(){
		return unprovenConclusions;
	}
	
	public int cardinalityNoSteps(int nosteps){
		int result = 0;
		for(Integer i: noSteps){
			if (i==nosteps)
				result++;
		}
		return result;
	}
	
	public int cardinalityNoVerbalisedSteps(int nosteps){
		int result = 0;
		for(Integer i: noVerbalisedSteps){
			if (i==nosteps)
				result++;
		}
		return result;
	}
	
	public List<String> getVerbalizations(){
		return verbalizations;
	}
	
	public Integer[] getComputationtimes(){
		return computationtimes;
	}
}
