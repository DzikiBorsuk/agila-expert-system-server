package uni.tukl.cs.cps.agilaserver.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uni.tukl.cs.cps.agilaserver.repository.ConstraintRepository;

@Component
public class DataPropertyDeleteValidator implements Validator {

	@Autowired
	private ConstraintRepository repository;

	@Override
	public boolean supports(Class<?> aClass) {
		return String.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		String id = (String) o;

		if (repository.findByOutputAndDeleted(id, false) != null) {
			errors.reject("dataProperty.delete.invalid.output");
		}

		if (repository.findByEquation_Variable(id).size() > 0) {
			errors.reject("dataProperty.delete.invalid.variable");
		}

		//TODO: validate nested variables
	}

}
