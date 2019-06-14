package uni.tukl.cs.cps.agilaserver.models.individual;

import java.util.List;

public class IndividualModel {

	private String name;

	private String id;

	private List<IndividualDataPropertyValueModel> dataProperties;

	private List<IndividualObjectPropertyModel> objectProperties;

	public IndividualModel() {

	}

	public IndividualModel(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IndividualDataPropertyValueModel> getDataProperties() {
		return dataProperties;
	}

	public void setDataProperties(List<IndividualDataPropertyValueModel> dataProperties) {
		this.dataProperties = dataProperties;
	}

	public List<IndividualObjectPropertyModel> getObjectProperties() {
		return objectProperties;
	}

	public void setObjectProperties(List<IndividualObjectPropertyModel> objectProperties) {
		this.objectProperties = objectProperties;
	}
}
