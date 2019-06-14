package uni.tukl.cs.cps.agilaserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uni.tukl.cs.cps.agilaserver.domain.Role;
import java.util.UUID;

public interface RoleRepository extends MongoRepository<Role, UUID> {

}