package uni.tukl.cs.cps.agilaserver.controllers;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import uni.tukl.cs.cps.agilaserver.dal.OntologyManager;
import uni.tukl.cs.cps.agilaserver.exceptions.OWLClassNotFoundException;
import uni.tukl.cs.cps.agilaserver.models.individual.IndividualModel;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

import java.util.List;

@RestController
public class IndividualController extends BaseController {

	@Autowired
	private OntologyRepository repository;

	@RequestMapping(value = "/api/individual", method = RequestMethod.GET)
	public List<IndividualModel> Get() throws OWLOntologyCreationException {
		return getOntologyManager(repository).getListOfIndividuals();
	}

	@RequestMapping("/api/individual/{id}")
	public IndividualModel Get(@PathVariable String id) throws OWLOntologyCreationException {
	    OntologyManager ontologyManager = getOntologyManager(repository);

		IndividualModel individual = new IndividualModel(id, id);
		individual.setDataProperties(ontologyManager.getDataPropertiesOfIndividual(id));
		individual.setObjectProperties(ontologyManager.getObjectPropertiesOfIndividual(id));

	    return individual;
	}

	@RequestMapping(value = "/api/individual", method = RequestMethod.POST)
	public IndividualModel Post(@RequestBody IndividualModel individual, BindingResult bindingResult)
			throws OWLClassNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException {

		getOntologyManager(repository).addNewIndividual(individual);

		return individual;
	}

	@RequestMapping(value = "/api/individual/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> Delete(@PathVariable String id)
			throws OWLOntologyCreationException, OWLOntologyStorageException {

		getOntologyManager(repository).deleteIndividual(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
