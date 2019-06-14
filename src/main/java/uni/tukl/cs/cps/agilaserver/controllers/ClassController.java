package uni.tukl.cs.cps.agilaserver.controllers;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uni.tukl.cs.cps.agilaserver.exceptions.OWLClassNotFoundException;
import uni.tukl.cs.cps.agilaserver.models.ontology.ClassModel;
import uni.tukl.cs.cps.agilaserver.models.ontology.ClassPostModel;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

import java.util.List;

@RestController
class ClassController extends BaseController {

	@Autowired
	private OntologyRepository repository;

	@RequestMapping(value = "/api/class/{id}", method = RequestMethod.GET)
	public List<ClassModel> Get(@PathVariable String id) {
		return null;
	}

	@RequestMapping(value = "/api/class", method = RequestMethod.POST)
	public ClassPostModel Post(@RequestBody ClassPostModel newClass, BindingResult bindingResult)
			throws OWLClassNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException {

		getOntologyManager(repository).addNewClass(newClass);
		
		return newClass;
	}

	@RequestMapping(value = "/api/class/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> Delete(@PathVariable String id)
			throws OWLOntologyCreationException, OWLOntologyStorageException {

		getOntologyManager(repository).deleteClass(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}