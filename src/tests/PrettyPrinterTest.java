package tests;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.cogExp.OWLAPIVerbaliser.VerbalisationManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;

public class PrettyPrinterTest {
	
	public static void main(String[] args) {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory=manager.getOWLDataFactory();
		
		OWLObjectProperty p1 = dataFactory.getOWLObjectProperty(IRI.create("http://www.foo.fr#p1"));
		OWLObjectProperty p2 = dataFactory.getOWLObjectProperty(IRI.create("http://www.foo.fr#p2"));
		OWLObjectProperty p3 = dataFactory.getOWLObjectProperty(IRI.create("http://www.foo.fr#p3"));
		OWLObjectProperty p4 = dataFactory.getOWLObjectProperty(IRI.create("http://www.foo.fr#p4"));
		List<OWLObjectProperty> exprslist = new ArrayList<OWLObjectProperty>();
		exprslist.add(p1);
		exprslist.add(p2);
		exprslist.add(p3);
	
		OWLSubPropertyChainOfAxiom chain = dataFactory.getOWLSubPropertyChainOfAxiom(exprslist,p4);
		System.out.println(VerbalisationManager.INSTANCE.prettyPrint(chain));
		
	}

}
