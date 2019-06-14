package uni.tukl.cs.cps.agilaserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends EntityNotFoundException {

	public UserNotFoundException(String username) {
		super("The user '" + username + "' could not be found.");
	}

}
