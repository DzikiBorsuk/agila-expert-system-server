package uni.tukl.cs.cps.agilaserver.models.ontology;

public class ObjectPropertyModel extends PropertyModel {

	private String range;

	public ObjectPropertyModel() {
	}

	public ObjectPropertyModel(String name) {
		super();
		this.name = name;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
}
