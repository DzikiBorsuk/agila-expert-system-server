package uni.tukl.cs.cps.agilaserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uni.tukl.cs.cps.agilaserver.domain.User;
import uni.tukl.cs.cps.agilaserver.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationSuccessHandler
		extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private UserRepository userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
	                                    HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		User user = userService.findByEmail(authentication.getName());
		SecurityUtils.sendResponse(response, HttpStatus.ACCEPTED, user);
	}
}