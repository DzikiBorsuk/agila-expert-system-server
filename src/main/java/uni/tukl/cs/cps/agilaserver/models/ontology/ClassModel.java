package uni.tukl.cs.cps.agilaserver.models.ontology;

import java.util.List;

public class ClassModel {

	private String name;

	private List<ObjectPropertyModel> objectProperties;

	public ClassModel(String name) {
		this.name = name;
	}

	public ClassModel(String name, List<ObjectPropertyModel> objectProperties) {
		this.name = name;
		this.objectProperties = objectProperties;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ObjectPropertyModel> getObjectProperties() {
		return objectProperties;
	}

	public void setObjectProperties(List<ObjectPropertyModel> objectProperties) {
		this.objectProperties = objectProperties;
	}
}
