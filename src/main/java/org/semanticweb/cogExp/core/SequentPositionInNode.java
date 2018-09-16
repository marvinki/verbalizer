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

public class SequentPositionInNode {

	private int nodeid;
	SequentPosition position;
	
	
	public int getNodeId(){
		return nodeid;
	}
	
	public SequentPositionInNode(int nid, SequentPart partpos, List<Integer> listpos){ 
		// super(partpos,listpos);
		position = new SequentSinglePosition(partpos,listpos);
		nodeid = nid;
	}
	
	public SequentPositionInNode(int nid, SequentPart partpos, int pos){ 
		position = new SequentSinglePosition(partpos,pos);
		// super(partpos, pos);
		nodeid = nid;
	}
	
	public SequentPositionInNode(int nid, SequentPart partpos, Integer[][] pos){ 
		position = new SequentMultiPosition(partpos,pos);
		// super(partpos, pos);
		nodeid = nid;
	}
	
	public SequentPositionInNode(int nid, SequentPosition seqpos){ 
		position = seqpos;
		// super(partpos, pos);
		nodeid = nid;
	}
	public SequentPart getSequentPart(){
		return getSequentPart();
	}
	
	public SequentPosition getSequentPosition(){
		return position;
	}
	
	@Override
	public java.lang.String toString(){
		return "node " + nodeid + "-" + position.toString();
	}
}
