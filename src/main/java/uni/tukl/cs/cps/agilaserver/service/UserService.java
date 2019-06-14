package uni.tukl.cs.cps.agilaserver.service;

import uni.tukl.cs.cps.agilaserver.domain.User;

public interface UserService {
	void save(User user);

	User findByEmail(String username);
}
