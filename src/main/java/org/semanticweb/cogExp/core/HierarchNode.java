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

import java.util.ArrayList;
import java.util.List;

public class HierarchNode<JustificationType,PositionType> {
	private List<JustificationNode<JustificationType,PositionType>> justifications = new ArrayList<JustificationNode<JustificationType,PositionType>>(); 
	
	public List<JustificationNode<JustificationType,PositionType>> getJustifications(){
		return justifications;
	}
	
	public HierarchNode<JustificationType,PositionType> copy(){
		HierarchNode<JustificationType,PositionType> new_hierarch_node = new HierarchNode<JustificationType,PositionType>();
		List<JustificationNode<JustificationType,PositionType>> new_justifications = new_hierarch_node.getJustifications();
		for (JustificationNode<JustificationType,PositionType> just: justifications){
				new_justifications.add(just.copy());
		}
		return new_hierarch_node;
		
	}
	
	
}
