package uni.tukl.cs.cps.agilaserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import uni.tukl.cs.cps.agilaserver.exceptions.AgilaApiError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public final class SecurityUtils {


	private static final ObjectMapper mapper = new ObjectMapper();


	private SecurityUtils() {
	}


	/**
	 * Get the login of the current user.
	 */
	public static String getCurrentLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		UserDetails springSecurityUser = null;
		String userName = null;

		if(authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				springSecurityUser = (UserDetails) authentication.getPrincipal();
				userName = springSecurityUser.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				userName = (String) authentication.getPrincipal();
			}
		}

		return userName;
	}


	public static void sendError(HttpServletResponse response, Exception exception, HttpStatus status, String message) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(status.value());
		PrintWriter writer = response.getWriter();
		writer.write(mapper.writeValueAsString(new AgilaApiError(status, message, exception)));
		writer.flush();
		writer.close();
	}


	public static void sendResponse(HttpServletResponse response, HttpStatus status, Object object) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(mapper.writeValueAsString(object));
		response.setStatus(status.value());
		writer.flush();
		writer.close();
	}

}