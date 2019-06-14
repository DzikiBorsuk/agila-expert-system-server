package uni.tukl.cs.cps.agilaserver.controllers;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uni.tukl.cs.cps.agilaserver.exceptions.AgilaApiError;
import uni.tukl.cs.cps.agilaserver.models.ontology.ObjectPropertyModel;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;
import uni.tukl.cs.cps.agilaserver.validators.ObjectPropertyValidator;

import java.util.List;

@RestController
public class ObjectPropertyController extends BaseController {

	@Autowired
	private OntologyRepository repository;

	@Autowired
	private ObjectPropertyValidator objectPropertyValidator;

	@RequestMapping(value = "/api/object-property", method = RequestMethod.GET)
	public List<ObjectPropertyModel> Get() throws OWLOntologyCreationException {
		return getOntologyManager(repository).getListOfObjectProperties();
	}

	@RequestMapping(value = "/api/object-property/{id}", method = RequestMethod.GET)
	public ObjectPropertyModel Get(@PathVariable("id") String id) throws OWLOntologyCreationException {
		return getOntologyManager(repository).getObjectProperty(id);
	}

	@RequestMapping(value = "/api/object-property", method = RequestMethod.POST)
	public ResponseEntity<Object> Post(@RequestBody ObjectPropertyModel objectProperty, BindingResult bindingResult) throws OWLOntologyCreationException, OWLOntologyStorageException {

		objectPropertyValidator.validate(objectProperty, bindingResult);

		if (bindingResult.hasErrors()) {
			return AgilaApiError.CreateErrorResponse(bindingResult.getFieldErrors());
		}

		getOntologyManager(repository).addNewObjectProperty(objectProperty);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/api/object-property/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object>  Delete(@PathVariable String id)
			throws OWLOntologyCreationException, OWLOntologyStorageException {

		getOntologyManager(repository).deleteObjectProperty(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
