package uni.tukl.cs.cps.agilaserver.controllers;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class DataTypeController extends BaseController {

	/**
	 * Ontology data types supported by the system
	 * @return the data types
	 * @throws OWLOntologyCreationException
	 */
	@RequestMapping("/api/data-types")
	public List<OWL2Datatype> Get() throws OWLOntologyCreationException {
		List<OWL2Datatype> datatypes = Arrays.asList(OWL2Datatype.XSD_INTEGER, OWL2Datatype.XSD_NON_NEGATIVE_INTEGER,
			OWL2Datatype.XSD_FLOAT, OWL2Datatype.XSD_HEX_BINARY, OWL2Datatype.XSD_BASE_64_BINARY, OWL2Datatype.XSD_DATE_TIME, OWL2Datatype.XSD_BOOLEAN);
		return datatypes;
	}

}
