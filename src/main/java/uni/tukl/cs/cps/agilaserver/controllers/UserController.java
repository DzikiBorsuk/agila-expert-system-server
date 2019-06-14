package uni.tukl.cs.cps.agilaserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.tukl.cs.cps.agilaserver.domain.User;
import uni.tukl.cs.cps.agilaserver.exceptions.UserNotFoundException;
import uni.tukl.cs.cps.agilaserver.models.user.UserInfoModel;
import uni.tukl.cs.cps.agilaserver.service.UserService;

import java.security.Principal;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/api/user")
	public UserInfoModel Get(Principal principal) {
		User user = userService.findByEmail(principal.getName());
		if (user == null) {
			throw new UserNotFoundException(principal.getName());
		}
		return new UserInfoModel(user);
	}

}
