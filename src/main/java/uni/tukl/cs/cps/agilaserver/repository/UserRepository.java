package uni.tukl.cs.cps.agilaserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uni.tukl.cs.cps.agilaserver.domain.User;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, UUID>{
	User findByEmail(String email);
}