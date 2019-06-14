package uni.tukl.cs.cps.agilaserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uni.tukl.cs.cps.agilaserver.domain.User;
import uni.tukl.cs.cps.agilaserver.exceptions.AgilaApiError;
import uni.tukl.cs.cps.agilaserver.models.signUp.SignUpModel;
import uni.tukl.cs.cps.agilaserver.service.SecurityService;
import uni.tukl.cs.cps.agilaserver.service.UserService;
import uni.tukl.cs.cps.agilaserver.validators.UserValidator;

@RestController
public class SignUpController {

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserValidator userValidator;

	@RequestMapping(value = "/api/sign-up", method = RequestMethod.POST)
	public ResponseEntity<Object> SignUp(@RequestBody SignUpModel signUp, BindingResult bindingResult) {

		User user = new User(signUp.getFirstName(), signUp.getLastName(), signUp.getEmail(), signUp.getPassword());

		userValidator.validate(user, bindingResult);

		if (bindingResult.hasErrors()) {
			return AgilaApiError.CreateErrorResponse(bindingResult.getFieldErrors());
		}

		userService.save(user);

		securityService.autologin(user.getEmail(), signUp.getPassword());

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
