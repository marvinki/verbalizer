package org.semanticweb.cogExp.coverageEvaluator;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

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
	private List<Integer> noNoncompressedSteps = new ArrayList<Integer>();
	private List<Integer> noVerbalisedSteps = new ArrayList<Integer>();
	private List<Integer>  noNoncompressedCalculatedSteps = new ArrayList<Integer>();
	private List<OWLAxiom> inferredAxiomsWithoutJustification = new ArrayList<OWLAxiom>();
	private List<OWLAxiom> unprovenConclusions = new ArrayList<OWLAxiom>();
	private List<Set<OWLAxiom>> justsForUnprovenConclusions = new ArrayList<Set<OWLAxiom>>();
	private List<String> verbalizations = new ArrayList<String>();
	private List<String> proofListings = new ArrayList<String>();
	private List<String> longVerbalizations = new ArrayList<String>();
	private List<String> longProofListings = new ArrayList<String>();
	private Integer[] computationtimes;
	private Set<String> usedrules = new HashSet<String>();
	private List<Integer> compressions = new ArrayList<Integer>();
	private boolean hasRoleaggregation = false;
	private boolean hasClassaggregation = false;
	private boolean hasAttribute = false;
	private List<List<String>> rulesReport = new ArrayList<List<String>>();
	private List<List<String>> longRulesReport = new ArrayList<List<String>>();
	
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
            Integer[] computationtimes,
            Set<String> usedrules
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
		this.usedrules = usedrules;
	}
	
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
            List<Integer> noNoncompressedSteps, 
            List<Integer> noVerbalisedSteps, 
            List<Integer> noNoncompressedCalculatedSteps, 
            List<OWLAxiom> justfailed, 
            List<OWLAxiom> prooffailed, 
            List<Set<OWLAxiom>> justsprooffailed, 
            List<String> verbalizations,
            List<String> proofListings,
            List<String> longVerbalizations,
            List<String> longProofListings,
            Integer[] computationtimes,
            Set<String> usedrules,
            List<Integer> compressions, 
            boolean hasRoleaggregation,
            boolean hasClassaggregation,
            boolean hasAttribute,
            List<List<String>> rulesReport,
            List<List<String>> longRulesReport
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
		this.noNoncompressedSteps = noNoncompressedSteps;
		this.noVerbalisedSteps = noVerbalisedSteps;
		this.noNoncompressedCalculatedSteps = noNoncompressedCalculatedSteps;
		this.inferredAxiomsWithoutJustification = justfailed;
		this.unprovenConclusions = prooffailed;
		this.justsForUnprovenConclusions = justsprooffailed;
		this.verbalizations = verbalizations;
		this.proofListings = proofListings;
		this.longVerbalizations = longVerbalizations;
		this.longProofListings = longProofListings;
		this.computationtimes = computationtimes;
		this.usedrules = usedrules;
		this.compressions = compressions;
		this.hasRoleaggregation = hasRoleaggregation;
		this.hasClassaggregation = hasClassaggregation;
		this.hasAttribute = hasAttribute;
		this.rulesReport = rulesReport;
		this.longRulesReport = longRulesReport;
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
	
	public List<Integer> getNoNoncompressedSteps() {
		return noNoncompressedSteps;
	}
	
	public List<Integer> getNoNoncompressedCalculatedSteps() {
		return noNoncompressedCalculatedSteps;
	}
	
	public List<Integer> getNoVerbalizedSteps() {
		return noVerbalisedSteps;
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
	
	public List<String> getProofListings(){
		return proofListings;
	}
	
	public List<String> getLongVerbalizations(){
		return longVerbalizations;
	}
	
	public List<String> getLongProofListings(){
		return longProofListings;
	}
	
	public Integer[] getComputationtimes(){
		return computationtimes;
	}
	
	public Set<String> getUsedrules(){
		return usedrules;
	}
	
	public List<Integer> getCompressions(){
		return compressions;
	}
	
	public boolean getHasRoleaggregation(){
		return hasRoleaggregation;
	}
	
	public boolean getHasClassaggregation(){
		return hasClassaggregation;
	}
	
	public boolean getHasAttribute(){
		return hasAttribute;
	}
	
	public List<List<String>> getRulesReport(){
		return rulesReport;
	}
	
	public List<List<String>> getLongRulesReport(){
		return longRulesReport;
	}
	
}

