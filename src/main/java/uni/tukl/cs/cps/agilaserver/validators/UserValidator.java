package uni.tukl.cs.cps.agilaserver.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uni.tukl.cs.cps.agilaserver.domain.User;
import uni.tukl.cs.cps.agilaserver.service.UserService;

@Component
public class UserValidator implements Validator {
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

		if (userService.findByEmail(user.getEmail()) != null) {
			errors.rejectValue("email", "user.duplicate.email");
		}

		if (user.getPassword() != null && (user.getPassword().length() < 10 || user.getPassword().length() > 32)) {
			errors.rejectValue("password", "user.password.length");
		}
	}
}
