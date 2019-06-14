package uni.tukl.cs.cps.agilaserver.service;

public interface SecurityService {
	String findLoggedInUsername();

	void autologin(String email, String password);
}
