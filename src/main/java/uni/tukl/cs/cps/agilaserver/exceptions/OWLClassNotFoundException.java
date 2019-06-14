package uni.tukl.cs.cps.agilaserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OWLClassNotFoundException extends EntityNotFoundException {

	public OWLClassNotFoundException(String className) {
		super("The class " + className + " has not been found");
	}
}
