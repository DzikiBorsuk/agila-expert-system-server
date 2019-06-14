package uni.tukl.cs.cps.agilaserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uni.tukl.cs.cps.agilaserver.domain.Ontology;

import java.util.UUID;

public interface OntologyRepository extends MongoRepository<Ontology, UUID>/*, JpaSpecificationExecutor<Ontology> */{

}
