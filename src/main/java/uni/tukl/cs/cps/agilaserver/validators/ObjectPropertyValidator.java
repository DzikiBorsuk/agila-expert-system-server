package uni.tukl.cs.cps.agilaserver.validators;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uni.tukl.cs.cps.agilaserver.dal.OntologyManager;
import uni.tukl.cs.cps.agilaserver.exceptions.OWLClassNotFoundException;
import uni.tukl.cs.cps.agilaserver.models.ontology.DataPropertyModel;
import uni.tukl.cs.cps.agilaserver.models.ontology.ObjectPropertyModel;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

import java.util.List;

@Component
public class ObjectPropertyValidator extends PropertyValidator implements Validator {

	@Autowired
	private OntologyRepository repository;

	@Override
	public boolean supports(Class<?> aClass) {
		return DataPropertyModel.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		ObjectPropertyModel objectProperty = (ObjectPropertyModel) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "range", "objectProperty.ranges.empty");

		try {
			OntologyManager ontologyManager = new OntologyManager(repository);
			List<ObjectPropertyModel> objectProperties = ontologyManager.getListOfObjectProperties();

			if (findProperty(objectProperties, objectProperty.getName())) {
				errors.rejectValue("name", "objectProperty.duplicate");
			}

			if (objectProperty.getParentName() != null && !findProperty(objectProperties, objectProperty.getParentName())) {
				errors.rejectValue("parentName", "objectProperty.parent.notFound");
			}

			try {
				if (objectProperty.getRange() != null) {
					ontologyManager.getClassIRI(objectProperty.getRange());
				}
			} catch (OWLClassNotFoundException ex) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "range", "objectProperty.ranges.notFound");
			}

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}


}