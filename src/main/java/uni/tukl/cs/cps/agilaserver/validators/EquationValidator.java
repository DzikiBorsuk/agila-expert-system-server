package uni.tukl.cs.cps.agilaserver.validators;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uni.tukl.cs.cps.agilaserver.dal.OntologyManager;
import uni.tukl.cs.cps.agilaserver.domain.Equation;
import uni.tukl.cs.cps.agilaserver.exceptions.OWLClassNotFoundException;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

public class EquationValidator implements Validator {

	public  EquationValidator(OntologyRepository ontologyRepository) {
		super();
		this.ontologyRepository = ontologyRepository;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Equation.class.equals(aClass);
	}

	private OntologyRepository ontologyRepository;

	private OntologyManager ontologyManager;

	@Override
	public void validate(Object o, Errors errors) {

		Equation equation = (Equation) o;
		try {
			ontologyManager = new OntologyManager(ontologyRepository);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tag", "NotEmpty");


		switch (equation.getTag()) {
			case OPERATION:
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "operation", "NotEmpty");

				errors.pushNestedPath("firstElement");
				validate(equation.getFirstElement(), errors);
				errors.popNestedPath();

				errors.pushNestedPath("secondElement");
				validate(equation.getSecondElement(), errors);
				errors.popNestedPath();
				break;
			case VARIABLE:
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "variable", "NotEmpty");
				try {
					if (equation.getVariable() != null) {
						ontologyManager.getClassIRI(equation.getVariable());
					}
				} catch (OWLClassNotFoundException ex) {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "variable", "constraint.variable.notFound");
				}
				break;
			case CONSTANT:
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "constant", "NotEmpty");
				break;
			default:
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tag", "constraint.tag.invalid");
		}
	}
}
