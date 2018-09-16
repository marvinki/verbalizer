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
import java.util.ArrayList;

public class SequentSinglePosition extends SequentPosition {
	
	
	private SequentPart sequentpart;
	private List<Integer> position;
	
	public SequentSinglePosition(SequentPart part, int pos){
		sequentpart = part;
		position = new ArrayList<Integer>();
		position.add(pos);
	}
	
	public SequentSinglePosition(SequentPart part, List<Integer> pos){
		sequentpart = part;
		position = new ArrayList<Integer>(pos);
	}
	
	
	/* (non-Javadoc)
	 * @see org.semanticweb.tOWL.SequentPosition#getSequentPart()
	 */
	@Override
	public SequentPart getSequentPart(){
		return sequentpart;
	}
	
	public List<Integer> getPosition(){
		return position;
	}
	
	public boolean isToplevelPosition(){
		return position.size() == 1;
	}
	
	public int getToplevelPosition(){
		return position.get(0);
	}
	
	@Override
	public boolean isNotNull(){
		if (position==null){return false;} else {return true;}
	}
	
	@Override
	public java.lang.String toString(){
	return position.toString();
	}
}
