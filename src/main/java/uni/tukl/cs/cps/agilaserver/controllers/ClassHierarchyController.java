package uni.tukl.cs.cps.agilaserver.controllers;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.tukl.cs.cps.agilaserver.models.ontology.ClassHierarchyModel;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

import java.util.List;
import java.util.Optional;

@RestController
class ClassHierarchyController extends BaseController {

	@Autowired
	private OntologyRepository repository;

	@RequestMapping(value = { "/api/class-hierarchy/{id}", "/api/class-hierarchy/", "/api/class-hierarchy"})
	public List<ClassHierarchyModel> Get(@PathVariable Optional<String> id) throws OWLOntologyCreationException {
    	return getOntologyManager(repository).getClassHierarchy(id);
	}

}