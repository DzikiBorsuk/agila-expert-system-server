package uni.tukl.cs.cps.agilaserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OWLClassAlreadyExists extends AgilaApiException {

	public OWLClassAlreadyExists(String className) {
		super("The class " + className + " already exists");
	}
}
