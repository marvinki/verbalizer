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

public class SequentMultiPosition extends SequentPosition {
	
	
	private SequentPart sequentpart;
	private Integer[][] positions;
	
	public SequentMultiPosition(SequentPart part, int pos){
		sequentpart = part;
		Integer[] positions1 = new Integer[]{};
		positions1[0] = pos;
		Integer[][] positions2 = new Integer[][]{};
		positions2[0] = positions1;
		positions = positions2;
	}
	
	public SequentMultiPosition(SequentPart part, Integer[][] pos){
		sequentpart = part;
		positions = pos;
	}
	
	
	@Override
	public SequentPart getSequentPart(){
		return sequentpart;
	}
	
	public Integer[][] getPositions(){
		return positions;
	}
	
	@Override
	public boolean isNotNull(){
		if (positions==null){return false;} else {return true;}
	}
	
}
