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
