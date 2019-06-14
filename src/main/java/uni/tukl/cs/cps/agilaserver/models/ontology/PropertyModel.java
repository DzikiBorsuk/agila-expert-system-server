package uni.tukl.cs.cps.agilaserver.models.ontology;

import java.util.List;

public abstract class PropertyModel {

	protected String name;

	protected String description;

	private String parentName;

	private List<? extends PropertyModel> subProperties;

	//FIXME: find another way to return a valid Id, as hashCode can have many collisions
	public int getId() {
		return this.name.hashCode();
	}

	public String getName() { return name; }

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) { this.parentName = parentName; }

	public List<? extends PropertyModel> getSubProperties() {
		return subProperties;
	}

	public void setSubProperties(List<? extends PropertyModel> subProperties) {
		this.subProperties = subProperties;
	}
}
