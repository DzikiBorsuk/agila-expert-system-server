package uni.tukl.cs.cps.agilaserver.controllers;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import uni.tukl.cs.cps.agilaserver.dal.OntologyManager;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

public abstract class BaseController {

	private OntologyManager ontologyManager;

	protected OntologyManager getOntologyManager(OntologyRepository repository) throws OWLOntologyCreationException {
		ontologyManager = new OntologyManager(repository);
		return ontologyManager;
	}

}
