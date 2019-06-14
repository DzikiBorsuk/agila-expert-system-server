package uni.tukl.cs.cps.agilaserver.controllers;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uni.tukl.cs.cps.agilaserver.models.dashboard.DashboardModel;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

@RestController
public class DashboardController extends BaseController{

	@Autowired
	private OntologyRepository repository;

	@RequestMapping("/api/dashboard/")
	public DashboardModel Get() throws OWLOntologyCreationException {

		DashboardModel dashboard = new DashboardModel();
		dashboard.setClasses(getOntologyManager(repository).getClassCount());
		dashboard.setIndividuals(getOntologyManager(repository).getIndividualsCount());
		dashboard.setObjectProperties(getOntologyManager(repository).getObjectPropertiesCount());
		dashboard.setDataProperties(getOntologyManager(repository).getDataPropertiesCount());

		return dashboard;
	}

}
