
package uni.tukl.cs.cps.agilaserver.models.individual;

import uni.tukl.cs.cps.agilaserver.models.ontology.ObjectPropertyModel;

public class IndividualObjectPropertyModel {

    private ObjectPropertyModel property;

	private String value;

	public IndividualObjectPropertyModel(ObjectPropertyModel property, String value) {
		this.property = property;
		this.value = value;
	}

    public IndividualObjectPropertyModel(String relationship, String value) {
		this(new ObjectPropertyModel(relationship), value);
    }

    public IndividualObjectPropertyModel() { }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ObjectPropertyModel getProperty() {
		return property;
	}

	public void setProperty(ObjectPropertyModel property) {
		this.property = property;
	}
}