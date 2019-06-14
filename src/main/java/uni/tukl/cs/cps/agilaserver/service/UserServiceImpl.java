package uni.tukl.cs.cps.agilaserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uni.tukl.cs.cps.agilaserver.domain.Role;
import uni.tukl.cs.cps.agilaserver.domain.User;
import uni.tukl.cs.cps.agilaserver.exceptions.UserNotFoundException;
import uni.tukl.cs.cps.agilaserver.repository.RoleRepository;
import uni.tukl.cs.cps.agilaserver.repository.UserRepository;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<Role>(roleRepository.findAll()));
		userRepository.save(user);
	}

	@Override
	public User findByEmail(String username) throws UserNotFoundException {
		return userRepository.findByEmail(username);
	}
}
