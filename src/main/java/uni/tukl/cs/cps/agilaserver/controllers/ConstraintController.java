package uni.tukl.cs.cps.agilaserver.controllers;

import org.modelmapper.ModelMapper;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uni.tukl.cs.cps.agilaserver.domain.Constraint;
import uni.tukl.cs.cps.agilaserver.domain.User;
import uni.tukl.cs.cps.agilaserver.exceptions.AgilaApiError;
import uni.tukl.cs.cps.agilaserver.models.constraint.ConstraintModel;
import uni.tukl.cs.cps.agilaserver.models.ontology.DataPropertyModel;
import uni.tukl.cs.cps.agilaserver.repository.ConstraintRepository;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;
import uni.tukl.cs.cps.agilaserver.service.UserService;
import uni.tukl.cs.cps.agilaserver.validators.ConstraintValidator;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ConstraintController extends BaseController {

	@Autowired
	private ConstraintRepository constraintRepo;

	@Autowired
	private ConstraintValidator constraintValidator;

	@Autowired
	private OntologyRepository repository;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/constraint", method = RequestMethod.GET)
	public List<ConstraintModel> Get() {
		return constraintRepo.findAll().stream()
				.filter(c -> c!= null && !c.isDeleted())
				.map(c -> new ConstraintModel(c)).collect(Collectors.toList());
	}

	@RequestMapping(value = "/api/constraint", method = RequestMethod.POST)
	public ResponseEntity<Object> Post(@RequestBody ConstraintModel model, BindingResult bindingResult, Principal principal)
			throws OWLOntologyCreationException, OWLOntologyStorageException {

		ModelMapper modelMapper = new ModelMapper();
		User user = userService.findByEmail(principal.getName());

		Constraint constraint = modelMapper.map(model, Constraint.class);
		constraint.setUser(user);
		constraint.setDateCreated(new Date());

		constraintValidator.validate(constraint, bindingResult);

		if (bindingResult.hasErrors()) {
			return AgilaApiError.CreateErrorResponse(bindingResult.getFieldErrors());
		}

		DataPropertyModel outputProperty = new DataPropertyModel(constraint);
		getOntologyManager(repository).addNewDataProperty(outputProperty);
		constraintRepo.save(constraint);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}


	@RequestMapping(value = "/api/constraint/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> Delete(@PathVariable String id) {

		Optional<Constraint> constraint = constraintRepo.findById(id);

		if (constraint.isPresent()) {

			constraint.get().setDeleted(true);
			constraintRepo.save(constraint.get());

			return new ResponseEntity<>(HttpStatus.OK);

		} else {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		}
	}

}
