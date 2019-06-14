package uni.tukl.cs.cps.agilaserver.controllers;

import org.apache.commons.io.IOUtils;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uni.tukl.cs.cps.agilaserver.domain.Ontology;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
class OntologyController extends BaseController {

	@Autowired
	private OntologyRepository repository;


	@RequestMapping(value="/api/ontology", method=RequestMethod.GET)
	public HttpServletResponse Get(HttpServletResponse response) throws IOException {

        List<Ontology> ontologies = repository.findAll();

        if (!ontologies.isEmpty()) {

            InputStream myStream = new ByteArrayInputStream(ontologies.get(0).getFile());

            response.addHeader("Content-disposition", "attachment;filename=ontology.owl");
            response.setContentType("application/rdf+xml");

            IOUtils.copy(myStream, response.getOutputStream());
            response.flushBuffer();

        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }

        return response;
	}

    @RequestMapping(value = "/api/ontology", method=RequestMethod.POST)
    public void Post(@RequestParam("file") MultipartFile fileRef) throws IOException, OWLOntologyCreationException {
        getOntologyManager(repository).saveOntology(fileRef.getInputStream());
    }

}