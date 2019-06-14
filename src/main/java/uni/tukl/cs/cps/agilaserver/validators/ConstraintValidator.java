package uni.tukl.cs.cps.agilaserver.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uni.tukl.cs.cps.agilaserver.domain.Constraint;
import uni.tukl.cs.cps.agilaserver.repository.ConstraintRepository;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

@Component
public class ConstraintValidator implements Validator {

	@Autowired
	private OntologyRepository ontologyRepository;

	@Autowired
	private ConstraintRepository constraintRepository;

	@Override
	public boolean supports(Class<?> aClass) {
		return Constraint.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Constraint constraint = (Constraint) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "formula", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "output", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "equation", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dataType", "NotEmpty");

		if (constraintRepository.findByNameAndDeleted(constraint.getName(), false) != null) {
			errors.rejectValue("name", "constraint.duplicate");
		}

		if (constraintRepository.findByOutputAndDeleted(constraint.getOutput(), false) != null) {
			errors.rejectValue("output", "constraint.output.duplicate");
		}

		errors.pushNestedPath("equation");
		ValidationUtils.invokeValidator(new EquationValidator(ontologyRepository), constraint.getEquation(), errors);
		errors.popNestedPath();
	}
}
