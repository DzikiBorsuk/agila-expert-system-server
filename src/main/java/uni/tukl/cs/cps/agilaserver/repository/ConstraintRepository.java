package uni.tukl.cs.cps.agilaserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uni.tukl.cs.cps.agilaserver.domain.Constraint;

import java.util.List;

public interface ConstraintRepository extends MongoRepository<Constraint, String> {

	Constraint findByOutput(String output);

	List<Constraint> findByEquation_Variable(String variable);

	Constraint findByName(String name);

	Constraint findByNameAndDeleted(String name, boolean deleted);

	Constraint findByOutputAndDeleted(String output, boolean deleted);

}
