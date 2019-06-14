package uni.tukl.cs.cps.agilaserver.dal;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.core.SWRLRuleRenderer;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import uni.tukl.cs.cps.agilaserver.domain.Ontology;
import uni.tukl.cs.cps.agilaserver.exceptions.EntityNotFoundException;
import uni.tukl.cs.cps.agilaserver.exceptions.OWLClassAlreadyExists;
import uni.tukl.cs.cps.agilaserver.exceptions.OWLClassNotFoundException;
import uni.tukl.cs.cps.agilaserver.models.individual.IndividualDataPropertyValueModel;
import uni.tukl.cs.cps.agilaserver.models.individual.IndividualModel;
import uni.tukl.cs.cps.agilaserver.models.individual.IndividualObjectPropertyModel;
import uni.tukl.cs.cps.agilaserver.models.ontology.*;
import uni.tukl.cs.cps.agilaserver.repository.OntologyRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class OntologyManager {

	private OWLOntology ontology;

	private final OWLReasoner reasoner;

	private final Ontology domainOntology;

	private final OWLDataFactory dataFactory;

	private final IRI ontologyIRI;

	private final OWLOntologyManager ontologyManager;

	private final OntologyRepository repository;

	private SWRLRuleEngine swrlRuleEngine;

	public OntologyManager(OntologyRepository repository) throws OWLParserException, OWLOntologyCreationException, EntityNotFoundException {
		ontologyManager = OWLManager.createOWLOntologyManager();
		dataFactory = ontologyManager.getOWLDataFactory();
		this.repository = repository;

		List<Ontology> ontologies = repository.findAll();
		if (ontologies.size() == 0) {
			throw new EntityNotFoundException("No ontologies were found in the repository");
		}

		domainOntology = ontologies.get(0);

		this.loadOntologyFromStream(new ByteArrayInputStream(domainOntology.getFile()));

        if (ontology.getOntologyID().getOntologyIRI().isPresent()) {
            ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        } else {
            ontologyIRI = IRI.generateDocumentIRI();
        }

        reasoner = new StructuralReasonerFactory().createReasoner(ontology);
	}

	public void loadOntologyFromStream(InputStream stream) throws OWLOntologyCreationException, OWLParserException {
		if (ontology != null)
			ontologyManager.removeOntology(ontology);

		ontology = ontologyManager.loadOntologyFromOntologyDocument(stream);


		ontologyManager.getOntologyFormat(ontology).asPrefixOWLOntologyFormat().setDefaultPrefix("http://www.semanticweb.org/ontologies/2017/11/vehicles-ontologyManager#");
	}

	public void saveOntology() throws OWLOntologyStorageException {
		if (ontology != null) {
			ByteArrayOutputStream ontologyStream = new ByteArrayOutputStream();
			ontologyManager.saveOntology(ontology, ontologyStream);
			domainOntology.setFile(ontologyStream.toByteArray());
			repository.save(domainOntology);
		}
	}

	public void saveOntology(InputStream ontologyContent) throws IOException, OWLOntologyCreationException {
		domainOntology.setFile(ontologyContent.readAllBytes());
		repository.save(domainOntology);
		loadOntologyFromStream(ontologyContent);
	}

	public void saveOntologyToFile(File file) throws OWLOntologyStorageException {
		if (ontology != null) {
			ontologyManager.saveOntology(ontology, IRI.create(file.toURI()));
		}
	}

	public List<ClassHierarchyModel> getClassHierarchy(Optional<String> className) {
		ClassHierarchyModel root;
		if (className.isPresent()) {
			root = new ClassHierarchyModel(className.get(), null, new ArrayList<>());
			setClassHierarchy(root, dataFactory.getOWLClass(getClassIRI(className.get())));
		} else {
			root = new ClassHierarchyModel("Thing", null, new ArrayList<>());
			setClassHierarchy(root, dataFactory.getOWLThing());
		}

		ArrayList<ClassHierarchyModel> list = new ArrayList<>();
		list.add(root);

		return list;
	}

	public ClassHierarchyModel setClassHierarchy(ClassHierarchyModel root, OWLClass cl) {
		if (reasoner.isSatisfiable(cl)) {
            Iterator<OWLClass> classIterator = reasoner.getSubClasses(cl, true).getFlattened().iterator();
			while (classIterator.hasNext()) {
				OWLClass subClass = classIterator.next();

				if (!subClass.equals(cl) && reasoner.isSatisfiable(subClass)) {
					String str = getShortForm(subClass.getIRI());
					//null to prevent creating empty subnode
					ClassHierarchyModel child = new ClassHierarchyModel(str, null, null);
					if (root.getNodes() == null) {
						root.setNodes(new ArrayList<>());
					}
					root.getNodes().add(child);
					setClassHierarchy(child, subClass);
				}
			}
		}

		return root;
	}

	public List<ObjectPropertyModel> getListOfObjectProperties() {
		OWLObjectProperty topProperty = dataFactory.getOWLTopObjectProperty();
		List<String> addedProperties = new ArrayList<>();
		List<ObjectPropertyModel> list = getListOfObjectProperties(topProperty, addedProperties);

		return list;
	}

	private List<ObjectPropertyModel> getListOfObjectProperties(OWLObjectProperty topProperty, List<String> addedProperties) {

		ArrayList<ObjectPropertyModel> list = new ArrayList<>();

		reasoner.getSubObjectProperties(topProperty, false).forEach(subObjectProperty -> {
			if (!reasoner.getSubObjectProperties(subObjectProperty.getRepresentativeElement(), false).isEmpty() &&
                    !subObjectProperty.getRepresentativeElement().isAnonymous()) {
				String propertyName = getShortForm(subObjectProperty.getRepresentativeElement().getNamedProperty().getIRI());
				if (!addedProperties.contains(propertyName)) {
					addedProperties.add(propertyName);

					ObjectPropertyModel property = new ObjectPropertyModel(propertyName);
					property.setSubProperties(getListOfObjectProperties(subObjectProperty.getRepresentativeElement().getNamedProperty(), addedProperties));

					if (!topProperty.isTopEntity()) {
						property.setParentName(getShortForm(topProperty.getNamedProperty().getIRI()));
					}

					setObjectPropertyRanges(subObjectProperty.getRepresentativeElement(), property);

					if (!subObjectProperty.getRepresentativeElement().isAnonymous())
						setPropertyDescription(subObjectProperty.getRepresentativeElement().asOWLObjectProperty(), property);

					list.add(property);
				}
			}
		});

		if (list.isEmpty())
			return null;

		return list;
	}

	public List<DataPropertyModel> getListOfDataProperties() {
		OWLDataProperty topProperty = dataFactory.getOWLTopDataProperty();
		ArrayList<String> addedProperties = new ArrayList<>();
		List<DataPropertyModel> list = getListOfDataProperties(topProperty, addedProperties);

		return list;
	}

	private List<DataPropertyModel> getListOfDataProperties(OWLDataProperty topProperty, ArrayList<String> addedProperties) {

		ArrayList<DataPropertyModel> list = new ArrayList<>();

		reasoner.getSubDataProperties(topProperty, false).forEach(subDataProperty -> {
			if (!reasoner.getSubDataProperties(subDataProperty.getRepresentativeElement(), false).isEmpty()) {
				String propertyName = getShortForm(subDataProperty.getRepresentativeElement().getIRI());
				if (!addedProperties.contains(propertyName)) {
					addedProperties.add(propertyName);

					DataPropertyModel dataProperty = new DataPropertyModel(propertyName, getDataType(subDataProperty.getRepresentativeElement()));
					dataProperty.setSubProperties(getListOfDataProperties(subDataProperty.getRepresentativeElement().asOWLDataProperty(), addedProperties));

					if (!topProperty.isTopEntity()) {
						dataProperty.setParentName(getShortForm(topProperty.getIRI()));
					}

					setPropertyDescription(subDataProperty.getRepresentativeElement(), dataProperty);

					list.add(dataProperty);
				}
			}
		});

		if (list.isEmpty())
			return null;

		return list;
	}

	private void setPropertyDescription(OWLProperty subProperty, PropertyModel property) {
		List<OWLAnnotationAssertionAxiom> annotations = getAnnotations();
		if (annotations != null && !annotations.isEmpty()) {
			List<OWLAnnotationAssertionAxiom> annotation = annotations.stream()
					.filter(a -> a.getSubject().equals(subProperty.getIRI()))
					.collect(Collectors.toList());

			if (!annotation.isEmpty()) {
				property.setDescription(annotation.get(0).getValue().asLiteral().get().getLiteral());
			}
		}
	}

	private void setObjectPropertyRanges(OWLObjectPropertyExpression subObjectProperty, ObjectPropertyModel property) {
		Optional<String> range =  reasoner.getObjectPropertyRanges(subObjectProperty, false).getNodes().stream()
				.filter(r-> !r.getRepresentativeElement().isTopEntity())
				.map(r -> getShortForm(r.getRepresentativeElement().getIRI()))
				.findFirst();

		if (range.isPresent()) {
			property.setRange(range.get());
		}
	}

	private List<OWLAnnotationAssertionAxiom> getAnnotations() {
		return ontology.getAxioms(AxiomType.ANNOTATION_ASSERTION).stream().collect(Collectors.toList());
	}

	private OWL2Datatype getDataType(OWLDataProperty property) {
		Stream<OWLDataRange> dataRanges = EntitySearcher.getRanges(property, ontology).stream();
		Optional<OWLDataRange> dataRange = dataRanges.findFirst();
		if (dataRange.isPresent()) {
			return dataRange.get().asOWLDatatype().getBuiltInDatatype();
		}
		return null;
	}

	public List<IndividualObjectPropertyModel> getObjectPropertiesOfIndividual(String Individual) {
		OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(getEntityIRI(Individual));
		Iterator<OWLClassAssertionAxiom> assertionsIterator = ontology.getClassAssertionAxioms(ind).iterator();
		ArrayList<IndividualObjectPropertyModel> list = new ArrayList<>();
		while (assertionsIterator.hasNext()) {
			OWLClassAssertionAxiom assertionAxiom = assertionsIterator.next();
			OWLObjectProperty property = assertionAxiom.getObjectPropertiesInSignature().iterator().next();
			OWLClass cl = assertionAxiom.getClassesInSignature().iterator().next();
			String propertyName = getShortForm(property.getIRI());
			String className = getShortForm(cl.getIRI());

			IndividualObjectPropertyModel objectProperty = new IndividualObjectPropertyModel(propertyName, className);
			setPropertyDescription(property, objectProperty.getProperty());

			list.add(objectProperty);
		}

		return list;
	}

	public List<IndividualDataPropertyValueModel> getDataPropertiesOfIndividual(String Individual) {
		OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(getEntityIRI(Individual));
		Iterator<OWLDataPropertyAssertionAxiom> assertionsIterator = ontology.getDataPropertyAssertionAxioms(ind).iterator();
		ArrayList<IndividualDataPropertyValueModel> list = new ArrayList<>();

		while (assertionsIterator.hasNext()) {
			OWLDataPropertyAssertionAxiom assertionAxiom = assertionsIterator.next();
			OWLDataProperty property = assertionAxiom.getDataPropertiesInSignature().iterator().next();
			String propertyName = getShortForm(property.getIRI());
			String value = reasoner.getDataPropertyValues(ind, property).iterator().next().getLiteral();

			DataPropertyModel dataProperty = new DataPropertyModel(propertyName);
			dataProperty.setDataType(reasoner.getDataPropertyValues(ind, property).iterator().next().getDatatype().getBuiltInDatatype().name());

			setPropertyDescription(property, dataProperty);

			list.add(new IndividualDataPropertyValueModel(dataProperty, value));
		}

		return list;
	}
	public List<IndividualModel> getListOfIndividuals() {
		List<IndividualModel> list = new ArrayList<>();
		ontology.getIndividualsInSignature().forEach(individual -> list.add(new IndividualModel(individual.getIRI().toString(), getShortForm(individual.getIRI()))));
		return list;
	}

//	public List<String> listOfInstancesForSpecifiedData(List<String> ObjectPropertiesList, List<String> DataPropertiesList) {
//		OWLClass temp = dataFactory.getOWLClass(ontologyIRI + "#" + "temp");
//		Set<OWLClassExpression> arguments = new HashSet<>();
//		//arguments.add(temp);
//
//		for (String str : ObjectPropertiesList) {
//			String[] splitStr = str.trim().split("\\s+");//0 - property, 1 - class
//
//			OWLObjectProperty property = dataFactory.getOWLObjectProperty(ontologyIRI + "#" + splitStr[0]);
//			OWLClass cl = dataFactory.getOWLClass(ontologyIRI + "#" + splitStr[1]);
//
//			arguments.add(dataFactory.getOWLObjectAllValuesFrom(property, cl));
//
//		}
//
//		for (String str : DataPropertiesList) {
//			String[] splitStr = str.trim().split("\\s+");//0 - property, 1 - restriction, 2 - value
//
//			OWLDataProperty property = dataFactory.getOWLDataProperty(getEntityIRI(splitStr[0]));
//			OWLDatatype dataType = getOWLDatatype(splitStr[0]);
//			OWLLiteral value = dataFactory.getOWLLiteral(splitStr[2], dataType);
//			Set<OWLFacetRestriction> facetRestriction = new HashSet<>();
//			switch (splitStr[1]) {
//				case ">":
//					facetRestriction.add(dataFactory.getOWLFacetRestriction(OWLFacet.MIN_EXCLUSIVE, value));
//					break;
//				case "<":
//					facetRestriction.add(dataFactory.getOWLFacetRestriction(OWLFacet.MAX_EXCLUSIVE, value));
//					break;
//				case ">=":
//					facetRestriction.add(dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, value));
//					break;
//				case "<=":
//					facetRestriction.add(dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, value));
//					break;
//				case "=":
//					facetRestriction.add(dataFactory.getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE, value));
//					facetRestriction.add(dataFactory.getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE, value));
//					break;
//			}
//
//			OWLDatatypeRestriction restriction = dataFactory.getOWLDatatypeRestriction(dataType,
//					facetRestriction);
//
//			arguments.add(dataFactory.getOWLDataSomeValuesFrom(property, restriction));
//
//		}
//		OWLClassExpression expr = dataFactory.getOWLObjectIntersectionOf(arguments);
//		OWLAxiom tempAxiom = dataFactory.getOWLEquivalentClassesAxiom(temp, expr);
//
//		ontologyManager.addAxiom(ontology, tempAxiom);
//
//		ArrayList<String> list = new ArrayList<>();
//
//		reasoner.instances(temp, true).forEach(individual -> list.add(getShortForm(individual.getIRI())));
//
//		ontology.removeAxiom(tempAxiom);
//
//		return list;
//	}

	public IRI getClassIRI(String className) throws OWLClassNotFoundException {
		IRI classIRI = getEntityIRI(className);
		if (!ontology.containsClassInSignature(classIRI)) {
			throw new OWLClassNotFoundException(className);
		}
		return classIRI;

	}

	public void addNewClass(ClassPostModel newClass) throws OWLOntologyStorageException {

		IRI classIRI = getEntityIRI(newClass.getName());
		if (ontology.containsClassInSignature(classIRI)) {
			throw new OWLClassAlreadyExists(newClass.getName());
		}

		OWLClass newClassOWL = dataFactory.getOWLClass(classIRI);
		OWLClass parent;
		if (newClass.getParentName() != null) {
			parent = dataFactory.getOWLClass(getClassIRI(newClass.getParentName()));
		} else {
			parent = dataFactory.getOWLThing();
		}
		OWLSubClassOfAxiom axiom = dataFactory.getOWLSubClassOfAxiom(newClassOWL, parent);

		ontologyManager.addAxiom(ontology, axiom);

		saveOntology();
	}

	public void addNewObjectProperty(ObjectPropertyModel newObjectProperty) throws OWLOntologyStorageException {
		OWLObjectProperty property = getOWLObjectProperty(newObjectProperty.getName().replaceAll("\\s", ""));
		Set<OWLAxiom> axioms = new HashSet<>();

		setObjectPropertyParent(newObjectProperty, property, axioms);

		setComment(newObjectProperty, property, axioms);

		setObjectPropertyRanges(newObjectProperty, property, axioms);

		ontologyManager.addAxioms(ontology, axioms);

		saveOntology();
	}

	private void setObjectPropertyRanges(ObjectPropertyModel newObjectProperty, OWLObjectProperty property, Set<OWLAxiom> axioms) {
		OWLClass rangeClass = dataFactory.getOWLClass(getClassIRI(newObjectProperty.getRange()));
		OWLAxiom axiom = dataFactory.getOWLObjectPropertyRangeAxiom(property, rangeClass);
		axioms.add(axiom);
	}

	private void setComment(PropertyModel propertyModel, OWLProperty property, Set<OWLAxiom> axioms) {
		if(propertyModel.getDescription() != null) {
			OWLLiteral label = dataFactory.getOWLLiteral(propertyModel.getDescription());
			OWLAnnotationProperty comment = dataFactory.getRDFSComment();

			OWLAnnotation annotation = dataFactory.getOWLAnnotation(comment, label);
			OWLAnnotationAxiom commentAxiom = dataFactory.getOWLAnnotationAssertionAxiom(property.getIRI(), annotation);
			axioms.add(commentAxiom);
		}
	}

	public OWLObjectProperty getOWLObjectProperty(String propertyName) {
		return dataFactory.getOWLObjectProperty(getEntityIRI(propertyName));
	}

	private void setObjectPropertyParent(ObjectPropertyModel newObjectProperty, OWLObjectProperty property, Set<OWLAxiom> axioms) {
		OWLObjectProperty parent;
		if (newObjectProperty.getParentName() != null) {
			parent = getOWLObjectProperty(newObjectProperty.getParentName());
		} else {
			parent = dataFactory.getOWLTopObjectProperty();
		}
		OWLAxiom axiom = dataFactory.getOWLSubObjectPropertyOfAxiom(property, parent);
		axioms.add(axiom);
	}

	private void setDataPropertyParent(DataPropertyModel newDataProperty, OWLDataProperty property, Set<OWLAxiom> axioms) {
		OWLDataProperty parent;
		if (newDataProperty.getParentName() != null) {
			parent = dataFactory.getOWLDataProperty(getEntityIRI(newDataProperty.getParentName()));
		} else {
			parent = dataFactory.getOWLTopDataProperty();
		}
		OWLAxiom axiom = dataFactory.getOWLSubDataPropertyOfAxiom(property, parent);
		axioms.add(axiom);
	}

	public void addNewDataProperty(DataPropertyModel newDataProperty) throws OWLOntologyStorageException {
		OWLDataProperty property = dataFactory.getOWLDataProperty(getEntityIRI(newDataProperty.getName()));
		OWL2Datatype dataType = OWL2Datatype.valueOf(newDataProperty.getDataType());
		Set<OWLAxiom> axioms = new HashSet<>();

		OWLAxiom axiom = dataFactory.getOWLDataPropertyRangeAxiom(property, dataType.getDatatype(dataFactory));
		axioms.add(axiom);

		setDataPropertyParent(newDataProperty, property, axioms);

		setComment(newDataProperty, property, axioms);

		ontologyManager.addAxioms(ontology, axioms);

		saveOntology();
	}

	public void addNewIndividual(IndividualModel individual) throws OWLOntologyStorageException, OWLClassNotFoundException {
		OWLIndividual newIndividual = dataFactory.getOWLNamedIndividual(getEntityIRI(individual.getName()));
		Set<OWLAxiom> axioms = new HashSet<>();

		for (IndividualObjectPropertyModel obj : individual.getObjectProperties()) {
			OWLObjectProperty property = dataFactory.getOWLObjectProperty(getEntityIRI(obj.getProperty().getName()));
			OWLClass cl = dataFactory.getOWLClass(getClassIRI(obj.getValue()));
			OWLClassExpression classExpression = dataFactory.getOWLObjectAllValuesFrom(property, cl);
			OWLClassAssertionAxiom assertionAxiom = dataFactory.getOWLClassAssertionAxiom(classExpression, newIndividual);
			axioms.add(assertionAxiom);
		}

		for (IndividualDataPropertyValueModel data : individual.getDataProperties()) {
			OWLDataProperty property = dataFactory.getOWLDataProperty(getEntityIRI(data.getProperty().getName()));
			OWLDatatype dataType = getOWLDatatype(property.getIRI().getShortForm());
			OWLLiteral value = dataFactory.getOWLLiteral(data.getValue(), dataType);
			OWLDataPropertyAssertionAxiom assertionAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(property, newIndividual, value);
			axioms.add(assertionAxiom);
		}

		ontologyManager.addAxioms(ontology, axioms);

		saveOntology();

	}

	public List<String> listOfInstancesOfClass(OWLClass cl) {
		ArrayList<String> list = new ArrayList<>();
		reasoner.getInstances(cl, true).forEach(individual -> list.add(getShortForm(individual.getRepresentativeElement().getIRI())));
		return list;
	}

	public boolean isClassSetSatisfiable(List<String> listOfClass) {
		OWLClass temp = dataFactory.getOWLClass(getEntityIRI("temp"));
		Set<OWLAxiom> axioms = new HashSet<>();
		for (String str : listOfClass) {
			OWLClass cl = dataFactory.getOWLClass(getEntityIRI(str));
			OWLSubClassOfAxiom subClassOfAxiom = dataFactory.getOWLSubClassOfAxiom(temp, cl);
			axioms.add(subClassOfAxiom);
		}

		ontologyManager.addAxioms(ontology, axioms);
		boolean satisfable = false;

		if (reasoner.isSatisfiable(temp))
			satisfable = true;


        HashSet<OWLOntology> toBeRemoved = new HashSet<OWLOntology>(1);
        toBeRemoved.add(ontology);
		OWLEntityRemover remover = new OWLEntityRemover(toBeRemoved);
		temp.accept(remover);
		remover.getChanges().forEach(removeAxiom -> ontologyManager.applyChange(removeAxiom));
		remover.reset();

		return satisfable;
	}

	public boolean hasDataPropertyNumericRange(String property) {
		OWLDatatype data = getOWLDatatype(property);
		return (data.isInteger() || data.isFloat() || data.isBoolean());
	}

	private OWLDatatype getOWLDatatype(String dataProperty) {
		OWLDataProperty property = dataFactory.getOWLDataProperty(getEntityIRI(dataProperty));
		Set<OWLDatatype> dataTypes = new HashSet<>();
		//long line
		ontology.getDataPropertyRangeAxioms(property).forEach(owlDataPropertyRangeAxiom -> owlDataPropertyRangeAxiom.getRange().getDatatypesInSignature().forEach(dataTypes::add));
		OWLDatatype out = null;
		for (OWLDatatype data : dataTypes) {
			out = data;
		}
		return out;
	}

	private String getShortForm(IRI objectIRI)  //original getShortForm() is removing numbers
	{
		String str = objectIRI.toString();
		return str.replaceAll(ontologyIRI.toString() + "#", "");
	}

	private IRI getEntityIRI(String entity) {
		return IRI.create(ontologyIRI + "#" + entity);
	}

	public boolean containsEntity(String entity) {
		return ontology != null && ontology.containsEntityInSignature(getEntityIRI(entity));
	}

	public void deleteIndividual(String entity) throws OWLOntologyStorageException {

        HashSet<OWLOntology> toBeRemoved = new HashSet<>(1);
        toBeRemoved.add(ontology);

		OWLEntityRemover remover = new OWLEntityRemover(toBeRemoved);
		OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(getEntityIRI(entity));
		ind.accept(remover);
		deleteEntity(remover);
	}

	public void deleteClass(String entity) throws OWLOntologyStorageException {
        HashSet<OWLOntology> toBeRemoved = new HashSet<>(1);
        toBeRemoved.add(ontology);

		OWLEntityRemover remover = new OWLEntityRemover(toBeRemoved);
		OWLClass owlClass = dataFactory.getOWLClass(getClassIRI(entity));
		owlClass.accept(remover);
		deleteEntity(remover);
	}

	public void deleteObjectProperty(String id) throws OWLOntologyStorageException {
		OWLObjectProperty owlProperty = getOWLObjectProperty(id);
		deleteProperty(owlProperty);
	}

	public void deleteDataProperty(String id) throws OWLOntologyStorageException {
		OWLDataProperty owlProperty = dataFactory.getOWLDataProperty(getEntityIRI(id));
		deleteProperty(owlProperty);
	}

	public void deleteProperty(OWLProperty entity) throws OWLOntologyStorageException {
        HashSet<OWLOntology> toBeRemoved = new HashSet<>(1);
        toBeRemoved.add(ontology);

        OWLEntityRemover remover = new OWLEntityRemover(toBeRemoved);
		entity.accept(remover);
		deleteEntity(remover);
	}

	private void deleteEntity(OWLEntityRemover remover) throws OWLOntologyStorageException {
		remover.getChanges().forEach(removeAxiom -> ontologyManager.applyChange(removeAxiom));
		remover.reset();

		saveOntology();
	}

	public OWLOntology getOntology() {
		return ontology;
	}

	public long getDataPropertiesCount() {
		return reasoner.getSubDataProperties(dataFactory.getOWLTopDataProperty(), false).getFlattened().size();
	}

	public long getObjectPropertiesCount() {
		return reasoner.getSubObjectProperties(dataFactory.getOWLTopObjectProperty(), false).getFlattened().size();
	}

	public long getIndividualsCount() {
		return ontology.getIndividualsInSignature().size();
	}

	public long getClassCount() {
		return reasoner.getSubClasses(dataFactory.getOWLThing(), false).getFlattened().size();
	}

	public DataPropertyModel getDataProperty(String propertyName) {

		OWLDataProperty dataPropertyOWL = dataFactory.getOWLDataProperty(getEntityIRI(propertyName));
		DataPropertyModel dataProperty = new DataPropertyModel(propertyName, getDataType(dataPropertyOWL));

		setPropertyDescription(dataPropertyOWL, dataProperty);

		return dataProperty;
	}

    public ObjectPropertyModel getObjectProperty(String propertyName) {

		OWLObjectProperty objectPropertyOWL = dataFactory.getOWLObjectProperty(getEntityIRI(propertyName));
		ObjectPropertyModel objectProperty = new ObjectPropertyModel(propertyName);

		setPropertyDescription(objectPropertyOWL, objectProperty);
        setObjectPropertyRanges(objectPropertyOWL, objectProperty);

		return objectProperty;
    }

    private SWRLRuleEngine getSWRLEngine() {
	    if (swrlRuleEngine == null) {
	        swrlRuleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
        }
        return swrlRuleEngine;
    }

    public void addNewSWRLRule(SWRLRuleModel rule) throws SWRLBuiltInException, SWRLParseException, OWLOntologyStorageException {
        getSWRLEngine().createSWRLRule(rule.getName(), rule.getRule(), rule.getComment(), true);
        getSWRLEngine().infer();
		saveOntology();
    }

    public List<SWRLRuleModel> getSWRLRules() {
	    List<SWRLRuleModel> rules = new ArrayList<>();
        getSWRLEngine().infer();
		SWRLRuleRenderer swrlRuleRenderer = getSWRLEngine().createSWRLRuleRenderer();
	    getSWRLEngine().getSWRLRules().forEach(swrlapiRule -> rules.add(
	    		new SWRLRuleModel(swrlapiRule.getRuleName(),
						swrlapiRule.getComment(),
						swrlRuleRenderer.renderSWRLRule(swrlapiRule))
		));
	    return rules;
    }

    public void deleteSWRLRule(String ruleName) throws OWLOntologyStorageException {
	    getSWRLEngine().deleteSWRLRule(ruleName);
        getSWRLEngine().infer();
	    saveOntology();
    }
}