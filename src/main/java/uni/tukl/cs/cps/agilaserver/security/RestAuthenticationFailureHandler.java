package uni.tukl.cs.cps.agilaserver.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthenticationFailureHandler
		extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
	                                    HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		SecurityUtils.sendError(response, exception, HttpStatus.UNAUTHORIZED,
				"Authentication failed");
	}
}
