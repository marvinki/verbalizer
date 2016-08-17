package org.semanticweb.cogExp.OWLFormulas;



/** This class represents the logical constants and connectives used in OWLFormulas
 * 
 * @author marvin
 *
 */
public enum OWLSymb implements OWLAtom{
	SUBCL, INT, UNION, EXISTS, FORALL, TOP, BOT, NEG, EQUIV, RANGE, DOMAIN, DATADOMAIN, DATAHASVALUE, DATASOMEVALUESFROM, DATAALLVALUESFROM, OBJECTHASVALUE, 
	TRANSITIVE, FUNCTIONAL, FUNCTIONALDATA, IRREFLEXIVE, INVERSEOBJPROP, INVFUNCTIONAL, DISJ, SUBPROPERTYCHAIN, SUBPROPERTYOF, EQUIVOBJPROP, ONEOF, DATAONEOF, 
	OBJECTMINCARDINALITY, OBJECTMAXCARDINALITY, OBJECTEXACTCARDINALITY,DATAMINCARDINALITY, DATAMAXCARDINALITY, DATAEXACTCARDINALITY; 
	
	/** Returns a pretty printed string for a symbol
	 * 
	 * @return string representing the logical constant/connective, using UTF-8
	 */
	public String prettyPrint(){ 
		String output = "";
		switch (this){
			case SUBCL:  output="⊑"; break; 
			case INT:    output="⊓"; break;
			case EXISTS: output="∃"; break;
			case FORALL: output="∀"; break;
			case TOP:
			case BOT:
			case NEG:    output="¬"; break;
			case EQUIV:
			case RANGE:        output="ran"; break;
			case DOMAIN:       output="dom"; break;
			case DATADOMAIN:       output="datadom"; break;
			case TRANSITIVE:   output="tran"; break;
			case FUNCTIONAL:   output="functional"; break;
			case FUNCTIONALDATA:   output="functionaldata"; break;
			case IRREFLEXIVE:   output="iref"; break;
			case INVFUNCTIONAL:   output="invfunc"; break;
			case INVERSEOBJPROP: output="inverse"; break;
			case SUBPROPERTYCHAIN:   output="subpropchain"; break;
			case SUBPROPERTYOF:   output="subpropof"; break;
			case EQUIVOBJPROP:	output="equivobprop"; break; 
			case OBJECTHASVALUE:     output="objhasval"; break;
			case DATAHASVALUE:     output="datahasval"; break;
			case DATASOMEVALUESFROM:     output="datasomeval"; break;
			case DATAALLVALUESFROM:     output="dataallval"; break;
			case OBJECTMINCARDINALITY:     output="objectmincardinality"; break;
			case OBJECTMAXCARDINALITY:     output="objectmaxcardinality"; break;
			case OBJECTEXACTCARDINALITY:   output="objectexactcardinality"; break;
			case DATAMINCARDINALITY:     output="datamincardinality"; break;
			case DATAMAXCARDINALITY:     output="datamaxcardinality"; break;
			case DATAEXACTCARDINALITY:   output="dataexactcardinality"; break;
			case ONEOF:     output="oneof"; break;
			case DATAONEOF:     output="dataoneof"; break;
			case DISJ:     output="disj"; break;
		}
		return output;
	}
	
	
	
	@Override
	public boolean isSymb() {
		return true;
	}

	@Override
	public boolean isVar() {
		return false;
	}

	@Override
	public boolean isClassName() {
		return false;
	}
	
	@Override
	public boolean isIndividual() {
		return false;
	}
	
}
