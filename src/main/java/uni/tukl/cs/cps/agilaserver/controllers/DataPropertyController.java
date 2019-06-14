package uni.tukl.cs.cps.agilaserver.controllers;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uni.tukl.cs.cps.agilaserver.exceptions.AgilaApiError;
import uni.tukl.cs.cps.agilaserver.models.ontology.DataPropertyModel;
import uni.tukl.cs.cps.agilaserver.repository.ConstraintRepository;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;
import uni.tukl.cs.cps.agilaserver.validators.DataPropertyDeleteValidator;
import uni.tukl.cs.cps.agilaserver.validators.DataPropertyValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DataPropertyController extends BaseController {

	@Autowired
	private OntologyRepository repository;

	@Autowired
	private ConstraintRepository constraintRepo;

	@Autowired
	private DataPropertyValidator dataPropertyValidator;

	@Autowired
	private DataPropertyDeleteValidator dataPropertyDeleteValidator;

	@RequestMapping(value = "/api/data-property", method = RequestMethod.GET)
	public List<DataPropertyModel> Get() throws OWLOntologyCreationException {
		return getOntologyManager(repository).getListOfDataProperties();
	}

	@RequestMapping(value = "/api/data-property/{id}", method = RequestMethod.GET)
	public DataPropertyModel Get(@PathVariable("id") String id) throws OWLOntologyCreationException {
		return getOntologyManager(repository).getDataProperty(id);
	}

	@RequestMapping(value = "/api/data-property", method = RequestMethod.POST)
	public ResponseEntity<Object> Post(@RequestBody DataPropertyModel dataProperty, BindingResult bindingResult) throws OWLOntologyCreationException, OWLOntologyStorageException {

		dataPropertyValidator.validate(dataProperty, bindingResult);

		if (bindingResult.hasErrors()) {
			return AgilaApiError.CreateErrorResponse(bindingResult.getFieldErrors());
		}
		
		getOntologyManager(repository).addNewDataProperty(dataProperty);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/api/data-property/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> Delete(@ModelAttribute("id") @Valid String id, BindingResult bindingResult)
			throws OWLOntologyCreationException, OWLOntologyStorageException {

		dataPropertyDeleteValidator.validate(id, bindingResult);

		if (bindingResult.hasErrors()) {
			return AgilaApiError.CreateErrorResponse(bindingResult.getFieldErrors());
		}

		getOntologyManager(repository).deleteDataProperty(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
