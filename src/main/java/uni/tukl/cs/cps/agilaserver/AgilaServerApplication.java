package uni.tukl.cs.cps.agilaserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import uni.tukl.cs.cps.agilaserver.domain.Ontology;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class AgilaServerApplication implements CommandLineRunner {

	@Autowired
	private OntologyRepository ontologyRepository;

	@Bean
	public MessageSource messageSource () {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("ValidationMessages");
		return messageSource;
	}

	@Override
	public void run(String... args) throws Exception {
		if(ontologyRepository.findAll().size() == 0) {
			Path path = Paths.get("resources/Teste.owl");
			try {
				ontologyRepository.save(new Ontology("Teste", Files.readAllBytes(path)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(AgilaServerApplication.class, args);
	}
}
