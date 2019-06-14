package uni.tukl.cs.cps.agilaserver.validators;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uni.tukl.cs.cps.agilaserver.dal.OntologyManager;
import uni.tukl.cs.cps.agilaserver.models.ontology.DataPropertyModel;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

import java.util.List;

@Component
public class DataPropertyValidator extends PropertyValidator implements Validator {

	@Autowired
	private OntologyRepository repository;

	@Override
	public boolean supports(Class<?> aClass) {
		return DataPropertyModel.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		DataPropertyModel dataProperty = (DataPropertyModel) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dataType", "NotEmpty");

		try {
			OntologyManager ontologyManager = new OntologyManager(repository);
			List<DataPropertyModel> dataProperties = ontologyManager.getListOfDataProperties();

			if (findProperty(dataProperties, dataProperty.getName())) {
				errors.rejectValue("name", "dataProperty.duplicate");
			}

			if (dataProperty.getParentName() != null && !findProperty(dataProperties, dataProperty.getParentName())) {
				errors.rejectValue("name", "dataProperty.duplicate");
			}
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

}