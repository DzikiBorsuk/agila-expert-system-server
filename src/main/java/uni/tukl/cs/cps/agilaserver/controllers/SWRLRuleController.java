package uni.tukl.cs.cps.agilaserver.controllers;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.parser.SWRLParseException;
import uni.tukl.cs.cps.agilaserver.exceptions.AgilaApiError;
import uni.tukl.cs.cps.agilaserver.models.ontology.SWRLRuleModel;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

import java.util.List;

@RestController
public class SWRLRuleController extends BaseController {

	@Autowired
	private OntologyRepository repository;

	@RequestMapping(value = "/api/swrl-rule", method = RequestMethod.POST)
	public ResponseEntity<Object> Post(@RequestBody SWRLRuleModel swrlRule, BindingResult bindingResult)
			throws OWLOntologyCreationException, SWRLParseException, SWRLBuiltInException, OWLOntologyStorageException {

		if (bindingResult.hasErrors()) {
			return AgilaApiError.CreateErrorResponse(bindingResult.getFieldErrors());
		}

		getOntologyManager(repository).addNewSWRLRule(swrlRule);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/api/swrl-rule", method = RequestMethod.GET)
	public List<SWRLRuleModel> Get() throws OWLOntologyCreationException {
        List<SWRLRuleModel> rules = getOntologyManager(repository).getSWRLRules();
		return rules;
	}

	@RequestMapping(value = "/api/swrl-rule/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> Delete(@PathVariable String id) throws OWLOntologyCreationException, OWLOntologyStorageException {
		getOntologyManager(repository).deleteSWRLRule(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
